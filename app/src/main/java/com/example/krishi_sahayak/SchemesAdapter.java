package com.example.krishi_sahayak;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SchemesAdapter extends RecyclerView.Adapter<SchemesAdapter.SchemeViewHolder> {

    private List<Scheme> schemeList;

    public SchemesAdapter(List<Scheme> schemeList) {
        this.schemeList = schemeList;
    }

    @NonNull
    @Override
    public SchemeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scheme, parent, false);
        return new SchemeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SchemeViewHolder holder, int position) {
        Scheme scheme = schemeList.get(position);
        holder.txtName.setText(scheme.getName());
        holder.txtDesc.setText(scheme.getDescription());

        holder.btnLink.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(scheme.getLink()));
            v.getContext().startActivity(intent);
        });

        holder.btnVideo.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(scheme.getVideo()));
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return schemeList.size();
    }

    public static class SchemeViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtDesc;
        Button btnLink, btnVideo;

        public SchemeViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtSchemeName);
            txtDesc = itemView.findViewById(R.id.txtSchemeDesc);
            btnLink = itemView.findViewById(R.id.btnSchemeLink);
            btnVideo = itemView.findViewById(R.id.btnSchemeVideo);
        }
    }
}
