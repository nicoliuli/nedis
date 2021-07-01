package com.nedis.model;

/**
 * cmd和key
 */
public class CmdAndKey {
    private String cmd;

    private String key;

    public CmdAndKey(String cmd, String key) {
        this.cmd = cmd;
        this.key = key;
    }

    public String getCmd() {
        return cmd;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "CmdAndKey{" +
                "cmd='" + cmd + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
