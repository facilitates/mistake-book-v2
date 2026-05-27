package com.mistake.v2.common.enums;

public enum QuestionType {

    CHOICE("选择题"),
    FILL("填空题"),
    CALCULATION("计算题"),
    ESSAY("简答题");

    private final String displayName;

    QuestionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static QuestionType fromDisplayName(String displayName) {
        for (QuestionType type : values()) {
            if (type.displayName.equals(displayName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown display name: " + displayName);
    }
}
