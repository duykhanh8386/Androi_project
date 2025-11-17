package com.example.studymate.ui.grade.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studymate.R;
import com.example.studymate.data.model.Grade;

public class GradeDetailAdapter extends ListAdapter<Grade, GradeDetailAdapter.GradeDetailViewHolder> {

    public interface OnGradeClickListener {
        void onEditClick(Grade grade);
        void onDeleteClick(Grade grade);
    }
    private OnGradeClickListener clickListener;

    public void setOnGradeClickListener(OnGradeClickListener listener) {
        this.clickListener = listener;
    }

    public GradeDetailAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Grade> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Grade>() {
                @Override
                public boolean areItemsTheSame(@NonNull Grade oldItem, @NonNull Grade newItem) {
                    return oldItem.getGradeId() == newItem.getGradeId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Grade oldItem, @NonNull Grade newItem) {
                    return (oldItem.getScore().equals(newItem.getScore())) &&
                            oldItem.getGradeType().equals(newItem.getGradeType());
                }
            };

    @NonNull
    @Override
    public GradeDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grade_editable, parent, false);
        return new GradeDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GradeDetailViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class GradeDetailViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvGradeType, tvGradeScore;
        private final ImageButton btnEditGrade, btnDeleteGrade;

        public GradeDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGradeType = itemView.findViewById(R.id.tvGradeType);
            tvGradeScore = itemView.findViewById(R.id.tvGradeScore);
            btnEditGrade = itemView.findViewById(R.id.btnEditGrade);
            btnDeleteGrade = itemView.findViewById(R.id.btnDeleteGrade);

            btnEditGrade.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (clickListener != null && pos != RecyclerView.NO_POSITION) {
                    clickListener.onEditClick(getItem(pos));
                }
            });

            btnDeleteGrade.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (clickListener != null && pos != RecyclerView.NO_POSITION) {
                    clickListener.onDeleteClick(getItem(pos));
                }
            });
        }

        public void bind(Grade grade) {
            tvGradeType.setText(grade.getGradeType() + ":");
            tvGradeScore.setText(String.valueOf(grade.getScore()));
        }
    }
}
