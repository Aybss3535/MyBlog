package com.aybss.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aybss.mapper.ArticleTagMapper;
import com.aybss.domain.entity.ArticleTag;
import com.aybss.service.ArticleTagService;
import org.springframework.stereotype.Service;

/**
 * 文章标签关联表(ArticleTag)表服务实现类
 *
 * @author makejava
 * @since 2022-12-19 21:07:20
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

}

