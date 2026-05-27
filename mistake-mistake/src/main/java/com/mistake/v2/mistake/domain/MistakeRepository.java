package com.mistake.v2.mistake.domain;

import java.util.List;
import java.util.Optional;

/**
 * 错题仓储接口 (领域层定义, 基础设施层实现)
 *
 * @author mistake-team
 */
public interface MistakeRepository {

    /** 保存 */
    Mistake save(Mistake mistake);

    /** 更新 */
    Mistake update(Mistake mistake);

    /** 根据ID查找 */
    Optional<Mistake> findById(Long id);

    /** 根据用户ID查找所有 */
    List<Mistake> findByUserId(Long userId);

    /** 根据用户ID + 过滤条件查找 */
    List<Mistake> findByUserIdAndFilter(Long userId, MistakeFilter filter);

    /** 查找需复习的题目 (未掌握 + 已到期) */
    List<Mistake> findDueForReview(Long userId);

    /** 查找未掌握的题目 */
    List<Mistake> findNotMastered(Long userId);

    /** 根据ID + 用户ID删除 */
    void deleteByIdAndUserId(Long id, Long userId);

    /** 统计用户错题数量 */
    long countByUserId(Long userId);

    /** 统计用户某掌握度的数量 */
    long countByUserIdAndMastery(Long userId, String mastery);
}
