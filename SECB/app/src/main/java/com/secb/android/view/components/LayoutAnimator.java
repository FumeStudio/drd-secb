package com.secb.android.view.components;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.secb.android.R;

import net.comptoirs.android.common.view.CTApplication;

public class LayoutAnimator {
	private final float startPointY;
	private final float startPointX;
	ViewGroup layout;
	View darkArea;
	public boolean isDown;
	public boolean isVerticalAnimation;
	String animationDirection = "translationY";

	final Animation fadeOut = AnimationUtils.loadAnimation(CTApplication.getContext(), R.anim.fadeout);
	final Animation fadeIn = AnimationUtils.loadAnimation(CTApplication.getContext(), R.anim.fade_in);

	public LayoutAnimator(ViewGroup layout, boolean isVerticalAnimation, float startPointX, float startPointY) {
		this.layout = layout;
		this.isVerticalAnimation = isVerticalAnimation;
		animationDirection = isVerticalAnimation ? "translationY" : "translationX";
		this.startPointX = startPointX;
		this.startPointY = startPointY;
		hideFirst();
//	    darkArea = layout.findViewById(R.id.layout_dark_layer);
	}

	public void setDarkLayer(View _darkLayer) {
		this.darkArea = _darkLayer;
	}

	public void hidePreviewPanel() {
		fadeOut();
		android.animation.Animator.AnimatorListener listener = new android.animation.Animator.AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animator) {
/*				//hide dark area
				if (darkArea != null)
					darkArea.setVisibility(View.INVISIBLE);*/

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

		ObjectAnimator animator = null;
		if (isVerticalAnimation)
			animator = ObjectAnimator.ofFloat(layout, animationDirection, layout.getMeasuredHeight())
					.setDuration(400);
		else
			animator = ObjectAnimator.ofFloat(layout, animationDirection, layout.getMeasuredWidth())
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
				fadeIn();
/*				//Show dark area
				if (darkArea != null)
					darkArea.setVisibility(View.VISIBLE);*/
			}

			@Override
			public void onAnimationCancel(Animator animator) {

			}

			@Override
			public void onAnimationRepeat(Animator animator) {

			}
		};
		ObjectAnimator animator = null;
		if (isVerticalAnimation)
			animator = ObjectAnimator.ofFloat(layout, animationDirection, -(0 /*layout.getMeasuredHeight()*/))
					.setDuration(850);
		else
			animator = ObjectAnimator.ofFloat(layout, animationDirection, -(0 /*layout.getMeasuredHeight()*/))
					.setDuration(850);

		animator.addListener(listener);
		animator.start();

	}

	public void hideFirst() {
		layout.setX(startPointX);
		layout.setY(startPointY);
		if (isVerticalAnimation) {
			if (!isDown) {
				ObjectAnimator animatorTemp =
						ObjectAnimator.ofFloat(layout, "translationY", layout.getMeasuredHeight())
								.setDuration(1);
				animatorTemp.start();
			}
		} else {
			ObjectAnimator animatorTemp =
					ObjectAnimator.ofFloat(layout, "translationX", layout.getMeasuredWidth())
							.setDuration(1);
			animatorTemp.start();
		}


	}


	public void fadeOut() {
		if (darkArea != null) {
			fadeOut.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					darkArea.setVisibility(View.INVISIBLE);
				}
			});
			darkArea.startAnimation(fadeOut);
		}
	}

	public void fadeIn() {
		if (darkArea != null) {
			darkArea.setVisibility(View.INVISIBLE);
			fadeIn.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					darkArea.setVisibility(View.VISIBLE);
				}
			});
			darkArea.startAnimation(fadeIn);
		}
	}
}

