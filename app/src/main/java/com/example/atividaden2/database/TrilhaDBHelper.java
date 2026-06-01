package com.example.atividaden2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class TrilhaDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "trilhas.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_TRILHA = "trilha";
    private static final String COL_ID = "id";
    private static final String COL_NOME = "nome";
    private static final String COL_DATA_INICIO = "data_inicio";
    private static final String COL_DATA_FIM = "data_fim";
    private static final String COL_VEL_MAX = "velocidade_maxima";
    private static final String COL_VEL_MEDIA = "velocidade_media";
    private static final String COL_DISTANCIA = "distancia_total";

    private static final String TABLE_PONTO = "ponto_trilha";
    private static final String COL_PONTO_ID = "id";
    private static final String COL_TRILHA_ID = "trilha_id";
    private static final String COL_LAT = "latitude";
    private static final String COL_LNG = "longitude";
    private static final String COL_TIMESTAMP = "timestamp";

    public TrilhaDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTrilha = "CREATE TABLE " + TABLE_TRILHA + "(" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NOME + " TEXT NOT NULL, " +
                COL_DATA_INICIO + " TEXT NOT NULL, " +
                COL_DATA_FIM + " TEXT, " +
                COL_VEL_MAX + " REAL, " +
                COL_VEL_MEDIA + " REAL, " +
                COL_DISTANCIA + " REAL" + ")";
        db.execSQL(createTrilha);

        String createPonto = "CREATE TABLE " + TABLE_PONTO + "(" +
                COL_PONTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TRILHA_ID + " INTEGER NOT NULL, " +
                COL_LAT + " REAL NOT NULL, " +
                COL_LNG + " REAL NOT NULL, " +
                COL_TIMESTAMP + " TEXT NOT NULL, " +
                "FOREIGN KEY(" + COL_TRILHA_ID + ") REFERENCES " + TABLE_TRILHA + "(" + COL_ID + ") ON DELETE CASCADE)";
        db.execSQL(createPonto);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PONTO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRILHA);
        onCreate(db);
    }

    public long inserirTrilha(Trilha trilha) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NOME, trilha.getNome());
        values.put(COL_DATA_INICIO, trilha.getDataInicio());
        values.put(COL_DATA_FIM, trilha.getDataFim());
        values.put(COL_VEL_MAX, trilha.getVelocidadeMaxima());
        values.put(COL_VEL_MEDIA, trilha.getVelocidadeMedia());
        values.put(COL_DISTANCIA, trilha.getDistanciaTotal());
        long id = db.insert(TABLE_TRILHA, null, values);
        db.close();
        return id;
    }

    public void atualizarTrilha(Trilha trilha) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_DATA_FIM, trilha.getDataFim());
        values.put(COL_VEL_MAX, trilha.getVelocidadeMaxima());
        values.put(COL_VEL_MEDIA, trilha.getVelocidadeMedia());
        values.put(COL_DISTANCIA, trilha.getDistanciaTotal());
        db.update(TABLE_TRILHA, values, COL_ID + "=?", new String[]{String.valueOf(trilha.getId())});
        db.close();
    }

    public List<Trilha> listarTodasTrilhas() {
        List<Trilha> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_TRILHA, null, null, null, null, null, COL_DATA_INICIO + " DESC");
        if (cursor.moveToFirst()) {
            do {
                Trilha t = new Trilha();
                t.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
                t.setNome(cursor.getString(cursor.getColumnIndexOrThrow(COL_NOME)));
                t.setDataInicio(cursor.getString(cursor.getColumnIndexOrThrow(COL_DATA_INICIO)));
                t.setDataFim(cursor.getString(cursor.getColumnIndexOrThrow(COL_DATA_FIM)));
                t.setVelocidadeMaxima(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_VEL_MAX)));
                t.setVelocidadeMedia(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_VEL_MEDIA)));
                t.setDistanciaTotal(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_DISTANCIA)));
                lista.add(t);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }

    public void deletarTrilha(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_TRILHA, COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deletarTodasTrilhas() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_TRILHA, null, null);
        db.close();
    }

    public void editarNomeTrilha(int id, String novoNome) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NOME, novoNome);
        db.update(TABLE_TRILHA, values, COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void inserirPonto(PontoTrilha ponto) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TRILHA_ID, ponto.getTrilhaId());
        values.put(COL_LAT, ponto.getLatitude());
        values.put(COL_LNG, ponto.getLongitude());
        values.put(COL_TIMESTAMP, ponto.getTimestamp());
        db.insert(TABLE_PONTO, null, values);
        db.close();
    }

    public List<PontoTrilha> listarPontosPorTrilha(int trilhaId) {
        List<PontoTrilha> pontos = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_PONTO, null, COL_TRILHA_ID + "=?", new String[]{String.valueOf(trilhaId)}, null, null, COL_TIMESTAMP);
        if (cursor.moveToFirst()) {
            do {
                PontoTrilha p = new PontoTrilha();
                p.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_PONTO_ID)));
                p.setTrilhaId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_TRILHA_ID)));
                p.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_LAT)));
                p.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_LNG)));
                p.setTimestamp(cursor.getString(cursor.getColumnIndexOrThrow(COL_TIMESTAMP)));
                pontos.add(p);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return pontos;
    }
}