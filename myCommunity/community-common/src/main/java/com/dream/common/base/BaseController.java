package com.dream.common.base;

import java.util.HashMap;
import java.util.Map;

/**
 * 项目里面Controller的基类
 * @author 乔宏展
 */
public abstract class BaseController {

    /**
     * ajax请求返回结果
     * @param success 请求状态
     * @param message 请求信息
     * @param entity  请求的结果对象
     * @return
     */
    protected Map<String,Object> ajaxResult(Boolean success,String message,Object entity){
        Map<String,Object> map=new HashMap<>();
        map.put("success",Boolean.valueOf(success));
        map.put("message",message);
        map.put("entity",entity);
        return map;
    }

}
