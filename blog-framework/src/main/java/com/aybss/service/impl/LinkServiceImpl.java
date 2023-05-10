package com.aybss.service.impl;

import com.aybss.constants.SystemConstants;
import com.aybss.domain.ResponseResult;
import com.aybss.domain.dto.LinkDto;
import com.aybss.domain.vo.LinkVo;
import com.aybss.domain.vo.PageVo;
import com.aybss.utils.BeanCopyUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aybss.mapper.LinkMapper;
import com.aybss.domain.entity.Link;
import com.aybss.service.LinkService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2022-12-12 12:04:03
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {



    @Override
    public ResponseResult getAllLink() {
        //查询Link表中审核通过的友链
        LambdaQueryWrapper<Link> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        List<Link> links = list(wrapper);
        //生成vo
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(links, LinkVo.class);
        return ResponseResult.okResult(linkVos);
    }

    @Override
    public ResponseResult getAllLinks(Integer pageNum, Integer pageSize, LinkDto linkDto) {
        //分页查询
        Page<Link> page = new Page<>(pageNum,pageSize);
        //根据名称或状态查询
        LambdaQueryWrapper<Link> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(linkDto.getName()),Link::getName,linkDto.getName());
        wrapper.eq(StringUtils.hasText(linkDto.getStatus()),Link::getStatus,linkDto.getStatus());
        //封装结果返回
        page(page,wrapper);
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(page.getRecords(), LinkVo.class);
        return ResponseResult.okResult(new PageVo(linkVos,page.getTotal()));
    }

    @Override
    public ResponseResult getLinkById(Integer id) {
        Link link = getById(id);
        LinkVo linkVo = BeanCopyUtils.copyBean(link, LinkVo.class);
        return ResponseResult.okResult(linkVo);
    }

    @Override
    public ResponseResult deleteLink(List<Integer> ids) {
        removeByIds(ids);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addLink(Link link) {
        save(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateLink(Link link) {
        updateById(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult changeLinkStatus(LinkDto linkDto) {
        LambdaUpdateWrapper<Link> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Link::getStatus,linkDto.getStatus());
        wrapper.eq(Link::getId,linkDto.getId());
        update(wrapper);
        return ResponseResult.okResult();
    }
}

