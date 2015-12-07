package com.gmail.khalilzaidoun.training.facedetectionapp.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gmail.khalilzaidoun.training.facedetectionapp.R;
import com.gmail.khalilzaidoun.training.facedetectionapp.app.AppConstants;
import com.gmail.khalilzaidoun.training.facedetectionapp.app.AppSettings;
import com.gmail.khalilzaidoun.training.facedetectionapp.helper.CustomLoaderCallback;
import com.gmail.khalilzaidoun.training.facedetectionapp.ui.opencv.MyOpenCvCameraView;
import com.gmail.khalilzaidoun.training.facedetectionapp.utils.DetectUtils;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class FDActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2, View.OnClickListener {

    public static final String TAG = FDActivity.class.getName();

    static {
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
            Log.e(TAG, "Handle initialization error open Cv");
        }
    }

    CustomLoaderCallback customLoaderCallback;

    ImageView imageView;

    LinearLayout linearLayoutHolder;

    public static MyOpenCvCameraView mOpenCvCameraView;


    private boolean safeToTakePicture = false;

    int learn_frames = 0;
    Mat teplateR;
    Mat teplateL;

    // matrix for zooming
    Mat mZoomWindow;
    Mat mZoomWindow2;

    float mRelativeFaceSize = 0.2f;
    int mAbsoluteFaceSize = 0;

    double xCenter = -1;
    double yCenter = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fd);

        customLoaderCallback = new CustomLoaderCallback(this);

        ImageButton imageButtonTake = (ImageButton) findViewById(R.id.activity_fd_take);
        ImageButton imageButtonRecreate = (ImageButton) findViewById(R.id.activity_fd_recreate);
        ImageButton imageButtonStartPreview = (ImageButton) findViewById(R.id.activity_fd_start_preview);
        ImageButton imageButtonDownload = (ImageButton) findViewById(R.id.activity_fd_download);

        ImageView imageView = (ImageView) findViewById(R.id.activity_fd_image_view);

        linearLayoutHolder = (LinearLayout) findViewById(R.id.activity_fd_holder);


        mOpenCvCameraView = (MyOpenCvCameraView) findViewById(R.id.fd_activity_surface_view);

        mOpenCvCameraView.setCvCameraViewListener(this);

        imageButtonTake.setOnClickListener(this);
        imageButtonRecreate.setOnClickListener(this);
        imageButtonStartPreview.setOnClickListener(this);
        imageButtonDownload.setOnClickListener(this);

        setMinFaceSize();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        safeToTakePicture = true;
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this,
                customLoaderCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOpenCvCameraView.disableView();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.activity_fd_take:

                String path = Environment.getExternalStorageDirectory().getPath();

                final String fileName = path + "/imag.jpg";


                if (safeToTakePicture) {
                    safeToTakePicture = false;


                }
                break;

            case R.id.activity_fd_start_preview:

                linearLayoutHolder.setVisibility(View.GONE);

                break;
            case R.id.activity_fd_recreate:

                learn_frames = 0;

                break;

            case R.id.activity_fd_download:


                break;
        }

    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        DetectUtils.mGray = new Mat();
        DetectUtils.mRgba = new Mat();

    }

    @Override
    public void onCameraViewStopped() {
        DetectUtils.mGray.release();
        DetectUtils.mRgba.release();
        mZoomWindow.release();
        mZoomWindow2.release();

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        DetectUtils.mRgba = inputFrame.rgba();
        DetectUtils.mGray = inputFrame.gray();

        if (mAbsoluteFaceSize == 0) {
            int height = DetectUtils.mGray.rows();
            if (Math.round(height * mRelativeFaceSize) > 0) {
                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
            }
        }


        if (mZoomWindow == null || mZoomWindow2 == null)
            CreateAuxiliaryMats();

        MatOfRect faces = new MatOfRect();

        if (customLoaderCallback.mJavaDetector != null)
            customLoaderCallback.mJavaDetector.detectMultiScale(DetectUtils.mGray, faces, 1.1, 2,
                    2, // TODO: objdetect.CV_HAAR_SCALE_IMAGE
                    new Size(mAbsoluteFaceSize, mAbsoluteFaceSize),
                    new Size());


        Rect[] facesArray = faces.toArray();
        for (Rect aFacesArray : facesArray) {
            Imgproc.rectangle(DetectUtils.mRgba, aFacesArray.tl(), aFacesArray.br(),
                    AppConstants.FACE_RECT_COLOR, 3);
            xCenter = (aFacesArray.x + aFacesArray.width + aFacesArray.x) / 2;
            yCenter = (aFacesArray.y + aFacesArray.y + aFacesArray.height) / 2;
            Point center = new Point(xCenter, yCenter);

            Imgproc.circle(DetectUtils.mRgba, center, 10, new Scalar(255, 0, 0, 255), 3);

            Imgproc.putText(DetectUtils.mRgba, "[" + center.x + "," + center.y + "]",
                    new Point(center.x + 20, center.y + 20),
                    Core.FONT_HERSHEY_SIMPLEX, 0.7, new Scalar(255, 255, 255,
                            255));


            // compute the eye area
            Rect eyearea = new Rect(aFacesArray.x + aFacesArray.width / 8,
                    (int) (aFacesArray.y + (aFacesArray.height / 4.5)), aFacesArray.width - 2 * aFacesArray.width / 8,
                    (int) (aFacesArray.height / 3.0));

            // split it
            Rect eyearea_right = new Rect(aFacesArray.x + aFacesArray.width / 16,
                    (int) (aFacesArray.y + (aFacesArray.height / 4.5)),
                    (aFacesArray.width - 2 * aFacesArray.width / 16) / 2, (int) (aFacesArray.height / 3.0));
            Rect eyearea_left = new Rect(aFacesArray.x + aFacesArray.width / 16
                    + (aFacesArray.width - 2 * aFacesArray.width / 16) / 2,
                    (int) (aFacesArray.y + (aFacesArray.height / 4.5)),
                    (aFacesArray.width - 2 * aFacesArray.width / 16) / 2, (int) (aFacesArray.height / 3.0));

            // draw the area - mGray is working grayscale mat, if you want to
            // see area in rgb preview, change mGray to mRgba
            Imgproc.rectangle(DetectUtils.mRgba, eyearea_left.tl(), eyearea_left.br(),
                    new Scalar(255, 0, 0, 255), 2);
            Imgproc.rectangle(DetectUtils.mRgba, eyearea_right.tl(), eyearea_right.br(),
                    new Scalar(255, 0, 0, 255), 2);

            if (learn_frames < 5) {
                teplateR = DetectUtils.get_template(customLoaderCallback.mJavaDetectorEye, eyearea_right, 24);
                teplateL = DetectUtils.get_template(customLoaderCallback.mJavaDetectorEye, eyearea_left, 24);
                learn_frames++;
            } else {
                // Learning finished, use the new templates for template
                // matching
                DetectUtils.match_eye(eyearea_right, teplateR, AppSettings.matchTemplateMethodIndex);
                DetectUtils.match_eye(eyearea_left, teplateL, AppSettings.matchTemplateMethodIndex);

            }


            // cut eye areas and put them to zoom windows
            Imgproc.resize(DetectUtils.mRgba.submat(eyearea_left), mZoomWindow2,
                    mZoomWindow2.size());
            Imgproc.resize(DetectUtils.mRgba.submat(eyearea_right), mZoomWindow,
                    mZoomWindow.size());


        }


        if (!safeToTakePicture)
            onFaceCaptured(DetectUtils.mRgba);

        return DetectUtils.mRgba;

    }

    private void setMinFaceSize() {
        float faceSize = 0;

        switch (AppSettings.minFaceSizeIndex) {

            case AppConstants.MIN_FACE_SIZE_20_INDEX:

                faceSize = 0.2f;
                break;
            case AppConstants.MIN_FACE_SIZE_30_INDEX:

                faceSize = 0.3f;
                break;
            case AppConstants.MIN_FACE_SIZE_40_INDEX:

                faceSize = 0.4f;
                break;
            case AppConstants.MIN_FACE_SIZE_50_INDEX:

                faceSize = 0.5f;
                break;
        }
        mAbsoluteFaceSize = 0;
        mRelativeFaceSize = faceSize;

    }


    private void CreateAuxiliaryMats() {
        if (DetectUtils.mGray.empty())
            return;

        int rows = DetectUtils.mGray.rows();
        int cols = DetectUtils.mGray.cols();

        if (mZoomWindow == null) {
            mZoomWindow = DetectUtils.mRgba.submat(rows / 2 + rows / 10, rows, cols / 2
                    + cols / 10, cols);
            mZoomWindow2 = DetectUtils.mRgba.submat(0, rows / 2 - rows / 10, cols / 2
                    + cols / 10, cols);
        }

    }

    private void onFaceCaptured(Mat faceMat) {
        if (!faceMat.empty()) {

            final Bitmap bmp = Bitmap.createBitmap(faceMat.cols(), faceMat.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(faceMat, bmp);

          // bmp   = drawORBFeatures(bmp);
            FDActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    imageView.setImageBitmap(bmp);
                    imageView.setVisibility(View.VISIBLE);
                    mOpenCvCameraView.setVisibility(View.GONE);
                }
            });
        }
    }
}
