package com.dream.config;
import com.dream.common.constans.CacheConstans;
import com.dream.common.service.cache.Cache;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
/**
 * 自定义凭证匹配器
 * @author 小乔
 */
public class RetryLimitCredentialsMatcher extends HashedCredentialsMatcher {

    /**
     * 密码输入错误次数就被冻结
     */
    private Integer errorPasswordTimes=5;
    /**
     * 方法名: doCredentialsMatch
     * 方法描述: 用户登录错误次数方法.
     * @param token
     * @param info
     * @return boolean
     * @throws
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token,
                                      AuthenticationInfo info) {
        String username = (String) token.getPrincipal();
        // 获取用户的登陆失败次数
        Integer retryCount = (Integer) CacheConstans.cache.get(username);
        if (retryCount == null) {
            retryCount = 0;
            CacheConstans.cache.set(username, retryCount,30);
        }
        if (retryCount >= errorPasswordTimes) {
            if (retryCount == 5){
                CacheConstans.cache.set(username, ++retryCount,60);
            }
            /**
             *  如果登陆失败次数大于5次抛出异常,登录重试次数，超限。
             *  只允许在一段时间内允许有一定数量的认证尝试!
             */
            throw new ExcessiveAttemptsException();
        }else {
            CacheConstans.cache.set(username, ++retryCount,30);
        }
        boolean matches = super.doCredentialsMatch(token, info);
        //如果用户认证成功
        if (matches) {
            // 清除登录的失败缓存
            CacheConstans.cache.remove(username);
        }
        return matches;
    }
}
