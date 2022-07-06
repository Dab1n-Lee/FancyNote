package com.example.fancynote;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ItemViewHolder> {

    private Context context;
    private ArrayList<MemoItem> memoItemArrayList;


    public Adapter(Context context) {
        this.context = context;
    }

    public void setDataToAdapter(ArrayList<MemoItem> list) {
        memoItemArrayList = list;
        notifyDataSetChanged();
    }




    @NonNull
    @Override
    public Adapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.memo_layout_include_image, parent, false);
        return new ItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull Adapter.ItemViewHolder holder, int position) {
        MemoItem memoItem = memoItemArrayList.get(position);
        holder.tv_content.setText(memoItem.getContent());
        holder.tv_title.setText(memoItem.getTitle());

        if (memoItem.imagePath.equals("")) {} else {
            Glide.with(context).load(memoItem.imagePath).into(holder.imageView);
        }


        holder.foldingCell.setOnClickListener((v)->{
            holder.foldingCell.toggle(false);
        });
    }

    @Override
    public int getItemCount() {
        return memoItemArrayList != null ? memoItemArrayList.size() : 0;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private FoldingCell foldingCell;

        private TextView tv_content;
        private TextView tv_title;
        private ImageView imageView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            foldingCell = itemView.findViewById(R.id.folding_cell);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_title = itemView.findViewById(R.id.tv_title);
            imageView = itemView.findViewById(R.id.imageView);
        }

    }


}
