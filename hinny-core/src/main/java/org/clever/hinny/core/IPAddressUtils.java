package org.clever.hinny.core;

import java.util.Set;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/07/28 22:34 <br/>
 */
public class IPAddressUtils {
    public static final IPAddressUtils Instance = new IPAddressUtils();

    private IPAddressUtils() {
    }

    /**
     * 获取本机所有的IPv4地址,已经获取过就直接返回
     */
    public Set<String> getInet4Address() {
        return org.clever.common.utils.IPAddressUtils.getInet4Address();
    }

    /**
     * 重新获取本机所有的IPv4地址
     */
    public Set<String> refreshInet4Address() {
        return org.clever.common.utils.IPAddressUtils.refreshInet4Address();
    }

    /**
     * 获取本机所有的IPv4地址,有多个用“;”分隔
     *
     * @return 失败返回"未知"
     */
    public String getInet4AddressStr() {
        return org.clever.common.utils.IPAddressUtils.getInet4AddressStr();
    }
}
