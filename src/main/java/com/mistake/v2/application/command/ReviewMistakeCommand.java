package com.mistake.v2.application.command;

import com.mistake.v2.domain.shared.enums.Rating;
import lombok.Builder;
import lombok.Data;

/**
 * 复习评分命令 DTO
 *
 * @author mistake-team
 */
@Data
@Builder
public class ReviewMistakeCommand {

    /** 用户ID */
    private Long userId;

    /** 错题ID */
    private Long mistakeId;

    /** 评分 */
    private Rating rating;
}
