package com.vueones.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import java.text.SimpleDateFormat;

/**
 * Jackson配置类
 * 用于解决JSON序列化时的循环引用问题和日期格式化
 */
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .build();
                
        // 配置Jackson以处理循环引用问题
        objectMapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);
        
        // 配置Jackson不要采用默认的引用处理策略
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        
        // 忽略未知属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
        // 设置日期格式
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        
        // 允许空对象
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        
        return objectMapper;
    }
} 