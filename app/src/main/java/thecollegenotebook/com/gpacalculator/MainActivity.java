package thecollegenotebook.com.gpacalculator;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private CoordinatorLayout coordinatorLayout; // creates variable for calls to activity_main
    private TextView textbox; // creates variable to interact with main output screen
    private double number = 0; // creates number variable to interact with most calculation functions
    private ArrayList<Double> group = new ArrayList<Double>(); // creates group array to assign unlimited number of gpa values
    private Vibrator vibe; // creates variable to interact with system vibrator
    private double hours; // creates variable to capture total number of credit hours
    private int doneClicked; // creates a variable to monitor when done has been clicked so that a user cannot accidentally miscalculate
    private double scale; // changes formula based on 4.0, 4.33, or 5.0 gpa scales
    private int userNumberNotified; // a variable to let the system know whether or not a user has already been notified of number range limits


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set coordinatorLayout of activity_main
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);

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

        // initializes the userNumberNotified variable to 0. 1 means that they have been notified
        userNumberNotified = 0;

        // create weight spinner
        final Spinner spinner = (Spinner) findViewById(R.id.number_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.weight_spinner_values, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vibe();
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
        sc_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textbox.setText("");
                number = 0;
                hours = 0;
                group = new ArrayList<Double>();
                userNumberNotified = 0;
                scale = Double.parseDouble(sc_spinner.getSelectedItem().toString());
                Snackbar.make(coordinatorLayout, "New calculation started", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                long[] pattern = {0, 30, 60, 30};
                vibe.vibrate(pattern, -1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



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
            textbox.setText("");
            number = 0;
            hours = 0;
            group = new ArrayList<Double>();
            userNumberNotified = 0;
        }else if (id == R.id.action_feedback) {
            String[] addresses = {"solomonarnett@gmail.com"};
            composeEmail(addresses, "GPA Calculator Feedback");
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

    public void numberClickHandler(View view) {
        Button button = (Button) (findViewById(view.getId()));
//        Snackbar.make(coordinatorLayout, "You clicked " +button.getText(), Snackbar.LENGTH_SHORT).setAction("Action", null).show();

        if (textbox != null) {
            if (doneClicked == 1) {
                doneClicked = 0;
                if (!textbox.getText().toString().isEmpty()) {
                    textbox.setText("");
                    Snackbar.make(coordinatorLayout, "New calculation started", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }
            }
            if (button.getText().toString().contains(".")) {
                if (textbox.getText().toString().isEmpty()) {
                    textbox.setText("0" + textbox.getText() + button.getText());
                    vibe();
                } else if (textbox.getText().toString().contains(".")) {
                    Snackbar.make(coordinatorLayout, "You already entered a decimal", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    long[] pattern = {0, 30, 60, 30};
                    vibe.vibrate(pattern, -1);
                } else {
                    textbox.setText("" + textbox.getText() + button.getText());
                    vibe();
                }
            } else if (textbox.getText().toString().isEmpty()) {
                if (scale == 4.0 && Double.parseDouble(button.getText().toString()) > 4.0) {
                    if (userNumberNotified != 1) {
                        Snackbar.make(coordinatorLayout, "Numbers greater than 4 will be converted", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                        userNumberNotified = 1;
                        long[] pattern = {0, 30, 60, 30};
                        vibe.vibrate(pattern, -1);
                    } else {
                        vibe();
                    }
                    textbox.setText("" + textbox.getText() + button.getText());
                } else if (scale == 4.3 && Double.parseDouble(button.getText().toString()) > 4.3) {
                    if (userNumberNotified != 1) {
                        Snackbar.make(coordinatorLayout, "Numbers greater than 4.3 will be converted", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                        userNumberNotified = 1;
                        long[] pattern = {0, 30, 60, 30};
                        vibe.vibrate(pattern, -1);
                    } else {
                        vibe();
                    }
                    textbox.setText("" + textbox.getText() + button.getText());
                } else if (scale == 5.0 && Double.parseDouble(button.getText().toString()) > 5.0) {
                    if (userNumberNotified != 1) {
                        Snackbar.make(coordinatorLayout, "Numbers greater than 5 will be converted", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                        userNumberNotified = 1;
                        long[] pattern = {0, 30, 60, 30};
                        vibe.vibrate(pattern, -1);
                    } else {
                        vibe();
                    }
                    textbox.setText("" + textbox.getText() + button.getText());
                } else {
                    textbox.setText("" + textbox.getText() + button.getText());
                    vibe();
                }
            } else {
                textbox.setText("" + textbox.getText() + button.getText());
                vibe();
            }
        }
    }

    // clears textbox on CLR button press
    public void clearClickHandler(View view) {
        textbox.setText("");
        vibe();

    }

    // switches to AlphActivity
    public void abcClickHandler(View view) {

        Intent intent = new Intent(this, AlphActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        vibe.vibrate(100);

    }

    // converts numbers higher than 4.33 to gpa values
    public double numConvert(double num) {
        if (num > scale){
            if (userNumberNotified != 1) {
                Snackbar.make(coordinatorLayout, "Numbers greater than 4 will be converted", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
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
                                                    } else {
                                                        num = 3.7;
                                                    }
                                                } else {
                                                    num = 3.3;
                                                }
                                            } else {
                                                num = 3.0;
                                            }
                                        } else {
                                            num = 2.7;
                                        }
                                    } else {
                                        num = 2.3;
                                    }
                                } else {
                                    num = 2.0;
                                }
                            } else {
                                num = 1.7;
                            }
                        } else {
                            num = 1.3;
                        }
                    } else {
                        num = 1.0;
                    }
                } else {
                    num = 0.0;
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
                                                    } else {
                                                        num = 4.0;
                                                    }
                                                } else {
                                                    num = 3.7;
                                                }
                                            } else {
                                                num = 3.3;
                                            }
                                        } else {
                                            num = 3.0;
                                        }
                                    } else {
                                        num = 2.7;
                                    }
                                } else {
                                    num = 2.3;
                                }
                            } else {
                                num = 2.0;
                            }
                        } else {
                            num = 1.7;
                        }
                    } else {
                        num = 1.3;
                    }
                } else {
                    num = 0.0;
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
                                                    } else {
                                                        num = 4.8;
                                                    }
                                                } else {
                                                    num = 4.6;
                                                }
                                            } else {
                                                num = 4.4;
                                            }
                                        } else {
                                            num = 4.2;
                                        }
                                    } else {
                                        num = 4.0;
                                    }
                                } else {
                                    num = 3.8;
                                }
                            } else {
                                num = 3.6;
                            }
                        } else {
                            num = 3.4;
                        }
                    } else {
                        num = 3.0;
                    }
                } else {
                    num = 0.0;
                }
            }

        }

        return num;
    }

    // adds value taken from textbox
    public void addClickHandler(View view) {
        if (doneClicked == 1) {
            Snackbar.make(coordinatorLayout, "New calculation started", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            long[] pattern = {0, 30, 60, 30};
            textbox.setText("");
            doneClicked = 0;
            vibe.vibrate(pattern, -1);
        } else {
            try {
                number = Double.parseDouble(textbox.getText().toString());
            } catch (NumberFormatException e) {
                Snackbar.make(coordinatorLayout, "Nothing to add", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                long[] pattern = {0, 30, 60, 30};
                vibe.vibrate(pattern, -1);
                return;
            }
            number = numConvert(number);
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

    // final calculation
    // returns gpa value
    public void doneClickHandler(View view) {
        if (!textbox.getText().toString().isEmpty()) {
            addClickHandler(coordinatorLayout);
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
