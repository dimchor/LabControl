package org.pada.ice.labcontrol;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public class AddPCActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pc);

        var window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.custom_turquoise));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            var controller = getWindow().getInsetsController();
            assert controller != null;
            controller.setSystemBarsAppearance(WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS);
        }

        Button button = (Button) findViewById(R.id.buttonAddPC);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                var ipAddress = (EditText) findViewById(R.id.editTextInternetProtocolAddress);
                String content = ipAddress.getText().toString();
                PCList.getInstance().add(new PCInfo("name", content, "mac", true, "unk"));
                Toast.makeText(v.getContext(), "clicked" + PCList.getInstance().size(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Title appearing only once
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
