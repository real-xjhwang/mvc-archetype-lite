package com.xjhwang.common.util;

import cn.hutool.core.util.ClassUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author 黄雪杰 on 2024-07-10 18:06
 */
@Slf4j
@UtilityClass
public class ClassUtils extends ClassUtil {
    
    /**
     * 获取默认的 ClassLoader
     *
     * @return ClassLoader
     */
    public static ClassLoader getDefaultClassLoader() {
        
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable tr) {
            // Cannot access thread context ClassLoader - falling back to system class loader...
        }
        // No thread context class loader -> use class loader of this class.
        if (Objects.isNull(cl)) {
            cl = ClassUtils.class.getClassLoader();
        }
        return cl;
    }
    
    /**
     * 检查指定的类是否是 CGLIB 生成的类
     *
     * @param clazz 要检查的类
     * @return true：CGLIB 生成的类
     */
    public static boolean isCglibProxyClass(Class<?> clazz) {
        
        return (Objects.nonNull(clazz) && isCglibProxyClass(clazz.getName()));
    }
    
    /**
     * 检查指定的类是否是 CGLIB 生成的类
     *
     * @param className 要检查的类的名称
     * @return true：CGLIB 生成的类
     */
    public static boolean isCglibProxyClass(String className) {
        
        return (StringUtils.isNotBlank(className) && className.contains("$$"));
    }
}
