package com.aybss.service.impl;

import com.aybss.constants.SystemConstants;
import com.aybss.domain.dto.ArticleDto;
import com.aybss.domain.dto.ArticleListDto;
import com.aybss.domain.entity.Article;
import com.aybss.domain.ResponseResult;
import com.aybss.domain.entity.ArticleTag;
import com.aybss.domain.entity.Category;
import com.aybss.domain.vo.ArticleDetailsVo;
import com.aybss.domain.vo.ArticleListVo;
import com.aybss.domain.vo.HotArticleVo;
import com.aybss.domain.vo.PageVo;
import com.aybss.enums.AppHttpCodeEnum;
import com.aybss.exception.SystemException;
import com.aybss.mapper.ArticleMapper;
import com.aybss.mapper.CategoryMapper;
import com.aybss.service.ArticleService;
import com.aybss.service.ArticleTagService;
import com.aybss.utils.BeanCopyUtils;
import com.aybss.utils.RedisCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleTagService articleTagService;

    @Override
    public ResponseResult hotArticleList() {
//        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
//        //不能是草稿
//        wrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
//        //降序排序，这个不是实时更新的排序
////        wrapper.orderByDesc(Article::getViewCount);
//        //前10篇文章
//        IPage<Article> page = new Page<>(1,10);
//        articleMapper.selectPage(page,wrapper);
//        List<Article> articles = page.getRecords();
//        //更新文章浏览量
//        setViewCountList(articles);
//        //根据Redis里的浏览量进行排序
//        Collections.sort(articles, (article1, article2) -> (int) (article2.getViewCount()-article1.getViewCount()));
//        //bean拷贝
//        List<HotArticleVo> hotArticleVos = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);
//        //封装结果并返回

        //从redis中查询阅读量
        Set<DefaultTypedTuple<String>> viewCountTuple = redisCache.getSortedSetTuplesByReverse(SystemConstants.REDIS_KEY_VIEWCOUNT, 0, 19);
        List<Article> articles = viewCountTuple.stream().map(o -> new Article(Long.parseLong(o.getValue()), o.getScore().longValue())).collect(Collectors.toList());
        List<Long> ids = viewCountTuple.stream().map(o -> Long.parseLong(o.getValue())).collect(Collectors.toList());
        //从数据库中查询这些top的文章的标题
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        //必须正式文章
        wrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        wrapper.in(Article::getId, ids);
        List<Article> articleList = articleMapper.selectList(wrapper);
        //获得id到title的映射
        Map<Long, String> titles = articleList.stream().collect(Collectors.toMap(o -> o.getId(), o -> o.getTitle()));
        //封装结果
        List<HotArticleVo> hotArticleVos = new ArrayList<>();
        for (Article article : articles) {
            if(titles.containsKey(article.getId())){
                hotArticleVos.add(new HotArticleVo(article.getId(),titles.get(article.getId()),article.getViewCount()));
            }
        }
        return ResponseResult.okResult(hotArticleVos);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //查询Article表
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        //如果categoryId非空且为正数，表示要查具体分类Id下的文章
        wrapper.eq(ObjectUtils.isNotEmpty(categoryId) && categoryId>0,Article::getCategoryId,categoryId);
        //要将置顶的文章放在前面（安装isTop字段降序排序)
        wrapper.orderByDesc(Article::getIsTop);
        //查询正式发布的文章
        wrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        //分页查询
        Page<Article> page = new Page<>(pageNum,pageSize);
        articleMapper.selectPage(page, wrapper);
        List<Article> articles = page.getRecords();
        //更新文章浏览量
        setViewCountList(articles);
        //从category表中查询分类名称
//        for(Article article:articles){
//            Long articleCategoryId = article.getCategoryId();
//            article.setCategoryName(categoryMapper.selectById(articleCategoryId).getName());
//        }
        articles.stream().map(article -> article.setCategoryName(categoryMapper.selectById(article.getCategoryId()).getName())).collect(Collectors.toList());
        //封装结果，返回
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articles, ArticleListVo.class);
        PageVo pageVo = new PageVo(articleListVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult articleDetails(Integer id) {
        //根据文章id查询文章
        Article article = articleMapper.selectById(id);
        //对文章进行判空
        if(Objects.isNull(article)){
            throw new SystemException(AppHttpCodeEnum.ARTICLE_NOT_EXIST);
        }
        //更新文章浏览量
        setViewCount(article);
        //查询文章的类别名
        Category category = categoryMapper.selectById(article.getCategoryId());
        article.setCategoryName(category.getName());
        ArticleDetailsVo articleDetailsVo = BeanCopyUtils.copyBean(article, ArticleDetailsVo.class);
        return ResponseResult.okResult(articleDetailsVo);
    }

    /**
     * 更新文章的阅读量
     * @param id
     * @return
     */
    @Override
    public ResponseResult updateViewCount(Integer id) {
        //更新redis中对应 id的浏览量
//        redisCache.incrementCacheMapValue(SystemConstants.REDIS_KEY_VIEWCOUNT,id.toString(),1);
        try {
            redisCache.incrementSortedSetScore(SystemConstants.REDIS_KEY_VIEWCOUNT,id.toString(),1);
        }catch (Exception e){
            log.info("this article's viewCount is not in the redis");
            Article article = articleMapper.selectById(id);
            redisCache.addSortedSet(SystemConstants.REDIS_KEY_VIEWCOUNT,article.getId().toString(),article.getViewCount().doubleValue());
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addArticle(ArticleDto articleDto) {
        //保存文章
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        save(article); //默认会有主键回填
        //保存标签
        List<Long> tags = articleDto.getTags();
        List<ArticleTag> articleTags = tags.stream().map(o -> new ArticleTag(article.getId(), o)).collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);
//        redisCache.addSortedSet(SystemConstants.REDIS_KEY_VIEWCOUNT,article.getId().toString(),0);
        //返回
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getAllArticles(Integer pageNum, Integer pageSize, ArticleListDto articleListDto) {
        //分页查询
        Page<Article> page = new Page<>(pageNum,pageSize);
        //根据文章名或摘要进行模糊查询
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.hasText(articleListDto.getTitle()),Article::getTitle,articleListDto.getTitle());
        lambdaQueryWrapper.like(StringUtils.hasText(articleListDto.getSummary()),Article::getSummary,articleListDto.getSummary());
        //查询
        page(page, lambdaQueryWrapper);
        //封装结果，返回
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);
        return ResponseResult.okResult(new PageVo(articleListVos,page.getTotal()));
    }

    @Override
    public ResponseResult deleteArticle(List<Integer> ids) {
        removeByIds(ids);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getArticleById(Integer id) {
        return ResponseResult.okResult(getById(id));
    }

    @Override
    public ResponseResult updateArticle(ArticleDto articleDto) {
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        updateById(article);
        return ResponseResult.okResult();
    }


    /**
     * 数据库里查到的浏览量不准确，通过查询redis对文章的浏览量进行更新
     * @param articles 要更新的文章列表
     */
    private void setViewCountList(List<Article> articles){
        articles.stream().forEach(o->setViewCount(o));
    }

    private void setViewCount(Article article){
//        Integer viewCount = redisCache.getCacheMapValue(SystemConstants.REDIS_KEY_VIEWCOUNT, article.getId().toString());
//        article.setViewCount(viewCount.longValue());
        try {
            Double viewCount = redisCache.getSortedSetScoreByValue(SystemConstants.REDIS_KEY_VIEWCOUNT, article.getId().toString());
            article.setViewCount(viewCount.longValue());
        }catch (Exception e){
            log.info("this article's viewCount is not in the redis");
            redisCache.addSortedSet(SystemConstants.REDIS_KEY_VIEWCOUNT,article.getId().toString(),article.getViewCount());
        }

    }


}
