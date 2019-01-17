package com.example.android.miwok;

public class Word {

    /**
     * Default English translation of the Word
     */
    private String mDefaultTranslation;

    /**
     * Miwok translation of the Word
     */
    private String mMiwokTranslation;

    /**
     * Miwok image resource ID of the Word
     */
    private int mImageResourceId = NO_IMAGE_PROVIDED;

    // No image provided with Word
    private static final int NO_IMAGE_PROVIDED = -1;

    /**
     * Miwok audio resource ID of the Word
     */
    private int mAudioResourceID;

    /**
     * Create a new Word object.
     *
     * @param defaultTranslation is the word in a language that the user is already familiar with
     *                           (such as English)
     * @param miwokTranslation   is the word in the Miwok language
     * @param audioResourceID   is the audio resource ID for the word
     */
    public Word(String defaultTranslation, String miwokTranslation, int audioResourceID) {
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
        mAudioResourceID = audioResourceID;
    }

    /**
     * Create a new Word object.
     *
     * @param defaultTranslation is the word in a language that the user is already familiar with
     *                           (such as English)
     * @param miwokTranslation   is the word in the Miwok language
     * @param imageResourceId    is the image resource ID for the word
     * @param audioResourceID   is the audio resource ID for the word
     */
    public Word(String defaultTranslation, String miwokTranslation, int imageResourceId, int audioResourceID) {
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
        mImageResourceId = imageResourceId;
        mAudioResourceID = audioResourceID;
    }

    /**
     * Get the default translation of the word.
     */
    public String getDefaultTranslation() {
        return mDefaultTranslation;
    }

    /**
     * Get the Miwok translation of the word.
     */
    public String getMiwokTranslation() {
        return mMiwokTranslation;
    }

    /**
     * Get the Miwok image ID of the word.
     */
    public int getImageResourceId() {
        return mImageResourceId;
    }

    /*
     * Does this Word include and image ID?
     */
    public boolean hasImage() {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }

    /**
     * Get the Miwok audio ID of the word.
     */
    public int getAudioResourceId() {
        return mAudioResourceID;
    }
}
