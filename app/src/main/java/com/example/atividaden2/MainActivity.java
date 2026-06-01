package com.example.atividaden2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.atividaden2.activities.ConfigActivity;
import com.example.atividaden2.activities.RegistrarTrilhaActivity;
import com.example.atividaden2.activities.ConsultarTrilhasActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnConfigurar = findViewById(R.id.btnConfigurar);
        Button btnRegistrar = findViewById(R.id.btnRegistrar);
        Button btnConsultar = findViewById(R.id.btnConsultar);

        btnConfigurar.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ConfigActivity.class)));
        btnRegistrar.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RegistrarTrilhaActivity.class)));
        btnConsultar.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ConsultarTrilhasActivity.class)));
    }
}