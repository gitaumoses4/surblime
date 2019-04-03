package com.mg.surblime.events;

/**
 * Created by moses on 5/6/18.
 */

public class NetworkConnectionInfo {
    private final boolean connected;

    public NetworkConnectionInfo(boolean connected) {
        this.connected = connected;
    }

    public boolean isConnected() {
        return connected;
    }
}
