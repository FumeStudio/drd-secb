package net.comptoirs.android.common.helper;

import net.comptoirs.android.common.view.CTApplication;
import android.animation.ObjectAnimator;
import android.animation.Animator.AnimatorListener;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;

import com.secb.android.R;


public class CTAnimation
{
	public static void animateHorizontalSliding(View panel, float fromX, float toX, AnimatorListener animationListener)
	{
		if (panel == null)
		{
			Logger.instance().v("Artist-Activity", "Can't animate null view", false);
			return;
		}
		ObjectAnimator mover = ObjectAnimator.ofFloat(panel, "translationX", fromX, toX);
		if (animationListener != null) mover.addListener(animationListener);
		mover.setDuration(300);
		mover.start();
	}
	/*
	 * get Animation Slide Down
	 */
	public static Animation getSlideDownAnimation()
	{
		Animation animation = AnimationUtils.loadAnimation(CTApplication.getContext(), R.anim.slide_down);
		return animation;
	}
	
	public static Animation inFromRightAnimation()
	{

		Animation inFromRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, +1.0f, Animation.RELATIVE_TO_PARENT,
		  0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromRight.setDuration(500);
		inFromRight.setInterpolator(new AccelerateInterpolator());
		return inFromRight;
	}

	public static Animation outToLeftAnimation()
	{
		Animation outtoLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
		  -1.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		outtoLeft.setDuration(500);
		outtoLeft.setInterpolator(new AccelerateInterpolator());
		return outtoLeft;
	}

	public static Animation inFromLeftAnimation()
	{
		Animation inFromLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1.0f, Animation.RELATIVE_TO_PARENT,
		  0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromLeft.setDuration(500);
		inFromLeft.setInterpolator(new AccelerateInterpolator());
		return inFromLeft;
	}

	public static Animation outToRightAnimation()
	{
		Animation outtoRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
		  +1.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		outtoRight.setDuration(500);
		outtoRight.setInterpolator(new AccelerateInterpolator());
		return outtoRight;
	}
}
