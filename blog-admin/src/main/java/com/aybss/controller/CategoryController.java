package com.aybss.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.aybss.domain.ResponseResult;
import com.aybss.domain.dto.CategoryDto;
import com.aybss.domain.entity.Category;
import com.aybss.domain.vo.CategoryVo;
import com.aybss.enums.AppHttpCodeEnum;
import com.aybss.service.CategoryService;
import com.aybss.utils.BeanCopyUtils;
import com.aybss.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController()
@RequestMapping("/content/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public ResponseResult getAllCategories(Integer pageNum, Integer pageSize, CategoryDto categoryDto){
        return categoryService.getAllCategories(pageNum, pageSize, categoryDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getCategoryById(@PathVariable("id") Integer id){
        return categoryService.getCategoryById(id);
    }

    @PutMapping()
    public ResponseResult updateCategory(@RequestBody Category category){
        return categoryService.updateCategory(category);
    }

    @PostMapping()
    public ResponseResult addCategory(@RequestBody Category category){
        return categoryService.addCategory(category);
    }

    @DeleteMapping("{id}")
    public ResponseResult deleteCategory(@PathVariable("id") List<Integer> ids){
        return categoryService.deleteCategory(ids);
    }

    @GetMapping("/export")
    @PreAuthorize("@ps.hasPermission('content:category:export')")
    public void exportCategory(HttpServletResponse response){
        try {
            //设置请求头
            WebUtils.setDownLoadHeader("分类",response);
            //从数据库中查找数据
            List<Category> categories = categoryService.list();
            List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), CategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("sheet 1")
                    .doWrite(categoryVos);
        } catch (IOException e) {
            e.printStackTrace();
            //异常
            response.reset();
            ResponseResult responseResult = ResponseResult.errorResult(AppHttpCodeEnum.DOWNLOAD_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(responseResult));
        }
    }

    /**
     * 查找所有的分类（不分页）
     * @return
     */
    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        return categoryService.listAllCategory();
    }

}
