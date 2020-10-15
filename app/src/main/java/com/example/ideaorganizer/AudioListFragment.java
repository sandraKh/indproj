package com.example.ideaorganizer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ideaorganizer.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DataSnapshot;

import java.io.File;
import java.io.IOException;


public class AudioListFragment extends Fragment implements AudioListAdapter.onItemListClick {

    private ConstraintLayout playerSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private RecyclerView audioList;
    private File[] allFiles;
    private AudioListAdapter audioListAdapter;
    private MediaPlayer mediaPlayer = null;
    private boolean isPlaying = false;
    private File fileToPlay = null;

    private ImageButton playBtn;
    private ImageButton deleteBtn;
    private TextView playerHeader;
    private TextView playerFilename;
    private SeekBar playerSeekbar;
    private Handler seekbarHandler;
    private Runnable updateSeekbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_audio_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        playerSheet = view.findViewById(R.id.player_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(playerSheet);
        audioList = view.findViewById(R.id.audio_list_view);

        playBtn = view.findViewById(R.id.player_play_btn);
        deleteBtn = view.findViewById(R.id.player_delete_btn);
        playerHeader = view.findViewById(R.id.player_header_title);
        playerFilename = view.findViewById(R.id.player_filename);
        playerSeekbar = view.findViewById(R.id.player_seekbar);

        String path = getActivity().getExternalFilesDir("/").getAbsolutePath();
        File directory = new File(path);
        allFiles = directory.listFiles();

        audioListAdapter = new AudioListAdapter(allFiles, this);
        audioList.setHasFixedSize(true);
        audioList.setLayoutManager(new LinearLayoutManager(getContext()));
        audioList.setAdapter(audioListAdapter);



        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.setState(bottomSheetBehavior.STATE_COLLAPSED);

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //--
            }
        });

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlaying) {
                    pauseAudio();
                } else {
                    if (fileToPlay != null) {
                        resumeAudio();
                    }
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fileToPlay != null) {
                    Toast.makeText(view.getContext(), "Deleted" , Toast.LENGTH_SHORT).show();
                    fileToPlay.delete();
                    audioListAdapter.notifyDataSetChanged();
                }
            }
        });

        playerSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if(fileToPlay != null) {
                    pauseAudio();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(fileToPlay != null) {
                    int progress = seekBar.getProgress();
                    mediaPlayer.seekTo(progress);
                    resumeAudio();
                }
            }
        });


        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.showaudio);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent i1 = new Intent(getActivity(), MainActivity.class);
                        startActivity(i1);
                        return true;
                    case R.id.shownotes:
                        Intent i2 = new Intent(getActivity(), TextActivity.class);
                        startActivity(i2);
                        return true;
                    case R.id.showimages:
                        Intent i3 = new Intent(getActivity(), ImagesActivity.class);
                        startActivity(i3);
                        return true;
                    case R.id.showaudio:
                        return true;
                }
                return false;
            }

        });


    }

    @Override
    public void onClickListener(File file, int position) {
        if(isPlaying) {
            stopAudio();
            playAudio(fileToPlay);
        } else {
            fileToPlay = file;
            playAudio(fileToPlay);
        }
    }

    private void pauseAudio() {
        mediaPlayer.pause();
        playBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_baseline_play_circle_filled_24, null));
        isPlaying = false;
        seekbarHandler.removeCallbacks(updateSeekbar);
    }

    private void resumeAudio() {
        mediaPlayer.start();
        playBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_baseline_pause_24, null));
        isPlaying = true;
        updateRunnable();
        seekbarHandler.postDelayed(updateSeekbar, 0);
    }

    private void stopAudio() {
        //stop audio
        playBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_baseline_play_circle_filled_24, null));
        playerHeader.setText("Stopped");
        isPlaying = false;
        mediaPlayer.stop();
    }

    private void playAudio(File fileToPlay) {
        mediaPlayer = new MediaPlayer();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        try {
            mediaPlayer.setDataSource(fileToPlay.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        playBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_baseline_pause_24, null));
        playerFilename.setText(fileToPlay.getName());
        playerHeader.setText("Playing");

        //play audio
        isPlaying = true;

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopAudio();
                playerHeader.setText("Finished");
            }
        });

        playerSeekbar.setMax(mediaPlayer.getDuration());
        seekbarHandler = new Handler();
        updateRunnable();
        seekbarHandler.postDelayed(updateSeekbar, 0);

    }

    private void updateRunnable() {
        updateSeekbar = new Runnable() {
            @Override
            public void run() {
                playerSeekbar.setProgress(mediaPlayer.getCurrentPosition());
                seekbarHandler.postDelayed(this, 500);
            }
        };
    }

    @Override
    public void onStop() {
        super.onStop();
        if(isPlaying) {
            stopAudio();
        }
    }


}