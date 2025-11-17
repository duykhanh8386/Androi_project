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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class FeedbackThreadAdapter extends ListAdapter<Feedback, FeedbackThreadAdapter.BaseViewHolder> {

    private final Long currentUserId;

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    public FeedbackThreadAdapter(Long currentUserId) {
        super(DIFF_CALLBACK);
        this.currentUserId = currentUserId;
    }

    // ⭐️ 3. Xác định layout nào sẽ dùng (gửi hay nhận)
    @Override
    public int getItemViewType(int position) {
        Feedback feedback = getItem(position);
        if (feedback.getSenderId() != null && feedback.getSenderId().equals(currentUserId)) {
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

    abstract class BaseViewHolder extends RecyclerView.ViewHolder {
        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        abstract void bind(Feedback feedback);

        String formatTimestamp(String timestamp) {
            if (timestamp == null || timestamp.isEmpty()) return "vừa xong";
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm dd/MM", Locale.getDefault());
                Date date = inputFormat.parse(timestamp);
                return outputFormat.format(date);
            } catch (ParseException e) {
                return timestamp;
            }
        }
    }

    class SentViewHolder extends BaseViewHolder {
        private final TextView tvSenderInfo;
        private final TextView tvFeedbackContent;

        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSenderInfo = itemView.findViewById(R.id.tvSenderInfo);
            tvFeedbackContent = itemView.findViewById(R.id.tvFeedbackContent);
        }

        void bind(Feedback feedback) {
            tvSenderInfo.setText("Bạn (" + formatTimestamp(feedback.getCreatedAt()) + ")");
            tvFeedbackContent.setText(feedback.getFeedbackContent());
        }
    }

    class ReceivedViewHolder extends BaseViewHolder {
        private final TextView tvSenderInfo;
        private final TextView tvFeedbackContent;

        public ReceivedViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSenderInfo = itemView.findViewById(R.id.tvSenderInfo);
            tvFeedbackContent = itemView.findViewById(R.id.tvFeedbackContent);
        }

        void bind(Feedback feedback) {
            String senderName = (feedback.getSenderName() != null) ? feedback.getSenderName() : "Giáo viên";
            tvSenderInfo.setText(senderName + " (" + formatTimestamp(feedback.getCreatedAt()) + ")");
            tvFeedbackContent.setText(feedback.getFeedbackContent());
        }
    }

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
}