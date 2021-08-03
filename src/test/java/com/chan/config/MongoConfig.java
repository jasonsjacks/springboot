package com.chan.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;


@Configuration
public class MongoConfig {
    //这是mongo的一个固定模板
    @Bean("mongoTemplatesQuote")
    MongoTemplate mongoTemplates(MongoDatabaseFactory factory, MongoConverter converter) {
        return new MongoTemplate(factory, converter);
    }

}
