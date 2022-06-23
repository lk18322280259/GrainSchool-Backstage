package com.atguigu.eduservice.controller.front;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.ordervo.UcenterMemberCommon;
import com.atguigu.eduservice.client.UcenterClient;
import com.atguigu.eduservice.entity.EduComment;
import com.atguigu.eduservice.service.EduCommentService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论 前端控制器
 * </p>
 *
 * @author luokai
 * @since 2022-06-23
 */
@SuppressWarnings({"AlibabaCollectionInitShouldAssignCapacity", "unchecked"})
@RestController
@RequestMapping("/eduservice/comment")
@CrossOrigin
public class CommentFrontController {

    @Autowired
    private EduCommentService commentService;
    @Autowired
    private UcenterClient ucenterClient;


    /**
     * 获取用户信息
     * @param request 请求对象
     * @return 成功
     */
    @ApiOperation(value = "获取用户信息")
    @GetMapping("getUserInfo")
    public R getAvatar(HttpServletRequest request) {

        String memberId = JwtUtils.getMemberIdByJwtToken(request);

        if (StringUtils.isEmpty(memberId)) {
            return R.ok().data("userInfo","");
        }
        UcenterMemberCommon memberCommon = ucenterClient.getUserInfo(memberId);

        String nickname = memberCommon.getNickname();
        String avatar = memberCommon.getAvatar();

        Map<String, String> userMap = new HashMap<>();
        userMap.put("name", nickname);
        userMap.put("avatar", avatar);

        return R.ok().data("userInfo",userMap);
    }

    /**
     * 根据课程id查询评论列表
     * @param page 分页
     * @param limit 每页记录数
     * @param courseId 课程id
     * @return 分页数据
     */
    @ApiOperation(value = "评论分页列表")
    @GetMapping("{page}/{limit}")
    public R index(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,
            //required 默认为false
            @ApiParam(name = "courseQuery", value = "查询对象")
            String courseId) {
        Page<EduComment> pageParam = new Page<>(page, limit);

        LambdaQueryWrapper<EduComment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EduComment::getCourseId, courseId);
        queryWrapper.orderByDesc(EduComment::getGmtCreate);


        commentService.page(pageParam,queryWrapper);
        List<EduComment> commentList = pageParam.getRecords();

        Map<String, Object> map = new HashMap<>();
        map.put("items", commentList);
        map.put("current", pageParam.getCurrent());
        map.put("pages", pageParam.getPages());
        map.put("size", pageParam.getSize());
        map.put("total", pageParam.getTotal());
        map.put("hasNext", pageParam.hasNext());
        map.put("hasPrevious", pageParam.hasPrevious());
        return R.ok().data(map);
    }

    /**
     * 添加评论
     * @param comment 评论对象
     * @param request 请求对象
     * @return 成功
     */
    @ApiOperation(value = "添加评论")
    @PostMapping("auth/save")
    public R save(@RequestBody EduComment comment, HttpServletRequest request) {
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(memberId)) {
            return R.error().code(28004).message("请登录");
        }
        if(StringUtils.isEmpty(comment.getContent())) {
            throw new GuliException(20001,"评论内容不能为空");
        }
        comment.setMemberId(memberId);

        UcenterMemberCommon memberCommon = ucenterClient.getUserInfo(memberId);

        String name = memberCommon.getNickname();
        String avatar = memberCommon.getAvatar();

        comment.setNickname(name);
        comment.setAvatar(avatar);

        commentService.save(comment);
        return R.ok();
    }
}

