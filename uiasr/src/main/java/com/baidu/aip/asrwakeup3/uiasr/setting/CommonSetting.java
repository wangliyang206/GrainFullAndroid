package com.baidu.aip.asrwakeup3.uiasr.setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

public abstract class CommonSetting extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    protected int setting;
    protected String title = "通用设置";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(title);
        this.addPreferencesFromResource(setting);

        PreferenceScreen s = (PreferenceScreen) findPreference("root_screen");
        bind(s);
    }

    private void bind(PreferenceGroup group) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        for (int i = 0; i < group.getPreferenceCount(); i++) {
            Preference p = group.getPreference(i);
            if (p instanceof PreferenceGroup) {
                bind((PreferenceGroup) p);
            } else {
                if (p instanceof CheckBoxPreference) {
                    ;
                } else {
                    Object val = sp.getAll().get(p.getKey());
                    p.setSummary(val == null ? "" : ("" + val));
                    p.setOnPreferenceChangeListener(this);
                }
            }
        }
    }

    @Override
    public boolean onPreferenceChange(Preference p, Object newValue) {
        if (p instanceof CheckBoxPreference) {
            ;
        } else {
            p.setSummary("" + newValue);
        }
        return true;
    }
}
