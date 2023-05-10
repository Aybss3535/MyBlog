package com.aybss.service;

import com.aybss.domain.ResponseResult;
import com.aybss.domain.dto.CategoryDto;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aybss.domain.entity.Category;

import java.util.List;

/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2022-12-11 21:27:29
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    ResponseResult getAllCategories(Integer pageNum, Integer pageSize, CategoryDto categoryDto);

    ResponseResult getCategoryById(Integer id);

    ResponseResult updateCategory(Category category);

    ResponseResult addCategory(Category category);

    ResponseResult deleteCategory(List<Integer> ids);

    ResponseResult listAllCategory();
}

