package com.mistake.v2.application.command;

import com.mistake.v2.domain.shared.enums.QuestionType;
import com.mistake.v2.domain.shared.enums.Subject;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 上传错题命令 DTO
 *
 * @author mistake-team
 */
@Data
@Builder
public class CreateMistakeCommand {

    /** 所属用户ID */
    private Long userId;

    /** 科目 */
    private Subject subject;

    /** 子类/章节 */
    private String topic;

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

    /** 题型 */
    private QuestionType questionType;

    /** 难度 (1-5) */
    private Integer difficulty;
}
