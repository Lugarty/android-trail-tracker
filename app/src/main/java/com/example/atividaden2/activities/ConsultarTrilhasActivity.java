package com.example.atividaden2.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.atividaden2.R;
import com.example.atividaden2.adapter.TrilhaAdapter;
import com.example.atividaden2.database.Trilha;
import com.example.atividaden2.database.TrilhaDBHelper;
import java.util.List;

public class ConsultarTrilhasActivity extends AppCompatActivity implements TrilhaAdapter.OnTrilhaListener {

    private RecyclerView rv;
    private TrilhaDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_trilhas);
        rv = findViewById(R.id.rvTrilhas);
        rv.setLayoutManager(new LinearLayoutManager(this));
        dbHelper = new TrilhaDBHelper(this);
        carregarLista();

        Button btnApagarTudo = findViewById(R.id.btnApagarTudo);
        btnApagarTudo.setOnClickListener(v -> {
            dbHelper.deletarTodasTrilhas();
            carregarLista();
        });
    }

    private void carregarLista() {
        List<Trilha> lista = dbHelper.listarTodasTrilhas();
        TrilhaAdapter adapter = new TrilhaAdapter(lista, this);
        rv.setAdapter(adapter);
    }

    @Override
    public void onEditar(Trilha trilha) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar nome");
        EditText input = new EditText(this);
        input.setText(trilha.getNome());
        builder.setView(input);
        builder.setPositiveButton("Salvar", (dialog, which) -> {
            String novoNome = input.getText().toString();
            if (!novoNome.isEmpty()) {
                dbHelper.editarNomeTrilha(trilha.getId(), novoNome);
                carregarLista();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    @Override
    public void onApagar(Trilha trilha) {
        new AlertDialog.Builder(this)
                .setTitle("Apagar trilha")
                .setMessage("Tem certeza?")
                .setPositiveButton("Sim", (d, w) -> {
                    dbHelper.deletarTrilha(trilha.getId());
                    carregarLista();
                })
                .setNegativeButton("Não", null)
                .show();
    }

    @Override
    public void onVisualizar(Trilha trilha) {
        Intent intent = new Intent(this, VisualizarTrilhaActivity.class);
        intent.putExtra("trilha_id", trilha.getId());
        startActivity(intent);
    }
}