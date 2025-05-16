package org.pada.ice.labcontrol;

import android.os.Bundle;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import java.util.ArrayList;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView pcRecyclerView;
    private ArrayList<PCInfo> pcList;
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
        pcList = new ArrayList<>();
        pcList.add(new PCInfo("PC1", "192.168.1.1", "11:22:33:44:55:66", true, "Linux"));
        pcList.add(new PCInfo("PC2", "192.168.1.2", "00:22:33:44:55:66", false, "Windows10"));
        pcList.add(new PCInfo("PC3", "192.168.1.3", "11:22:33:44:55:77", false, "Windows7"));

        pcAdapter = new PCAdapter(pcList);
        pcRecyclerView.setAdapter(pcAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.menu_add) {
            Toast.makeText(this, "Add clicked", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menu_scan) {
            Toast.makeText(this, "Scan clicked", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menu_about) {
            Toast.makeText(this, "About clicked", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}