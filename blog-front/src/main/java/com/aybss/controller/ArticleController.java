package com.aybss.controller;

import com.aybss.annotation.SystemLog;
import com.aybss.domain.ResponseResult;
import com.aybss.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
@Api(tags = "文章",description = "文章相关API")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

//    @GetMapping("/list")
//    public List<Article> list(){
//        List<Article> list = articleService.list();
//        return list;
//    }

    /**
     * 查询浏览量最高的前10篇文章的信息，要求展示文章标题和浏览量
     * @return
     */
    @GetMapping("/hotArticleList")
    @ApiOperation(value = "热点文章", notes = "阅读次数最多的文章")
    public ResponseResult hotArticleList(){
        return articleService.hotArticleList();
    }

    @GetMapping("/articleList")
    @SystemLog(businessName = "获取文章列表")
    @ApiOperation(value = "文章列表", notes = "获取文章列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "页号"),
            @ApiImplicitParam(name = "pageSize",value = "页大小"),
            @ApiImplicitParam(name = "categoryId", value = "分类Id")
    })
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId ){
        return articleService.articleList(pageNum,pageSize,categoryId);
    }

    @GetMapping("/{id}")
    @SystemLog(businessName = "获取文章详情")
    public ResponseResult articleDetails(@PathVariable("id") Integer id){
        return articleService.articleDetails(id);
    }

    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable("id") Integer id){
        return articleService.updateViewCount(id);
    }

}
