package com.aybss.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.io.Serializable;

/**
 * 标签(Tag)实体类
 *
 * @author makejava
 * @since 2022-12-18 22:08:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tag implements Serializable {
    private static final long serialVersionUID = 946114117610465699L;
    
    private Long id;
    /**
     * 标签名
     */
    private String name;
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    /**
     * 删除标志（0代表未删除，1代表已删除）
     */
    private Integer delFlag;
    /**
     * 备注
     */
    private String remark;

}

