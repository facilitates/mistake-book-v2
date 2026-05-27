package com.mistake.v2.mistake.interfaces.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 错题列表视图对象
 *
 * @author mistake-team
 */
@Data
@Builder
public class MistakeListVO {

    /** 错题列表 */
    private List<MistakeVO> list;

    /** 总记录数 */
    private long total;

    /** 当前页码 */
    private int page;

    /** 每页数量 */
    private int pageSize;
}
