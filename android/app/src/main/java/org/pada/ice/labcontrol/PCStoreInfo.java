package org.pada.ice.labcontrol;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class PCStoreInfo {
    public static void saveToFile(Context context, ArrayList<PCInfo> pcList){
        Gson gson = new Gson();
        String json = gson.toJson(pcList);

        try (FileOutputStream fos = context.openFileOutput("pcList.json", Context.MODE_PRIVATE)){
            fos.write(json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<PCInfo> loadFromFile(Context context) {
        Gson gson = new Gson();
        ArrayList<PCInfo> pcList = new ArrayList<>();

        try (FileInputStream fis = context.openFileInput("pcList.json");
        InputStreamReader isr = new InputStreamReader(fis)){
            pcList = gson.fromJson(isr, new TypeToken<ArrayList<PCInfo>>(){}.getType());
        } catch (IOException e){
            e.printStackTrace();
        }
        return pcList;
    }
}
