package com.example.opensourcesoftwareproject_team;

// https://recipes4dev.tistory.com/154 참조 사이트

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Recycler_Adapter extends RecyclerView.Adapter<Recycler_Adapter.ViewHolder> {
    private ArrayList<Post> mData = null;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView title;
        TextView time;
        TextView tag;

        ViewHolder(View itemView) {
            super(itemView);

            // 뷰 객체에 대한 참조. (hold strong reference)
            name = itemView.findViewById(R.id.name_Label_R);
            title = itemView.findViewById(R.id.title_Label_R);
            time = itemView.findViewById(R.id.time_Label_R);
            tag = itemView.findViewById(R.id.tag_Label_R);
        }
    }

    Recycler_Adapter(ArrayList<Post> data) {
        mData = data;
    }

    @NonNull
    @Override
    public Recycler_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recycler_view_item, parent, false);
        Recycler_Adapter.ViewHolder vh = new Recycler_Adapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull Recycler_Adapter.ViewHolder holder, int position) {
        Post text = mData.get(position);

        holder.name.setText(text.userName);
        holder.title.setText(text.title);
        holder.time.setText(text.write_Date.toString());
        holder.tag.setText(text.tag);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
