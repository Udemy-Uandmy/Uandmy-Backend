package com.uandmy.back.common.utils;


import com.uandmy.back.core.CoreConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;

@Slf4j
public class CoreUtil {

    private CoreUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String getStackTraceAsString(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    public static String base64EncodeToUrlSafeString(byte[] src) {
        return java.util.Base64.getUrlEncoder().encodeToString(src);
    }

    public static byte[] base64DecodeFromUrlSafeString(String src) {
        return base64DecodeFromUrlSafeString(src.getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] base64DecodeFromUrlSafeString(byte[] src) {
        return java.util.Base64.getUrlDecoder().decode(src);
    }

    /**
     * ARIA 암호화
     * @param srcStr 평문
     * @return 암호화 문자열
     */
    public static String ariaEncode(String srcStr) {
        return ariaEncode(srcStr, CoreConstants.ARIA_KEY);
    }
    public static String ariaEncode(String srcStr, String keyStr) {
        if (StringUtils.isBlank(srcStr)) {
            return srcStr;
        }
        String encodeStr;
        try {
            byte[] encodeByte = ariaEncode(srcStr.getBytes(StandardCharsets.UTF_8), keyStr);
            encodeStr = base64EncodeToUrlSafeString(encodeByte);
        } catch (InvalidKeyException e) {
            encodeStr = "Encryption error";
            //log.error(encodeStr, e);
        }
        return encodeStr;
    }

    public static byte[] ariaEncode(byte[] srcByte, String keyStr) throws InvalidKeyException {
        ARIAEngine instance = new ARIAEngine(256);

        int padLength = 16 - srcByte.length % 16;
        byte[] padByte = new byte[padLength];
        for(int i = 0 ; i < padLength; i++) {
            padByte[i] = (byte) padLength;
        }

        byte[] plainByte = new byte[srcByte.length + padLength];
        System.arraycopy(srcByte, 0, plainByte, 0, srcByte.length);
        System.arraycopy(padByte, 0, plainByte, srcByte.length, padLength);

        instance.setKey(ariaKey(keyStr));
        instance.setupRoundKeys();

        byte[] encodeByte = new byte[plainByte.length];
        for (int i = 0; i < srcByte.length; i += 16) {
            instance.encrypt(plainByte, i, encodeByte, i);
        }

        return encodeByte;
    }

    public static String oldAriaEncode(String srcStr) {
        String encodeStr;
        try {
            ARIAEngine instance = new ARIAEngine(256);

            /*
             * 128bit(16byte) 씩 끊어서 암호화가 진행되므로 전체 길이를 16의 배수로 맞춤
             * 이에 따라 복호화 시 문자열 끝에 공백이 붙는 경우가 생김
             * 이를 해결하기 위해 원문의 마지막에 :end 를 붙여 복호화 시 제거
             */
            srcStr += ":end";
            byte[] srcByte = srcStr.getBytes(StandardCharsets.UTF_8);

            int blength = srcByte.length;
            if ((blength % 16) > 0) {
                blength += 16 - blength % 16;
            }

            byte[] sb = new byte[blength];
            byte[] eb = new byte[blength];

            System.arraycopy(srcByte, 0, sb, 0, srcByte.length);

            instance.setKey(ariaKey(""));
            instance.setupRoundKeys();

            for (int i = 0; i < blength; i += 16) {
                instance.encrypt(sb, i, eb, i);
            }

            encodeStr = Base64.encodeBase64URLSafeString(eb);
        } catch (InvalidKeyException e) {
            encodeStr = "Encryption error";
            //log.error(encodeStr, e);
        }
        return encodeStr;
    }

    private static byte[] ariaKey(String keyStr) {
        if (StringUtils.isEmpty(keyStr)) {
            keyStr = CoreConstants.ARIA_KEY;
        }
        byte[] key = keyStr.getBytes(StandardCharsets.UTF_8);
        byte[] kb = new byte[32];
        System.arraycopy(key, 0, kb, 0, key.length);
        return kb;
    }

    /**
     * ARIA 복호화
     * @param encStr 암호화 문자열
     * @return 복호화 문자열
     */
    public static String ariaDecode(String encStr) {
        return ariaDecode(encStr, CoreConstants.ARIA_KEY);
    }
    public static String ariaDecode(String encStr, String keyStr) {
        if (StringUtils.isBlank(encStr)) {
            return encStr;
        }
        String result;
        try {
            byte[] resultByte = ariaDecode(base64DecodeFromUrlSafeString(encStr), keyStr);
            result = new String(resultByte, StandardCharsets.UTF_8);
        } catch (InvalidKeyException e) {
            result = "Decryption error";
            //log.error(result, e);
        }
        return result;
    }

    public static byte[] ariaDecode(byte[] srcByte, String keyStr) throws InvalidKeyException {
        ARIAEngine instance = new ARIAEngine(256);

        byte[] decodeByte = new byte[srcByte.length];

        instance.setKey(ariaKey(keyStr));
        instance.setupRoundKeys();

        for (int i = 0; i < srcByte.length; i += 16) {
            instance.decrypt(srcByte, i, decodeByte, i);
        }

        int padding = decodeByte[decodeByte.length - 1];
        byte[] resultByte = new byte[decodeByte.length - padding];
        System.arraycopy(decodeByte, 0, resultByte, 0, decodeByte.length - padding);

        return resultByte;
    }


}
