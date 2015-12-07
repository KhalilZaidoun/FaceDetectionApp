package com.gmail.khalilzaidoun.training.facedetectionapp.helper;


import android.content.Context;
import android.util.Log;

import com.gmail.khalilzaidoun.training.facedetectionapp.R;
import com.gmail.khalilzaidoun.training.facedetectionapp.activities.FDActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CustomLoaderCallback extends BaseLoaderCallback {

    public static final String TAG = CustomLoaderCallback.class.getName();

    //private Context AppContext;

    public File mCascadeFile;
    public CascadeClassifier mJavaDetector;
    public CascadeClassifier mJavaDetectorEye;


    public CustomLoaderCallback(Context AppContext) {
        super(AppContext);

    }

    @Override
    public void onManagerConnected(int status) {


        switch (status) {
            case LoaderCallbackInterface.SUCCESS: {
                Log.i(TAG, "OpenCV loaded successfully");

                try {
                    // load cascade file from application resources
                    InputStream is = mAppContext.getResources().openRawResource(
                            R.raw.lbpcascade_frontal_face);

                    File cascadeDir = mAppContext.getDir("cascade", Context.MODE_PRIVATE);
                    mCascadeFile = new File(cascadeDir,
                            "lbpcascade_frontal_facee.xml");
                    FileOutputStream os = new FileOutputStream(mCascadeFile);

                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                    is.close();
                    os.close();

                    // --------------------------------- load left eye
                    // classificator -----------------------------------
                    InputStream iser = mAppContext.getResources().openRawResource(
                            R.raw.haarcascade_lefteye_2splits);
                    File cascadeDirER = mAppContext.getDir("cascadeER",
                            Context.MODE_PRIVATE);
                    File cascadeFileER = new File(cascadeDirER,
                            "haarcascade_eye_right.xml");
                    FileOutputStream oser = new FileOutputStream(cascadeFileER);

                    byte[] bufferER = new byte[4096];
                    int bytesReadER;
                    while ((bytesReadER = iser.read(bufferER)) != -1) {
                        oser.write(bufferER, 0, bytesReadER);
                    }
                    iser.close();
                    oser.close();

                    mJavaDetector = new CascadeClassifier(
                            mCascadeFile.getAbsolutePath());
                    if (mJavaDetector.empty()) {
                        Log.e(TAG, "Failed to load cascade classifier");
                        mJavaDetector = null;
                    } else
                        Log.i(TAG, "Loaded cascade classifier from "
                                + mCascadeFile.getAbsolutePath());

                    mJavaDetectorEye = new CascadeClassifier(
                            cascadeFileER.getAbsolutePath());
                    if (mJavaDetectorEye.empty()) {
                        Log.e(TAG, "Failed to load cascade classifier");
                        mJavaDetectorEye = null;
                    } else
                        Log.i(TAG, "Loaded cascade classifier from "
                                + mCascadeFile.getAbsolutePath());


                    cascadeDir.delete();

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Failed to load cascade. Exception thrown: " + e);
                }
                FDActivity.mOpenCvCameraView.setCameraIndex(1);
                FDActivity.mOpenCvCameraView.enableFpsMeter();
                FDActivity.mOpenCvCameraView.enableView();

            }
            break;
            default: {
                super.onManagerConnected(status);
            }
            break;
        }
    }
}
