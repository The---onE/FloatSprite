package com.xmx.floatsprite.Tools.Data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by The_onE on 2016/2/21.
 */
public class DataManager {
    private static DataManager instance;

    Context mContext;
    SharedPreferences mData;

    public synchronized static DataManager getInstance() {
        if (null == instance) {
            instance = new DataManager();
        }
        return instance;
    }

    public void setContext(Context context) {
        mContext = context;
        mData = context.getSharedPreferences("DATA", Context.MODE_PRIVATE);
    }

    public long getVersion() {
        return mData.getLong("version", -1);
    }

    public void setVersion(long value) {
        SharedPreferences.Editor editor = mData.edit();
        editor.putLong("version", value);
        editor.apply();
    }

    public int getInt(String key) {
        return mData.getInt(key, -1);
    }

    public int getInt(String key, int def) {
        return mData.getInt(key, def);
    }

    public void setInt(String key, int value) {
        SharedPreferences.Editor editor = mData.edit();
        editor.putInt(key, value);
        editor.apply();
    }
}
