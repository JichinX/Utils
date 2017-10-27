package me.xujichang.xutil.download;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.webkit.MimeTypeMap;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.MDButton;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.ResourceObserver;
import io.reactivex.schedulers.Schedulers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

import me.xujichang.xutil.tool.StringTool;
import okhttp3.ResponseBody;

/**
 * 下载管理
 * Created by xjc on 2017/5/25.
 */

public class DownLoadTool {

    private static DownLoadTool instance;
    private String fileName;
    private File storeDir;
    private WeakReference<Context> contextWeakReference;
    private boolean showProgress;
    private MaterialDialog progressDialog;
    private CompositeDisposable disposable;
    private String title;
    private int tempProgress;

    private DownLoadTool() {
        disposable = new CompositeDisposable();
    }

    protected DownLoadTool(String pS) {

    }

    private DownLoadTool init(Builder builder) {
        fileName = builder.fileName;
        storeDir = builder.storeFile;
        contextWeakReference = new WeakReference<Context>(builder.context);
        showProgress = builder.showProgress;
        title = builder.title;
        disposable = new CompositeDisposable();
        return this;
    }

    private static DownLoadTool getInstance() {
        if (null == instance) {
            instance = new DownLoadTool();
        }
        instance.clear();
        return instance;
    }

    private void clear() {
        fileName = null;
        storeDir = null;
        contextWeakReference = null;
        showProgress = true;
        title = null;
        tempProgress = -1;
        disposable = null;
        progressDialog = null;
    }

    public void apply(Observable<ResponseBody> observable) {
        apply(observable, null);
    }

    public void apply(Observable<ResponseBody> observable, DownLoadStatusCallback statusCallback) {
        observable.flatMap(new Function<ResponseBody, ObservableSource<Integer>>() {
            @Override
            public ObservableSource<Integer> apply(final ResponseBody body) throws Exception {
                ObservableOnSubscribe<Integer> subscribe = new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                        writeResponseBodyToDisk(body, e);
                    }
                };
                return Observable.create(subscribe);
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(createObservable(statusCallback));
    }

    private ResourceObserver<Integer> createObservable() {

        return createObservable(null);
    }

    private ResourceObserver<Integer> createObservable(
            final DownLoadStatusCallback statusCallback) {
        ResourceObserver<Integer> observer = new ResourceObserver<Integer>() {
            @Override
            public void onNext(Integer integer) {

                if (null != statusCallback) {
                    if (integer > tempProgress) {
                        statusCallback.onNext(integer);
                    }
                }
                if (showProgress) {
                    if (integer > tempProgress) {
                        onDialogProgress(integer);
                    }
                }
                tempProgress = integer;
            }

            @Override
            public void onError(Throwable e) {
                if (null != statusCallback) {
                    statusCallback.onError(e);
                }
                if (showProgress) {
                    onDialogError(StringTool.getErrorMsg(e));
                }
            }

            @Override
            public void onComplete() {
                if (null != statusCallback) {
                    statusCallback.onComplete(fileName);
                }
                if (showProgress) {
                    onDialogComplete();
                }
            }

            @Override
            protected void onStart() {
                if (null != statusCallback) {
                    statusCallback.onStart();
                }
                if (showProgress) {
                    createProgressDialog();
                }
            }
        };
        disposable.add(observer);
        return observer;
    }

    private void onDialogComplete() {
        progressDialog.setContent(fileName + "下载完成！");
        //默认查看文件
        onDefaultComplete();
    }

    private void onDefaultComplete() {
        MDButton positiveButton =
                progressDialog.getActionButton(DialogAction.POSITIVE);
        MDButton negativeButton =
                progressDialog.getActionButton(DialogAction.NEGATIVE);

        if (fileName.endsWith(".apk")) {
            positiveButton.setVisibility(View.VISIBLE);
            positiveButton.setText("安装");
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    installApk();
                }
            });
            negativeButton.setText("取消");
            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog.dismiss();
                }
            });
            return;
        } else {
            positiveButton.setVisibility(View.VISIBLE);
            positiveButton.setText("确定");
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog.dismiss();
                }
            });
            negativeButton.setText("查看");
            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //查看下载的文件
                    if (null == contextWeakReference.get()) {
                        return;
                    }
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setDataAndType(getUri(storeDir), "*/*");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    contextWeakReference.get().startActivity(intent);
                }
            });
        }
    }

    private void installApk() {
        Context context = contextWeakReference.get();
        if (null == context) {
            return;
        }
        File storeFile = new File(storeDir + File.separator + fileName);
        Uri uri = getUri(storeFile);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //适配Android N
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.setDataAndType(uri, MimeTypeMap.getSingleton().getMimeTypeFromExtension("apk"));
        context.startActivity(intent);
        progressDialog.dismiss();
    }

    private void installApk(File file) {
        Uri uri = null;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //适配Android N
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(contextWeakReference.get(),
                    "android.support.v4.content.fileprovider", file);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.setDataAndType(uri, MimeTypeMap.getSingleton().getMimeTypeFromExtension("apk"));
        contextWeakReference.get().startActivity(intent);
        progressDialog.dismiss();
    }

    private void onDialogError(String msg) {
        if (null != progressDialog) {
            progressDialog.setContent(fileName + " 下载失败:" + "error{" + msg + "}");
        }
    }

    /**
     * 设置进度
     */
    private void onDialogProgress(Integer integer) {
        if (null == progressDialog) {
            createProgressDialog();
        }
        progressDialog.setProgress(integer);
    }

    /**
     * 创建Dialog
     */
    private void createProgressDialog() {
        Context context = contextWeakReference.get();
        if (null == context) {
            return;
        }
        progressDialog = new MaterialDialog.Builder(context)
                .title(title)
                .content(fileName + "下载中...")
                .progress(false, 100)
                .cancelable(false)
                .positiveText("确定")
                .negativeText("取消")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (!disposable.isDisposed()) {
                            disposable.dispose();
                        }
                    }
                })
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        dialog = null;
                    }
                })
                .build();
        //默认下载时不显示确定
        progressDialog.getActionButton(DialogAction.POSITIVE).setVisibility(View.GONE);
        progressDialog.show();
    }

    private void writeResponseBodyToDisk(ResponseBody body, ObservableEmitter<Integer> emitter) {
        if (emitter.isDisposed()) {
            return;
        }
        Context context = contextWeakReference.get();
        if (context == null) {
            emitter.onError(new RuntimeException("context is null"));
            return;
        }
        //fileDir
        if (null == storeDir || !storeDir.exists()) {
            storeDir = context.getExternalFilesDir(null);
        }
        try {
            File futureStudioIconFile = new File(storeDir + File.separator + fileName);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[1024];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    emitter.onNext((int) (fileSizeDownloaded * 100 / fileSize));
                }

                outputStream.flush();
                emitter.onComplete();
            } catch (IOException e) {
                if (!emitter.isDisposed()) {
                    emitter.onError(e);
                } else {
                    e.printStackTrace();
                }
                deleteFile();
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            if (!emitter.isDisposed()) {
                emitter.onError(e);
            } else {
                e.printStackTrace();
            }
            deleteFile();
        }
    }

    private void deleteFile() {
        File storeFile = new File(storeDir + File.separator + fileName);
        if (storeFile.exists()) {
            storeFile.delete();
        }
    }

    /**
     * 从Url中取文件名
     */
    private String getReallyFileName(String url) {
        String filename = "";
        int index = url.lastIndexOf("/");
        filename = url.substring(index + 1, url.length());
        index = filename.lastIndexOf(".");
        String extension = filename.substring(index + 1, filename.length());
        if (null == MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)) {
            filename = System.currentTimeMillis() + "";
        }
        return filename;
    }

    public static class Builder {

        private String fileName;
        private Context context;
        private File storeFile;
        private boolean showProgress;
        private String title;

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder withContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder storeDir(File storeFile) {
            this.storeFile = storeFile;
            return this;
        }

        public Builder showProgress(boolean showProgress) {
            this.showProgress = showProgress;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public DownLoadTool build() {
            return getInstance().init(this);
        }
    }

    private Uri getUri(File file) {
        Context context = contextWeakReference.get();
        Uri uri = null;
        //适配Android N
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, "android.support.v4.content.fileprovider",
                    file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    public interface DownLoadStatusCallback {

        void onNext(Integer next);

        void onStart();

        void onError(Throwable throwable);

        void onComplete(String fileName);
    }

    private DownLoadStatusCallback statusCallback;

    public void setStatusCallback(DownLoadStatusCallback statusCallback) {
        this.statusCallback = statusCallback;
    }

    public static class SimpleDownloadStatusCallBack implements DownLoadStatusCallback {


        @Override
        public void onNext(Integer next) {

        }

        @Override
        public void onStart() {

        }

        @Override
        public void onError(Throwable throwable) {

        }

        @Override
        public void onComplete(String fileName) {

        }
    }
}
