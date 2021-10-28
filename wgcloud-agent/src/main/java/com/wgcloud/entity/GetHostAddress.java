package com.wgcloud.entity;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class GetHostAddress {
    /*
    public static InetAddress getLocalHostAddress() {
        try {
            InetAddress candiateAddress = null;
            Enumeration<NetworkInterface> networkInterface = NetworkInterface.getNetworkInterfaces();
            while (networkInterface.hasMoreElements()) {
                NetworkInterface iface = networkInterface.nextElement();
                for (Enumeration<InetAddress> inetaddrs = iface.getInetAddresses(); inetaddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = inetaddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {
                        if (inetAddr.isSiteLocalAddress()) {
                            return inetAddr;
                        }
                    }
                    if (candiateAddress == null) {
                        candiateAddress = inetAddr;
                    }
                }
            }
            return candiateAddress == null ? InetAddress.getLocalHost() : candiateAddress;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
     */
    /*
    根据socket请求获取本地真实ip
     */
    public static String hostAddress(){
        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            String ip = socket.getLocalAddress().getHostAddress();
//            System.out.println(ip);
            return ip;
        }catch ( Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
