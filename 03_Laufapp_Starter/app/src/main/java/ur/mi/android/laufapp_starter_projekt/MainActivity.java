package ur.mi.android.laufapp_starter_projekt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
    EditText textboxRundistance;
    EditText textboxRuntime;
    EditText textboxRunpause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textboxRundistance = (EditText) findViewById(R.id.textbox_rundistance);
        textboxRuntime = (EditText) findViewById(R.id.textbox_runtime);
        textboxRunpause = (EditText) findViewById(R.id.textbox_runpause);

        Button buttonCalculator = (Button) findViewById(R.id.button_calculator);
        buttonCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] values = getInputs();
                Intent runningIntent = new Intent(MainActivity.this, Results.class);
                runningIntent.putExtra(Constants.RUNNING_DISTANCE, values[0]);
                runningIntent.putExtra(Constants.RUNNING_TIME, values[1]);
                runningIntent.putExtra(Constants.RUNNING_PAUSE, values[2]);
                startActivity(runningIntent);
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
