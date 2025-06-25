package org.pada.ice.labcontrol;

import android.util.Log;

public class PCStatusScanner {

    Boolean scanning = false;

    private static PCStatusScanner instance = null;

    public static synchronized PCStatusScanner getInstance() {
        if (instance == null)
            instance = new PCStatusScanner();
        return instance;
    }

    public Boolean isScanning() {
        return scanning;
    }

    public void update() {
        scanning = true;
        for (int i = 0; i < PCList.getInstance().size(); ++i) {
            var echo = PCOption.echo(PCList.getInstance().get(i).ip);
            if (echo.startsWith("error"))
            {
                PCList.getInstance().get(i).status = false;
                continue;
            }
            var echoContent = echo.split("[%]");
            PCList.getInstance().get(i).status = true;
            PCList.getInstance().get(i).name = echoContent[0];
            PCList.getInstance().get(i).operatingSystem = echoContent[1];
            PCList.getInstance().get(i).mac = echoContent[2];
        }
        scanning = false;
    }

}
