package com.example.studymate.ui.feedback;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.studymate.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class FeedbackDetailFragment extends Fragment {

    private TextView tvUserInfo, tvContent;
    private TextInputEditText edtReply;
    private MaterialButton btnSend;

    private String userInfo;
    private String content;

    public FeedbackDetailFragment() {
        // Bắt buộc cần constructor rỗng
    }

    /**
     * Hàm khởi tạo fragment với dữ liệu truyền vào.
     * Gọi: FeedbackDetailFragment.newInstance("Nguyễn Văn A", "Em xin nghỉ học ạ");
     */
    public static FeedbackDetailFragment newInstance(String userInfo, String content) {
        FeedbackDetailFragment fragment = new FeedbackDetailFragment();
        Bundle args = new Bundle();
        args.putString("userInfo", userInfo);
        args.putString("content", content);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Nạp giao diện từ XML
        return inflater.inflate(R.layout.fragment_feedback_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ view
        tvUserInfo = view.findViewById(R.id.tvUserInfo);
        tvContent = view.findViewById(R.id.tvContent);
        edtReply = view.findViewById(R.id.edtReply);
        btnSend = view.findViewById(R.id.btnSend);

        // Lấy dữ liệu từ Bundle
        if (getArguments() != null) {
            userInfo = getArguments().getString("userInfo");
            content = getArguments().getString("content");
        }

        // Gán dữ liệu lên giao diện
        if (userInfo != null) tvUserInfo.setText(userInfo);
        if (content != null) tvContent.setText(content);

        // Sự kiện nút Gửi
        btnSend.setOnClickListener(v -> {
            String replyText = edtReply.getText() != null ? edtReply.getText().toString().trim() : "";

            if (TextUtils.isEmpty(replyText)) {
                Toast.makeText(requireContext(), "Vui lòng nhập nội dung phản hồi", Toast.LENGTH_SHORT).show();
                return;
            }

            // TODO: Gửi phản hồi lên server hoặc xử lý logic tại đây
            Toast.makeText(requireContext(), "Đã gửi phản hồi: " + replyText, Toast.LENGTH_SHORT).show();

            // Xóa nội dung sau khi gửi
            edtReply.setText("");
        });
    }
}
