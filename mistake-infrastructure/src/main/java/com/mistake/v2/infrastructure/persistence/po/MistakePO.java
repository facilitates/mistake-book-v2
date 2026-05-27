package com.mistake.v2.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 错题持久化对象 — 映射 mistakes 表
 *
 * @author mistake-team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "mistakes")
public class MistakePO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 所属用户ID */
    @TableField("user_id")
    private Long userId;

    /** 科目 (枚举名: HIGHER_MATH, LINEAR_ALGEBRA, ...) */
    @TableField("subject")
    private String subject;

    /** 子类/章节 */
    @TableField("topic")
    private String topic;

    /** 题型 (枚举名: CHOICE, FILL, CALCULATION, ESSAY) */
    @TableField("question_type")
    private String questionType;

    /** 难度 (1-5) */
    @TableField("difficulty")
    private Integer difficulty;

    /** 原始图片路径 */
    @TableField("image_path")
    private String imagePath;

    /** 题目文本 (Markdown) */
    @TableField("problem_text")
    private String problemText;

    /** 解析文本 (Markdown) */
    @TableField("solution")
    private String solution;

    /** 答案 */
    @TableField("answer")
    private String answer;

    /** 知识点列表 (JSON 数组字符串) */
    @TableField("knowledge_points")
    private String knowledgePoints;

    /** 掌握程度 (new / reviewing / mastered) */
    @TableField("mastery")
    private String mastery;

    /** SM-2: 复习间隔 (天) */
    @TableField("review_interval")
    private Integer reviewInterval;

    /** SM-2: 难度系数 (默认 2.5) */
    @TableField("ease_factor")
    private Double easeFactor;

    /** SM-2: 复习次数 */
    @TableField("review_count")
    private Integer reviewCount;

    /** 下次复习日期 */
    @TableField("next_review_at")
    private LocalDate nextReviewAt;

    /** 创建时间 */
    @TableField("created_at")
    private LocalDateTime createdAt;

    /** 更新时间 */
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
