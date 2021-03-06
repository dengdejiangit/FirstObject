package com.example.administrator.testproject.update;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.example.administrator.testproject.R;
import com.example.administrator.testproject.XApplication;
import com.example.administrator.testproject.xtimer.IXTimerListener;
import com.example.administrator.testproject.xtimer.XTimer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateDownLoadService extends Service {
    public static final String TAG = "UpdateDownLoadService";
    public static final String ACTION = "me.shenfan.UPDATE_APP";
    public static final String STATUS = "status";
    public static final String PROGRESS = "progress";
    private static final String VALUE_STR_DOWNLOAD_FILE_PATH = "download_file_path";
    public static boolean DEBUG = true;

    //下载大小通知频率
    public static final int UPDATE_NUMBER_SIZE = 1;
    public static final int DEFAULT_RES_ID = -1;

    public static final int UPDATE_PROGRESS_STATUS = 0;
    public static final int UPDATE_ERROR_STATUS = -1;
    public static final int UPDATE_SUCCESS_STATUS = 1;

    private static final String URL = "downloadUrl";
    private static final String ICO_RES_ID = "icoResId";
    private static final String ICO_SMALL_RES_ID = "icoSmallResId";
    private static final String UPDATE_PROGRESS = "updateProgress";
    private static final String STORE_DIR = "storeDir";
    private static final String DOWNLOAD_NOTIFICATION_FLAG = "downloadNotificationFlag";
    private static final String DOWNLOAD_SUCCESS_NOTIFICATION_FLAG = "downloadSuccessNotificationFlag";
    private static final String DOWNLOAD_ERROR_NOTIFICATION_FLAG = "downloadErrorNotificationFlag";
    private static final String IS_SEND_BROADCAST = "isSendBroadcast";

    private String downloadUrl;
    private int updateProgress;   //update notification progress when it add number
    private String storeDir;          //default sdcard/Android/package/update
    private int downloadNotificationFlag;
    private int downloadSuccessNotificationFlag;
    private int downloadErrorNotificationFlag;
    private boolean isSendBroadcast;

    private UpdateProgressListener updateProgressListener;
    public LocalBinder localBinder = new LocalBinder();

    private boolean startDownload;
    private int lastProgressNumber;
    private NotificationCompat.Builder builder;
    private NotificationManager manager;
    private int notifyId;
    private String appName;
    private LocalBroadcastManager localBroadcastManager;
    private Intent localIntent;
    private DownloadApk downloadApkTask;

    public class LocalBinder extends Binder {
        public void setUpdateProgressListener(UpdateProgressListener listener) {
            UpdateDownLoadService.this.setUpdateProgressListener(listener);
        }
    }

    public static void debug() {
        DEBUG = true;
    }

    private Intent installIntent(String path) {
        Uri uri = Uri.fromFile(new File(path));
        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
        return installIntent;
    }

    private void autoInstall(String path) {
        Uri uri = Uri.fromFile(new File(path));
        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
        XApplication.getApplication().startActivity(installIntent);
    }

    private static Intent webLauncher(String downloadUrl) {
        Intent intent;
        if (!TextUtils.isEmpty(downloadUrl)) {
            Uri download = Uri.parse(downloadUrl);
            intent = new Intent(Intent.ACTION_VIEW, download);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            intent = new Intent();
        }
        return intent;
    }

    private static String getSaveFileName(String downloadUrl) {
        if (downloadUrl == null || TextUtils.isEmpty(downloadUrl)) {
            return "noName.apk";
        }
        return downloadUrl.substring(downloadUrl.lastIndexOf("/"));
    }

    private static File getDownloadDir(UpdateDownLoadService service) {
        File downloadDir;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            if (service.storeDir != null) {
                downloadDir = new File(Environment.getExternalStorageDirectory(), service.storeDir);
            } else {
                downloadDir = new File(service.getExternalCacheDir(), "update");
            }
        } else {
            downloadDir = new File(service.getCacheDir(), "update");
        }
        if (!downloadDir.exists()) {
            downloadDir.mkdirs();
        }
        return downloadDir;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!startDownload && intent != null) {
            startDownload = true;
            downloadUrl = intent.getStringExtra(URL);
            int icoResId = intent.getIntExtra(ICO_RES_ID, DEFAULT_RES_ID);
            int icoSmallResId = intent.getIntExtra(ICO_SMALL_RES_ID, DEFAULT_RES_ID);
            storeDir = intent.getStringExtra(STORE_DIR);
            updateProgress = intent.getIntExtra(UPDATE_PROGRESS, UPDATE_NUMBER_SIZE);
            downloadNotificationFlag = intent.getIntExtra(DOWNLOAD_NOTIFICATION_FLAG, 0);
            downloadErrorNotificationFlag = intent.getIntExtra(DOWNLOAD_ERROR_NOTIFICATION_FLAG, 0);
            downloadSuccessNotificationFlag = intent.getIntExtra(DOWNLOAD_SUCCESS_NOTIFICATION_FLAG, 0);
            isSendBroadcast = intent.getBooleanExtra(IS_SEND_BROADCAST, false);

            if (DEBUG) {
                LogUtils.dTag(TAG, "downloadUrl: " + downloadUrl);
                LogUtils.dTag(TAG, "icoResId: " + icoResId);
                LogUtils.dTag(TAG, "icoSmallResId: " + icoSmallResId);
                LogUtils.dTag(TAG, "storeDir: " + storeDir);
                LogUtils.dTag(TAG, "updateProgress: " + updateProgress);
                LogUtils.dTag(TAG, "downloadNotificationFlag: " + downloadNotificationFlag);
                LogUtils.dTag(TAG, "downloadErrorNotificationFlag: " + downloadErrorNotificationFlag);
                LogUtils.dTag(TAG, "downloadSuccessNotificationFlag: " + downloadSuccessNotificationFlag);
                LogUtils.dTag(TAG, "isSendBroadcast: " + isSendBroadcast);
            }

            notifyId = startId;
            buildNotification();
            buildBroadcast();
            downloadApkTask = new DownloadApk(this);
            downloadApkTask.execute(downloadUrl);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    public void setUpdateProgressListener(UpdateProgressListener updateProgressListener) {
        this.updateProgressListener = updateProgressListener;
    }

    @Override
    public void onDestroy() {
        if (downloadApkTask != null) {
            downloadApkTask.cancel(true);
        }

        if (updateProgressListener != null) {
            updateProgressListener = null;
        }
        localIntent = null;
        builder = null;
        super.onDestroy();
    }

    public String getApplicationName() {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.eTag(TAG, e);
        }
        return (String) packageManager.getApplicationLabel(applicationInfo);
    }

    private void buildBroadcast() {
        if (!isSendBroadcast) {
            return;
        }
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localIntent = new Intent(ACTION);
    }

    private void sendLocalBroadcast(int status, int progress) {
        if (!isSendBroadcast || localIntent == null) {
            return;
        }
        localIntent.putExtra(STATUS, status);
        localIntent.putExtra(PROGRESS, progress);
        localBroadcastManager.sendBroadcast(localIntent);
    }

    @SuppressLint("StringFormatInvalid")
    private void buildNotification() {
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(this, null);
        builder.setContentTitle("title")
                .setWhen(System.currentTimeMillis())
                .setProgress(100, 1, false)
                .setSmallIcon(R.mipmap.app_icon)
                .setDefaults(downloadNotificationFlag);

        manager.notify(notifyId, builder.build());
    }

    private void start() {
        builder.setContentTitle(getString(R.string.app_name));
        builder.setContentText(getString(R.string.update_prepare));
        manager.notify(notifyId, builder.build());
        sendLocalBroadcast(UPDATE_PROGRESS_STATUS, 1);
        if (updateProgressListener != null) {
            updateProgressListener.start();
        }
    }

    private void update(int progress) {
        if (progress - lastProgressNumber > updateProgress) {
            lastProgressNumber = progress;
            builder.setProgress(100, progress, false);
            builder.setContentText(getString(R.string.downloading));
            manager.notify(notifyId, builder.build());
            sendLocalBroadcast(UPDATE_PROGRESS_STATUS, progress);
            if (updateProgressListener != null) {
                updateProgressListener.update(progress);
            }
        }
    }

    private void success(String path) {
        builder.setProgress(0, 0, false);
        builder.setContentText(getString(R.string.download_success));

        Intent i = installIntent(path);
        PendingIntent intent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(intent);
        builder.setDefaults(downloadSuccessNotificationFlag);
        Notification n = builder.build();
        n.contentIntent = intent;
        manager.notify(notifyId, n);
        sendLocalBroadcast(UPDATE_SUCCESS_STATUS, 100);

        if (updateProgressListener != null) {
            updateProgressListener.success();
        }
        autoInstall(path);
        stopSelf();
    }

    private void error() {
        Intent i = webLauncher(downloadUrl);
        PendingIntent intent = PendingIntent.getActivity(this, 0, i,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentText(getString(R.string.update_app_error));
        builder.setContentIntent(intent);
        builder.setProgress(0, 0, false);
        builder.setDefaults(downloadErrorNotificationFlag);
        Notification n = builder.build();
        n.contentIntent = intent;
        manager.notify(notifyId, n);
        sendLocalBroadcast(UPDATE_ERROR_STATUS, -1);
        if (updateProgressListener != null) {
            updateProgressListener.error();
        }
        stopSelf();
    }

    private static class DownloadApk extends AsyncTask<String, Integer, String> {

        private WeakReference<UpdateDownLoadService> updateServiceWeakReference;

        DownloadApk(UpdateDownLoadService service) {
            updateServiceWeakReference = new WeakReference<>(service);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            UpdateDownLoadService service = updateServiceWeakReference.get();
            if (service != null) {
                service.start();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            final String downloadUrl = params[0];
            final File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + VALUE_STR_DOWNLOAD_FILE_PATH + "/apks",
                    UpdateDownLoadService.getSaveFileName(downloadUrl));
            if (DEBUG) {
                LogUtils.dTag(TAG, "download url is " + downloadUrl);
                LogUtils.dTag(TAG, "download apk cache at " + file.getAbsolutePath());
            }
            File dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }

            HttpURLConnection httpConnection = null;
            InputStream is = null;
            FileOutputStream fos = null;
            int updateTotalSize = 0;
            URL url;
            try {
                url = new URL(downloadUrl);
                httpConnection = (HttpURLConnection) url.openConnection();
                httpConnection.setConnectTimeout(20000);
                httpConnection.setReadTimeout(20000);

                if (DEBUG) {
                    LogUtils.dTag(TAG, "download status code: " + httpConnection.getResponseCode());
                }

                if (httpConnection.getResponseCode() != 200) {
                    return null;
                }

                updateTotalSize = httpConnection.getContentLength();

                if (file.exists()) {
                    if (updateTotalSize == file.length()) {
                        return file.getAbsolutePath();
                    } else {
                        file.delete();
                    }
                }
                file.createNewFile();
                is = httpConnection.getInputStream();
                fos = new FileOutputStream(file, false);
                byte[] buffer = new byte[4096];

                int readSize = 0;
                int currentSize = 0;

                while ((readSize = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, readSize);
                    currentSize += readSize;
                    publishProgress((currentSize * 100 / updateTotalSize));
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                if (httpConnection != null) {
                    httpConnection.disconnect();
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return file.getAbsolutePath();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (DEBUG) {
                LogUtils.dTag(TAG, "current progress is " + values[0]);
            }
            UpdateDownLoadService service = updateServiceWeakReference.get();
            if (service != null) {
                service.update(values[0]);
            }
        }

        @Override
        protected void onPostExecute(final String s) {
            super.onPostExecute(s);
            final UpdateDownLoadService service = updateServiceWeakReference.get();
            if (service != null) {
                if (!TextUtils.isEmpty(s)) {
                    final XTimer ixTimer = new XTimer();
                    ixTimer.start(150, new IXTimerListener() {
                        @Override
                        public void onTimerComplete() {
                            service.success(s);
                            ixTimer.stop();
                        }

                        @Override
                        public void onTimerRepeatComplete() {

                        }
                    });
                } else {
                    service.error();
                }
            }
        }
    }

    public static class Builder {

        private String downloadUrl;
        private int icoResId = DEFAULT_RES_ID;
        private int icoSmallResId = DEFAULT_RES_ID;
        private int updateProgress = UPDATE_NUMBER_SIZE;   //update notification progress when it add number
        private String storeDir;          //default sdcard/Android/package/update
        private int downloadNotificationFlag;
        private int downloadSuccessNotificationFlag;
        private int downloadErrorNotificationFlag;
        private boolean isSendBroadcast;

        Builder(String downloadUrl) {
            this.downloadUrl = downloadUrl;
        }

        public static Builder create(String downloadUrl) {
            if (downloadUrl == null) {
                throw new NullPointerException("downloadUrl == null");
            }
            return new Builder(downloadUrl);
        }

        public String getDownloadUrl() {
            return downloadUrl;
        }

        public int getIcoResId() {
            return icoResId;
        }

        public Builder setIcoResId(int icoResId) {
            this.icoResId = icoResId;
            return this;
        }

        public int getIcoSmallResId() {
            return icoSmallResId;
        }

        public Builder setIcoSmallResId(int icoSmallResId) {
            this.icoSmallResId = icoSmallResId;
            return this;
        }

        public int getUpdateProgress() {
            return updateProgress;
        }

        public Builder setUpdateProgress(int updateProgress) {
            if (updateProgress < 1) {
                throw new IllegalArgumentException("updateProgress < 1");
            }
            this.updateProgress = updateProgress;
            return this;
        }

        public String getStoreDir() {
            return storeDir;
        }

        public Builder setStoreDir(String storeDir) {
            this.storeDir = storeDir;
            return this;
        }

        public int getDownloadNotificationFlag() {
            return downloadNotificationFlag;
        }

        public Builder setDownloadNotificationFlag(int downloadNotificationFlag) {
            this.downloadNotificationFlag = downloadNotificationFlag;
            return this;
        }

        public int getDownloadSuccessNotificationFlag() {
            return downloadSuccessNotificationFlag;
        }

        public Builder setDownloadSuccessNotificationFlag(int downloadSuccessNotificationFlag) {
            this.downloadSuccessNotificationFlag = downloadSuccessNotificationFlag;
            return this;
        }

        public int getDownloadErrorNotificationFlag() {
            return downloadErrorNotificationFlag;
        }

        public Builder setDownloadErrorNotificationFlag(int downloadErrorNotificationFlag) {
            this.downloadErrorNotificationFlag = downloadErrorNotificationFlag;
            return this;
        }

        public boolean isSendBroadcast() {
            return isSendBroadcast;
        }

        public Builder setIsSendBroadcast(boolean isSendBroadcast) {
            this.isSendBroadcast = isSendBroadcast;
            return this;
        }

        public Builder build(Context context) {
            if (context == null) {
                throw new NullPointerException("context == null");
            }
            Intent intent = new Intent();
            intent.setClass(context, UpdateDownLoadService.class);
            intent.putExtra(URL, downloadUrl);

            if (icoResId == DEFAULT_RES_ID) {
                icoResId = getIcon(context);
            }

            if (icoSmallResId == DEFAULT_RES_ID) {
                icoSmallResId = icoResId;
            }
            intent.putExtra(ICO_RES_ID, icoResId);
            intent.putExtra(STORE_DIR, storeDir);
            intent.putExtra(ICO_SMALL_RES_ID, icoSmallResId);
            intent.putExtra(UPDATE_PROGRESS, updateProgress);
            intent.putExtra(DOWNLOAD_NOTIFICATION_FLAG, downloadNotificationFlag);
            intent.putExtra(DOWNLOAD_SUCCESS_NOTIFICATION_FLAG, downloadSuccessNotificationFlag);
            intent.putExtra(DOWNLOAD_ERROR_NOTIFICATION_FLAG, downloadErrorNotificationFlag);
            intent.putExtra(IS_SEND_BROADCAST, isSendBroadcast);
            context.startService(intent);
            return this;
        }

        private int getIcon(Context context) {
            final PackageManager packageManager = context.getPackageManager();
            ApplicationInfo appInfo = null;
            try {
                appInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if (appInfo != null) {
                return appInfo.icon;
            }
            return 0;
        }
    }

}
