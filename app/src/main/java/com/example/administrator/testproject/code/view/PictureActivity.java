package com.example.administrator.testproject.code.view;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.administrator.testproject.R;
import com.example.administrator.testproject.base.BaseActivity;
import com.example.administrator.testproject.code.main.view.MainActivity;
import com.example.administrator.testproject.util.PictureUtils;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PictureActivity extends BaseActivity implements View.OnClickListener {

    public final static int CAMERA_REQUEST_CODE = 0;
    public final static int GALLERY_REQUEST_CODE = 1;
    @BindView(R.id.bt_take_picture)
    Button btTakePicture;
    @BindView(R.id.bt_choose_picture)
    Button btChoosePicture;
    @BindView(R.id.iv_picture)
    ImageView mIvPicture;

    private Uri imageUri;
    private Bitmap mWallpaper;
    private Bitmap mBit;

    public static void launch(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, PictureActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        ButterKnife.bind(this);

        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        mWallpaper = PictureUtils.getInstance().drawableToBitmap(wallpaperDrawable);

        btTakePicture.setOnClickListener(this);
        btChoosePicture.setOnClickListener(this);
    }

    private void takePhoto() {
        Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String tempPhotoPath = Environment.getExternalStorageDirectory() + File.separator + "photo.jpeg";
        imageUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".my.provider",
                new File(tempPhotoPath));
        intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intentToTakePhoto, CAMERA_REQUEST_CODE);
    }

    private void choosePhoto() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    private void startUCrop() {
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "cropImg.jpg"));
        UCrop uCrop = UCrop.of(imageUri, destinationUri);
        UCrop.Options options = new UCrop.Options();
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        options.setToolbarColor(ActivityCompat.getColor(this, R.color.gray));
        options.setStatusBarColor(ActivityCompat.getColor(this, R.color.gray));
        options.setFreeStyleCropEnabled(true);
        uCrop.withOptions(options);
        uCrop.start(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == MainActivity.RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST_CODE: {
                    try {
                        Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        mIvPicture.setImageBitmap(bit);
                        startUCrop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case GALLERY_REQUEST_CODE: {
                    try {
                        imageUri = data.getData();
                        startUCrop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case UCrop.REQUEST_CROP: {
                    Uri croppedUri = UCrop.getOutput(data);
                    try {
                        if (croppedUri != null) {
                            mBit = BitmapFactory.decodeStream(getContentResolver().openInputStream(croppedUri));
                            mIvPicture.setImageBitmap(mBit);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case UCrop.RESULT_ERROR: {
                    Throwable cropError = UCrop.getError(data);
                    ToastUtils.showShort("图片裁剪失败，详情看log");
                    LogUtils.eTag("RESULT_ERROR", "UCrop_RESULT_ERROR:" + cropError);
                    break;
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.bt1, R.id.bt2, R.id.bt3, R.id.bt4, R.id.bt5, R.id.bt6, R.id.bt7,
            R.id.bt8, R.id.bt9, R.id.bt10, R.id.bt11, R.id.bt12, R.id.bt13, R.id.bt14, R.id.bt15})
    public void onViewClicked(View view) {
        if (mBit == null) {
            ToastUtils.showShort("请先选择图片");
            return;
        }
        Bitmap bitmap = mBit;
        Bitmap bit = null;

        try {
            switch (view.getId()) {
                case R.id.bt1:
                    bit = PictureUtils.getInstance().convertToBlur(bitmap);
                    break;
                case R.id.bt2:
//                    bit = PictureUtils.getInstance().convertToSketch(bitmap);
                    break;
                case R.id.bt3:
                    bit = PictureUtils.getInstance().sharpenImageAmeliorate(bitmap);
                    break;
                case R.id.bt4:
                    bit = PictureUtils.getInstance().oldRemeberImage(bitmap);
                    break;
                case R.id.bt5:
                    bit = PictureUtils.getInstance().reliefImage(bitmap);
                    break;
                case R.id.bt6:
                    bit = PictureUtils.getInstance().sunshineImage(bitmap, PictureUtils.Position.CENTRE, 150);
                    break;
                case R.id.bt7:
                    bit = PictureUtils.getInstance().iceImage(bitmap);
                    break;
                case R.id.bt8:
                    bit = PictureUtils.getInstance().toReflectedImage(bitmap);
                    break;
                case R.id.bt9:
                    bit = PictureUtils.getInstance().createBitmapWithWatermark(mWallpaper, bitmap, PictureUtils.Position.CENTRE);
                    break;
                case R.id.bt10:
                    bit = PictureUtils.getInstance().toBlackAndWhite(bitmap);
                    break;
                case R.id.bt11:
                    bit = PictureUtils.getInstance().negativeFilm(bitmap);
                    break;
                case R.id.bt12:
                    bit = PictureUtils.getInstance().oilPainting(bitmap);
                    break;
                case R.id.bt13:
                    bit = PictureUtils.getInstance().smoothImage(bitmap);
                    break;
                case R.id.bt14:
                    bit = PictureUtils.getInstance().averageFilter(bitmap, 100, 100);
                    break;
                case R.id.bt15:
//                    bit = PictureUtils.getInstance().brightenBitmap(bitmap,100);
                    break;
            }
        } catch (Exception e) {
            LogUtils.e(e.getMessage());
        }

        mIvPicture.setImageBitmap(bit);
        ToastUtils.showShort("不可使用的为：素描、滤波");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_take_picture:
                takePhoto();
                break;
            case R.id.bt_choose_picture:
                choosePhoto();
                break;
        }
    }
}
