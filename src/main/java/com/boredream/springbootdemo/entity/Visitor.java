package com.boredream.springbootdemo.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author boredream
 * @since 2024-09-23
 */
@Getter
@Setter
@ApiModel(value = "Visitor对象", description = "用户")
public class Visitor extends BaseEntity {

    @ApiModelProperty("所属用户")
    private Long userId;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("印象")
    private String impression;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("性别")
    private String gender;

    @ApiModelProperty("国籍")
    private String nationality;

    @ApiModelProperty("出生地")
    private String placeOfBirth;

    @ApiModelProperty("现居地")
    private String address;

    @ApiModelProperty("受教育程度")
    private String educationLevel;

    @ApiModelProperty("现职业/专业")
    private String currentOccupation;

    @ApiModelProperty("宗教信仰")
    private String religion;

    @ApiModelProperty("婚姻状况")
    private String marriage;

    @ApiModelProperty("初次来访时间")
    private Long firstContactTime;

    @ApiModelProperty("联系方式")
    private String number;

    @ApiModelProperty("家庭成员构成")
    private String family;

    @ApiModelProperty("家庭成员人格特点")
    private String personality;

    @ApiModelProperty("家庭成员与来访的关系")
    private String relationship;

    @ApiModelProperty("父母关系")
    private String parent;

    @ApiModelProperty("重大生活事件")
    private String events;

    @ApiModelProperty("既往创伤")
    private String trauma;

    @ApiModelProperty("自我评价")
    private String selfRated;

    @ApiModelProperty("同伴关系")
    private String peer;

    @ApiModelProperty("亲密关系")
    private String intimat;

    @ApiModelProperty("症状主诉")
    private String symptoms;

    @ApiModelProperty("现病史")
    private String present;

    @ApiModelProperty("既往病史")
    private String past;

    @ApiModelProperty("上瘾史")
    private String addiction;

}
