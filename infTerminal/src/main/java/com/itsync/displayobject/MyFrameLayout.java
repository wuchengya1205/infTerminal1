package com.itsync.displayobject;



import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;

public class MyFrameLayout extends FrameLayout {

	ObjectAnimator objectAnimator;
	private int _currentViewIndex;
	public MyFrameLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
	}

	public MyFrameLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MyFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void addView(View child) {
		// TODO Auto-generated method stub
		child.setVisibility(View.INVISIBLE);
		super.addView(child);
	}
	
	public ObjectAnimator CrawlImageDoAnimate(int i, int StartValue, int EndValue) {
		_currentViewIndex = i;
		getChildAt(i).setVisibility(View.VISIBLE);
		objectAnimator = ObjectAnimator.ofFloat(getChildAt(i), "translationY", StartValue,EndValue);
		return ObjectAnimator.ofFloat(getChildAt(i), "translationY", StartValue,EndValue);
		
	}
	
	public ObjectAnimator NormalImageDoAnimate(int i) {
		getChildAt(i).setVisibility(View.VISIBLE);
		objectAnimator = ObjectAnimator.ofFloat(getChildAt(i), "alpha", 0,1);
		return ObjectAnimator.ofFloat(getChildAt(i), "alpha", 0,1);
	}

	
	@Override
	protected void onVisibilityChanged(View changedView, int visibility) {
		// TODO Auto-generated method stub
		super.onVisibilityChanged(changedView, visibility);
		if (visibility != VISIBLE){
			View childAt = getChildAt(_currentViewIndex);
			Animation animation = childAt.getAnimation();
			animation.cancel();
			}else{
			View childAt = getChildAt(_currentViewIndex);
			childAt.getAnimation().reset();
			childAt.getAnimation().start();
			}
	}
}
