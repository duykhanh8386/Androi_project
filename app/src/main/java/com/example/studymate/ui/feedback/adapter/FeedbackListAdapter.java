package com.example.studymate.ui.feedback.adapter;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView; // ⭐️ THÊM
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studymate.R;
import com.example.studymate.data.model.Feedback;


public class FeedbackListAdapter extends ListAdapter<Feedback, FeedbackListAdapter.FeedbackViewHolder> {

    // 1. Interface để xử lý click (Không đổi)
    public interface OnFeedbackClickListener {
        void onConversationClick(Feedback feedback);
    }
    private OnFeedbackClickListener clickListener;

    public void setOnFeedbackClickListener(OnFeedbackClickListener listener) {
        this.clickListener = listener;
    }
    // -------------------

    public FeedbackListAdapter() {
        super(DIFF_CALLBACK);
    }

    // 2. DiffUtil (Không đổi, vẫn so sánh Feedback)
    private static final DiffUtil.ItemCallback<Feedback> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Feedback>() {
                @Override
                public boolean areItemsTheSame(@NonNull Feedback oldItem, @NonNull Feedback newItem) {
                    // (Giả sử ConversationId là duy nhất cho mỗi cuộc trò chuyện)
                    return oldItem.getConversationId().equals(newItem.getConversationId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Feedback oldItem, @NonNull Feedback newItem) {
                    // (Kiểm tra tên và trạng thái đọc)
                    return oldItem.isRead() == newItem.isRead() &&
                            oldItem.getSenderName().equals(newItem.getSenderName());
                }
            };

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 3. Dùng layout R.layout.item_student_feedback (layout CardView của bạn)
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student_feedback, parent, false);
        return new FeedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    // ⭐️ 4. SỬA LẠI HOÀN TOÀN VIEWHOLDER (ĐỂ KHỚP VỚI CARDVIEW CỦA BẠN) ⭐️
    class FeedbackViewHolder extends RecyclerView.ViewHolder {

        // 4a. Ánh xạ các ID từ XML của bạn
        private final TextView tvStudentName, tvStudentID;
        private final ImageView iconMessage;

        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);

            // 4b. Dùng đúng ID từ XML của bạn
            tvStudentName = itemView.findViewById(R.id.textViewStudentName);
            tvStudentID = itemView.findViewById(R.id.textViewStudentID);
            iconMessage = itemView.findViewById(R.id.iconMessage);

            // 4c. Xử lý click (trên cả item và icon)
            View.OnClickListener itemClickListener = v -> {
                int position = getAdapterPosition();
                if (clickListener != null && position != RecyclerView.NO_POSITION) {
                    clickListener.onConversationClick(getItem(position));
                }
            };

            itemView.setOnClickListener(itemClickListener);
            iconMessage.setOnClickListener(itemClickListener);
        }

        // 4d. Sửa lại hàm bind
        public void bind(Feedback feedback) {
            // (Lấy Tên từ API)
            tvStudentName.setText(feedback.getSenderName());

            // (Lấy Mã SV từ trường senderUsername MỚI)
            // (Đảm bảo bạn đã thêm 'senderUsername' vào Feedback.java)
            tvStudentID.setText(feedback.getSenderUsername());

            // (Nếu chưa đọc thì in đậm Tên)
            if (!feedback.isRead()) {
                tvStudentName.setTypeface(null, Typeface.BOLD);
            } else {
                tvStudentName.setTypeface(null, Typeface.NORMAL);
            }
        }
    }
}