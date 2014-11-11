package com.projekt.mygenerator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;


public class Display extends Activity
{
    static class DisplayView extends View
    {

        public DisplayView(Context context)
        {
            super(context);
            init(context);
        }

        public DisplayView(Context context, AttributeSet attrs)
        {
            super(context, attrs);
            init(context);
        }

        void init(Context context)
        {
            if (Build.VERSION.SDK_INT<19)
                setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        parent.makeFullscreen();
                    }
                });
        }

        @Override
        protected void onDraw(Canvas canvas)
        {

            super.onDraw(canvas);
            boolean error = bitmap == null || bitmap.getHeight()!=canvas.getHeight()
                    || bitmap.getWidth() !=canvas.getWidth();
            if (error) {drawError(canvas); return;}

            canvas.drawBitmap(bitmap, null, new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), null);
        }

        void drawError(Canvas canvas)
        {
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setTextSize(15);
            String s;
            if (bitmap==null) s = "bitmap == null";
            else s = "Error\nCanvas size: " + canvas.getWidth() + " x " + canvas.getHeight() +
                    "\nBitmap size: " + bitmap.getWidth() + " x " + bitmap.getHeight();
            canvas.drawText(s, 50, 50, paint);
        }


        public void setBitmap(Bitmap bitmap)
        {
            this.bitmap = bitmap;
            invalidate();
        }

        Bitmap bitmap = null;
        Display parent;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_display);

        view = (DisplayView) findViewById(R.id.displayView);
        view.parent = this;

        Intent intent = getIntent();
        int w = intent.getIntExtra("width", -1);
        int h = intent.getIntExtra("height", -1);
        long seed = intent.getLongExtra("seed", -1);
        if (w==-1 || h == -1 || seed == -1) throw new Error("coś się zrypało w getextra");
        Generator g = (Generator) intent.getSerializableExtra("generator");

        Bitmap b = g.generate(seed, w, h);
        view.setBitmap(b);


        decor = getWindow().getDecorView();

        //to i tak nie działa
//        if (Build.VERSION.SDK_INT>=11)
//            decor.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
//            {
//                @Override
//                public void onSystemUiVisibilityChange(int visibility)
//                {
//                    makeFullscreen();
//                }
//            });
//        else{
//
//        }

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        makeFullscreen();
    }

    void makeFullscreen()
    {
        if (Build.VERSION.SDK_INT>=11)
            decor.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | (Build.VERSION.SDK_INT>=19?View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY:0));
        else{
            //todo fullscreen na gingerbreadzie?
            throw new Error("bez fullscreena nie ma sensu");
        }
    }

    View decor;
    DisplayView view;
}