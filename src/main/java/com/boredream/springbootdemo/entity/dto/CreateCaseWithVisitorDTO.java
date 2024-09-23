package com.boredream.springbootdemo.entity.dto;

import com.boredream.springbootdemo.entity.Case;
import com.boredream.springbootdemo.entity.Visitor;
import lombok.Data;

@Data
public class CreateCaseWithVisitorDTO {

    private int action;
    private Case caseDto;
    private Visitor visitorDto;
}
