package com.mistake.v2.review.interfaces.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 复习统计视图对象
 *
 * @author mistake-team
 */
@Data
@Builder
public class ReviewStatsVO {

    /** 错题总数 */
    private long total;

    /** 已掌握数量 */
    private long mastered;

    /** 复习中数量 */
    private long reviewing;

    /** 今日待复习数量 */
    private long dueToday;
}
