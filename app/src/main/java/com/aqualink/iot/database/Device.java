package com.aqualink.iot.database;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "device_table")
public class Device {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String deviceId;
    private String deviceName;
    private String fbHost;
    private String fbAuth;
    private String deviceMode;
    private String localHost;

    public Device(String deviceId, String deviceName, String fbHost,String fbAuth, String deviceMode, String localHost ) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.fbHost = fbHost;
        this.fbAuth = fbAuth;
        this.deviceMode = deviceMode;
        this.localHost = localHost;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public String getDeviceId() { return deviceId;}
    public String getDeviceName() {
        return deviceName;
    }
    public String getFbHost() {
        return fbHost;
    }
    public String getFbAuth(){return fbAuth;}
    public String getDeviceMode(){return deviceMode;}
    public String getLocalHost(){return localHost;}
}