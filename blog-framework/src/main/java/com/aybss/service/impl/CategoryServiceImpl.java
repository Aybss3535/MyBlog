package com.aybss.service.impl;

import com.aybss.constants.SystemConstants;
import com.aybss.domain.ResponseResult;
import com.aybss.domain.dto.CategoryDto;
import com.aybss.domain.entity.Article;
import com.aybss.domain.vo.CategoryVo;
import com.aybss.domain.vo.CategoryListVo;
import com.aybss.domain.vo.PageVo;
import com.aybss.service.ArticleService;
import com.aybss.utils.BeanCopyUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aybss.mapper.CategoryMapper;
import com.aybss.domain.entity.Category;
import com.aybss.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2022-12-11 21:29:09
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private ArticleService articleService;

    @Override
    public ResponseResult getCategoryList() {
        //从article中查询非草稿文章
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper();
        wrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articles = articleService.list(wrapper);
        //取出文章中的分类，并去重
        Set<Long> categoryId = articles.stream().map(o -> o.getCategoryId()).collect(Collectors.toSet());
        if(categoryId == null || categoryId.size()==0) return ResponseResult.okResult();
        //查找category表，判断是否有效
        List<Category> categories = listByIds(categoryId);
        categories = categories.stream().filter(o -> o.getStatus().equals(SystemConstants.CATEGORY_STATUS_NORMAL)).collect(Collectors.toList());
        //封装结果返回
        List<CategoryListVo> categoryListVos = BeanCopyUtils.copyBeanList(categories, CategoryListVo.class);
        return ResponseResult.okResult(categoryListVos);
    }

    @Override
    public ResponseResult getAllCategories(Integer pageNum, Integer pageSize, CategoryDto categoryDto) {
        //分页查询
        Page<Category> page = new Page<>(pageNum,pageSize);
        //按条件搜索
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(categoryDto.getName()),Category::getName,categoryDto.getName());
        wrapper.eq(StringUtils.hasText(categoryDto.getStatus()),Category::getStatus,categoryDto.getStatus());
        //封装结果，返回
        page(page,wrapper);
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(page.getRecords(), CategoryVo.class);
        return ResponseResult.okResult(new PageVo(categoryVos,page.getTotal()));
    }

    @Override
    public ResponseResult getCategoryById(Integer id) {
        Category category = getById(id);
        CategoryVo categoryVo = BeanCopyUtils.copyBean(category, CategoryVo.class);
        return ResponseResult.okResult(categoryVo);
    }

    @Override
    public ResponseResult updateCategory(Category category) {
        updateById(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addCategory(Category category) {
        save(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteCategory(List<Integer> ids) {
        removeByIds(ids);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllCategory() {
        List<Category> categories = list();
        List<CategoryListVo> categoryListVos = BeanCopyUtils.copyBeanList(categories, CategoryListVo.class);
        return ResponseResult.okResult(categoryListVos);
    }

}

