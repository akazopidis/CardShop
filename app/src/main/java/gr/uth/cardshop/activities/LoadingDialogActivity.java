package gr.uth.cardshop.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import gr.uth.cardshop.R;

public class LoadingDialogActivity {
    private Activity activity;
    private AlertDialog dialog;

    LoadingDialogActivity(Activity myActivity){
        activity = myActivity;
    }
    void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.activity_custom_dialog, null));
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();
    }
    void dismissDialog(){
        dialog.dismiss();
    }
}