package baksha97.com.euleritychallenge.ui.list;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import baksha97.com.euleritychallenge.R;
import baksha97.com.euleritychallenge.data.model.ImageItem;

/**
 * Adapter class used to generate the RecyclerView with items.
 */
public class ImageItemAdapter extends RecyclerView.Adapter<ImageItemAdapter.ViewHolder> {

    private List<ImageItem> list;
    private Context context;
    private onItemClickListener mListener;

    public ImageItemAdapter(List<ImageItem> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.image_item, viewGroup, false);

        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n") //Due to demo purposes
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ImageItem item = list.get(i);
        String imageUrl = item.getUrl();

        Glide.with(context).load(imageUrl)
                .apply(new RequestOptions()
                        .centerInside())
                .into(viewHolder.mImageView);
        viewHolder.mTextViewCreated.setText("Created @ " + item.getCreated());
        viewHolder.mTextViewUpdated.setText("Updated @ " + item.getUpdated());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface onItemClickListener {
        void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public TextView mTextViewCreated;
        public TextView mTextViewUpdated;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.image_view);
            mTextViewCreated = (TextView) itemView.findViewById(R.id.text_created);
            mTextViewUpdated = (TextView) itemView.findViewById(R.id.text_updated);

            itemView.setOnClickListener(v -> {
                if (mListener != null) {
                    int pos = getAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(pos);
                    }
                }
            });
        }
    }
}
