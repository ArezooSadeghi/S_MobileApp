package com.example.sipmobileapp.model;

public class ServerData {

    private String mCenterName;
    private String mIPAddress;
    private String mPort;

    public ServerData() {

    }

    public ServerData(String centerName, String IPAddress, String port) {
        mCenterName = centerName;
        mIPAddress = IPAddress;
        mPort = port;
    }

    public String getCenterName() {
        return mCenterName;
    }

    public void setCenterName(String centerName) {
        mCenterName = centerName;
    }

    public String getIPAddress() {
        return mIPAddress;
    }

    public void setIPAddress(String IPAddress) {
        mIPAddress = IPAddress;
    }

    public String getPort() {
        return mPort;
    }

    public void setPort(String port) {
        mPort = port;
    }
}
