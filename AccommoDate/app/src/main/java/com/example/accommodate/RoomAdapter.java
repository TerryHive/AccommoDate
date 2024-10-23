package com.example.accommodate;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.accommodate.global.Room;
import java.util.ArrayList;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private ArrayList<Room> roomList;
    private Context context;

    public RoomAdapter(Context context, ArrayList<Room> roomList) {
        this.context = context;
        this.roomList = roomList;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);
        holder.tvName.setText(room.NAME);
        holder.tvGuests.setText("Guests: " + room.GUESTS);
        holder.tvArea.setText("Area: " + room.AREA);
        holder.tvPrice.setText("Price: $" + room.PRICE);
        holder.tvStars.setText("Stars: " + room.STARS);
        holder.tvReviews.setText("Reviews: " + room.REVIEWS);
        // Load the image into the ImageView (assuming you have a method for that)
        // For example, using Glide:
        // Glide.with(holder.itemView.getContext()).load(room.IMAGE).into(holder.ivImage);

        holder.btnSeeMore.setOnClickListener(v -> {
            Log.d("RoomAdapter", "See More button clicked for room: " + room.NAME);
           Intent intent = new Intent(context, BookRoomActivity.class);
            intent.putExtra("room", room);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvGuests, tvArea, tvPrice, tvStars, tvReviews;
        public ImageView ivImage;
        public Button btnSeeMore;

        public RoomViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tv_name);
            tvGuests = view.findViewById(R.id.tv_guests);
            tvArea = view.findViewById(R.id.tv_area);
            tvPrice = view.findViewById(R.id.tv_price);
            tvStars = view.findViewById(R.id.tv_stars);
            tvReviews = view.findViewById(R.id.tv_reviews);
            ivImage = view.findViewById(R.id.iv_image);
            btnSeeMore = view.findViewById(R.id.btn_see_more);
        }
    }
}
