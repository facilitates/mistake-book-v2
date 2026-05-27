package com.mistake.v2.mistake.application;

import com.mistake.v2.mistake.domain.Mistake;
import com.mistake.v2.mistake.domain.MistakeFilter;
import com.mistake.v2.mistake.domain.MistakeRepository;
import com.mistake.v2.common.exception.BusinessException;
import com.mistake.v2.common.vo.Difficulty;
import com.mistake.v2.common.vo.Topic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 错题 CRUD 应用服务
 *
 * @author mistake-team
 */
@Service
public class MistakeService {

    private final MistakeRepository mistakeRepository;

    public MistakeService(MistakeRepository mistakeRepository) {
        this.mistakeRepository = mistakeRepository;
    }

    /**
     * 创建错题
     */
    @Transactional
    public Mistake create(CreateMistakeCommand command) {
        Mistake mistake = Mistake.builder()
                .userId(command.getUserId())
                .subject(command.getSubject())
                .topic(command.getTopic() != null ? new Topic(command.getTopic()) : null)
                .questionType(command.getQuestionType())
                .difficulty(command.getDifficulty() != null ? new Difficulty(command.getDifficulty()) : null)
                .imagePath(command.getImagePath())
                .problemText(command.getProblemText())
                .solution(command.getSolution())
                .answer(command.getAnswer())
                .knowledgePoints(command.getKnowledgePoints())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return mistakeRepository.save(mistake);
    }

    /**
     * 根据ID查找错题（校验所属用户）
     */
    public Mistake findById(Long id, Long userId) {
        Mistake mistake = mistakeRepository.findById(id)
                .orElseThrow(() -> new BusinessException("错题不存在"));
        if (!mistake.getUserId().equals(userId)) {
            throw new BusinessException("无权访问该错题");
        }
        return mistake;
    }

    /**
     * 根据过滤条件查询错题列表
     */
    public List<Mistake> listByFilter(Long userId, MistakeFilter filter) {
        return mistakeRepository.findByUserIdAndFilter(userId, filter);
    }

    /**
     * 更新错题
     */
    @Transactional
    public Mistake update(Long id, Long userId, CreateMistakeCommand command) {
        Mistake existing = findById(id, userId);
        existing.updateContent(
                command.getProblemText(),
                command.getSolution(),
                command.getAnswer(),
                command.getKnowledgePoints()
        );
        return mistakeRepository.update(existing);
    }

    /**
     * 删除错题
     */
    @Transactional
    public void delete(Long id, Long userId) {
        // verify ownership
        findById(id, userId);
        mistakeRepository.deleteByIdAndUserId(id, userId);
    }
}
