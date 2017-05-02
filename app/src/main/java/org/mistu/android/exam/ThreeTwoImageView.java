package org.mistu.android.exam;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by kedee on 7/3/17.
 */

public class ThreeTwoImageView extends ImageView {

    public ThreeTwoImageView(Context context) {
        super(context);
    }

    public ThreeTwoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ThreeTwoImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int twoThirdHeight = MeasureSpec.getSize(widthMeasureSpec)*2 / 3;
        int threeTwoHeightSpec = MeasureSpec.makeMeasureSpec(twoThirdHeight, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, threeTwoHeightSpec);
    }
}
