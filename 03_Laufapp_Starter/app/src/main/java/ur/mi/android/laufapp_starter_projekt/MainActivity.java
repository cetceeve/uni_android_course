package ur.mi.android.laufapp_starter_projekt;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
    EditText textboxRundistance;
    EditText textboxRuntime;
    EditText textboxRunpause;
    TextView textViewPace;
    TextView textViewCals;
    Calculator calculator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textboxRundistance = (EditText) findViewById(R.id.textbox_rundistance);
        textboxRuntime = (EditText) findViewById(R.id.textbox_runtime);
        textboxRunpause = (EditText) findViewById(R.id.textbox_runpause);
        textViewPace = (TextView) findViewById(R.id.textview_pace);
        textViewCals = (TextView) findViewById(R.id.textview_cals);
        calculator = new Calculator();

        Button buttonCalculator = (Button) findViewById(R.id.button_calculator);
        buttonCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] values = getInputs();
                calculator.setValues(values[0], values[1], values[2]);
                String pace = Double.toString(calculator.calculatePace());
                String cals = Double.toString(calculator.calculateKcal());

                textViewPace.setText(pace);
                textViewCals.setText(cals);
            }
        });

    }

    private int[] getInputs() {
        String runDistanceString = textboxRundistance.getText().toString();
        String runTimeString = textboxRuntime.getText().toString();
        String runPauseString = textboxRunpause.getText().toString();

        int runDistance = Integer.parseInt(runDistanceString);
        int runTime = Integer.parseInt(runTimeString);
        int runPause = Integer.parseInt(runPauseString);

        return new int[]{runDistance, runTime, runPause};
    }


}
