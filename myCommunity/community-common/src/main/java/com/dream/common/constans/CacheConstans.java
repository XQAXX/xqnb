package com.dream.common.constans;

import com.dream.common.service.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 神秘人
 */
@Component
public class CacheConstans {

    @Autowired
    @Qualifier("redisUtil")
    private Cache redisUtil;

    public static Cache cache;

    @PostConstruct
    private void init(){
        cache = redisUtil;
    }
}
