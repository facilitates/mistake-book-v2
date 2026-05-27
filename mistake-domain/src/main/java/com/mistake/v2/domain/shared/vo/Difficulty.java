package com.mistake.v2.domain.shared.vo;

public record Difficulty(int value) {

    public Difficulty {
        if (value < 1 || value > 5) {
            throw new IllegalArgumentException("Difficulty value must be between 1 and 5, got: " + value);
        }
    }
}
