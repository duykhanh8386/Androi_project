package com.example.studymate.ui.feedback;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studymate.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter cho luồng hỏi đáp/feedback.
 * Hai view type:
 *  - Tin nhắn của tôi (mine)  -> item_message_mine.xml
 *  - Tin nhắn của người khác (other) -> item_message_other.xml
 */
public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static class MessageItem {
        public final String content;
        public final boolean isMine;
        public final long timeMillis;

        public MessageItem(String content, boolean isMine, long timeMillis) {
            this.content = content;
            this.isMine = isMine;
            this.timeMillis = timeMillis;
        }
    }

    private static final int TYPE_MINE = 1;
    private static final int TYPE_OTHER = 2;

    private final List<MessageItem> items = new ArrayList<>();
    private final SimpleDateFormat fmt =
        new SimpleDateFormat("HH:mm dd/MM", Locale.getDefault());

    /** Thay toàn bộ danh sách */
    public void submitList(List<MessageItem> data) {
        items.clear();
        if (data != null) items.addAll(data);
        notifyDataSetChanged();
    }

    /** Thêm 1 message vào cuối list */
    public void add(MessageItem item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    @Override public int getItemViewType(int position) {
        return items.get(position).isMine ? TYPE_MINE : TYPE_OTHER;
    }

    @NonNull @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inf = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_MINE) {
            View v = inf.inflate(R.layout.item_message_mine, parent, false);
            return new MineVH(v);
        } else {
            View v = inf.inflate(R.layout.item_message_other, parent, false);
            return new OtherVH(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageItem m = items.get(position);
        if (holder instanceof MineVH) {
            ((MineVH) holder).bind(m, fmt);
        } else if (holder instanceof OtherVH) {
            ((OtherVH) holder).bind(m, fmt);
        }
    }

    @Override public int getItemCount() { return items.size(); }

    static class MineVH extends RecyclerView.ViewHolder {
        TextView tvMessage, tvTime;
        MineVH(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
        void bind(MessageItem m, SimpleDateFormat fmt) {
            tvMessage.setText(m.content);
            tvTime.setText(fmt.format(new Date(m.timeMillis)));
        }
    }

    static class OtherVH extends RecyclerView.ViewHolder {
        TextView tvMessage, tvTime;
        OtherVH(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
        void bind(MessageItem m, SimpleDateFormat fmt) {
            tvMessage.setText(m.content);
            tvTime.setText(fmt.format(new Date(m.timeMillis)));
        }
    }
}
