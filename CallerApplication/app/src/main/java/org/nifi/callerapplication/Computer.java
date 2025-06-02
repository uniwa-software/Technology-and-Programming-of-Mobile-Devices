package org.nifi.callerapplication;

public class Computer {
    public String ip;
    public String name;
    public String os;
    public boolean isOnline;

    public Computer(String ip, String name, String os, boolean isOnline) {
        this.ip = ip;
        this.name = name;
        this.os = os;
        this.isOnline = isOnline;
    }


    public Computer(String ip, boolean isOnline) {
        this.ip = ip;
        this.name = "Unknown";
        this.os = "Offline";
        this.isOnline = isOnline;
    }
}