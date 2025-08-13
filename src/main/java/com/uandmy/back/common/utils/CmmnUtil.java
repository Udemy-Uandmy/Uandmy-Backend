package com.uandmy.back.common.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.uandmy.back.core.CoreConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
public class CmmnUtil {


    private CmmnUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static HttpServletRequest request;

    public static void setRequest(HttpServletRequest request) {
        CmmnUtil.request = request;
    }

    private static Validator validator;

    public static void setValidator(Validator validator) {
        CmmnUtil.validator = validator;
    }

    public static ObjectMapper getObjectMapper() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(CoreConstants.DATE_FORMAT)));
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(CoreConstants.DATE_TIME_FORMAT)));

        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(CoreConstants.DATE_FORMAT)));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(CoreConstants.DATE_TIME_FORMAT)));

        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(javaTimeModule);
    }

    /**
     * 사용자 IP 추출
     *
     * @return 접속 IP
     */
    public static String getClientIp() {
        String clientIp = "unKnown";
        try {
            clientIp = request.getRemoteAddr();

            String[] ipHeaderCandidates = {
                    "X-Forwarded-For",
                    "Proxy-Client-IP",
                    "WL-Proxy-Client-IP",
                    "HTTP_X_FORWARDED_FOR",
                    "HTTP_X_FORWARDED",
                    "HTTP_X_CLUSTER_CLIENT_IP",
                    "HTTP_CLIENT_IP",
                    "HTTP_FORWARDED_FOR",
                    "HTTP_FORWARDED",
                    "HTTP_VIA",
                    "REMOTE_ADDR"
            };

            for (String header : ipHeaderCandidates) {
                String ipFromHeader = request.getHeader(header);
                if (Objects.nonNull(ipFromHeader) && !ipFromHeader.isEmpty() && !"unknown".equalsIgnoreCase(ipFromHeader)) {
                    clientIp = ipFromHeader.split(",")[0];
                    break;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return clientIp;
    }

    public static String getClientUserAgent() {
        String userAgent = request.getHeader("User-Agent");
        return userAgent == null ? "User-Agent 정보를 가져올 수 없습니다." : userAgent;
    }

    public static List<Map<String, String>> validate(Object dto, Class<?>... groups) {
        List<Map<String, String>> errors = new ArrayList<>();

        Set<ConstraintViolation<Object>> violations = validator.validate(dto, groups);
        Map<String, String> error;
        for (ConstraintViolation<Object> violation : violations) {
            error = new HashMap<>();
            error.put("message", violation.getMessage());
            error.put("field", violation.getPropertyPath().toString());
            errors.add(error);
        }

        return errors;
    }

    public static String jsonParameter(HttpServletRequest request) {
        String jsonParameter = "";
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            if (request instanceof ContentCachingRequestWrapper cachingRequest) {

                if (cachingRequest.getContentAsByteArray().length > 0) {
                    jsonParameter = objectMapper.readTree(cachingRequest.getContentAsByteArray()).toString();
                }
            }
            if (StringUtils.isEmpty(jsonParameter)) {
                Map<String, String> params = new HashMap<>();
                Enumeration<String> parameterNames = request.getParameterNames();
                String name;
                while (parameterNames.hasMoreElements()) {
                    name = parameterNames.nextElement();
                    params.put(name, StringUtils.join(request.getParameterValues(name)));
                }

                jsonParameter = objectMapper.writeValueAsString(params);
            }
        } catch (IOException e) {
            jsonParameter = String.format("{\"error\":\"%s\"}", e);
            log.error(CoreUtil.getStackTraceAsString(e));
        }

        return jsonParameter;
    }
}
