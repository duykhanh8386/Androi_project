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
import com.example.studymate.data.model.StudentClass;

public class TeacherApprovalAdapter extends ListAdapter<StudentClass, TeacherApprovalAdapter.ApprovalViewHolder> {

    public interface OnApprovalClickListener {
        void onApproveClick(StudentClass user);
        void onRejectClick(StudentClass user);
    }

    private OnApprovalClickListener listener;

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
        StudentClass currentUser = getItem(position);
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

        public void bind(StudentClass user) {
            tvStudentId.setText(user.getUsername());
            tvStudentName.setText(user.getFullName());
        }
    }

    private static final DiffUtil.ItemCallback<StudentClass> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<StudentClass>() {
                @Override
                public boolean areItemsTheSame(@NonNull StudentClass oldItem, @NonNull StudentClass newItem) {
                    return oldItem.getStudentClassId() == newItem.getStudentClassId();
                }
                @Override
                public boolean areContentsTheSame(@NonNull StudentClass oldItem, @NonNull StudentClass newItem) {
                    return oldItem.getFullName().equals(newItem.getFullName());
                }
            };
}