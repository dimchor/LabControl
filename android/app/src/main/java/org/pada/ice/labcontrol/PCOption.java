package org.pada.ice.labcontrol;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import io.github.reines.wol.WakeOnLan;

public class PCOption {

    private static class SocketThread extends Thread {
        String command;
        String response;
        String ip;

        int timeout;

        final static int TIMEOUT = 3;
        final static int BUFSIZE = 1024;


        SocketThread(String ip, String command) {
            this.ip = ip;
            this.command = command;
            this.timeout = TIMEOUT;
        }

        SocketThread(String ip, String command, int timeout) {
            this.ip = ip;
            this.command = command;
            this.timeout = timeout;
        }

        String getResponse() {
            return response;
        }

        @Override
        public void run() {
            try (var socket = new Socket()) {
                socket.connect(new InetSocketAddress(ip, 47001), timeout * 1000); // connect timeout
                socket.setSoTimeout(timeout * 1000); // read timeout

                try (var out = new DataOutputStream(socket.getOutputStream());
                     var in = new DataInputStream(socket.getInputStream())) {

                    out.write(command.getBytes(StandardCharsets.UTF_8));

                    byte[] bResponse = new byte[BUFSIZE];
                    int length = in.read(bResponse, 0, bResponse.length);
                    if (length < 1) {
                        throw new Exception("empty");
                    }

                    response = new String(bResponse, 0, length, StandardCharsets.UTF_8);
                }
            } catch (Exception e) {
                e.printStackTrace();
                response = "error: " + e.toString();
            }
        }
    }
    private static String runCommand(String ip, String command) {
        String response;

        var thread = new SocketThread(ip, command);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Log.println(Log.INFO, "PCOption", e.toString());
        }

        return thread.getResponse();
    }

    private static String runCommand(String ip, String command, int timeout) {
        String response;

        var thread = new SocketThread(ip, command, timeout);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Log.println(Log.INFO, "PCOption", e.toString());
        }

        return thread.getResponse();
    }

    public static String shutdown(String ip) {
        return runCommand(ip, "shutdown");
    }

    public static String reboot(String ip) {
        return runCommand(ip, "reboot");
    }

    public static String restore(String ip) {
        return runCommand(ip, "restore");
    }

    public static String wakeOnLan(String mac) {
        try {
            WakeOnLan.wake(mac);
            return "WOL packet sent to " + mac;
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to send WOL packet: " + e.toString();
        }
    }

    public static String echo(String ip) {
        return runCommand(ip, "echo");
    }

    public static String echo(String ip, int timeout) {
        return runCommand(ip, "echo", timeout);
    }

    public static String mac(String ip) {
        return runCommand(ip, "mac");
    }
}
