package com.example.krishi_sahayak;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.materialswitch.MaterialSwitch;

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Dark mode switch
        MaterialSwitch switchDark = view.findViewById(R.id.switchDarkMode);
        switchDark.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(getContext(),
                    isChecked ? "Dark mode enabled (preview)" : "Dark mode disabled",
                    Toast.LENGTH_SHORT).show();
        });

        // Feedback button
        MaterialButton feedbackBtn = view.findViewById(R.id.btnFeedback);
        feedbackBtn.setOnClickListener(v -> {
            Intent email = new Intent(Intent.ACTION_SENDTO);
            email.setData(Uri.parse("mailto:krishisahayak.support@gmail.com"));
            email.putExtra(Intent.EXTRA_SUBJECT, "Feedback for Krishi Sahayak");
            startActivity(Intent.createChooser(email, "Send Feedback"));
        });

        // About card
        MaterialCardView aboutCard = view.findViewById(R.id.cardAbout);
        aboutCard.setOnClickListener(v ->
                Toast.makeText(getContext(),
                        "Krishi Sahayak v1.0\nHelping farmers access schemes easily.",
                        Toast.LENGTH_LONG).show());

        return view;
    }
}
