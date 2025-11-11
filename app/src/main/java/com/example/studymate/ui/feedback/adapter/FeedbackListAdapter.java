package com.example.studymate.ui.feedback.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studymate.R;
import com.example.studymate.data.model.Feedback;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class FeedbackListAdapter extends ListAdapter<Feedback, FeedbackListAdapter.BaseViewHolder> {

    // ⭐️ Giả sử ID của học sinh đang đăng nhập (sẽ lấy từ SessionManager)
    // Dùng 123 để khớp với logic mock
    private final int currentUserId = 123;

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    public FeedbackListAdapter() {
        super(DIFF_CALLBACK);
    }

    // ⭐️ Xác định layout nào sẽ dùng (gửi hay nhận)
    @Override
    public int getItemViewType(int position) {
        Feedback feedback = getItem(position);
        if (feedback.getSender().getUserId() == currentUserId) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_feedback_sent, parent, false);
            return new SentViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_feedback_received, parent, false);
            return new ReceivedViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        Feedback current = getItem(position);
        holder.bind(current);
    }

    // ⭐️ Lớp ViewHolder CƠ SỞ
    abstract class BaseViewHolder extends RecyclerView.ViewHolder {
        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        abstract void bind(Feedback feedback);
    }

    // ⭐️ ViewHolder cho tin nhắn GỬI (của bạn)
    class SentViewHolder extends BaseViewHolder {
        private final TextView tvSenderInfo;
        private final TextView tvFeedbackContent;

        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSenderInfo = itemView.findViewById(R.id.tvSenderInfo);
            tvFeedbackContent = itemView.findViewById(R.id.tvFeedbackContent);
        }

        void bind(Feedback feedback) {
            tvSenderInfo.setText("Bạn (" + formatDate(feedback.getCreatedAt()) + ")");
            tvFeedbackContent.setText(feedback.getFeedbackContent());
        }
    }

    // ⭐️ ViewHolder cho tin nhắn NHẬN (của GV)
    class ReceivedViewHolder extends BaseViewHolder {
        private final TextView tvSenderInfo;
        private final TextView tvFeedbackContent;

        public ReceivedViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSenderInfo = itemView.findViewById(R.id.tvSenderInfo);
            tvFeedbackContent = itemView.findViewById(R.id.tvFeedbackContent);
        }

        void bind(Feedback feedback) {
            tvSenderInfo.setText("Giáo viên (" + formatDate(feedback.getCreatedAt()) + ")");
            tvFeedbackContent.setText(feedback.getFeedbackContent());
        }
    }

    // --- (DiffUtil và hàm Helper) ---

    private static final DiffUtil.ItemCallback<Feedback> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Feedback>() {
                @Override
                public boolean areItemsTheSame(@NonNull Feedback oldItem, @NonNull Feedback newItem) {
                    return oldItem.getFeedbackId() == newItem.getFeedbackId();
                }
                @Override
                public boolean areContentsTheSame(@NonNull Feedback oldItem, @NonNull Feedback newItem) {
                    return oldItem.getFeedbackContent().equals(newItem.getFeedbackContent());
                }
            };

    private String formatDate(java.util.Date date) {
        if (date == null) return "mới đây";
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM", Locale.getDefault());
        return sdf.format(date);
    }
}
