package com.boredream.springbootdemo.service.impl;

import com.boredream.springbootdemo.entity.Case;
import com.boredream.springbootdemo.entity.CaseAiResultResponse;
import com.boredream.springbootdemo.entity.TalkCaseDetail;
import com.boredream.springbootdemo.exception.ApiException;
import com.boredream.springbootdemo.mapper.CaseMapper;
import com.boredream.springbootdemo.service.ICaseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boredream.springbootdemo.service.ITalkCaseDetailService;
import com.google.gson.Gson;
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
    private ITalkCaseDetailService caseDtailService;

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
            caseDtailService.save(detail);
        }

        // 最后更新 case 的 AI解析状态
        talkCase.setAiParseStatus(Case.AI_PARSE_STATUS_SUCCESS);
        updateById(talkCase);
    }
}
