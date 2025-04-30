package org.nifi.callerapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Button;

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
            for (int i = 62; i <= 113; i++) {
                String host = "172.16.80." + i;

                try (Socket socket = new Socket()) {
                    socket.connect(new InetSocketAddress(host, 41007), 200);

                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out.println("Connected");

                    String response = in.readLine();

                    if (response != null && response.contains(" - ")) {
                        String[] parts = response.split(" - ");
                        String name = parts[0];
                        String os = parts[1];

                        Computer pc = new Computer(host, name, os);
                        runOnUiThread(() -> {
                            computers.add(pc);
                            adapter.notifyItemInserted(computers.size() - 1);
                        });
                    }

                } catch (Exception ignored) {}
            }
        }).start();
    }

}