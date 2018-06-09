package student_1625111047.androidtest_shakerecoder;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity implements SensorEventListener {


    private SensorManager sensorManager;

    private Vibrator vibrator;
    private TextView textView;

    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;


    public void stop(View view){
        mediaRecorder.stop();
        textView.setVisibility(View.GONE);
        mediaRecorder.release();
        Toast.makeText(MainActivity.this, "录制完成", LENGTH_SHORT).show();
    }

    public void play(View view){
        try {
            String path= Environment.getExternalStorageDirectory().
                    getAbsolutePath()+"/ShakeRecords.amr";
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=(TextView) findViewById(R.id.TextView_0);
        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator=(Vibrator) getSystemService(VIBRATOR_SERVICE);
        mediaPlayer=new MediaPlayer();
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile
                (Environment.getExternalStorageDirectory().
                        getAbsolutePath()+"/ShakeRecords.amr");
        try {
            mediaRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume(){
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
        super.onResume();
    }

    @Override
    protected void onStop(){
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                if(Math.abs(event.values[0])>15||Math.abs(event.values[1])>15||Math.abs(event.values[2])>15){
                    vibrator.vibrate(100);
                    textView.setText("开始录制中...");
                    Toast.makeText(MainActivity.this, "开始录制", LENGTH_SHORT).show();
                    mediaRecorder.start();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }
}
