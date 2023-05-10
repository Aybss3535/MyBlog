package com.aybss.domain.vo;

import com.aybss.domain.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleMenuTreeVo {

    /**
     * 所有的menu
     */
    private List<MenuTreeVo> menus;
    /**
     * 该角色关联的menuId
     */
    private List<Long> checkedKeys;

}
