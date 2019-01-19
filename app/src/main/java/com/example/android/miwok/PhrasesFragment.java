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
public class PhrasesFragment extends Fragment {

    public PhrasesFragment() {
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

        words.add(new Word(getString(R.string.Where_are_you_going), getString(R.string.minto_wuksus), R.raw.phrase_where_are_you_going));
        words.add(new Word(getString(R.string.What_is_your_name), getString(R.string.tinnә_oyaasenә), R.raw.phrase_what_is_your_name));
        words.add(new Word(getString(R.string.My_name_is), getString(R.string.oyaaset), R.raw.phrase_my_name_is));
        words.add(new Word(getString(R.string.How_are_you_feeling), getString(R.string.michәksәs), R.raw.phrase_how_are_you_feeling));
        words.add(new Word(getString(R.string.Im_feeling_good), getString(R.string.kuchi_achit), R.raw.phrase_im_feeling_good));
        words.add(new Word(getString(R.string.Are_you_coming), getString(R.string.әәnәsaa), R.raw.phrase_are_you_coming));
        words.add(new Word(getString(R.string.Yes_Im_coming), getString(R.string.hәә_әәnәm), R.raw.phrase_yes_im_coming));
        words.add(new Word(getString(R.string.Im_coming), getString(R.string.әәnәm), R.raw.phrase_im_coming));
        words.add(new Word(getString(R.string.Lets_go), getString(R.string.yoowutis), R.raw.phrase_lets_go));
        words.add(new Word(getString(R.string.Come_here), getString(R.string.әnninem), R.raw.phrase_come_here));

        WordAdapter wordAdapter = new WordAdapter(getActivity(), words, R.color.category_phrases);

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
