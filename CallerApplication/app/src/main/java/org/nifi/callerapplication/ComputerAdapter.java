package org.nifi.callerapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

public class ComputerAdapter extends RecyclerView.Adapter<ComputerAdapter.ViewHolder> {

    private final List<Computer> computers;
    private final Context context;

    public ComputerAdapter(Context context, List<Computer> computers) {
        this.context = context;
        this.computers = computers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_computer, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Computer pc = computers.get(position);

        holder.ipText.setText(pc.ip);
        holder.infoText.setText(pc.networkName + (pc.name.equals(pc.networkName) ? "" : " - " + pc.name) + " - " + pc.os);

        if (pc.isOnline) {
            holder.statusIndicator.setBackgroundColor(Color.GREEN);
            holder.statusText.setText("ONLINE");
            holder.statusText.setTextColor(Color.GREEN);

            holder.itemView.setOnClickListener(v -> showCommandDialog(pc));
            holder.itemView.setAlpha(1.0f);
        } else {
            holder.statusIndicator.setBackgroundColor(Color.RED);
            holder.statusText.setText("OFFLINE");
            holder.statusText.setTextColor(Color.RED);

            holder.itemView.setOnClickListener(v -> showWolDialog(pc));
            holder.itemView.setAlpha(0.6f);
        }
    }

    private void showWolDialog(Computer pc) {
        new AlertDialog.Builder(context)
                .setTitle("Wake " + pc.networkName)
                .setMessage("Do you want to send a Wake-on-LAN packet to this computer?")
                .setPositiveButton("Wake Up", (dialog, which) -> {
                    ((MainActivity) context).sendWakeOnLan(pc);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showCommandDialog(Computer pc) {
        if (!pc.isOnline) {
            Toast.makeText(context, "Computer is offline", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] commands = {"Echo", "Restart", "Shutdown", "Restore"};

        new AlertDialog.Builder(context)
                .setTitle("Send command to " + pc.name)
                .setItems(commands, (dialog, which) -> sendCommand(pc.ip, commands[which]))
                .show();
    }

    private void sendCommand(String ip, String command) {
        new Thread(() -> {
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(ip, 41007), 300);

                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                out.println(command);
                String response = in.readLine();

                ((MainActivity) context).runOnUiThread(() ->
                        Toast.makeText(context, response != null ? response : "No response", Toast.LENGTH_SHORT).show()
                );
                if(command.equalsIgnoreCase("Restore")){
                    String response2 = in.readLine();

                    ((MainActivity) context).runOnUiThread(() ->
                            Toast.makeText(context, response2 != null ? response2 : "No response", Toast.LENGTH_SHORT).show()
                    );
                }

            } catch (Exception e) {
                ((MainActivity) context).runOnUiThread(() ->
                        Toast.makeText(context, "Failed to send command", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }

    @Override
    public int getItemCount() {
        return computers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView ipText, infoText, statusText;
        View statusIndicator;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ipText = itemView.findViewById(R.id.ipText);
            infoText = itemView.findViewById(R.id.infoText);
            statusText = itemView.findViewById(R.id.statusText);
            statusIndicator = itemView.findViewById(R.id.statusIndicator);
        }
    }
}