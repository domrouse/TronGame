package com.mygdx.trongame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class appPreferences {
    private static final String PREF_MUSIC_VOLUME = "volume";
    private static final String PREF_MUSIC_ENABLED = "music.enabled";
    private static final String PREF_SOUND_ENABLED = "sound.enabled";
    private static final String PREF_SOUND_VOL = "sound";
    private static final String PREFS_NAME = "b2dtut";
    private static final String PREF_NoOfPlayers = "NoP";
    private static final String PREF_INVINSIBLE_ENABLED = "inv.enabled";
    private static final String PREF_SHRINK_ENABLED = "shrink.enabled";
    private static final String PREF_SCREEN_VEL = "velocity";

    protected Preferences getPrefs() {
        return Gdx.app.getPreferences(PREFS_NAME);
    }

    public boolean isSoundEffectsEnabled() {
        return getPrefs().getBoolean(PREF_SOUND_ENABLED, true);
    }

    public void setSoundEffectsEnabled(boolean soundEffectsEnabled) {
        getPrefs().putBoolean(PREF_SOUND_ENABLED, soundEffectsEnabled);
        getPrefs().flush();
    }

    public boolean isMusicEnabled() {
        return getPrefs().getBoolean(PREF_MUSIC_ENABLED, true);
    }

    public void setMusicEnabled(boolean musicEnabled) {
        getPrefs().putBoolean(PREF_MUSIC_ENABLED, musicEnabled);
        getPrefs().flush();
    }

    public float getMusicVolume() {
        return getPrefs().getFloat(PREF_MUSIC_VOLUME, 0.5f);
    }

    public void setMusicVolume(float volume) {
        getPrefs().putFloat(PREF_MUSIC_VOLUME, volume);
        getPrefs().flush();
    }

    public float getSoundVolume() {
        return getPrefs().getFloat(PREF_SOUND_VOL, 0.5f);
    }

    public void setSoundVolume(float volume) {
        getPrefs().putFloat(PREF_SOUND_VOL, volume);
        getPrefs().flush();
    }

    public float getNoP() {
        return getPrefs().getFloat(PREF_NoOfPlayers, 2f);
    }

    public void setNoP(float NoP) {
        getPrefs().putFloat(PREF_NoOfPlayers, NoP);
        getPrefs().flush();
    }

    public boolean isInvEnabled() {
        return getPrefs().getBoolean(PREF_INVINSIBLE_ENABLED, false);
    }

    public void setInvEnabled(boolean invEnabled) {
        getPrefs().putBoolean(PREF_INVINSIBLE_ENABLED, invEnabled);
        getPrefs().flush();
    }

    public boolean isShrinkEnabled() {
        return getPrefs().getBoolean(PREF_SHRINK_ENABLED, false);
    }

    public void setShrinkEnabled(boolean shrinkEnabled) {
        getPrefs().putBoolean(PREF_SHRINK_ENABLED, shrinkEnabled);
        getPrefs().flush();
    }

    public float getScreenVelocity() {
        return getPrefs().getFloat(PREF_SCREEN_VEL, 4f);
    }

    public void setScreenVelocity(float screenv) {
        getPrefs().putFloat(PREF_SCREEN_VEL, screenv);
        getPrefs().flush();
    }

}
