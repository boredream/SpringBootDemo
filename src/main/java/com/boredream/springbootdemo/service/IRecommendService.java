package com.boredream.springbootdemo.service;

/**
 * <p>
 * 推荐 服务类
 * </p>
 *
 * @author boredream
 * @since 2021-12-07
 */
public interface IRecommendService {

    /**
     * 生成推荐内容
     */
    void genRecommendData(Long userId);

    /**
     * 合并推荐内容，隐藏其中一方的推荐数据
     */
    void mergeRecommendData(Long userId, Long cpUserId);

    /**
     * 拆分推荐内容，恢复双方的隐藏推荐
     */
    void splitRecommendData(Long userId, Long cpUserId);

}
