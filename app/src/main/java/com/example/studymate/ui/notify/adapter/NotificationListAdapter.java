package com.example.studymate.ui.notify.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studymate.R;
import com.example.studymate.data.model.Notification;

public class NotificationListAdapter extends ListAdapter<Notification, NotificationListAdapter.NotificationViewHolder> {

    public NotificationListAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Notification> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Notification>() {
                @Override
                public boolean areItemsTheSame(@NonNull Notification oldItem, @NonNull Notification newItem) {
                    return oldItem.getNotificationId() == newItem.getNotificationId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Notification oldItem, @NonNull Notification newItem) {
                    return oldItem.getNotificationTitle().equals(newItem.getNotificationTitle());
                }
            };

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification current = getItem(position);
        holder.bind(current);
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvDate;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvNotificationTitle);
            tvDate = itemView.findViewById(R.id.tvNotificationDate);

            // TODO: Xử lý click để đi đến chi tiết thông báo
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Notification clickedNotification = getItem(position);

                    // 1. Tạo Bundle
                    Bundle bundle = new Bundle();
                    bundle.putInt("notificationId", clickedNotification.getNotificationId());

                    // 2. Điều hướng (dùng ID action từ nav_graph)
                    Navigation.findNavController(itemView)
                            .navigate(R.id.action_list_to_notifyDetail, bundle);
                }
            });
        }

        public void bind(Notification notification) {
            tvTitle.setText(notification.getNotificationTitle());
            tvDate.setText("Ngày đăng: " + notification.getCreatedAt()); // Giả sử createdAt là String
        }
    }
}
