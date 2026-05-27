package com.mistake.v2.review.application;

import com.mistake.v2.mistake.domain.Mistake;
import com.mistake.v2.mistake.domain.MistakeRepository;
import com.mistake.v2.common.exception.BusinessException;
import com.mistake.v2.review.domain.algorithm.SpacedRepetitionAlgorithm;
import com.mistake.v2.review.domain.algorithm.SpacedRepetitionAlgorithm.ReviewResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 复习应用服务 — SM-2 间隔重复
 *
 * @author mistake-team
 */
@Service
public class ReviewService {

    private final MistakeRepository mistakeRepository;
    private final SpacedRepetitionAlgorithm algorithm;

    public ReviewService(MistakeRepository mistakeRepository, SpacedRepetitionAlgorithm algorithm) {
        this.mistakeRepository = mistakeRepository;
        this.algorithm = algorithm;
    }

    /**
     * 复习评分 — 应用 SM-2 算法更新掌握状态
     */
    @Transactional
    public void review(ReviewMistakeCommand command) {
        Mistake mistake = mistakeRepository.findById(command.getMistakeId())
                .orElseThrow(() -> new BusinessException("错题不存在"));
        if (!mistake.getUserId().equals(command.getUserId())) {
            throw new BusinessException("无权复习该错题");
        }

        ReviewResult result = algorithm.calculate(
                command.getRating(),
                mistake.getReviewInterval(),
                mistake.getEaseFactor(),
                mistake.getReviewCount()
        );

        mistake.updateMastery(
                result.newMastery(),
                result.interval(),
                result.easeFactor(),
                mistake.getReviewCount() + 1,
                result.nextReviewAt()
        );

        mistakeRepository.update(mistake);
    }

    /**
     * 获取待复习错题列表
     */
    public List<Mistake> getDueForReview(Long userId) {
        return mistakeRepository.findDueForReview(userId);
    }

    /**
     * 复习统计 — 按掌握度分组统计
     */
    public ReviewStatsResult getStats(Long userId) {
        List<Mistake> all = mistakeRepository.findByUserId(userId);

        long total = all.size();
        long mastered = all.stream().filter(Mistake::isMastered).count();
        long dueToday = all.stream().filter(Mistake::isDue).count();
        long reviewing = total - mastered;

        return new ReviewStatsResult(total, mastered, reviewing, dueToday);
    }

    /**
     * 复习统计结果（内部 DTO）
     */
    public record ReviewStatsResult(long total, long mastered, long reviewing, long dueToday) {
    }
}
