package com.example.atividaden2.activities;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.atividaden2.R;
import com.example.atividaden2.database.PontoTrilha;
import com.example.atividaden2.database.Trilha;
import com.example.atividaden2.database.TrilhaDBHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class RegistrarTrilhaActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Location lastLocation;
    private Marker userMarker;
    private Circle accuracyCircle;

    private TextView txtVelocidade, txtVelMax, txtCronometro, txtDistancia;
    private Button btnIniciarParar;

    private TrilhaDBHelper dbHelper;
    private long trilhaId = -1;
    private boolean isRecording = false;

    private double velocidadeMaxima = 0.0;
    private float distanciaTotal = 0.0f;
    private long startTime = 0;
    private final Handler cronometroHandler = new Handler();
    private Runnable cronometroRunnable;

    private SimpleDateFormat sdf;
    private static final int LOCATION_PERMISSION_REQUEST = 100;
    private boolean mapaPronto = false;
    private boolean aguardandoPermissao = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_trilha);

        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));

        txtVelocidade = findViewById(R.id.txtVelocidade);
        txtVelMax = findViewById(R.id.txtVelMax);
        txtCronometro = findViewById(R.id.txtCronometro);
        txtDistancia = findViewById(R.id.txtDistancia);
        btnIniciarParar = findViewById(R.id.btnIniciarParar);

        dbHelper = new TrilhaDBHelper(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Toast.makeText(this, R.string.erro_carregar_mapa, Toast.LENGTH_SHORT).show();
        }

        btnIniciarParar.setOnClickListener(v -> {
            if (isRecording) {
                pararTrilha();
            } else {
                verificarPermissaoEIniciar();
            }
        });

        configurarLocationCallback();
    }

    private void configurarLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    try {
                        atualizarUIComLocalizacao(location);
                    } catch (Exception e) {
                        Toast.makeText(RegistrarTrilhaActivity.this,
                                String.format(getString(R.string.erro_formato), e.getMessage()),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
    }

    private void verificarPermissaoEIniciar() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
            aguardandoPermissao = true;
        } else {
            iniciarTrilha();
        }
    }

    private BitmapDescriptor criarMarcadorPersonalizado() {
        int size = 80;
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(size / 2f, size / 2f, size / 2f - 2, paint);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        canvas.drawCircle(size / 2f, size / 2f, size / 2f - 2, paint);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(size / 2f, size / 2f, 8, paint);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void atualizarUIComLocalizacao(Location location) {
        if (location == null || mMap == null) return;

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        float velocidade = location.getSpeed() * 3.6f;
        txtVelocidade.setText(String.format(Locale.getDefault(), getString(R.string.velocidade), velocidade));
        if (velocidade > velocidadeMaxima) {
            velocidadeMaxima = velocidade;
            txtVelMax.setText(String.format(Locale.getDefault(), getString(R.string.velocidade_maxima), velocidadeMaxima));
        }

        if (lastLocation != null && isRecording) {
            float delta = lastLocation.distanceTo(location);
            distanciaTotal += delta;
            txtDistancia.setText(String.format(Locale.getDefault(), getString(R.string.distancia), distanciaTotal));
        }
        lastLocation = location;

        if (userMarker == null) {
            userMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(criarMarcadorPersonalizado())
                    .anchor(0.5f, 0.5f)
                    .title(getString(R.string.voce)));
        } else {
            userMarker.setPosition(latLng);
        }

        if (accuracyCircle == null) {
            accuracyCircle = mMap.addCircle(new CircleOptions()
                    .center(latLng)
                    .radius(location.getAccuracy())
                    .strokeColor(0xCC0000FF)
                    .fillColor(0x330000FF));
        } else {
            accuracyCircle.setCenter(latLng);
            accuracyCircle.setRadius(location.getAccuracy());
        }

        SharedPreferences prefs = getSharedPreferences("app_config", MODE_PRIVATE);
        int navegacao = prefs.getInt("navegacao", 0);
        float bearing = (navegacao == 1) ? location.getBearing() : 0f;

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                com.google.android.gms.maps.model.CameraPosition.builder()
                        .target(latLng)
                        .zoom(18f)
                        .bearing(bearing)
                        .build()));

        if (isRecording && trilhaId != -1) {
            PontoTrilha ponto = new PontoTrilha();
            ponto.setTrilhaId((int) trilhaId);
            ponto.setLatitude(location.getLatitude());
            ponto.setLongitude(location.getLongitude());
            ponto.setTimestamp(sdf.format(new Date()));
            dbHelper.inserirPonto(ponto);
        }
    }

    private void iniciarTrilha() {
        isRecording = true;
        startTime = SystemClock.elapsedRealtime();
        distanciaTotal = 0;
        velocidadeMaxima = 0;
        lastLocation = null;

        Trilha trilha = new Trilha();
        String nome = "Trilha " + sdf.format(new Date());
        trilha.setNome(nome);
        trilha.setDataInicio(sdf.format(new Date()));
        trilha.setDataFim(null);
        trilha.setVelocidadeMaxima(0);
        trilha.setVelocidadeMedia(0);
        trilha.setDistanciaTotal(0);
        trilhaId = dbHelper.inserirTrilha(trilha);

        iniciarCronometro();
        iniciarAtualizacoesLocalizacao();
        btnIniciarParar.setText(R.string.parar);
        btnIniciarParar.setBackgroundTintList(getColorStateList(android.R.color.holo_red_dark));
        Toast.makeText(this, R.string.gravacao_iniciada, Toast.LENGTH_SHORT).show();
    }

    private void pararTrilha() {
        isRecording = false;
        pararAtualizacoesLocalizacao();
        cronometroHandler.removeCallbacks(cronometroRunnable);

        if (trilhaId != -1) {
            Trilha trilha = new Trilha();
            trilha.setId((int) trilhaId);
            trilha.setDataFim(sdf.format(new Date()));
            trilha.setVelocidadeMaxima(velocidadeMaxima);
            long elapsed = SystemClock.elapsedRealtime() - startTime;
            double horas = elapsed / 3600000.0;
            double velMedia = (horas > 0) ? (distanciaTotal / 1000.0) / horas : 0;
            trilha.setVelocidadeMedia(velMedia);
            trilha.setDistanciaTotal(distanciaTotal);
            dbHelper.atualizarTrilha(trilha);
        }

        trilhaId = -1;
        btnIniciarParar.setText(R.string.iniciar);
        btnIniciarParar.setBackgroundTintList(getColorStateList(android.R.color.holo_green_dark));
        Toast.makeText(this, R.string.trilha_salva, Toast.LENGTH_SHORT).show();
    }

    private void iniciarCronometro() {
        cronometroRunnable = new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.elapsedRealtime() - startTime;
                int horas = (int) (elapsed / 3600000);
                int minutos = (int) ((elapsed % 3600000) / 60000);
                int segundos = (int) ((elapsed % 60000) / 1000);
                txtCronometro.setText(String.format(Locale.getDefault(), "%02d:%02d:%02d", horas, minutos, segundos));
                cronometroHandler.postDelayed(this, 1000);
            }
        };
        cronometroHandler.post(cronometroRunnable);
    }

    private void iniciarAtualizacoesLocalizacao() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationRequest request = new LocationRequest.Builder(2000)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .build();
        fusedLocationClient.requestLocationUpdates(request, locationCallback, null);
    }

    private void pararAtualizacoesLocalizacao() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mapaPronto = true;

        SharedPreferences prefs = getSharedPreferences("app_config", MODE_PRIVATE);
        int tipoMapa = prefs.getInt("tipo_mapa", 1);
        mMap.setMapType(tipoMapa == 1 ? GoogleMap.MAP_TYPE_NORMAL : GoogleMap.MAP_TYPE_SATELLITE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }

        if (aguardandoPermissao && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            aguardandoPermissao = false;
            iniciarTrilha();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, R.string.permissao_concedida, Toast.LENGTH_SHORT).show();
                if (mapaPronto) {
                    iniciarTrilha();
                } else {
                    aguardandoPermissao = true;
                }
            } else {
                Toast.makeText(this, R.string.permissao_necessaria, Toast.LENGTH_SHORT).show();
            }
        }
    }
}