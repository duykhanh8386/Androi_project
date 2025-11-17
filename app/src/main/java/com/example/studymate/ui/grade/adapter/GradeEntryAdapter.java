package com.example.studymate.ui.grade.adapter;

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

public class GradeEntryAdapter extends ListAdapter<StudentResponse, GradeEntryAdapter.GradeViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(StudentResponse student);
    }
    private OnItemClickListener clickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    public GradeEntryAdapter() {
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
                            oldItem.getGrades().size() == newItem.getGrades().size(); // (Kiểm tra cả điểm)
                }
            };

    @NonNull
    @Override
    public GradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grade_entry, parent, false);
        return new GradeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GradeViewHolder holder, int position) {
        StudentResponse currentUser = getItem(position);
        holder.bind(currentUser);
    }

    class GradeViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvStudentName, tvStudentID;
        private final TextView tvStudentScoreTX, tvStudentScoreGK, tvStudentScoreCK;

        public GradeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStudentName = itemView.findViewById(R.id.tvStudentName);
            tvStudentID = itemView.findViewById(R.id.tvStudentID);
            tvStudentScoreTX = itemView.findViewById(R.id.tvStudentScoreTX);
            tvStudentScoreGK = itemView.findViewById(R.id.tvStudentScoreGK);
            tvStudentScoreCK = itemView.findViewById(R.id.tvStudentScoreCK);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (clickListener != null && position != RecyclerView.NO_POSITION) {
                    clickListener.onItemClick(getItem(position));
                }
            });
        }

        public void bind(StudentResponse studentResponse) {
            tvStudentName.setText(studentResponse.getUser().getFullName());
            tvStudentID.setText(studentResponse.getUser().getUserName());
            List<Grade> grades = studentResponse.getGrades();

            String scoreTX = "";
            String scoreGK = "";
            String scoreCK = "";

            if (grades != null && !grades.isEmpty()) {
                for (Grade grade : grades) {
                    String scoreStr = String.valueOf(grade.getScore()) + " ";
                    if (grade.getGradeType().equals("TX")) {
                        scoreTX += scoreStr;
                    } else if (grade.getGradeType().equals("GK")) {
                        scoreGK += scoreStr;
                    } else if (grade.getGradeType().equals("CK")) {
                        scoreCK += scoreStr;
                    }
                }
            }

            tvStudentScoreTX.setText(scoreTX.isEmpty() ? "-" : scoreTX.trim());
            tvStudentScoreGK.setText(scoreGK.isEmpty() ? "-" : scoreGK.trim());
            tvStudentScoreCK.setText(scoreCK.isEmpty() ? "-" : scoreCK.trim());
        }
    }
}