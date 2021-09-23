package org.seefly.springweb.utils;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

/**
 * 跟spring 的 BeanUtils差不多
 *
 * @author liujianxin
 */
public class BeanUtils {
    
    private BeanUtils() {
    }
    
    @Nullable
    public static <T> T transformFrom(@Nullable Object source, @NonNull Class<T> targetClass) {
        Assert.notNull(targetClass, "Target class must not be null");
        
        if (source == null) {
            return null;
        }
        
        try {
            T targetInstance = targetClass.newInstance();
            org.springframework.beans.BeanUtils.copyProperties(source, targetInstance, getNullPropertyNames(source));
            return targetInstance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to new " + targetClass.getName() + " instance or copy properties", e);
        }
    }
    
    
    public static void updateProperties(@NonNull Object source, @NonNull Object target) {
        Assert.notNull(source, "source object must not be null");
        Assert.notNull(target, "target object must not be null");
        
        try {
            org.springframework.beans.BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
        } catch (BeansException e) {
            throw new RuntimeException("Failed to copy properties", e);
        }
    }
    
    
    @NonNull
    private static String[] getNullPropertyNames(@NonNull Object source) {
        return getNullPropertyNameSet(source).toArray(new String[0]);
    }
    
    @NonNull
    private static Set<String> getNullPropertyNameSet(@NonNull Object source) {
        
        Assert.notNull(source, "source object must not be null");
        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(source);
        PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();
        
        Set<String> emptyNames = new HashSet<>();
        
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            String propertyName = propertyDescriptor.getName();
            Object propertyValue = beanWrapper.getPropertyValue(propertyName);
            
            if (propertyValue == null) {
                emptyNames.add(propertyName);
            }
        }
        
        return emptyNames;
    }
}
