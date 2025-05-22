package org.pada.ice.labcontrol;

import static com.google.android.material.internal.ContextUtils.getActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import java.util.ArrayList;

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

        pcAdapter = new PCAdapter();
        pcRecyclerView.setAdapter(pcAdapter);
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
        if (id == R.id.menu_add) {
            Intent intent = new Intent(this, AddPCActivity.class);
            startActivity(intent);
            recreate();
            return true;
        } else if (id == R.id.menu_scan) {
            Toast.makeText(this, "Scan clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ScanActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_about) {
            Toast.makeText(this, "About clicked", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}