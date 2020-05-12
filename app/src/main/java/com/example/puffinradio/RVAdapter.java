package com.example.puffinradio;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedHashMap;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.GuessViewHolder>{

    LinkedHashMap<String, String> guesses;

    RVAdapter(LinkedHashMap<String, String> guesses){
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
        String userGuess = guesses.get(callSign);

        String correct;
        if(userGuess.equalsIgnoreCase(callSign)) {
            correct = "Y";
            guessViewHolder.guess.setText(callSign);
        } else {
            correct = "N";
            SpannableString ss = new SpannableString(callSign);
            ForegroundColorSpan redColor = new ForegroundColorSpan(Color.RED);
            if(userGuess.length() == callSign.length() || userGuess.length() > callSign.length()){
                for(int j = 0; j < callSign.length();j++){
                    if(callSign.charAt(j) != userGuess.charAt(j)){
                        ss.setSpan(redColor, j, j + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            } else {
                for(int j = 0; j < userGuess.length();j++){
                    if(callSign.charAt(j) != userGuess.charAt(j)){
                        ss.setSpan(redColor, j, j + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
                ss.setSpan(redColor, userGuess.length(), callSign.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            guessViewHolder.guess.setText(ss);
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