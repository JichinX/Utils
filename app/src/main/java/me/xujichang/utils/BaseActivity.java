package me.xujichang.utils;

import me.xujichang.util.activity.SuperActivity;

/**
 * des:
 *
 * @author xjc by 2017/11/16 17:12 .
 */

public abstract class BaseActivity extends SuperActivity {
    @Override
    protected long getActivityExitDuration() {
        return 1000;
    }

    @Override
    protected String getMainActivityName() {
        return "MainActivity";
    }

}
