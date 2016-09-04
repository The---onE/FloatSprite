package com.xmx.floatsprite;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.xmx.floatsprite.Float.FloatService;
import com.xmx.floatsprite.Float.FloatView;
import com.xmx.floatsprite.OperationLog.OperationLogActivity;
import com.xmx.floatsprite.Tools.ActivityBase.BaseNavigationActivity;
import com.xmx.floatsprite.Tools.Float.FloatViewManager;
import com.xmx.floatsprite.User.Callback.AutoLoginCallback;
import com.xmx.floatsprite.User.UserConstants;
import com.xmx.floatsprite.User.UserManager;

import org.xutils.view.annotation.ContentView;

import java.util.List;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseNavigationActivity {
    private long mExitTime = 0;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void setListener() {
        getViewById(R.id.btn_show_float).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean flag = false;

                ActivityManager manager =
                        (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                int defaultNum = 1000;
                List<ActivityManager.RunningServiceInfo> runServiceList = manager
                        .getRunningServices(defaultNum);
                for (ActivityManager.RunningServiceInfo runServiceInfo : runServiceList) {
                    if (runServiceInfo.foreground) {
                        if (runServiceInfo.service
                                .getShortClassName().equals(".Float.FloatService")) {
//                            Intent intent = new Intent();
//                            intent.setComponent(runServiceInfo.service);
//                            stopService(intent);
                            flag = true;
                        }
                    }
                }
                if (flag) {
                    FloatViewManager.getInstance().
                            showFloatView(getApplicationContext(),
                                    new FloatView(getApplicationContext()));
                } else {
                    Intent service = new Intent(MainActivity.this, FloatService.class);
                    startService(service);
                }
            }
        });

        getViewById(R.id.btn_hide_float).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FloatViewManager.getInstance().hideFloatView(getApplicationContext());
//                ActivityManager manager =
//                        (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//                int defaultNum = 1000;
//                List<ActivityManager.RunningServiceInfo> runServiceList = manager
//                        .getRunningServices(defaultNum);
//                boolean flag = false;
//                for (ActivityManager.RunningServiceInfo runServiceInfo : runServiceList) {
//                    if (runServiceInfo.foreground) {
//                        if (runServiceInfo.service
//                                .getShortClassName().equals(".Float.FloatService")) {
//                            Intent intent = new Intent();
//                            intent.setComponent(runServiceInfo.service);
//                            stopService(intent);
//                            showToast("已关闭浮动窗口");
//                            flag = true;
//                        }
//                    }
//                }
//                if (!flag) {
//                    showToast("浮动窗口未开启");
//                }
            }
        });

        getViewById(R.id.btn_oper_log).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(OperationLogActivity.class);
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        boolean flag = false;

        ActivityManager manager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        int defaultNum = 1000;
        List<ActivityManager.RunningServiceInfo> runServiceList = manager
                .getRunningServices(defaultNum);
        for (ActivityManager.RunningServiceInfo runServiceInfo : runServiceList) {
            if (runServiceInfo.foreground) {
                if (runServiceInfo.service
                        .getShortClassName().equals(".Float.FloatService")) {
//                            Intent intent = new Intent();
//                            intent.setComponent(runServiceInfo.service);
//                            stopService(intent);
                    flag = true;
                }
            }
        }
        if (flag) {
            FloatViewManager.getInstance().
                    showFloatView(getApplicationContext(),
                            new FloatView(getApplicationContext()));
        } else {
            Intent service = new Intent(MainActivity.this, FloatService.class);
            startService(service);
        }

        NavigationView navigation = getViewById(R.id.nav_view);
        Menu menu = navigation.getMenu();
        final MenuItem login = menu.findItem(R.id.nav_logout);

        UserManager.getInstance().autoLogin(new AutoLoginCallback() {
            @Override
            public void success(final AVObject user) {
                login.setTitle(user.getString("nickname") + " 点击注销");
            }

            @Override
            public void error(AVException e) {
                filterException(e);
            }

            @Override
            public void error(int error) {
                switch (error) {
                    case UserConstants.NOT_LOGGED_IN:
                        showToast("请在侧边栏中选择登录");
                        break;
                    case UserConstants.USERNAME_ERROR:
                        showToast("请在侧边栏中选择登录");
                        break;
                    case UserConstants.CHECKSUM_ERROR:
                        showToast("登录过期，请在侧边栏中重新登录");
                        break;
                }
            }
        });
    }

    private void stopService() {
        ActivityManager manager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        int defaultNum = 1000;
        List<ActivityManager.RunningServiceInfo> runServiceList = manager
                .getRunningServices(defaultNum);
        for (ActivityManager.RunningServiceInfo runServiceInfo : runServiceList) {
            if (runServiceInfo.foreground) {
                if (runServiceInfo.service
                        .getShortClassName().equals(".Float.FloatService")) {
                    Intent intent = new Intent();
                    intent.setComponent(runServiceInfo.service);
                    stopService(intent);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if ((System.currentTimeMillis() - mExitTime) > Constants.LONGEST_EXIT_TIME) {
                showToast(R.string.confirm_exit);
                mExitTime = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        super.onNavigationItemSelected(item);

        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
