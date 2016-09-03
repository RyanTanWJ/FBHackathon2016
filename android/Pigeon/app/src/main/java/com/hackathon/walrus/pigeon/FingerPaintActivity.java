package com.hackathon.walrus.pigeon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FingerPaintActivity extends Activity
        implements ColorPickerDialog.OnColorChangedListener {

    MyView mv;
    AlertDialog dialog;
    static String TAG = "FingerPaintActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AttributeSet blank = new AttributeSet() {
            @Override
            public int getAttributeCount() {
                return 0;
            }

            @Override
            public String getAttributeName(int index) {
                return null;
            }

            @Override
            public String getAttributeValue(int index) {
                return null;
            }

            @Override
            public String getAttributeValue(String namespace, String name) {
                return null;
            }

            @Override
            public String getPositionDescription() {
                return null;
            }

            @Override
            public int getAttributeNameResource(int index) {
                return 0;
            }

            @Override
            public int getAttributeListValue(String namespace, String attribute, String[] options, int defaultValue) {
                return 0;
            }

            @Override
            public boolean getAttributeBooleanValue(String namespace, String attribute, boolean defaultValue) {
                return false;
            }

            @Override
            public int getAttributeResourceValue(String namespace, String attribute, int defaultValue) {
                return 0;
            }

            @Override
            public int getAttributeIntValue(String namespace, String attribute, int defaultValue) {
                return 0;
            }

            @Override
            public int getAttributeUnsignedIntValue(String namespace, String attribute, int defaultValue) {
                return 0;
            }

            @Override
            public float getAttributeFloatValue(String namespace, String attribute, float defaultValue) {
                return 0;
            }

            @Override
            public int getAttributeListValue(int index, String[] options, int defaultValue) {
                return 0;
            }

            @Override
            public boolean getAttributeBooleanValue(int index, boolean defaultValue) {
                return false;
            }

            @Override
            public int getAttributeResourceValue(int index, int defaultValue) {
                return 0;
            }

            @Override
            public int getAttributeIntValue(int index, int defaultValue) {
                return 0;
            }

            @Override
            public int getAttributeUnsignedIntValue(int index, int defaultValue) {
                return 0;
            }

            @Override
            public float getAttributeFloatValue(int index, float defaultValue) {
                return 0;
            }

            @Override
            public String getIdAttribute() {
                return null;
            }

            @Override
            public String getClassAttribute() {
                return null;
            }

            @Override
            public int getIdAttributeResourceValue(int defaultValue) {
                return 0;
            }

            @Override
            public int getStyleAttribute() {
                return 0;
            }
        };
        mv= new MyView(this,null);
        mv.setDrawingCacheEnabled(true);
//        mv.setBackgroundResource(R.drawable.afor);//set the back ground if you wish to
        setContentView(mv);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFFFF0000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(20);
        mEmboss = new EmbossMaskFilter(new float[] { 1, 1, 1 },
                0.4f, 6, 3.5f);
        mBlur = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);
    }

    private Paint       mPaint;
    private MaskFilter mEmboss;
    private MaskFilter  mBlur;

    public void colorChanged(int color) {
        mPaint.setColor(color);
    }

    public class MyView extends View {

        private Bitmap  mBitmap, bitmapArr[];
        private Canvas  mCanvas;
        private Path    mPath;
        private Paint   mBitmapPaint;
        private Paint   mPaint;
        private List<Path> mPathsArray = new ArrayList<Path>();
        private int frameCounter = 0;

        //GIF encoder

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        AnimatedGifEncoder encoder;

        public MyView(Context c, AttributeSet attrs) {
            super(c, attrs);
            mPaint = new Paint();
            mPaint.setColor(0xFF00F1F5);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeWidth(6);

            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
            bitmapArr = new Bitmap[100];
        }

        public void nextFrame(){
            Bitmap bitmap = getDrawingCache();
            bitmapArr[frameCounter] = bitmap;
            System.out.println("@@@bitmap " + frameCounter + " = " +  bitmap.toString());
            frameCounter++;
        }
        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
        }
        public void clear(){
            mBitmap = Bitmap.createBitmap(480, 480, Bitmap.Config.ARGB_8888);
            mPath = new Path();
            invalidate();
        }
        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
            canvas.drawPath(mPath, mPaint);
        }

        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;

        private void touch_start(float x, float y) {
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
        }
        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
                mX = x;
                mY = y;
            }
        }
        private void touch_up() {
            mPath.lineTo(mX, mY);
            // commit the path to our offscreen
            mCanvas.drawPath(mPath, mPaint);
            // kill this so we don't double draw
            mPathsArray.add(mPath);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
            return true;
        }

        public void saveGif() {
            for(int i=0; i<=frameCounter; i++) {
                Log.i(TAG, "ADDING FRAME " + i + " ...");
                Bitmap bitmap = bitmapArr[i];
                encoder.addFrame(bitmap);
                bitmap.recycle();
            }
            encoder.finish();
            File filePath = new File("/sdcard/PigeonMessenger/images/", "sample.gif");
            FileOutputStream outputStream;
            try {
                outputStream = new FileOutputStream(filePath);
                // bosに生成されたgifデータをファイルに吐き出す
                outputStream.write(bos.toByteArray());
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        }

        public void start_gif() {
            if(encoder==null) {
                encoder = new AnimatedGifEncoder();
                encoder.setDelay(100);  // ディレイ 500/ms
                encoder.setRepeat(0);   // 0:ループする -1:ループしない
                encoder.start(bos);     // gitデータ生成先ををbosに設定
            }
        }
    }
    private static final int COLOR_MENU_ID = Menu.FIRST;
    private static final int EMBOSS_MENU_ID = Menu.FIRST + 1;
    private static final int BLUR_MENU_ID = Menu.FIRST + 2;
    private static final int ERASE_MENU_ID = Menu.FIRST + 3;
    private static final int SRCATOP_MENU_ID = Menu.FIRST + 4;
    private static final int START_GIF = Menu.FIRST + 5;
    private static final int NEXT_FRAME = Menu.FIRST + 6;
    private static final int Clear = Menu.FIRST + 7;
    private static final int Save = Menu.FIRST + 8;
    private static final int SAVE_GIF = Menu.FIRST + 9;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(0, COLOR_MENU_ID, 0, "Color").setShortcut('3', 'c');
        menu.add(0, EMBOSS_MENU_ID, 0, "Emboss").setShortcut('4', 's');
        menu.add(0, BLUR_MENU_ID, 0, "Blur").setShortcut('5', 'z');
        menu.add(0, ERASE_MENU_ID, 0, "Erase").setShortcut('5', 'z');
        menu.add(0, SRCATOP_MENU_ID, 0, "SrcATop").setShortcut('5', 'z');
        menu.add(0, Clear, 0, "Clear").setShortcut('5','z');
        menu.add(0, START_GIF, 0, "Start GIF").setShortcut('5','z');
        menu.add(0, NEXT_FRAME, 0, "Next Frame").setShortcut('5','z');
        menu.add(0, Save, 0, "Save").setShortcut('5', 'z');
        menu.add(0, SAVE_GIF, 0, "Export GIF").setShortcut('5', 'z');


        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xFF);

        switch (item.getItemId()) {
            case COLOR_MENU_ID:
                new ColorPickerDialog(this, this, mPaint.getColor()).show();
                return true;
            case EMBOSS_MENU_ID:
                if (mPaint.getMaskFilter() != mEmboss) {
                    mPaint.setMaskFilter(mEmboss);
                } else {
                    mPaint.setMaskFilter(null);
                }
                return true;
            case BLUR_MENU_ID:
                if (mPaint.getMaskFilter() != mBlur) {
                    mPaint.setMaskFilter(mBlur);
                } else {
                    mPaint.setMaskFilter(null);
                }
                return true;
            case START_GIF:
                mv.start_gif();
                return true;
            case ERASE_MENU_ID:
                mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                mPaint.setAlpha(0x80);
                return true;
            case SRCATOP_MENU_ID:

                mPaint.setXfermode(new PorterDuffXfermode(
                        PorterDuff.Mode.SRC_ATOP));
                mPaint.setAlpha(0x80);
                return true;
            case Clear:
                mv.clear();
                return true;
            case NEXT_FRAME:
                mv.nextFrame();
//                mv.encoder.addFrame(mv.getDrawingCache());
                //mv.clear();
                return true;
            case SAVE_GIF:
                mv.saveGif();
                return true;
            case Save:
                AlertDialog.Builder editalert = new AlertDialog.Builder(FingerPaintActivity.this);
                editalert.setTitle("Please Enter the name with which you want to Save");
                final EditText input = new EditText(FingerPaintActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT,
                        LinearLayout.LayoutParams.FILL_PARENT);
                input.setLayoutParams(lp);
                editalert.setView(input);
                editalert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String name= input.getText().toString();
                        Bitmap bitmap = mv.getDrawingCache();

                        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                        File directory = new File(path + "/PigeonMessenger/images");
                        if(!directory.exists() && !directory.isDirectory()) {
                            directory.mkdirs();
                            Log.i(TAG,"directory created");
                        }
                        File file = new File(directory + File.separator +name+ ".png");
                        try
                        {
                            if(!file.exists())
                            {
                                Log.i(TAG,"file does not exist, creating file");
                                file.createNewFile();
                            }
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 10, ostream);
                            ostream.close();
                            Log.i(TAG,"file created");
                            mv.invalidate();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }finally
                        {

                            mv.setDrawingCacheEnabled(false);
                        }
                    }
                });

                editalert.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
/*
public class FingerPaintActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_paint);
    }
}
*/