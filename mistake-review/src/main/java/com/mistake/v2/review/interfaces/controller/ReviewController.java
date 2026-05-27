package com.mistake.v2.review.interfaces.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.mistake.v2.mistake.domain.Mistake;
import com.mistake.v2.review.application.ReviewMistakeCommand;
import com.mistake.v2.review.application.ReviewService;
import com.mistake.v2.review.application.ReviewService.ReviewStatsResult;
import com.mistake.v2.common.response.R;
import com.mistake.v2.mistake.interfaces.assembler.MistakeAssembler;
import com.mistake.v2.mistake.interfaces.dto.MistakeListVO;
import com.mistake.v2.mistake.interfaces.dto.MistakeVO;
import com.mistake.v2.review.interfaces.dto.ReviewStatsVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 复习控制器 — SM-2 间隔重复
 *
 * @author mistake-team
 */
@RestController
@RequestMapping("/mistakes/review")
@SaCheckLogin
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * 复习评分
     */
    @PostMapping("/{id}/review")
    @SaCheckPermission("mistake:write")
    public R<Void> review(@PathVariable Long id,
                          @Valid @RequestBody ReviewMistakeCommand command) {
        command.setUserId(StpUtil.getLoginIdAsLong());
        command.setMistakeId(id);
        reviewService.review(command);
        return R.ok();
    }

    /**
     * 获取待复习错题
     */
    @GetMapping("/due")
    public R<MistakeListVO> getDueForReview() {
        Long userId = StpUtil.getLoginIdAsLong();
        List<Mistake> dueList = reviewService.getDueForReview(userId);
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
    @GetMapping("/stats")
    public R<ReviewStatsVO> getStats() {
        Long userId = StpUtil.getLoginIdAsLong();
        ReviewStatsResult stats = reviewService.getStats(userId);

        ReviewStatsVO vo = ReviewStatsVO.builder()
                .total(stats.total())
                .mastered(stats.mastered())
                .reviewing(stats.reviewing())
                .dueToday(stats.dueToday())
                .build();
        return R.ok(vo);
    }
}
