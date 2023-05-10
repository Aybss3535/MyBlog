package com.aybss.service;

import com.aybss.domain.ResponseResult;
import com.aybss.domain.dto.LinkDto;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aybss.domain.entity.Link;

import java.util.List;

/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2022-12-12 12:04:03
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult getAllLinks(Integer pageNum, Integer pageSize, LinkDto linkDto);

    ResponseResult getLinkById(Integer id);

    ResponseResult deleteLink(List<Integer> ids);

    ResponseResult addLink(Link link);

    ResponseResult updateLink(Link link);

    ResponseResult changeLinkStatus(LinkDto linkDto);
}

