package me.xujichang.util.tool;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import me.xujichang.util.R;


/**
 * Created by xjc on 2017/6/5.
 */

public class SnackBarTool {
    private Snackbar snackbar;
    private static SnackBarTool instance;

    protected SnackBarTool(String pS) {

    }

    public static SnackBarTool getInstance() {
        if (null == instance) {
            instance = new SnackBarTool();
        }
        return instance;
    }

    private SnackBarTool() {
    }

    public void showToast(View view, String msg) {
        snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
        initDefaultSnackBar(snackbar);
        snackbar.show();
    }

    private void initDefaultSnackBar(Snackbar snackbar) {
        Context context = snackbar.getContext();
        View view = snackbar.getView();
        TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        view.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.WHITE);
    }

    public void showToastWithAction(View view, String msg, View.OnClickListener listener) {
        snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_INDEFINITE);
        initDefaultSnackBar(snackbar);
        snackbar.setAction("Action", listener);
        snackbar.show();
    }

    public SnackBarTool showToastWithAction(View view, String msg) {
        snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
        initDefaultSnackBar(snackbar);
        return this;
    }

    public SnackBarTool action(String action, View.OnClickListener listener) {
        snackbar.setAction(action, listener);
        return this;
    }

    public void show() {
        snackbar.show();
    }

    public SnackBarTool cancel(boolean cancel) {
        snackbar.setDuration(cancel ? BaseTransientBottomBar.LENGTH_SHORT : BaseTransientBottomBar.LENGTH_INDEFINITE);
        return this;
    }
}
