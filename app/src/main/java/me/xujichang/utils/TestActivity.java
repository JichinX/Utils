package me.xujichang.utils;

import android.app.ActivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.List;

import me.xujichang.util.tool.LogTool;

import static android.app.ActivityManager.RECENT_IGNORE_UNAVAILABLE;
import static android.app.ActivityManager.RECENT_WITH_EXCLUDED;

/**
 * des:
 *
 * @author xjc by 2017/11/16 17:11 .
 */

public class TestActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initActionBar();
    }

    private void initActionBar() {
        showBackArrow();
        setRightText("哈哈");
        setActionBarTitle("测试测试测试测试测试测试测试测试测试测试测试");
        setLeftText("哈哈");
//        showErrorTip("网络错误", true);
    }

    @Override
    protected void onErrorTipClick() {
        //获取Activity栈信息
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        if (null != activityManager) {
            List<ActivityManager.RunningAppProcessInfo> processInfos = activityManager.getRunningAppProcesses();
            List<ActivityManager.RecentTaskInfo> taskInfos = activityManager.getRecentTasks(10, RECENT_WITH_EXCLUDED);
            List<ActivityManager.RecentTaskInfo> recentTaskInfos = activityManager.getRecentTasks(10, RECENT_IGNORE_UNAVAILABLE);
            LogTool.d(taskInfos.size() + "  " + recentTaskInfos.size());
        }

    }

    /**
     * 显示日志
     *
     * @param view
     */
    public void showLog(View view) {
//        LogTool.e(new RuntimeException("Hello Error!"));
        LogTool.d("Nothing!");
    }
}
