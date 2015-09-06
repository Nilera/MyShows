package me.myshows.android.ui.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;

import com.bumptech.glide.Glide;

import org.apache.commons.io.FileUtils;

import java.io.File;

import me.myshows.android.R;

/**
 * Created by Whiplash on 06.09.2015.
 */
public class SettingsFragment extends PreferenceFragment {

    public static final String COMPACT_MODE = "compact_mode";
    public static final String CLEAR_CACHE = "clear_cache";
    public static final String CACHE_SIZE = "cache_size";
    public static final String CHECK_NEW_SERIES = "check_new_series";
    public static final String RINGTONE = "ringtone";
    public static final String VIBRATION = "vibration";

    private static final String TAG = SettingsFragment.class.getSimpleName();

    private CheckBoxPreference compactModePreference;
    private Preference clearCachePreference;
    private ListPreference cacheSizePreference;
    private CheckBoxPreference checkNewSeriesPreference;
    private RingtonePreference ringtonePreference;
    private CheckBoxPreference vibrationPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.myshows_preference);

        compactModePreference = (CheckBoxPreference) findPreference(COMPACT_MODE);
        clearCachePreference = findPreference(CLEAR_CACHE);
        cacheSizePreference = (ListPreference) findPreference(CACHE_SIZE);
        checkNewSeriesPreference = (CheckBoxPreference) findPreference(CHECK_NEW_SERIES);
        ringtonePreference = (RingtonePreference) findPreference(RINGTONE);
        vibrationPreference = (CheckBoxPreference) findPreference(VIBRATION);

        clearCachePreferenceInitialize();
        checkNewSeriesPreferenceInitialize();
        ringtonePreferenceInitialize();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == ClearCacheDialog.REQUEST_CODE) {
            new ClearCacheTask().execute();
        }
    }

    private void clearCachePreferenceInitialize() {
        setCacheSize(clearCachePreference);
        clearCachePreference.setOnPreferenceClickListener(preference -> {
            FragmentManager fragmentManager = getFragmentManager();
            DialogFragment fragment = new ClearCacheDialog();
            fragment.setTargetFragment(this, ClearCacheDialog.REQUEST_CODE);
            fragment.show(fragmentManager, null);
            return true;
        });
    }

    private void setCacheSize(Preference preference) {
        long bytes = getCacheSize(Glide.getPhotoCacheDir(getActivity()));
        preference.setSummary(FileUtils.byteCountToDisplaySize(bytes));
    }

    private long getCacheSize(File dir) {
        long bytes = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                bytes += getCacheSize(dir);
            } else {
                bytes += file.length();
            }
        }
        return bytes;
    }

    private void checkNewSeriesPreferenceInitialize() {
        checkNewSeriesPreference.setOnPreferenceChangeListener((preference, value) -> {
            ringtonePreference.setEnabled((Boolean) value);
            vibrationPreference.setEnabled((Boolean) value);
            return true;
        });
    }

    private void ringtonePreferenceInitialize() {
        setRingtoneName(ringtonePreference);
        ringtonePreference.setOnPreferenceChangeListener((preference, value) -> {
            setRingtoneName(ringtonePreference, value.toString());
            return true;
        });
    }

    private void setRingtoneName(Preference preference) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        setRingtoneName(preference, preferences.getString(RINGTONE, null));
    }

    private void setRingtoneName(Preference preference, String uri) {
        if (uri == null || uri.isEmpty()) {
            preference.setSummary(R.string.none_ringtone);
        } else {
            Ringtone ringtone = RingtoneManager.getRingtone(getActivity(), Uri.parse(uri));
            preference.setSummary(ringtone.getTitle(getActivity()));
        }
    }

    private class ClearCacheTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Glide.get(getActivity()).clearDiskCache();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setCacheSize(clearCachePreference);
        }
    }
}
