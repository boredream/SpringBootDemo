package com.boredream.springbootdemo.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Data
public class WxMsgSecCheckResult extends BaseWxResponse {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(WxMsgSecCheckResult.class);

    @ApiModelProperty(value = "唯一请求标识，标记单次请求")
    private String trace_id;

    @ApiModelProperty(value = "综合结果")
    private CheckResult result;

    @ApiModelProperty(value = "综合结果")
    private List<CheckResultDetail> detail;

    /**
     * 合规
     */
    public boolean isPass() {
        return result != null && "pass".equals(result.getSuggest());
    }

    @Data
    public static class CheckResult {

        @ApiModelProperty(value = "建议，有risky、pass、review三种值")
        private String suggest;

        @ApiModelProperty(value = "命中标签枚举值，100 正常；10001 广告；20001 时政；20002 色情；20003 辱骂；20006 违法犯罪；20008 欺诈；20012 低俗；20013 版权；21000 其他")
        private String label;
    }

    @Data
    public static class CheckResultDetail extends CheckResult {

        @ApiModelProperty(value = "策略类型")
        private String strategy;

        @ApiModelProperty(value = "错误码，仅当该值为0时，该项结果有效")
        private int errcode;

        @ApiModelProperty(value = "0-100，代表置信度，越高代表越有可能属于当前返回的标签（label）")
        private int prob;

        @ApiModelProperty(value = "命中的自定义关键词")
        private String keyword;
    }

}