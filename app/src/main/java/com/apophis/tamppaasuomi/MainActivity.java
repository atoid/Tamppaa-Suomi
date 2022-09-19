package com.apophis.tamppaasuomi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MAIN";
    private static final String BUNDLE_STATE = "ImageViewState";
    private GameView gameView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageViewState imageViewState = null;
        if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_STATE)) {
            imageViewState = (ImageViewState)savedInstanceState.getSerializable(BUNDLE_STATE);
        }

        gameView = findViewById(R.id.imageView);
        gameView.setImage(ImageSource.asset(Util.MAP_IMAGE_FILE), imageViewState);
        gameView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
        gameView.setQuickScaleEnabled(false);
        gameView.setMinimumDpi(60);
        gameView.setDoubleTapZoomDpi(60);
        gameView.setDoubleTapZoomDuration(250);
        gameView.setDoubleTapZoomStyle(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER);

        Util.restoreState(this);
        gameView.restore();
        updateProgress();

        final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (gameView.isReady()) {
                    PointF sCoord = gameView.viewToSourceCoord(e.getX(), e.getY());
                    if (sCoord != null) {
                        if (gameView.colorRect(sCoord.x, sCoord.y)) {
                            updateProgress();
                        }
                        updateXY(gameView.getLastXY());
                    }
                }
                return true;
            }
        });

        gameView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });

        View v = findViewById(R.id.trophyButton);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), TrophiesActivity.class);
                startActivityForResult(intent, Util.TROPHY_RESULT);
            }
        });

        Log.i(TAG, "onCreate");
    }

    private void updateProgress() {
        TextView tv = findViewById(R.id.gameProgress);
        tv.setText(Trophy.getProgress());
    }

    private void updateXY(int[] xy) {
        TextView tv = findViewById(R.id.gameCoords);
        if (tv != null) {
            tv.setText(String.format(Locale.ROOT, "%d, %d", xy[0], xy[1]));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Util.saveState(this);
        Log.i(TAG, "onStop");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        SubsamplingScaleImageView imageView = findViewById(R.id.imageView);
        ImageViewState state = imageView.getState();
        if (state != null) {
            outState.putSerializable(BUNDLE_STATE, imageView.getState());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Util.TROPHY_RESULT) {
            if (resultCode == Activity.RESULT_OK && data != null && gameView != null) {
                int[] xy = data.getIntArrayExtra("coords");
                if (xy != null) {
                    gameView.gotoLocation(xy[0], xy[1]);
                }
            }
        }
    }
}
