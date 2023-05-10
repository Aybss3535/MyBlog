package com.aybss.handler;

import com.aybss.enums.AppHttpCodeEnum;
import com.aybss.exception.SystemException;
import com.aybss.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Long userId;
        try {
            userId = SecurityUtils.getUserId();
        } catch (Exception e) {
            e.printStackTrace();
            userId = -1l;
        }
        this.setFieldValByName("createBy", userId, metaObject);
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("updateBy", userId, metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Long userId;
        try {
            userId = SecurityUtils.getUserId();
        } catch (Exception e) {
//            e.printStackTrace();
            log.info("this is updated by system automatically...");
            userId = -1l;
        }
        this.setFieldValByName("updateBy", userId,metaObject);
        this.setFieldValByName("updateTime",new Date(),metaObject);
    }
}
