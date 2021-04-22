package com.example.administrator.testproject.code.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.example.administrator.testproject.R;
import com.example.administrator.testproject.XApplication;
import com.example.administrator.testproject.base.BaseActivity;
import com.example.administrator.testproject.util.ProgressDialogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebViewActivity extends BaseActivity {
    private static final String APP_CACHE_DIRNAME = "";
    @BindView(R.id.bt_to_blankj)
    Button mBtToBlankj;
    @BindView(R.id.bt_to_AbrahamCaiJin)
    Button mBtToAbrahamCaiJin;
    @BindView(R.id.bt_to_GrenderG)
    Button mBtToGrenderG;
    @BindView(R.id.bt_to_litesuits)
    Button mBtToLitesuits;
    @BindView(R.id.bt_list)
    LinearLayout mBtList;
    private WebView webView;

    public static void launch(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, WebViewActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        FrameLayout layout = findViewById(R.id.fl_web_view);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        webView = new WebView(XApplication.getContext());
        webView.setLayoutParams(params);
        layout.addView(webView);

//        webView.loadUrl("https://blog.csdn.net/u012611878/article/details/78838464");
        webView.loadUrl("https://github.com/afkT/DevUtils/blob/master/DevLibUtils/README.md");

        webView.setWebViewClient(new WebViewClient() {

            //步骤3. 复写shouldOverrideUrlLoading()方法，使得打开网页时不调用系统浏览器， 而是在本WebView中显示
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //设定加载开始的操作
                ProgressDialogUtils.showDialog(WebViewActivity.this,"请等待",true);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //设定加载结束的操作
                ProgressDialogUtils.dismissDialog();
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                //设定加载资源的操作
            }

            //步骤1：写一个html文件（error_handle.html），用于出错时展示给用户看的提示页面
            //步骤2：将该html文件放置到代码根目录的assets文件夹下
            //步骤3：复写WebViewClient的onRecievedError方法
            //该方法传回了错误码，根据错误类型可以进行不同的错误分类处理
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if (errorCode == 404) {
                    view.loadUrl("file:///android_assets/error_handle.html");
                }
            }

            //webView默认是不处理https请求的，页面显示空白，需要进行如下设置：
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();    //表示等待证书响应
                // handler.cancel();      //表示挂起连接，为默认方式
                // handler.handleMessage(null);    //可做其他处理
            }
        });

    }

    @OnClick({R.id.bt_to_blankj, R.id.bt_to_AbrahamCaiJin, R.id.bt_to_GrenderG, R.id.bt_to_litesuits})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_to_blankj:
                webView.loadUrl("https://github.com/Blankj/AndroidUtilCode/blob/master/utilcode/README-CN.md");
                break;
            case R.id.bt_to_AbrahamCaiJin:
                webView.loadUrl("https://github.com/AbrahamCaiJin/CommonUtilLibrary/blob/master/README.md");
                break;
            case R.id.bt_to_GrenderG:
                webView.loadUrl("https://github.com/GrenderG/Toasty/blob/master/README.md");
                break;
            case R.id.bt_to_litesuits:
                webView.loadUrl("https://github.com/litesuits/android-common/blob/master/README.md");
                break;
        }
    }

    private void method() {
        //方式1. 加载一个网页：
        webView.loadUrl("http://www.google.com/");

        //方式2：加载apk包中的html页面
        webView.loadUrl("file:///android_asset/test.html");

        //方式3：加载手机本地的html页面
        webView.loadUrl("content://com.android.htmlfileprovider/sdcard/test.html");

        // 方式4： 加载 HTML 页面的一小段内容
        webView.loadData("data", "mimeType", "encoding");
        // 参数说明：
        // 参数1：需要截取展示的内容
        // 内容里不能出现 ’#’, ‘%’, ‘\’ , ‘?’ 这四个字符，若出现了需用 %23, %25, %27, %3f 对应来替代，否则会出现异常
        // 参数2：展示内容的类型
        // 参数3：字节码

        //激活WebView为活跃状态，能正常执行网页的响应
        webView.onResume();

        //当页面被失去焦点被切换到后台不可见状态，需要执行onPause
        //通过onPause动作通知内核暂停所有的动作，比如DOM的解析、plugin的执行、JavaScript执行。
        webView.onPause();

        //当应用程序(存在webview)被切换到后台时，这个方法不仅仅针对当前的webview而是全局的全应用程序的webview
        //它会暂停所有webview的layout，parsing，javascripttimer。降低CPU功耗。
        webView.pauseTimers();
        //恢复pauseTimers状态
        webView.resumeTimers();

        //销毁Webview
        //在关闭了Activity时，如果Webview的音乐或视频，还在播放。就必须销毁Webview
        //但是注意：webview调用destory时,webview仍绑定在Activity上
        //这是由于自定义webview构建时传入了该Activity的context对象
        //因此需要先从父容器中移除webview,然后再销毁webview:
        //rootLayout.removeView(webView);
        webView.destroy();

        //是否可以后退
        webView.canGoBack();
        //后退网页
        webView.goBack();

        //是否可以前进
        webView.canGoForward();
        //前进网页
        webView.goForward();

        //以当前的index为起始点前进或者后退到历史记录中指定的steps
        //如果steps为负数则为后退，正数则为前进
        webView.goBackOrForward(0);

        //清除网页访问留下的缓存
        //由于内核缓存是全局的因此这个方法不仅仅针对webview而是针对整个应用程序.
        webView.clearCache(true);

        //清除当前webview访问的历史记录
        //只会webview访问历史记录里的所有记录除了当前访问记录
        webView.clearHistory();

        //这个api仅仅清除自动完成填充的表单数据，并不会清除WebView存储到本地的数据
        webView.clearFormData();

        //声明WebSettings子类
        WebSettings webSettings = webView.getSettings();

        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        // 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
        // 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        //优先使用缓存:
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //缓存模式如下：
        //LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
        //LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
        //LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        //LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。

        //不使用缓存:
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);


        if (NetworkUtils.isConnected()) {
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//根据cache-control决定是否从网络上取数据。
        } else {
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//没网，则从本地获取，即离线加载
        }

        webSettings.setDomStorageEnabled(true); // 开启 DOM storage API 功能
        webSettings.setDatabaseEnabled(true);   //开启 database storage API 功能
        webSettings.setAppCacheEnabled(true);//开启 Application Caches 功能

        String cacheDirPath = getFilesDir().getAbsolutePath() + APP_CACHE_DIRNAME;
        webSettings.setAppCachePath(cacheDirPath); //设置  Application Caches 缓存目录

        webView.setWebChromeClient(new WebChromeClient() {

            //获得网页的加载进度并显示
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    String progress = newProgress + "%";
                    LogUtils.e(progress);
                }
            }

            //每个网页的页面都有一个标题，比如www.baidu.com这个页面的标题即“百度一下，你就知道”，对当前webview正在加载的页面的title进行设置
            @Override
            public void onReceivedTitle(WebView view, String title) {
                LogUtils.e(title);
            }

            //支持javascript的警告框
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                new AlertDialog.Builder(WebViewActivity.this)
                        .setTitle("JsAlert")
                        .setMessage(message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        })
                        .setCancelable(false)
                        .show();
                return true;
            }

            //支持javascript的确认框
            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                new AlertDialog.Builder(WebViewActivity.this)
                        .setTitle("JsConfirm")
                        .setMessage(message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.cancel();
                            }
                        })
                        .setCancelable(false)
                        .show();
                // 返回布尔值：判断点击时确认还是取消
                // true表示点击了确认；false表示点击了取消；
                return true;
            }

            //支持javascript输入框
            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
                final EditText et = new EditText(WebViewActivity.this);
                et.setText(defaultValue);
                new AlertDialog.Builder(WebViewActivity.this)
                        .setTitle(message)
                        .setView(et)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm(et.getText().toString());
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.cancel();
                            }
                        })
                        .setCancelable(false)
                        .show();
                return true;
            }
        });
    }

    //处理网页中的回退
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            ViewParent parent = webView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(webView);
            }
            webView.stopLoading();
            webView.getSettings().setJavaScriptEnabled(false);
            webView.clearHistory();
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }
}
