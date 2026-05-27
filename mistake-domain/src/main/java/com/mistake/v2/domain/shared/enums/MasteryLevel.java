package com.mistake.v2.domain.shared.enums;

public enum MasteryLevel {

    NEW("new", "新题"),
    REVIEWING("reviewing", "复习中"),
    MASTERED("mastered", "已掌握");

    private final String code;
    private final String displayName;

    MasteryLevel(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static MasteryLevel fromCode(String code) {
        for (MasteryLevel level : values()) {
            if (level.code.equals(code)) {
                return level;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}
