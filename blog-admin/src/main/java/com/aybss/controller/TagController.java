package com.aybss.controller;

import com.aybss.domain.ResponseResult;
import com.aybss.domain.dto.TagDto;
import com.aybss.domain.entity.Tag;
import com.aybss.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * 标签列表,根据pageNum和pageSize进行分页查询，按照tagDto中标签的名字和备注进行模糊查询
     * @param pageNum
     * @param pageSize
     * @param tagDto
     * @return
     */
    @GetMapping("/list")
    public ResponseResult getAllTags(Integer pageNum, Integer pageSize, TagDto tagDto){
        return tagService.getAllTags(pageNum,pageSize, tagDto);
    }

    /**
     * 新增标签
     * @param tagDto
     * @return
     */
    @PostMapping()
    public ResponseResult addTag(@RequestBody TagDto tagDto){
        return tagService.addTag(tagDto);
    }

    /**
     * 删除标签
     * @param ids
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult deleteTag(@PathVariable("id") List<Integer> ids){
        return tagService.deleteTag(ids);
    }

    /**
     * 根据id获取标签
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult getTagById(@PathVariable("id") Integer id){
        return tagService.getTagById(id);
    }

    /**
     * 修改标签
     * @param tag
     * @return
     */
    @PutMapping()
    public ResponseResult updateTag(@RequestBody Tag tag){
        return tagService.updateTag(tag);
    }

    /**
     * 查找所有的标签（不分页）
     * @return
     */
    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        return tagService.listAllTag();
    }

}
