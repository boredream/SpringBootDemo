package com.boredream.springbootdemo.controller;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boredream.springbootdemo.entity.Case;
import com.boredream.springbootdemo.entity.Visitor;
import com.boredream.springbootdemo.entity.dto.CaseQueryDTO;
import com.boredream.springbootdemo.entity.dto.CreateCaseWithVisitorDTO;
import com.boredream.springbootdemo.entity.dto.PageResultDTO;
import com.boredream.springbootdemo.entity.dto.ResponseDTO;
import com.boredream.springbootdemo.service.IAiService;
import com.boredream.springbootdemo.service.ICaseService;
import com.boredream.springbootdemo.service.IVisitorService;
import com.boredream.springbootdemo.utils.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 案例 前端控制器
 * </p>
 *
 * @author boredream
 * @since 2024-09-23
 */
@RestController
@RequestMapping("/case")
@Api(tags = {"案例"})
public class CaseController {

    @Autowired
    private ICaseService service;

    @Autowired
    private IVisitorService visitorService;

    @Autowired
    private IAiService aiService;

    @ApiOperation(value = "新增案例预检")
    @GetMapping("/checkCreate")
    public ResponseDTO<Boolean> check(Long curUserId) {
        // 校验最大数量，和正在解析中文件情况，防止重复上传文件浪费资源
        QueryWrapper<Case> wrapper = new QueryWrapper<Case>()
                .eq("delete_flag", 0)
                .eq("user_id", curUserId);
        long count = service.count(wrapper);
        if (count >= 5) {
            return ResponseDTO.error("案例数量已达上限5条，请联系管理员删除后再试");
        }

        // 如果有正在解析中的案例，也暂时不让上传
        QueryWrapper<Case> caseWrapper = new QueryWrapper<Case>()
                .eq("user_id", curUserId)
                .eq("ai_parse_status", Case.AI_PARSE_STATUS_PARSING);
        if (service.count(caseWrapper) > 0) {
            return ResponseDTO.error("有正在解析中的案例，请稍后再试");
        }

        // 先预检AI服务器接口
        boolean available = aiService.isAvailable();
        if(!available) {
            return ResponseDTO.error("AI 服务器不可用，请稍后再试");
        }

        return ResponseDTO.success(true);
    }

    @Transactional
    @ApiOperation(value = "添加案例并关联访客信息")
    @PostMapping("/createWithVisitor")
    public ResponseDTO<Boolean> add(@RequestBody @Validated CreateCaseWithVisitorDTO body, Long curUserId) {
        try {
            Visitor visitorDto = body.getVisitorDto();
            visitorDto.setUserId(curUserId);
            if (visitorDto.getId() == null || visitorDto.getId() == 0L) {
                // 新访客，创建访客之后再创建案例
                visitorService.save(visitorDto);
            }

            // 设置案例索引
            Long visitorId = visitorDto.getId();
            int type = body.getCaseDto().getType();

            // 查询同一visitor_id和type的最大case_index
            QueryWrapper<Case> wrapper = new QueryWrapper<>();
            wrapper.eq("visitor_id", visitorId)
                    .eq("type", type)
                    .orderByDesc("case_index");
            List<Case> list = service.list();
            int maxIndex = 0;
            if(!CollectionUtil.isEmpty(list)) {
                maxIndex = list.get(0).getCaseIndex();
            }

            Case caseDto = body.getCaseDto();
            caseDto.setCaseIndex(maxIndex + 1);
            caseDto.setAiParseStatus(Case.AI_PARSE_STATUS_PARSING);
            caseDto.setUserId(curUserId);
            caseDto.setVisitorId(visitorDto.getId());

            service.save(caseDto);
            caseDto.setVisitor(visitorDto);

            // 异步调用 AI 解析
            aiService.parseAIContent(caseDto);

            return ResponseDTO.success(true);
        } catch (Exception e) {
            return ResponseDTO.error(e.getMessage());
        }
    }

    @ApiOperation(value = "案例发起AI解析")
    @PostMapping("/parseAI")
    public ResponseDTO<Boolean> parseAI(@RequestBody @Validated Case body, Long curUserId) {
        body.setAiParseStatus(Case.AI_PARSE_STATUS_PARSING);
        service.updateById(body);

        // 异步调用 AI 解析
        aiService.parseAIContent(body);
        return ResponseDTO.success(true);
    }

    @ApiOperation(value = "查询解析中的案例")
    @GetMapping("/getParsingCase")
    public ResponseDTO<Case> queryParsingCase(Long curUserId) {
        QueryWrapper<Case> wrapper = new QueryWrapper<Case>()
                .eq("user_id", curUserId)
                .eq("ai_parse_status", Case.AI_PARSE_STATUS_PARSING);

        List<Case> list = service.list(wrapper);
        return ResponseDTO.success(CollectionUtil.isEmpty(list) ? null : list.get(0));
    }

    @ApiOperation(value = "分页查询案例")
    @GetMapping("/page")
    public ResponseDTO<PageResultDTO<Case>> queryByPage(CaseQueryDTO dto, Long curUserId) {
        QueryWrapper<Case> wrapper = new QueryWrapper<Case>()
                .eq("delete_flag", 0)
                .eq("user_id", curUserId)
                .orderByDesc("create_time");

        Page<Case> page = PageUtil.convert2QueryPage(dto);
        Page<Case> resultDto = service.page(page, wrapper);
        if (resultDto.getRecords() != null) {
            // TODO 数据库查询更好？
            resultDto.getRecords().forEach(item -> {
                QueryWrapper<Visitor> visitorWrapper = new QueryWrapper<Visitor>()
                        .eq("id", item.getVisitorId());
                item.setVisitor(visitorService.getOne(visitorWrapper));
            });
        }
        return ResponseDTO.success(PageUtil.convert2PageResult(resultDto));
    }

    @ApiOperation(value = "添加案例")
    @PostMapping
    public ResponseDTO<Boolean> add(@RequestBody @Validated Case body, Long curUserId) {
        body.setUserId(curUserId);
        return ResponseDTO.success(service.save(body));
    }

    @ApiOperation(value = "修改案例")
    @PutMapping("/{id}")
    public ResponseDTO<Boolean> update(@PathVariable("id") Long id, @RequestBody @Validated Case body) {
        body.setId(id);
        return ResponseDTO.success(service.updateById(body));
    }

    @ApiOperation(value = "删除案例")
    @DeleteMapping("/{id}")
    public ResponseDTO<Boolean> delete(@PathVariable("id") Long id) {
        return ResponseDTO.success(service.removeById(id));
    }

}
