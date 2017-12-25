package com.flchy.blog.privilege.provider.holder;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.flchy.blog.base.holder.SpringContextHolder;
import com.flchy.blog.privilege.provider.AbstractPrivilegeProvider;
import com.flchy.blog.privilege.provider.impl.PrivilegeProviderWithBase;
import com.google.common.collect.Maps;

public class PrivilegeHolder {
	private static final Logger logger = LoggerFactory.getLogger(PrivilegeHolder.class);
    private static Map<String, AbstractPrivilegeProvider> providerMap = null;

    private static void getAllPrivilegeProvider() {
        Map<String, AbstractPrivilegeProvider> beanMap = SpringContextHolder.getBeansOfType(AbstractPrivilegeProvider.class);
        Map<String, AbstractPrivilegeProvider> resultMap = Maps.newHashMapWithExpectedSize(beanMap.size());
        if (!CollectionUtils.isEmpty(beanMap)) {
            Collection<AbstractPrivilegeProvider> list = beanMap.values();
            for (AbstractPrivilegeProvider privilegeProvider : list) {
                resultMap.put(privilegeProvider.getClass().getCanonicalName(), privilegeProvider);
            }
        } else {
            logger.error("未获取到可用的权限集成Provider Bean对象！");
            throw new RuntimeException("未获取到可用的权限集成Provider Bean对象！");
        }
        providerMap = resultMap;
    }

    public static boolean getIsPrivilegeProvider() {
        if (providerMap == null) {
            getAllPrivilegeProvider();
        }
        Collection<AbstractPrivilegeProvider> collection = providerMap.values();
        if (collection.size() == 1) {
            return false;
        } else {
            return true;
        }
    }

    public static AbstractPrivilegeProvider getProvider() {
        boolean isProvider = getIsPrivilegeProvider();
        if (isProvider) {
            Collection<AbstractPrivilegeProvider> collection = providerMap.values();
            for (AbstractPrivilegeProvider privilegeProvider : collection) {
                if (!privilegeProvider.getClass().getCanonicalName().equals(PrivilegeProviderWithBase.class.getCanonicalName())) {
                	String  providerName = privilegeProvider.getClass().getCanonicalName();
                	 return providerMap.get(providerName);
                }
            }
            return null;
        } else {
            return providerMap.get(PrivilegeProviderWithBase.class.getCanonicalName());
        }
    }
}
