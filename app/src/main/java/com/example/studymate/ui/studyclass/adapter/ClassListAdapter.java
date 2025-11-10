package com.example.studymate.ui.studyclass.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.studymate.R;
import com.example.studymate.data.model.StudyClass;

public class ClassListAdapter extends ListAdapter<StudyClass, ClassListAdapter.ClassViewHolder> {

    public ClassListAdapter() {
        super(DIFF_CALLBACK);
    }

    // Định nghĩa cách RecyclerView so sánh các item (tối ưu hiệu suất)
    private static final DiffUtil.ItemCallback<StudyClass> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<StudyClass>() {
                @Override
                public boolean areItemsTheSame(@NonNull StudyClass oldItem, @NonNull StudyClass newItem) {
                    return oldItem.getClassId() == newItem.getClassId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull StudyClass oldItem, @NonNull StudyClass newItem) {
                    return oldItem.getClassName().equals(newItem.getClassName()) &&
                            oldItem.getClassTime().equals(newItem.getClassTime());
                }
            };

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // "Thổi phồng" layout item_class_card.xml
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_class_card, parent, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        StudyClass currentClass = getItem(position);
        holder.bind(currentClass);
    }

    // Lớp "ViewHolder" giữ tham chiếu đến các View trong item_class_card.xml
    class ClassViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvClassName;
        private final TextView tvClassTime;

        public ClassViewHolder(@NonNull View itemView) {
            super(itemView);
            tvClassName = itemView.findViewById(R.id.tvClassName);
            tvClassTime = itemView.findViewById(R.id.tvClassTime);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Lấy vị trí của item được bấm
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {

                        // Lấy đối tượng StudyClass tại vị trí đó
                        StudyClass clickedClass = getItem(position);

                        // ⭐️ BƯỚC 3: ĐIỀU HƯỚNG TỚI MÀN HÌNH CHI TIẾT
                        // (Giả sử bạn đã tạo một action trong nav_graph
                        //  để đi từ HomeStudentFragment -> ClassDetailFragment)

                        // Nếu chưa có action, bạn có thể gọi ID của hành động:
                         Navigation.findNavController(itemView).navigate(R.id.action_student_home_to_classDetail);
                    }
                }
            });
        }

        // Gán dữ liệu từ StudyClass vào các TextView
        public void bind(StudyClass studyClass) {
            tvClassName.setText(studyClass.getClassName());
            tvClassTime.setText(studyClass.getClassTime()); // Giả sử classTime chứa tên GV
        }
    }
}