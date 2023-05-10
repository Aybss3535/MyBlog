package com.aybss.service.impl;

import com.aybss.constants.SystemConstants;
import com.aybss.domain.ResponseResult;
import com.aybss.domain.vo.CommentVo;
import com.aybss.domain.vo.PageVo;
import com.aybss.enums.AppHttpCodeEnum;
import com.aybss.exception.SystemException;
import com.aybss.service.UserService;
import com.aybss.utils.BeanCopyUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aybss.mapper.CommentMapper;
import com.aybss.domain.entity.Comment;
import com.aybss.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2022-12-15 10:30:01
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserService userService;

    @Override
    public ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize) {
        //从comment中查询
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<Comment>();
        //如果是对文章的评论的话，查询对应文章的评论
        wrapper.eq(SystemConstants.COMMENT_TYPE_ARTICLE.equals(commentType),Comment::getArticleId,articleId);
        //类型为commentType
        wrapper.eq(Comment::getType,commentType);
        //降序
        wrapper.orderByAsc(Comment::getCreateTime);
        //查询根评论，即rootId为-1
        wrapper.eq(Comment::getRootId, SystemConstants.COMMENT_STATUS_ROOT);
        Page<Comment> page = new Page<>(pageNum,pageSize);
        commentMapper.selectPage(page,wrapper);
        //使用user表查询username和toCommentUserName字段，并封装
        List<CommentVo> commentVos = getCommentVos(page.getRecords());
        //查询子评论
        for (CommentVo commentVo : commentVos) {
            List<CommentVo> children = getChindren(commentVo.getId());
            commentVo.setChildren(children);
        }
        return ResponseResult.okResult(new PageVo(commentVos,page.getTotal()));
    }

    @Override
    public ResponseResult addComment(Comment comment) {
        if(!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_CONTENT);
        }
        save(comment);
        return ResponseResult.okResult();
    }

    /**
     * 根据根评论id查询子评论
     * @param rootId 根评论Id
     * @return
     */
    public List<CommentVo> getChindren(Long rootId){
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Comment::getCreateTime);
        wrapper.eq(Comment::getRootId,rootId);
        List<Comment> comments = commentMapper.selectList(wrapper);
        List<CommentVo> children = getCommentVos(comments);
        return children;
    }

    public List<CommentVo> getCommentVos(List<Comment> list){
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        for (CommentVo commentVo:commentVos){
            commentVo.setUsername(userService.getById(commentVo.getCreateBy()).getUserName());
            if(commentVo.getRootId()!=-1){
                commentVo.setToCommentUserName(userService.getById(commentVo.getToCommentUserId()).getUserName());
            }
        }
        return commentVos;
    }
}

