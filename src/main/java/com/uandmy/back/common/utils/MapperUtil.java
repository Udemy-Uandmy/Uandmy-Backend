package com.uandmy.back.common.utils;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MapperUtil {

    private static ModelMapper modelMapper;

    // 생성자에 ModelMapper를 주입받고 static 필드에 할당
    public MapperUtil(ModelMapper modelMapper) {
        MapperUtil.modelMapper = modelMapper;
    }

    public static ModelMapper getModelMapper() {
        return modelMapper;
    }
}
