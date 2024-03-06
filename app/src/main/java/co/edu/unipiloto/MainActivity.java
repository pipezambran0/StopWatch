package co.edu.unipiloto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button btnStart, btnStop, btnRestart;
    private int seconds = 0;
    private boolean running, wasRunning;
    private static List<String> laps = new ArrayList<>(); // Lista para almacenar las vueltas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);
        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
        }
        runTimer();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("seconds", seconds);
        savedInstanceState.putBoolean("running", running);
        savedInstanceState.putBoolean("wasRunning", wasRunning);
    }

    protected void onPause() {
        super.onPause();
        wasRunning = running;
        running = false;
    }

    protected void onResume() {
        super.onResume();
        if (wasRunning) {
            running = true;
        }
    }

    public void runTimer() {
        final TextView timeView = (TextView) findViewById(R.id.secView);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int sec = seconds % 60;

                String time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, sec);

                timeView.setText(time);

                if (running) {
                    seconds++;
                }
                handler.postDelayed(this, 1000);
            }
        });

    }

    public void currentTime(View view) {
        TextView txtCurrent = (TextView) findViewById(R.id.current);
        int currentSeconds = seconds; // Tomar una copia del tiempo actual

        int hours = currentSeconds / 3600;
        int minutes = (currentSeconds % 3600) / 60;
        int sec = currentSeconds % 60;

        String time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, sec);
        laps.add(time); // Agregar la vuelta actual a la lista
        updateLapsTextView(txtCurrent);
    }

    private void updateLapsTextView(TextView textView) {
        StringBuilder lapsText = new StringBuilder();
        int lapNumber = 1;
        for (String lap : laps) {
            lapsText.append("Lap ").append(lapNumber).append(" : ").append(lap).append("\n");
            lapNumber++;
        }
        textView.setText(lapsText.toString());
    }

    public void onClickStart(View view) {
        running = true;
    }

    public void onClickStop(View view) {
        running = false;
    }

    public void onClickReset(View view) {
        running = false;
        seconds = 0;
        laps.clear(); // Limpiar la lista de vueltas al reiniciar
        TextView txtCurrent = findViewById(R.id.current);
        txtCurrent.setText(""); // Limpiar el TextView de vueltas al reiniciar
    }
}