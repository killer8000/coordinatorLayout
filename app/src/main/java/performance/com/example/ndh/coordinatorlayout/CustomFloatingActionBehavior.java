package performance.com.example.ndh.coordinatorlayout;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by ndh on 17/4/26.
 */

public class CustomFloatingActionBehavior extends FloatingActionButton.Behavior {
    private static final String TAG = "NDH1--";
    float lastY = 0;
    int originalSize = 0;
    int maxScrollRange = 0;
    private ValueAnimator mAnimator;
private AppBarLayout mAppBarLayout;
    public CustomFloatingActionBehavior(Context context, AttributeSet attrs) {
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        if (originalSize == 0 && dependency instanceof AppBarLayout) {
            originalSize = (child.getWidth() + child.getHeight()) / 2;
            maxScrollRange = ((AppBarLayout) dependency).getTotalScrollRange();
            mAppBarLayout= (AppBarLayout) dependency;
        }
        return dependency instanceof Snackbar.SnackbarLayout || dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        if (dependency instanceof AppBarLayout) {
            ((AppBarLayout) dependency).getTotalScrollRange();
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            layoutParams.width = (int) (originalSize * (Math.abs(dependency.getY()) * 1.0f / maxScrollRange));
            layoutParams.height = (int) (originalSize * (Math.abs(dependency.getY()) * 1.0f / maxScrollRange));
            child.setLayoutParams(layoutParams);
            child.setAlpha((Math.abs(dependency.getY()) * 1.0f / maxScrollRange));
            return true;
        } else {

            return super.onDependentViewChanged(parent, child, dependency);
        }

    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        Log.d(TAG, "onStartNestedScroll-->");
//        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != -1;//判断是否为垂直滚动

    }

    @Override
    public void onNestedScrollAccepted(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        Log.d(TAG, "onNestedScrollAccepted-->");

        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target) {
//        Log.d(TAG, "onStopNestedScroll-->");
//
//      if(Math.abs(offset)<mAppBarLayout.getTotalScrollRange()/2){
//          snap(mAppBarLayout,Math.abs(offset),0);
//      }else if(Math.abs(offset)>=mAppBarLayout.getTotalScrollRange()/2){
//          snap(mAppBarLayout,mAppBarLayout.getTotalScrollRange(),Math.abs(offset));
//      }
        super.onStopNestedScroll(coordinatorLayout, child, target);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.d(TAG, "onNestedScroll-->" + target.getClass().getName() + "," + dxConsumed + "," + dyConsumed + "," + dxUnconsumed + "," + dyUnconsumed);
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dx, int dy, int[] consumed) {
        Log.d(TAG, "onNestedPreScroll-->" + target.getClass().getName() + "," + dx + "," + dy + "," + consumed[0] + "," + consumed[1]);
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, 5, consumed);
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, float velocityX, float velocityY, boolean consumed) {
        Log.d(TAG, "onNestedFling-->");
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, float velocityX, float velocityY) {
        Log.d(TAG, "onNestedPreFling-->");
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }

    private void snap(View view, float start, float end) {
        mAnimator = ObjectAnimator
                .ofFloat(view, "translationY", start, end)//
                .setDuration(500);
        mAnimator.start();
    }
}
