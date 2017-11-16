package me.xujichang.utils;

import android.os.Bundle;
import android.support.annotation.Nullable;

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
        showErrorTip("网络错误", true);
    }

    @Override
    protected void onErrorTipClick() {
        startLoading("哈喽哈喽");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopLoading();
    }
}
