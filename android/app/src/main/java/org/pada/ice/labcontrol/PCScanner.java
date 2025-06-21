package org.pada.ice.labcontrol;

import android.telecom.Call;
import android.util.Log;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class PCScanner {

    Boolean scanning = false;

    private static PCScanner instance = null;

    public static synchronized PCScanner getInstance() throws Exception {
        if (instance == null)
            instance = new PCScanner();
        return instance;
    }
    private static class NetworkIPSocketThread extends Thread {
        private Pair<Integer, Integer> response;

        public Pair<Integer, Integer> getResponse() {
            return response;
        }

        @Override
        public void run() {
            try (var socket = new DatagramSocket()) {
                socket.connect(InetAddress.getByName("uniwa.gr"), 80);
                var inetAddress = socket.getLocalAddress();

                var networkInterface = NetworkInterface.getByInetAddress(inetAddress);
                if (networkInterface == null)
                    throw new Exception("network interface not found");

                var ifaceAddress = networkInterface.getInterfaceAddresses().stream()
                        .filter(address -> inetAddress.equals(address.getAddress()))
                        .findAny()
                        .orElseThrow(() -> new Exception("interface address not found"));

                int prefixLength = ifaceAddress.getNetworkPrefixLength();
                var array = inetAddress.getAddress();
                int address = ByteBuffer.wrap(array).getInt();
                int rbitmask = (((int) 1) << (32 - prefixLength)) - 1;
                response = new Pair<>(address & (~rbitmask), rbitmask);
            } catch (Exception e) {
                e.printStackTrace();
                response = null;
            }

        }
    }

    final private Pair<Integer, Integer> networkIP;
    private PCScanner() throws Exception {
        var thread = new NetworkIPSocketThread();
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Log.println(Log.INFO, "PCScanner", e.toString());
        }

        if (thread.getResponse() == null)
            throw new Exception("error: couldn't find network IP");
        networkIP = thread.getResponse();
    }

    public Boolean isScanning() {
        return scanning;
    }

    public void populate() {
        if (scanning)
            return;

        scanning = true;
        var broadcast = networkIP.first | networkIP.second;
        for (int i = networkIP.first + 1; i < broadcast; ++i) {
            byte[] raw =  ByteBuffer.allocate(4).putInt(i).array();
            var ip = (int)(raw[0] & 0xFF) + "."
                    + (int)(raw[1] & 0xFF) + "."
                    + (int)(raw[2] & 0xFF) + "."
                    + (int)(raw[3] & 0xFF);
            var echo = PCOption.echo(ip);
            if (echo.startsWith("error"))
                continue;
            var echoContent = echo.split("[%]");
            PCScanList.getInstance().add(new PCInfo(echoContent[0], ip, echoContent[2], true, echoContent[1]));
        }
        scanning = false;
    }
}
