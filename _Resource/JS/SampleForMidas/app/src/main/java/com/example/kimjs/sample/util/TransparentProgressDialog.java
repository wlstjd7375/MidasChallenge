package com.example.kimjs.sample.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.kimjs.sample.R;

/**
 * Created by KimJS on 2015-08-16.
 */
public class TransparentProgressDialog extends Dialog {
    public static TransparentProgressDialog show(Context context, CharSequence title,
                                        CharSequence message) {
        return show(context, title, message, false);
    }

    public static TransparentProgressDialog show(Context context, CharSequence title,
                                        CharSequence message, boolean indeterminate) {
        return show(context, title, message, indeterminate, false, null);
    }

    public static TransparentProgressDialog show(Context context, CharSequence title,
                                        CharSequence message, boolean indeterminate, boolean cancelable) {
        return show(context, title, message, indeterminate, cancelable, null);
    }

    public static TransparentProgressDialog show(Context context, CharSequence title,
                                        CharSequence message, boolean indeterminate,
                                        boolean cancelable, OnCancelListener cancelListener) {
        TransparentProgressDialog dialog = new TransparentProgressDialog(context);
        //dialog.setTitle(title);
        dialog.setCancelable(cancelable);
        //dialog.setOnCancelListener(cancelListener);
        /* The next line will add the ProgressBar to the dialog. */
        dialog.addContentView(new ProgressBar(context), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        dialog.show();

        return dialog;
    }

    public TransparentProgressDialog(Context context) {
        super(context, R.style.TransparentDialog);
    }
}
