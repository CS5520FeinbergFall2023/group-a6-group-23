package edu.northeastern.s3kb;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Recycler view adapter class implementation to display stickers history
 */

public class ReviewRecyclerViewAdapter extends RecyclerView.Adapter<ReviewRecyclerViewHolderView> {

    // use list to store a list of stickers
    private final List<Sticker> stickerList;

    public ReviewRecyclerViewAdapter(List<Sticker> stickers) {
        this.stickerList = stickers;
    }

    @NonNull
    @Override
    public ReviewRecyclerViewHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewRecyclerViewHolderView(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_review_stickers, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewRecyclerViewHolderView viewHolder, int position) {
        viewHolder.bindThisData(stickerList.get(position));
    }

    // to get count of stickers
    @Override
    public int getItemCount() {
        if (stickerList == null) {
            return 0;
        } else {
            return stickerList.size();
        }
    }

}
