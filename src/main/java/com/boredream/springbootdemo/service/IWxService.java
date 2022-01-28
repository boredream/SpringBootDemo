package com.boredream.springbootdemo.service;

import com.boredream.springbootdemo.entity.dto.WxLoginDTO;
import com.boredream.springbootdemo.entity.dto.WxSessionDTO;

/**
 * <p>
 * 消息合规性检测 服务类
 * </p>
 *
 * @author boredream
 * @since 2022-01-28
 */
public interface IWxService {

    /**
     * 场景枚举值 资料
     */
    int SCENE_INFO = 1;

    /**
     * 场景枚举值 评论
     */
    int SCENE_COMMENT = 2;

    /**
     * 场景枚举值 论坛
     */
    int SCENE_BBS = 3;

    /**
     * 场景枚举值 社交日志
     */
    int SCENE_SOCIAL = 4;

    /**
     * 登录凭证校验
     */
    WxSessionDTO wxLogin(WxLoginDTO dto);

    /**
     * 接口调用凭证
     */
    String getAccessToken();

    /**
     * 消息合规性检测
     *
     * @param platform 平台，暂时只有wx需要检测
     * @param curUserId 当前用户id
     * @param scene 场景枚举值（1 资料；2 评论；3 论坛；4 社交日志）
     * @param content 内容
     */
    boolean checkMsgSec(String platform, Long curUserId, int scene, String content);

}
