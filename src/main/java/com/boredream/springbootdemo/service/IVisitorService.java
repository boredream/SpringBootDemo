package com.boredream.springbootdemo.service;

import com.boredream.springbootdemo.entity.Visitor;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户 服务类
 * </p>
 *
 * @author boredream
 * @since 2024-09-23
 */
public interface IVisitorService extends IService<Visitor> {

    boolean updateField(Visitor visitor, String json, boolean overwrite);
}
