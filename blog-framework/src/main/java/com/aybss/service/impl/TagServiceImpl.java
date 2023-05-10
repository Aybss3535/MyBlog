package com.aybss.service.impl;

import com.aybss.domain.ResponseResult;
import com.aybss.domain.dto.TagDto;
import com.aybss.domain.vo.PageVo;
import com.aybss.domain.vo.TagVo;
import com.aybss.enums.AppHttpCodeEnum;
import com.aybss.exception.SystemException;
import com.aybss.utils.BeanCopyUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aybss.mapper.TagMapper;
import com.aybss.domain.entity.Tag;
import com.aybss.service.TagService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2022-12-18 22:09:06
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {


    @Override
    public ResponseResult getAllTags(Integer pageNum, Integer pageSize, TagDto tagDto) {
        //分页查询
        Page<Tag> page = new Page<>(pageNum,pageSize);
        //根据名称或备注查询
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper();
        wrapper.like(StringUtils.hasText(tagDto.getName()),Tag::getName, tagDto.getName());
        wrapper.like(StringUtils.hasText(tagDto.getRemark()),Tag::getRemark, tagDto.getRemark());
        //封装结果并返回
        page(page,wrapper);
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(page.getRecords(), TagVo.class);
        return ResponseResult.okResult(new PageVo(tagVos,page.getTotal()));
    }

    @Override
    public ResponseResult addTag(TagDto tagDto) {
        //标签名判空
        if(!StringUtils.hasText(tagDto.getName())){
            throw new SystemException(AppHttpCodeEnum.TAG_REQUIRE_NAME);
        }
        Tag tag = BeanCopyUtils.copyBean(tagDto, Tag.class);
        save(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteTag(List<Integer> ids) {
        if(ids.size()<=0){
            throw new SystemException(AppHttpCodeEnum.TAG_REQUIRE_ID);
        }
        removeByIds(ids);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getTagById(Integer id) {
        Tag tag = getById(id);
        TagVo tagVo = BeanCopyUtils.copyBean(tag, TagVo.class);
        return ResponseResult.okResult(tagVo);
    }

    @Override
    public ResponseResult updateTag(Tag tag) {
        updateById(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllTag() {
        List<Tag> tags = list();
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(tags, TagVo.class);
        return ResponseResult.okResult(tagVos);
    }
}

