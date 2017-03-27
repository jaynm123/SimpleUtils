package com.tianer.ch.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tianer.ch.R;
import com.tianer.ch.base.BaseActivity;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.File;
import java.util.List;

/**
 * ch
 * 相册 基础类
 */
public abstract class PicActivity extends BaseActivity {

    private PopupWindow pop;
    private TextView tv_photograph;
    private TextView tv_album;
    private LinearLayout ll_cancle_layout;
    /**
     * 基础路径
     */

    private String basePath;
    /**
     * 裁剪存放路径
     */
    private String corpPath = "corp/";
    /**
     * 拍照
     */
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 0;
    /**
     * 从相册选择
     */
    private static final int REQUEST_CODE_PICK_IMAGE = 1;
    /**
     * 剪切
     */
    private static final int CROP_REQUEST_CODE = 4;
    /**
     * 返回路径
     */
    private Uri photoUri;
    /**
     * 是否裁剪
     */
    private boolean iscorp;
    private String crop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 相册回调
     *
     * @param path
     */
    public abstract void formAlbum(String path);

    /**
     * 相机回调
     *
     * @param path
     */
    public abstract void formCamera(String path);

    /**
     * 裁剪回调
     *
     * @param path
     */
    public abstract void formCorp(String path);


    /**
     * 显示选择pop
     *
     * @param parent
     * @param basePath /mayatu/
     * @param iscorp   是否裁剪
     */
    public void showPicPopWindow(View parent, String basePath, boolean iscorp) {
        this.iscorp = iscorp;
        this.basePath = Environment.getExternalStorageDirectory() + basePath;
        View view = LayoutInflater.from(PicActivity.this).inflate(R.layout.popw_alert_select_phone, null);
        initView(view);
        // 设置SelectPicPopupWindow的View
        pop = new PopupWindow(PicActivity.this);
        pop.setContentView(view);
        // 设置SelectPicPopupWindow弹出窗体的宽
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        pop.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        pop.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        pop.setBackgroundDrawable(dw);
        // 获取当前打开的界面的 父View
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        view.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {
                pop.dismiss();
                return true;
            }
        });
        pop.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void initView(View view) {
        tv_photograph = (TextView) view.findViewById(R.id.tv_photograph);
        tv_album = (TextView) view.findViewById(R.id.tv_album);
        ll_cancle_layout = (LinearLayout) view.findViewById(R.id.ll_cancle_layout);
        tv_photograph.setOnClickListener(new MyOnClickListener());
        tv_album.setOnClickListener(new MyOnClickListener());
        ll_cancle_layout.setOnClickListener(new MyOnClickListener());
    }


    private class MyOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                // 拍照
                case R.id.tv_photograph:
                    if (AndPermission.hasPermission(context, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE
                            , Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        // 有权限，直接do anything.
                        getImageFromCamera();
                    } else {
                        // 申请权限。
                        AndPermission.with(PicActivity.this)
                                .requestCode(100)
                                .permission(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .send();
                    }


                    if (pop != null) {
                        pop.dismiss();
                    }
                    break;
                // 相册
                case R.id.tv_album:

                    if (AndPermission.hasPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )) {
                        // 有权限，直接do anything.
                        getImageFromAlbum();// 从相册里获取
                    } else {
                        // 申请权限。
                        AndPermission.with(PicActivity.this)
                                .requestCode(101)
                                .permission(Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .send();
                    }
                    if (pop != null) {
                        pop.dismiss();
                    }
                    break;
                // 取消
                case R.id.ll_cancle_layout:
                    if (pop != null) {
                        pop.dismiss();
                    }
                    break;

                default:
                    break;
            }
        }
    }

    /**
     * 从相册里选取
     */
    protected void getImageFromAlbum() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    /**
     * 获取安卓系统照相机拍照
     */
    protected void getImageFromCamera() {

        try {
            Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
            String state = Environment.getExternalStorageState();
            if (state.equals(Environment.MEDIA_MOUNTED)) {// 检查是否有存储卡
                // 创建位置

                File fileDir = new File(basePath);
                if (!fileDir.exists()) {// 目录是否存在
                    fileDir.mkdirs();// 删掉
                }

                File file = new File(basePath + System.currentTimeMillis() + ".jpg");
                if (file != null) {
                    photoUri = Uri.fromFile(file); // Create path for temp file
                    // 相机存储的地址
                    getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(getImageByCamera, REQUEST_CODE_CAPTURE_CAMEIA);// 拍照完送去裁剪
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 裁剪
     *
     * @param uri
     */
    private void startPhotoZoom(Uri uri) {
        try {
            // ========================================
            File fileDir = new File(basePath + corpPath);
            if (!fileDir.exists()) {// 目录是否存在
                fileDir.mkdirs();// 创建
            }

            crop = basePath + corpPath + System.currentTimeMillis() + ".jpg";

            Uri imageUri = Uri.parse("file://" + crop);

            final Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
            intent.putExtra("noFaceDetection", false); // 不启用人脸识别
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", false);
            intent.putExtra("return-data", false);
            startActivityForResult(intent, CROP_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // 拍照回调
            case REQUEST_CODE_CAPTURE_CAMEIA:
                if (iscorp) {
                    startPhotoZoom(photoUri);
                } else {
                    formCamera(photoUri.getPath());
                }

                break;
            // 从相册里选取
            case REQUEST_CODE_PICK_IMAGE:
                if (null != data) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        if (iscorp) {
                            startPhotoZoom(uri);
                        } else {
                            formAlbum(uri.getPath());
                        }
                    }
                }

                break;
            // 剪切
            case CROP_REQUEST_CODE:
                formCorp(crop);
                break;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 只需要调用这一句，其它的交给AndPermission吧，最后一个参数是PermissionListener。
        AndPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, listener);
    }

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。
            if (requestCode == 100) {
                getImageFromCamera();
            } else if (requestCode == 101) {
                getImageFromAlbum();
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。

            // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
            if (AndPermission.hasAlwaysDeniedPermission(PicActivity.this, deniedPermissions)) {
                // 第一种：用默认的提示语。
                AndPermission.defaultSettingDialog(PicActivity.this, 300).show();

                // 第二种：用自定义的提示语。
                // AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING)
                // .setTitle("权限申请失败")
                // .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
                // .setPositiveButton("好，去设置")
                // .show();

                // 第三种：自定义dialog样式。
                // SettingService settingService =
                //    AndPermission.defineSettingDialog(this, REQUEST_CODE_SETTING);
                // 你的dialog点击了确定调用：
                // settingService.execute();
                // 你的dialog点击了取消调用：
                // settingService.cancel();
            }
        }
    };

}
