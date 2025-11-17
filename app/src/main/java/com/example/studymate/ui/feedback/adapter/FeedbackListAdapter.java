package com.example.studymate.ui.feedback.adapter;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studymate.R;
import com.example.studymate.data.model.Feedback;


public class FeedbackListAdapter extends ListAdapter<Feedback, FeedbackListAdapter.FeedbackViewHolder> {

    public interface OnFeedbackClickListener {
        void onConversationClick(Feedback feedback);
    }
    private OnFeedbackClickListener clickListener;

    public void setOnFeedbackClickListener(OnFeedbackClickListener listener) {
        this.clickListener = listener;
    }

    public FeedbackListAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Feedback> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Feedback>() {
                @Override
                public boolean areItemsTheSame(@NonNull Feedback oldItem, @NonNull Feedback newItem) {
                    return oldItem.getConversationId().equals(newItem.getConversationId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Feedback oldItem, @NonNull Feedback newItem) {
                    return oldItem.isRead() == newItem.isRead() &&
                            oldItem.getSenderName().equals(newItem.getSenderName());
                }
            };

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student_feedback, parent, false);
        return new FeedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class FeedbackViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvStudentName, tvStudentID;
        private final ImageView iconMessage;

        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);

            tvStudentName = itemView.findViewById(R.id.textViewStudentName);
            tvStudentID = itemView.findViewById(R.id.textViewStudentID);
            iconMessage = itemView.findViewById(R.id.iconMessage);

            View.OnClickListener itemClickListener = v -> {
                int position = getAdapterPosition();
                if (clickListener != null && position != RecyclerView.NO_POSITION) {
                    clickListener.onConversationClick(getItem(position));
                }
            };

            itemView.setOnClickListener(itemClickListener);
            iconMessage.setOnClickListener(itemClickListener);
        }

        public void bind(Feedback feedback) {
            tvStudentName.setText(feedback.getSenderName());
            tvStudentID.setText(feedback.getSenderUsername());
            if (!feedback.isRead()) {
                tvStudentName.setTypeface(null, Typeface.BOLD);
            } else {
                tvStudentName.setTypeface(null, Typeface.NORMAL);
            }
        }
    }
}