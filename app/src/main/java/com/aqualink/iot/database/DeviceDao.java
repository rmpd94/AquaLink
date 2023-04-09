package com.aqualink.iot.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface DeviceDao {

    @Insert
    void insert(Device device);
    @Update
    void update(Device device);
    @Delete
    void delete(Device device);
    @Query("DELETE FROM device_table")
    void deleteAllDevices();
    @Query("SELECT * FROM device_table WHERE deviceMode ='ST' ORDER BY id ASC")
    LiveData<List<Device>> getAllDevices();
    @Query("SELECT * FROM device_table WHERE deviceMode ='AP' ORDER BY id ASC")
    LiveData<List<Device>> getAllAPDevices();
@Query("SELECT * FROM device_table WHERE deviceMode ='LT' ORDER BY id ASC")
    LiveData<List<Device>> getAllRGBDevices();
}
