package org.pada.ice.labcontrol;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class PCOption {

    private static class SocketThread extends Thread {
        String command;
        String response;
        String ip;


        SocketThread(String ip, String command) {
            this.ip = ip;
            this.command = command;
        }

        String getResponse() {
            return response;
        }

        @Override
        public void run() {
            try (var socket = new Socket(ip, 47001);
                 var out = new DataOutputStream(socket.getOutputStream());
                 var in = new DataInputStream(socket.getInputStream()))
            {
                out.write(command.getBytes(StandardCharsets.UTF_8));

                byte[] bResponse = new byte[128];
                int length = in.read(bResponse, 0, bResponse.length);
                if (length < 1) {
                    throw new Exception("empty");
                }
                response = new String(bResponse, StandardCharsets.UTF_8);
            }
            catch (Exception e)
            {
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

    public static String shutdown(String ip) {
        return runCommand(ip, "shutdown");
    }

    public static String reboot(String ip) {
        return runCommand(ip, "reboot");
    }

    public static String restore(String ip) {
        return runCommand(ip, "restore");
    }

    public static String echo(String ip) {
        return runCommand(ip, "echo");
    }

    public static String mac(String ip) {
        return runCommand(ip, "mac");
    }
}
