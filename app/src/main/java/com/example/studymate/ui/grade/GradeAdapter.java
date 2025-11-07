package com.example.studymate.ui.grade;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studymate.R;
// Bỏ import 'Grade' vì ta dùng dữ liệu giả
// import com.example.studymate.data.local.entity.Grade;
import com.example.studymate.ui.grade.GradeEntryFragment.MockGradeItem; // Import lớp giả

import java.util.List;

public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.GradeViewHolder> {

    // THAY ĐỔI: Dùng List<MockGradeItem>
    private List<MockGradeItem> gradeList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        // THAY ĐỔI: Truyền MockGradeItem
        void onEnterClick(MockGradeItem grade);
        void onEditClick(MockGradeItem grade);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // THAY ĐỔI: Nhận List<MockGradeItem>
    public GradeAdapter(List<MockGradeItem> gradeList) {
        this.gradeList = gradeList;
    }

    // (Hàm updateData không cần thiết nữa nếu chỉ dùng mock data)

    @NonNull
    @Override
    public GradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_class_grade_row, parent, false);
        return new GradeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GradeViewHolder holder, int position) {
        // Lấy dữ liệu giả
        MockGradeItem current = gradeList.get(position);

        // "Bơm" dữ liệu vào View (THAY ĐỔI: Dùng trường của MockGradeItem)
        holder.tvStudentName.setText(current.studentName);
        holder.tvStudentId.setText("Mã SV: " + current.studentId);
        holder.tvSubject.setText("Môn học: " + current.subjectName);

        holder.tvQuiz.setText(String.valueOf(current.quiz));
        holder.tvMid.setText(String.valueOf(current.mid));
        holder.tvFinal.setText(String.valueOf(current.fin));

        // Logic hiển thị nút (Ví dụ)
        if (current.fin < 0) { // Giả sử < 0 là chưa nhập
            holder.btnNhapDiem.setVisibility(View.VISIBLE);
            holder.btnCapNhat.setVisibility(View.GONE);
        } else {
            holder.btnNhapDiem.setVisibility(View.GONE);
            holder.btnCapNhat.setVisibility(View.VISIBLE);
        }

        // Gán sự kiện click
        holder.btnNhapDiem.setOnClickListener(v -> {
            if (listener != null) listener.onEnterClick(current);
        });
        holder.btnCapNhat.setOnClickListener(v -> {
            if (listener != null) listener.onEditClick(current);
        });
    }

    @Override
    public int getItemCount() {
        return gradeList.size();
    }

    // Lớp ViewHolder (THAY ĐỔI: Sửa ID cho khớp XML)
    public static class GradeViewHolder extends RecyclerView.ViewHolder {
        TextView tvStudentName, tvStudentId, tvSubject;
        TextView tvQuiz, tvMid, tvFinal; // Sửa tên
        Button btnNhapDiem, btnCapNhat;

        public GradeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStudentName = itemView.findViewById(R.id.textViewStudentName);
            tvStudentId = itemView.findViewById(R.id.textViewStudentID);
            tvSubject = itemView.findViewById(R.id.textViewSubject);

            // Sửa ID để khớp XML (quan trọng)
            tvQuiz = itemView.findViewById(R.id.textViewQuiz);
            tvMid = itemView.findViewById(R.id.textViewMid);
            tvFinal = itemView.findViewById(R.id.textViewFinal);

            btnNhapDiem = itemView.findViewById(R.id.btnNhapDiem);
            btnCapNhat = itemView.findViewById(R.id.btnCapNhat);
        }
    }
}