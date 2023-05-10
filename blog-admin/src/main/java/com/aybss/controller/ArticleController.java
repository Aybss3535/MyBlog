package com.aybss.controller;

import com.aybss.domain.ResponseResult;
import com.aybss.domain.dto.ArticleDto;
import com.aybss.domain.dto.ArticleListDto;
import com.aybss.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 添加文章
     * @param articleDto
     * @return
     */
    @PostMapping()
    public ResponseResult addArticle(@RequestBody ArticleDto articleDto){
        return articleService.addArticle(articleDto);
    }

    /**
     * 分页查询出所有的文章，按照articleListDto里的标题和摘要进行模糊查询
     * @param pageNum
     * @param pageSize
     * @param articleListDto
     * @return
     */
    @GetMapping("/list")
    public ResponseResult getAllArticles(Integer pageNum, Integer pageSize, ArticleListDto articleListDto){
        return articleService.getAllArticles(pageNum, pageSize,articleListDto);
    }

    /**
     * 删除文章
     * @param ids
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult deleteArticle(@PathVariable("id") List<Integer> ids){
        return articleService.deleteArticle(ids);
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult getArticleById(@PathVariable("id") Integer id){
        return articleService.getArticleById(id);
    }

    /**
     * 修改文章
     * @param article
     * @return
     */
    @PutMapping()
    public ResponseResult updateArticle(@RequestBody ArticleDto article){
        return articleService.updateArticle(article);
    }

}
