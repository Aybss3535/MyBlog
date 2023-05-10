package com.aybss.constants;


/**
 * 系统常量类
 */
public class SystemConstants {
    /**
     * 已发布文章
     */
    public static final Integer ARTICLE_STATUS_NORMAL = 0;
    /**
     * 草稿文章
     */
    public static final Integer ARTICLE_STATUS_DRAFT = 1;
    /**
     * 文章置顶
     */
    public static final Integer ARTICLE_TOP = 1;
    /**
     * 文章不置顶
     */
    public static final Integer ARTICLE_NOT_TOP = 0;
    /**
     * 文章运行评论
     */
    public static final Integer ARTICLE_ALLOW_COMMENT = 1;
    /**
     * 文章不允许评论
     */
    public static final Integer ARTICLE_NOT_ALLOW_COMMENT = 0;
    /**
     * 分类正常
     */
    public static final String CATEGORY_STATUS_NORMAL = "0";
    /**
     * 分类被禁用
     */
    public static final String CATEGORY_STATUS_DISABLED = "1";
    /**
     * 审核通过的友链
     */
    public static final String LINK_STATUS_NORMAL = "0";
    /**
     * 审核未通过
     */
    public static final String LINK_STATUS_DISABLED = "1";
    /**
     * 待审核
     */
    public static final String LINK_STATUS_WAIT_VERIFY = "2";
    /**
     * 根评论
     */
    public static final Integer COMMENT_STATUS_ROOT = -1;
    /**
     * 文章评论
     */
    public static final String COMMENT_TYPE_ARTICLE = "0";
    /**
     * 友链评论
     */
    public static final String COMMENT_TYPE_LINK = "1";

    /**
     * 浏览量在redis中存储的key
     */
    public static final String REDIS_KEY_VIEWCOUNT = "viewCount";

    public static final String MENU_TYPE_CATEGORY = "C";

    public static final String MENU_TYPE_BUTTON = "F";

    public static final String MENU_TYPE_MENU = "M";

    public static final String MENU_STATUS_NORMAL = "0";

    /**
     * 头像类型的图片存放路径
     */
    public static final String IMAGE_DIRECTORY_AVATAR = "avatar";
    /**
     * 文章的图片存放路径
     */
    public static final String IMAGE_DIRECTORY_ARTICLE = "article";

    public static final String USER_TYPE_ADMIN = "1";
}
