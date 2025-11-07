package com.example.studymate.ui.feedback;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.studymate.R;
import com.example.studymate.ui.viewmodel.FeedbackViewModel;
import com.example.studymate.utils.SessionManager;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FeedbackThreadFragment extends Fragment {

    private LinearLayout llFeedbackContainer;

    // --- Lớp mô phỏng phản hồi ---
    static class FeedbackItem {
        String author;
        Date time;

        FeedbackItem(String author, Date time) {
            this.author = author;
            this.time = time;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Gắn layout chính của fragment
        View v = inflater.inflate(R.layout.fragment_feedback_thread, container, false);
        llFeedbackContainer = v.findViewById(R.id.llFeedbackContainer);

        // Tạo danh sách phản hồi mẫu
        List<FeedbackItem> feedbackList = mockFeedbackData();

        // Hiển thị danh sách phản hồi
        showFeedbackList(feedbackList, inflater);

        return v;
    }

    private List<FeedbackItem> mockFeedbackData() {
        List<FeedbackItem> list = new ArrayList<>();
        list.add(new FeedbackItem("Nguyễn Văn A", new Date(System.currentTimeMillis())));
        list.add(new FeedbackItem("Trần Thị B", new Date(System.currentTimeMillis() - 86400000L)));
        list.add(new FeedbackItem("Lê Văn C", new Date(System.currentTimeMillis() - 2 * 86400000L)));
        return list;
    }

    private void showFeedbackList(List<FeedbackItem> feedbackList, LayoutInflater inflater) {
        llFeedbackContainer.removeAllViews(); // Xóa dữ liệu cũ nếu có

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm - dd/MM/yyyy", Locale.getDefault());

        for (FeedbackItem item : feedbackList) {
            // Nạp layout của từng thẻ phản hồi
            CardView card = (CardView) inflater.inflate(R.layout.item_feedback_card, llFeedbackContainer, false);

            TextView tvAuthor = card.findViewById(R.id.tvAuthor);
            TextView tvTime = card.findViewById(R.id.tvTime);

            tvAuthor.setText(item.author);
            tvTime.setText(sdf.format(item.time));

            llFeedbackContainer.addView(card);
        }
    }
}
