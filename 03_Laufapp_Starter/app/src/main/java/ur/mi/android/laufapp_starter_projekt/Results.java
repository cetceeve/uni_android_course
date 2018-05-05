package ur.mi.android.laufapp_starter_projekt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Results extends Activity {
    TextView textViewPace;
    TextView textViewCals;
    Calculator calculator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        textViewPace = (TextView) findViewById(R.id.textview_pace);
        textViewCals = (TextView) findViewById(R.id.textview_cals);
        calculator = new Calculator();

        Intent i = getIntent();
        Bundle extras = i.getExtras();

        int runningDistance = extras.getInt(Constants.RUNNING_DISTANCE);
        int runningTime = extras.getInt(Constants.RUNNING_TIME);
        int runningPause = extras.getInt(Constants.RUNNING_PAUSE);

        calculator.setValues(runningDistance, runningTime, runningPause);
        String pace = Double.toString(calculator.calculatePace());
        String cals = Double.toString(calculator.calculateKcal());

        textViewPace.setText(pace);
        textViewCals.setText(cals);
    }
}
