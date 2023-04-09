package com.aqualink.iot.database;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.aqualink.iot.R;

public class DeviceAdapter extends ListAdapter<Device, DeviceAdapter.DeviceHolder> {
    private OnItemClickListener listener;
    public DeviceAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Device> DIFF_CALLBACK = new DiffUtil.ItemCallback<Device>() {
        @Override
        public boolean areItemsTheSame(Device oldItem, Device newItem) {
            return oldItem.getId() == newItem.getId();
        }
        @Override
        public boolean areContentsTheSame(Device oldItem, Device newItem) {
            return  oldItem.getDeviceId().equals(newItem.getDeviceId()) &&
                    oldItem.getDeviceName().equals(newItem.getDeviceName()) &&
                    oldItem.getFbHost().equals(newItem.getFbHost()) &&
                    oldItem.getFbAuth().equals(newItem.getFbAuth())&&
                    oldItem.getDeviceMode().equals(newItem.getDeviceMode())&&
                    oldItem.getLocalHost().equals(newItem.getLocalHost());

        }
    };
    @NonNull
    @Override
    public DeviceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.devices_list, parent, false);
        return new DeviceHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull DeviceHolder holder, int position) {
        Device currentNote = getItem(position);
        holder.textViewDeviceID.setText(currentNote.getDeviceId());
        holder.textViewDeviceName.setText(currentNote.getDeviceName());

    }
    public Device getDeviceAt(int position) {
        return getItem(position);
    }
    class DeviceHolder extends RecyclerView.ViewHolder {
        private TextView textViewDeviceID;
        private TextView textViewDeviceName;
        private ImageView Edit;
        private ImageView Delete;
        public DeviceHolder(View itemView) {
            super(itemView);
            textViewDeviceName = itemView.findViewById(R.id.text_view_device_desc);
            textViewDeviceID = itemView.findViewById(R.id.text_view_device_id);
            Edit = itemView.findViewById(R.id.edit);
            Delete = itemView.findViewById(R.id.delete);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
            Edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onEditClick(getItem(position));
                    }
                }
            });
            Delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onDeleteClick(getItem(position));
                    }
                }
            });
        }
    }
    public interface OnItemClickListener {
        void onItemClick(Device device);
        void onEditClick(Device device);
        void onDeleteClick(Device device);

    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
