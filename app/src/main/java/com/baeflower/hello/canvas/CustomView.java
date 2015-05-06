package com.baeflower.hello.canvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by sol on 2015-04-06.
 */
public class CustomView extends View {

    private Paint mPaint;

    // canvas에 바로 그릴수 없고 버퍼가 필요함
    private Bitmap mBitmap;
    private Canvas mCanvas;


    // 코드 상에서 생성시 호출되는 생성자
    public CustomView(Context context) {
        this(context, null);
        Toast.makeText(context, "CustomView(Context context)", Toast.LENGTH_SHORT).show();
    }


    //
    public CustomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        Toast.makeText(context, "CustomView(Context context, AttributeSet attrs)", Toast.LENGTH_SHORT).show();
    }

    // xml에서 생성시 호출되는 생성자
    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Toast.makeText(context, "CustomView(Context context, AttributeSet attrs, int defStyleAttr)", Toast.LENGTH_SHORT).show();


        mPaint = new Paint();
        mPaint.setStrokeWidth(3); // 굵기
        mPaint.setColor(Color.BLUE); // 색상
        mPaint.setStyle(Paint.Style.STROKE); // 선

        // 메모리만 차지하는 빈 비트맵
        mBitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
        // Toast.makeText(context, "width : " + getMeasuredWidth() + ", height : " + getMeasuredHeight(), Toast.LENGTH_SHORT).show();

        // 비트맵은 그냥 메모리고, canvas에 연결
        mCanvas = new Canvas(mBitmap);
    }

    // 그리는 것은 이곳에서
    @Override
    protected void onDraw(Canvas canvas) {

        /*
        Paint paint = new Paint(); // 붓
        paint.setColor(Color.RED);

        canvas.drawRect(100, 100, 300, 500, paint); // 그리기

        Paint paint2 = new Paint();
        paint2.setColor(Color.BLUE);
        paint2.setStrokeWidth(10);
        paint2.setAlpha(100);

        // (50, 50) 부터 (500, 50) 까지
        canvas.drawLine(50, 50, 500, 50, paint2);
        */


        // 최종 결과물은 비트맵에 있는 것 (paint는 이 안에 들어있으니깐 의미가 없음. null 넣어도 됨)
        canvas.drawBitmap(mBitmap, 0, 0, null);


    }

    // view의 크기
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    // onLayout 에서 위치 수정


    private float mX1, mY1, mX2, mY2;
    private Path mPath; // 그리는 궤적을 표현


    //
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // 손가락 down -> x1, y2
        // move -> x2, y2,
        // x1, x2, y1, y2 선으로 연결 -> onDraw에 그리기
        // 반복
        // up -> onDraw()에 그리기

        // 멀티터치를 구현하려면 actionmasked를 쓴다
        int action = event.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_DOWN:

                mPath = new Path();
                mX1 = event.getX();
                mY1 = event.getY();

                mPath.moveTo(mX1, mY1);

                break;
            case MotionEvent.ACTION_MOVE:
                mX2 = event.getX();
                mY2 = event.getY();


                mPath.lineTo(mX2, mY2);
                // mPath.quadTo(mX1, mY2, mX2, mY2); // 베지어 알고리즘 적용된 것. 이상한디?

                // 두점 사이의 거리가 어느정도 이상이면 새로운 점으로 인식해서 그때 그리도록 할수도 있다(모든 터치를 받으면 좀 ..?)


                // 비트맵은 그냥 메모리기 떄문에 바로 그릴수가 없음. 캔버스가 필요함
                // canvas는 비트맵과 연결이 되어있으니깐 비트맵에 변화가 일어남
                mCanvas.drawPath(mPath, mPaint);

                // 첫번째 좌표 갱신
                mX1 = mX2;
                mY1 = mY2;

                break;
            case MotionEvent.ACTION_UP:
                mPath.setLastPoint(event.getX(), event.getY());
                break;
        }

        // onDraw()를 호출 - onDraw()를 호출해야 화면이 갱신됨
        invalidate();

        // 그대로 둬야된다
        // touchevent를 더 심도있게 공부하면 바꿀수도 있지만 아직은!
        // touch가 들어간 기본동작을 따른다는것?...
        // 계속 view의 계층을 타고 들어감
        // return super.onTouchEvent(event);

        // 이렇게 하지 않고 touch가 해당 view에 머물게 하고싶다
        // 처리 완료 되었다 : true
        // 여기서 처리 안했다 : false
        // Action_Down에서 터치를 받을 객체의 레퍼런스를 받는다
        // Action_Move가 이 객체로 바로 들어오도록
        return true;
    }


    // TODO 나중에 파일저장 구현
    public Bitmap getBitmap() {
        return mBitmap;
    }




}
