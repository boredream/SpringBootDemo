package com.boredream.springbootdemo.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.boredream.springbootdemo.entity.Case;
import com.boredream.springbootdemo.entity.CaseAiResultResponse;
import com.boredream.springbootdemo.entity.TalkCaseDetail;
import com.boredream.springbootdemo.exception.ApiException;
import com.boredream.springbootdemo.service.IAiService;
import com.boredream.springbootdemo.service.ICaseService;
import com.boredream.springbootdemo.service.ITalkCaseDetailService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AiServiceImpl implements IAiService {

    @Autowired
    private ICaseService caseService;

    @Autowired
    private ITalkCaseDetailService caseDetailService;

    @Autowired
    private RestTemplate restTemplate;

    @Async
    @Override
    @Transactional
    public void parseAIContent(Case talkCase) {
        try {
            if (StringUtils.isEmpty(talkCase.getFileUrl())) {
                throw new ApiException("案例缺失文件地址");
            }

            if (talkCase.getId() == null) {
                // 需要先创建案例成功后，才发起AI解析
                throw new ApiException("案例创建失败");
            }

            // 调用 AI 服务器进行解析
            String aiResult = sendRequestToAiServer(talkCase);
            if (StringUtils.isEmpty(aiResult)) {
                throw new ApiException("AI 服务器返回结果为空");
            }

            try {
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
                }

                // 最后更新 case 的 AI解析状态
                talkCase.setAiParseStatus(Case.AI_PARSE_STATUS_SUCCESS);
                caseService.updateById(talkCase);
            } catch (Exception e) {
                throw new ApiException("AI 服务器返回结果解析失败");
            }
        } catch (Exception e) {
            // AI解析失败，更新case状态
            talkCase.setAiParseStatus(Case.AI_PARSE_STATUS_FAIL);
            caseService.updateById(talkCase);
            log.error(e.getMessage());
            throw new ApiException(e.getMessage());
        }
    }

    private String sendRequestToAiServer(Case talkCase) {
        // 实现对 AI 服务器的请求
        System.out.println("case[" + talkCase.getId() + "] sendRequestToAiServer");
        String url = "http://121.41.57.69:80/test";

        // 文本格式：.txt、.doc .docx、.pdf、.rtf、.md
        // 音频格式：.mp3、.wav .aac、.flac、.ogg、.wma、.m4a
        int file_type = 0;
        String fileUrl = talkCase.getFileUrl();
        if (Arrays.asList(".mp3", ".wav", ".aac", ".flac", ".ogg", ".wma", ".m4a")
                .contains(fileUrl.substring(fileUrl.lastIndexOf('.')))) {
            file_type = 1;
        }

        // TODO 异常没有更新案例错误状态
        // TODO 假如超时、轮询机制检查案例ai解析状态

        Map<String, Object> params = new HashMap<>();
        params.put("file_type", file_type);  // 0为文档，1为音频
        params.put("doc_url", fileUrl); // 文档链接
        params.put("counsel", talkCase.getType() == 2 ? 1 : 0);  // 0不是咨询稿件(评估)，1为咨询稿件

        long startTime = System.currentTimeMillis();
        ResponseEntity<String> response = restTemplate.postForEntity(url, params, String.class);
        System.out.println("case[" + talkCase.getId() + "] Time taken: " + (System.currentTimeMillis() - startTime) + " ms");

        return response.getBody();  // 返回解析结果
    }
}