package performance.com.example.ndh.coordinatorlayout;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

@SuppressWarnings("unused")
public class CircleViewBehavior extends CoordinatorLayout.Behavior<CircleImageView> {
    private final Context mContext;
    private float mAvatarMaxSize;
    private float diffY;    // 移动过程中y方向上的偏移量
    private int toolbarHeight = 0;
    private int toolbarWidth = 0;
    private float circleImageOriginalY = 0; // 圆形图片的最初y方向上坐标
    private int circleImageEndHeight = 0;   // 圆形图片的最终大小
    private int circleImageOriginalHeight = 0; // 圆形图片的原始大小
    private float circleImageScale = 0; //  圆形图片的  最终大小同原始大小的比例
    private float maxLengthY = 0;  // y方向上最大移动距离
    private float maxLengthX = 0;  // x方向上的最大移动距离
    private float circleImageOriginalX = 0;  // 圆形图片的最初x方向上的坐标
    private float lastY;   // 记录上一次移动结束后y方向的坐标

    private boolean flagX = false; // 控制x方向上移动
    private boolean flagY = true;  //控制y方向上移动

    public CircleViewBehavior(Context context, AttributeSet attrs) {
        mContext = context;
    }


    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, CircleImageView child, View dependency) {
        // 依赖Appbar控件

        if (circleImageOriginalY == 0) {
            circleImageOriginalY = child.getY();
            circleImageOriginalX = child.getX();
        }
        if (circleImageOriginalHeight == 0) {
            circleImageOriginalHeight = child.getHeight();
        }
        if (toolbarHeight == 0 && dependency.getId() == R.id.toolbar && circleImageOriginalHeight != 0) {
            toolbarHeight = dependency.getHeight();
            toolbarWidth = dependency.getWidth();
            circleImageEndHeight = toolbarHeight * 2 / 3;
            circleImageScale = circleImageEndHeight * 1.0f / circleImageOriginalHeight;
            // 最终的圆形图片大小为toolbar 2/3高度， 为了最终显示在toolbar的正中间,因此 最终位置离toolbar顶部有1/6 toolbar高度的距离
            maxLengthY = circleImageOriginalY - toolbarHeight * 1.0f / 6;
            // 最终x方向的距离 大概在toolBar的1/4位置
            maxLengthX = circleImageOriginalX - toolbarWidth / 4 + circleImageEndHeight / 2;
        }
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, CircleImageView child, View dependency) {
        Log.d("ndh--", "child=" + child.getY() + "," + child.getScrollY() + "," + child.getTranslationY());
        Log.d("ndh--", "dependency=" + dependency.getY() + "," + dependency.getScrollY() + "," + dependency.getTranslationY());


        if (lastY == 0) {
            lastY = dependency.getY();
        }
        diffY = dependency.getY() - lastY;

        float offset = Math.abs(diffY);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        float diffScale = (child.getY() + diffY) / circleImageOriginalY;
        if (diffScale < circleImageScale) {
            params.height = params.width = (int) (circleImageOriginalHeight * circleImageScale);
        } else if (diffScale > 1) {
            params.height = params.width = (int) circleImageOriginalHeight;
        } else {
            params.height = params.width = (int) (diffScale * circleImageOriginalHeight);
        }
        child.setLayoutParams(params);
        // 因为最终的circleView高为toolBar的2/3  因此最终的Y轴最小为toolBar的1/6
        // y方向上的移动
        if (flagY) {
            if (child.getY() + diffY < toolbarHeight * 1.0f / 6) {
                child.setY(toolbarHeight * 1.0f / 6);
                flagX = true;
                flagY = false;
            } else if (child.getY() + diffY > circleImageOriginalY) {
                child.setY(circleImageOriginalY);
            } else {
                child.setY(child.getY() + diffY);
            }
        }
// x方向上的移动
        if (flagX) {
            if (child.getX() + diffY < toolbarWidth / 4 - circleImageEndHeight / 2) {
                // 最终距离在x方向1/4处
                child.setX(toolbarWidth / 4 - circleImageEndHeight / 2);
                // 这里因为 circleImage 的最终大小比原始大小 小了(circleImageOriginalHeight-circleImageEndHeight)，因此需要再向右边移动 大小差一般的距离，再向下移动
            } else if (child.getX() + diffY > circleImageOriginalX + (circleImageOriginalHeight - circleImageEndHeight) / 2) {
                child.setX(circleImageOriginalX + (circleImageOriginalHeight - circleImageEndHeight) / 2);
                flagY = true;
                flagX = false;
            } else {
                child.setX(child.getX() + diffY);
            }
        }
        lastY = dependency.getY();
        return true;
    }


    // 获取状态栏高度
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, CircleImageView child, View directTargetChild, View target, int nestedScrollAxes) {
        Log.d(TAG, "onStartNestedScroll-->");
        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onNestedScrollAccepted(CoordinatorLayout coordinatorLayout, CircleImageView child, View directTargetChild, View target, int nestedScrollAxes) {
        Log.d(TAG, "onNestedScrollAccepted-->");

        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, CircleImageView child, View target) {
        Log.d(TAG, "onStopNestedScroll-->");
        super.onStopNestedScroll(coordinatorLayout, child, target);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, CircleImageView child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.d(TAG, "onNestedScroll-->");
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, CircleImageView child, View target, int dx, int dy, int[] consumed) {
        Log.d(TAG, "onNestedPreScroll-->");
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, CircleImageView child, View target, float velocityX, float velocityY, boolean consumed) {
        Log.d(TAG, "onNestedFling-->");
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, CircleImageView child, View target, float velocityX, float velocityY) {
        Log.d(TAG, "onNestedPreFling-->");
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }


}