package com.aybss;

import com.aybss.annotation.SystemLog;
import com.aybss.constants.SystemConstants;
import com.aybss.domain.entity.Article;
import com.aybss.domain.entity.User;
import com.aybss.domain.vo.HotArticleVo;
import com.aybss.mapper.ArticleMapper;
import com.aybss.mapper.UserMapper;
import com.aybss.service.ArticleService;
import com.aybss.utils.BeanCopyUtils;
import com.aybss.utils.JwtUtil;
import com.aybss.utils.RedisCache;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
//@ConfigurationProperties(prefix = "oss")
public class ApplicationTest {


    private String accessKey;
    private String secretKey;
    private String bucket;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    @Test
    public void testOSS(){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.huanan());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
//...其他参数参考类注释

        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
//        String accessKey = "your access key";
//        String secretKey = "your secret key";
//        String bucket = "your bucket name";

//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;

        try {
//            byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
////            ByteArrayInputStream byteInputStream=new ByteArrayInputStream(uploadBytes);
            InputStream inputStream = new FileInputStream("D:\\picture\\头像\\微信图片_20221111192921.jpg");
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            try {
                Response response = uploadManager.put(inputStream,key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception ex) {
            //ignore
        }

    }

    @Test
    public void testDate(){
        System.out.println(new Date());
    }

    @Autowired
    private RedisCache redisCache;

    @Test
    public void setPasword(){
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getUserName,"噜啦噜啦嘞");
//        wrapper.set(User::getPassword,"1234");
        User user = new User();
//        bCryptPasswordEncoder.encode("1234");
        user.setPassword(bCryptPasswordEncoder.encode("WSX980912"));
        userMapper.update(user,wrapper);
    }

//    @Test
//    public void testRedis(){
//        Map<String,Long> map = new HashMap<>();
//        map.put("1",1L);
//        map.put("2",2L);
//        redisCache.setCacheMap("map",map);
//    }
//
//    @Test
//    public void testRedis02(){
//        Map map = redisCache.getCacheObject("map");
//        System.out.println(map.get("key1"));
//    }
//
//    @Autowired
//    private ArticleService articleService;
//
//    @Test
//    public void testSchedule(){
//        //从redis中查询浏览量
//        Map<String,String> viewCounts = redisCache.getCacheObject("viewCount");
//        for (Map.Entry<String, String> entry : viewCounts.entrySet()) {
//            LambdaUpdateWrapper<Article> wrapper = new LambdaUpdateWrapper<>();
//            wrapper.set(Article::getViewCount,Long.parseLong(entry.getValue())+1);
//            wrapper.eq(Article::getId,Long.parseLong(entry.getKey()));
//            articleService.update(wrapper);
//        }
//    }

    @Autowired
    private ArticleService articleService;
    @Test
    public void testUpdate(){
        List<Article> articles = new ArrayList<>();
        articles.add(new Article(1l,150l));
        articles.add(new Article(2l,25l));
        articles.add(new Article(20l,30l));
        articles.add(new Article(19l,4l));
        articleService.updateBatchById(articles);
    }

    @Autowired
    private ArticleMapper articleMapper;

    @Test
    public void testHotArticleList(){
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
        List<HotArticleVo> hotArticleVos = new ArrayList<>();
        for (Article article : articles) {
            if(titles.containsKey(article.getId())){
                hotArticleVos.add(new HotArticleVo(article.getId(),titles.get(article.getId()),article.getViewCount()));
            }
        }
        System.out.println(hotArticleVos);
    }

    @Test
    public void testJwt(){
        String jwt = JwtUtil.createJWT("1");
        System.out.println(jwt);
    }


}
