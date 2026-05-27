package com.mistake.v2.interfaces.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 错题视图对象 — 返回给前端的DTO
 *
 * @author mistake-team
 */
@Data
@Builder
public class MistakeVO {

    /** 唯一标识 */
    private Long id;

    /** 科目 (displayName) */
    private String subject;

    /** 子类/章节 */
    private String topic;

    /** 题型 (displayName) */
    private String questionType;

    /** 难度 (1-5) */
    private Integer difficulty;

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

    /** 掌握程度 (displayName) */
    private String mastery;

    /** SM-2: 复习间隔 (天) */
    private Integer reviewInterval;

    /** SM-2: 难度系数 */
    private Double easeFactor;

    /** SM-2: 复习次数 */
    private Integer reviewCount;

    /** 下次复习日期 (yyyy-MM-dd) */
    private String nextReviewAt;

    /** 创建时间 */
    private LocalDateTime createdAt;
}
