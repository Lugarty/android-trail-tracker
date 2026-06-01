package com.example.atividaden2.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.atividaden2.R;
import com.example.atividaden2.database.Trilha;
import java.util.List;

public class TrilhaAdapter extends RecyclerView.Adapter<TrilhaAdapter.ViewHolder> {

    private final List<Trilha> lista;
    private final OnTrilhaListener listener;

    public interface OnTrilhaListener {
        void onEditar(Trilha trilha);
        void onApagar(Trilha trilha);
        void onVisualizar(Trilha trilha);
    }

    public TrilhaAdapter(List<Trilha> lista, OnTrilhaListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trilha, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trilha t = lista.get(position);
        holder.txtNome.setText(t.getNome());
        holder.txtData.setText(t.getDataInicio());
        holder.btnEditar.setOnClickListener(v -> listener.onEditar(t));
        holder.btnApagar.setOnClickListener(v -> listener.onApagar(t));
        holder.btnVisualizar.setOnClickListener(v -> listener.onVisualizar(t));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtNome, txtData;
        public Button btnEditar, btnApagar, btnVisualizar;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNome = itemView.findViewById(R.id.txtNomeTrilha);
            txtData = itemView.findViewById(R.id.txtDataInicio);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnApagar = itemView.findViewById(R.id.btnApagar);
            btnVisualizar = itemView.findViewById(R.id.btnVisualizar);
        }
    }
}