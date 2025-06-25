package org.pada.ice.labcontrol;

import static com.google.android.material.internal.ContextUtils.getActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.content.Intent;
import java.util.ArrayList;
import java.util.concurrent.Executors;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private RecyclerView pcRecyclerView;
    private PCAdapter pcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        var window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.custom_turquoise));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            var controller = getWindow().getInsetsController();
            assert controller != null;
            controller.setSystemBarsAppearance(WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS);
        }

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Title appearing only once
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        //RecyclerView
        pcRecyclerView = findViewById(R.id.pcRecyclerView);
        pcRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Data testing
//        var pcListHandle = PCList.getInstance();
//        pcListHandle.add(new PCInfo("PC1", "192.168.1.1", "11:22:33:44:55:66", true, "Linux"));

        //Load the saved PC list
        ArrayList<PCInfo> savedList = PCStoreInfo.loadFromFile(this);
        if (savedList != null && !savedList.isEmpty()) {
            PCList.getInstance().getList().clear();
            PCList.getInstance().addAll(savedList);
        }

        pcAdapter = new PCAdapter();
        pcRecyclerView.setAdapter(pcAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (PCStatusScanner.getInstance().isScanning())
            return;

        if (PCScanList.getInstance().size() > 0)
        {
            var progressBar = (ProgressBar) findViewById(R.id.mainBarScan);
            progressBar.setVisibility(RecyclerView.INVISIBLE);
            return;
        }



        var executor = Executors.newSingleThreadExecutor();
        var handler = new Handler(Looper.getMainLooper());


        executor.execute(() -> {
            PCStatusScanner.getInstance().update();
            handler.post(() -> {
                var progressBar = (ProgressBar) findViewById(R.id.mainBarScan);
                if (progressBar == null)
                    return;
                progressBar.setVisibility(RecyclerView.INVISIBLE);
                pcRecyclerView.getAdapter().notifyDataSetChanged();
            });
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.main_menu_add) {
            if (PCStatusScanner.getInstance().isScanning()) {
                Toast.makeText(this, "Cannot add while checking status", Toast.LENGTH_SHORT).show();
                return true;
            }
            Intent intent = new Intent(this, AddPCActivity.class);
            startActivity(intent);
            recreate();
            return true;
        } else if (id == R.id.main_menu_scan) {
            if (PCStatusScanner.getInstance().isScanning()) {
                Toast.makeText(this, "Cannot scan while checking status", Toast.LENGTH_SHORT).show();
                return true;
            }
            Toast.makeText(this, "Scan clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ScanActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.main_menu_about) {
            new AlertDialog.Builder(this)
                    .setTitle("About")
                    .setMessage("This is a simple android app for managing and controlling multiple PCs on your network. Try the different options by clicking on one of them!")
                    .setPositiveButton("OK", null)
                    .show();
            return true;
        } else if (id == R.id.main_menu_load_example) {
            if (PCStatusScanner.getInstance().isScanning()) {
                Toast.makeText(this, "Cannot load example PCs while checking status", Toast.LENGTH_SHORT).show();
                return true;
            }
            PCList.getInstance().clear();
            loadExample();
            PCStoreInfo.saveToFile(this, PCList.getInstance().getList());
            pcRecyclerView.getAdapter().notifyDataSetChanged();
            return true;
        } else if (id == R.id.main_menu_clear) {
            if (PCStatusScanner.getInstance().isScanning()) {
                Toast.makeText(this, "Cannot clear while checking status", Toast.LENGTH_SHORT).show();
                return true;
            }
            PCList.getInstance().clear();
            PCStoreInfo.saveToFile(this, PCList.getInstance().getList());
            pcRecyclerView.getAdapter().notifyDataSetChanged();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        pcAdapter.notifyDataSetChanged();
    }

    private void loadExample() {
        var examplePCs = new ArrayList<PCInfo>();
        examplePCs.add(new PCInfo("PRPC01", "192.168.88.2", "50:81:40:2B:91:8D", false, "unknown"));
        examplePCs.add(new PCInfo("PRPC02", "192.168.88.3", "50:81:40:2B:7C:78", false, "unknown"));
        examplePCs.add(new PCInfo("PRPC03", "192.168.88.4", "50:81:40:2B:78:DD", false, "unknown"));
        examplePCs.add(new PCInfo("PRPC04", "192.168.88.5", "50:81:40:2B:7B:3D", false, "unknown"));
        examplePCs.add(new PCInfo("PRPC05", "192.168.88.6", "50:81:40:2B:79:91", false, "unknown"));
        examplePCs.add(new PCInfo("PRPC06", "192.168.88.7", "C8:5A:CF:0F:76:3D", false, "unknown"));
        examplePCs.add(new PCInfo("PRPC07", "192.168.88.8", "C8:5A:CF:0D:71:24", false, "unknown"));
        examplePCs.add(new PCInfo("PRPC08", "192.168.88.9", "C8:5A:CF:0F:B3:FF", false, "unknown"));
        examplePCs.add(new PCInfo("PRPC09", "192.168.88.10", "C8:5A:CF:0E:2C:C4", false, "unknown"));
        examplePCs.add(new PCInfo("PRPC10", "192.168.88.11", "C8:5A:CF:0F:7C:D0", false, "unknown"));
        examplePCs.add(new PCInfo("PRPC11", "192.168.88.12", "C8:5A:CF:0D:71:3A", false, "unknown"));
        examplePCs.add(new PCInfo("PRPC12", "192.168.88.13", "C8:5A:CF:0F:EE:01", false, "unknown"));
        examplePCs.add(new PCInfo("PRPC13", "192.168.88.14", "C8:5A:CF:0E:1D:88", false, "unknown"));
        examplePCs.add(new PCInfo("PRPC14", "192.168.88.15", "C8:5A:CF:0F:F0:1E", false, "unknown"));
        examplePCs.add(new PCInfo("PRPC15", "192.168.88.16", "50:81:40:2B:7D:A4", false, "unknown"));
        examplePCs.add(new PCInfo("PRPC16", "192.168.88.17", "C8:5A:CF:0E:2C:78", false, "unknown"));
        examplePCs.add(new PCInfo("PRPC17", "192.168.88.18", "50:81:40:2B:87:F4", false, "unknown"));
        examplePCs.add(new PCInfo("PRPC18", "192.168.88.19", "C8:5A:CF:0F:EC:11", false, "unknown"));
        examplePCs.add(new PCInfo("PRPC19", "192.168.88.20", "C8:5A:CF:0F:7C:1F", false, "unknown"));
        examplePCs.add(new PCInfo("PRPC20", "192.168.88.21", "C8:5A:CF:0D:71:2C", false, "unknown"));
        examplePCs.add(new PCInfo("PRPC21", "192.168.88.22", "C8:5A:CF:0D:70:95", false, "unknown"));
        examplePCs.add(new PCInfo("PRPC22", "192.168.88.23", "50:81:40:2B:5F:D0", false, "unknown"));
        examplePCs.add(new PCInfo("PRPC23", "192.168.88.24", "50:81:40:2B:7A:0B", false, "unknown"));
        examplePCs.add(new PCInfo("PRPC24", "192.168.88.25", "50:81:40:2B:8F:D3", false, "unknown"));
        examplePCs.add(new PCInfo("PRPC25", "192.168.88.26", "50:81:40:2B:72:E0", false, "unknown"));
        examplePCs.add(new PCInfo("PRPC26", "192.168.88.27", "50:81:40:2B:7A:74", false, "unknown"));
        examplePCs.add(new PCInfo("PRPC27", "192.168.88.28", "C8:5A:CF:0F:7C:D4", false, "unknown"));
        PCList.getInstance().addAll(examplePCs);
    }

}