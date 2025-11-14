package com.example.studymate.ui.notify;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.studymate.R;
import com.example.studymate.data.model.request.NotificationRequest;
import com.example.studymate.data.model.response.MessageResponse;
import com.example.studymate.data.repository.NotificationRepository;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class NotificationCreateFragment extends Fragment {

    private TextInputEditText edtTitle, edtContent;
    private Button btnCancel, btnSend;
    private TextInputLayout spinnerLayoutClass;
    private AutoCompleteTextView autoCompleteClass;

    private NotificationRepository notificationRepository;
    private int classId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificationRepository = new NotificationRepository();

        if (getArguments() != null) {
            classId = getArguments().getInt("classId");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification_create, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ View
        edtTitle = view.findViewById(R.id.editTextNotificationTitle);
        edtContent = view.findViewById(R.id.editTextNotificationContent);
        btnCancel = view.findViewById(R.id.buttonCancel);
        btnSend = view.findViewById(R.id.buttonSendNotification);
        spinnerLayoutClass = view.findViewById(R.id.spinnerLayoutClass);
        autoCompleteClass = view.findViewById(R.id.autoCompleteTextViewClass);

        // Xử lý Spinner
        if (classId > 0) {
            spinnerLayoutClass.setVisibility(View.GONE);
        } else {
            spinnerLayoutClass.setError("Lỗi: Không có ID lớp");
            btnSend.setEnabled(false);
        }

        // Gán sự kiện
        btnSend.setOnClickListener(this::doSend);
        btnCancel.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());

        // ⭐️ BẮT ĐẦU QUAN SÁT KẾT QUẢ NGAY LẬP TỨC
        setupObservers();
    }

    /**
     * Hàm này chỉ chịu trách nhiệm GỌI HÀNH ĐỘNG
     */
    private void doSend(View v) {
        if (edtTitle == null || edtContent == null) {
            Toast.makeText(getContext(), "Thiếu view trong layout. Kiểm tra id!", Toast.LENGTH_LONG).show();
            return;
        }

        String title = text(edtTitle);
        String content = text(edtContent);

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            Toast.makeText(getContext(), "Vui lòng nhập đầy đủ tiêu đề và nội dung!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (classId <= 0) {
            Toast.makeText(getContext(), "Lỗi: ID Lớp không hợp lệ.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo Request
        NotificationRequest req = new NotificationRequest(title, content);

        // ⭐️ SỬA LỖI: Chỉ gọi hàm, KHÔNG observe ở đây
        // Hàm này là void, nó sẽ kích hoạt LiveData trong Repository
        notificationRepository.createNotification(classId, req);
    }

    /**
     * ⭐️ HÀM MỚI: Thiết lập các observers (giống mẫu FeedbackThreadFragment)
     * Các hàm này lắng nghe sự thay đổi từ Repository
     */
    private void setupObservers() {

        // 1. Quan sát trạng thái Loading
        notificationRepository.getIsCreateLoading().observe(getViewLifecycleOwner(), isLoading -> {
            btnSend.setEnabled(!isLoading);
            btnCancel.setEnabled(!isLoading);
            btnSend.setText(isLoading ? "Đang gửi..." : "Gửi thông báo");
        });

        // 2. Quan sát sự kiện THÀNH CÔNG
        notificationRepository.getCreateSuccessEvent().observe(getViewLifecycleOwner(), (MessageResponse resp) -> {
            // LiveData này chỉ được kích hoạt khi có kết quả thành công
            if (resp != null) {
                Toast.makeText(getContext(), resp.getMessage(), Toast.LENGTH_SHORT).show();
                Navigation.findNavController(getView()).navigateUp(); // Quay lại
            }
        });

        // 3. Quan sát sự kiện LỖI
        notificationRepository.getCreateErrorEvent().observe(getViewLifecycleOwner(), (String error) -> {
            // LiveData này chỉ được kích hoạt khi có lỗi
            if (error != null && !error.isEmpty()) {
                Toast.makeText(getContext(), "Gửi thất bại: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * Helper để lấy text (theo mẫu)
     */
    private String text(TextInputEditText e) {
        return e == null ? "" : String.valueOf(e.getText()).trim();
    }
}