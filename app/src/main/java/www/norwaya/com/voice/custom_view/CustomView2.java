package www.norwaya.com.voice.custom_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by Administrator on 2017/5/12 0012.
 */

public class CustomView2 extends android.support.v7.widget.AppCompatImageView {
    public CustomView2(Context context) {
        super(context);
    }

    public CustomView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int c = Math.min(getMeasuredHeight(), getMeasuredWidth());
        setMeasuredDimension(200,200);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        init();
        canvas.drawCircle(100,100,100,paint);
        /*init2();
        canvas.drawBitmap(init2(),0,0,null);*/
    }

    Paint paint = new Paint();
    private void init(){
        this.paint.setAntiAlias(true);
        Bitmap bitmap = getBitmapFromDrawable(getDrawable());

        bitmap = Bitmap.createBitmap(bitmap,0,100,200,300);
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        int x = Math.min(bitmap.getWidth(), bitmap.getHeight());

        Matrix matrix = new Matrix();
        matrix.setScale(200/x,200/x);

        bitmapShader.setLocalMatrix(matrix);
        this.paint.setShader(bitmapShader);
    }
    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            Log.i("init view", "getBitmapFromDrawable: ");
            return ((BitmapDrawable) drawable).getBitmap();
        }
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        return bitmap;
    }
    /**
     * 在target 上 绘制
     * @return
     */
    private Bitmap  init2(){
        Bitmap bitmap = getBitmapFromDrawable(getDrawable());
        Bitmap target = Bitmap.createBitmap(200,200 , Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawCircle(100, 100, 100, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap,0,0,paint);
        return target;
    }
}
