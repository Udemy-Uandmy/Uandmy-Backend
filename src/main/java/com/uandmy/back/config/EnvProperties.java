package com.uandmy.back.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class EnvProperties {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${base-url}")
    private String baseUrl;

    @Value("${file.upload-path}")
    private String uploadPath;

    @Value("${file.editor-upload-path}")
    private String editorUploadPath;
}
