package com.invent.inms.helper;

public class Hosts {

        private int hostId;
        private String hostIp;
        private int port;

        public Hosts() {}

        public Hosts(int hostId, String hostIp, int port) {
            // [START_EXCLUDE]
            this.hostId = hostId;
            this.hostIp = hostIp;
            this.port = port;
        }

        public String GetHostIp() {
            return hostIp;
        }

        public int GetHostId() {
            return hostId;
        }

        public int GetPort() {
            return port;
    }
}
