package org.nifi.callerapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button scanButton;
    List<Computer> computers = new ArrayList<>();
    ComputerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        recyclerView = findViewById(R.id.recyclerView);
        scanButton = findViewById(R.id.scanButton);

        adapter = new ComputerAdapter(this, computers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        scanButton.setOnClickListener(v -> scanNetwork());

        scanNetwork();
    }
    private void scanNetwork() {
        computers.clear();
        adapter.notifyDataSetChanged();

        new Thread(() -> {
            scanNetworkRange();
        }).start();
    }

    private void scanNetworkRange() {
        for (int i = 1; i <= 27; i++) {
            String host = "10.0.2" + "." + i;

            Computer offlinePC = new Computer(host, false);

            runOnUiThread(() -> {
                computers.add(offlinePC);
                adapter.notifyItemInserted(computers.size() - 1);
            });
        }

        for (int i = 0; i < computers.size(); i++) {
            testConnection(computers.get(i).ip, i);
        }
    }

    private void testConnection(String ip, int index) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(ip, 41007), 500);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println("Connected");

            String response = in.readLine();

            if (response != null && response.contains(" - ")) {
                String[] parts = response.split(" - ");
                String name = parts[0];
                String os = parts[1];

                runOnUiThread(() -> {
                    if (index < computers.size()) {
                        computers.set(index, new Computer(ip, name, os, true));
                        adapter.notifyItemChanged(index);

                        Toast.makeText(MainActivity.this,
                                "Found server: " + name + " at " + ip,
                                Toast.LENGTH_SHORT).show();
                    }
                });

                Log.d("ServerFound", "Connected to: " + name + " (" + os + ") at " + ip);
            }

        } catch (Exception e) {
            Log.d("NetworkScan", "No server at " + ip + ": " + e.getMessage());
        }
    }
}