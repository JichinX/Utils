package me.xujichang.utils;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import me.xujichang.util.tool.LogTool;

public class MainActivity extends AppCompatActivity {

    private int mLastHeight;
    private Rect r;
    private View decorView;
    private View container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_main);
        //获取window的视图
        r = new Rect();
        decorView = this.getWindow().getDecorView();
        container = findViewById(R.id.container);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.material_green_300));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            container.setBackgroundColor(getResources().getColor(R.color.material_green_300));
        }
        enableGlobalLayoutListener();
    }

    public void onClick(View view) {
        startActivity(new Intent(this, TestActivity.class));
        finish();
    }

    /**
     * 软键盘弹出的操作、
     * 软键盘弹出 改变布局大小
     */
    protected void enableGlobalLayoutListener() {
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        r.setEmpty();
                        decorView.getWindowVisibleDisplayFrame(r);
                        //window视图添加控件此方法可能会被多次调用,防止重复调用
                        if (mLastHeight != r.bottom) {
                            int[] location = new int[2];
                            decorView.getLocationOnScreen(location);
                            //上次的高度，比这次大，说明这次是软键盘显示的状态
                            mLastHeight = r.bottom;
                            ViewGroup.LayoutParams params = decorView.getLayoutParams();
                            params.height = r.bottom - (decorView.getTop() + location[1]);
                            decorView.setLayoutParams(params);
                        }
                    }
                });
    }

}
