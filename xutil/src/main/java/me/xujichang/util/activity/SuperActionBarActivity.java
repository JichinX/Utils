package me.xujichang.util.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.xujichang.util.R;
import me.xujichang.util.tool.LogTool;

/**
 * des:最基本布局 包括 ActionBar statusBar errorTip
 *
 * @author xjc
 *         Created by xjc on 2017/6/23.
 */

public class SuperActionBarActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * 左侧图片
     */
    private ImageView actionbarLeftImg;
    /**
     * 左侧文字
     */
    private TextView actionbarLeftText;
    /**
     * Title
     */
    private TextView actionbarTitle;
    /**
     * 右侧文字
     */
    private TextView actionbarRightText;
    /**
     * 右侧图片
     */
    private ImageView actionbarRightImg;
    /**
     * ActionBar
     */
    private LinearLayout actionBar;
    /**
     * 子页面
     */
    private ViewGroup root;
    /**
     * 错误提示 icon
     */
    private ImageView mIvErrorTypeImg;
    /**
     * 错误提示信息
     */
    private TextView mTvErrorMsg;
    /**
     * 可修复提示 icon
     */
    private ImageView mIvErrorToRepair;
    /**
     * error 提示布局
     */
    private LinearLayout mLlErrorFloatTip;
    private LinearLayout mStatusBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_root);
        //获取控件
        root = (ViewGroup) findViewById(R.id.activity_layout_container);
        mLlErrorFloatTip = (LinearLayout) findViewById(R.id.ll_error_float_container);
        actionBar = (LinearLayout) findViewById(R.id.activity_actionbar_container);
        mStatusBar = findViewById(R.id.status_bar);
        actionbarRightImg = (ImageView) actionBar.findViewById(R.id.actionbar_right_img);
        actionbarRightText = (TextView) actionBar.findViewById(R.id.actionbar_right_text);
        actionbarTitle = (TextView) actionBar.findViewById(R.id.actionbar_title);
        actionbarLeftText = (TextView) actionBar.findViewById(R.id.actionbar_left_text);
        actionbarLeftImg = (ImageView) actionBar.findViewById(R.id.actionbar_left_img);
        mIvErrorTypeImg = (ImageView) mLlErrorFloatTip.findViewById(R.id.iv_error_type_img);
        mTvErrorMsg = (TextView) mLlErrorFloatTip.findViewById(R.id.tv_error_msg);
        mIvErrorToRepair = (ImageView) mLlErrorFloatTip.findViewById(R.id.iv_error_to_repair);

        LogTool.d("获取完控件");
    }

    @Override
    public void setContentView(View view) {
        LogTool.d("添加子布局");
        //添加子布局
        root.addView(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        paletteColor(actionBar);
    }

    private void paletteColor(View view) {
        Drawable background = view.getBackground();
        if (background instanceof ColorDrawable) {
            ColorDrawable colorDrawable = (ColorDrawable) background;
            int color = colorDrawable.getColor();
            initStatusBar(color);
            LogTool.d("Color Drawable");
        } else {
            LogTool.d("Bitmap Drawable");
            BitmapDrawable bitmapDrawable = (BitmapDrawable) background;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    //1.获取活力颜色值
                    Palette.Swatch a = palette.getVibrantSwatch();
                    if (null != a) {
                        initStatusBar(a.getRgb());
                    }
                }
            });
        }
    }

    /**
     * 对StatusBar做处理 默认提取ActionBar的颜色
     */
    private void initStatusBar(int color) {
        Window window = getWindow();
        int version = Build.VERSION.SDK_INT;
        //小于4.4 无法实现
        if (version < Build.VERSION_CODES.KITKAT) {
            return;
        }
        ColorDrawable colorDrawable = new ColorDrawable(color);
        colorDrawable.setAlpha(230);
        int barcolor = colorDrawable.getColor();
        actionBar.setBackgroundColor(barcolor);
        colorDrawable = null;
        //大于4.4  小于5.0
        if (version < Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int height = getStatusBarHeight(this);
            LogTool.d("原来的高度：" + mStatusBar.getLayoutParams().height);
            ViewGroup.LayoutParams params = mStatusBar.getLayoutParams();
            params.height = height;
            mStatusBar.setLayoutParams(params);
            //设置一个状态栏
            mStatusBar.setVisibility(View.VISIBLE);
            mStatusBar.setBackgroundColor(color);
            LogTool.d("后来的高度：" + mStatusBar.getLayoutParams().height);
            //设置一个半透明效果，Material Design的效果
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }


    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = context.getResources().getDimensionPixelSize(resId);
        }
        return result;
    }

    @Override
    public void setContentView(int layoutResID) {
        setContentView(View.inflate(this, layoutResID, null));
    }

    /*********************************actionBar start***********************************************
     * 设置Title
     *
     * @param title title
     */
    protected void setActionBarTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            actionbarTitle.setVisibility(View.GONE);
        } else {
            actionbarTitle.setVisibility(View.VISIBLE);
            actionbarTitle.setText(title);
            actionbarTitle.setOnClickListener(this);
        }
    }

    protected void setActionBarDrawable(@DrawableRes int id) {
        actionBar.setBackgroundResource(id);
        paletteColor(actionBar);
    }

    protected void setActionBarColor(@ColorInt int color) {
        actionBar.setBackgroundColor(color);
        initStatusBar(color);
    }

    /**
     * 显示返回箭头
     */
    protected void showBackArrow() {
        actionbarLeftImg.setVisibility(View.VISIBLE);
        actionbarLeftImg.setOnClickListener(this);
        keepBalance();
    }

    /**
     * 为了title居中
     */
    private void keepBalance() {
        if (actionbarLeftImg.getVisibility() == View.VISIBLE) {
            if (actionbarRightImg.getVisibility() != View.VISIBLE) {
                actionbarRightImg.setVisibility(View.INVISIBLE);
            }
        } else {
            if (actionbarRightImg.getVisibility() == View.VISIBLE) {
                actionbarLeftImg.setVisibility(View.INVISIBLE);
            }
        }
        if (actionbarLeftText.getVisibility() == View.VISIBLE) {
            if (actionbarRightText.getVisibility() != View.VISIBLE) {
                actionbarRightText.setVisibility(View.INVISIBLE);
            }
        } else {
            if (actionbarRightText.getVisibility() == View.VISIBLE) {
                actionbarLeftText.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 设置左侧文字
     */
    protected void setLeftText(String str) {
        if (TextUtils.isEmpty(str)) {
            actionbarLeftText.setVisibility(View.GONE);
        } else {
            actionbarLeftText.setVisibility(View.VISIBLE);
            actionbarLeftText.setText(str);
            actionbarLeftText.setOnClickListener(this);
        }
        keepBalance();
    }

    /**
     * 设置左侧图片
     */
    protected void setLeftImg(@DrawableRes int id) {
        actionbarLeftImg.setVisibility(View.VISIBLE);
        actionbarLeftImg.setImageResource(id);
        actionbarLeftImg.setOnClickListener(this);
        keepBalance();
    }

    /**
     * 显示返回箭头
     */
    protected void showForwardArrow() {
        actionbarRightImg.setVisibility(View.VISIBLE);
        actionbarRightImg.setOnClickListener(this);
        keepBalance();
    }

    /**
     * 设置右侧文字
     */
    protected void setRightText(String str) {
        if (TextUtils.isEmpty(str)) {
            actionbarRightText.setVisibility(View.GONE);
        } else {
            actionbarRightText.setVisibility(View.VISIBLE);
            actionbarRightText.setText(str);
            actionbarRightText.setOnClickListener(this);
        }
        keepBalance();
    }

    /**
     * 设置右侧图片
     */
    protected void setRightImg(@DrawableRes int id) {
        actionbarRightImg.setVisibility(View.VISIBLE);
        actionbarRightImg.setImageResource(id);
        actionbarRightImg.setOnClickListener(this);
        keepBalance();
    }


    protected void onTitleClick() {

    }

    protected void onRightAreaClick() {

    }

    protected void onLeftAreaClick() {
        onBackPressed();
    }

    protected void hideActionBar() {
        actionBar.setVisibility(View.GONE);
    }

    @Nullable
    protected LinearLayout getSuperActionBar() {
        return actionBar;
    }

    protected ViewGroup getSuperRoot() {
        return root;
    }

    protected boolean isActionBarShow() {
        return actionBar.getVisibility() == View.VISIBLE;
    }

    /**
     * ==================================actionbar  end==============================================
     * ==================================error tip   start==============================================
     */
    /**
     * 设置错误提示信息
     *
     * @param msg
     */
    protected void setErrorMsg(String msg, boolean show) {
        mTvErrorMsg.setText(msg);
        showView(mTvErrorMsg, show);
    }

    /**
     * 设置错误提示icon
     *
     * @param id
     */
    protected void setErrorIcon(@DrawableRes int id, boolean show) {
        if (id != -1) {
            if (id != 0) {
                mIvErrorTypeImg.setImageResource(id);
            }
        } else {
            show = false;
        }
        showView(mIvErrorTypeImg, show);
    }

    /**
     * 设置错误提示icon
     *
     * @param id
     */
    protected void setErrorIcon(@DrawableRes int id) {
        if (id != -1) {
            if (id != 0) {
                mIvErrorTypeImg.setImageResource(id);
            }
        } else {
            mIvErrorTypeImg.setVisibility(View.GONE);
        }
    }

    private void showView(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    protected void setErrorIcon(Drawable drawable) {
        if (drawable == null) {
            return;
        }
        mIvErrorTypeImg.setImageDrawable(drawable);
    }

    protected void setErrorRepairIcon(@DrawableRes int id) {
        if (id != -1) {
            mIvErrorToRepair.setImageResource(id);
        } else {
            mIvErrorToRepair.setVisibility(View.GONE);
        }
    }

    protected void setIvErrorRepair(Drawable drawable) {
        mIvErrorToRepair.setImageDrawable(drawable);
    }

    protected void showErrorTip(String msg) {
        showErrorTip(0, msg, 0, false);
    }

    protected void showErrorTip(String msg, boolean canDeal) {
        showErrorTip(0, msg, 0, canDeal);
    }

    protected void showErrorTip(@DrawableRes int errorTypeRes, String msg, @DrawableRes int repairRes, boolean canDeal) {
        setErrorIcon(errorTypeRes, true);
        setErrorRepairIcon(repairRes, canDeal);
        setErrorMsg(msg, true);
        mLlErrorFloatTip.setVisibility(View.VISIBLE);
        mLlErrorFloatTip.setOnClickListener(canDeal ? this : null);
    }

    private void setErrorRepairIcon(@DrawableRes int repairRes, boolean show) {
        if (repairRes != 0) {
            mIvErrorToRepair.setImageResource(repairRes);
        }
        showView(mIvErrorToRepair, show);
    }

    protected void onErrorTipClick() {

    }

    protected void hideErrorTip() {
        mLlErrorFloatTip.setVisibility(View.GONE);
    }

    /**
     * ==================================error tip  end==============================================
     */
    @Override
    public void onClick(View v) {
        //library 中 不能使用资源ID来做switch case的条件
        int id = v.getId();
        if (id == R.id.actionbar_left_img || id == R.id.actionbar_left_text) {
            onLeftAreaClick();
        } else if (id == R.id.actionbar_right_text || id == R.id.actionbar_right_img) {
            onRightAreaClick();
        } else if (id == R.id.actionbar_title) {
            onTitleClick();
        } else if (id == R.id.ll_error_float_container) {
            onErrorTipClick();
        }
    }
}
