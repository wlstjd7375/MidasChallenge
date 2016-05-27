package com.example.woqja.young;

import android.support.design.widget.Snackbar;
import android.text.Html;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/*
* 요청 실패했을 때
* Error 표시해주는 리스너
* */

public class ErrorListener implements Response.ErrorListener {
    private View mMainLayout;

    public ErrorListener(View mainLayout) {
        this.mMainLayout = mainLayout;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        showErrorMessage(mMainLayout.getContext().getString(R.string.error_network));
    }

    public void showErrorMessage(String message) {
        message = String.format(mMainLayout.getContext().getString(R.string.error_message), message);
        Snackbar snackbar = Snackbar.make(mMainLayout, Html.fromHtml(message), Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}
