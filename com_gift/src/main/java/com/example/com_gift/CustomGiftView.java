package com.example.com_gift;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CustomGiftView extends LinearLayout {
    private Timer timer;
    private List<View> giftViewCollection = new ArrayList<>();
    public CustomGiftView(Context context) {
        super(context,null);
    }

    public CustomGiftView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs,0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomGiftView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr,0);
    }

    /**
     *<br> Description: todo(这里用一句话描述这个方法的作用)
     *<br> Author:   yangyinglong
     *<br> Date:    2017/7/11 17:40
     */
    public void pause() {
        if (null != timer) {
            timer.cancel();
        }
    }
    public void cancel() {
        if (null != timer) {
            timer.cancel();
        }
    }
    public void resume() {
        clearTiming();
    }

    private void clearTiming() {

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                int count = CustomGiftView.this.getChildCount();
                for (int i = 0; i < count; i++) {
                    View view = CustomGiftView.this.getChildAt(i);
//                    CustomRoundView crvheadimage = (CustomRoundView) view.findViewById(R.id.crvheadimage);
//                    long nowtime = System.currentTimeMillis();
//                    long upTime = (Long) crvheadimage.getTag();
//                    if ((nowtime - upTime) >= 3000) {
//                        final int j = i;
//                        post(new Runnable() {
//                            @Override
//                            public void run() {
//                                CustomGiftView.this.removeViewAt(j);
//                            }
//                        });
////            removeGiftView(i);
//                        return;
//                    }
                }
            }
        };
        if (null != timer) {
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(task, 0, 100);

    }

    /**
     * 添加礼物view,(考虑垃圾回收)
     */
    private View addGiftView() {
        View view = null;
        if (giftViewCollection.size() <= 0) {
            /*如果垃圾回收中没有view,则生成一个*/
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_gift, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.topMargin = 10;
            view.setLayoutParams(lp);
            this.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View view) { }
                //复用Item，当一个View移除时将它放到池内
                @Override
                public void onViewDetachedFromWindow(View view) {
                    if (giftViewCollection.size() < 5) {
                        giftViewCollection.add(view);
                    }
                }
            });
        } else {
            //如果Item池内有缓存的view，将它取出来，并从池中删除
            view = giftViewCollection.get(0);
            giftViewCollection.remove(view);
        }
        return view;
    }


    /**
     *<br> Description: todo(这里用一句话描述这个方法的作用)
     *<br> Author:   yangyinglong
     *<br> Date:    2017/7/11 16:54
     * @param tag
     */
//    public void showGift(String tag) {
//        View giftView = this.findViewWithTag(tag);
//        if (giftView == null) {/*该用户不在礼物显示列表*/
//            giftView = addGiftView();/*获取礼物的View的布局*/
//            giftView.setTag(tag);/*设置view标识*/
//            CustomRoundView crvheadimage = (CustomRoundView) giftView.findViewById(R.id.crvheadimage);
//            final MagicTextView giftNum = (MagicTextView) giftView.findViewById(R.id.giftNum);/*找到数量控件*/
//            TextView sender = (TextView) giftView.findViewById(R.id.sender);
//            sender.setText(tag);
//            giftNum.setText("x1");/*设置礼物数量*/
//            crvheadimage.setTag(System.currentTimeMillis());/*设置时间标记*/
//            giftNum.setTag(1);/*给数量控件设置标记*/
//            this.addView(giftView,0);/*将礼物的View添加到礼物的ViewGroup中*/
////      llgiftcontent.invalidate();/*刷新该view*/
//            TranslateAnimation inAnim = (TranslateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.gift_in);
//            giftView.startAnimation(inAnim);/*开始执行显示礼物的动画*/
//            inAnim.setAnimationListener(new Animation.AnimationListener() {/*显示动画的监听*/
//                @Override
//                public void onAnimationStart(Animation animation) { }
//                @Override
//                public void onAnimationEnd(Animation animation) {
//                    //注释调，第一次添加没动画
////          giftNumAnim.start(giftNum);
//                    Log.d("gao","" + CustomGiftView.this.getHeight());
//                }
//                @Override
//                public void onAnimationRepeat(Animation animation) { }
//            });
//        } else {/*该用户在礼物显示列表*/
//            for (int i = 0;i < CustomGiftView.this.getChildCount();i ++) {
//                if (giftView.equals(CustomGiftView.this.getChildAt(i))) {
//                    if (i >= 3) {
//                        CustomGiftView.this.removeView(giftView);
//                    }
//                }
//            }
////            llgiftcontent.addView(giftView,0);
//            CustomRoundView crvheadimage = (CustomRoundView) giftView.findViewById(R.id.crvheadimage);/*找到头像控件*/
//            MagicTextView giftNum = (MagicTextView) giftView.findViewById(R.id.giftNum);/*找到数量控件*/
//            int showNum = (Integer) giftNum.getTag() + 1;
//            giftNum.setText("x"+showNum);
//            giftNum.setTag(showNum);
//            crvheadimage.setTag(System.currentTimeMillis());
//            new NumAnim().start(giftNum);
//        }
//    }



    /**
     * 数字放大动画
     */
    public static class NumAnim {
        private Animator lastAnimator = null;
        public void start(View view) {
            if (lastAnimator != null) {
                lastAnimator.removeAllListeners();
                lastAnimator.end();
                lastAnimator.cancel();
            }
            ObjectAnimator anim1 = ObjectAnimator.ofFloat(view, "scaleX",0.7f, 1.5f,1f);
            ObjectAnimator anim2 = ObjectAnimator.ofFloat(view, "scaleY",0.7f, 1.5f,1f);
            AnimatorSet animSet = new AnimatorSet();
            lastAnimator = animSet;
            animSet.setDuration(500);
            animSet.setInterpolator(new OvershootInterpolator());
            animSet.playTogether(anim1, anim2);
            animSet.start();
        }
    }


    public static class GiftInfo {
        private String senderFace;
        private String senderNickName;
        private String giftUrl;
        private int giftID;
        public String getSenderFace() {
            return senderFace;
        }
        public void setSenderFace(String senderFace) {
            this.senderFace = senderFace;
        }
        public String getSenderNickName() {
            return senderNickName;
        }
        public void setSenderNickName(String senderNickName) {
            this.senderNickName = senderNickName;
        }
        public String getGiftUrl() {
            return giftUrl;
        }
        public void setGiftUrl(String giftUrl) {
            this.giftUrl = giftUrl;
        }
        public int getGiftID() {
            return giftID;
        }
        public void setGiftID(int giftID) {
            this.giftID = giftID;
        }
    }


}
