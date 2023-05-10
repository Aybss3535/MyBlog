package com.aybss.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryVo {
    @ExcelProperty("id")
    private Long id;
    /**
     * 分类名
     */
    @ExcelProperty("分类名")
    private String name;
    /**
     * 描述
     */
    @ExcelProperty("描述")
    private String description;
    /**
     * 状态0:正常,1禁用
     */
    @ExcelProperty("状态")
    private String status;

}
