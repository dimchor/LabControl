package org.pada.ice.labcontrol;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import io.github.reines.wol.WakeOnLan;

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
                Log.println(Log.INFO, "", "good");

                out.write(command.getBytes(StandardCharsets.UTF_8));

                int length;
                while ((length = in.readInt()) > 0);
                byte[] bResponse = new byte[length];
                in.readFully(bResponse, 0, bResponse.length);
                response = new String(bResponse);
            }
            catch (Exception e)
            {
                Log.println(Log.INFO, "", e.toString());

                response = e.toString();
            }
        }
    }
    private static String runCommand(String ip, String command) {
        Log.println(Log.INFO, "", "i'm here " + ip);

        String response;

        var thread = new SocketThread(ip, command);
        thread.start();

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

    public static String wakeOnLan(String mac){
        try {
            WakeOnLan.wake(mac);
            return "WOL packet sent to " + mac;
        } catch (Exception e) {
            return "Failed to send WOL packet: " + e.getMessage();
        }
    }
}
