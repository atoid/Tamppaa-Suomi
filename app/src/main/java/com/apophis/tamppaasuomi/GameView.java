package com.apophis.tamppaasuomi;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import com.apophis.tamppaasuomi.Util.TapResult;
import com.google.android.material.snackbar.Snackbar;

public class GameView extends SubsamplingScaleImageView {
    private static final String TAG = "GW";
    private static final float SNACKBAR_TEXT_SIZE = 20.f;

    private Bitmap bm;
    private Paint paintGrid, paintInside, paintOutside, paintClear, paintBm;
    private Canvas cs;
    private Matrix mtx = new Matrix();
    private int[] last_xy = new int[2];
    private boolean help = true;

    public GameView(Context context) {
        this(context, null);
    }

    public GameView(Context context, AttributeSet attr) {
        super(context, attr);
        init();
    }

    public void gotoLocation(int x, int y) {
        PointF p = new PointF(x * Util.GRID_SIZEF, y * Util.GRID_SIZEF);
        setScaleAndCenter(getMaxScale(), p);
    }

    public int[] getLastXY() {
        return last_xy;
    }

    public boolean colorRect(float x, float y) {
        int ix = ((int) x) >> 4;
        int iy = ((int) y) >> 4;
        TapResult res = Util.tap(ix, iy);

        Log.i(TAG, "tap at: " + ix + ", " + iy);
        last_xy[0] = ix;
        last_xy[1] = iy;

        if (res == TapResult.TAP_MASK_EMPTY) {
            Trophy.TrophyItem[] ti = Trophy.checkSingle(ix, iy, Util.getTapCount());
            showTrophies(ti);
            cs.drawPoint(ix, iy, paintInside);
            invalidate();
            return true;
        }
        else if (res == TapResult.TAP_OUTSIDE_EMPTY) {
            cs.drawPoint(ix, iy, paintOutside);
            invalidate();
        }
        else if (res == TapResult.TAP_OUTSIDE_FULL) {
            cs.drawPoint(ix, iy, paintClear);
            invalidate();
        }

        return false;
    }

    public void restore()
    {
        Util.restoreBm(cs, paintInside, paintOutside);
    }

    private void showTrophies(Trophy.TrophyItem[] ti) {
        Context ctx = getContext();
        Resources r = ctx.getResources();

        StringBuilder msg = new StringBuilder();
        for (Trophy.TrophyItem trophyItem : ti) {
            if (trophyItem != null) {
                if (msg.length() > 0) {
                    msg.append("\n");
                }
                msg.append(Trophy.getAsString(r, trophyItem, true));
            }
        }

        if (msg.length() > 0) {
            showSnackbar(msg.toString(), 4000);
        }
    }

    @Override
    protected void onReady() {
        super.onReady();
        if (help && Util.getTapCount() < Util.HELP_LIMIT) {
            showHelp();
            help = false;
        }
    }

    private void showHelp() {
        Context ctx = getContext();
        Resources r = ctx.getResources();
        showSnackbar(r.getString(R.string.app_help), 4000);
    }

    private void showSnackbar(String msg, int duration) {
        Snackbar sb = Snackbar.make(this, msg, duration);
        View tmp = sb.getView();
        TextView tw = tmp.findViewById(com.google.android.material.R.id.snackbar_text);
        tw.setTextSize(SNACKBAR_TEXT_SIZE);
        tw.setMaxLines(2);
        //tmp.setAlpha(0.8f);
        //tmp.setBackgroundColor(R.color.transparentBlack);
        sb.show();
    }

    private void init() {
        bm = Bitmap.createBitmap(Util.BM_WIDTH, Util.BM_HEIGHT, Bitmap.Config.ARGB_8888);
        cs = new Canvas(bm);

        paintGrid = new Paint();
        paintGrid.setColor(Util.GRID_COLOR);
        paintGrid.setAlpha(Util.GRID_ALPHA);
        paintGrid.setStrokeWidth(Util.GRID_WIDTH);

        paintInside = new Paint();
        paintInside.setColor(Util.INSIDE_COLOR);
        paintInside.setAlpha(Util.INSIDE_ALPHA);
        paintInside.setFlags(0);

        paintOutside = new Paint();
        paintOutside.setColor(Util.OUTSIDE_COLOR);
        paintOutside.setAlpha(Util.OUTSIDE_ALPHA);
        paintOutside.setFlags(0);

        paintClear = new Paint();
        paintClear.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        paintClear.setFlags(0);

        paintBm = new Paint();
        paintBm.setFlags(0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!isReady()) {
            return;
        }

        PointF sCoord = viewToSourceCoord(0, 0);
        if (sCoord == null) {
            return;
        }

        float spacing = getScale() * Util.GRID_SIZEF;

        // Draw grid
        if (spacing >= Util.MIN_GRID_DISPLAY_SIZE) {
            float startx = -(sCoord.x % Util.GRID_SIZEF) * getScale();
            float starty = -(sCoord.y % Util.GRID_SIZEF) * getScale();

            for (float x = startx; x < getWidth(); x += spacing) {
                canvas.drawLine(x, 0, x, getHeight(), paintGrid);
            }

            for (float y = starty; y < getHeight(); y += spacing) {
                canvas.drawLine(0, y, getWidth(), y, paintGrid);
            }
        }

        // Draw bitmap
        mtx.reset();
        mtx.postScale(Util.GRID_SIZEF * getScale(), Util.GRID_SIZEF * getScale());
        mtx.preTranslate(-sCoord.x / Util.GRID_SIZEF, -sCoord.y / Util.GRID_SIZEF);
        canvas.drawBitmap(bm, mtx, paintBm);
    }
}

