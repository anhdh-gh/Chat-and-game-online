package noughts_and_crosses.support;

import java.net.*;
import java.util.*;

public class Ip {

    private Ip() {}
    
    public static String getIpv4() throws SocketException, UnknownHostException {
        // Địa chỉ ip có khả năng sử dụng được
        String ipv4 = "";
        
        try(final DatagramSocket socket = new DatagramSocket()) {
           socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
           ipv4 = socket.getLocalAddress().getHostAddress();
       }             
        
        // Lưu tất cả các địa chỉ ipv4 có trong máy 
        ArrayList<String> ipv4s = new ArrayList<>();
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface iface = interfaces.nextElement();

            // Filters out 127.0.0.1 and inactive interfaces
            if (iface.isLoopback() || !iface.isUp()) continue;

            Enumeration<InetAddress> addresses = iface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();
                // Bỏ ipv6
                if (addr instanceof Inet6Address) continue;

                // Lấy cái ipv4
                ipv4s.add(addr.getHostAddress());
            }
        }
        
        if(ipv4s.isEmpty()) return "localhost";

        if(ipv4s.contains(ipv4)) return ipv4;

        return ipv4s.get(0);
    }
}