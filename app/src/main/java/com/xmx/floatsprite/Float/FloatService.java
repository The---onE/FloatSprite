package com.xmx.floatsprite.Float;

import android.content.Intent;

import com.xmx.floatsprite.MainActivity;
import com.xmx.floatsprite.Tools.Float.FloatViewManager;
import com.xmx.floatsprite.Tools.ServiceBase.BaseService;

public class FloatService extends BaseService {

    @Override
    protected void processLogic() {
        FloatViewManager.getInstance().showFloatView(this, new FloatView(getApplicationContext()));
    }

    @Override
    public void onDestroy() {
        //FloatViewManager.getInstance().hideFloatView(this);
        Intent service = new Intent(FloatService.this, FloatService.class);
        startService(service);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    protected void setForeground() {
        showForeground(MainActivity.class, "桌面精灵运行中");
    }
}
