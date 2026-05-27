package com.mistake.v2.mistake.infrastructure.persistence.repository;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mistake.v2.mistake.domain.Mistake;
import com.mistake.v2.mistake.domain.MistakeFilter;
import com.mistake.v2.mistake.domain.MistakeRepository;
import com.mistake.v2.common.enums.MasteryLevel;
import com.mistake.v2.common.enums.QuestionType;
import com.mistake.v2.common.enums.Subject;
import com.mistake.v2.common.vo.Difficulty;
import com.mistake.v2.common.vo.Topic;
import com.mistake.v2.mistake.infrastructure.persistence.mapper.MistakeMapper;
import com.mistake.v2.mistake.infrastructure.persistence.po.MistakePO;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 错题仓储实现 — PO ↔ Domain 转换
 *
 * @author mistake-team
 */
@Repository
public class MistakeRepositoryImpl implements MistakeRepository {

    private final MistakeMapper mistakeMapper;

    public MistakeRepositoryImpl(MistakeMapper mistakeMapper) {
        this.mistakeMapper = mistakeMapper;
    }

    // ── 持久化操作 ──

    @Override
    public Mistake save(Mistake mistake) {
        MistakePO po = domainToPo(mistake);
        mistakeMapper.insert(po);
        return poToDomain(po);
    }

    @Override
    public Mistake update(Mistake mistake) {
        MistakePO po = domainToPo(mistake);
        mistakeMapper.updateById(po);
        return poToDomain(po);
    }

    @Override
    public Optional<Mistake> findById(Long id) {
        MistakePO po = mistakeMapper.selectById(id);
        return Optional.ofNullable(po).map(this::poToDomain);
    }

    @Override
    public List<Mistake> findByUserId(Long userId) {
        LambdaQueryWrapper<MistakePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MistakePO::getUserId, userId)
               .orderByDesc(MistakePO::getId);
        return mistakeMapper.selectList(wrapper).stream()
                .map(this::poToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Mistake> findByUserIdAndFilter(Long userId, MistakeFilter filter) {
        List<MistakePO> poList = mistakeMapper.findByUserIdAndFilter(userId, filter);
        return poList.stream()
                .map(this::poToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Mistake> findDueForReview(Long userId) {
        List<MistakePO> poList = mistakeMapper.findDueForReview(userId);
        return poList.stream()
                .map(this::poToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Mistake> findNotMastered(Long userId) {
        LambdaQueryWrapper<MistakePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MistakePO::getUserId, userId)
               .ne(MistakePO::getMastery, MasteryLevel.MASTERED.getCode())
               .orderByAsc(MistakePO::getNextReviewAt);
        return mistakeMapper.selectList(wrapper).stream()
                .map(this::poToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByIdAndUserId(Long id, Long userId) {
        LambdaQueryWrapper<MistakePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MistakePO::getId, id)
               .eq(MistakePO::getUserId, userId);
        mistakeMapper.delete(wrapper);
    }

    @Override
    public long countByUserId(Long userId) {
        LambdaQueryWrapper<MistakePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MistakePO::getUserId, userId);
        return mistakeMapper.selectCount(wrapper);
    }

    @Override
    public long countByUserIdAndMastery(Long userId, String mastery) {
        return mistakeMapper.countByUserIdAndMastery(userId, mastery);
    }

    // ── PO ↔ Domain 转换 ──

    /**
     * 持久化对象 → 领域对象
     */
    private Mistake poToDomain(MistakePO po) {
        return Mistake.builder()
                .id(po.getId())
                .userId(po.getUserId())
                .subject(parseSubject(po.getSubject()))
                .topic(po.getTopic() != null ? new Topic(po.getTopic()) : null)
                .questionType(parseQuestionType(po.getQuestionType()))
                .difficulty(po.getDifficulty() != null ? new Difficulty(po.getDifficulty()) : null)
                .imagePath(po.getImagePath())
                .problemText(po.getProblemText())
                .solution(po.getSolution())
                .answer(po.getAnswer())
                .knowledgePoints(parseKnowledgePoints(po.getKnowledgePoints()))
                .mastery(parseMastery(po.getMastery()))
                .reviewInterval(po.getReviewInterval() != null ? po.getReviewInterval() : 0)
                .easeFactor(po.getEaseFactor() != null ? po.getEaseFactor() : 2.5)
                .reviewCount(po.getReviewCount() != null ? po.getReviewCount() : 0)
                .nextReviewAt(po.getNextReviewAt())
                .createdAt(po.getCreatedAt())
                .updatedAt(po.getUpdatedAt())
                .build();
    }

    /**
     * 领域对象 → 持久化对象
     */
    private MistakePO domainToPo(Mistake mistake) {
        return MistakePO.builder()
                .id(mistake.getId())
                .userId(mistake.getUserId())
                .subject(mistake.getSubject() != null ? mistake.getSubject().name() : null)
                .topic(mistake.getTopic() != null ? mistake.getTopic().value() : null)
                .questionType(mistake.getQuestionType() != null ? mistake.getQuestionType().name() : null)
                .difficulty(mistake.getDifficulty() != null ? mistake.getDifficulty().value() : null)
                .imagePath(mistake.getImagePath())
                .problemText(mistake.getProblemText())
                .solution(mistake.getSolution())
                .answer(mistake.getAnswer())
                .knowledgePoints(serializeKnowledgePoints(mistake.getKnowledgePoints()))
                .mastery(mistake.getMastery() != null ? mistake.getMastery().getCode() : null)
                .reviewInterval(mistake.getReviewInterval())
                .easeFactor(mistake.getEaseFactor())
                .reviewCount(mistake.getReviewCount())
                .nextReviewAt(mistake.getNextReviewAt())
                .createdAt(mistake.getCreatedAt())
                .updatedAt(mistake.getUpdatedAt())
                .build();
    }

    // ── 枚举/值对象解析 ──

    private Subject parseSubject(String subject) {
        if (subject == null) return null;
        try {
            return Subject.valueOf(subject);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private QuestionType parseQuestionType(String questionType) {
        if (questionType == null) return null;
        try {
            return QuestionType.valueOf(questionType);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private MasteryLevel parseMastery(String mastery) {
        if (mastery == null) return null;
        try {
            return MasteryLevel.fromCode(mastery);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private List<String> parseKnowledgePoints(String json) {
        if (json == null || json.isBlank()) {
            return Collections.emptyList();
        }
        try {
            return JSONUtil.toList(json, String.class);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private String serializeKnowledgePoints(List<String> points) {
        if (points == null || points.isEmpty()) {
            return "[]";
        }
        return JSONUtil.toJsonStr(points);
    }
}
