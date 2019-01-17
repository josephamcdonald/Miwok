package com.example.android.miwok;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class WordAdapter extends ArrayAdapter<Word> {

    private int mColorId;

    public WordAdapter(Context context, ArrayList<Word> words, int colorId) {
        super(context, 0, words);

        mColorId = colorId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Word currentWord = getItem(position);

        TextView miwokTextView = (TextView) listItemView.findViewById(R.id.miwok_text_view);
        miwokTextView.setText(currentWord.getMiwokTranslation());

        TextView defaultTextView = (TextView) listItemView.findViewById(R.id.default_text_view);
        defaultTextView.setText(currentWord.getDefaultTranslation());

        ImageView wordImageView = (ImageView) listItemView.findViewById(R.id.image);

        if (currentWord.hasImage()) {
            wordImageView.setImageResource(currentWord.getImageResourceId());
            wordImageView.setVisibility(View.VISIBLE);

        } else {
            wordImageView.setVisibility(View.GONE);
        }

        int color = ContextCompat.getColor(getContext(), mColorId);

        View textContainer = listItemView.findViewById(R.id.text_container);
        View playButtonView = listItemView.findViewById(R.id.play_arrow);

        textContainer.setBackgroundColor(color);
        playButtonView.setBackgroundColor(color);

        return listItemView;
    }
}
