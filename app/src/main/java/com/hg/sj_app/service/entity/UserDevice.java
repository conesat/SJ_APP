package com.hg.sj_app.service.entity;

public class UserDevice {
    private String id;

    private String userId;

    private String deviceId;

    private String deviceWifiMac;

    private String deviceBluetoothMac;

    private Integer deviceScreenWidth;

    private Integer deviceScreenHeight;

    private String deviceName;

    private String deviceBrand;

    private String deviceModel;

    public UserDevice() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceWifiMac() {
        return deviceWifiMac;
    }

    public void setDeviceWifiMac(String deviceWifiMac) {
        this.deviceWifiMac = deviceWifiMac;
    }

    public String getDeviceBluetoothMac() {
        return deviceBluetoothMac;
    }

    public void setDeviceBluetoothMac(String deviceBluetoothMac) {
        this.deviceBluetoothMac = deviceBluetoothMac;
    }

    public Integer getDeviceScreenWidth() {
        return deviceScreenWidth;
    }

    public void setDeviceScreenWidth(Integer deviceScreenWidth) {
        this.deviceScreenWidth = deviceScreenWidth;
    }

    public Integer getDeviceScreenHeight() {
        return deviceScreenHeight;
    }

    public void setDeviceScreenHeight(Integer deviceScreenHeight) {
        this.deviceScreenHeight = deviceScreenHeight;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceBrand() {
        return deviceBrand;
    }

    public void setDeviceBrand(String deviceBrand) {
        this.deviceBrand = deviceBrand;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }
}