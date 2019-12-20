package com.dream.common.service.shiro.permission;

import com.dream.common.entity.shiro.Permission;

import java.util.List;

public interface PermissionService {
    List<Permission> getPermissionByUserId(Long userId);
}
