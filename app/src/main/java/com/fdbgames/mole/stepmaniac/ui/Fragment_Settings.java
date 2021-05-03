package com.fdbgames.mole.stepmaniac.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;

import com.fdbgames.mole.stepmaniac.MainActivity;
import com.fdbgames.mole.stepmaniac.R;

import java.util.Locale;

public class Fragment_Settings extends PreferenceFragment implements OnPreferenceClickListener {

    final static int DEFAULT_GOAL = 10000;
    public final static float DEFAULT_STEP_SIZE = Locale.getDefault() == Locale.US ? 2.5f : 75f;
    public final static String DEFAULT_GENDER = "notparticipate";
    public final static String DEFAULT_TEAM = "None";
    final static String DEFAULT_STEP_UNIT = Locale.getDefault() == Locale.US ? "ft" : "cm";

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);

        final SharedPreferences prefs =
                getActivity().getSharedPreferences("molegame", Context.MODE_PRIVATE);
        Preference gender = findPreference("volume");
        gender.setOnPreferenceClickListener(this);
        gender.setSummary(getString(R.string.volume_summary,
                prefs.getString("volume", getString(R.string.on))));

        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onResume() {
        super.onResume();
       // getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public void onPrepareOptionsMenu(final Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_settings).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        return ((MainActivity) getActivity()).onOptionsItemSelected(item);
    }

    @Override
    public boolean onPreferenceClick(final Preference preference) {
        AlertDialog.Builder builder;
        View v;
        final SharedPreferences prefs =
                getActivity().getSharedPreferences("molegame", Context.MODE_PRIVATE);
        Log.d("Fragment settings",""+preference.getTitleRes()+" string key "+preference.getKey());
        switch (preference.getTitleRes()) {

            case R.string.volume:
                Log.i("Fragment Settings "," elegido volume");
                builder = new AlertDialog.Builder(getActivity());
                v = getActivity().getLayoutInflater().inflate(R.layout.volume, null);
                final RadioGroup volume = (RadioGroup) v.findViewById(R.id.volume);
                final String selVolume = prefs.getString("volume", Fragment_Settings.DEFAULT_GENDER);
                Log.i("Fragment Settings "," elegido volume con anterioridad "+selVolume);
                if ( selVolume.equals("off")){
                      volume.check( R.id.off);
                } else if (selVolume.equals("on") ){
                     volume.check( R.id.on);
                }
                volume.check(
                        prefs.getString("volume", "off").equals("on") ? R.id.on :
                                R.id.off);

                builder.setView(v);
                builder.setTitle(R.string.volume);
                builder.setPositiveButton(android.R.string.ok, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            int selVolumerInt = volume.getCheckedRadioButtonId();
                            Log.i("Fragment Settings ","******* elegido volume onclick "+selVolumerInt+" on "+R.id.on+" off "+R.id.off);
                            if ( selVolumerInt == R.id.on){
                                Log.i("Fragment Settings "," elegido gender male ");
                                prefs.edit()
                                        .putString("volume","on").apply();
                            }else if ( selVolumerInt == R.id.off){
                                prefs.edit()
                                        .putString("volume","off").apply();
                            }
                            prefs.edit()
                                    .putString("volume :",
                                            volume.getCheckedRadioButtonId() == R.id.on ? "on" : "off")
                                    .apply();/*
                            preference.setSummary(getString(R.string.gender_summary,
                                    prefs.getString("gender", DEFAULT_GENDER)));
                            //prefs.setSummary(getString(R.string.step_size_summary, prefs.getString("gender",Fragment_Settings.DEFAULT_GENDER)));*/
                        } catch (NumberFormatException nfe) {
                            nfe.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;

        }
        return false;
    }

    private boolean hasWriteExternalPermission() {
        return getActivity().getPackageManager()
                .checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        getActivity().getPackageName()) == PackageManager.PERMISSION_GRANTED;
    }


}
