package com.boredream.springbootdemo.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boredream.springbootdemo.entity.TalkCaseDetail;
import com.boredream.springbootdemo.entity.dto.ResponseDTO;
import com.boredream.springbootdemo.entity.dto.TalkCaseDetailQueryDTO;
import com.boredream.springbootdemo.service.ITalkCaseDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 案例详情 前端控制器
 * </p>
 *
 * @author boredream
 * @since 2024-09-24
 */
@RestController
@RequestMapping("/talk_case_detail")
@Api(tags = {"案例详情"})
public class TalkCaseDetailController {

    @Autowired
    private ITalkCaseDetailService service;

    @ApiOperation(value = "查询案例详情")
    @GetMapping("/type")
    public ResponseDTO<TalkCaseDetail> queryByPage(TalkCaseDetailQueryDTO dto) {
        QueryWrapper<TalkCaseDetail> wrapper = new QueryWrapper<TalkCaseDetail>()
                .eq("case_id", dto.getCaseId())
                .eq("result_type", dto.getResultType());
        return ResponseDTO.success(service.getOne(wrapper));
    }

}
