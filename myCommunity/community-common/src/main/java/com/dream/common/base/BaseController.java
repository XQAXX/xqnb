package com.dream.common.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
@Slf4j
/**
 * 项目里面Controller的基类
 * @author 神秘人
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
    /**
     * TODO 后期去掉
     * ajax请异常
     */
    public Map<String,Object> setAjaxException(Map<String,Object> map){
        map.put("success", false);
        map.put("message", "网络繁忙，请稍后再操作！");
        map.put("entity", null);
        return map;
    }

    /**
     * TODO 后期去掉
     * 异常 跳转 重定向
     * @param request
     * @param e
     */
    public String setExceptionRequestRedirect(HttpServletRequest request, Exception e, RedirectAttributes redirectAttributes) {
        log.error(request.getContextPath(), e);
        StackTraceElement[] messages = e.getStackTrace();
        if (messages != null && messages.length > 0) {
            StringBuffer buffer = new StringBuffer();
            buffer.append(e.toString()).append("<br/>");
            for (int i = 0; i < messages.length; i++) {
                buffer.append(messages[i].toString()).append("<br/>");
            }
            request.setAttribute("myexception", buffer.toString());

            //请求路径
            String requestUri=request.getRequestURI();
            if(requestUri.indexOf("/uc/")>-1){
                redirectAttributes.addAttribute("myexception", buffer.toString());
                //个人中心 异常 ，重定向到前台，不走/uc布局
                return "redirect:/front/error";
            }
        }
        return "/common/error";
    }
}
