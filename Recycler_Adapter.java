package com.example.opensourcesoftwareproject_team;

// https://recipes4dev.tistory.com/154 참조 사이트

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Recycler_Adapter extends RecyclerView.Adapter<Recycler_Adapter.ViewHolder> {
    private ArrayList<Post> mData = null;
    DatabaseManagement dm;
    Context context;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView title;
        TextView time;
        TextView tag;
        ImageView image;

        ViewHolder(View itemView) {
            super(itemView);

            // 뷰 객체에 대한 참조. (hold strong reference)
            name = itemView.findViewById(R.id.name_Label_R);
            title = itemView.findViewById(R.id.title_Label_R);
            time = itemView.findViewById(R.id.time_Label_R);
            tag = itemView.findViewById(R.id.tag_Label_R);
            image = itemView.findViewById(R.id.imageView_R);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        String pid = name.getText().toString();
                        String ptitle = title.getText().toString();
                        String ptime = time.getText().toString();
                        String ptag = tag.getText().toString();

                        ArrayList<String> columns = dm.getPostInformation(pid, ptitle);
                        mData.set(pos, new Post(pid, ptitle, ptime, columns.get(0), columns.get(1), ptag, columns.get(2),
                                columns.get(3), columns.get(4), columns.get(5), columns.get(6), columns.get(7), columns.get(8), columns.get(9)));
                        notifyItemChanged(pos);

                        Intent selectionPage = new Intent(v.getContext(), SelectionPostPage.class);
                        selectionPage.putExtra("id", pid);
                        selectionPage.putExtra("title", ptitle);
                        selectionPage.putExtra("time", ptime);
                        selectionPage.putExtra("price", columns.get(0));
                        selectionPage.putExtra("content", columns.get(1));
                        selectionPage.putExtra("tag", ptag);

                        for (int i = 2; i < columns.size(); i++) {
                            selectionPage.putExtra("image" + (i - 1), columns.get(i));
                        }

                        context.startActivity(selectionPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                }
            });
        }
    }

    Recycler_Adapter(ArrayList<Post> data, DatabaseManagement db, Context con) {
        mData = data;
        dm = db;
        context = con;
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
        holder.time.setText(text.write_Date);
        holder.tag.setText(text.tag);
        holder.image.setImageURI(Uri.parse(text.image1));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
