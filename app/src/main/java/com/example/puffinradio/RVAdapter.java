package com.example.puffinradio;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedHashMap;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.GuessViewHolder>{

    LinkedHashMap<String, Boolean> guesses;

    RVAdapter(LinkedHashMap<String, Boolean> guesses){
        this.guesses = guesses;
    }

    @Override
    public int getItemCount() {
        return guesses.size();
    }

    @Override
    public GuessViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.guess, viewGroup, false);
        GuessViewHolder holder = new GuessViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(GuessViewHolder guessViewHolder, int i) {
        String callSign = guesses.keySet().toArray()[i].toString();
        guessViewHolder.guess.setText(callSign);
        String correct;
        if(guesses.get(callSign)) {
            correct = "Y";
        } else {
            correct = "N";
        }
        guessViewHolder.correct.setText(correct);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class GuessViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView guess;
        TextView correct;

        GuessViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.cardView);
            guess = (TextView)itemView.findViewById(R.id.guessText);
            correct = (TextView)itemView.findViewById(R.id.correct);
        }
    }

}