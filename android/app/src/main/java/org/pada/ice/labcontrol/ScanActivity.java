package org.pada.ice.labcontrol;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ScanActivity extends AppCompatActivity {
    private RecyclerView scanRecyclerView;
    private ArrayList<PCInfo> scannedPcList;
    private PCScanAdapter pcScanAdapter;
    private boolean allSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_activity);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.getOverflowIcon().setTint(ContextCompat.getColor(this, R.color.custom_turquoise));

        //Title appearing only once
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);
        }


        //RecyclerView
        scanRecyclerView = findViewById(R.id.scanRecyclerView);
        scanRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Data testing
        scannedPcList = new ArrayList<>();
        scannedPcList.add(new PCInfo("192.168.1.1", "11:22:33:44:55:66"));
        scannedPcList.add(new PCInfo("192.168.1.2", "00:22:33:44:55:66"));
        scannedPcList.add(new PCInfo("192.168.1.3", "11:22:33:44:55:77"));

        pcScanAdapter = new PCScanAdapter(scannedPcList);
        scanRecyclerView.setAdapter(pcScanAdapter);
    }

    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.scan_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.scan_menu_select_all) {
            allSelected = !allSelected;
            pcScanAdapter.selectAll(allSelected);

            String message;
            if (allSelected) {
                message = "All PCs selected";
            } else {
                message = "All PCs cleared";
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            return true;

        } else if (id == R.id.scan_menu_subnet) {
            Toast.makeText(this, "Subnet clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ScanActivity.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.scan_menu_add) {
            ArrayList<PCInfo> selectedPCs = new ArrayList<>(pcScanAdapter.getSelectedPCs());

            if (selectedPCs.isEmpty()) {
                Toast.makeText(this, "No PCs selected", Toast.LENGTH_SHORT).show();
                return true;
            }

            //Add the selected PCs to the global list
            PCList.getInstance().addAll(selectedPCs);

            //Save the file
            PCStoreInfo.saveToFile(this, PCList.getInstance().getList());

            //Remove the selected PCs from the scan list
            scannedPcList.removeAll(selectedPCs);
            pcScanAdapter.notifyDataSetChanged();
            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            finish();
            Toast.makeText(this, "Back arrow clicked", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (item.getItemId() == R.id.refresh) {
            Toast.makeText(this, "Refresh clicked", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
