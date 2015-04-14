package com.prateek.eskores;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by prateek on 23/3/15.
 */
public class CustomDialog {

    private static CustomDialog _dialog = new CustomDialog();
    private static AlertDialog.Builder builder = null;

    public static final String DIALOG_TYPE = "TYPE_DIALOG";
    public static final String INFO_DIALOG = "INFO_DIALOG";
    public static final String NO_PREFERENCE_TEAM_DIALOG = "NO_PREFERENCE_TEAM_DIALOG";
    public static final String NO_PREFERENCE_LIVE_MATCH_DIALOG = "NO_PREFERENCE_LIVE_MATCH_DIALOG";

    public interface OnDialogButtonClickListener {
        public void onDialogPositiveButtonClicked(String dialogType);
        public void onDialogNegativeButtonClicked();
    }

    private CustomDialog() {

    }

    public static CustomDialog getInstance(Context context) {
        builder = new AlertDialog.Builder(context);
        return _dialog;
    }

    public void show(String title, String message, String positiveButton, String negativeButton, final String dialogType, final OnDialogButtonClickListener listener) {
        if(title != null) {
            builder.setTitle(title);
        }
        if(message != null) {
            builder.setMessage(message);
        }

        if(positiveButton != null) {
            builder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listener.onDialogPositiveButtonClicked(dialogType);
                }
            });
        }

        if(negativeButton != null) {
            builder.setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listener.onDialogNegativeButtonClicked();
                }
            });
        }

        builder.show();
    }
}
