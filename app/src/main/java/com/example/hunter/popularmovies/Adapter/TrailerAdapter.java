package com.example.hunter.popularmovies.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hunter.popularmovies.Model.Trailer;
import com.example.hunter.popularmovies.R;


import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerHolder> {
       private Context mcontext;
       private List<Trailer> trailerList;
        private com.example.hunter.popularmovies.Adapter.TrailerAdapter.OnItemClickListener mListner;

        public interface OnItemClickListener{
           void onItemClick(int position);
       }

       public void setOnItemClickListener(OnItemClickListener listener){mListner = listener;}


    public TrailerAdapter(Context mcontext, List<Trailer> trailerList) {
        this.mcontext = mcontext;
        this.trailerList = trailerList;
    }

    @NonNull
    @Override
    public TrailerAdapter.TrailerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer,parent,false);
        return new TrailerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerAdapter.TrailerHolder holder, int position) {
            holder.tv_trailer.setText(trailerList.get(position).getmName());
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }

    public class TrailerHolder extends RecyclerView.ViewHolder{
        public TextView tv_trailer;
        public ImageView iv_trailer;

        public TrailerHolder(View itemView) {
            super(itemView);
            tv_trailer = itemView.findViewById(R.id.textView_trailer);
            iv_trailer = itemView.findViewById(R.id.imageView_trailer);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        Trailer clickedDataItem = trailerList.get(position);
                        String videoId = trailerList.get(position).getMkey();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="+videoId));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("VIDEO_ID",videoId);
                        mcontext.startActivity(intent);

                        Toast.makeText(v.getContext(), "You clicked " + clickedDataItem.getmName(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
