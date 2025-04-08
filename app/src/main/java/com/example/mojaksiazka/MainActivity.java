package com.example.mojaksiazka;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

public class MainActivity extends AppCompatActivity {

    private Button descriptionButton;
    private Button saveBookButton;
    private Button remindAboutBookButton;
    private TextView addToList;
    private boolean switchState = true;

    public static final String CHANNEL_ID = "Default_channel";
    private static final String CHANNEL_NAME = "Kanał Powiadomień";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel(this);

        descriptionButton = findViewById(R.id.descriptionButton);
        saveBookButton = findViewById(R.id.saveBookButton);
        remindAboutBookButton = findViewById(R.id.remindAboutBookButton);
        addToList = findViewById(R.id.addToList);

        descriptionButton.setOnClickListener(v -> {
            sendNotification(1, CHANNEL_ID, this, this, "Moja książka", "Krótki opis: Ekscytująca historia pełna zwrotów akcji.");
        });
        saveBookButton.setOnClickListener(v -> {
            if(switchState) {
                addToList.setVisibility(View.VISIBLE);
                saveBookButton.setText("Usuń z chcę przeczytać");
                switchState = false;
            } else {
                addToList.setVisibility(View.GONE);
                saveBookButton.setText("Dodaj do chcę przeczytać");
                switchState = true;
            }
        });
        remindAboutBookButton.setOnClickListener(v -> {
            sendNotification(2, CHANNEL_ID, this, this, "Moja książka", "Pamiętaj, aby znaleźć czas na lekturę!");
        });
    }
    public static void createNotificationChannel(Context context) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationChannel channeldefault = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channeldefault);
            }
        }
    }
    public static void sendNotification(int NOTIFICATION_ID, String CHANNEL_ID, AppCompatActivity activity, Context context, String title, String message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 100);
                return;
            }
        }
        NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity, CHANNEL_ID)
                .setSmallIcon(R.drawable.book)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}