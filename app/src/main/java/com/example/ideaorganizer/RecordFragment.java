package com.example.ideaorganizer;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.NavigatorProvider;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RecordFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "YOUR-TAG-NAME";
    private NavController navController;
    private ImageButton listBtn;
    private ImageButton recordBtn;
    private  String recordPermission = Manifest.permission.RECORD_AUDIO;
    private int PERMISSION_CODE = 21;
    private MediaRecorder mediaRecorder;
    private String recordFile;
    private boolean isRecording = false;
    private Chronometer timer;
    private TextView filenameText;


    public RecordFragment() {
        //constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_record, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        listBtn = view.findViewById(R.id.record_list_btn);
        recordBtn = view.findViewById(R.id.record_btn);
        timer = view.findViewById(R.id.record_timer);
        filenameText = view.findViewById(R.id.record_filename);

        listBtn.setOnClickListener(this);
        recordBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.record_list_btn:
                if(isRecording) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                    alertDialog.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            navController.navigate(R.id.action_recordFragment_to_audioListFragment);
                            isRecording = false;
                        }
                    });
                    alertDialog.setNegativeButton("CANCEL", null);
                    alertDialog.setTitle("Audio still recording");
                    alertDialog.setMessage("Are you sure you want to stop the recording?");
                    alertDialog.create().show();
                } else {
                    navController.navigate(R.id.action_recordFragment_to_audioListFragment);
                }
                break;

            case R.id.record_btn:
                if (isRecording == true) {
                    //stop recording
                    stopRecording();
                    recordBtn.setImageDrawable(getResources().getDrawable(R.mipmap.ic_microphone_using_1_foreground));
                    isRecording = false;
                } else {
                    //start recording
                    if(checkPermissions()) {
                        startRecording();

                        recordBtn.setImageDrawable(getResources().getDrawable(R.mipmap.ic_microphone_not_using_1_foreground, null));
                        isRecording = true;
                    }
                }
                break;
        }
    }

    private void stopRecording() {
        timer.stop();
        filenameText.setText("Recording Stopped. File Saved");
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    private void startRecording() {
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();

        String recordPath = getActivity().getExternalFilesDir("/").getAbsolutePath();
        recordFile = "filename.3gp";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.GERMAN);
        Date now = new Date();

        recordFile = "Recording_" + formatter.format(now) + ".3gp";

        filenameText.setText("Recording, File Name" + recordFile);

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(recordPath + "/" + recordFile);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaRecorder.start();
    }

    private boolean checkPermissions() {
        if(ActivityCompat.checkSelfPermission(getContext(), recordPermission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{recordPermission}, PERMISSION_CODE);
            return false;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(isRecording) {
            stopRecording();
        }
    }
}