package com.mistake.v2.review.domain.algorithm;

import com.mistake.v2.common.enums.MasteryLevel;
import com.mistake.v2.common.enums.Rating;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * SM-2 spaced repetition algorithm implementation.
 * Based on the SuperMemo SM-2 algorithm by Piotr Woźniak.
 */
@Component
public class SpacedRepetitionAlgorithm {

    public record ReviewResult(int interval, double easeFactor, MasteryLevel newMastery, LocalDate nextReviewAt) {
    }

    /**
     * Calculate the next review parameters based on the user's rating.
     *
     * @param rating            the user's self-assessment rating
     * @param currentInterval   current interval in days
     * @param currentEaseFactor current ease factor (default 2.5 for new items)
     * @param reviewCount       number of successful reviews so far
     * @return the calculated review result with updated parameters
     */
    public ReviewResult calculate(Rating rating, int currentInterval, double currentEaseFactor, int reviewCount) {
        int newInterval;
        double newEaseFactor = currentEaseFactor;
        int newReviewCount;

        switch (rating) {
            case AGAIN:
                newEaseFactor = Math.max(1.3, currentEaseFactor - 0.2);
                newInterval = 1;
                newReviewCount = 0;
                break;

            case FUZZY:
                // interval = currentInterval * easeFactor * 0.5, at least 1 day
                newInterval = Math.max(1, (int) Math.round(currentInterval * currentEaseFactor * 0.5));
                newReviewCount = reviewCount + 1;
                break;

            case GOT_IT:
                newEaseFactor = Math.min(2.5, currentEaseFactor + 0.1);
                newInterval = (int) Math.round(currentInterval * newEaseFactor);
                newReviewCount = reviewCount + 1;
                break;

            default:
                throw new IllegalArgumentException("Unknown rating: " + rating);
        }

        MasteryLevel newMastery;
        if (newInterval >= 120) {
            newMastery = MasteryLevel.MASTERED;
        } else if (newReviewCount == 0) {
            newMastery = MasteryLevel.NEW;
        } else {
            newMastery = MasteryLevel.REVIEWING;
        }

        LocalDate nextReviewAt = LocalDate.now().plusDays(newInterval);

        return new ReviewResult(newInterval, newEaseFactor, newMastery, nextReviewAt);
    }
}
