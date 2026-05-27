package com.mistake.v2.domain.mistake;

import com.mistake.v2.domain.shared.enums.MasteryLevel;
import com.mistake.v2.domain.shared.enums.QuestionType;
import com.mistake.v2.domain.shared.enums.Subject;
import com.mistake.v2.domain.shared.vo.Difficulty;
import com.mistake.v2.domain.shared.vo.Topic;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 错题聚合根
 * <p>聚合边界：一道错题的完整生命周期，包括题目信息、图片、知识点、掌握状态</p>
 *
 * @author mistake-team
 */
@Getter
@Builder
public class Mistake {

    /** 唯一标识 */
    private Long id;

    /** 所属用户ID */
    private Long userId;

    /** 科目 */
    private Subject subject;

    /** 子类/章节 */
    private Topic topic;

    /** 题型 */
    private QuestionType questionType;

    /** 难度 (1-5) */
    private Difficulty difficulty;

    /** 原始图片路径 */
    private String imagePath;

    /** 题目文本 (Markdown) */
    private String problemText;

    /** 解析文本 (Markdown) */
    private String solution;

    /** 答案 */
    private String answer;

    /** 知识点列表 */
    private List<String> knowledgePoints;

    /** 掌握程度 */
    private MasteryLevel mastery;

    /** SM-2: 复习间隔 (天) */
    private int reviewInterval;

    /** SM-2: 难度系数 (默认 2.5) */
    private double easeFactor;

    /** SM-2: 复习次数 */
    private int reviewCount;

    /** 下次复习日期 */
    private LocalDate nextReviewAt;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;

    // ── 领域行为 ──

    /**
     * 更新掌握程度
     */
    public void updateMastery(MasteryLevel newMastery,
                               int interval,
                               double easeFactor,
                               int reviewCount,
                               LocalDate nextReviewAt) {
        this.mastery = newMastery;
        this.reviewInterval = interval;
        this.easeFactor = easeFactor;
        this.reviewCount = reviewCount;
        this.nextReviewAt = nextReviewAt;
    }

    /**
     * 更新题目内容
     */
    public void updateContent(String problemText, String solution, String answer, List<String> knowledgePoints) {
        this.problemText = problemText;
        this.solution = solution;
        this.answer = answer;
        this.knowledgePoints = knowledgePoints;
    }

    /**
     * 判断是否需要复习 (已到复习日期)
     */
    public boolean isDue() {
        return mastery != MasteryLevel.MASTERED
            && (nextReviewAt == null || !nextReviewAt.isAfter(LocalDate.now()));
    }

    /**
     * 判断是否已掌握
     */
    public boolean isMastered() {
        return mastery == MasteryLevel.MASTERED;
    }
}
