package org.pada.ice.labcontrol;

import android.telecom.Call;
import android.util.Log;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class PCScanner {
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

    final private Callback callback;
    final private Pair<Integer, Integer> networkIP;
    public PCScanner(Callback callback) throws Exception {
        this.callback = callback;

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

    public <T> ArrayList<T> populate() {
        var broadcast = networkIP.first | networkIP.second;
        var list = new ArrayList<T>();
        for (int i = networkIP.first + 1; i < broadcast; ++i) {
            byte[] raw =  ByteBuffer.allocate(4).putInt(i).array();
            var o = callback.apply(raw);
            if (o != null)
                list.add((T) o);
        }
        return list;
    }
}
