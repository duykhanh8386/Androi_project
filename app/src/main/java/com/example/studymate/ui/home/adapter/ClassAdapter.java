package com.example.studymate.ui.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studymate.R;
import com.example.studymate.ui.home.model.ClassItem;

import java.util.ArrayList;
import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassVH> {

    /** Trả về cả item để tiện lấy id/tên. */
    public interface OnItemClick {
        void onClick(ClassItem item, int position);
    }

    private final List<ClassItem> items = new ArrayList<>();
    private OnItemClick onItemClick;

    public ClassAdapter() { }

    public void setOnItemClick(OnItemClick l) {
        this.onItemClick = l;
    }

    /** Nhận List<ClassItem> để khớp với ViewModel. */
    public void submitList(List<ClassItem> data) {
        items.clear();
        if (data != null) items.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull @Override
    public ClassVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_class_card, parent, false);
        return new ClassVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassVH holder, int position) {
        ClassItem item = items.get(position);
        holder.tvName.setText(item.getName());               // <-- dùng name từ ClassItem
        holder.itemView.setOnClickListener(v -> {
            if (onItemClick != null) onItemClick.onClick(item, holder.getBindingAdapterPosition());
        });
    }

    @Override public int getItemCount() { return items.size(); }

    static class ClassVH extends RecyclerView.ViewHolder {
        TextView tvName;
        ClassVH(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvClassName);
        }
    }
}
