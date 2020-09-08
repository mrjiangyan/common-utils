package com.touchbiz.common.utils.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * IP工具类
 * Created by sun on 15/8/30.
 */
@Slf4j
public class IPAddressUtils {


    private static final Map<String, String> IP4S = new HashMap<>();

    private static final Map<String, String> IP6S = new HashMap<>();

    private static String ipAddress = null;

    private static String hostName = null;

    static {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            // Get hostName
            hostName = addr.getHostName();
            if ("127.0.0.1".equals(addr.getHostAddress())) {
                Enumeration e = NetworkInterface.getNetworkInterfaces();
                while (e.hasMoreElements()) {
                    NetworkInterface netface = (NetworkInterface) e
                            .nextElement();
                    String name = netface.getName();
                    if (!name.startsWith("lo")) {
                        Enumeration e2 = netface.getInetAddresses();
                        while (e2.hasMoreElements()) {
                            InetAddress ip2 = (InetAddress) e2.nextElement();
                            if (ip2 instanceof Inet4Address) {
                                IP4S.put(name, ip2.getHostAddress());
                            } else {
                                IP6S.put(name, ip2.getHostAddress());
                            }
                        }
                    }
                }
            } else {
                IP4S.put(hostName, addr.getHostAddress());
            }
            // 可以通过-D传入系统参数
            String serverName = System.getProperty("serverName");
            if (!StringUtils.isEmpty(serverName)) {
                hostName = serverName;
            }

        } catch (Exception e) {
            log.error("LocalIPUtils error: {}, stackInfo: {} ", e, e.getStackTrace());
        }
    }

    private IPAddressUtils() {
    }

    /**
     * 获取用户的真实ip，并进行md5加密
     */
//    public static String getIpAddress(HttpServletRequest request) {
//        String ip = getClientIP(request);
//        return DigestUtils.md5Hex(ip);
//    }

    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
     * <p>
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     * <p>
     * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130,
     * 192.168.1.100
     * <p>
     * 用户真实IP为： 192.168.1.110
     *
     * @param request
     * @return
     */
//    public static String getClientIP(HttpServletRequest request) {
//        String ip = request.getHeader("x-forwarded-for");
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("WL-Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("HTTP_CLIENT_IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getRemoteAddr();
//        }
//        return ip;
//    }


    /**
     * 得到IPV4模式的唯一IP地址，如果有多个，那么随机获得一个
     *
     * @return
     */
    public static String getIp4Single() {
        if (IP4S.isEmpty()) {
            return "127.0.0.1";
        }
        if (null == ipAddress) {
            for (String s : IP4S.values()) {
                ipAddress = s;
                if (!"127.0.0.1".equals(ipAddress)) {
                    break;
                }
            }
        }
        if (null == ipAddress) {
            ipAddress = "127.0.0.1";
        }
        return ipAddress;
    }


    public static int ip2Int(String ip) {
        try {
            InetAddress address = InetAddress.getByName(ip);
            byte[] bytes = address.getAddress();
            int a, b, c, d;
            a = byte2int(bytes[0]);
            b = byte2int(bytes[1]);
            c = byte2int(bytes[2]);
            d = byte2int(bytes[3]);
            return (a << 24) | (b << 16) | (c << 8) | d;
        } catch (UnknownHostException e) {
            log.info("WebTool error:", e);
            return 0;
        }
    }

    public static String long2ip(long ip) {
        int[] b = new int[4];
        b[0] = (int) ((ip >> 24) & 0xff);
        b[1] = (int) ((ip >> 16) & 0xff);
        b[2] = (int) ((ip >> 8) & 0xff);
        b[3] = (int) (ip & 0xff);
        String x;
        x = b[0] + "." + b[1] + "." + b[2] + "." + b[3];
        return x;

    }

    private static int byte2int(byte b) {
        int l = b & 0x07f;
        if (b < 0) {
            l |= 0x80;
        }
        return l;
    }
}
