package com.xmx.floatsprite.Float;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.xmx.floatsprite.R;
import com.xmx.floatsprite.Tools.Float.BaseFloatView;
import com.xmx.floatsprite.Tools.Float.FloatViewManager;

/**
 * Created by The_onE on 2016/8/30.
 */
public class FloatDetailView extends BaseFloatView {

    public FloatDetailView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatDetailView(Context context) {
        this(context, null);
    }

    public FloatDetailView(final Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.layout_float_detail, this);
        edgeMode = EDGE_MODE_XY;

        Button wifi = (Button) findViewById(R.id.btn_wifi);
        wifi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });

        Button bluetooth = (Button) findViewById(R.id.btn_bluetooth);
        bluetooth.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });
    }

    @Override
    public void onTouchStart(MotionEvent event) {

    }

    @Override
    public void onTouchMove(MotionEvent event, long deltaTime,
                            float deltaX, float deltaY, double distance) {

    }

    @Override
    public void onTouchEnd(MotionEvent event, long deltaTime,
                           float deltaX, float deltaY, double distance) {
    }

    @Override
    public void onLongClick() {

    }

    @Override
    public void onSingleClick() {
        FloatViewManager.getInstance().showFloatView(getContext(),
                new FloatView(getContext()));
    }

    @Override
    public void onDoubleClick() {

    }

    @Override
    public void onTripleClick() {

    }
}
