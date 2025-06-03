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

        // Initialize with predefined computers
        computers.addAll(ComputerList.getPredefinedComputers());
        adapter.notifyDataSetChanged();
        
        // Start initial scan
        scanNetwork();
    }

    private void scanNetwork() {
        new Thread(() -> {
            for (int i = 0; i < computers.size(); i++) {
                final Computer computer = computers.get(i);
                testConnection(computer, i);
            }
        }).start();
    }

    private void testConnection(Computer computer, int index) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(computer.ip, 41007), 500);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            out.println("Echo");
            String response = in.readLine();

            if (response != null && response.contains(" - ")) {
                String[] parts = response.split(" - ");
                String name = parts[0];
                String os = parts[1];

                runOnUiThread(() -> {
                    computer.name = name;
                    computer.os = os;
                    computer.isOnline = true;
                    adapter.notifyItemChanged(index);
                });

                Log.d("ServerFound", "Connected to: " + name + " (" + os + ") at " + computer.ip);
            }

        } catch (Exception e) {
            runOnUiThread(() -> {
                computer.isOnline = false;
                computer.os = "Offline";
                adapter.notifyItemChanged(index);
            });
            Log.d("NetworkScan", "No server at " + computer.ip + ": " + e.getMessage());
        }
    }

    public void sendWakeOnLan(Computer computer) {
        if (computer.macAddress.isEmpty()) {
            Toast.makeText(this, "No MAC address available for this computer", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            WolUtil.sendWakeOnLan(computer.macAddress, computer.ip);
            runOnUiThread(() -> 
                Toast.makeText(MainActivity.this, 
                    "Wake-on-LAN packet sent to " + computer.networkName, 
                    Toast.LENGTH_SHORT).show()
            );
        }).start();
    }
}