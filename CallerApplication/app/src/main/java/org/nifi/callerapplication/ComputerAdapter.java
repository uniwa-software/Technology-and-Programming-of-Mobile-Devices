package org.nifi.callerapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
        holder.infoText.setText(pc.name + " - " + pc.os);

        holder.itemView.setOnClickListener(v -> showCommandDialog(pc));
    }

    private void showCommandDialog(Computer pc) {
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
        TextView ipText, infoText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ipText = itemView.findViewById(R.id.ipText);
            infoText = itemView.findViewById(R.id.infoText);
        }
    }
}
