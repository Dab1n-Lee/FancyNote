package com.example.fancynote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private Context context;
    private ArrayList<MemoItem> memoArray;

    public CustomAdapter(Context context, ArrayList<MemoItem> memoArray) {
        this.context = context;
        this.memoArray = memoArray;
    }

    @NonNull
    @Override

    public CustomAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.memo_layout, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.CustomViewHolder holder, int position) {
        MemoItem memoItem = memoArray.get(position);

        holder.tv_title.setText(memoItem.getTitle());
        holder.tv_content.setText(memoItem.getContent());
        holder.foldingCell.setOnClickListener((v)->{
            holder.foldingCell.toggle(false);
        });
    }

    @Override
    public int getItemCount() {
        return (memoArray != null ? memoArray.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private FoldingCell foldingCell;

        private TextView tv_content;
        private TextView tv_title;
        private ImageView imageView;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            foldingCell = itemView.findViewById(R.id.folding_cell);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_title = itemView.findViewById(R.id.tv_title);
            imageView = itemView.findViewById(R.id.imageView);

        }
    }
}
