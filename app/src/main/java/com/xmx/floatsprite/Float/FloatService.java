package com.xmx.floatsprite.Float;

import android.content.Intent;

import com.xmx.floatsprite.MainActivity;
import com.xmx.floatsprite.OperationLog.OperationLogEntityManager;
import com.xmx.floatsprite.Tools.Float.FloatViewManager;
import com.xmx.floatsprite.Tools.ServiceBase.BaseService;
import com.xmx.floatsprite.Tools.Timer;

public class FloatService extends BaseService {

    long time = System.currentTimeMillis();
    Timer timer;

    @Override
    protected void processLogic() {
        FloatViewManager.getInstance().showFloatView(this, new FloatView(getApplicationContext()));
        OperationLogEntityManager.getInstance().addLog("服务开始");

        timer = new Timer() {
            @Override
            public void timer() {
                long now = System.currentTimeMillis();
                OperationLogEntityManager.getInstance().addLog("服务已运行" + (now - time) + "毫秒");
            }
        };
        timer.start(5000);
    }

    @Override
    public void onDestroy() {
        //FloatViewManager.getInstance().hideFloatView(this);
        OperationLogEntityManager.getInstance().addLog("服务停止");
        Intent service = new Intent(FloatService.this, FloatService.class);
        startService(service);
        OperationLogEntityManager.getInstance().addLog("服务重启");
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
