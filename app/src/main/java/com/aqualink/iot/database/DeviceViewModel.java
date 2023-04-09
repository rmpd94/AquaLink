package com.aqualink.iot.database;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.aqualink.iot.database.Device;
import com.aqualink.iot.database.DeviceRepository;

import java.util.List;

public class DeviceViewModel extends AndroidViewModel  {
    private DeviceRepository repository;
    private LiveData<List<Device>> allDevices;
    private LiveData<List<Device>> allAPDevices;
    private LiveData<List<Device>> allRGBDevices;
    public DeviceViewModel(@NonNull Application application) {
        super(application);
        repository = new DeviceRepository(application);
        allDevices = repository.getAllDevices();
        allAPDevices = repository.getAllAPDevices();
        allRGBDevices = repository.getAllRGBDevices();
    }
    public void insert(Device device) {
        repository.insert(device);
    }
    public void update(Device device) {
        repository.update(device);
    }
    public void delete(Device device) {
        repository.delete(device);
    }
    public void deleteAllDevices() {
        repository.deleteAllDevices();
    }
    public LiveData<List<Device>> getAllDevices() {
        return allDevices;
    }
    public LiveData<List<Device>> getAllAPDevices() {
        return allAPDevices;
    }
    public LiveData<List<Device>> getAllRGBDevices() {
        return allRGBDevices;
    }
}
