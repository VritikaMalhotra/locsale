package com.example.locsaleapplication.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.example.locsaleapplication.R;
import com.example.locsaleapplication.presentation.chat.commons.API_CallBack;
import com.example.locsaleapplication.presentation.chat.commons.ApiRequest;
import com.example.locsaleapplication.presentation.chat.commons.Callback;
import com.gmail.samehadar.iosdialog.CamomileSpinner;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by AQEEL on 2/20/2019.
 */
public class Functions {


    public static Dialog dialog;

    public static void Show_loader(Context context, boolean outside_touch, boolean cancleable) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_dialog_loading_view);
        dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.d_round_white_background));


        CamomileSpinner loader = dialog.findViewById(R.id.loader);
        loader.start();


        if (!outside_touch)
            dialog.setCanceledOnTouchOutside(false);

        if (!cancleable)
            dialog.setCancelable(false);

        dialog.show();
    }

    public static void cancel_loader() {
        if (dialog != null) {
            dialog.cancel();
        }
    }


    /*public static void Call_Api_For_Get_User_data
            (final Activity activity,
             String fb_id,
             final API_CallBack api_callBack) {

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", fb_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("resp", parameters.toString());

        *//*ApiRequest.Call_Api(activity, AppGlobal.get_user_data, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Functions.cancel_loader();
                try {
                    JSONObject response = new JSONObject(resp);
                    String code = response.optString("code");
                    if (code.equals("200")) {
                        api_callBack.OnSuccess(response.toString());

                    } else {
                        Toast.makeText(activity, "" + response.optString("msg"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    api_callBack.OnFail(e.toString());
                    e.printStackTrace();
                }
            }
        });*//*

    }*/


}
