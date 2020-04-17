package com.example.popularmoviesapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmoviesapp.R;
import com.example.popularmoviesapp.model.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    final private ReviewItemClickListener clickListener;
    private List<Review> reviewList;

    public ReviewAdapter(ReviewItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
        Log.d("setReviewList", reviewList.toString());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.review_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutId, parent, false);
        ReviewViewHolder holder = new ReviewViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);
       
        holder.author.setText(review.getAuthor());
        holder.content.setText(review.getShortContent());

        if(review.isLongContent()){
            holder.buttonFullContent.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if (reviewList == null) {
            return 0;
        }

        return reviewList.size();
    }

    public interface ReviewItemClickListener {
        void onReviewItemClick(int itemIndex);
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView author;
        TextView content;
        Button buttonFullContent;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            
            author = (TextView) itemView.findViewById(R.id.author);
            content = (TextView) itemView.findViewById(R.id.content);
            buttonFullContent = (Button) itemView.findViewById(R.id.button_full_content);

            buttonFullContent.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.button_full_content){
                int position = getAdapterPosition();
                clickListener.onReviewItemClick(position);
            }
        }
    }
}
