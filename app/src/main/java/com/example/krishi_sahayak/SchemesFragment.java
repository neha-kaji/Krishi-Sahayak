package com.example.krishi_sahayak;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SchemesFragment extends Fragment {

    private RecyclerView recyclerView;
    private SchemesAdapter adapter;
    private List<Scheme> schemeList = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schemes, container, false);

        FirebaseApp.initializeApp(requireContext());
        db = FirebaseFirestore.getInstance();

        recyclerView = view.findViewById(R.id.recyclerSchemes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SchemesAdapter(schemeList);
        recyclerView.setAdapter(adapter);

        loadSchemes();
        return view;
    }

    private void loadSchemes() {
        db.collection("Schemes") // 👈 use capital S
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    schemeList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Scheme scheme = doc.toObject(Scheme.class);
                        if (scheme != null) schemeList.add(scheme);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("SchemesFragment", "Error loading schemes", e);
                    Toast.makeText(getContext(), "Failed to load schemes", Toast.LENGTH_SHORT).show();
                });
    }
}
