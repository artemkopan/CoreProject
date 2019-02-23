package io.project.core.ui.widgets;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Checkable;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import io.project.core.ui.R;


public class ProgressButtonView extends AppCompatButton implements Checkable {

    private static final String TAG = "ProgressButtonView";
    private static final int DEFAULT_BORDER_WIDTH = 10;
    private static final int DEFAULT_PROGRESS_COLOR = Color.BLACK;
    private static final int DEFAULT_ANIM_DURATION = 150;


    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };

    private CircularProgressDrawable progressDrawable;
    private ValueAnimator backgroundAnimator;
    private int animDuration;
    private int progressSize, progressPadding;
    private boolean showProgress, drawProgress;
    private boolean checked;

    public ProgressButtonView(Context context) {
        this(context, null);
    }

    public ProgressButtonView(Context context, AttributeSet attrs) {
        this(context, attrs, androidx.appcompat.R.attr.buttonStyle);
    }

    public ProgressButtonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        int color = DEFAULT_PROGRESS_COLOR;
        int borderWidth = DEFAULT_BORDER_WIDTH;
        animDuration = DEFAULT_ANIM_DURATION;

        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.ProgressButtonView);

            try {
                color = array.getColor(R.styleable.ProgressButtonView_pbv_progressColor, color);
                borderWidth = array.getDimensionPixelSize(
                        R.styleable.ProgressButtonView_pbv_progressBorderWidth,
                        borderWidth);

                progressSize = array.getDimensionPixelSize(R.styleable.ProgressButtonView_pbv_progressSize,
                        progressSize);
                progressPadding = array.getDimensionPixelSize(
                        R.styleable.ProgressButtonView_pbv_progressPadding,
                        progressPadding);
            } finally {
                array.recycle();
            }
        }

        progressDrawable = new CircularProgressDrawable(color, borderWidth);
        progressDrawable.setCallback(this);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            int w = getWidth(), h = getHeight();

            if (progressSize == 0) progressSize = w > h ? h : w;

            int sizeHalfProgress = (progressSize - progressPadding) / 2;

            progressDrawable.setBounds(w / 2 - sizeHalfProgress, h / 2 - sizeHalfProgress,
                    w / 2 + sizeHalfProgress, h / 2 + sizeHalfProgress);
        }
    }

    public void showProgress(final boolean show) {
        showProgress(show, true);
    }

    public void showProgress(final boolean show, boolean animate) {
        Log.d(TAG, "showProgress: is show " + show);
        if (show == showProgress) {
            return;
        }

        showProgress = show;
        setClickable(!show);

        if (animate && getBackground() != null) {
            showProgressAnim();
            return;
        }

        showProgressWithoutAnim();
    }

    public boolean isShowProgress() {
        return showProgress;
    }

    private void showProgressAnim() {
        if (backgroundAnimator == null) {
            backgroundAnimator = ObjectAnimator.ofInt(255, 0);
            backgroundAnimator.setDuration(animDuration);
            backgroundAnimator.setInterpolator(new LinearOutSlowInInterpolator());
            backgroundAnimator.addUpdateListener(new AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    getBackground().setAlpha(value);

                    if ((showProgress && value == 0) || (!showProgress && value == 255)) {
                        showProgressWithoutAnim();
                    }
                }
            });
            backgroundAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationCancel(Animator animation) {
                    showProgressWithoutAnim();
                }
            });
        }

        if (showProgress) {
            backgroundAnimator.start();
        } else {
            backgroundAnimator.reverse();
        }
    }

    private void showProgressWithoutAnim() {
        if (showProgress) {
            drawProgress = true;
            progressDrawable.start();
        } else {
            drawProgress = false;
            progressDrawable.stop();
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (showProgress && drawProgress) {
            progressDrawable.draw(canvas);
        } else {
            super.onDraw(canvas);
        }
    }

    @Override
    protected boolean verifyDrawable(@NonNull Drawable who) {
        return who == progressDrawable || super.verifyDrawable(who);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        //begin boilerplate code that allows parent classes to save status
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);
        ss.showProgress = showProgress;
        ss.drawProgress = drawProgress;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            SavedState ss = (SavedState) state;
            super.onRestoreInstanceState(ss.getSuperState());
            drawProgress = ss.drawProgress;
            showProgress(ss.showProgress);
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void setChecked(boolean checked) {
        if (this.checked != checked) {
            this.checked = checked;
            refreshDrawableState();
        }
    }

    @Override
    public void toggle() {
        setChecked(!checked);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    static class SavedState extends View.BaseSavedState {

        //required field that makes Parcelables from a Parcel
        public static final Creator<SavedState> CREATOR =
                new Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
        boolean showProgress, drawProgress;

        SavedState(Parcel source) {
            super(source);
            this.showProgress = source.readByte() != 0;
            this.drawProgress = source.readByte() != 0;
        }

        private SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeByte(this.showProgress ? (byte) 1 : (byte) 0);
            dest.writeByte(this.drawProgress ? (byte) 1 : (byte) 0);
        }
    }

}