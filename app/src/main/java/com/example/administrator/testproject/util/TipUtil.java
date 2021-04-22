package com.example.administrator.testproject.util;

import android.app.Service;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Vibrator;

import com.example.administrator.testproject.R;

/**
 * Create  by  User:WS  Data:2019/7/2
 */

public class TipUtil {
    private static Vibrator vibrator;
    private static MediaPlayer mediaPlayer;

    public static void vibrate(Context context, long milliSeconds) {
        if (vibrator == null) {
            vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        }
        if (vibrator != null) {
            vibrator.vibrate(milliSeconds);
        }
    }

    //自定义震动（模式）周期 long[] pattern={100,400,100,400}//停止 开启 停止 开启
    public static void vibrate(Context context, long[] pattern, boolean isRepeat) {
        if (vibrator == null) {
            vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        }
        if (vibrator != null) {
            vibrator.vibrate(pattern, isRepeat ? 1 : -1);
        }
    }

    public static void stopAndDestroy() {
        if (vibrator != null) {
            vibrator.cancel();
            vibrator = null;
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();//停止
            mediaPlayer.release();//释放资源
            mediaPlayer = null;
        }
    }

    public static void playVoid(Context context) {
        try {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            }
            mediaPlayer.reset();
            mediaPlayer.setLooping(true);//重复播放
            Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.bo);//raw文件夹下的yes音频文件，使用的时候要注意修改
            mediaPlayer.setDataSource(context, uri);//设置文件员
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
            mediaPlayer.prepare();//添加缓存
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();//开始播放
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
