package com.touchbiz.common.utils.security;

import com.touchbiz.common.entity.model.SysPermissionDataRuleModel;
import com.touchbiz.common.entity.model.SysUserCacheInfo;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.List;

public interface IDataAutor {

    public static final String MENU_DATA_AUTHOR_RULES = "MENU_DATA_AUTHOR_RULES";

    public static final String MENU_DATA_AUTHOR_RULE_SQL = "MENU_DATA_AUTHOR_RULE_SQL";

    public static final String SYS_USER_INFO = "SYS_USER_INFO";


    /**
     * 往链接请求里面，传入数据查询条件
     *
     * @param request
     * @param dataRules
     */
    void installDataSearchConditon(ServerHttpRequest request, List<SysPermissionDataRuleModel> dataRules);
    /**
     * 获取请求对应的数据权限规则
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    List<SysPermissionDataRuleModel> loadDataSearchConditon();

    /**
     * 获取请求对应的数据权限SQL
     *
     * @return
     */
    String loadDataSearchConditonSqlString();
    /**
     * 往链接请求里面，传入数据查询条件
     *
     * @param request
     * @param sql
     */
    void installDataSearchConditon(ServerHttpRequest request, String sql);

    /**
     * 将用户信息存到request
     * @param userinfo
     */
    void installUserInfo(SysUserCacheInfo userinfo);

    /**
     * 从request获取用户信息
     * @return
     */
    SysUserCacheInfo loadUserInfo();
}
