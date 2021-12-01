package com.boredream.springbootdemo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author boredream
 * @since 2021-10-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="User对象", description="用户")
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "角色")
    private String role;

    @ApiModelProperty(value = "第三方id")
    private String openId;

    @ApiModelProperty(value = "伴侣用户id")
    private Long cpUserId;

    @ApiModelProperty(value = "伴侣在一起时间")
    private String cpTogetherDate;

    @TableField(exist = false)
    @ApiModelProperty(value = "伴侣用户")
    private User cpUser;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "性别")
    private String gender;

    @ApiModelProperty(value = "生日")
    private String birthday;

}
