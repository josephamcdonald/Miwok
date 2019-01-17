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
public class ColorsFragment extends Fragment {

    public ColorsFragment() {
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

        words.add(new Word(getString(R.string.red), getString(R.string.weṭeṭṭi), R.drawable.color_red, R.raw.color_red));
        words.add(new Word(getString(R.string.green), getString(R.string.chokokki), R.drawable.color_green, R.raw.color_green));
        words.add(new Word(getString(R.string.brown), getString(R.string.ṭakaakki), R.drawable.color_brown, R.raw.color_brown));
        words.add(new Word(getString(R.string.gray), getString(R.string.ṭopoppi), R.drawable.color_gray, R.raw.color_gray));
        words.add(new Word(getString(R.string.black), getString(R.string.kululli), R.drawable.color_black, R.raw.color_black));
        words.add(new Word(getString(R.string.white), getString(R.string.kelelli), R.drawable.color_white, R.raw.color_white));
        words.add(new Word(getString(R.string.dusty_yellow), getString(R.string.ṭopiisә), R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        words.add(new Word(getString(R.string.mustard_yellow), getString(R.string.chiwiiṭә), R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));

        WordAdapter wordAdapter = new WordAdapter(getActivity(), words, R.color.category_colors);

        ListView listView = (ListView) rootView.findViewById(R.id.list);

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
