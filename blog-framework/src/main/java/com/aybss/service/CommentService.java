package com.aybss.service;

import com.aybss.domain.ResponseResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aybss.domain.entity.Comment;

/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2022-12-15 10:30:01
 */
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}

