package com.example.luiz.chuvinhaz.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;

import com.example.luiz.chuvinhaz.models.User;
import com.example.luiz.chuvinhaz.utils.Constants;

/**
 * Created by luiz on 6/26/15.
 */
public class PreferencesManager {
    private Context _context;

    public PreferencesManager(Context _context) {
        this._context = _context;
    }

    public String loadPreferences(String key)
    {
        SharedPreferences settings = _context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, _context.MODE_PRIVATE);
        return settings.getString("user_name", "");
    }

    public void savePreferences(String key, String value)
    {
        SharedPreferences settings = _context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, _context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString(key, value);

        editor.commit();
    }

}
