package thecollegenotebook.com.gpacalculator;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class PageFragment extends Fragment {
    TextView textView;
    TextView swipeView;
    ImageView imageView;

    public PageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.page_fragment_layout, container, false);
        textView = (TextView) view.findViewById(R.id.textView);
        swipeView = (TextView) view.findViewById(R.id.textView4);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        Bundle bundle = getArguments();
        String count = Integer.toString(bundle.getInt("count"));
        if (count.contains("7")){
            Button button = (Button) view.findViewById(R.id.button10);
            button.setVisibility(View.VISIBLE);
        }else{
            Button button = (Button) view.findViewById(R.id.button10);
            button.setVisibility(View.GONE);
        }
        if (!count.contains("1")){
            swipeView.setVisibility(View.GONE);
        }else{
            swipeView.setVisibility(View.VISIBLE);
        }
        String message = bundle.getString("message");
        Integer image = bundle.getInt("image");
        textView.setText(message);
        imageView.setImageResource(image);
//        imageView.setImageDrawable(Drawable.createFromPath("@drawable/gpa1"));
        return view;
    }



}
