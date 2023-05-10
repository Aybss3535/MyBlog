package com.aybss.service;

import com.aybss.domain.ResponseResult;
import com.aybss.domain.dto.ArticleDto;
import com.aybss.domain.dto.ArticleListDto;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aybss.domain.entity.Article;

import java.util.List;

/**
 * 文章表(Article)表服务接口
 *
 * @author makejava
 * @since 2022-12-09 20:01:55
 */
public interface ArticleService extends IService<Article> {

    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult articleDetails(Integer id);

    ResponseResult updateViewCount(Integer id);

    ResponseResult addArticle(ArticleDto articleDto);

    ResponseResult getAllArticles(Integer pageNum, Integer pageSize, ArticleListDto articleListDto);

    ResponseResult deleteArticle(List<Integer> ids);

    ResponseResult getArticleById(Integer id);

    ResponseResult updateArticle(ArticleDto articleDto);
}

