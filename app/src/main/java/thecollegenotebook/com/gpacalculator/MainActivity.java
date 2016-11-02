package thecollegenotebook.com.gpacalculator;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.lang.annotation.Target;
import java.net.URI;
import java.util.ArrayList;
import java.util.InputMismatchException;


public class MainActivity extends AppCompatActivity {
    private ConstraintLayout constraintLayout; // creates variable for calls to activity_main
    private TextView textbox; // creates variable to interact with main output screen
    private double number = 0; // creates number variable to interact with most calculation functions
    private ArrayList<Double> group = new ArrayList<Double>(); // creates group array to assign unlimited number of gpa values
    private Vibrator vibe; // creates variable to interact with system vibrator
    private double hours; // creates variable to capture total number of credit hours
    private double finalHours;
    private int doneClicked; // creates a variable to monitor when done has been clicked so that a user cannot accidentally miscalculate
    private static double scale; // changes formula based on 4.0, 4.33, or 5.0 gpa scales
    private int userNumberNotified; // a variable to let the system know whether or not a user has already been notified of number range limits
    private int startMode;
    private int stage;
    private int clearCounter;
    private ShowcaseView showcaseView;
    private String letterGrade = "";
    private int calcContinue = 0;
    private int weightClicked = 0;
    private int startClicked = 0;
    private double finalResult = 0;
    private int vibeValue = 15;
    private int alphMode = 0;
    private Button alphButton;
    private double savedGPA;
    private String savedGPAString;
    private double savedHours;
    private String savedHoursString;
//    private Target t1, t2, t3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean firstTimeRun = getFirstTimeRun();

        if (firstTimeRun == true) {
            firstTimeRun();
            storeFirstTimeRun();
        } else {
            run();
        }

    }


    private void firstTimeRun() {
        setContentView(R.layout.activity_tutor);
        Intent intent = new Intent(this, TutorActivity.class);
        startActivity(intent);
    }

    private boolean getFirstTimeRun() {
        SharedPreferences prefs = getSharedPreferences("First Time Run Value", MODE_PRIVATE);
        boolean firstTimeRun = prefs.getBoolean("firstRun", true);
        return firstTimeRun;
    }

    private void storeFirstTimeRun() {
        SharedPreferences prefs = getSharedPreferences("First Time Run Value", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstRun", false);
        editor.commit();
    }

    private String getSavedGPA(){
        SharedPreferences prefs = getSharedPreferences("User Custom Data", MODE_PRIVATE);
        String gpaValue = prefs.getString("savedGPA", "null");
        return gpaValue;
    }

    private void storeSavedGPA(double value){
        SharedPreferences prefs = getSharedPreferences("User Custom Data", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("savedGPA", ("" + value));
        editor.commit();
    }

    private String getSavedHours(){
        SharedPreferences prefs = getSharedPreferences("User Custom Data", MODE_PRIVATE);
        String numOfHours = prefs.getString("savedHours", "null");
        return numOfHours;
    }

    private void storeSavedHours(double value){
        SharedPreferences prefs = getSharedPreferences("User Custom Data", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("savedHours", ("" + value));
        editor.commit();
    }


    public void run() {
        setContentView(R.layout.activity_main);

        // set coordinatorLayout of activity_main
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);

        // initialize textbox
        textbox = (TextView) findViewById(R.id.textView);
        alphButton = (Button) findViewById(R.id.buttonAlph);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // initialize vibrator service
        vibe = (Vibrator) getSystemService(VIBRATOR_SERVICE);


        // initializes hours variable to 0
        hours = 0.0;

        // initializes the doneClicked variable to 0
        doneClicked = 0;

        // initializes the scale variable to 4.0
//        scale = 4.0;

        // initializes the userNumberNotified variable to 0. 1 means that they have been notified
        userNumberNotified = 0;

        //initializes the startMode which starts in the normal mode
        startMode = 0;

        //initializes stage variable to 0
        stage = 0;

        //initializes the clearCounter variable to 0
        clearCounter = 0;

        // create weight spinner
        final Spinner spinner = (Spinner) findViewById(R.id.number_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.weight_spinner_values, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null){
            spinner.setSelection(getIntent().getExtras().getInt("spinnerHoursPos"));
        }else{
            spinner.setSelection(1);
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                vibe();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // create scale spinner
        final Spinner sc_spinner = (Spinner) findViewById(R.id.scale_spinner);
        ArrayAdapter<CharSequence> sc_adapter = ArrayAdapter.createFromResource(this, R.array.scale_spinner_values, R.layout.scale_spinner_item);
        sc_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sc_spinner.setAdapter(sc_adapter);
        if (extras != null){
            sc_spinner.setSelection(getIntent().getExtras().getInt("spinnerScalePos"));
        }else{
            sc_spinner.setSelection(0);
        }
        sc_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (weightClicked == 0) {
                    assignIntent();
                } else {
                    clearAll();
                }
                scale = Double.parseDouble(sc_spinner.getSelectedItem().toString());
                if (alphMode == 0){
                    assignValuesNum();
                }else if (alphMode == 1){
                    assignValuesAlph(scale);
                }
                weightClicked = 0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                weightClicked = 0;
            }
        });

        // create weight spinner
        final Spinner st_spinner = (Spinner) findViewById(R.id.start_spinner);
        ArrayAdapter<CharSequence> st_adapter = ArrayAdapter.createFromResource(this, R.array.start_spinner_values, R.layout.start_spinner_item);
        st_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        st_spinner.setAdapter(st_adapter);
//        startMode = st_spinner.getSelectedItemPosition();
        st_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                vibe();
//                Snackbar.make(coordinatorLayout, "Normal", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                startMode = st_spinner.getSelectedItemPosition();
                TextView startText = (TextView) findViewById(R.id.textViewStartMode);
                if (startClicked == 1) {
                    clearAll();
                    startClicked = 0;
                }
                if (startMode == 0) {
                    startText.setText("Normal");
                } else if (startMode == 1){
                    startText.setText("Current GPA");
//                    savedGPAString = getSavedGPA();
//                    if (savedGPAString.equals("null")){
//                        textbox.setText("Enter GPA");
//                    }else{
//                        try {
//                            savedGPA = Double.parseDouble(savedGPAString);
//                            number = savedGPA;
//                            savedHoursString = getSavedHours();
//                            if (!savedHoursString.equals("null")){
//                                try{
//                                    savedHours = Double.parseDouble(savedHoursString);
//                                    hours = savedHours;
//                                }catch (NumberFormatException e){
//                                    Snackbar.make(constraintLayout, "An error has occured. Please report this.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//                                    textbox.setText("Error");
//                                    throw new NumberFormatException();
//                                }
//                            }
//                            number = number * hours;
//                            group.add(number);
//                            number = 0;
//                            stage = 2;
//                            textbox.setText("Continue");
//                        }catch (NumberFormatException e){
//                            Snackbar.make(constraintLayout, "An error has occured. Please report this.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//                            textbox.setText("Error");
//                        }
//
//                    }
                    Button doneButton = (Button) findViewById(R.id.buttonDone);
                    doneButton.setText("Next");
//                    Snackbar.make(coordinatorLayout, "Current GPA", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                startClicked = 0;
            }
        });
    }

    public void assignIntent() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null){
            doneClicked = getIntent().getExtras().getInt("doneClicked");
            calcContinue = getIntent().getExtras().getInt("calcContinue");
            if (doneClicked == 1) {
                finalResult = getIntent().getExtras().getDouble("finalResult");
                letterGrade = letterGrader(finalResult, scale);
                textbox.setText("" + finalResult);
            } else if (calcContinue == 1) {
                group = (ArrayList<Double>) getIntent().getExtras().getSerializable("group");
                hours = getIntent().getExtras().getDouble("hours");
                Snackbar.make(constraintLayout, "Continue your calculation", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            } else {
                clearAll();
            }
        }else {
            clearAll();
        }
    }

    public void assignValuesNum() {
        ArrayList<Button> buttonList = new ArrayList<Button>();
        buttonList.add((Button) findViewById(R.id.button9));
        buttonList.add((Button) findViewById(R.id.button8));
        buttonList.add((Button) findViewById(R.id.button7));
        buttonList.add((Button) findViewById(R.id.button6));
        buttonList.add((Button) findViewById(R.id.button5));
        buttonList.add((Button) findViewById(R.id.button4));
        buttonList.add((Button) findViewById(R.id.button3));
        buttonList.add((Button) findViewById(R.id.button2));
        buttonList.add((Button) findViewById(R.id.button1));
        buttonList.add((Button) findViewById(R.id.buttonDot));
        buttonList.add((Button) findViewById(R.id.button0));

        ArrayList<Integer> attribute = new ArrayList<Integer>();
        attribute.add(9);
        ((Button) findViewById(R.id.button9)).setText("9");
        attribute.add(8);
        ((Button) findViewById(R.id.button8)).setText("8");
        attribute.add(7);
        ((Button) findViewById(R.id.button7)).setText("7");
        attribute.add(6);
        ((Button) findViewById(R.id.button6)).setText("6");
        attribute.add(5);
        ((Button) findViewById(R.id.button5)).setText("5");
        attribute.add(4);
        ((Button) findViewById(R.id.button4)).setText("4");
        attribute.add(3);
        ((Button) findViewById(R.id.button3)).setText("3");
        attribute.add(2);
        ((Button) findViewById(R.id.button2)).setText("2");
        attribute.add(1);
        ((Button) findViewById(R.id.button1)).setText("1");
        attribute.add(909);
        ((Button) findViewById(R.id.buttonDot)).setText(".");
        attribute.add(0);
        ((Button) findViewById(R.id.button0)).setText("0");


        for (int i = 0; i < buttonList.size(); i++) {
            buttonList.get(i).setTag(attribute.get(i));
        }
        alphButton.setText("ABC");
//        assignIntent();
    }

    public void assignValuesAlph(double value) {
        ArrayList<Button> buttonList = new ArrayList<Button>();
        buttonList.add((Button) findViewById(R.id.button9));
        buttonList.add((Button) findViewById(R.id.button8));
        buttonList.add((Button) findViewById(R.id.button7));
        buttonList.add((Button) findViewById(R.id.button6));
        buttonList.add((Button) findViewById(R.id.button5));
        buttonList.add((Button) findViewById(R.id.button4));
        buttonList.add((Button) findViewById(R.id.button3));
        buttonList.add((Button) findViewById(R.id.button2));
        buttonList.add((Button) findViewById(R.id.button1));
        buttonList.add((Button) findViewById(R.id.buttonDot));
        buttonList.add((Button) findViewById(R.id.button0));

        ArrayList<Double> attribute = new ArrayList<Double>();

        if (value == 4.0) {
            attribute.add(4.0);
            ((Button) findViewById(R.id.button9)).setText("A+/A");
            attribute.add(3.7);
            ((Button) findViewById(R.id.button8)).setText("A-");
            attribute.add(3.3);
            ((Button) findViewById(R.id.button7)).setText("B+");
            attribute.add(3.0);
            ((Button) findViewById(R.id.button6)).setText("B");
            attribute.add(2.7);
            ((Button) findViewById(R.id.button5)).setText("B-");
            attribute.add(2.3);
            ((Button) findViewById(R.id.button4)).setText("C+");
            attribute.add(2.0);
            ((Button) findViewById(R.id.button3)).setText("C");
            attribute.add(1.7);
            ((Button) findViewById(R.id.button2)).setText("C-");
            attribute.add(1.3);
            ((Button) findViewById(R.id.button1)).setText("D+");
            attribute.add(1.0);
            ((Button) findViewById(R.id.buttonDot)).setText("D");
            attribute.add(0.0);
            ((Button) findViewById(R.id.button0)).setText("E/F");
        } else if (value == 4.3) {
            attribute.add(4.3);
            ((Button) findViewById(R.id.button9)).setText("A+");
            attribute.add(4.0);
            ((Button) findViewById(R.id.button8)).setText("A");
            attribute.add(3.7);
            ((Button) findViewById(R.id.button7)).setText("A-");
            attribute.add(3.3);
            ((Button) findViewById(R.id.button6)).setText("B+");
            attribute.add(3.0);
            ((Button) findViewById(R.id.button5)).setText("B");
            attribute.add(2.7);
            ((Button) findViewById(R.id.button4)).setText("B-");
            attribute.add(2.3);
            ((Button) findViewById(R.id.button3)).setText("C+");
            attribute.add(2.0);
            ((Button) findViewById(R.id.button2)).setText("C");
            attribute.add(1.7);
            ((Button) findViewById(R.id.button1)).setText("D+");
            attribute.add(1.3);
            ((Button) findViewById(R.id.buttonDot)).setText("D");
            attribute.add(0.0);
            ((Button) findViewById(R.id.button0)).setText("F");
        } else {
            attribute.add(5.0);
            ((Button) findViewById(R.id.button9)).setText("A+");
            attribute.add(4.8);
            ((Button) findViewById(R.id.button8)).setText("A");
            attribute.add(4.6);
            ((Button) findViewById(R.id.button7)).setText("A-");
            attribute.add(4.4);
            ((Button) findViewById(R.id.button6)).setText("B+");
            attribute.add(4.2);
            ((Button) findViewById(R.id.button5)).setText("B");
            attribute.add(4.0);
            ((Button) findViewById(R.id.button4)).setText("B-");
            attribute.add(3.8);
            ((Button) findViewById(R.id.button3)).setText("C+");
            attribute.add(3.6);
            ((Button) findViewById(R.id.button2)).setText("C");
            attribute.add(3.4);
            ((Button) findViewById(R.id.button1)).setText("C-");
            attribute.add(3.0);
            ((Button) findViewById(R.id.buttonDot)).setText("C-2");
            attribute.add(0.0);
            ((Button) findViewById(R.id.button0)).setText("F");
        }

        for (int i = 0; i < buttonList.size(); i++) {
            buttonList.get(i).setTag(attribute.get(i));
        }
        alphButton.setText("123");
//        assignIntent();
    }

    //load splash screen
    public void splashClickListener() {
        Intent intent = new Intent(this, TutorActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }


    // creates a standard vibrator function
    public void vibe() {
        vibe.vibrate(vibeValue);
    }

    public void clearAll() {
        Button addButton = (Button) findViewById(R.id.buttonAdd);
        addButton.setText("+");
        textbox.setText("");
        if (startMode == 1) {
            textbox.setText("Enter GPA");
            Button doneButton = (Button) findViewById(R.id.buttonDone);
            doneButton.setText("Next");

        } else {
            Button doneButton = (Button) findViewById(R.id.buttonDone);
            doneButton.setText("Done");
        }
        number = 0;
        hours = 0;
        stage = 0;
        clearCounter = 0;
        calcContinue = 0;
        doneClicked = 0;
        finalResult = 0;
        finalHours = 0;
        group = new ArrayList<Double>();
        userNumberNotified = 0;
        letterGrade = "";
        Snackbar.make(constraintLayout, "New calculation started", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
        if (startMode == 1){
            textbox.setText("Enter GPA");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
        if (id == R.id.action_clear) {
            clearAll();
        } else if (id == R.id.action_feedback) {
            String[] addresses = {"solomonarnett@gmail.com"};
            composeEmail(addresses, "GPA Calculator Feedback");
        } else if (id == R.id.action_help) {
            splashClickListener();
        }
        return super.onOptionsItemSelected(item);
    }

    public void composeEmail(String[] addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public String textboxString(){
        return textbox.getText().toString();
    }

    public void numberClickHandler(View view) {
        Button button = (Button) (findViewById(view.getId()));
        String buttonTagString = button.getTag().toString();
//        Snackbar.make(coordinatorLayout, "You clicked " +button.getText(), Snackbar.LENGTH_SHORT).setAction("Action", null).show();
        clearCounter = 0;
        if (textbox != null) {
            if (doneClicked == 1) {
                doneClicked = 0;
                if (!textbox.getText().toString().isEmpty()) {
                    clearAll();
                }
            }

            if (startMode == 1) {
                if (stage == 0) {
                    if (textboxString().contains("Enter GPA")) {
                        textbox.setText("");
                    }
                } else if (stage == 1) {
                    if (textboxString().contains("Enter Hours")) {
                        textbox.setText("");
                    }
                } else if (stage == 2 || stage == 3) {
                    if (textboxString().contains("Continue")) {
                        textbox.setText("");
                    }
                }
            }


            //check for decimal entry
            // if decimal is pressed without number infront automatically enter 0 followed by decimal "."
            if (buttonTagString.contains("909")){ //button.getText().toString().contains(".")) {
                if (textboxString().isEmpty()) {
                    textbox.setText("0" + textboxString() + ".");
                    vibe();
                } else if (textboxString().contains(".")) {
                    Snackbar.make(constraintLayout, "You already entered a decimal", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    long[] pattern = {0, 30, 60, 30};
                    vibe.vibrate(pattern, -1);
                } else {
                    textbox.setText("" + textboxString() + ".");
                    vibe();
                }
            } else if ((startMode == 0 || (startMode == 1 && (stage == 3 || stage == 0))) && textboxString().isEmpty()) {
                if (scale == 4.0 && Double.parseDouble(buttonTagString) > 4.0) {
                    if (userNumberNotified != 1) {
                        Snackbar.make(constraintLayout, "Numbers greater than 4 will be converted", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                        userNumberNotified = 1;
                        long[] pattern = {0, 30, 60, 30};
                        vibe.vibrate(pattern, -1);
                    } else {
                        vibe();
                    }
                    if (alphMode == 1){
                        textbox.setText("" + buttonTagString);
                    }else if (alphMode == 0){
                        textbox.setText("" + textboxString() + buttonTagString);
                    }
                } else if (scale == 4.3 && Double.parseDouble(buttonTagString) > 4.3) {
                    if (userNumberNotified != 1) {
                        Snackbar.make(constraintLayout, "Numbers greater than 4.3 will be converted", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                        userNumberNotified = 1;
                        long[] pattern = {0, 30, 60, 30};
                        vibe.vibrate(pattern, -1);
                    } else {
                        vibe();
                    }
                    if (alphMode == 1){
                        textbox.setText("" + buttonTagString);
                    }else if (alphMode == 0){
                        textbox.setText("" + textboxString() + buttonTagString);
                    }
                } else if (scale == 5.0 && Double.parseDouble(buttonTagString) > 5.0) {
                    if (userNumberNotified != 1) {
                        Snackbar.make(constraintLayout, "Numbers greater than 5 will be converted", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                        userNumberNotified = 1;
                        long[] pattern = {0, 30, 60, 30};
                        vibe.vibrate(pattern, -1);
                    } else {
                        vibe();
                    }
                    if (alphMode == 1){
                        textbox.setText("" + buttonTagString);
                    }else if (alphMode == 0){
                        textbox.setText("" + textboxString() + buttonTagString);
                    }
                } else {
                    if (alphMode == 1){
                        textbox.setText("" + buttonTagString);
                    }else if (alphMode == 0){
                        textbox.setText("" + textboxString() + buttonTagString);
                    }
                    vibe();
                }
            } else {
                if (alphMode == 1){
                    textbox.setText("" + buttonTagString);
                }else if (alphMode == 0){
                    textbox.setText("" + textboxString() + buttonTagString);
                }
                vibe();
            }
        }
    }

    // clears textbox on CLR button press
    public void clearClickHandler(View view) {
        if (startMode == 1 && stage > 3) {
            clearAll();
        } else if (clearCounter < 1) {
            textbox.setText("");
            clearCounter += 1;
        } else {
            clearAll();
        }
        vibe();
    }

    // switches to AlphActivity
    public void abcClickHandler(View view) {
//        if (!textbox.getText().toString().isEmpty()) {
//            if (doneClicked != 1) {
//                addClickHandler(constraintLayout);
//            }
//        }
//        Intent intent = new Intent(this, AlphActivity.class);
//        intent.putExtra("doneClicked", doneClicked);
//        intent.putExtra("textBox", textbox.getText().toString());
//        intent.putExtra("letterGrade", letterGrade);
//        intent.putExtra("finalResult", finalResult);
//        final Spinner spinner = (Spinner) findViewById(R.id.number_spinner);
//        intent.putExtra("spinnerHoursPos", (Integer) spinner.getSelectedItemPosition());
//        final Spinner spinner_sc = (Spinner) findViewById(R.id.scale_spinner);
//        intent.putExtra("spinnerScalePos", (Integer) spinner_sc.getSelectedItemPosition());
//        intent.putExtra("hours", hours);
//        intent.putExtra("group", group);
//        if (hours > 0) {
//            calcContinue = 1;
//        }
//        intent.putExtra("calcContinue", calcContinue);
//        startActivity(intent);
//        overridePendingTransition(0, 0);
        if (startMode == 1  && stage == 1){

        }else{
            if(alphMode == 0){
                alphMode = 1;
                assignValuesAlph(scale);
            }else if(alphMode == 1){
                alphMode = 0;
                assignValuesNum();
            }
        }

        vibe();

    }



    // converts numbers higher than 4.33 to gpa values
    public double numConvert(double num) {
        if (num > scale) {
            if (userNumberNotified != 1) {
                Snackbar.make(constraintLayout, "Numbers greater than 4 will be converted", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                userNumberNotified = 1;
            }
        }
        if (scale == 4.0) {
            if (num > scale) {
                if (num > 65) {
                    if (num > 66) {
                        if (num > 69) {
                            if (num > 72) {
                                if (num > 76) {
                                    if (num > 79) {
                                        if (num > 82) {
                                            if (num > 86) {
                                                if (num > 89) {
                                                    if (num > 92) {
                                                        num = 4.0;
                                                        if (num > 96) {
                                                            letterGrade = "A+";
                                                        } else {
                                                            letterGrade = "A";
                                                        }
                                                    } else {
                                                        num = 3.7;
                                                        letterGrade = "A-";
                                                    }
                                                } else {
                                                    num = 3.3;
                                                    letterGrade = "B+";
                                                }
                                            } else {
                                                num = 3.0;
                                                letterGrade = "B";
                                            }
                                        } else {
                                            num = 2.7;
                                            letterGrade = "B-";
                                        }
                                    } else {
                                        num = 2.3;
                                        letterGrade = "C+";
                                    }
                                } else {
                                    num = 2.0;
                                    letterGrade = "C";
                                }
                            } else {
                                num = 1.7;
                                letterGrade = "C-";
                            }
                        } else {
                            num = 1.3;
                            letterGrade = "D+";
                        }
                    } else {
                        num = 1.0;
                        letterGrade = "D";
                    }
                } else {
                    num = 0.0;
                    letterGrade = "F";
                }
            }
        } else if (scale == 4.3) {
            if (num > scale) {
                if (num > 39) {
                    if (num > 44) {
                        if (num > 49) {
                            if (num > 54) {
                                if (num > 59) {
                                    if (num > 64) {
                                        if (num > 69) {
                                            if (num > 74) {
                                                if (num > 79) {
                                                    if (num > 89) {
                                                        num = 4.3;
                                                        letterGrade = "A+";
                                                    } else {
                                                        num = 4.0;
                                                        letterGrade = "A";
                                                    }
                                                } else {
                                                    num = 3.7;
                                                    letterGrade = "A-";
                                                }
                                            } else {
                                                num = 3.3;
                                                letterGrade = "B+";
                                            }
                                        } else {
                                            num = 3.0;
                                            letterGrade = "B";
                                        }
                                    } else {
                                        num = 2.7;
                                        letterGrade = "B-";
                                    }
                                } else {
                                    num = 2.3;
                                    letterGrade = "C+";
                                }
                            } else {
                                num = 2.0;
                                letterGrade = "C";
                            }
                        } else {
                            num = 1.7;
                            letterGrade = "D+";
                        }
                    } else {
                        num = 1.3;
                        letterGrade = "D";
                    }
                } else {
                    num = 0.0;
                    letterGrade = "F";
                }
            }
        } else if (scale == 5.0) {
            if (num > scale) {
                if (num > 69) {
                    if (num > 70) {
                        if (num > 72) {
                            if (num > 76) {
                                if (num > 79) {
                                    if (num > 82) {
                                        if (num > 86) {
                                            if (num > 89) {
                                                if (num > 92) {
                                                    if (num > 96) {
                                                        num = 5.0;
                                                        letterGrade = "A+";
                                                    } else {
                                                        num = 4.8;
                                                        letterGrade = "A";
                                                    }
                                                } else {
                                                    num = 4.6;
                                                    letterGrade = "A-";
                                                }
                                            } else {
                                                num = 4.4;
                                                letterGrade = "B+";
                                            }
                                        } else {
                                            num = 4.2;
                                            letterGrade = "B";
                                        }
                                    } else {
                                        num = 4.0;
                                        letterGrade = "B-";
                                    }
                                } else {
                                    num = 3.8;
                                    letterGrade = "C+";
                                }
                            } else {
                                num = 3.6;
                                letterGrade = "C";
                            }
                        } else {
                            num = 3.4;
                            letterGrade = "C-";
                        }
                    } else {
                        num = 3.0;
                        letterGrade = "C-";
                    }
                } else {
                    num = 0.0;
                    letterGrade = "F";
                }
            }

        }

        return num;
    }

    public static String letterGrader(double num, double scaleValue) {
        String numString = ((Double) num).toString();
        if (scaleValue == 4.0) {
            if (num >= 1.0) {
                if (num >= 1.3) {
                    if (num >= 1.7) {
                        if (num >= 2.0) {
                            if (num >= 2.3) {
                                if (num >= 2.7) {
                                    if (num >= 3.0) {
                                        if (num >= 3.3) {
                                            if (num >= 3.7) {
                                                if (num == 4.0){
                                                    return "A or A+";
                                                }else {
                                                    return "A-";
                                                }
                                            }else{
                                                return "B+";
                                            }
                                        } else {
                                            return "B";
                                        }
                                    } else {
                                        return "B-";
                                    }
                                } else {
                                    return "C+";
                                }
                            } else {
                                return "C";
                            }
                        } else {
                            return "C-";
                        }
                    } else {
                        return "D+";
                    }
                } else {
                    return "D";
                }
            } else {
                return "F";
            }
        } else if (scaleValue == 4.3) {
            if (num >= 1.3) {
                if (num >= 1.7) {
                    if (num >= 2.0) {
                        if (num >= 2.3) {
                            if (num >= 2.7) {
                                if (num >= 3.0) {
                                    if (num >= 3.3) {
                                        if (num >= 3.7) {
                                            if (num >= 4.0) {
                                                if (num == 4.3){
                                                    return "A+";
                                                } else {
                                                    return "A";
                                                }
                                            } else {
                                                return "A-";
                                            }
                                        } else {
                                            return "B+";
                                        }
                                    } else {
                                        return "B";
                                    }
                                } else {
                                    return "B-";
                                }
                            } else {
                                return "C+";
                            }
                        } else {
                            return "C";
                        }
                    } else {
                        return "D+";
                    }
                } else {
                    return "D";
                }
            } else {
                return "F";
            }
        } else if (scaleValue == 5.0) {
            if (num >= 3.0) {
                if (num >= 3.4) {
                    if (num >= 3.6) {
                        if (num >= 3.8) {
                            if (num >= 4.0) {
                                if (num >= 4.2) {
                                    if (num >= 4.4) {
                                        if (num >= 4.6) {
                                            if (num >= 4.8) {
                                                if (num == 5.0){
                                                    return "A+";
                                                } else {
                                                    return "A";
                                                }
                                            } else {
                                                return "A-";
                                            }
                                        } else {
                                            return "B+";
                                        }
                                    } else {
                                        return "B";
                                    }
                                } else {
                                    return "B-";
                                }
                            } else {
                                return "C+";
                            }
                        } else {
                            return "C";
                        }
                    } else {
                        return "C-";
                    }
                } else {
                    return "C-";
                }
            } else {
                return "F";
            }
        } else {
            return "";
        }
    }


    // adds value taken from textbox
    public void addClickHandler(View view) {
        Button addButton = (Button) findViewById(R.id.buttonAdd);
        if (addButton.getText().equals("Save")){
            storeSavedGPA(finalResult);
            storeSavedHours(finalHours);
            Snackbar.make(constraintLayout, "A GPA of " + finalResult + " over " + finalHours + " has been saved.", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            addButton.setText("+");
        }else{
            if (doneClicked == 1) {
                clearAll();
                doneClicked = 0;
                vibe();
            } else {
                if (startMode == 1 && stage < 2) {
                    doneClickHandler(constraintLayout);
                } else {
                    double spinner_value;
                    if (startMode == 0 || (startMode == 1 && stage == 3)) {
                        Spinner spinner = (Spinner) findViewById(R.id.number_spinner);
                        spinner_value = Double.parseDouble(spinner.getSelectedItem().toString());
                        if (!textbox.getText().toString().isEmpty()) {
                            try {
                                number = Double.parseDouble(textbox.getText().toString());
                            } catch (NumberFormatException e) {
                                Snackbar.make(constraintLayout, "Nothing to add", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                                long[] pattern = {0, 30, 60, 30};
                                vibe.vibrate(pattern, -1);
                                return;
                            }
                            number = numConvert(number);
                            hours += spinner_value;
                        }
                        vibe();
                    } else {
                        spinner_value = hours;
                        stage += 1;
                    }
//                System.out.println("The spinner value is " + spinner_value);
//                System.out.println("The current hours are " + hours);

                    number = number * spinner_value;
                    group.add(number);
                    number = 0;
                    textbox.setText("");

                }

            }
        }


    }

    // final calculation
// returns gpa value
    public void doneClickHandler(View view) {
        if (startMode == 1 && stage < 2) {
            if (stage == 0) {
                if (!(textbox.getText().toString().isEmpty())) {
                    try {
                        number = Double.parseDouble(textbox.getText().toString());
                    } catch (NumberFormatException e) {
                        Snackbar.make(constraintLayout, "Enter a GPA value before continuing", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                        long[] pattern = {0, 30, 60, 30};
                        vibe.vibrate(pattern, -1);
                        return;
                    }
                    number = numConvert(number);
                    stage += 1;
                    assignValuesNum();
                    alphMode = 0;
                    textbox.setText("Enter Hours");


                } else {
                    Snackbar.make(constraintLayout, "Enter a GPA value before continuing", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    textbox.setText("Enter GPA");
                }

            } else if (stage == 1) {
                if (!(textbox.getText().toString().isEmpty())) {
                    try {
                        hours = Double.parseDouble(textbox.getText().toString());
                    } catch (NumberFormatException e) {
                        Snackbar.make(constraintLayout, "Enter your total hours before continuing", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                        long[] pattern = {0, 30, 60, 30};
                        vibe.vibrate(pattern, -1);
                        return;
                    }
                    stage += 1;
                    Button doneButton = (Button) findViewById(R.id.buttonDone);
                    doneButton.setText("Done");
                    addClickHandler(constraintLayout);
                    textbox.setText("Continue");
                } else {
                    Snackbar.make(constraintLayout, "Enter a your total hours before continuing", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    textbox.setText("Enter Hours");
                }
            }
            vibe();
        }else if(startMode == 1 && stage == 4){
            clearAll();
        } else {

            if (!textbox.getText().toString().isEmpty()) {
                addClickHandler(constraintLayout);
            } else {
                vibe();
            }
            double sum = 0;
            for (int i = 0; i < group.size(); i++) {
                sum += group.get(i);
            }
            double result = sum / hours;
            result = (double) Math.round(result * 100) / 100;
            finalResult = result;
            finalHours = hours;
            letterGrade = letterGrader(finalResult, scale);
            textbox.setText("" + result);
            number = 0;
            hours = 0;
            group = new ArrayList<Double>();
            doneClicked = 1;
            calcContinue = 0;
            if (startMode == 1 && stage > 1) {
                if (stage > 2) {
                    Snackbar.make(constraintLayout, "Click CLR or DONE to start a new calculation.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//                    Button textButton = (Button) findViewById(R.id.buttonAdd);
//                    textButton.setText("Save");
                    stage += 1;

                } else if(stage > 3) {
//                    clearAll();
                    textbox.setText("Enter GPA");
//                    Snackbar.make(constraintLayout, "Reached this point", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }

        }
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
        weightClicked = 1;
        spinner.performClick();
        vibe();
    }

    public void onStartClickListener(View view) {
        Spinner spinner = (Spinner) findViewById(R.id.start_spinner);
        startClicked = 1;
        spinner.performClick();
        vibe();
    }
}
