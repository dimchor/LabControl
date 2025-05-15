package org.pada.ice.labcontrol;

public class PCInfo {
    public String name;
    public String ip;
    public String mac;

    public boolean status;

    public PCInfo(String name, String ip, String mac, boolean status){
        this.name = name;
        this.ip = ip;
        this.mac = mac;
        this.status = status;
    }
}
