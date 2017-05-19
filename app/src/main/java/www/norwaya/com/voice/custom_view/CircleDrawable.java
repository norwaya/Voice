package www.norwaya.com.voice.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import www.norwaya.com.voice.R;

/**
 * Created by Administrator on 2017/5/12 0012.
 */

public class CircleDrawable extends android.support.v7.widget.AppCompatImageView {
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    public static final int CIRCLE = 0x102;
    public static final int ROUND = 0x101;

    private static final int mDefaultSize = 50;

    private int mType;
    private int mBorderColor;
    private float mBorderWidth;
    private int mFillColor;
    private Paint mPaint;

    public CircleDrawable(Context context) {
        super(context);
    }

    public CircleDrawable(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleDrawable(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CircleDrawable);
        mBorderColor = ta.getColor(R.styleable.CircleDrawable_border_color, Color.TRANSPARENT);
        mType = ta.getInt(R.styleable.CircleDrawable_type, CIRCLE);
        mBorderWidth = ta.getDimension(R.styleable.CircleDrawable_border_width, 1.0f);
        mFillColor = ta.getColor(R.styleable.CircleDrawable_fill_color, Color.TRANSPARENT);
        initMetrics();
    }
    public void setType(int type){
        if(this.mType != type)
        this.mType = type;
        requestLayout();
    }
    public int getType(){
        return this.mType;
    }
    private void initMetrics() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    private int mWidth;
    private int mHeight;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mType == CIRCLE) {
            mWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
            setMeasuredDimension(mWidth + 2 * ((int) (mBorderWidth + 0.5f)),
                    mWidth + 2 * ((int) (mBorderWidth + 0.5f)));
        } else {
            mWidth = getMeasuredWidth();
            mHeight = getMeasuredHeight();
            setMeasuredDimension(getMeasuredWidth() + 2 * ((int) (mBorderWidth + 0.5f)),
                    getMeasuredHeight() + 2 * ((int) (mBorderWidth + 0.5f)));
        }
    }
    RectF mRoundRect;
   @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);

        // 圆角图片的范围
        if (mType == ROUND)
            mRoundRect = new RectF(mBorderWidth, mBorderWidth, mBorderWidth + mWidth, mBorderWidth + mHeight);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        init();
        if (mType == ROUND)
            mRoundRect = new RectF(mBorderWidth, mBorderWidth, mBorderWidth + mWidth, mBorderWidth + mHeight);
        Paint borderPaint = new Paint();
        borderPaint.setColor(mBorderColor);
        if (mType == CIRCLE) {
            canvas.drawCircle(mWidth / 2 + mBorderWidth, mWidth / 2 + mBorderWidth, mWidth / 2 + mBorderWidth, borderPaint);
            canvas.drawCircle(mWidth / 2 + mBorderWidth, mWidth / 2 + mBorderWidth, mWidth / 2, mPaint);
        } else {
            canvas.drawRoundRect(0, 0, 2 * mBorderWidth + mWidth, 2 * mBorderWidth + mHeight, 10f, 10f, borderPaint);
            canvas.drawRoundRect(mRoundRect, 10f, 10f, mPaint);
        }


    }




    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            Log.i("custom view", "getBitmapFromDrawable: ");
            return ((BitmapDrawable) drawable).getBitmap();
        }
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        return bitmap;
    }

    private void init() {
        Bitmap bitmap = getBitmapFromDrawable(getDrawable());
        if (mType == CIRCLE) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int c = Math.abs((width - height) / 2);
            if (width > height) {
                bitmap = Bitmap.createBitmap(bitmap, c, 0, c + height, height);
            } else {
                bitmap = Bitmap.createBitmap(bitmap, 0, c, width, c + width);
            }
        }
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);


        Matrix matrix = new Matrix();
        float scale;
        if (mType == CIRCLE) {
            scale = getWidth() * 1.0f / bitmap.getWidth();
        } else {
            scale = Math.max(getWidth() * 1.0f / bitmap.getWidth(), getHeight() * 1.0f / bitmap.getHeight());
        }
        matrix.setScale(scale, scale);
        bitmapShader.setLocalMatrix(matrix);
        mPaint.setShader(bitmapShader);

    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("bundle", super.onSaveInstanceState());
        bundle.putInt("type", mType);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof  Bundle){
            Bundle bundle = ((Bundle) state);
            super.onRestoreInstanceState(bundle.getParcelable("bundle"));
            mType = bundle.getInt("type");
        }else{
            super.onRestoreInstanceState(state);
        }
    }
}
