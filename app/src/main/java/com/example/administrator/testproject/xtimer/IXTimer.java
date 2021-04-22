package com.example.administrator.testproject.xtimer;

public interface IXTimer{
    boolean start(long lDelayTime, IXTimerListener iXTimerListener);

    boolean startRepeat(long lDelayTime, long lSpacingTime, IXTimerListener iXTimerListener);

    void stop();
}
