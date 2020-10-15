package com.example.firebase2;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ideaorganizer.R;
import com.example.ideaorganizer.Upload;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TextAdapter extends RecyclerView.Adapter<TextAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Upload> mUploads;
    private OnItemClickListener mListener;
    public TextAdapter(Context context, List<Upload> uploads) {
        mContext = context;
        mUploads = uploads;
    }
    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.text_item, parent, false);
        return new ImageViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Upload uploadCurrent = mUploads.get(position);
        holder.textViewName.setText(uploadCurrent.getName());
    }
    @Override
    public int getItemCount() {
        return mUploads.size();
    }
    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        public TextView textViewName;
        public ImageViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem delete = menu.add(Menu.NONE, 1, 1, "Delete");
            delete.setOnMenuItemClickListener(this);
        }
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    switch (item.getItemId()) {
                        case 1:
                            mListener.onDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}