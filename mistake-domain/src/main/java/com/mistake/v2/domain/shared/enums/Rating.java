package com.mistake.v2.domain.shared.enums;

public enum Rating {

    AGAIN("again", "不认识"),
    FUZZY("fuzzy", "模糊"),
    GOT_IT("gotit", "已掌握");

    private final String code;
    private final String displayName;

    Rating(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }
}
