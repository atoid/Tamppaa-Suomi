package com.apophis.tamppaasuomi;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Util {
    private static final String TAG = "UTIL";

    private static final boolean TEST_MODE = false;

    public static final String MAP_IMAGE_FILE = "yleiskarttarasteri_1milj.png";
    public static final String MASK_FILE = "mask_xtrab.bin";
    public static final String TAPS_FILE = "taps.bin";
    public static final int HELP_LIMIT = 2000;

    public static final int GRID_SIZE = 16;
    public static final float GRID_SIZEF = 16.f;
    public static final float MIN_GRID_DISPLAY_SIZE = 48.f;
    public static final int BM_WIDTH = (5632 / GRID_SIZE);
    public static final int BM_HEIGHT = (9760 / GRID_SIZE);

    public static final int GRID_COLOR = 0x000000;
    public static final int GRID_ALPHA = 0xc0;
    public static final float GRID_WIDTH = 3.f;

    public static final int INSIDE_COLOR = 0x228B22;
    public static final int INSIDE_ALPHA = 0x90;

    public static final int OUTSIDE_COLOR = 0xff8c00;
    public static final int OUTSIDE_ALPHA = 0x80;

    public static final int TROPHY_RESULT = 1;

    enum TapResult {
        TAP_NONE,
        TAP_MASK_EMPTY,
        TAP_MASK_FULL,
        TAP_OUTSIDE_EMPTY,
        TAP_OUTSIDE_FULL
    }

    private static byte[] mask = new byte[BM_WIDTH * BM_HEIGHT / 8];
    private static byte[] taps = new byte[BM_WIDTH * BM_HEIGHT / 8];
    private static int tapCount = 0;
    private static boolean dirty = false;

    public static int getTapCount() {
        return tapCount;
    }

    public static int getTapBit(int x, int y) {
        int res = 0;
        if (x < BM_WIDTH && y < BM_HEIGHT) {
            int i = (x >> 3) + (y * (BM_WIDTH / 8));
            int b = 1 << (x & 7);
            res = taps[i] & b;
        }

        return res;
    }

    public static TapResult tap(int x, int y)
    {
        if (x < BM_WIDTH && y < BM_HEIGHT) {
            int i = (x >> 3) + (y * (BM_WIDTH / 8));
            int b = 1 << (x & 7);
            dirty = true;

            if((mask[i] & b) != 0) {
                if ((taps[i] & b) != 0) {
                    return TapResult.TAP_MASK_FULL;
                } else {
                    taps[i] |= b;
                    tapCount++;
                    return TapResult.TAP_MASK_EMPTY;
                }
            }
            else {
                if ((taps[i] & b) != 0) {
                    taps[i] &= ~b;
                    return TapResult.TAP_OUTSIDE_FULL;
                } else {
                    taps[i] |= b;
                    return TapResult.TAP_OUTSIDE_EMPTY;
                }
            }
        }
        else {
            Log.e(TAG, "Tap outside game area: x=" + x + ", y=" + y);
        }

        return TapResult.TAP_NONE;
    }

    public static void saveState(Context ctx)
    {
        if (!dirty || TEST_MODE) {
            return;
        }

        try {
            FileOutputStream out = ctx.openFileOutput(TAPS_FILE, Context.MODE_PRIVATE);
            out.write(taps);
            out.close();
            dirty = false;
        }
        catch (IOException e) {
            Log.e(TAG, "Problem saving state");
        }
    }

    public static void restoreState(Context ctx)
    {
        try {
            InputStream in = ctx.getAssets().open(MASK_FILE);
            in.read(mask);
            in.close();

            if (TEST_MODE) {
                return;
            }

            in = ctx.openFileInput(TAPS_FILE);
            in.read(taps);
            in.close();
            dirty = false;
        }
        catch (IOException e) {
            Log.e(TAG, "No saved state found");
        }
    }

    public static void restoreBm(Canvas cs, Paint paintInside, Paint paintOutside)
    {
        tapCount = 0;

        for (int i = 0; i < taps.length; i++) {
            if (taps[i] != 0) {
                int x = (i * 8) % BM_WIDTH;
                int y = i / (BM_WIDTH / 8);
                byte b = taps[i];
                byte m = mask[i];
                for (int j = 0; j < 8; j++) {
                    if ((b & 1) != 0) {
                        if ((m & 1) == 1) {
                            tapCount++;
                            cs.drawPoint(x+j, y, paintInside);
                        }
                        else {
                            cs.drawPoint(x + j, y, paintOutside);
                        }
                    }
                    b >>= 1;
                    m >>= 1;
                }
            }
        }
    }

}
