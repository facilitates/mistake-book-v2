package com.mistake.v2.interfaces.assembler;

import com.mistake.v2.domain.mistake.Mistake;
import com.mistake.v2.domain.mistake.MistakeFilter;
import com.mistake.v2.interfaces.dto.MistakeListVO;
import com.mistake.v2.interfaces.dto.MistakeVO;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 错题领域对象与视图对象转换器
 *
 * @author mistake-team
 */
public final class MistakeAssembler {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private MistakeAssembler() {
    }

    /**
     * 领域对象 → 视图对象
     */
    public static MistakeVO toVO(Mistake mistake) {
        return MistakeVO.builder()
                .id(mistake.getId())
                .subject(mistake.getSubject() != null ? mistake.getSubject().getDisplayName() : null)
                .topic(mistake.getTopic() != null ? mistake.getTopic().value() : null)
                .questionType(mistake.getQuestionType() != null ? mistake.getQuestionType().getDisplayName() : null)
                .difficulty(mistake.getDifficulty() != null ? mistake.getDifficulty().value() : null)
                .imagePath(mistake.getImagePath())
                .problemText(mistake.getProblemText())
                .solution(mistake.getSolution())
                .answer(mistake.getAnswer())
                .knowledgePoints(mistake.getKnowledgePoints())
                .mastery(mistake.getMastery() != null ? mistake.getMastery().getDisplayName() : null)
                .reviewInterval(mistake.getReviewInterval())
                .easeFactor(mistake.getEaseFactor())
                .reviewCount(mistake.getReviewCount())
                .nextReviewAt(mistake.getNextReviewAt() != null
                        ? mistake.getNextReviewAt().format(DATE_FORMATTER) : null)
                .createdAt(mistake.getCreatedAt())
                .build();
    }

    /**
     * 领域对象列表 → 列表视图对象
     */
    public static MistakeListVO toListVO(List<Mistake> mistakes, long total, MistakeFilter filter) {
        List<MistakeVO> voList = mistakes.stream()
                .map(MistakeAssembler::toVO)
                .collect(Collectors.toList());

        return MistakeListVO.builder()
                .list(voList)
                .total(total)
                .page(filter.getPage())
                .pageSize(filter.getPageSize())
                .build();
    }
}
