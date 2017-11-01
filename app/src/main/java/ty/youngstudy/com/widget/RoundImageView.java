package ty.youngstudy.com.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import ty.youngstudy.com.R;


/**
 * Created by edz on 2017/10/20.
 */

public class RoundImageView extends ImageView {

    /**
     * 边框颜色
     */
    ColorStateList color;

    /**
     * 边框宽度
     */
    int border_width = 1;

    /**
     * image形状
     */
    ShapeType shapeType = ShapeType.ROUND;

    /**
     * 是否原型，默认椭圆
     */
    boolean isRound = false;

    private Bitmap bitmap;
    private final Rect boderRect = new Rect();
    private final Rect drawRect = new Rect();
    private final Paint bmpPaint = new Paint();
    private final Paint boderPaint = new Paint();
    private boolean mReady;
    private boolean mSetupPending;
    private Matrix mShaderMatrix = new Matrix();
    private int mBitmapWidth;
    private int mBitmapHeight;
    int mDrawableRadius;
    int mBorderRadius;

    private ShapeType[] mShapeType = {
        ShapeType.SQUARE,
        ShapeType.ROUND,
        ShapeType.OVAL
    };

    /**
     * image 形状类型
     */
    public enum  ShapeType {
        SQUARE,
        ROUND,
        OVAL
    }

    public RoundImageView(Context context) {
        this(context,null);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    //初始化
    private void init(Context context, @Nullable AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.RoundImageView);
        int count = a.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = a.getIndex(i);
            switch (attr){
                case R.styleable.RoundImageView_boder_color:
                    color = a.getColorStateList(attr);
                    break;

                case R.styleable.RoundImageView_boder_width:
                    border_width = a.getDimensionPixelSize(attr,border_width);
                    break;

                case R.styleable.RoundImageView_shape:
                    int index = a.getInt(attr,-1);
                    if (index > 0){
                        setShape(mShapeType[index]);
                    }
            }
        }
        a.recycle();
        mReady = true;
        if (mSetupPending) {
            setUp();
            mSetupPending = false;
        }
    }

    /**
     * 设置形状
     * @param shape
     */
    public void setShape(ShapeType shape){
        /*if (shape == null)
        {
            throw new NullPointerException();
        }
        if (shape != shapeType){
            shapeType = shape;
            requestLayout();
            invalidate();
        }*/



    }

    private ColorFilter colorFilter;

    @Override
    public void setColorFilter(ColorFilter cf) {
        //super.setColorFilter(cf);
        if (cf == colorFilter) {
            return;
        }
        colorFilter = cf;
        bmpPaint.setColorFilter(colorFilter);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        Drawable drawable = getDrawable();
        if (drawable == null){
            return;
        }

        if (getWidth() == 0 || getHeight() == 0){
            return;
        }

        /*Bitmap bmp = ((BitmapDrawable)drawable).getBitmap();
        if (bmp == null) {
            return;
        }*/

        canvas.translate(0,0);
        canvas.drawCircle(getWidth()/2,getHeight()/2,mDrawableRadius,bmpPaint);

        if (border_width != 0) {
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, mBorderRadius, boderPaint);
        }
        /*Rect boderRect = new Rect();
        Rect drawRect = new Rect();
        Paint bmpPaint = new Paint();
        Paint boderPaint = new Paint();*/

/*
        Bitmap roundMap = getRoundBitMap(bmp);


        BitmapShader shader = new BitmapShader(roundMap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        bmpPaint.setAntiAlias(true);
        bmpPaint.setColorFilter(colorFilter);
        bmpPaint.setShader(shader);

        boderPaint.setAntiAlias(true);
        boderPaint.setStyle(Paint.Style.STROKE);
        boderPaint.setStrokeWidth(border_width);
        boderPaint.setColor(color.getColorForState(getDrawableState(),0));

        int bitHeight = roundMap.getHeight();
        int bitWidth = roundMap.getWidth();

        boderRect.set(0,0,getWidth(),getHeight());
        int mBorderRadius = Math.min((boderRect.height() - border_width) / 2, (boderRect.width() - border_width) / 2);

        drawRect.set(boderRect);

        drawRect.inset(border_width, border_width);
        //paint.reset();


        int mDrawableRadius = Math.min(drawRect.height() / 2, drawRect.width() / 2);

        canvas.drawCircle(getWidth()/2,getHeight()/2,mDrawableRadius,bmpPaint);

        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mBorderRadius, boderPaint);*/
/*        final Paint paint = new Paint();
        final Rect rect = new Rect(getLeft(), getTop(), getRight(), getBottom());
        final Rect bodrect = new Rect(getLeft()-60,getTop() - 60,getRight() + 60,
                getBottom() + 60);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        //paint.setShader(shader);
        paint.setColor(color.getColorForState(getDrawableState(),0));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(60);
        canvas.drawCircle(getWidth()/2, getHeight()/2, getWidth() / 2+30, paint);*/


    }


    private BitmapShader shader;

    private void setUp(){

        if (!mReady) {
            mSetupPending = true;
            return;
        }
        //防止空指针异常
        if (bitmap == null) {
            return;
        }

        shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        bmpPaint.setAntiAlias(true);
        bmpPaint.setShader(shader);

        boderPaint.setAntiAlias(true);
        boderPaint.setStyle(Paint.Style.STROKE);
        boderPaint.setStrokeWidth(border_width);
        if (color != null) {
            boderPaint.setColor(color.getColorForState(getDrawableState(), 0));
        }

        boderRect.set(0,0,getWidth(),getHeight());
        mBorderRadius = Math.min((boderRect.height() - border_width) / 2, (boderRect.width() - border_width) / 2);

        drawRect.set(boderRect);

        drawRect.inset(border_width, border_width);
        //paint.reset();

        mBitmapHeight = bitmap.getHeight();
        mBitmapWidth = bitmap.getWidth();

        mDrawableRadius = Math.min(drawRect.height() / 2, drawRect.width() / 2);
        updateShaderMatrix();
        invalidate();
    }

    private void updateShaderMatrix() {
        float scale;
        float dx = 0;
        float dy = 0;

        mShaderMatrix.set(null);
        // 这里不好理解 这个不等式也就是(mBitmapWidth / mDrawableRect.width()) > (mBitmapHeight / mDrawableRect.height())
        //取最小的缩放比例
        if (mBitmapWidth * drawRect.height() > drawRect.width() * mBitmapHeight) {
            //y轴缩放 x轴平移 使得图片的y轴方向的边的尺寸缩放到图片显示区域（mDrawableRect）一样）
            scale = drawRect.height() / (float) mBitmapHeight;
            dx = (drawRect.width() - mBitmapWidth * scale) * 0.5f;
        } else {
            //x轴缩放 y轴平移 使得图片的x轴方向的边的尺寸缩放到图片显示区域（mDrawableRect）一样）
            scale = drawRect.width() / (float) mBitmapWidth;
            dy = (drawRect.height() - mBitmapHeight * scale) * 0.5f;
        }
        // shaeder的变换矩阵，我们这里主要用于放大或者缩小。
        mShaderMatrix.setScale(scale, scale);
        // 平移
        mShaderMatrix.postTranslate((int) (dx + 0.5f) + drawRect.left, (int) (dy + 0.5f) + drawRect.top);
        // 设置变换矩阵
        shader.setLocalMatrix(mShaderMatrix);
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        bitmap = ((BitmapDrawable)getDrawable()).getBitmap();
        setUp();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        bitmap = bm;
        setUp();
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        bitmap = ((BitmapDrawable)getDrawable()).getBitmap();
        setUp();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        bitmap = ((BitmapDrawable)getDrawable()).getBitmap();
        setUp();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setUp();
    }

    public Bitmap getRoundBitMap(Drawable drawable){
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            //通常来说 我们的代码就是执行到这里就返回了。返回的就是我们最原始的bitmap
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;

            //if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
            //} else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            //}

            Canvas canvas = new Canvas(bitmap);
            /*drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);*/
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

}
