package com.dream.test;
import com.dream.common.service.cache.RedisUtil;
import com.dream.common.util.ObjectUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import sun.net.www.http.HttpClient;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
public class TestController {
    @Resource
    RedisUtil redisUtil;
    @RequestMapping("/login1")
    public String test(){
        Map map2=new HashMap();
        Map map=new HashMap();
        map.put("Name","111");
        map.put("Mobile","13118111111");
        map.put("ExpectCity","111");
        map.put("ExpectJob","111");
       // map2.put("data",map2);
        for (int i = 0; i < 1; i++) {
            new Thread(()->{
                for (int j = 0; j < 1; j++) {
                    //String s = TestController.doPost("http://www.letiao.com/talents/addcontact", map);
                    System.out.println(Thread.currentThread().getName()+"------");
                }
            }).start();
        }
        System.out.println("123");
        return "log2in";
    }

  /*  public static String doPost(String url, Map<String, String> params) {
        StringBuffer result = new StringBuffer();
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(url);
        method.addRequestHeader("x-forwarded-for","27.114.26.222");
        method.getParams().setParameter("http.protocol.content-charset", "UTF-8");
        if(ObjectUtils.isNotNull(params)) {
            NameValuePair[] e = new NameValuePair[params.size()];
            int str = 0;

            Map.Entry entry;
            for(Iterator i$ = params.entrySet().iterator(); i$.hasNext(); e[str++] = new NameValuePair((String)entry.getKey(), (String)entry.getValue())) {
                entry = (Map.Entry)i$.next();
            }

            method.setRequestBody(e);
        }

        try {
            client.executeMethod(method);
            if(method.getStatusCode() == 200) {
                BufferedReader var14 = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), "UTF-8"));
                String var15 = null;

                while((var15 = var14.readLine()) != null) {
                    result.append(var15);
                }
            }
        } catch (Exception e) {
           e.printStackTrace();
        } finally {
            method.releaseConnection();
        }

        return result.toString();
    }*/
}
