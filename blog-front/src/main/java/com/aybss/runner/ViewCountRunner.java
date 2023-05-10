package com.aybss.runner;

import com.aybss.constants.SystemConstants;
import com.aybss.domain.entity.Article;
import com.aybss.service.ArticleService;
import com.aybss.utils.RedisCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.querydsl.QuerydslUtils;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
/**
 * 在应用启动时把博客的浏览器存储到redis中
 */
public class ViewCountRunner implements CommandLineRunner {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Article::getViewCount,Article::getId);
        queryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articles = articleService.list(queryWrapper);
        //以map形式存储浏览量
//        Map<Long,Long> viewCount = new HashMap<>();
//        articles.stream().forEach(o->viewCount.put(o.getId(),o.getViewCount()));
        Set<DefaultTypedTuple<String>> viewCountTuples = articles.stream().map(o -> new DefaultTypedTuple<String>(o.getId().toString(), o.getViewCount().doubleValue())).collect(Collectors.toSet());
//        Map<String, Integer> viewCountMap = articles.stream().collect(Collectors.toMap(o -> o.getId().toString(), o -> o.getViewCount().intValue()));
//        redisCache.setCacheMap(SystemConstants.REDIS_KEY_VIEWCOUNT,viewCountMap);
        redisCache.setSortedSetTuples(SystemConstants.REDIS_KEY_VIEWCOUNT,viewCountTuples);
    }
}
