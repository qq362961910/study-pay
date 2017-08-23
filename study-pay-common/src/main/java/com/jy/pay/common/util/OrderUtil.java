package com.jy.pay.common.util;


import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class OrderUtil {

    private static String localIp = null;

    static {
        setLocalIp() ;
    }

    public static void setLocalIp(){
        localIp = getLocalIp() ;
    }

    public static String getLocalIp(){
        StringBuilder ip = new StringBuilder();
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && !inetAddress.isLinkLocalAddress()
                            && inetAddress.isSiteLocalAddress()) {
                        ip.append(inetAddress.getHostAddress().toString());
                    }

                }
            }
        } catch (SocketException ex) {}
        return ip.toString() ;
    }

    public static void main(String[] args) {
        System.out.println("ip: " + localIp);
    }

}
