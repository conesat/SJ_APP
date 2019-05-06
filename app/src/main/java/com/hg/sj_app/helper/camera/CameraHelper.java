package com.hg.sj_app.helper.camera;

import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.FrameLayout;

import com.hg.sj_app.view.FaceRectView;

import java.io.IOException;
import java.util.List;

/**
 * 相机辅助类，和{@link CameraListener}共同使用，获取nv21数据等操作
 */
public class CameraHelper implements Camera.PreviewCallback {
    public boolean openFlash=false;

    private static final String TAG = "CameraHelper";
    private Camera mCamera;
    private int mCameraId;
    private Point previewViewSize;
    private View previewDisplayView;
    private Camera.Size previewSize;
    private Point specificPreviewSize;
    private int displayOrientation = 0;
    private int rotation;
    private int additionalRotation;
    private boolean isMirror = false;
    private FaceRectView faceRectView;

    private Integer specificCameraId = null;
    private CameraListener cameraListener;

    private CameraHelper(Builder builder) {
        previewDisplayView = builder.previewDisplayView;
        specificCameraId = builder.specificCameraId;
        cameraListener = builder.cameraListener;
        rotation = builder.rotation;
        additionalRotation = builder.additionalRotation;
        previewViewSize = builder.previewViewSize;
        specificPreviewSize = builder.previewSize;
        faceRectView=builder.faceRectView;
        if (builder.previewDisplayView instanceof TextureView) {
            isMirror = builder.isMirror;
        } else if (isMirror) {
            throw new RuntimeException("mirror is effective only when the preview is on a textureView");
        }
    }

    public void init() {
        if (previewDisplayView instanceof TextureView) {
            ((TextureView) this.previewDisplayView).setSurfaceTextureListener(textureListener);
        } else if (previewDisplayView instanceof SurfaceView) {
            ((SurfaceView) previewDisplayView).getHolder().addCallback(surfaceCallback);
        }

        if (isMirror) {
            previewDisplayView.setScaleX(-1);
        }
    }

    public void start() {
        synchronized (this) {
            //相机数量为2则打开1,1则打开0,相机ID 1为前置，0为后置
            mCameraId = Camera.getNumberOfCameras() - 1;
            //若指定了相机ID且该相机存在，则打开指定的相机
            if (specificCameraId != null && specificCameraId <= mCameraId) {
                mCameraId = specificCameraId;
            }

            //没有相机
            if (mCameraId == -1) {
                if (cameraListener != null) {
                    cameraListener.onCameraError(new Exception("camera not found"));
                }
                return;
            }
            if (mCamera == null) {
                mCamera = Camera.open(mCameraId);
            }
            displayOrientation = getCameraOri(rotation);
            mCamera.setDisplayOrientation(displayOrientation);
            try {
                Camera.Parameters parameters = mCamera.getParameters();
                parameters.setPreviewFormat(ImageFormat.NV21);

                //预览大小设置
                previewSize = getBestPreviewSize(previewViewSize.x, previewViewSize.y);
                previewDisplayView.getLayoutParams().height = previewSize.width;
                previewDisplayView.getLayoutParams().width = previewSize.height;
                faceRectView.getLayoutParams().height = previewSize.width;
                faceRectView.getLayoutParams().width = previewSize.height;

                ((FrameLayout)faceRectView.getParent()).getLayoutParams().height = previewSize.width;
                ((FrameLayout)faceRectView.getParent()).getLayoutParams().width = previewSize.height;

                previewDisplayView.requestLayout();
                faceRectView.requestLayout();
                ((FrameLayout)faceRectView.getParent()).requestLayout();

                parameters.setPreviewSize(previewSize.width, previewSize.height);

                //对焦模式设置
                List<String> supportedFocusModes = parameters.getSupportedFocusModes();
                if (supportedFocusModes != null && supportedFocusModes.size() > 0) {
                    if (supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                    } else if (supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                    } else if (supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                    }
                }
                mCamera.setParameters(parameters);
                if (previewDisplayView instanceof TextureView) {
                    mCamera.setPreviewTexture(((TextureView) previewDisplayView).getSurfaceTexture());
                } else {
                    mCamera.setPreviewDisplay(((SurfaceView) previewDisplayView).getHolder());
                }
                mCamera.setPreviewCallback(this);
                mCamera.startPreview();
                if (cameraListener != null) {
                    cameraListener.onCameraOpened(mCamera, mCameraId, displayOrientation, isMirror);
                }
            } catch (Exception e) {
                if (cameraListener != null) {
                    cameraListener.onCameraError(e);
                }
            }
        }
    }

    private int getCameraOri(int rotation) {
        int degrees = rotation * 90;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
            default:
                break;
        }
        additionalRotation /= 90;
        additionalRotation *= 90;
        degrees += additionalRotation;
        int result;
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(mCameraId, info);
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }

    public void stop() {
        synchronized (this) {
            if (mCamera == null) {
                return;
            }
            try {
                mCamera.setPreviewCallback(null);
                mCamera.setPreviewDisplay(null);
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
                if (cameraListener != null) {
                    cameraListener.onCameraClosed();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isStopped() {
        synchronized (this) {
            return mCamera == null;
        }
    }

    public void release() {
        stop();
        previewDisplayView = null;
        specificCameraId = null;
        cameraListener = null;
        previewViewSize = null;
        specificPreviewSize = null;
        previewSize = null;
    }

    private Camera.Size getBestPreviewSize(int width, int height) {
        Camera.Size result = null;
        final Camera.Parameters p = mCamera.getParameters();
        //特别注意此处需要规定rate的比是大的比小的，不然有可能出现rate = height/width，但是后面遍历的时候，current_rate = width/height,所以我们限定都为大的比小的。
        float rate = (float) Math.max(width, height) / (float) Math.min(width, height);
        float tmp_diff;
        float min_diff = -1f;
        for (Camera.Size size : p.getSupportedPreviewSizes()) {
            float current_rate = (float) Math.max(size.width, size.height) / (float) Math.min(size.width, size.height);
            tmp_diff = Math.abs(current_rate - rate);
            if (min_diff < 0) {
                min_diff = tmp_diff;
                result = size;
            }
            if (tmp_diff < min_diff) {
                min_diff = tmp_diff;
                result = size;
            }
        }
        return result;
    }

    public List<Camera.Size> getSupportedPreviewSizes() {
        if (mCamera == null) {
            return null;
        }
        return mCamera.getParameters().getSupportedPreviewSizes();
    }

    public List<Camera.Size> getSupportedPictureSizes() {
        if (mCamera == null) {
            return null;
        }
        return mCamera.getParameters().getSupportedPictureSizes();
    }


    @Override
    public void onPreviewFrame(byte[] nv21, Camera camera) {
        if (cameraListener != null) {
            cameraListener.onPreview(nv21, camera);
        }
    }

    private TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
            start();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
            Log.i(TAG, "onSurfaceTextureSizeChanged: " + width + "  " + height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            stop();
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

        }
    };
    private SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            start();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            stop();
        }
    };

    public void changeDisplayOrientation(int rotation) {
        if (mCamera != null) {
            this.rotation = rotation;
            displayOrientation = getCameraOri(rotation);
            mCamera.setDisplayOrientation(displayOrientation);
            if (cameraListener != null) {
                cameraListener.onCameraConfigurationChanged(mCameraId, displayOrientation);
            }
        }
    }

    public static final class Builder {

        /**
         * 预览显示的view，目前仅支持surfaceView和textureView
         */
        private View previewDisplayView;

        /**
         * 是否镜像显示，只支持textureView
         */
        private boolean isMirror;
        /**
         * 指定的相机ID
         */
        private Integer specificCameraId;
        /**
         * 事件回调
         */
        private CameraListener cameraListener;
        /**
         * 屏幕的长宽，在选择最佳相机比例时用到
         */
        private Point previewViewSize;
        /**
         * 传入getWindowManager().getDefaultDisplay().getRotation()的值即可
         */
        private int rotation;
        /**
         * 指定的预览宽高，若系统支持则会以这个预览宽高进行预览
         */
        private Point previewSize;

        /**
         * 额外的旋转角度（用于适配一些定制设备）
         */
        private int additionalRotation;

        private FaceRectView faceRectView;

        public Builder() {
        }


        public Builder previewOn(View val) {
            if (val instanceof SurfaceView || val instanceof TextureView) {
                previewDisplayView = val;
                return this;
            } else {
                throw new RuntimeException("you must preview on a textureView or a surfaceView");
            }
        }

        public Builder faceRectView(FaceRectView val) {
            faceRectView = val;
            return this;
        }

        public Builder isMirror(boolean val) {
            isMirror = val;
            return this;
        }

        public Builder previewSize(Point val) {
            previewSize = val;
            return this;
        }

        public Builder previewViewSize(Point val) {
            previewViewSize = val;
            return this;
        }

        public Builder rotation(int val) {
            rotation = val;
            return this;
        }

        public Builder additionalRotation(int val) {
            additionalRotation = val;
            return this;
        }

        public Builder specificCameraId(Integer val) {
            specificCameraId = val;
            return this;
        }

        public Builder cameraListener(CameraListener val) {
            cameraListener = val;
            return this;
        }

        public CameraHelper build() {
            if (previewViewSize == null) {
                Log.e(TAG, "previewViewSize is null, now use default previewSize");
            }
            if (cameraListener == null) {
                Log.e(TAG, "cameraListener is null, callback will not be called");
            }
            if (previewDisplayView == null) {
                throw new RuntimeException("you must preview on a textureView or a surfaceView");
            }
            return new CameraHelper(this);
        }
    }
    public void onFlash(boolean open) {
        // 判断闪光灯当前状态來修改
        Camera.Parameters parameters=mCamera.getParameters();
        this.openFlash=open;
        if (open) {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mCamera.setParameters(parameters);
        } else  {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(parameters);
        }
    }
}
