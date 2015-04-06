package com.miaoxg.device.monitor.util;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

/**
 * 按约定封装后台返回前台的数据
 * 
 * @author miaoxinguo2002@gmail.com
 * @version orange 2013-5-22
 */
public class JsonUtils {

    private JsonUtils(){
    }

    /**
     * 通用方法
     */
    public static String toJsonString(Object object)  {
        return JSON.toJSONString(object);
    }
    
    /**
     * 配合属性filter的转json方法
     */
    public static String toJsonString(Object object, SimplePropertyPreFilter filter)  {
        return JSON.toJSONString(object, filter);
    }
    
    /**
     * 只包含指定属性
     * 
     * @param properties 属性名数组
     */
    public static String toJsonStringWithIncludes(Object object, String... properties)  {
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        for(String p : properties){
            filter.getIncludes().add(p);
        }
        return toJsonString(object, filter);
    }
    
    /**
     * 只包含指定属性
     * 
     * @param properties 属性名集合
     */
    public static String toJsonStringWithIncludes(Object object, List<String> properties)  {
        return toJsonStringWithIncludes(object, properties.toArray(new String[0]));
    }

    /**
     * 忽略object中的指定属性
     * 
     * @param properties 属性名
     */
    public static String toJsonStringWithExcludes(Object object, String... properties)  {
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        for(String p : properties){
            filter.getExcludes().add(p);
        }
        return toJsonString(object, filter);
    }
    
    /**
     * 忽略object中的指定属性
     * 
     * @param properties 属性名
     */
    public static String toJsonStringWithExcludes(Object object, List<String> properties)  {
        return toJsonStringWithExcludes(object, properties.toArray(new String[0]));
    }
    
    /**
     * 返回成功标识
     */
    public static String toSuccessJson(){
        return "{\"success\":true}";
    }
    
    /**
     * 返回成功标识和信息
     */
    public static String toSuccessJson(final String msg){
        if(StringUtils.isBlank(msg)){
            return toSuccessJson();
        }
        return "{\"success\":true,\"msg\":"+msg+"}";
    }
    
    /**
     * 返回失败标识和信息
     */
    public static String toFailureJson(){
        return "{\"success\":false}";
    }
    
    /**
     * 返回失败标识和信息
     */
    public static String toFailureJson(final String msg){
        return "{\"success\":false,\"msg\":\""+msg+"\"}";
    }
}
