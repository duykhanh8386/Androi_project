package com.example.studymate.ui.classdetail.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.studymate.R;
import com.example.studymate.data.model.User;

public class TeacherApprovalAdapter extends ListAdapter<User, TeacherApprovalAdapter.ApprovalViewHolder> {

    // 1. Định nghĩa Interface
    public interface OnApprovalClickListener {
        void onApproveClick(User user);
        void onRejectClick(User user);
    }

    private OnApprovalClickListener listener;

    // 2. Hàm public để Fragment set listener
    public void setOnApprovalClickListener(OnApprovalClickListener listener) {
        this.listener = listener;
    }

    public TeacherApprovalAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ApprovalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_approval, parent, false);
        return new ApprovalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApprovalViewHolder holder, int position) {
        User currentUser = getItem(position);
        holder.bind(currentUser);
    }

    class ApprovalViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvStudentId;
        private final TextView tvStudentName;
        private final ImageButton btnApprove;
        private final ImageButton btnReject;

        public ApprovalViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStudentId = itemView.findViewById(R.id.tvStudentId);
            tvStudentName = itemView.findViewById(R.id.tvStudentName);
            btnApprove = itemView.findViewById(R.id.btnApproveItem);
            btnReject = itemView.findViewById(R.id.btnRejectItem);

            // 3. Xử lý click trong
            btnApprove.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onApproveClick(getItem(position));
                }
            });

            btnReject.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onRejectClick(getItem(position));
                }
            });
        }

        public void bind(User user) {
            // (Giả sử getUserId() trả về student_class_id, và getEmail() trả về student_id thật)
            tvStudentId.setText(user.getEmail()); // Hiển thị ID/email thật
            tvStudentName.setText(user.getFullName());
        }
    }

    // --- DiffUtil ---
    private static final DiffUtil.ItemCallback<User> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<User>() {
                @Override
                public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
                    return oldItem.getUserId() == newItem.getUserId(); // So sánh student_class_id
                }
                @Override
                public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
                    return oldItem.getFullName().equals(newItem.getFullName());
                }
            };
}