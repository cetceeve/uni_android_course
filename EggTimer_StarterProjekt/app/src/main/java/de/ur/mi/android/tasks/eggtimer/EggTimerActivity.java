package de.ur.mi.android.tasks.eggtimer;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import de.mi.eggtimer.R;
import de.ur.mi.android.tasks.eggtimer.debug.DebugHelper;
import de.ur.mi.android.tasks.eggtimer.listener.OnEggTimerStatusChangedListener;

// Interface implementieren und Methoden überschreiben
public class EggTimerActivity extends Activity implements OnEggTimerStatusChangedListener{

    private TextView timerView;
    private Spinner spinnerEggSize;
    private Spinner spinnerDoneness;
    private Button startAndStop;

    private EggTimerService myEggTimerService;
    private ServiceConnection serviceConnection;

    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_egg_timer);

        setupUI();
        createEggTimer();
        initServiceConnection();
    }

    private void createEggTimer() {
        // Es soll ein neues EggTimer-Objekt erstellt werden.
        int[] userInputs = getSelectedItemPositions();
        new EggTimer(this, userInputs[0], userInputs[1]);
    }


    private int[] getSelectedItemPositions() {
        int[] itemPositions = new int[2];

        // Zurückgeben eines Arrays, das die aktuellen Spinnerpositionen enthält.
        // Diese erhält man mit der Methode getSelectedItemPosition
        itemPositions[0] = spinnerEggSize.getSelectedItemPosition();
        itemPositions[1] = spinnerDoneness.getSelectedItemPosition();
        return itemPositions;
    }


    private void startEggTimerService() {
        int[] userInputs = getSelectedItemPositions();
        // Intent erstellen, der als Extras die Spinnerpositionen erhält
        // Die Methode startService mit diesem Intent als Parameter aufrufen.
        Intent eggTimerServiceIntent = new Intent(this, EggTimerService.class);
        eggTimerServiceIntent.putExtra(getString(R.string.eggSize), userInputs[0]);
        eggTimerServiceIntent.putExtra(getString(R.string.doneness), userInputs[1]);
        startService(eggTimerServiceIntent);
    }

    private void modifyButtonLayout(String caption, int colorId) {
        startAndStop.getBackground().setColorFilter(colorId, PorterDuff.Mode.MULTIPLY);
        startAndStop.setText(caption);
    }

    private void setupUI() {

        timerView = (TextView) findViewById(R.id.timerView);
        spinnerEggSize = (Spinner) findViewById(R.id.spinnerEggSize);
        spinnerDoneness = (Spinner) findViewById(R.id.spinnerDoneness);
        startAndStop = (Button) findViewById(R.id.button);

        initButton();
        initSpinner(spinnerEggSize, R.array.eggSizeArray);
        initSpinner(spinnerDoneness, R.array.donenessArray);
    }

    private void initButton() {

        modifyButtonLayout(getString(R.string.start), Color.GREEN);

        startAndStop.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                // Falls der Timer gerade läuft, soll dieser über die geeignete Methode des Services gestoppt werden.
                // Der Button soll mit der Methode modifyButtonLayout entsprechend verändert werden
                // Zuletzt soll ein neuer EggTimer instanziert werden
                if (isRunning) {
                    myEggTimerService.stopTimer();
                    modifyButtonLayout(getString(R.string.start), Color.GREEN);
                    new EggTimer(EggTimerActivity.this, getSelectedItemPositions()[0], getSelectedItemPositions()[1]);
                }

                // Falls der Timer nicht läuft, soll der Button verändert und ein Timer über den Service gestartet werden.
                if (!isRunning) {
                    modifyButtonLayout(getString(R.string.stop), Color.RED);
                    startEggTimerService();
                }

                // In beiden Fällen muss der Status, ob der Timer gerade läuft, verändert werden
                isRunning = !isRunning;

            }
        });

    }


    // diese Methode führt zweimal beinahe identischen Code für die zwei
    // SPinner aus und wurde deshalb parametrisiert (spinner und array id)
    private void initSpinner(Spinner spinner, int arrayID) {

        // Adaptersetup
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                EggTimerActivity.this, arrayID,
                android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Benötigten Listener Implementieren und die Methoden überschreiben
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View v,
                                       int position, long arg3) {

                if (!isRunning) {
                    // wenn der Timer nicht läuft, muss die Zeit für den neu
                    // ausgewählten wert immer neu berechnet werden ->
                    // hierzu wird immer ein Timerobjekt mit entsprechender Zeit erstellt
                    // dieses ist dann sofort bereit zum Start
                    createEggTimer();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

	private void initServiceConnection() {

		serviceConnection = new ServiceConnection() {

			@Override
			public void onServiceDisconnected(ComponentName name) {
				System.out.println("Service disconnected");
			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
                System.out.println("Service connected");

                myEggTimerService = ((EggTimerService.LocalBinder) service).getBinder();
                if (myEggTimerService != null) {
                    //Activity beim Service als OnEggTimerStatusChangedListener registrieren
                    myEggTimerService.setOnEggTimerStatusChangedListener(EggTimerActivity.this);
                }
            }
		};
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.egg_timer, menu);
        return true;
    }

    @Override
    protected void onPause() {
        DebugHelper.logDebugMessage("onPause");
        unbindService(serviceConnection);
        super.onPause();
        ActivityVisibilityState.setIsVisible(false);
    }

    @Override
    protected void onResume() {
        DebugHelper.logDebugMessage("onResume");
        bindService(new Intent(EggTimerActivity.this, EggTimerService.class), serviceConnection, BIND_AUTO_CREATE);
        super.onResume();
        ActivityVisibilityState.setIsVisible(true);
    }

    @Override
    protected void onDestroy() {
        DebugHelper.logDebugMessage("onDestroy");
        stopService(new Intent(EggTimerActivity.this, EggTimerService.class));
        super.onDestroy();
    }

    @Override
    public void onUpdateCountdownView(String toUpdate) {
        timerView.setText(toUpdate);
    }

    @Override
    public void onResetCountdownView(int stringID) {
        timerView.setText(getString(stringID));
    }

    @Override
    public void onEggTimerFinished() {
        Context context = getApplicationContext();
        CharSequence text = getString(R.string.notificationText);
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        timerView.setText(getString(R.string.standard));
        modifyButtonLayout(getString(R.string.start), Color.GREEN);
        isRunning = false;
    }
}
