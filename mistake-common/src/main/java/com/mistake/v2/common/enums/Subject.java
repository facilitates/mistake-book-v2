package com.mistake.v2.common.enums;

public enum Subject {

    HIGHER_MATH("高等数学"),
    LINEAR_ALGEBRA("线性代数"),
    DATA_STRUCTURE("数据结构"),
    COMPUTER_ORG("计算机组成原理"),
    OS("操作系统"),
    COMPUTER_NETWORK("计算机网络");

    private final String displayName;

    Subject(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Subject fromDisplayName(String displayName) {
        for (Subject subject : values()) {
            if (subject.displayName.equals(displayName)) {
                return subject;
            }
        }
        throw new IllegalArgumentException("Unknown display name: " + displayName);
    }
}
