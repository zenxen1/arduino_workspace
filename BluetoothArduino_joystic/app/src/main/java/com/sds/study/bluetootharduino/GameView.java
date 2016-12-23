package com.sds.study.bluetootharduino;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lee on 2016-12-09.
 */

public class GameView extends View {
    RectF rect;
    Paint paint;
    int x=0;
    int y=0;
    int width=100;
    int height=100;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        rect = new RectF(x,y,x+width,y+height);
        paint = new Paint();
        paint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(rect,paint);
    }
}
