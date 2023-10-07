package com.hcmute.drink.config;

import com.hcmute.drink.collection.ProductCollection;
import com.hcmute.drink.dto.CreateProductRequest;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Configuration
@Slf4j
public class ModelMapperConfig {
    @Autowired
    @Lazy
    private ObjectIdToStringConverter objectIdToStringConverter;
    @Bean
    @Primary
    public ModelMapper modelMapper() {
        log.info("modelMapper");
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
        return modelMapper;
    }

    @Bean("modelMapperNotNull")
    public ModelMapper modelMapperNotNull() {
        log.info("modelMapperNotNull");
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.getConfiguration()
                .setPropertyCondition(Conditions.isNotNull())
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
        return modelMapper;
    }


    @Component
    public class ObjectIdToStringConverter extends AbstractConverter<ObjectId, String> {

        @Override
        protected String convert(ObjectId objectId) {
            return objectId.toString();
        }
    }
}
