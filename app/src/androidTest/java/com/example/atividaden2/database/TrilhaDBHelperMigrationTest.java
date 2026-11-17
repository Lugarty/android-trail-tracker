package com.example.atividaden2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class TrilhaDBHelperMigrationTest {

    @Test
    public void upgradingVersionOnePreservesDataAndEnablesIndexesAndCascade() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        String databaseName = "trilhas-migration-" + System.nanoTime() + ".db";
        SQLiteOpenHelper legacyHelper = new SQLiteOpenHelper(context, databaseName, null, 1) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL("CREATE TABLE trilha (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "nome TEXT NOT NULL, " +
                        "data_inicio TEXT NOT NULL, " +
                        "data_fim TEXT, " +
                        "velocidade_maxima REAL, " +
                        "velocidade_media REAL, " +
                        "distancia_total REAL)");
                db.execSQL("CREATE TABLE ponto_trilha (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "trilha_id INTEGER NOT NULL, " +
                        "latitude REAL NOT NULL, " +
                        "longitude REAL NOT NULL, " +
                        "timestamp TEXT NOT NULL, " +
                        "FOREIGN KEY(trilha_id) REFERENCES trilha(id) ON DELETE CASCADE)");
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                // The fixture intentionally represents the original version 1 schema.
            }
        };

        TrilhaDBHelper upgradedHelper = null;
        try {
            SQLiteDatabase legacyDb = legacyHelper.getWritableDatabase();
            ContentValues trail = new ContentValues();
            trail.put("nome", "Migration fixture");
            trail.put("data_inicio", "2026-01-01T10:00:00");
            long trailId = legacyDb.insertOrThrow("trilha", null, trail);

            ContentValues point = new ContentValues();
            point.put("trilha_id", trailId);
            point.put("latitude", -12.97);
            point.put("longitude", -38.50);
            point.put("timestamp", "2026-01-01T10:01:00");
            legacyDb.insertOrThrow("ponto_trilha", null, point);
            legacyHelper.close();

            upgradedHelper = new TrilhaDBHelper(context, databaseName);
            SQLiteDatabase upgradedDb = upgradedHelper.getWritableDatabase();

            assertEquals(1, countRows(upgradedDb, "trilha"));
            assertEquals(1, countRows(upgradedDb, "ponto_trilha"));
            assertTrue(hasIndex(upgradedDb, "idx_trilha_data_inicio"));
            assertTrue(hasIndex(upgradedDb, "idx_ponto_trilha_id_timestamp"));

            upgradedDb.delete("trilha", "id=?", new String[]{String.valueOf(trailId)});
            assertEquals(0, countRows(upgradedDb, "ponto_trilha"));
        } finally {
            legacyHelper.close();
            if (upgradedHelper != null) {
                upgradedHelper.close();
            }
            context.deleteDatabase(databaseName);
        }
    }

    private static int countRows(SQLiteDatabase db, String table) {
        try (Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + table, null)) {
            cursor.moveToFirst();
            return cursor.getInt(0);
        }
    }

    private static boolean hasIndex(SQLiteDatabase db, String indexName) {
        try (Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM sqlite_master WHERE type='index' AND name=?",
                new String[]{indexName})) {
            cursor.moveToFirst();
            return cursor.getInt(0) == 1;
        }
    }
}
