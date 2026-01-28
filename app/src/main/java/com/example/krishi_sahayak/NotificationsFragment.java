package com.example.krishi_sahayak;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private NotificationsAdapter adapter;
    private List<Scheme> notificationsList = new ArrayList<>();

    private static final String CHANNEL_ID = "scheme_updates_channel";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        recyclerView = view.findViewById(R.id.recyclerNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new NotificationsAdapter(notificationsList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        createNotificationChannel();

        // Add default notification
        addInitialNotification();

        // Listen for new schemes
        listenForNewSchemes();

        return view;
    }

    private void addInitialNotification() {
        notificationsList.add(new Scheme(
                "  Welcome to Krishi Sahayak 🌾",
                "  You’ll be notified here when new government  schemes are added.",
                "",
                ""
        ));
        adapter.notifyItemInserted(notificationsList.size() - 1);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Scheme Updates";
            String description = "Notifies when new schemes are added";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager =
                    requireContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void listenForNewSchemes() {
        db.collection("schemes")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e("Firestore", "Listen failed.", error);
                        Toast.makeText(getContext(), "Error loading notifications.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (value == null) return;

                    for (DocumentChange dc : value.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            String schemeName = dc.getDocument().getString("name");
                            String schemeDesc = dc.getDocument().getString("description");

                            if (schemeName == null) schemeName = "Unnamed Scheme";
                            if (schemeDesc == null) schemeDesc = "No description available.";

                            notificationsList.add(new Scheme(schemeName, schemeDesc, "", ""));
                            adapter.notifyItemInserted(notificationsList.size() - 1);

                            // ✅ Properly call the notification method here
                            sendNotification(schemeName, schemeDesc);
                        }
                    }
                });
    }

    private void sendNotification(@NonNull String title, @NonNull String message) {
        if (getContext() == null) return;

        // ✅ Check permission at runtime for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (requireContext().checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission not granted; don’t send notification
                return;
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle("New Scheme Added: " + title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(requireContext());
        notificationManager.notify((int) (System.currentTimeMillis() & 0xfffffff), builder.build());
    }

}
