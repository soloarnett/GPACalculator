package thecollegenotebook.com.gpacalculator;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by solomonarnett on 3/30/16.
 */
public class SwipeAdapter extends FragmentStatePagerAdapter {

    private ArrayList<String> fragmentString;
    private ArrayList<Integer> drawableString;
    public SwipeAdapter(FragmentManager fm) {
        super(fm);
    }

    public ArrayList<String> getMessages(){
        ArrayList<String> array = new ArrayList<String>();
        array.add("thank you for your support");
        array.add("in normal mode, you can simply enter your gpa value or class average");
        array.add("select the amount of hours each class is worth from\nthe CREDIT HOURS menu");
        array.add("4.0 not your scale?\n\nselect the right one");
        array.add("select \"Current GPA\" from the START menu to begin your calculation from your current average");
        array.add("there's even an Alpha Layout for when you don't know the numbers");
        array.add("all set?\n\ntap GO to get started");
        return array;
    }
    public ArrayList<Integer> getImages(){
        ArrayList<Integer> array = new ArrayList<Integer>();
        array.add(R.drawable.gpa1);
        array.add(R.drawable.gpa1);
        array.add(R.drawable.gpa2);
        array.add(R.drawable.gpa3);
        array.add(R.drawable.gpa4);
        array.add(R.drawable.gpa5);
        array.add(R.drawable.gpa1);
        return array;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new PageFragment();
        fragmentString = getMessages();
        drawableString = getImages();
        Bundle bundle = new Bundle();
        bundle.putString("message", fragmentString.get(i));
        bundle.putInt("count", i + 1);
        bundle.putInt("image", drawableString.get(i));

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return 7;

    }

}
