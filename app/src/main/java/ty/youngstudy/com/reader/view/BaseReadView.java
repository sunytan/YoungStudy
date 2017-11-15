/**
 * Copyright 2016 JustWayward Team
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ty.youngstudy.com.reader.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import java.util.List;

import ty.youngstudy.com.reader.BookStatus;
import ty.youngstudy.com.reader.Chapter;
import ty.youngstudy.com.reader.OnReadStateChangeListener;
import ty.youngstudy.com.reader.PageFactory;
import ty.youngstudy.com.reader.manager.SettingManager;
import ty.youngstudy.com.reader.manager.ThemeManager;
import ty.youngstudy.com.util.ScreenUtils;

/**
 * @author yuyh.
 * @date 2016/10/18.
 */
@SuppressLint("WrongCall")
public abstract class BaseReadView extends View {

    private static final String TAG = "BaseReadView";

    protected int mScreenWidth;
    protected int mScreenHeight;

    protected PointF mTouch = new PointF();
    protected float actiondownX, actiondownY;
    protected float touch_down = 0; // 当前触摸点与按下时的点的差值

    protected Bitmap mCurPageBitmap, mNextPageBitmap;
    protected Canvas mCurrentPageCanvas, mNextPageCanvas;
    protected PageFactory pagefactory = null;

    protected OnReadStateChangeListener listener;
    protected String bookId;
    public boolean isPrepared = false;

    public Scroller mScroller;
    
    private int theme;

    public BaseReadView(Context context, String bookId, List<Chapter> chaptersList,
                        OnReadStateChangeListener listener) {
        super(context);
        this.listener = listener;
        this.bookId = bookId;

        mScreenWidth = ScreenUtils.getScreenWidth();
        mScreenHeight = ScreenUtils.getScreenHeight();

        mCurPageBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ARGB_8888);
        mNextPageBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ARGB_8888);
        mCurrentPageCanvas = new Canvas(mCurPageBitmap);
        mNextPageCanvas = new Canvas(mNextPageBitmap);

        mScroller = new Scroller(getContext());

        pagefactory = new PageFactory(getContext(), bookId, chaptersList);
        pagefactory.setOnReadStateChangeListener(listener);
    }

    public synchronized void init(int theme) {
        if (!isPrepared) {
            try {
            	this.theme = theme;
                pagefactory.setBgBitmap(ThemeManager.getThemeDrawable(theme));
                // 自动跳转到上次阅读位置
                int pos[] = SettingManager.getInstance().getReadProgress(bookId);
                int ret = pagefactory.openBook(pos[0], new int[]{pos[1], pos[2]});
                Log.i(TAG,"上次阅读位置：chapter=" + pos[0] + " startPos=" + pos[1] + " endPos=" + pos[2]);
                if (ret == 0) {
                    listener.onLoadChapterFailure(pos[0]);
                    return;
                }
                pagefactory.onDraw(mCurrentPageCanvas);
                postInvalidate();
            } catch (Exception e) {
            }
            isPrepared = true;
        }
    }
    
    public void open(int chapter) {
    	 int ret = pagefactory.openBook(chapter, new int[]{0, 0});
         if (ret == 0) {
             listener.onLoadChapterFailure(chapter);
             return;
         }
         pagefactory.onDraw(mCurrentPageCanvas);
         postInvalidate();
    }
    
    public void setCurrentChapter(int chapter) {
    	SettingManager.getInstance().saveReadProgress(bookId, chapter, 0, 0);
    }

    private int dx, dy;
    private long et = 0;
    private boolean cancel = false;
    private boolean center = false;

	@Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                et = System.currentTimeMillis();
                dx = (int) e.getX();
                dy = (int) e.getY();
                mTouch.x = dx;
                mTouch.y = dy;
                actiondownX = dx;
                actiondownY = dy;
                touch_down = 0;
                pagefactory.onDraw(mCurrentPageCanvas);
                if (actiondownX >= mScreenWidth / 3 && actiondownX <= mScreenWidth * 2 / 3
                        && actiondownY >= mScreenHeight / 3 && actiondownY <= mScreenHeight * 2 / 3) {
                    center = true;
                } else {
                    center = false;
                    calcCornerXY(actiondownX, actiondownY);
                    if (actiondownX < mScreenWidth / 2) {// 从左翻
                        BookStatus status = pagefactory.prePage();
                        if (status == BookStatus.NO_PRE_PAGE) {
//                            ToastUtils.showSingleToast("没有上一页啦");
                            return false;
                        } else if (status == BookStatus.LOAD_SUCCESS) {
                            abortAnimation();
                            pagefactory.onDraw(mNextPageCanvas);
                        } else {
                            return false;
                        }
                    } else if (actiondownX >= mScreenWidth / 2) {// 从右翻
                        BookStatus status = pagefactory.nextPage();
                        if (status == BookStatus.NO_NEXT_PAGE) {
//                            ToastUtils.showSingleToast("没有下一页啦");
                            return false;
                        } else if (status == BookStatus.LOAD_SUCCESS) {
                            abortAnimation();
                            pagefactory.onDraw(mNextPageCanvas);
                        } else {
                            return false;
                        }
                    }
                    listener.onFlip();
                    setBitmaps(mCurPageBitmap, mNextPageBitmap);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (center)
                    break;
                int mx = (int) e.getX();
                int my = (int) e.getY();
                cancel = (actiondownX < mScreenWidth / 2 && mx < mTouch.x) || (actiondownX > mScreenWidth / 2 && mx > mTouch.x);
                mTouch.x = mx;
                mTouch.y = my;
                touch_down = mTouch.x - actiondownX;
                this.postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                long t = System.currentTimeMillis();
                int ux = (int) e.getX();
                int uy = (int) e.getY();

                if (center) { // ACTION_DOWN的位置在中间，则不响应滑动事件
                    resetTouchPoint();
                    if (Math.abs(ux - actiondownX) < 5 && Math.abs(uy - actiondownY) < 5) {
                        listener.onCenterClick();
                        return false;
                    }
                    break;
                }

                if ((Math.abs(ux - dx) < 10) && (Math.abs(uy - dy) < 10)) {
                    if ((t - et < 1000)) { // 单击
                        startAnimation();
                    } else { // 长按
                        pagefactory.cancelPage();
                        restoreAnimation();
                    }
                    postInvalidate();
                    return true;
                }
                if (cancel) {
                    pagefactory.cancelPage();
                    restoreAnimation();
                    postInvalidate();
                } else {
                    startAnimation();
                    postInvalidate();
                }
                cancel = false;
                center = false;
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        calcPoints();
        drawCurrentPageArea(canvas);
        drawNextPageAreaAndShadow(canvas);
        drawCurrentPageShadow(canvas);
        drawCurrentBackArea(canvas);
    }

    protected abstract void drawNextPageAreaAndShadow(Canvas canvas);

    protected abstract void drawCurrentPageShadow(Canvas canvas);

    protected abstract void drawCurrentBackArea(Canvas canvas);

    protected abstract void drawCurrentPageArea(Canvas canvas);

    protected abstract void calcPoints();

    protected abstract void calcCornerXY(float x, float y);

    /**
     * 开启翻页
     */
    protected abstract void startAnimation();

    /**
     * 停止翻页动画（滑到一半调用停止的话  翻页效果会卡住 可调用#{restoreAnimation} 还原效果）
     */
    protected abstract void abortAnimation();

    /**
     * 还原翻页
     */
    protected abstract void restoreAnimation();

    protected abstract void setBitmaps(Bitmap mCurPageBitmap, Bitmap mNextPageBitmap);

    public abstract void setTheme(int theme);

    /**
     * 复位触摸点位
     */
    protected void resetTouchPoint() {
        mTouch.x = 0.1f;
        mTouch.y = 0.1f;
        touch_down = 0;
        calcCornerXY(mTouch.x, mTouch.y);
    }

    /**总是打开第一个page*/
	public void jumpToChapter(int chapter) {
		if(chapter < 1 ) {
			return;
		}
        resetTouchPoint();
        int r = pagefactory.openBook(chapter, new int[]{0, 0});
        if(r == 0) {
        	pagefactory.onLoadChapterFailure(chapter);
        }
        pagefactory.onDraw(mCurrentPageCanvas);
        pagefactory.onDraw(mNextPageCanvas);
        postInvalidate();
    }
    
	/**往前翻页未下载的章节时，打开最后一个page*/
	public void openLoadedChapter(int chapter) {
		if(chapter < 1 ) {
			return;
		}
        resetTouchPoint();
        pagefactory.setLoadFinish(chapter, true);
        pagefactory.onDraw(mCurrentPageCanvas);
        pagefactory.onDraw(mNextPageCanvas);
        postInvalidate();
    }

    public void nextPage() {
        BookStatus status = pagefactory.nextPage();
        if (status == BookStatus.NO_NEXT_PAGE) {
//            ToastUtils.showSingleToast("没有下一页啦");
            return;
        } else if (status == BookStatus.LOAD_SUCCESS) {
            if (isPrepared) {
                pagefactory.onDraw(mCurrentPageCanvas);
                pagefactory.onDraw(mNextPageCanvas);
                postInvalidate();
            }
        } else {
            return;
        }

    }

    public void prePage() {
        BookStatus status = pagefactory.prePage();
        if (status == BookStatus.NO_PRE_PAGE) {
//            ToastUtils.showSingleToast("没有上一页啦");
            return;
        } else if (status == BookStatus.LOAD_SUCCESS) {
            if (isPrepared) {
                pagefactory.onDraw(mCurrentPageCanvas);
                pagefactory.onDraw(mNextPageCanvas);
                postInvalidate();
            }
        } else {
            return;
        }
    }

    public synchronized void setFontSize(final int fontSizePx) {
        resetTouchPoint();
        pagefactory.setTextFont(fontSizePx);
        if (isPrepared) {
            pagefactory.onDraw(mCurrentPageCanvas);
            pagefactory.onDraw(mNextPageCanvas);
            //SettingManager.getInstance().saveFontSize(bookId, fontSizePx);
            SettingManager.getInstance().saveFontSize(fontSizePx);
            postInvalidate();
        }
    }

    public synchronized void setTextColor(int textColor, int titleColor) {
        resetTouchPoint();
        pagefactory.setTextColor(textColor, titleColor);
        if (isPrepared) {
            pagefactory.onDraw(mCurrentPageCanvas);
            pagefactory.onDraw(mNextPageCanvas);
            postInvalidate();
        }
    }

    public void setBattery(int battery) {
        pagefactory.setBattery(battery);
        if (isPrepared) {
            pagefactory.onDraw(mCurrentPageCanvas);
            postInvalidate();
        }
    }

    public void setTime(String time) {
        pagefactory.setTime(time);
    }

    public void setPosition(int[] pos) {
        int ret = pagefactory.openBook(pos[0], new int[]{pos[1], pos[2]});
        if (ret == 0) {
            listener.onLoadChapterFailure(pos[0]);
            return;
        }
        pagefactory.onDraw(mCurrentPageCanvas);
        postInvalidate();
    }

    public int[] getReadPos() {
        return pagefactory.getPosition();
    }

    public String getHeadLine() {
        return pagefactory.getHeadLineStr().replaceAll("@", "");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (pagefactory != null) {
            pagefactory.recycle();
        }

        if (mCurPageBitmap != null && !mCurPageBitmap.isRecycled()) {
            mCurPageBitmap.recycle();
            mCurPageBitmap = null;
            Log.d(TAG,"mCurPageBitmap recycle");
        }

        if (mNextPageBitmap != null && !mNextPageBitmap.isRecycled()) {
            mNextPageBitmap.recycle();
            mNextPageBitmap = null;
            Log.d(TAG,"mNextPageBitmap recycle");
        }
    }
    
    /**if hide NAVIGATION,this will be needed*/
//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//    	super.onSizeChanged(w, h, oldw, oldh);
//    	pagefactory.setScreen(w, h);
//        mScreenWidth = w;
//        mScreenHeight = h;
//
//        mCurPageBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ARGB_8888);
//        mNextPageBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ARGB_8888);
//        mCurrentPageCanvas = new Canvas(mCurPageBitmap);
//        mNextPageCanvas = new Canvas(mNextPageBitmap);
//        
//        if(isPrepared) {
//        	isPrepared = false;
//        	init(theme);
//        }
//    }
}
