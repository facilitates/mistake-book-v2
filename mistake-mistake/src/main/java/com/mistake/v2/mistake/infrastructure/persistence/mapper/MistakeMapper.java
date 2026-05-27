package com.mistake.v2.mistake.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mistake.v2.mistake.domain.MistakeFilter;
import com.mistake.v2.mistake.infrastructure.persistence.po.MistakePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 错题 Mapper 接口
 *
 * @author mistake-team
 */
@Mapper
public interface MistakeMapper extends BaseMapper<MistakePO> {

    /**
     * 根据用户ID + 过滤条件动态查询
     */
    @Select("<script>" +
            "<bind name='keywordPattern' value=\"'%' + filter.keyword + '%'\"/>" +
            "SELECT * FROM mistakes WHERE user_id = #{userId}" +
            "<if test='filter.subject != null'> AND subject = #{filter.subject}</if>" +
            "<if test='filter.topic != null and filter.topic != \"\"'> AND topic = #{filter.topic}</if>" +
            "<if test='filter.mastery != null'> AND mastery = #{filter.mastery}</if>" +
            "<if test='filter.questionType != null'> AND question_type = #{filter.questionType}</if>" +
            "<if test='filter.keyword != null and filter.keyword != \"\"'>" +
            " AND (problem_text LIKE #{keywordPattern} OR solution LIKE #{keywordPattern})" +
            "</if>" +
            " ORDER BY id DESC" +
            " LIMIT #{filter.pageSize} OFFSET #{filter.offset}" +
            "</script>")
    List<MistakePO> findByUserIdAndFilter(@Param("userId") Long userId, @Param("filter") MistakeFilter filter);

    /**
     * 查询需要复习的错题 (未掌握 + 已到期或未设置复习日期)
     */
    @Select("SELECT * FROM mistakes WHERE user_id = #{userId}" +
            " AND mastery != 'mastered'" +
            " AND (next_review_at IS NULL OR next_review_at <= date('now'))" +
            " ORDER BY next_review_at ASC NULLS FIRST")
    List<MistakePO> findDueForReview(@Param("userId") Long userId);

    /**
     * 统计指定用户 + 掌握度的错题数量
     */
    @Select("SELECT COUNT(*) FROM mistakes WHERE user_id = #{userId} AND mastery = #{mastery}")
    long countByUserIdAndMastery(@Param("userId") Long userId, @Param("mastery") String mastery);
}
