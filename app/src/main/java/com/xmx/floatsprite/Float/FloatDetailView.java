package com.xmx.floatsprite.Float;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.xmx.floatsprite.OperationLog.OperationLogEntityManager;
import com.xmx.floatsprite.QRCode.ScanQRCodeActivity;
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
        OperationLogEntityManager.getInstance().addLog("创建详细浮动窗口");

        Button wifi = (Button) findViewById(R.id.btn_wifi);
        wifi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
                OperationLogEntityManager.getInstance().addLog("打开WIFI");
            }
        });

        Button bluetooth = (Button) findViewById(R.id.btn_bluetooth);
        bluetooth.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
                OperationLogEntityManager.getInstance().addLog("打开蓝牙");
            }
        });

        Button scan = (Button) findViewById(R.id.btn_scan);
        scan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ScanQRCodeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
                OperationLogEntityManager.getInstance().addLog("打开扫码");
            }
        });

        Button search = (Button) findViewById(R.id.btn_search);
        search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String res = "http://www.bing.com";
                try {
                    ClipboardManager cm = (ClipboardManager) getContext().
                            getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData cd = cm.getPrimaryClip();
                    String str = cd.getItemAt(0).getText().toString();
                    if (!str.equals("")) {
                        res = "http://www.bing.com/search?q=" + str;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Uri uri = Uri.parse(res);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(intent);
                    OperationLogEntityManager.getInstance().addLog("打开搜索");
                }
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
