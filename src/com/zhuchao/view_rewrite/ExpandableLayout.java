package com.zhuchao.view_rewrite;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.luquba678.R;

public class ExpandableLayout extends LinearLayout {

	private Context mContext;
	private LinearLayout mHandleView;
	private LinearLayout mContentView;
	private ImageView mIconExpand;
	int mContentHeight = 0;
	int mTitleHeight = 0;
	private boolean isExpand;
	private Animation animationDown;
	private Animation animationUp;

	private int up_arrow,down_arrow;

	public ExpandableLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		initAttributes(attrs);
	}

	private void initAttributes(AttributeSet attributeSet){
		TypedArray array=getContext().obtainStyledAttributes(attributeSet,R.styleable.expandableLayout);
		up_arrow=array.getResourceId(R.styleable.expandableLayout_up_arrow,R.drawable.update_detail_up);
		down_arrow=array.getResourceId(R.styleable.expandableLayout_down_arrow,R.drawable.update_detail_down);
		isExpand=array.getBoolean(R.styleable.expandableLayout_is_expand,false);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (this.mContentHeight == 0) {
			this.mContentView.measure(widthMeasureSpec, 0);
			this.mContentHeight = this.mContentView.getMeasuredHeight();
		}
		if (this.mTitleHeight == 0) {
			this.mHandleView.measure(widthMeasureSpec, 0);
			this.mTitleHeight = this.mHandleView.getMeasuredHeight();
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
        this.mHandleView = (LinearLayout) this.findViewById(R.id.collapse_value);
        this.mContentView = (LinearLayout)this.findViewById(R.id.expand_value);
        this.mIconExpand = (ImageView) this.findViewById(R.id.icon_value);
        this.mHandleView.setOnClickListener(new ExpandListener());
        this.mContentView.setOnClickListener(new ExpandListener());
        if(!isExpand)
            mContentView.setVisibility(View.GONE);
	}
	private class ExpandListener implements OnClickListener {
		@Override
		public final void onClick(View paramView) {
			clearAnimation();
			Log.d("zhuchao", String.valueOf(mContentHeight));
			if (!isExpand) {
				if (animationDown == null) {
					animationDown = new DropDownAnim(mContentView, mContentHeight, true);
					animationDown.setDuration(200); // SUPPRESS CHECKSTYLE
				}
				startAnimation(animationDown);
				mContentView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.animalpha));
				mIconExpand.setImageResource(up_arrow);
				isExpand = true;
			} else {
				isExpand = false;
				if (animationUp == null) {
					animationUp = new DropDownAnim(mContentView, mContentHeight, false);
					animationUp.setDuration(200); // SUPPRESS CHECKSTYLE
				}
				startAnimation(animationUp);
				mIconExpand.setImageResource(down_arrow);
			}
		}
	}

	class DropDownAnim extends Animation {
		private int targetHeight;
		private View view;
		private boolean down;

		public DropDownAnim(View targetview, int vieweight, boolean isdown) {
			this.view = targetview;
			this.targetHeight = vieweight;
			this.down = isdown;
		}
		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			int newHeight;
			if (down) {
				newHeight = (int) (targetHeight * interpolatedTime);
			} else {
				newHeight = (int) (targetHeight * (1 - interpolatedTime));
			}
			view.getLayoutParams().height = newHeight;
			view.requestLayout();
			if (view.getVisibility() == View.GONE) {
				view.setVisibility(View.VISIBLE);
			}
		}

		@Override
		public void initialize(int width, int height, int parentWidth,
				int parentHeight) {
			super.initialize(width, height, parentWidth, parentHeight);
		}

		@Override
		public boolean willChangeBounds() {
			return true;
		}
	}
	public void onRefresh(){
        mContentView.measure(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        int height =mContentView.getMeasuredHeight();
        mContentHeight=height;
	}
}
