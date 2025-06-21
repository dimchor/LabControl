package org.pada.ice.labcontrol;

public class PCInfo {
    public String name;
    public String ip;
    public String mac;
    public String operatingSystem;
    public boolean status;
    private boolean selected = false;

    public PCInfo(String name, String ip, String mac, boolean status, String operatingSystem) {
        this.name = name;
        this.ip = ip;
        this.mac = mac;
        this.operatingSystem = operatingSystem;
        this.status = status;
    }

    public PCInfo(String ip, String mac) {
        this.ip = ip;
        this.mac = mac;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }
}
