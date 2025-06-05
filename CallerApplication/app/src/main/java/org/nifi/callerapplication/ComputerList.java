package org.nifi.callerapplication;

import java.util.ArrayList;
import java.util.List;

public class ComputerList {
    public static List<Computer> getPredefinedComputers() {
        List<Computer> computers = new ArrayList<>();

        computers.add(new Computer("10.11.21.29", "ON0005", "80:6D:97:5C:2D:25", false));

        computers.add(new Computer("192.168.88.2", "PRPC01", "50:81:40:2B:91:8D", false));
        computers.add(new Computer("192.168.88.3", "PRPC02", "50:81:40:2B:7C:78", false));
        computers.add(new Computer("192.168.88.4", "PRPC03", "50:81:40:2B:78:DD", false));
        computers.add(new Computer("192.168.88.5", "PRPC04", "50:81:40:2B:7B:3D", false));
        computers.add(new Computer("192.168.88.6", "PRPC05", "50:81:40:2B:79:91", false));
        computers.add(new Computer("192.168.88.7", "PRPC06", "C8:5A:CF:0F:76:3D", false));
        computers.add(new Computer("192.168.88.8", "PRPC07", "C8:5A:CF:0D:71:24", false));
        computers.add(new Computer("192.168.88.9", "PRPC08", "C8:5A:CF:0F:B3:FF", false));
        computers.add(new Computer("192.168.88.10", "PRPC09", "C8:5A:CF:0E:2C:C4", false));
        computers.add(new Computer("192.168.88.11", "PRPC10", "C8:5A:CF:0F:7C:D0", false));
        computers.add(new Computer("192.168.88.12", "PRPC11", "C8:5A:CF:0D:71:3A", false));
        computers.add(new Computer("192.168.88.13", "PRPC12", "C8:5A:CF:0F:EE:01", false));
        computers.add(new Computer("192.168.88.14", "PRPC13", "C8:5A:CF:0E:1D:88", false));
        computers.add(new Computer("192.168.88.15", "PRPC14", "C8:5A:CF:0F:F0:1E", false));
        computers.add(new Computer("192.168.88.16", "PRPC15", "50:81:40:2B:7D:A4", false));
        computers.add(new Computer("192.168.88.17", "PRPC16", "C8:5A:CF:0E:2C:78", false));
        computers.add(new Computer("192.168.88.18", "PRPC17", "50:81:40:2B:87:F4", false));
        computers.add(new Computer("192.168.88.19", "PRPC18", "C8:5A:CF:0F:EC:11", false));
        computers.add(new Computer("192.168.88.20", "PRPC19", "C8:5A:CF:0F:7C:1F", false));
        computers.add(new Computer("192.168.88.21", "PRPC20", "C8:5A:CF:0D:71:2C", false));
        computers.add(new Computer("192.168.88.22", "PRPC21", "C8:5A:CF:0D:70:95", false));
        computers.add(new Computer("192.168.88.23", "PRPC22", "50:81:40:2B:5F:D0", false));
        computers.add(new Computer("192.168.88.24", "PRPC23", "50:81:40:2B:7A:0B", false));
        computers.add(new Computer("192.168.88.25", "PRPC24", "50:81:40:2B:8F:D3", false));
        computers.add(new Computer("192.168.88.26", "PRPC25", "50:81:40:2B:72:E0", false));
        computers.add(new Computer("192.168.88.27", "PRPC26", "50:81:40:2B:7A:74", false));
        computers.add(new Computer("192.168.88.28", "PRPC27DESK", "C8:5A:CF:0F:7C:D4", false));
        
        return computers;
    }
} 