package com.sds.study.serfaceview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 뷰에 그림을 그려 다시 빠른속도로 갱신시키면 깜빡이는 효과를 볼수있따....
 * 그리고 지우고를 반복하는 과정에서 생기는 현상
 * 해결책)프로그램밍 적으로는 더블 버퍼링을 이용하면된다...
 * 안드로이드) 서피스뷰가 내부부적으로 해결해 놓았다
 * 따라서 제작할 앱이 고속의 카메라 제어, 동영상,에니메이션을 구현하려면 서피스뷰를 사용하면 된다
 */

public class MyView extends SurfaceView implements SurfaceHolder.Callback,Runnable{
    //그림이 그려질 표면 관리자
    SurfaceHolder holder;
    RectF rect;
    Paint paint;
    int x=100,y=100,width=50,height=50;
    Canvas canvas;
    Thread thread;

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        rect = new RectF(x,y,x+width,y+height);
        paint = new Paint();
        paint.setColor(Color.RED);

        /*서피스 홀더와 콜백인터페이스 연결!!*/
        holder = getHolder();
        holder.addCallback(this);


    }


    //그림이 그려질 표면이 준비되면 호출되는 메서드
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new Thread(this);
        thread.start();
    }

    //그림이 그려질 표면에 무언가 변경사항이 생기면 호출되는 메서드
    public void surfaceChanged(SurfaceHolder holder, int i, int i1, int i2) {

    }

    //더이상 표면이 필요없는경우
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    //그래픽 처리 메서드
    public void render(){
        //그림 그리기 시작!!!
        canvas = holder.lockCanvas();
        canvas.drawColor(Color.YELLOW);
        canvas.drawRect(rect,paint);

        x+=2;
        y+=2;
        rect.set(x,y,x+width,y+height);
        canvas.drawRect(rect,paint);

        //그림종료
        holder.unlockCanvasAndPost(canvas);
    }

    //사각형을 움직이게 한다...
    public void run() {
        while(true){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            render();
        }

    }
}
