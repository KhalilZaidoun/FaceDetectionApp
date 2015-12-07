package com.gmail.khalilzaidoun.training.facedetectionapp.dialogs;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gmail.khalilzaidoun.training.facedetectionapp.R;
import com.gmail.khalilzaidoun.training.facedetectionapp.app.AppConfig;
import com.gmail.khalilzaidoun.training.facedetectionapp.app.AppSettings;

public class SettingsDialog extends DialogFragment implements SeekBar.OnSeekBarChangeListener {

    int minFaceSizeIndexTemporal = AppSettings.minFaceSizeIndex;
    String minFaceSizeValueTemporal = AppSettings.minFaceSizeValue;

    int matchTemplateMethodIndexTemporal = AppSettings.matchTemplateMethodIndex;
    String matchTemplateMethodValueTemporal = AppSettings.matchTemplateMethodValue;


    SeekBar seekBarMethod;
    SeekBar seekBarFaceSize;

    TextView textViewMethod;
    TextView textViewFaceSise;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        View rootView = inflater.inflate(R.layout.dialog_settings, null);


        seekBarMethod = (SeekBar) rootView.findViewById(R.id.seek_bar_method);
        seekBarFaceSize = (SeekBar) rootView.findViewById(R.id.seek_bar_face_size);


        textViewMethod = (TextView) rootView.findViewById(R.id.text_view_method);
        textViewFaceSise = (TextView) rootView.findViewById(R.id.text_view_face_size);


        textViewMethod.setText(AppSettings.matchTemplateMethodValue);
        textViewFaceSise.setText(AppSettings.minFaceSizeValue);


        seekBarMethod.setOnSeekBarChangeListener(this);
        seekBarFaceSize.setOnSeekBarChangeListener(this);


        builder.setView(rootView)
                // Add action buttons
                .setPositiveButton(R.string.dialog_settings_btn_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        AppSettings.matchTemplateMethodIndex = matchTemplateMethodIndexTemporal;
                        AppSettings.matchTemplateMethodValue = matchTemplateMethodValueTemporal;

                        AppSettings.minFaceSizeIndex = minFaceSizeIndexTemporal;
                        AppSettings.minFaceSizeValue = minFaceSizeValueTemporal;

                    }
                })
                .setNegativeButton(R.string.dialog_settings_btn_negative, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SettingsDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if (seekBar.getId() == R.id.seek_bar_method) {

            matchTemplateMethodIndexTemporal = progress;
            switch (progress) {

                case AppConfig.TM_SQDIFF_INDEX:
                    textViewMethod.setText(AppConfig.TM_SQDIFF);
                    matchTemplateMethodValueTemporal = AppConfig.TM_SQDIFF;
                    break;
                case AppConfig.TM_SQDIFF_NORMED_INDEX:
                    textViewMethod.setText(AppConfig.TM_SQDIFF_NORMED);
                    matchTemplateMethodValueTemporal = AppConfig.TM_SQDIFF_NORMED;
                    break;
                case AppConfig.TM_CCOEFF_INDEX:
                    textViewMethod.setText(AppConfig.TM_CCOEFF);
                    matchTemplateMethodValueTemporal = AppConfig.TM_CCOEFF;
                    break;
                case AppConfig.TM_CCOEFF_NORMED_INDEX:
                    textViewMethod.setText(AppConfig.TM_CCOEFF_NORMED);
                    matchTemplateMethodValueTemporal = AppConfig.TM_CCOEFF_NORMED;
                    break;
                case AppConfig.TM_CCORR_INDEX:
                    textViewMethod.setText(AppConfig.TM_CCORR);
                    matchTemplateMethodValueTemporal = AppConfig.TM_CCORR;
                    break;
                case AppConfig.TM_CCORR_NORMED_INDEX:
                    textViewMethod.setText(AppConfig.TM_CCORR_NORMED);
                    matchTemplateMethodValueTemporal = AppConfig.TM_CCORR_NORMED;
                    break;
            }
        } else if (seekBar.getId() == R.id.seek_bar_face_size) {

            minFaceSizeIndexTemporal = progress;
            switch (progress) {

                case AppConfig.MIN_FACE_SIZE_20_INDEX:
                    textViewFaceSise.setText(AppConfig.MIN_FACE_SIZE_20);
                    matchTemplateMethodValueTemporal = AppConfig.MIN_FACE_SIZE_20;
                    break;
                case AppConfig.MIN_FACE_SIZE_30_INDEX:
                    textViewFaceSise.setText(AppConfig.MIN_FACE_SIZE_30);
                    matchTemplateMethodValueTemporal = AppConfig.MIN_FACE_SIZE_30;
                    break;
                case AppConfig.MIN_FACE_SIZE_40_INDEX:
                    textViewFaceSise.setText(AppConfig.MIN_FACE_SIZE_40);
                    matchTemplateMethodValueTemporal = AppConfig.MIN_FACE_SIZE_40;
                    break;
                case AppConfig.MIN_FACE_SIZE_50_INDEX:
                    textViewFaceSise.setText(AppConfig.MIN_FACE_SIZE_50);
                    matchTemplateMethodValueTemporal = AppConfig.MIN_FACE_SIZE_50;
                    break;

            }


        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
