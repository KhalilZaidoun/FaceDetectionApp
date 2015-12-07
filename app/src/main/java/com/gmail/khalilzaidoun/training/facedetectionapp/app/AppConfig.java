package com.gmail.khalilzaidoun.training.facedetectionapp.app;


import org.opencv.core.Scalar;

public class AppConfig {

    public static final String APP_NAME = "FaceDetectionApp";

    public static final String AUTHOR = "KhalilZaidoun";

    public static final Scalar FACE_RECT_COLOR = new Scalar(0, 255, 0, 255);

    public static final int MIN_FACE_SIZE_20_INDEX = 0;
    public static final String MIN_FACE_SIZE_20 = "Face size 20%";

    public static final int MIN_FACE_SIZE_30_INDEX = 1;
    public static final String MIN_FACE_SIZE_30 = "Face size 30%";

    public static final int MIN_FACE_SIZE_40_INDEX = 2;
    public static final String MIN_FACE_SIZE_40 = "Face size 40%";

    public static final int MIN_FACE_SIZE_50_INDEX = 3;
    public static final String MIN_FACE_SIZE_50 = "Face size 50%";


    public static final int TM_SQDIFF_INDEX = 0;
    public static final String TM_SQDIFF = "TM_SQDIFF";

    public static final int TM_SQDIFF_NORMED_INDEX = 1;
    public static final String TM_SQDIFF_NORMED = "TM_SQDIFF_NORMED";

    public static final int TM_CCOEFF_INDEX = 2;
    public static final String TM_CCOEFF = "TM_CCOEFF";


    public static final int TM_CCOEFF_NORMED_INDEX = 3;
    public static final String TM_CCOEFF_NORMED = "TM_CCOEFF_NORMED";

    public static final int TM_CCORR_INDEX = 4;
    public static final String TM_CCORR = "TM_CCORR";

    public static final int TM_CCORR_NORMED_INDEX = 5;
    public static final String TM_CCORR_NORMED = "TM_CCORR_NORMED";

    public static final String ITEM_FACE50 = "";

}
