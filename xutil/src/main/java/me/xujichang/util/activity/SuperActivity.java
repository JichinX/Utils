package me.xujichang.util.activity;

import android.app.ActivityManager;
import android.arch.lifecycle.Lifecycle.State;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import me.xujichang.util.R;
import me.xujichang.util.base.SuperPresenter;
import me.xujichang.util.bean.AppInfo;
import me.xujichang.util.tool.LogTool;
import me.xujichang.util.tool.SnackBarTool;

/**
 * SuperActivity
 * <p>
 * 包含 Loading Dialog 权限申请 ActionBar等
 *
 * @author xjc
 *         Created by xjc on 2017/5/23.
 */
public abstract class SuperActivity extends SuperActionBarActivity {
    /**
     * 错误提示Dialog
     */
    private MaterialDialog errorDialog;
    /**
     * 警告提示
     */
    private MaterialDialog warningDialog;
    /**
     * 加载进度
     */
    private MaterialDialog progressDialog;
    /**
     * Loading的请求数量
     */
    private static int requestNum = 0;
    /**
     * 时间
     */
    private long startTime = 0;
    /**
     * 网络请求 Presenter
     */
    private SuperPresenter cachePresenter;

    public SuperPresenter getCachePresenter() {
        return cachePresenter;
    }

    protected void registerPresenter(SuperPresenter presenter) {
        this.cachePresenter = presenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //调用Destroy方法 停止网络请求 或返回数据
        if (null != cachePresenter) {
            cachePresenter.destroy();
        }
    }

    @Override
    public void onBackPressed() {
        if (hideSoftKeyBoard() || hideKeyBoardForDialog()) {
            //首先关闭 输入法
            return;
        }
        //判断是否是首页（Activity栈底）
        if (isAppBaseActivity()) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - startTime > getActivityExitDuration()) {
                showToastWithAction("再次点击将退出程序", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        System.gc();
                    }
                }, "退出");
                startTime = currentTime;
            } else {
                finish();
                System.gc();
            }
            return;
        }
        super.onBackPressed();
    }

    private boolean isAppBaseActivity() {
        String className = getClass().getName();
        return className.equals(getAppBaseActivity());
    }

    private String getAppBaseActivity() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        if (null != activityManager) {
            return activityManager.getRunningTasks(1).get(0).baseActivity.getClassName();
        }
        return null;
    }

    private boolean isAppTopActivity() {
        String className = getClass().getSimpleName();
        return className.equals(getAppBaseActivity());
    }

    private String getAppTopActivity() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        if (null != activityManager) {
            return activityManager.getRunningTasks(1).get(0).topActivity.getShortClassName();
        }
        return null;
    }

    /**
     * 关闭Dialog上的输入法
     * TODO 暂未找到合适的方法
     *
     * @return
     */
    public boolean hideKeyBoardForDialog() {
        return false;
    }

    /**
     * 关闭输入法
     *
     * @return true 表示  有输入法打开 并成功关闭
     * false 表示 没有打开的输入法
     */
    public boolean hideSoftKeyBoard() {
        boolean closed = false;
        InputMethodManager methodManager = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (null != methodManager) {
            closed = methodManager.hideSoftInputFromWindow(
                    getWindow().getDecorView().getWindowToken(), 0);
        }
        return closed;
    }

    /**
     * 开启Loading
     *
     * @param msg
     */
    public void startLoading(String msg) {
        if (TextUtils.isEmpty(msg)) {
            msg = "加载中...";
        }
        if (getLifecycle().getCurrentState() == State.DESTROYED) {
            return;
        }
        if (requestNum == 0) {
            progressDialog = new MaterialDialog
                    .Builder(this)
                    .content(msg)
                    .cancelable(false)
                    .progress(true, 100)
                    .build();
            progressDialog.show();
        } else {
            //直接更改提示信息
            progressDialog.setContent(msg);
        }
        requestNum++;
        LogTool.d("==============loading start======:" + requestNum);
    }

    /**
     * 停止Loading
     */
    public void stopLoading() {
        LogTool.d("==============loading stop 1. ======:" + requestNum);
        if (requestNum <= 0) {
            requestNum = 0;
            return;
        }
        requestNum--;
        LogTool.d("==============loading stop 2. ======:" + requestNum);
        if (requestNum > 0) {
            return;
        }
        LogTool.d("==============loading stop 3. ======:end");
        //requestNum ==0
        if (null != progressDialog) {
            progressDialog.dismiss();
            progressDialog = null;
            requestNum = 0;
        }
    }

    public void showToast(String msg) {
        showToast(getSuperRoot(), msg);
    }

    public void showToast(View view, String msg) {
        showToastWithAction(view, msg, null);
    }

    public void showToast(String msg, View.OnClickListener listener) {
        showToastWithAction(getSuperRoot(), msg, listener);
    }

    public void showToastWithAction(View view, String msg, View.OnClickListener listener) {
        showToastWithAction(view, msg, listener, "action");
    }

    public void showToastWithAction(String msg, View.OnClickListener listener, String actionstr) {
        showToastWithAction(getSuperRoot(), msg, listener, actionstr);
    }

    public void showToastWithAction(View view, String msg, View.OnClickListener listener,
                                    String actionstr) {
        showToastWithAction(view, msg, listener, actionstr, true);
    }

    public void showToastWithAction(String msg, View.OnClickListener listener, String actionstr,
                                    boolean cancel) {
        showToastWithAction(getSuperRoot(), msg, listener, actionstr, cancel);
    }

    public void showToastWithAction(View view, String msg, View.OnClickListener listener,
                                    String actionstr, boolean cancel) {
        if (!cancel) {
            msg = msg + "(右滑删除此消息)";
        }
        SnackBarTool
                .getInstance()
                .showToastWithAction(view, msg)
                .action(actionstr, listener)
                .cancel(cancel)
                .show();
    }

    public void loadingError(String msg) {
        showErrorDialog(msg);
    }


    public void loadingComplete() {
        stopLoading();
    }

    public void finishActivity() {
        finish();
    }

    public void startAnotherActivity(String activityName) {
        //根据name获取Class
        Intent intent = new Intent();
        intent.setClassName(this, getPackageName() + "." + activityName);
        List<ResolveInfo> resolveInfoList = getPackageManager().queryIntentActivities(intent, 0);
        if (resolveInfoList.size() > 0) {
            startActivity(intent);
        } else {
            showErrorDialog("目标 " + activityName + "不存在", null);
        }
    }

    //=====================================dialog===================================================
    public void showWarningDialog(@NonNull String msg) {
        showWarningDialog(msg, null);
    }

    public void showWarningDialog(@NonNull String msg,
                                  MaterialDialog.SingleButtonCallback callback) {
        if (isFinishing()) {
            return;
        }
        if (null != warningDialog) {
            String temp = warningDialog.getContentView().getText().toString();
            warningDialog.setContent(new StringBuilder(temp).append(",").append(msg));
            return;
        }
        warningDialog = new MaterialDialog.Builder(this)
                .title("警告")
                .iconRes(R.drawable.ic_warning_yellow)
                .content(msg)
                .positiveText("确定")
                .negativeText("取消")
                .onPositive(callback)
                .onNegative(callback)
                .cancelable(false)
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        warningDialog = null;
                    }
                })
                .build();
        warningDialog.show();

    }

    @Deprecated
    protected void createErrorDialog(String msg) {
        showErrorDialog(msg, null);
    }

    @Deprecated
    protected void createErrorDialog(String msg, MaterialDialog.SingleButtonCallback callback) {
        showErrorDialog(msg, callback);
    }

    protected void showErrorDialog(String msg) {
        showErrorDialog(msg, null);
    }

    protected void showErrorDialog(String msg, MaterialDialog.SingleButtonCallback callback) {
        stopLoading();
        if (isFinishing()) {
            return;
        }
        if (TextUtils.isEmpty(msg)) {
            msg = "此错误未知";
        }
        //错误叠加
        if (null != errorDialog) {
            String temp = errorDialog.getContentView().getText().toString();
            errorDialog.setContent(new StringBuilder(temp).append(",").append(msg));
            return;
        }
        errorDialog = new MaterialDialog
                .Builder(this)
                .iconRes(R.drawable.ic_error_red)
                .title("发生错误")
                .content(msg)
                .positiveText("确定")
                .onPositive(callback)
                .cancelable(false)
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        errorDialog = null;
                    }
                })
                .build();
        errorDialog.show();
    }

    //=======================================ActionBar==============================================

    public void doFullScreen() {
        //取消标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void toActivity(Class c) {
        toActivity(c, null);
    }

    public void toActivity(Class c, Map<String, String> parmas) {
        Intent intent = new Intent(this, c);
        if (null != parmas) {
            for (Map.Entry<String, String> entry : parmas.entrySet()) {
                intent.putExtra(entry.getKey(), entry.getValue());
            }
        }
        startActivity(intent);
    }

    public void toActivityForResult(Class c, Map<String, String> parmas, int code) {
        Intent intent = new Intent(this, c);
        if (null != parmas) {
            for (Map.Entry<String, String> entry : parmas.entrySet()) {
                intent.putExtra(entry.getKey(), entry.getValue());
            }
        }
        startActivityForResult(intent, code);
    }

    public void toActivityForResult(Class c, int code) {
        startActivityForResult(new Intent(this, c), code);
    }

    protected void showUpdateDialog(AppInfo appInfo, MaterialDialog.SingleButtonCallback callback) {
        showUpdateDialog(appInfo, false, callback);
    }

    protected void showUpdateDialog(AppInfo appInfo, boolean must,
                                    MaterialDialog.SingleButtonCallback callback) {
        MaterialDialog dialog = new MaterialDialog
                .Builder(this)
                .title(appInfo.getAppName() + "可更新")
                .content(appInfo.getContent())
                .autoDismiss(false)
                .cancelable(false)
                .positiveText("更新")
                .onPositive(callback)
                .negativeText("取消")
                .onNegative(callback)
                .build();
        if (must) {
            //隐藏掉取消按钮 强制更新
            dialog.getActionButton(DialogAction.NEGATIVE).setVisibility(View.GONE);
        }
        dialog.show();

    }

    /**
     * 检测权限
     */
    protected String[] checkPermissions(String[] permissions) {
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(permissions));
        Iterator<String> iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            if (checkPermission(iterator.next())) {
                iterator.remove();
            }
        }
        return arrayList.toArray(new String[]{});
    }

    /**
     * 检测权限是否开启
     *
     * @param permission 需要检测的权限
     * @return 返回结果
     */
    protected boolean checkPermission(String permission) {
        return ActivityCompat.checkSelfPermission(this, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission(String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        onRequestPermissionCallBack(requestCode, grantResults);
    }

    protected void onRequestPermissionCallBack(int requestCode, int[] grantResults) {

    }

    /**
     * 首页双击退出的判断间隔
     *
     * @return 间隔时间段
     */
    protected abstract long getActivityExitDuration();

    /**
     * 首页 类名字
     *
     * @return 类的名称
     */
    protected abstract String getMainActivityName();
}