package com.xmx.floatsprite.Float;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xmx.floatsprite.OperationLog.OperationLogEntityManager;
import com.xmx.floatsprite.QRCode.ScanQRCodeActivity;
import com.xmx.floatsprite.R;
import com.xmx.floatsprite.Tools.Float.BaseFloatView;
import com.xmx.floatsprite.Tools.Float.FloatViewManager;

import java.util.List;

/**
 * Created by The_onE on 2016/8/30.
 */
public class FloatDetailView extends BaseFloatView {

    Camera camera;
    private boolean flashlightFlag = false;

    public FloatDetailView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatDetailView(Context context) {
        this(context, null);
    }

    public FloatDetailView(final Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.layout_float_detail, this);
        //edgeMode = EDGE_MODE_XY;
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

        Button vpn = (Button) findViewById(R.id.btn_vpn);
        vpn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.net.vpn.SETTINGS");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
                OperationLogEntityManager.getInstance().addLog("打开VPN");
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

        Button flashlight = (Button) findViewById(R.id.btn_flashlight);
        flashlight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!flashlightFlag) {
                    camera = Camera.open();
                    Camera.Parameters params = camera.getParameters();
                    List<String> list = params.getSupportedFlashModes();
                    if (list.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                        params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    } else {
                        Toast.makeText(getContext(), "此设备不支持手电筒功能",
                                Toast.LENGTH_SHORT).show();
                    }
                    camera.setParameters(params);
                    camera.startPreview();
                    flashlightFlag = true;
                } else {
                    Camera.Parameters closeParameters = camera.getParameters();
                    closeParameters
                            .setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    camera.setParameters(closeParameters);
                    camera.stopPreview();
                    camera.release();
                    flashlightFlag = false;
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
