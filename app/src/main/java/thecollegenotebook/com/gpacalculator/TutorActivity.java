package thecollegenotebook.com.gpacalculator;


import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class TutorActivity extends FragmentActivity {

    private ViewPager viewPager;
    private final static int NUM_PAGES = 6;
    private ArrayList<ImageView> dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor);
        viewPager = (ViewPager) findViewById(R.id.pager);
        SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager());
        viewPager.setAdapter(swipeAdapter);

//        addDots();


    }

    public void buttonClickListener(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

//    public void addDots() {
//        dots = new ArrayList<>();
//        LinearLayout dotsLayout = (LinearLayout)viewPager.findViewById(R.id.dots);
//
//        for (int i = 0; i < NUM_PAGES; i++) {
//            ImageView dot = new ImageView(this);
//            dot.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.pager_dot_not_selected));
//
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//            );
//            dotsLayout.addView(dot, params);
//
//            dots.add(dot);
//        }
//
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                selectDot(position);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//            }
//        });
//    }
//
//    public void selectDot(int idx) {
//        Resources res = getResources();
//        for (int i = 0; i < NUM_PAGES; i++) {
//            int drawableId = (i == idx) ? (R.drawable.pager_dot_selected) : (R.drawable.pager_dot_not_selected);
//            Drawable drawable = ContextCompat.getDrawable(this,drawableId);
//            dots.get(i).setImageDrawable(drawable);
//        }
//
//
//    }
}

