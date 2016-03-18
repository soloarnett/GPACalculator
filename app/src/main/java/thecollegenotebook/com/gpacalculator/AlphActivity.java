package thecollegenotebook.com.gpacalculator;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AlphActivity extends AppCompatActivity {
    private CoordinatorLayout coordinatorLayout2;
    private TextView textbox;
    private double number = 0;
    private ArrayList<Double> group = new ArrayList<Double>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alph);
        coordinatorLayout2 = (CoordinatorLayout) findViewById(R.id.coordinator2);
        textbox = (TextView) findViewById(R.id.textView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList<Button> buttonList = new ArrayList<Button>();
        buttonList.add((Button) findViewById(R.id.buttonAp));
        buttonList.add((Button) findViewById(R.id.buttonA));
        buttonList.add((Button) findViewById(R.id.buttonAm));
        buttonList.add((Button) findViewById(R.id.buttonBp));
        buttonList.add((Button) findViewById(R.id.buttonB));
        buttonList.add((Button) findViewById(R.id.buttonBm));
        buttonList.add((Button) findViewById(R.id.buttonCp));
        buttonList.add((Button) findViewById(R.id.buttonC));
        buttonList.add((Button) findViewById(R.id.buttonCm));
        buttonList.add((Button) findViewById(R.id.buttonD));
        buttonList.add((Button) findViewById(R.id.buttonF));
        double attribute = 4.33;
        for (int i = 0; i < buttonList.size(); i++) {
            if (i == 3 || i == 6){
                attribute -= 0.01;
            }
            if (attribute < 1.67) {
                if (attribute < 1.0) {
                    attribute = 0;
                } else {
                    attribute = 1.0;
                }
            }
            buttonList.get(i).setTag(attribute);
            attribute -= 0.33;
            attribute = (double)Math.round(attribute*100)/100;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            Toast.makeText(MainActivity.this, "Your orientation is portrait", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(MainActivity.this, "Your orientation is landscape", Toast.LENGTH_SHORT).show();
        }
    }

    public void alphClickHandler(View view) {
        Button button = (Button) (findViewById(view.getId()));
//        Snackbar.make(coordinatorLayout, "You clicked " +button.getText(), Snackbar.LENGTH_SHORT).setAction("Action", null).show();
        if (textbox != null) {
            textbox.setText(button.getTag().toString());
        }
    }

    public void numClickHandler(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
    }

    public void clearClickHandler(View view) {
        textbox.setText("");
    }

    public void addClickHandler(View view) {
        try {
            number = Double.parseDouble(textbox.getText().toString());
        } catch (NumberFormatException e) {
            Snackbar.make(coordinatorLayout2, "Something went wrong.", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            return;
        }
        group.add(number);
        number = 0;
        textbox.setText("");

    }

    public void doneClickHandler(View view) {
        if (!textbox.getText().toString().isEmpty()) {
            try {
                number = Double.parseDouble(textbox.getText().toString());
            } catch (NumberFormatException e) {
                Snackbar.make(coordinatorLayout2, "Something went wrong.", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }
            group.add(number);
        }
        double sum = 0;
        for (int i = 0; i < group.size(); i++) {
            sum += group.get(i);
        }
        double result = sum / group.size();
        result = (double) Math.round(result * 100) / 100;
        textbox.setText("" + result);
        number = 0;
        group = new ArrayList<Double>();
    }

}
