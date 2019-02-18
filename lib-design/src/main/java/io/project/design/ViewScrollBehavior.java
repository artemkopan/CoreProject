package io.project.design;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.appbar.AppBarLayout;
import org.jetbrains.annotations.NotNull;

public class ViewScrollBehavior extends CoordinatorLayout.Behavior<View> {

    private int heightToOffset;

    public ViewScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ViewScrollBehavior);
        heightToOffset = ta.getDimensionPixelSize(R.styleable.ViewScrollBehavior_vsb_heightToOffset, 0);
        ta.recycle();
    }

    @Override
    public boolean layoutDependsOn(@NotNull CoordinatorLayout parent, @NotNull View view, @NotNull View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(@NotNull CoordinatorLayout parent, @NotNull View view, @NotNull View dependency) {
        if (dependency instanceof AppBarLayout) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
            int bottomMargin = lp.bottomMargin;
            int distanceToScroll = view.getHeight() + bottomMargin;
            float ratio = dependency.getY() / (float) heightToOffset;
            view.setTranslationY(-distanceToScroll * ratio);
        }
        return true;
    }
}