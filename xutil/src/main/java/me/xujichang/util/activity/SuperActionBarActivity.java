package me.xujichang.util.activity;

import android.arch.lifecycle.LifecycleActivity;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.xujichang.util.R;

/**
 * @author xjc
 * Created by xjc on 2017/6/23.
 */

public class SuperActionBarActivity extends LifecycleActivity implements View.OnClickListener {

    private ImageView actionbarLeftImg;
    private TextView actionbarLeftText;
    private TextView actionbarTitle;
    private TextView actionbarRightText;
    private ImageView actionbarRightImg;

    public ImageView getActionbarLeftImg() {
        return actionbarLeftImg;
    }

    public void setActionbarLeftImg(ImageView actionbarLeftImg) {
        this.actionbarLeftImg = actionbarLeftImg;
    }

    public TextView getActionbarLeftText() {
        return actionbarLeftText;
    }

    public void setActionbarLeftText(TextView actionbarLeftText) {
        this.actionbarLeftText = actionbarLeftText;
    }

    public TextView getActionbarTitle() {
        return actionbarTitle;
    }

    public void setActionbarTitle(TextView actionbarTitle) {
        this.actionbarTitle = actionbarTitle;
    }

    public TextView getActionbarRightText() {
        return actionbarRightText;
    }

    public void setActionbarRightText(TextView actionbarRightText) {
        this.actionbarRightText = actionbarRightText;
    }

    public ImageView getActionbarRightImg() {
        return actionbarRightImg;
    }

    public void setActionbarRightImg(ImageView actionbarRightImg) {
        this.actionbarRightImg = actionbarRightImg;
    }

    private LinearLayout actionBar;
    private ViewGroup root;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_layout_root);
    }

    @Override
    public void setContentView(View view) {
        root = (ViewGroup) findViewById(R.id.activity_layout_root);
        View rootContainer = findViewById(R.id.activity_layout_root_container);
        if (null != root) {
            root.addView(view, LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            actionBar = (LinearLayout) rootContainer.findViewById(R.id.activity_actionbar);
            initBaseActionBar(actionBar);
        }
    }

    private void initBaseActionBar(LinearLayout actionBar) {
        if (null == actionBar) {
            return;
        }
        //获取控件
        actionbarRightImg = (ImageView) actionBar.findViewById(R.id.actionbar_right_img);
        actionbarRightText = (TextView) actionBar.findViewById(R.id.actionbar_right_text);
        actionbarTitle = (TextView) actionBar.findViewById(R.id.actionbar_title);
        actionbarLeftText = (TextView) actionBar.findViewById(R.id.actionbar_left_text);
        actionbarLeftImg = (ImageView) actionBar.findViewById(R.id.actionbar_left_img);
    }

    @Override
    public void setContentView(int layoutResID) {
        setContentView(View.inflate(this, layoutResID, null));
    }

    /**
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

    /**
     * 显示返回箭头
     */
    protected void showBackArrow() {
        actionbarLeftImg.setVisibility(View.VISIBLE);
        actionbarLeftImg.setOnClickListener(this);
        keepBalance();
    }

    private void keepBalance() {
        if (actionbarLeftImg.getVisibility() == View.VISIBLE
                && actionbarRightImg.getVisibility() == View.GONE) {
            actionbarRightImg.setVisibility(View.INVISIBLE);
        }
        if (actionbarRightImg.getVisibility() == View.VISIBLE
                && actionbarLeftImg.getVisibility() == View.GONE) {
            actionbarLeftImg.setVisibility(View.INVISIBLE);
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
        }
    }

    protected void onTitleClick() {

    }

    protected void onRightAreaClick() {

    }

    protected void onLeftAreaClick() {
        onBackPressed();
    }

    protected void hideActionBar() {
        if (null != actionBar) {
            actionBar.setVisibility(View.GONE);
        }
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
    //==================================actionbar  end==============================================

}
