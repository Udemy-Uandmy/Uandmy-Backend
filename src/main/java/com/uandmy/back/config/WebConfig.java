package com.uandmy.back.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final EnvProperties envProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //TODO :: window :file:/// , linux : file:/ 추후 분기 필수
        registry.addResourceHandler("/files/editor/**").addResourceLocations("file:///" + envProperties.getEditorUploadPath() + "/");
    }
}