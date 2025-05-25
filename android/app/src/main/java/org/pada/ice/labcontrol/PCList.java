package org.pada.ice.labcontrol;

import java.util.ArrayList;

public class PCList {
    private static PCList instance = null;
    private PCList() {
    }

    public static synchronized PCList getInstance() {
        if (instance == null)
            instance = new PCList();
        return instance;
    }

    final private ArrayList<PCInfo> pcList = new ArrayList<>();

    public void add(PCInfo pcInfo) {
        pcList.add(pcInfo);
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
