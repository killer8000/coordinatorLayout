package performance.com.example.ndh.coordinatorlayout;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by ndh on 17/4/25.
 */

public class Utils {
    public static void showToast(View view, String str) {
        Snackbar.make(view, str, Snackbar.LENGTH_SHORT).show();
    }
}
