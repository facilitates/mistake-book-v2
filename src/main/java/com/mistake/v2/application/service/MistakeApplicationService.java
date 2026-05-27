package com.mistake.v2.application.service;

import com.mistake.v2.application.command.CreateMistakeCommand;
import com.mistake.v2.application.command.ReviewMistakeCommand;
import com.mistake.v2.domain.mistake.Mistake;
import com.mistake.v2.domain.mistake.MistakeFilter;
import com.mistake.v2.domain.mistake.MistakeRepository;
import com.mistake.v2.domain.review.algorithm.SpacedRepetitionAlgorithm;
import com.mistake.v2.domain.review.algorithm.SpacedRepetitionAlgorithm.ReviewResult;
import com.mistake.v2.domain.shared.enums.MasteryLevel;
import com.mistake.v2.domain.shared.vo.Difficulty;
import com.mistake.v2.domain.shared.vo.Topic;
import com.mistake.v2.infrastructure.common.enums.ErrorCode;
import com.mistake.v2.infrastructure.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 错题应用服务
 * <p>编排领域对象完成业务用例，不包含业务逻辑，只做任务协调。</p>
 *
 * @author mistake-team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MistakeApplicationService {

    private final MistakeRepository mistakeRepository;
    private final SpacedRepetitionAlgorithm spacedRepetitionAlgorithm;

    /**
     * 创建错题
     *
     * @param cmd 创建命令
     * @return 保存后的错题聚合根
     */
    public Mistake create(CreateMistakeCommand cmd) {
        log.info("创建错题: userId={}, subject={}, topic={}", cmd.getUserId(), cmd.getSubject(), cmd.getTopic());

        Mistake mistake = Mistake.builder()
                .userId(cmd.getUserId())
                .subject(cmd.getSubject())
                .topic(new Topic(cmd.getTopic()))
                .questionType(cmd.getQuestionType())
                .difficulty(new Difficulty(cmd.getDifficulty()))
                .imagePath(cmd.getImagePath())
                .problemText(cmd.getProblemText())
                .solution(cmd.getSolution())
                .answer(cmd.getAnswer())
                .knowledgePoints(cmd.getKnowledgePoints())
                .mastery(MasteryLevel.NEW)
                .reviewInterval(0)
                .easeFactor(2.5)
                .reviewCount(0)
                .nextReviewAt(LocalDate.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Mistake saved = mistakeRepository.save(mistake);
        log.info("错题创建成功: id={}", saved.getId());
        return saved;
    }

    /**
     * 根据ID查找错题
     *
     * @param id     错题ID
     * @param userId 用户ID（用于校验归属）
     * @return 错题聚合根
     * @throws BusinessException 错题不存在或不属于该用户
     */
    public Mistake findById(Long id, Long userId) {
        log.info("查找错题: id={}, userId={}", id, userId);

        Mistake mistake = mistakeRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        if (!mistake.getUserId().equals(userId)) {
            log.warn("错题不属于该用户: id={}, userId={}, ownerId={}", id, userId, mistake.getUserId());
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }

        return mistake;
    }

    /**
     * 按过滤条件查询错题列表
     *
     * @param userId 用户ID
     * @param filter 查询过滤器
     * @return 错题列表
     */
    public List<Mistake> listByFilter(Long userId, MistakeFilter filter) {
        log.info("查询错题列表: userId={}, filter={}", userId, filter);
        return mistakeRepository.findByUserIdAndFilter(userId, filter);
    }

    /**
     * 更新错题内容
     *
     * @param id     错题ID
     * @param userId 用户ID
     * @param cmd    更新命令（复用创建命令）
     * @return 更新后的错题
     */
    public Mistake update(Long id, Long userId, CreateMistakeCommand cmd) {
        log.info("更新错题: id={}, userId={}", id, userId);

        Mistake mistake = findById(id, userId);
        mistake.updateContent(cmd.getProblemText(), cmd.getSolution(), cmd.getAnswer(), cmd.getKnowledgePoints());

        Mistake updated = mistakeRepository.save(mistake);
        log.info("错题更新成功: id={}", updated.getId());
        return updated;
    }

    /**
     * 删除错题
     *
     * @param id     错题ID
     * @param userId 用户ID
     */
    public void delete(Long id, Long userId) {
        log.info("删除错题: id={}, userId={}", id, userId);

        // 先校验错题存在且属于该用户
        findById(id, userId);
        mistakeRepository.deleteByIdAndUserId(id, userId);

        log.info("错题删除成功: id={}", id);
    }

    /**
     * 复习评分
     * <p>根据用户评分调用 SM-2 算法更新掌握度和复习计划</p>
     *
     * @param cmd 复习评分命令
     * @return 更新后的错题
     */
    public Mistake review(ReviewMistakeCommand cmd) {
        log.info("复习评分: userId={}, mistakeId={}, rating={}", cmd.getUserId(), cmd.getMistakeId(), cmd.getRating());

        Mistake mistake = findById(cmd.getMistakeId(), cmd.getUserId());

        ReviewResult result = spacedRepetitionAlgorithm.calculate(
                cmd.getRating(),
                mistake.getReviewInterval(),
                mistake.getEaseFactor(),
                mistake.getReviewCount()
        );

        mistake.updateMastery(
                result.newMastery(),
                result.interval(),
                result.easeFactor(),
                mistake.getReviewCount() + 1,
                result.nextReviewAt()
        );

        Mistake updated = mistakeRepository.save(mistake);
        log.info("复习评分处理完成: id={}, newMastery={}, nextReviewAt={}",
                updated.getId(), updated.getMastery(), updated.getNextReviewAt());
        return updated;
    }

    /**
     * 获取需复习的错题列表
     *
     * @param userId 用户ID
     * @return 到期待复习的错题列表
     */
    public List<Mistake> getDueForReview(Long userId) {
        log.info("获取待复习错题: userId={}", userId);
        List<Mistake> dueList = mistakeRepository.findDueForReview(userId);
        log.info("待复习错题数量: {}", dueList.size());
        return dueList;
    }
}
