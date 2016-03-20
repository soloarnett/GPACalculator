package thecollegenotebook.com.gpacalculator;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AlphActivity extends AppCompatActivity {
    private CoordinatorLayout coordinatorLayout2;
    private TextView textbox;
    private double number = 0;
    private ArrayList<Double> group = new ArrayList<Double>();
    private Vibrator vibe; // creates variable to interact with system vibrator
    private double hours; // creates variable to capture total number of credit hours
    private int doneClicked; // creates a variable to monitor when done has been clicked so that a user cannot accidentally miscalculate
    private double scale; // changes formula based on 4.0, 4.33, or 5.0 gpa scales

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alph);
        coordinatorLayout2 = (CoordinatorLayout) findViewById(R.id.coordinator2);

        // initialize textbox
        textbox = (TextView) findViewById(R.id.textView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // initialize vibrator service
        vibe = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        // initializes hours variable to 0
        hours = 0.0;

        // initializes the doneClicked variable to 0
        doneClicked = 0;

        // initializes the scale variable to 4.0
        scale = 4.0;

        // create weight spinner
        final Spinner spinner = (Spinner) findViewById(R.id.number_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.weight_spinner_values, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // create scale spinner
        final Spinner sc_spinner = (Spinner) findViewById(R.id.scale_spinner);
        ArrayAdapter<CharSequence> sc_adapter = ArrayAdapter.createFromResource(this, R.array.scale_spinner_values, R.layout.scale_spinner_item);
        sc_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sc_spinner.setAdapter(sc_adapter);
        sc_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textbox.setText("");
                number = 0;
                hours = 0;
                group = new ArrayList<Double>();
                scale = Double.parseDouble(sc_spinner.getSelectedItem().toString());
                assignValues(scale);
                Snackbar.make(coordinatorLayout2, "New calculation started", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                long[] pattern = {0, 30, 60, 30};
                vibe.vibrate(pattern, -1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void assignValues(double value){
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

        ArrayList<Double> attribute = new ArrayList<Double>();

        if (value == 4.0){
            attribute.add(4.0);
            attribute.add(4.0);
            attribute.add(3.7);
            attribute.add(3.3);
            attribute.add(3.0);
            attribute.add(2.7);
            attribute.add(2.3);
            attribute.add(2.0);
            attribute.add(1.7);
            attribute.add(1.0);
            attribute.add(0.0);
        }else if(value == 4.3){
            attribute.add(4.3);
            attribute.add(4.0);
            attribute.add(3.7);
            attribute.add(3.3);
            attribute.add(3.0);
            attribute.add(2.7);
            attribute.add(2.3);
            attribute.add(2.0);
            attribute.add(1.7);
            attribute.add(1.0);
            attribute.add(0.0);
        }else{
            attribute.add(5.0);
            attribute.add(4.8);
            attribute.add(4.6);
            attribute.add(4.4);
            attribute.add(4.2);
            attribute.add(4.0);
            attribute.add(3.8);
            attribute.add(3.6);
            attribute.add(3.4);
            attribute.add(3.0);
            attribute.add(0.0);
        }

        for (int i = 0; i < buttonList.size(); i++) {
            buttonList.get(i).setTag(attribute.get(i));
        }
    }

    // creates a standard vibrator function
    public void vibe() {
        vibe.vibrate(30);
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
            if (doneClicked == 1) {
                doneClicked = 0;
                if (!textbox.getText().toString().isEmpty()) {
                    textbox.setText("");
                    Snackbar.make(coordinatorLayout2, "New calculation started", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }
            }
            textbox.setText(button.getTag().toString());
        }
        vibe();
    }

    public void numClickHandler(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        vibe.vibrate(100);
    }

    public void clearClickHandler(View view) {
        textbox.setText("");
        vibe();
    }

    public void addClickHandler(View view) {
        if (doneClicked == 1) {
            Snackbar.make(coordinatorLayout2, "New calculation started", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            long[] pattern = {0, 30, 60, 30};
            textbox.setText("");
            doneClicked = 0;
            vibe.vibrate(pattern, -1);
        } else {
            try {
                number = Double.parseDouble(textbox.getText().toString());
            } catch (NumberFormatException e) {
                Snackbar.make(coordinatorLayout2, "Nothing to add", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                long[] pattern = {0, 30, 60, 30};
                vibe.vibrate(pattern, -1);
                return;
            }
            Spinner spinner = (Spinner) findViewById(R.id.number_spinner);
            double spinner_value = Double.parseDouble(spinner.getSelectedItem().toString());
            number = number * spinner_value;
            hours += spinner_value;
            group.add(number);
            number = 0;
            textbox.setText("");
            vibe();
        }

    }

    public void doneClickHandler(View view) {
        if (!textbox.getText().toString().isEmpty()) {
            addClickHandler(coordinatorLayout2);
        }
        double sum = 0;
        for (int i = 0; i < group.size(); i++) {
            sum += group.get(i);
        }
        double result = sum / hours;
        result = (double) Math.round(result * 100) / 100;
        textbox.setText("" + result);
        number = 0;
        hours = 0;
        group = new ArrayList<Double>();
        doneClicked = 1;
        vibe();
    }

    // spinner on click listener for CHANGE WEIGHT button below textbox
    public void onClickWeightListener(View view) {
        Spinner spinner = (Spinner) findViewById(R.id.number_spinner);
        spinner.performClick();
        vibe();
    }

    // spinner on click listener for SCALE button below textbox
    public void onClickScaleListener(View view) {
        Spinner spinner = (Spinner) findViewById(R.id.scale_spinner);
        spinner.performClick();
        vibe();
    }

}
