package com.example.movie2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private ArrayList<Movie> items = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Movie item){
        items.add(item);
    }

    public void setItems(ArrayList<Movie> items){
        this.items = items;
    }

    public void clearItems() {
        items.clear();
        notifyDataSetChanged();
    }



    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView, textView2, textView3, textView4, textView5;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            textView4 = itemView.findViewById(R.id.textView_genre);
            textView5 = itemView.findViewById(R.id.textView_nation);

        }

        public void setItem(Movie item) {
            textView.setText(item.movieNm);
            StringBuilder directorNames = new StringBuilder();
            List<Director> directors = item.directors; // 감독 정보 가져오기
            for (Director director : item.directors) {
                directorNames.append(director.peopleNm).append(", ");
            }
            if (directorNames.length() > 0) {
                directorNames.setLength(directorNames.length() - 2); // 마지막 쉼표 및 공백 제거
            }
            textView2.setText("감독 : " + directorNames.toString());
            textView3.setText("개봉일 : " + item.openDt);
            textView4.setText("장르 : " + item.genreAlt);
            textView5.setText("국가 : " + item.nationAlt);

        }


    }
}

