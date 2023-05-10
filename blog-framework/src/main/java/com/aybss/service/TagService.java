package com.aybss.service;

import com.aybss.domain.ResponseResult;
import com.aybss.domain.dto.TagDto;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aybss.domain.entity.Tag;

import java.util.List;

/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2022-12-18 22:09:06
 */
public interface TagService extends IService<Tag> {

    ResponseResult getAllTags(Integer pageNum, Integer pageSize, TagDto tagDto);

    ResponseResult addTag(TagDto tagDto);

    ResponseResult deleteTag(List<Integer> ids);

    ResponseResult getTagById(Integer id);

    ResponseResult updateTag(Tag tag);

    ResponseResult listAllTag();
}

