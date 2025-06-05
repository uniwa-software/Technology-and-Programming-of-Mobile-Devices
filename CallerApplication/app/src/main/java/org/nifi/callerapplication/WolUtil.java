package org.nifi.callerapplication;

import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class WolUtil {
    private static final int PORT = 9;

    public static void sendWakeOnLan(String macAddress, String ipAddress) {
        try {
            byte[] macBytes = getMacBytes(macAddress);
            byte[] bytes = new byte[6 + 16 * macBytes.length];

            // 6 X 0xFF
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) 0xFF;
            }

            for (int i = 6; i < bytes.length; i += macBytes.length) {
                System.arraycopy(macBytes, 0, bytes, i, macBytes.length);
            }

            String broadcastAddress = getBroadcastAddress(ipAddress);

            InetAddress address = InetAddress.getByName(broadcastAddress);
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, PORT);
            DatagramSocket socket = new DatagramSocket();
            socket.setBroadcast(true);
            socket.send(packet);
            socket.close();

            Log.d("WolUtil", "Sent WOL packet to " + macAddress + " via broadcast " + broadcastAddress);
        } catch (Exception e) {
            Log.e("WolUtil", "Failed to send WOL packet: " + e.getMessage());
        }
    }

    private static String getBroadcastAddress(String ipAddress) {
        String[] parts = ipAddress.split("\\.");
        if (parts.length == 4) {
            return parts[0] + "." + parts[1] + "." + parts[2] + ".255";
        }
        return "255.255.255.255";
    }

    private static byte[] getMacBytes(String macAddress) throws IllegalArgumentException {
        byte[] bytes = new byte[6];
        String[] hex = macAddress.split("[:-]");
        if (hex.length != 6) {
            throw new IllegalArgumentException("Invalid MAC address format");
        }
        try {
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) Integer.parseInt(hex[i], 16);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid hex digit in MAC address");
        }
        return bytes;
    }
}