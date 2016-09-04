package com.xmx.floatsprite.Float;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.Toast;

import com.xmx.floatsprite.OperationLog.OperationLogEntityManager;
import com.xmx.floatsprite.R;
import com.xmx.floatsprite.Tools.Data.DataManager;
import com.xmx.floatsprite.Tools.Float.BaseFloatView;
import com.xmx.floatsprite.Tools.Float.FloatViewManager;

/**
 * Created by The_onE on 2016/8/26.
 */
public class FloatView extends BaseFloatView {

    public FloatView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatView(Context context) {
        this(context, null);
    }

    public FloatView(final Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.layout_float, this);

        edgeMode = EDGE_MODE_XY;
        OperationLogEntityManager.getInstance().addLog("创建浮动窗口");

//        Button hideButton = (Button) findViewById(R.id.btn_hide);
//        hideButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FloatViewManager.getInstance().showFloatView(context, new SmallFloatView(context));
//            }
//        });
//
//        Button floatButton = (Button) findViewById(R.id.btn_float);
//        floatButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getContext(), MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                getContext().startActivity(intent);
//            }
//        });
    }

    private void changeVolume() {
        int prevMusicVolume = DataManager.getInstance().getInt("music_volume", 0);
        int prevSystemVolume = DataManager.getInstance().getInt("system_volume", 0);

        AudioManager audioManager = (AudioManager) getContext().
                getSystemService(Service.AUDIO_SERVICE);
        //通话音量
        int currentCall = audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);

        //系统音量
        int currentSystem = audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);

        //铃声音量
        int currentRing = audioManager.getStreamVolume(AudioManager.STREAM_RING);

        //音乐音量
        int currentMusic = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        //提示声音音量
        int currentAlarm = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);


        if (currentMusic > 0 || currentSystem > 0) {
            DataManager.getInstance().setInt("music_volume", currentMusic);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0,
                    AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);

            DataManager.getInstance().setInt("system_volume", currentSystem);
            audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, 0,
                    AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);

            Toast.makeText(getContext(), "已关闭音量", Toast.LENGTH_SHORT).show();
            OperationLogEntityManager.getInstance().addLog("关闭音量");
        } else {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, prevMusicVolume,
                    AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);

            audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, prevSystemVolume,
                    AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);

            Toast.makeText(getContext(), "已恢复音量", Toast.LENGTH_SHORT).show();
            OperationLogEntityManager.getInstance().addLog("恢复音量");
        }
    }

    private void startCamera() {
        Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
        OperationLogEntityManager.getInstance().addLog("打开相机");
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
        changeVolume();
    }

    @Override
    public void onSingleClick() {
        FloatViewManager.getInstance().showFloatView(getContext(), new FloatDetailView(getContext()));
    }

    @Override
    public void onDoubleClick() {
        startCamera();
    }

    @Override
    public void onTripleClick() {
        FloatViewManager.getInstance().hideFloatView(getContext());
    }
}
