package com.boredream.springbootdemo.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boredream.springbootdemo.entity.Case;
import com.boredream.springbootdemo.entity.CaseAiResultResponse;
import com.boredream.springbootdemo.entity.TalkCaseDetail;
import com.boredream.springbootdemo.entity.Visitor;
import com.boredream.springbootdemo.exception.ApiException;
import com.boredream.springbootdemo.mapper.CaseMapper;
import com.boredream.springbootdemo.service.ICaseService;
import com.boredream.springbootdemo.service.ITalkCaseDetailService;
import com.boredream.springbootdemo.service.IVisitorService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 案例 服务实现类
 * </p>
 *
 * @author boredream
 * @since 2024-09-23
 */
@Service
public class CaseServiceImpl extends ServiceImpl<CaseMapper, Case> implements ICaseService {

    // TODO 这种写法不好？

    @Autowired
    private ITalkCaseDetailService caseDetailService;

    @Autowired
    private IVisitorService visitorService;

    @Transactional
    @Override
    public void saveAiResult(String aiResult, Case talkCase) {

        // 解析结果
        CaseAiResultResponse response = new Gson().fromJson(aiResult, CaseAiResultResponse.class);

        List<HashMap<String, String>> label = response.getLabel();
        if (CollectionUtils.isEmpty(label)) {
            throw new ApiException("AI 服务器返回 label 结果为空");
        }

        // 分 result 1/2/3/4/5... 挨个保存数据
        for (Map.Entry<String, String> entry : label.get(0).entrySet()) {
            TalkCaseDetail detail = new TalkCaseDetail();
            detail.setCaseId(talkCase.getId());
            detail.setResultType(entry.getKey());
            detail.setAiResult(entry.getValue());
            caseDetailService.save(detail);

            // 更新访客数据
            if (talkCase.getType() != null && talkCase.getType() == 1) {
                // 评估
                if ("result_4".equals(entry.getKey())) {
                    // result_4是个人信息
                    visitorService.updateField(talkCase.getVisitor(), entry.getValue(), false);
                }
            } else if (talkCase.getType() != null && talkCase.getType() == 2) {
                // 咨询 TODO
                if ("result_3".equals(entry.getKey())) {
                    // result_4是个案评估数据，只要有值就覆盖操作
                    Visitor visitor = talkCase.getVisitor();
                    String json = entry.getValue().replace("'", "\"");
                    JsonObject jo = new Gson().fromJson(json, JsonObject.class);
                    if (jo.has("suicide")) {
                        String suicide = jo.get("suicide").getAsString();
                        if (!StringUtils.isEmpty(suicide)) {
                            visitor.setSuicide(suicide);
                        }
                    }
                    if (jo.has("risk")) {
                        int risk = jo.get("risk").getAsInt();
                        visitor.setRisk(risk);
                    }
                    visitorService.updateById(visitor);
                }
            }
        }

        // 最后更新 case 的 AI解析状态
        talkCase.setAiParseStatus(Case.AI_PARSE_STATUS_SUCCESS);
        updateById(talkCase);
    }
}
