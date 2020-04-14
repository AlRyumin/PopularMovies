package com.example.popularmoviesapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmoviesapp.R;
import com.example.popularmoviesapp.model.Trailer;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder>{
    final private ListItemClickListener clickListener;
    private List<Trailer> trailerList;

//    public TrailerAdapter(List<Trailer> trailerList, ListItemClickListener clickListener) {
//        this.trailerList = trailerList;
//        this.clickListener = clickListener;
//    }

    public TrailerAdapter(ListItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setTrailerList(List<Trailer> trailerList){
        this.trailerList = trailerList;
        Log.d("setTrailerList", trailerList.toString());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.trailer_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutId, parent, false);
        TrailerViewHolder holder = new TrailerViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        Trailer trailer = trailerList.get(position);
        holder.trailerName.setText(trailer.getName());

        Picasso.get().load(trailer.getImageUrl())
                .placeholder(R.drawable.empty_backdrop)
                .into(holder.trailerImage);

    }

    @Override
    public int getItemCount() {
        if(trailerList == null){
            return 0;
        }

        return trailerList.size();
    }

    public interface ListItemClickListener {
        void onListItemClick(int itemIndex);
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView trailerImage;
        TextView trailerName;

        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);

            trailerImage = (ImageView) itemView.findViewById(R.id.trailer_image);
            trailerName = (TextView) itemView.findViewById(R.id.trailer_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            clickListener.onListItemClick(position);
        }
    }
}
