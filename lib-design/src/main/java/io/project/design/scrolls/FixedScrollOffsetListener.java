package io.project.design.scrolls;


import androidx.annotation.FloatRange;

public abstract class FixedScrollOffsetListener extends ScrollOffsetListener {

    private final float spaceToOffset;

    protected FixedScrollOffsetListener(@FloatRange(from = 0.0) float spaceToOffset) {
        super();
        this.spaceToOffset = spaceToOffset;
    }

    public FixedScrollOffsetListener(int spaceToOffset, float factor) {
        super(factor);
        this.spaceToOffset = spaceToOffset;
    }

    @Override
    protected void calculateFraction(int offset, int range, int extent) {
        if (spaceToOffset < 0) {
            return;
        }
        float f = offset / spaceToOffset;
        fraction(interpolator.getInterpolation(f * factor));
    }
}
