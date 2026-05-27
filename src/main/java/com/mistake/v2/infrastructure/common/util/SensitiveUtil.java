package com.mistake.v2.infrastructure.common.util;

/**
 * 敏感信息脱敏工具
 *
 * <p>对日志输出中的敏感字段（password、token、secret 等）进行脱敏处理。
 *
 * @author mistake-team
 * @since 2.0.0
 */
public class SensitiveUtil {

    /** 敏感字段匹配正则 */
    private static final String SENSITIVE_PATTERN = "(password|token|secret)\\s*[:=]\\s*\"[^\"]*\"|"
            + "(password|token|secret)\\s*[:=]\\s*'[^']*'|"
            + "(password|token|secret)\\s*[:=]\\s*[^,\\s}]+";

    /** 脱敏替换模板 */
    private static final String REPLACEMENT = "$1$2$3=***";

    /**
     * 对字符串中的敏感字段值进行脱敏
     *
     * @param text 原始文本
     * @return 脱敏后的文本
     */
    public static String mask(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.replaceAll(SENSITIVE_PATTERN, REPLACEMENT);
    }

}
