package com.boredream.springbootdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boredream.springbootdemo.entity.Case;
import com.boredream.springbootdemo.entity.CaseAiResultResponse;
import com.boredream.springbootdemo.service.IAiService;
import com.boredream.springbootdemo.service.ICaseService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AiServiceImpl implements IAiService {

    @Autowired
    private ICaseService caseService;

    @Autowired
    private RestTemplate restTemplate;

    @Async
    @Override
    @Transactional
    public void parseAIContent(Case talkCase) {
        // 调用 AI 服务器进行解析
        String aiResult = sendRequestToAiServer(talkCase);
        // TODO 异常处理更新状态
        if (aiResult != null) {
            // 返回数据后，保存数据并更新案例的AI解析状态
            CaseAiResultResponse response = new Gson().fromJson(aiResult, CaseAiResultResponse.class);

            QueryWrapper<Case> wrapper = new QueryWrapper<>();
            wrapper.eq("id", talkCase.getId());
            caseService.update(talkCase, wrapper);
        }
    }

    private String sendRequestToAiServer(Case talkCase) {
        // 实现对 AI 服务器的请求
        System.out.println("case[" + talkCase.getId() + "] sendRequestToAiServer");
        String url = "http://121.41.57.69:80/test";

        Map<String, Object> params = new HashMap<>();
        params.put("file_type", 0);  // 0为文档，1为音频
        params.put("doc_url", talkCase.getFileUrl()); // 文档链接
        params.put("counsel", talkCase.getType() == 2 ? 1 : 0);  // 0不是咨询稿件(评估)，1为咨询稿件

        long startTime = System.currentTimeMillis();
        ResponseEntity<String> response = restTemplate.postForEntity(url, params, String.class);
        System.out.println("case[" + talkCase.getId() + "] Time taken: " + (System.currentTimeMillis() - startTime) + " ms");

        return response.getBody();  // 返回解析结果
    }
}