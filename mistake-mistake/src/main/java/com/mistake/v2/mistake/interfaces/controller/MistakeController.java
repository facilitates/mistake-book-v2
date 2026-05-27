package com.mistake.v2.mistake.interfaces.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.mistake.v2.mistake.application.CreateMistakeCommand;
import com.mistake.v2.mistake.application.MistakeService;
import com.mistake.v2.mistake.domain.Mistake;
import com.mistake.v2.mistake.domain.MistakeFilter;
import com.mistake.v2.common.enums.MasteryLevel;
import com.mistake.v2.common.enums.Subject;
import com.mistake.v2.common.response.R;
import com.mistake.v2.mistake.interfaces.assembler.MistakeAssembler;
import com.mistake.v2.mistake.interfaces.dto.MistakeListVO;
import com.mistake.v2.mistake.interfaces.dto.MistakeVO;
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
 * 错题 CRUD 控制器
 *
 * @author mistake-team
 */
@RestController
@RequestMapping("/mistakes")
@SaCheckLogin
public class MistakeController {

    private final MistakeService mistakeService;

    public MistakeController(MistakeService mistakeService) {
        this.mistakeService = mistakeService;
    }

    /**
     * 创建错题
     */
    @PostMapping
    @SaCheckPermission("mistake:write")
    public R<MistakeVO> create(@Valid @RequestBody CreateMistakeCommand command) {
        command.setUserId(StpUtil.getLoginIdAsLong());
        Mistake mistake = mistakeService.create(command);
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

        List<Mistake> mistakes = mistakeService.listByFilter(userId, filter);
        MistakeListVO vo = MistakeAssembler.toListVO(mistakes, mistakes.size(), filter);
        return R.ok(vo);
    }

    /**
     * 查询错题详情
     */
    @GetMapping("/{id}")
    public R<MistakeVO> getById(@PathVariable Long id) {
        Long userId = StpUtil.getLoginIdAsLong();
        Mistake mistake = mistakeService.findById(id, userId);
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
        Mistake mistake = mistakeService.update(id, userId, command);
        return R.ok(MistakeAssembler.toVO(mistake));
    }

    /**
     * 删除错题
     */
    @DeleteMapping("/{id}")
    @SaCheckPermission("mistake:write")
    public R<Void> delete(@PathVariable Long id) {
        Long userId = StpUtil.getLoginIdAsLong();
        mistakeService.delete(id, userId);
        return R.ok();
    }
}
