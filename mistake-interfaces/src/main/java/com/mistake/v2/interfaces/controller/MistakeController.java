package com.mistake.v2.interfaces.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.mistake.v2.application.command.CreateMistakeCommand;
import com.mistake.v2.application.command.ReviewMistakeCommand;
import com.mistake.v2.application.service.MistakeApplicationService;
import com.mistake.v2.domain.mistake.Mistake;
import com.mistake.v2.domain.mistake.MistakeFilter;
import com.mistake.v2.domain.shared.enums.MasteryLevel;
import com.mistake.v2.domain.shared.enums.Subject;
import com.mistake.v2.common.response.R;
import com.mistake.v2.interfaces.assembler.MistakeAssembler;
import com.mistake.v2.interfaces.dto.MistakeListVO;
import com.mistake.v2.interfaces.dto.MistakeVO;
import com.mistake.v2.interfaces.dto.ReviewStatsVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 错题管理控制器
 *
 * @author mistake-team
 */
@RestController
@RequestMapping("/mistakes")
@SaCheckLogin
public class MistakeController {

    private final MistakeApplicationService mistakeApplicationService;

    public MistakeController(MistakeApplicationService mistakeApplicationService) {
        this.mistakeApplicationService = mistakeApplicationService;
    }

    /**
     * 创建错题
     */
    @PostMapping
    @SaCheckPermission("mistake:write")
    public R<MistakeVO> create(@Valid @RequestBody CreateMistakeCommand command) {
        command.setUserId(StpUtil.getLoginIdAsLong());
        Mistake mistake = mistakeApplicationService.create(command);
        return R.ok(MistakeAssembler.toVO(mistake));
    }

    /**
     * 分页查询错题列表
     */
    @GetMapping
    public R<MistakeListVO> list(
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String topic,
            @RequestParam(required = false) String mastery,
            @RequestParam(required = false) String questionType,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        Long userId = StpUtil.getLoginIdAsLong();

        MistakeFilter filter = MistakeFilter.builder()
                .subject(subject != null ? Subject.fromDisplayName(subject) : null)
                .topic(topic)
                .mastery(mastery != null ? MasteryLevel.fromCode(mastery) : null)
                .questionType(questionType)
                .keyword(keyword)
                .page(page)
                .pageSize(pageSize)
                .build();

        List<Mistake> mistakes = mistakeApplicationService.listByFilter(userId, filter);
        MistakeListVO vo = MistakeAssembler.toListVO(mistakes, mistakes.size(), filter);
        return R.ok(vo);
    }

    /**
     * 查询错题详情
     */
    @GetMapping("/{id}")
    public R<MistakeVO> getById(@PathVariable Long id) {
        Long userId = StpUtil.getLoginIdAsLong();
        Mistake mistake = mistakeApplicationService.findById(id, userId);
        return R.ok(MistakeAssembler.toVO(mistake));
    }

    /**
     * 更新错题
     */
    @PutMapping("/{id}")
    @SaCheckPermission("mistake:write")
    public R<MistakeVO> update(@PathVariable Long id,
                               @Valid @RequestBody CreateMistakeCommand command) {
        Long userId = StpUtil.getLoginIdAsLong();
        Mistake mistake = mistakeApplicationService.update(id, userId, command);
        return R.ok(MistakeAssembler.toVO(mistake));
    }

    /**
     * 删除错题
     */
    @DeleteMapping("/{id}")
    @SaCheckPermission("mistake:write")
    public R<Void> delete(@PathVariable Long id) {
        Long userId = StpUtil.getLoginIdAsLong();
        mistakeApplicationService.delete(id, userId);
        return R.ok();
    }

    /**
     * 复习错题
     */
    @PostMapping("/{id}/review")
    @SaCheckPermission("mistake:write")
    public R<Void> review(@PathVariable Long id,
                          @Valid @RequestBody ReviewMistakeCommand command) {
        command.setUserId(StpUtil.getLoginIdAsLong());
        command.setMistakeId(id);
        mistakeApplicationService.review(command);
        return R.ok();
    }

    /**
     * 获取待复习错题
     */
    @GetMapping("/review/due")
    public R<MistakeListVO> getDueForReview() {
        Long userId = StpUtil.getLoginIdAsLong();
        List<Mistake> dueList = mistakeApplicationService.getDueForReview(userId);
        List<MistakeVO> voList = dueList.stream()
                .map(MistakeAssembler::toVO)
                .toList();
        MistakeListVO vo = MistakeListVO.builder()
                .list(voList)
                .total(voList.size())
                .page(1)
                .pageSize(voList.size())
                .build();
        return R.ok(vo);
    }

    /**
     * 复习统计
     */
    @GetMapping("/review/stats")
    public R<ReviewStatsVO> getStats() {
        Long userId = StpUtil.getLoginIdAsLong();

        // 统计全部错题
        MistakeFilter allFilter = MistakeFilter.builder()
                .page(1)
                .pageSize(Integer.MAX_VALUE)
                .build();
        List<Mistake> all = mistakeApplicationService.listByFilter(userId, allFilter);

        long total = all.size();
        long mastered = all.stream().filter(Mistake::isMastered).count();
        long dueToday = all.stream().filter(Mistake::isDue).count();
        long reviewing = total - mastered;

        ReviewStatsVO vo = ReviewStatsVO.builder()
                .total(total)
                .mastered(mastered)
                .reviewing(reviewing)
                .dueToday(dueToday)
                .build();
        return R.ok(vo);
    }
}
