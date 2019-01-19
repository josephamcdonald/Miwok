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
public class FamilyFragment extends Fragment {

    public FamilyFragment() {
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

        words.add(new Word(getString(R.string.father),getString(R.string.әpә), R.drawable.family_father, R.raw.family_father));
        words.add(new Word(getString(R.string.mother),getString(R.string.әṭa), R.drawable.family_mother, R.raw.family_mother));
        words.add(new Word(getString(R.string.son),getString(R.string.angsi), R.drawable.family_son, R.raw.family_son));
        words.add(new Word(getString(R.string.daughter),getString(R.string.tune), R.drawable.family_daughter, R.raw.family_daughter));
        words.add(new Word(getString(R.string.older_brother),getString(R.string.taachi), R.drawable.family_older_brother, R.raw.family_older_brother));
        words.add(new Word(getString(R.string.younger_brother),getString(R.string.chalitti), R.drawable.family_younger_brother, R.raw.family_younger_brother));
        words.add(new Word(getString(R.string.older_sister),getString(R.string.teṭe), R.drawable.family_older_sister, R.raw.family_older_sister));
        words.add(new Word(getString(R.string.younger_sister),getString(R.string.kolliti), R.drawable.family_younger_sister, R.raw.family_younger_sister));
        words.add(new Word(getString(R.string.grandmother),getString(R.string.ama), R.drawable.family_grandmother, R.raw.family_grandmother));
        words.add(new Word(getString(R.string.grandfather),getString(R.string.paapa), R.drawable.family_grandfather, R.raw.family_grandfather));

        WordAdapter wordAdapter = new WordAdapter(getActivity(), words, R.color.category_family);
        ListView listView = rootView.findViewById(R.id.list);

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
