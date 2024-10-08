package com.boredream.springbootdemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boredream.springbootdemo.entity.Visitor;
import com.boredream.springbootdemo.mapper.VisitorMapper;
import com.boredream.springbootdemo.service.IVisitorService;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author boredream
 * @since 2024-09-23
 */
@Service
public class VisitorServiceImpl extends ServiceImpl<VisitorMapper, Visitor> implements IVisitorService {

    @Override
    public boolean updateField(Visitor visitor, String json, boolean overwrite) {
        // json里面有值，且原字段不存在的才进行更新
        // {'family':'暂无','personality':'暂无','relationship':'来访者认为母亲会在其学习时打扰，令其反感。来访者认为父亲未提及。','parent':'父母关系答案暂无','events':'重大生活事件答案暂无','trauma':'既往创伤答案暂无','self-rated':'自我评价为自控能力不强，需要督促。','peer':'同伴关系答案暂无','intimat':'亲密关系答案暂无','symptoms':'紧张、担心，两年半','present':'对学习失去兴趣，伴随督促需求与反感，持续至大学。','past':'既往病史答案暂无','addiction':'上瘾史答案暂无'}
        StringBuilder sb = new StringBuilder();
        sb.append("updateIfEmptyField ==>");
        JsonObject jo = new Gson().fromJson(json, JsonObject.class);
        for (Map.Entry<String, JsonElement> entry : jo.entrySet()) {
            try {
                String key = entry.getKey();
                if ("name".equals(key)) {
                    // AI结果不覆盖姓名
                    continue;
                }

                if ("self-rated".equals(key)) {
                    key = "selfRated";
                }
                Object value;
                if ("age".equals(key)) {
                    value = entry.getValue().getAsInt();
                } else {
                    value = entry.getValue().getAsString();
                }

                sb.append("\n").append("ai result : key = ").append(key)
                        .append(", value = ").append(value);

                if (value != null && !"null".equals(value)) {
                    Field field = visitor.getClass().getDeclaredField(key);
                    field.setAccessible(true); // 允许访问私有字段

                    if (overwrite) {
                        // 如果是覆盖操作，直接更新
                        field.set(visitor, value);
                        sb.append(", OVERWRITE UPDATE !");
                    } else {
                        // 非覆盖操作，只有原有字段为空时才更新
                        Object fieldValue = field.get(visitor);
                        if (fieldValue == null || (fieldValue instanceof String && ((String) fieldValue).isEmpty())) {
                            field.set(visitor, value); // 更新字段值
                            sb.append(", old value is empty UPDATE !");
                        }
                    }
                }
            } catch (Exception e) {
                // 处理反射异常，比如字段不存在，跳过
                sb.append("\n SKIP ERROR FIELD ")
                        .append(entry.getKey())
                        .append("! error info = ")
                        .append(e.getMessage());
            }
        }
        System.out.println(sb);
        return updateById(visitor);
    }
}
