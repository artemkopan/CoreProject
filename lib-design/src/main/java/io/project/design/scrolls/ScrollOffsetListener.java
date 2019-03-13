package io.project.design.scrolls;

import android.view.animation.Interpolator;

import com.google.android.material.appbar.AppBarLayout;

import androidx.core.widget.NestedScrollView;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.RecyclerView;

public abstract class ScrollOffsetListener extends RecyclerView.OnScrollListener
        implements NestedScrollView.OnScrollChangeListener, AppBarLayout.OnOffsetChangedListener {

    private static final FastOutSlowInInterpolator BASE_INTERPOLATOR = new FastOutSlowInInterpolator();

    protected Interpolator interpolator = BASE_INTERPOLATOR;
    protected float factor = 1f;

    public ScrollOffsetListener() {
    }

    public ScrollOffsetListener(float factor) {
        this.factor = factor;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    public void setFactor(float factor) {
        this.factor = factor;
    }

    @Override
    @SuppressWarnings("RestrictedApi")
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        calculateFraction(
                v.computeVerticalScrollOffset(),
                v.computeVerticalScrollRange(),
                v.computeVerticalScrollExtent());
    }

    @Override
    public void onScrolled(RecyclerView v, int dx, int dy) {
        calculateFraction(
                v.computeVerticalScrollOffset(),
                v.computeVerticalScrollRange(),
                v.computeVerticalScrollExtent());
    }

    @Override
    public void onOffsetChanged(AppBarLayout v, int verticalOffset) {
        calculateFraction(Math.abs(verticalOffset), v.getTotalScrollRange(), 0);
    }

    protected void calculateFraction(int offset, int range, int extent) {
        float f = offset / (float) (range - extent);
        fraction(interpolator.getInterpolation(f * factor));
    }

    public abstract void fraction(float f);

}


