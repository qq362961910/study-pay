package com.jy.pay.common.util;


import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.Enumeration;

public class OrderUtil {

    public static  int seed = 0;
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

    private synchronized static int getSeed(){
        if(++seed > 9999){
            seed = 1;
        }
        return seed;
    }

    /**
     * 生成唯一订单号。
     *
     * @param pre
     * @return
     */
    private synchronized static String createOrderCode(long preOrder){
        String pattern = "00";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        String patternSeed = "0000";
        DecimalFormat seedFormat = new DecimalFormat(patternSeed);
        if(null == localIp || "".equals(localIp)){
            setLocalIp() ;
        }
        return decimalFormat.format(preOrder) + System.currentTimeMillis() + localIp + seedFormat.format(getSeed());
    }

    public static void main(String[] args) {
        System.out.println("ip: " + localIp);

        for (int i = 0; i < 20000; i++) {
            System.out.println(createOrderCode(0));
        }
    }

}
