package org.pada.ice.labcontrol;

import android.content.SharedPreferences;
import android.content.Context;
import com.google.gson.Gson;

import java.util.ArrayList;

public class PCList {
    private static PCList instance = null;
    final private ArrayList<PCInfo> pcList = new ArrayList<>();

    private PCList() {}

    public static synchronized PCList getInstance() {
        if (instance == null)
            instance = new PCList();
        return instance;
    }

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


//    public void save(Context context) {
//        SharedPreferences sharedPrefs = context.getSharedPreferences("PC_Prefs", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPrefs.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(pcList);
//        editor.putString("pc_list", json);
//        editor.apply();
//    }
}
