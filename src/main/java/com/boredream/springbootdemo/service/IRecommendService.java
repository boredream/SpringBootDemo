package com.boredream.springbootdemo.service;

import com.boredream.springbootdemo.entity.RecommendDataEntity;

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
     * 用户提交之后视为自己的数据，清楚推荐信息
     */
    void unbindRecommendData(RecommendDataEntity data);

    /**
     * 拆分推荐内容，恢复双方的隐藏推荐
     */
    void splitRecommendData(Long userId, Long cpUserId);

}
