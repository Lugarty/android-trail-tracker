package com.example.atividaden2.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;
import com.example.atividaden2.R;

public class ConfigActivity extends AppCompatActivity {

    private RadioGroup rgTipoMapa, rgNavegacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        rgTipoMapa = findViewById(R.id.radioTipoMapa);
        rgNavegacao = findViewById(R.id.radioNavegacao);
        Button btnSalvar = findViewById(R.id.btnSalvarConfig);

        carregarConfiguracoes();

        btnSalvar.setOnClickListener(v -> salvarConfiguracoes());
    }

    private void carregarConfiguracoes() {
        SharedPreferences prefs = getSharedPreferences("app_config", MODE_PRIVATE);
        int tipoMapa = prefs.getInt("tipo_mapa", 1);
        int navegacao = prefs.getInt("navegacao", 0);

        if (tipoMapa == 1) ((RadioButton) findViewById(R.id.radioVetorial)).setChecked(true);
        else ((RadioButton) findViewById(R.id.radioSatelite)).setChecked(true);

        if (navegacao == 0) ((RadioButton) findViewById(R.id.radioNorthUp)).setChecked(true);
        else ((RadioButton) findViewById(R.id.radioCourseUp)).setChecked(true);
    }

    private void salvarConfiguracoes() {
        SharedPreferences prefs = getSharedPreferences("app_config", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        int tipoMapa;
        if (rgTipoMapa.getCheckedRadioButtonId() == R.id.radioVetorial) {
            tipoMapa = 1;
        } else {
            tipoMapa = 2;
        }

        int navegacao;
        if (rgNavegacao.getCheckedRadioButtonId() == R.id.radioNorthUp) {
            navegacao = 0;
        } else {
            navegacao = 1;
        }

        editor.putInt("tipo_mapa", tipoMapa);
        editor.putInt("navegacao", navegacao);
        editor.apply();

        finish();
    }
}