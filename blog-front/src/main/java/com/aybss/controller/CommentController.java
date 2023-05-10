package com.aybss.controller;

import com.aybss.constants.SystemConstants;
import com.aybss.domain.ResponseResult;
import com.aybss.domain.entity.Comment;
import com.aybss.enums.AppHttpCodeEnum;
import com.aybss.exception.SystemException;
import com.aybss.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/commentList")
    public ResponseResult commentList(Long articleId,Integer pageNum,Integer pageSize){
        return commentService.commentList(SystemConstants.COMMENT_TYPE_ARTICLE,articleId, pageNum, pageSize);
    }

    @PostMapping()
    public ResponseResult comment(@RequestBody Comment comment){
        return commentService.addComment(comment);
    }

    @GetMapping("/linkCommentList")
    public ResponseResult commentList(Integer pageNum,Integer pageSize){
        return commentService.commentList(SystemConstants.COMMENT_TYPE_LINK,null, pageNum, pageSize);
    }

}
