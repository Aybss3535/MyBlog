package com.aybss.enums;

public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200,"操作成功"),
    // 登录
    NEED_LOGIN(401,"需要登录后操作"),
    NO_OPERATOR_AUTH(403,"无权限操作"),
    SYSTEM_ERROR(500,"出现错误"),
    USERNAME_EXIST(501,"用户名已存在"),
     PHONENUMBER_EXIST(502,"手机号已存在"), EMAIL_EXIST(503, "邮箱已存在"),
    REQUIRE_USERNAME(504, "必需填写用户名"),
    LOGIN_ERROR(505,"用户名或密码错误"),
    REQUIRE_CONTENT(506, "需要有评论内容"),
    IMAGE_TYPE_ERROR(507,"仅支持jpg和png类型的文件"),
    REQUIRE_NICKNAME(508, "必需填写昵称"),
    REQUIRE_EMAIL(508, "必需填写邮箱"),
    REQUIRE_PASSWORD(509, "必需填写密码"),
    ARTICLE_NOT_EXIST(510, "文章不存在"),
    TAG_REQUIRE_NAME(511, "请输入标签名"),
    TAG_REQUIRE_ID(511, "请输入要删除的标签ID"),
    DOWNLOAD_ERROR(512, "下载错误");
    int code;
    String msg;

    AppHttpCodeEnum(int code, String errorMessage){
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}