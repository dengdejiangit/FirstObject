package com.example.com_gift.popview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.example.com_gift.R;
import com.example.com_gift.adapter.TypeSelectPopuAdapter;
import com.example.com_gift.bean.TypeSelect;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.WINDOW_SERVICE;

public class CustomOperationPopWindow extends PopupWindow {
    private Context context;
    private View conentView;
    private View backgroundView;
    private Animation anim_backgroundView;
    private MyListView listView;
    private TypeSelectPopuAdapter selectAdapter;
    ImageView arrow_up, arrow_down;
    List<TypeSelect> typeSelectlist = new ArrayList<>();
    int[] location = new int[2];
    private OnItemListener onItemListener;
    private AdapterView.OnItemClickListener onItemClickListener;
    private int height;
    private int width;


    public interface OnItemListener {
        public void OnItemListener(int position, TypeSelect typeSelect);
    }

    ;

    public void setOnItemMyListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    public CustomOperationPopWindow(Context context) {
        this.context = context;
        initView();
    }

    public CustomOperationPopWindow(Context context, List<TypeSelect> typeSelectlist) {
        this.context = context;
        this.typeSelectlist = typeSelectlist;
        initView();
    }


    private void initView() {
        this.anim_backgroundView = AnimationUtils.loadAnimation(context, R.anim.alpha_show_anim);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.conentView = inflater.inflate(R.layout.pop_view, null);
        // ??????SelectPicPopupWindow???View
        this.setContentView(conentView);
        // ??????SelectPicPopupWindow??????????????????
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // ??????SelectPicPopupWindow??????????????????
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // ??????SelectPicPopupWindow?????????????????????
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // ????????????
        this.update();
        // ???????????????ColorDrawable??????????????????
        ColorDrawable dw = new ColorDrawable(0000000000);
        // ???back??????????????????????????????,???????????????????????????OnDismisslistener ????????????????????????????????????
        this.setBackgroundDrawable(dw);
        // ??????SelectPicPopupWindow????????????????????????s
        this.setAnimationStyle(R.style.open_pop_jin);
        this.listView = (MyListView) conentView.findViewById(R.id.lv_list);
        this.arrow_up = (ImageView) conentView.findViewById(R.id.arrow_up);
        this.arrow_down = (ImageView) conentView.findViewById(R.id.arrow_down);
        //???????????????
//        TypeSelectPopuAdapter typeSelectPopuAdapter = new TypeSelectPopuAdapter(this, typeSelectlist);
//        listView.setAdapter((ListAdapter) typeSelectPopuAdapter);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isShowing()) {
                    dismiss();
                }
                onItemListener.OnItemListener(position, typeSelectlist.get(position));
            }
        });
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                if (backgroundView != null) {
                    backgroundView.setVisibility(View.GONE);
                }
            }
        });
    }

    //????????????
    public void setDataSource(List<TypeSelect> typeSelectlist) {
        this.typeSelectlist = typeSelectlist;
        this.selectAdapter.notify();
    }

    /**
     * ????????????????????? ??????popupWindow
     *
     * @param
     */
    public void showPopupWindow(View v) {
        v.getLocationOnScreen(location); //???????????????????????????
        //????????????????????????
        conentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        WindowManager wm = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();

        if (location[1] > width / 2 + 100) { //MainApplication.SCREEN_H ??????????????????????????????????????????
            this.setAnimationStyle(R.style.open_pop_jin);
            arrow_up.setVisibility(View.GONE);
            arrow_down.setVisibility(View.VISIBLE);
            this.showAtLocation(v, Gravity.NO_GRAVITY, (location[0]), location[1] - conentView.getMeasuredHeight());
        } else {
            this.setAnimationStyle(R.style.open_pop_tui);
            arrow_up.setVisibility(View.VISIBLE);
            arrow_down.setVisibility(View.GONE);
            this.showAsDropDown(v, 0, 0);
        }
    }

    /**
     * ????????????????????? ??????popupWindow
     *
     * @param
     */
    public void showPopupWindow(View v, View backgroundView) {
        this.backgroundView = backgroundView;
        v.getLocationOnScreen(location); //???????????????????????????
        //????????????????????????
        conentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        backgroundView.setVisibility(View.VISIBLE);
        //???view????????????
        backgroundView.startAnimation(anim_backgroundView);
        if (location[1] > height / 2 + 100) { //???????????????y???????????????????????????????????????????????????
            this.setAnimationStyle(R.style.open_pop_jin);
            arrow_up.setVisibility(View.GONE);
            arrow_down.setVisibility(View.VISIBLE);
            this.showAtLocation(v, Gravity.NO_GRAVITY, (location[0]), location[1] - conentView.getMeasuredHeight()); //???????????????????????????
        } else {
            this.setAnimationStyle(R.style.open_pop_tui); //??????????????????
            arrow_up.setVisibility(View.VISIBLE);
            arrow_down.setVisibility(View.GONE);
            this.showAsDropDown(v, 0, 0);  //???????????????????????????
        }
    }

    /**
     * ??????popupWindow ????????????????????????????????????
     *
     * @param
     */
    public void showPopupWindow(View v, View backgroundView, int hight) {
        this.backgroundView = backgroundView;
        v.getLocationOnScreen(location);
        //????????????????????????
        conentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        backgroundView.setVisibility(View.VISIBLE);
        //???view????????????
        backgroundView.startAnimation(anim_backgroundView);
        if (location[1] > width / 2 + 100) {
            this.setAnimationStyle(R.style.open_pop_tui);
            arrow_up.setVisibility(View.GONE);
            arrow_down.setVisibility(View.VISIBLE);
            this.showAtLocation(v, Gravity.NO_GRAVITY, (location[0]), location[1] - conentView.getMeasuredHeight() - hight);
        } else {
            this.setAnimationStyle(R.style.open_pop_jin);
            arrow_up.setVisibility(View.VISIBLE);
            arrow_down.setVisibility(View.GONE);
            this.showAsDropDown(v, 0, 0);
        }
    }


}
