package com.secb.android.view.components.dialogs;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.secb.android.R;

public class CustomProgressDialog  extends ProgressDialog
{
    private AnimationDrawable animation;
    RotateAnimation rotateAnimation ;
    ImageView imgv_loadingCircle;

    public CustomProgressDialog(Context context)
    {
        super(context);
    }

    public static CustomProgressDialog getInstance(Context context,boolean isCancelable)
    {
        CustomProgressDialog dialog = new CustomProgressDialog(context);
        dialog.setIndeterminate(true);
        dialog.setCancelable(isCancelable);
        return dialog;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_progress_dialog);
        imgv_loadingCircle= (ImageView) findViewById(R.id.imgv_loadingCircle);
/*        imgv_loadingCircle.setBackgroundResource(R.drawable.loading_circle_white);
        animation = (AnimationDrawable) imgv_loadingCircle.getBackground();*/
    }

    @Override
    public void show() {
        super.show();
/*        if(animation!=null)
            animation.start();*/

        startAnimation();
    }

    private void startAnimation() {
        rotateAnimation = new RotateAnimation(0.0f, 360.0f,
                Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF,
                .5f);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setDuration(700);
        imgv_loadingCircle.setAnimation(rotateAnimation);
        imgv_loadingCircle.startAnimation(rotateAnimation);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        /*if(animation!=null)
            animation.stop();*/


    }
}
