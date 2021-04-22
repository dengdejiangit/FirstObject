package com.example.administrator.testproject.update;

/**
 * Created by wy on 2016/6/30.
 *
 * bindService call back
 */
public interface UpdateProgressListener {
    /**
     * download start
     */
    public void start();

    /**
     * update download progress
     * @param progress
     */
    public void update(int progress);

    /**
     * download success
     */
    public void success();

    /**
     * download error
     */
    public void error();
}
