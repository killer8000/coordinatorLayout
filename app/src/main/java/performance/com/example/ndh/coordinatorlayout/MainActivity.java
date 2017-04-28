package performance.com.example.ndh.coordinatorlayout;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final Toolbar toolbarSearch = (Toolbar) findViewById(R.id.toolbar_search);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBr);
        final ImageView search = (ImageView) findViewById(R.id.search);
        final ImageView back = (ImageView) findViewById(R.id.back);
        final ImageView more = (ImageView) findViewById(R.id.more);
        final ImageView back_search =(ImageView)findViewById(R.id.back_search);
        back_search.setOnClickListener(this);
        search.setOnClickListener(this);
        more.setOnClickListener(this);
        back.setOnClickListener(this);
        setSupportActionBar(toolbar);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                // 展开状态
                if (verticalOffset == 0) {
                    toolbar.setAlpha(0);
                    toolbarSearch.setAlpha(1);
                } else if (Math.abs(verticalOffset * 1.0f) >= appBarLayout.getTotalScrollRange()) {
                    //收缩状态
                    toolbar.setAlpha(1);
                    toolbarSearch.setAlpha(0);
                    toolbarSearch.setVisibility(View.GONE);
                } else {
                    //滑动状态
                    toolbarSearch.setVisibility(View.VISIBLE);
                    float alpha = Math.abs(verticalOffset * 1.0f) / appBarLayout.getTotalScrollRange();
                    Log.d("ndh--", "alpha=" + alpha);
                    toolbar.setAlpha(alpha);
                    toolbarSearch.setAlpha(1 - alpha);
                }
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.showToast(view,"click fat button");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    public static int dip2px(Context context, float dpValue) {

        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (dpValue * scale + 0.5f);

    }

    @Override
    public void onClick(View v) {
        String str = null;
        switch (v.getId()) {
            case R.id.back:
                str = "back button click";
                break;
            case R.id.search:
                str = "search button click";
                break;
            case R.id.more:
                str = "more button click";
            case R.id.back_search:
                str = "search back button click";
                break;
        }
        Snackbar.make(v, str, Snackbar.LENGTH_SHORT).show();
    }
}
