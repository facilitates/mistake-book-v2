package com.mistake.v2.common.vo;

public record Topic(String value) {

    public Topic {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Topic value must not be null or blank");
        }
    }
}
