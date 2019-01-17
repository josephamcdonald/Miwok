package com.example.android.miwok;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NumbersFragment extends Fragment {

    public NumbersFragment() {
        // Required empty public constructor
    }

    // Declare MediaPlayer to play the audio.
    private MediaPlayer mp;

    // Declare an AudioManager.
    private AudioManager myAudioManager;

    // Create an OnAudioFocusChangeListener.
    private final AudioManager.OnAudioFocusChangeListener myAFChangeListener = new AudioManager.OnAudioFocusChangeListener() {

        @Override
        public void onAudioFocusChange(int afChange) {
            switch (afChange) {
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    // Pause and set to start.
                    mp.pause();
                    mp.seekTo(0);
                    break;

                case AudioManager.AUDIOFOCUS_LOSS:
                    // Release audio resources.
                    releaseMediaPlayer();
                    break;

                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    // Pause and set to start.
                    mp.pause();
                    mp.seekTo(0);
                    break;

                case AudioManager.AUDIOFOCUS_GAIN:
                    // Start the aduio.
                    mp.start();
                    break;
            }
        }
    };

    // Declare OnCompletionListener to release media player resources.
    private final MediaPlayer.OnCompletionListener myOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        // Get AudioManager System Service and assign to the AudioManager.
        myAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> words = new ArrayList<>();

        words.add(new Word(getString(R.string.one), getString(R.string.lutti), R.drawable.number_one, R.raw.number_one));
        words.add(new Word(getString(R.string.two), getString(R.string.otiiko), R.drawable.number_two, R.raw.number_two));
        words.add(new Word(getString(R.string.three), getString(R.string.tolookosu), R.drawable.number_three, R.raw.number_three));
        words.add(new Word(getString(R.string.four), getString(R.string.oyyisa), R.drawable.number_four, R.raw.number_four));
        words.add(new Word(getString(R.string.five), getString(R.string.massokka), R.drawable.number_five, R.raw.number_five));
        words.add(new Word(getString(R.string.six), getString(R.string.temmokka), R.drawable.number_six, R.raw.number_six));
        words.add(new Word(getString(R.string.seven), getString(R.string.kenekaku), R.drawable.number_seven, R.raw.number_seven));
        words.add(new Word(getString(R.string.eight), getString(R.string.kawinta), R.drawable.number_eight, R.raw.number_eight));
        words.add(new Word(getString(R.string.nine), getString(R.string.woe), R.drawable.number_nine, R.raw.number_nine));
        words.add(new Word(getString(R.string.ten), getString(R.string.naaacha), R.drawable.number_ten, R.raw.number_ten));

        WordAdapter wordAdapter = new WordAdapter(getActivity(), words, R.color.category_numbers);

        final ListView listView = (ListView) rootView.findViewById(R.id.list);

        listView.setAdapter(wordAdapter);

        // Create OnItemClickListener for audio.
        AdapterView.OnItemClickListener myOnItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // Get the Word clicked.
                Word playThisWord = words.get(i);

                releaseMediaPlayer();

                //  Check to see if we have audio focus.
                int afRequestResult = myAudioManager.requestAudioFocus(myAFChangeListener,
                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                // If we have audio focus, create and start the audio.
                if (afRequestResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mp = MediaPlayer.create(getActivity(), playThisWord.getAudioResourceId());
                    mp.setOnCompletionListener(myOnCompletionListener);
                    mp.start();
                }
            }
        };
        // Set myOnItemClickListener to list view.
        listView.setOnItemClickListener(myOnItemClickListener);
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();

        releaseMediaPlayer();
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mp != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mp.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mp = null;

            // Abandon audio focus.
            myAudioManager.abandonAudioFocus(myAFChangeListener);
        }
    }
}
