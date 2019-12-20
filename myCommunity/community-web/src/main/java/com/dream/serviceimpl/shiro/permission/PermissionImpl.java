package com.dream.serviceimpl.shiro.permission;

import com.dream.common.mapper.permission.PermissionMapper;
import com.dream.common.entity.shiro.Permission;
import com.dream.common.service.shiro.permission.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * shiro的权限实现类
 *
 * @author 小乔
 */
@Service("permissionService")
public class PermissionImpl implements PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public List<Permission> getPermissionByUserId(Long userId) {
        return null;
    }
}
