package com.example.studymate.ui.classdetail.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studymate.R;
import com.example.studymate.data.model.Grade;
import com.example.studymate.data.model.response.StudentResponse;

import java.util.List;

public class StudentListAdapter extends ListAdapter<StudentResponse, StudentListAdapter.StudentViewHolder> {

    public StudentListAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<StudentResponse> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<StudentResponse>() {
                @Override
                public boolean areItemsTheSame(@NonNull StudentResponse oldItem, @NonNull StudentResponse newItem) {
                    return oldItem.getUser().getUserId() == newItem.getUser().getUserId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull StudentResponse oldItem, @NonNull StudentResponse newItem) {
                    return oldItem.getUser().getFullName().equals(newItem.getUser().getFullName()) &&
                            oldItem.getUser().getUserName().equals(newItem.getUser().getUserName());
                }
            };

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_class_student_row, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        StudentResponse currentUser = getItem(position);
        holder.bind(currentUser);
    }

    class StudentViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvStudentName;
        private final TextView tvStudentScoreTX;
        private final TextView tvStudentScoreGK;
        private final TextView tvStudentScoreCK;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStudentName = itemView.findViewById(R.id.tvStudentName);
            tvStudentScoreTX = itemView.findViewById(R.id.textViewQuiz);
            tvStudentScoreGK = itemView.findViewById(R.id.textViewMid);
            tvStudentScoreCK = itemView.findViewById(R.id.textViewFinal);
        }

        public void bind(StudentResponse studentResponse) {
            tvStudentName.setText(studentResponse.getUser().getFullName());
            List<Grade> grades = studentResponse.getGrades();
            String scoreTX = "";
            String scoreGK = "";
            String scoreCK = "";
            if (!grades.isEmpty()) {
                for (Grade grade : grades) {
                    if (grade.getGradeType().equals("TX")) {
                        scoreTX += String.valueOf(grade.getScore()) + " ";
                    } else if (grade.getGradeType().equals("GK")) {
                        scoreGK += String.valueOf(grade.getScore()) + " ";
                    } else if (grade.getGradeType().equals("CK")) {
                        scoreCK += String.valueOf(grade.getScore()) + " ";
                    }
                }
                tvStudentScoreTX.setText(scoreTX.equals("") ? "-" : scoreTX);
                tvStudentScoreGK.setText(scoreGK.equals("") ? "-" : scoreGK);
                tvStudentScoreCK.setText(scoreCK.equals("") ? "-" : scoreCK);
            }
        }
    }
}
