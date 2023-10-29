package edu.northeastern.s3kb;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.StickerViewHolder> {
    private List<String> stickerIdentifiers;

    private Context context;
    public StickerAdapter(Context context, List<String> stickerIdentifiers) {
        this.context = context;
        this.stickerIdentifiers = stickerIdentifiers;
    }

    @NonNull
    @Override
    public StickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sticker, parent, false);
        return new StickerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StickerViewHolder holder, int position) {
        String stickerIdentifier = stickerIdentifiers.get(position);
        holder.bindSticker(stickerIdentifier);
    }

    @Override
    public int getItemCount() {
        return stickerIdentifiers.size();
    }

    public class StickerViewHolder extends RecyclerView.ViewHolder {
        private ImageView stickerImageView;

        public StickerViewHolder(@NonNull View itemView) {
            super(itemView);
            stickerImageView = itemView.findViewById(R.id.stickerImageView);
            itemView.setOnClickListener(view -> {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String selectedSticker = stickerIdentifiers.get(position);
                        sendSelectedSticker(selectedSticker);
                    }
            });
        }

        public void bindSticker(String stickerIdentifier) {
            // Load and set the sticker image based on the identifier
            int drawableId = itemView.getResources().getIdentifier(stickerIdentifier, "drawable", itemView.getContext().getPackageName());
            if (drawableId != 0) {
                stickerImageView.setImageResource(drawableId);
            }
        }
    }

    private void sendSelectedSticker(String stickerIdentifier) {
        String displayToast = "This was clicked: " + stickerIdentifier;
        Toast.makeText(context, displayToast, Toast.LENGTH_SHORT).show();
    }
}
