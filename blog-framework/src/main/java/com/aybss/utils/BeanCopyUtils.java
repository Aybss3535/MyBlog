package com.aybss.utils;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {

    public static <T> T copyBean(Object source, Class<T> clazz) {
        T result = null;
        try {
            //创建目标对象
            result = clazz.getDeclaredConstructor().newInstance();
            //bean拷贝
            BeanUtils.copyProperties(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static <O,T> List<T> copyBeanList(List<O> sources, Class<T> clazz){
        return sources.stream().map(o->copyBean(o,clazz)).collect(Collectors.toList());
    }

}
