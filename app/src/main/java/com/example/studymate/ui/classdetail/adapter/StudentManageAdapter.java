package com.example.studymate.ui.classdetail.adapter;

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
import com.example.studymate.data.model.Grade;
import com.example.studymate.data.model.response.StudentResponse;

import java.util.List;

public class StudentManageAdapter extends ListAdapter<StudentResponse, StudentManageAdapter.StudentViewHolder> {

    public interface OnStudentKickListener {
        void onKickClick(StudentResponse student);
    }

    private OnStudentKickListener kickListener;

    public void setKickListener(OnStudentKickListener listener) {
        this.kickListener = listener;
    }

    public StudentManageAdapter() {
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
                .inflate(R.layout.item_student_manage, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        StudentResponse currentUser = getItem(position);
        holder.bind(currentUser);
    }

    class StudentViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvStudentName;

        private final TextView tvStudentID;

        private final TextView tvStudentScoreTX;
        private final TextView tvStudentScoreGK;
        private final TextView tvStudentScoreCK;
        private final ImageView iconTrash;


        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStudentName = itemView.findViewById(R.id.tvStudentName);
            tvStudentScoreTX = itemView.findViewById(R.id.tvStudentScoreTX);
            tvStudentScoreGK = itemView.findViewById(R.id.tvStudentScoreGK);
            tvStudentScoreCK = itemView.findViewById(R.id.tvStudentScoreCK);
            tvStudentID = itemView.findViewById(R.id.tvStudentID);
            iconTrash =itemView.findViewById(R.id.iconTrash);

            iconTrash.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (kickListener != null && position != RecyclerView.NO_POSITION) {
                    kickListener.onKickClick(getItem(position));
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
