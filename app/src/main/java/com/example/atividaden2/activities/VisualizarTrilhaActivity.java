package com.example.atividaden2.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.example.atividaden2.R;
import com.example.atividaden2.database.PontoTrilha;
import com.example.atividaden2.database.Trilha;
import com.example.atividaden2.database.TrilhaDBHelper;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class VisualizarTrilhaActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TrilhaDBHelper dbHelper;
    private int trilhaId;
    private TextView txtInfo;
    private static final int LOCATION_PERMISSION_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_trilha);
        trilhaId = getIntent().getIntExtra("trilha_id", -1);
        dbHelper = new TrilhaDBHelper(this);
        txtInfo = findViewById(R.id.txtInfoTrilha);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapVisualizar);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Toast.makeText(this, R.string.erro_carregar_mapa, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
        }

        List<Trilha> todas = dbHelper.listarTodasTrilhas();
        Trilha trilha = null;
        for (Trilha t : todas) {
            if (t.getId() == trilhaId) {
                trilha = t;
                break;
            }
        }

        List<PontoTrilha> pontos = dbHelper.listarPontosPorTrilha(trilhaId);

        PolylineOptions options = new PolylineOptions().color(0xFF0000FF).width(10);
        LatLng first = null;
        for (PontoTrilha p : pontos) {
            LatLng latLng = new LatLng(p.getLatitude(), p.getLongitude());
            options.add(latLng);
            if (first == null) first = latLng;
        }
        googleMap.addPolyline(options);
        if (first != null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(first, 15f));
        }

        long duracaoMs = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date inicio = sdf.parse(trilha.getDataInicio());
            Date fim = sdf.parse(trilha.getDataFim());
            if (inicio != null && fim != null) {
                duracaoMs = fim.getTime() - inicio.getTime();
            }
        } catch (Exception ignored) {
        }
        long horas = TimeUnit.MILLISECONDS.toHours(duracaoMs);
        long minutos = TimeUnit.MILLISECONDS.toMinutes(duracaoMs) % 60;
        long segundos = TimeUnit.MILLISECONDS.toSeconds(duracaoMs) % 60;
        String duracao = String.format(Locale.getDefault(), "%02d:%02d:%02d", horas, minutos, segundos);

        String info = String.format(Locale.getDefault(), getString(R.string.inicio), trilha.getDataInicio()) + "\n" +
                String.format(Locale.getDefault(), getString(R.string.velocidade_media), trilha.getVelocidadeMedia()) + "\n" +
                String.format(Locale.getDefault(), getString(R.string.velocidade_maxima_format), trilha.getVelocidadeMaxima()) + "\n" +
                String.format(Locale.getDefault(), getString(R.string.distancia_format), trilha.getDistanciaTotal()) + "\n" +
                String.format(Locale.getDefault(), getString(R.string.duracao_format), duracao);
        txtInfo.setText(info);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                recreate();
            }
        }
    }
}