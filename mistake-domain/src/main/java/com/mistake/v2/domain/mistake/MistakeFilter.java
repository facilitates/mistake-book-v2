package com.mistake.v2.domain.mistake;

import com.mistake.v2.domain.shared.enums.MasteryLevel;
import com.mistake.v2.domain.shared.enums.Subject;
import lombok.Builder;
import lombok.Getter;

/**
 * 错题查询过滤器 (值对象)
 *
 * @author mistake-team
 */
@Getter
@Builder
public class MistakeFilter {

    /** 科目过滤 (null=全部) */
    private Subject subject;

    /** 子类过滤 (null=全部) */
    private String topic;

    /** 掌握度过滤 (null=全部) */
    private MasteryLevel mastery;

    /** 题型过滤 (null=全部) */
    private String questionType;

    /** 关键词搜索 */
    private String keyword;

    /** 页码 (从1开始) */
    @Builder.Default
    private int page = 1;

    /** 每页数量 */
    @Builder.Default
    private int pageSize = 10;

    /** 偏移量 */
    public int getOffset() {
        return (page - 1) * pageSize;
    }
}
