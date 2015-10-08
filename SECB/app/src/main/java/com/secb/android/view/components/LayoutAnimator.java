package com.secb.android.view.components;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

public class LayoutAnimator {
    ViewGroup layout;
    public boolean isDown;

    public LayoutAnimator(ViewGroup layout) {
        this.layout = layout;
        moveDownFirst();
    }

    public void hidePreviewPanel() {

        android.animation.Animator.AnimatorListener listener = new android.animation.Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                layout.setVisibility(View.GONE);
                isDown = true;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        };

        ObjectAnimator animator;
        animator = ObjectAnimator.ofFloat(layout, "translationY", layout.getMeasuredHeight())
                .setDuration(400);
        animator.addListener(listener);
        animator.start();

    }


    public void showPreviewPanel() {

        android.animation.Animator.AnimatorListener listener = new android.animation.Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animator) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layout.setVisibility(View.VISIBLE);
                    }
                }, 300);

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                layout.setVisibility(View.VISIBLE);
                isDown = false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        };

        ObjectAnimator animator = ObjectAnimator.ofFloat(layout, "translationY", -(0 /*layout.getMeasuredHeight()*/))
                .setDuration(850);
        animator.addListener(listener);
        animator.start();

    }

    public void moveDownFirst() {
        if (!isDown) {
            ObjectAnimator animatorTemp =
                    ObjectAnimator.ofFloat(layout, "translationY", layout.getMeasuredHeight())
                            .setDuration(1);
            animatorTemp.start();
        }
    }

}
