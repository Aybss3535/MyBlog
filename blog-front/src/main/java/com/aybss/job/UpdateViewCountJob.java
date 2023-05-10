package com.aybss.job;

import com.aybss.constants.SystemConstants;
import com.aybss.domain.entity.Article;
import com.aybss.service.ArticleService;
import com.aybss.utils.RedisCache;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
/**
 * 定时任务类
 */
public class UpdateViewCountJob {

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleService articleService;

    /**
     * 每10分钟把viewCount数据存入数据库
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void updateviewCount(){
//        //从redis中查询浏览量
//        Map<String,Integer> viewCounts = redisCache.getCacheMap(SystemConstants.REDIS_KEY_VIEWCOUNT);
//        //批量更新
//        List<Article> articles = viewCounts.entrySet().stream().map(o -> new Article(Long.parseLong(o.getKey()), o.getValue().longValue())).collect(Collectors.toList());
//        articleService.updateBatchById(articles);

        //从redis中取出浏览量
        Set<DefaultTypedTuple<String>> viewCountTuples = redisCache.getSortedSetTuples(SystemConstants.REDIS_KEY_VIEWCOUNT, 0, -1);
        List<Article> articles = viewCountTuples.stream().map(o -> new Article(Long.parseLong(o.getValue()), o.getScore().longValue())).collect(Collectors.toList());
        articleService.updateBatchById(articles);

    }

}
