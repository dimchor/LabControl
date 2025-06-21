package org.pada.ice.labcontrol;

import java.util.ArrayList;

public class PCScanList {
    private static PCScanList instance = null;
    final private ArrayList<PCInfo> pcList = new ArrayList<>();

    private PCScanList() {}

    public static synchronized PCScanList getInstance() {
        if (instance == null)
            instance = new PCScanList();
        return instance;
    }

    public void add(PCInfo pcInfo) {
        pcList.add(pcInfo);
    }

    public void remove(int index) {
        pcList.remove(index);
    }

    public void addAll(ArrayList<PCInfo> pcs) {
        pcList.addAll(pcs);
    }

    public PCInfo get(int position) {
        return pcList.get(position);
    }

    public int size() {
        return pcList.size();
    }

    public ArrayList<PCInfo> getList(){
        return pcList;
    }
}
