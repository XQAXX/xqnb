package com.dream.common.util.user;

import com.dream.common.constans.UserCacheConstans;
import com.dream.common.entity.shiro.User;
import com.dream.common.service.cache.CookieUtils;
import com.dream.common.service.cache.RedisUtil;
import com.dream.common.util.ObjectUtils;
import com.dream.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 乔宏站
 *
 */
public class SingletonLoginUtils {
    @Autowired
	private static RedisUtil redisUtil;
	
	/**
	 * 获取前台登录用户ID
	 * @param request
	 * @return 返回用户ID
	 */
	public static Integer getLoginUserId(HttpServletRequest request){
		User user = getLoginUser(request);
		if(user!=null){
			return user.getUserId();
		}
		return 0;
	}
	
	/**
	 * 获取前台登录用户
	 * @param request
	 * @return User
	 */
	public static User getLoginUser(HttpServletRequest request){
		String userKey = String.valueOf(CookieUtils.getCookie(request, UserCacheConstans.USER_LOGIN));
		if(StringUtils.isNotEmpty(userKey)){
			User user = (User) redisUtil.get(userKey);
			if(ObjectUtils.isNotNull(user)){
				return user;
			}
		}
		return null;
	}

	//判断是否为手机浏览器
	public static boolean JudgeIsMoblie(HttpServletRequest request) {
		boolean isMoblie = false;
		String[] mobileAgents = { "iphone", "android","ipad", "phone", "mobile", "wap", "netfront", "java", "opera mobi",
				"opera mini", "ucweb", "windows ce", "symbian", "series", "webos", "sony", "blackberry", "dopod",
				"nokia", "samsung", "palmsource", "xda", "pieplus", "meizu", "midp", "cldc", "motorola", "foma",
				"docomo", "up.browser", "up.link", "blazer", "helio", "hosin", "huawei", "novarra", "coolpad", "webos",
				"techfaith", "palmsource", "alcatel", "amoi", "ktouch", "nexian", "ericsson", "philips", "sagem",
				"wellcom", "bunjalloo", "maui", "smartphone", "iemobile", "spice", "bird", "zte-", "longcos",
				"pantech", "gionee", "portalmmm", "jig browser", "hiptop", "benq", "haier", "^lct", "320x320",
				"240x320", "176x220", "w3c ", "acs-", "alav", "alca", "amoi", "audi", "avan", "benq", "bird", "blac",
				"blaz", "brew", "cell", "cldc", "cmd-", "dang", "doco", "eric", "hipt", "inno", "ipaq", "java", "jigs",
				"kddi", "keji", "leno", "lg-c", "lg-d", "lg-g", "lge-", "maui", "maxo", "midp", "mits", "mmef", "mobi",
				"mot-", "moto", "mwbp", "nec-", "newt", "noki", "oper", "palm", "pana", "pant", "phil", "play", "port",
				"prox", "qwap", "sage", "sams", "sany", "sch-", "sec-", "send", "seri", "sgh-", "shar", "sie-", "siem",
				"smal", "smar", "sony", "sph-", "symb", "t-mo", "teli", "tim-", "tosh", "tsm-", "upg1", "upsi", "vk-v",
				"voda", "wap-", "wapa", "wapi", "wapp", "wapr", "webc", "winw", "winw", "xda", "xda-",
				"Googlebot-Mobile" };
		if (request.getHeader("User-Agent") != null) {
			String agent=request.getHeader("User-Agent");
			for (String mobileAgent : mobileAgents) {
				if (agent.toLowerCase().indexOf(mobileAgent) >= 0&&agent.toLowerCase().indexOf("windows nt")<=0 &&agent.toLowerCase().indexOf("macintosh")<=0) {
					isMoblie = true;
					break;
				}
			}
		}
		return isMoblie;
	}
}
