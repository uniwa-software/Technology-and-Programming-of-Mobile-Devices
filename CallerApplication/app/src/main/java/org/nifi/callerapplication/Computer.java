package org.nifi.callerapplication;

public class Computer {
    public String ip;
    public String name;
    public String os;
    public boolean isOnline;
    public String macAddress;
    public String networkName;

    public Computer(String ip, String networkName, String macAddress, boolean isOnline) {
        this.ip = ip;
        this.networkName = networkName;
        this.macAddress = macAddress;
        this.name = networkName;
        this.os = "Unknown";
        this.isOnline = isOnline;
    }

    public Computer(String ip, String name, String os, String macAddress, boolean isOnline) {
        this.ip = ip;
        this.name = name;
        this.networkName = name;
        this.os = os;
        this.macAddress = macAddress;
        this.isOnline = isOnline;
    }

    public Computer(String ip, boolean isOnline) {
        this.ip = ip;
        this.name = "Unknown";
        this.networkName = "Unknown";
        this.os = "Offline";
        this.isOnline = isOnline;
        this.macAddress = "";
    }
}