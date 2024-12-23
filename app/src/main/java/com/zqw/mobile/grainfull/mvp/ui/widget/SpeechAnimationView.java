package com.zqw.mobile.grainfull.mvp.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.zqw.mobile.grainfull.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import app.dinus.com.loadingdrawable.DensityUtil;

/**
 * 语音动画视图
 */
public class SpeechAnimationView extends SurfaceView implements SurfaceHolder.Callback {
    private DrawThread mDrawThread;
    private AnimationType mAnimationType = AnimationType.WAITING;

    public enum AnimationType {
        Connecting,
        PAUSED,
        WAITING,
        LISTENING,
        ROBOT_THINKING,
        ROBOT_SPEAKING
    }

    public SpeechAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    public void setAnimationType(AnimationType animationType) {
        if (null != mDrawThread) {
            mDrawThread.setAnimationType(animationType);
        }
        mAnimationType = animationType;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 设置SurfaceView为透明
        holder.setFormat(PixelFormat.TRANSPARENT);

        if (null == mDrawThread) {
            mDrawThread = new DrawThread(getContext(), holder);
            mDrawThread.setAnimationType(mAnimationType);
            mDrawThread.setRunning(true);
            mDrawThread.start();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        if (null != mDrawThread) {
            mDrawThread.setRunning(false);
            while (retry) {
                try {
                    mDrawThread.join();
                    mDrawThread = null;
                    retry = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class DrawThread extends Thread {
        private AtomicBoolean isRunning = new AtomicBoolean(false);
        private SurfaceHolder mSurfaceHolder;
        private int mBackgroundColor;
        private Bitmap mAnimationWaitingBmp = null;
        private Context mContext = null;
        private Map<AnimationType, AnimationDrawer> mAnimationDrawers = new HashMap<>();
        private AtomicReference<AnimationType> mAnimationType = new AtomicReference(AnimationType.WAITING);
        private Paint mAssistLinePaint;

        public DrawThread(Context context, SurfaceHolder surfaceHolder) {
            mContext = context;
            this.mSurfaceHolder = surfaceHolder;
            mBackgroundColor = context.getResources().getColor(R.color.layout_base_background);
            mAssistLinePaint = new Paint();
            mAssistLinePaint.setColor(0xFF00FF00);
            mAssistLinePaint.setStyle(Paint.Style.FILL);
            mAssistLinePaint.setStrokeWidth(2f);

            if (null == mAnimationWaitingBmp) {
                mAnimationWaitingBmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.res_animation_waiting);
            }
            mAnimationDrawers = new HashMap<>();
            mAnimationDrawers.put(AnimationType.Connecting, new ThinkingAnimationDrawer());
            mAnimationDrawers.put(AnimationType.PAUSED, new PausedAnimationDrawer(mAnimationWaitingBmp));
            mAnimationDrawers.put(AnimationType.WAITING, new WaitingAnimationDrawer(mAnimationWaitingBmp));
            mAnimationDrawers.put(AnimationType.LISTENING, new ListeningAnimationDrawer(mAnimationWaitingBmp));
            mAnimationDrawers.put(AnimationType.ROBOT_THINKING, new ThinkingAnimationDrawer());

            SpeakingAnimationDrawer speakingAnimationDrawer = new SpeakingAnimationDrawer(1.0f, true);
            speakingAnimationDrawer.setTranslateFromCenter(0f, DensityUtil.dip2px(mContext, 16) / 2f);
            mAnimationDrawers.put(AnimationType.ROBOT_SPEAKING, speakingAnimationDrawer);
        }

        public void setRunning(boolean running) {
            this.isRunning.set(running);
        }

        public void setAnimationType(AnimationType mAnimationType) {
            this.mAnimationType.set(mAnimationType);
        }

        @Override
        public void run() {
            AnimationType runningAnimationType = null;
            long animationProgress = 0;
            long animationStartMillis = 0;

            while (isRunning.get()) {
                Canvas canvas = null;
                AnimationType currentAnimationType = mAnimationType.get();
                if (runningAnimationType == null || runningAnimationType != currentAnimationType) {
                    runningAnimationType = currentAnimationType;
                    animationProgress = 0;
                    animationStartMillis = SystemClock.uptimeMillis();
                } else {
                    animationProgress = (SystemClock.uptimeMillis() - animationStartMillis);
                }
//                Log.i("SpeechAnimationView", "animationProgress: " + animationProgress +", -> " + SystemClock.uptimeMillis());
                try {
                    canvas = mSurfaceHolder.lockCanvas();
                    if (canvas != null) {
                        synchronized (mSurfaceHolder) {
                            // Clear canvas
                            canvas.drawColor(mBackgroundColor);

                            AnimationDrawer animationDrawer = mAnimationDrawers.get(runningAnimationType);
                            animationDrawer.updateProgress(animationProgress);
                            if (null != animationDrawer) {
                                animationDrawer.draw(canvas);
                            }

//                            // 辅助线绘制
//                            canvas.drawLine(0, canvas.getHeight()/2f, canvas.getWidth(), canvas.getHeight()/2f, mAssistLinePaint);
//                            canvas.drawLine(canvas.getWidth()/2f, 0, canvas.getWidth()/2f, canvas.getHeight(), mAssistLinePaint);
                        }
                    }
                } finally {
                    if (canvas != null) {
                        mSurfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }

                // Sleep to control frame rate
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private abstract class AnimationDrawer {
        protected Paint mPaint = null;
        protected long mProgress = 0;
        protected PointF mTranslateFromCenter = new PointF(0f, 0f);
        private List<AnimationDrawer> mChainDrawerList = new ArrayList<>();

        public AnimationDrawer() {
            mPaint = new Paint();
            mPaint.setColor(Color.WHITE);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setAntiAlias(true);
        }

        public abstract void draw(Canvas canvas);

        public void updateProgress(long progress) {
            mProgress = progress;
            if (null != mChainDrawerList && !mChainDrawerList.isEmpty()) {
                for (AnimationDrawer animationDrawer : mChainDrawerList) {
                    animationDrawer.updateProgress(progress);
                }
            }
        }

        public void setTranslateFromCenter(float translateX, float translateY) {
            this.mTranslateFromCenter.x = translateX;
            this.mTranslateFromCenter.y = translateY;
        }

        public void addChainDrawer(AnimationDrawer animationDrawer) {
            mChainDrawerList.add(animationDrawer);
        }
    }

    private class PausedAnimationDrawer extends WaitingAnimationDrawer {
        public PausedAnimationDrawer(Bitmap waitingBmp) {
            super(waitingBmp);
        }

        @Override
        public void draw(Canvas canvas) {
            super.draw(canvas);
        }

        @Override
        protected float updateDegree() {
            return 0.0f;
        }
    }

    private class WaitingAnimationDrawer extends AnimationDrawer {
        protected Bitmap mWaitingBmp = null;
        protected final Matrix mMatrix = new Matrix();
        private final static float ONE_CYCLE_TIME = 1500f;
        protected int mBmpRenderSize = 0;

        public WaitingAnimationDrawer(Bitmap waitingBmp) {
            super();
            mWaitingBmp = waitingBmp;
            mBmpRenderSize = (int) DensityUtil.dip2px(getContext(), 156);
        }

        @Override
        public void draw(Canvas canvas) {
            mMatrix.reset();

            // 设置变换矩阵，进行旋转和缩放操作
            float degree = updateDegree();
            float scale = ((mBmpRenderSize * 1.0f) / mWaitingBmp.getWidth());
            mMatrix.postRotate(degree, mWaitingBmp.getWidth() / 2f, mWaitingBmp.getHeight() / 2f); // 旋转
            mMatrix.postScale(scale, scale, mWaitingBmp.getWidth() / 2f, mWaitingBmp.getHeight() / 2f); // 缩放
            // 平移到画布中心
            mMatrix.postTranslate(
                    (canvas.getWidth() - mWaitingBmp.getWidth()) / 2f + mTranslateFromCenter.x,
                    (canvas.getHeight() - mWaitingBmp.getHeight()) / 2f + mTranslateFromCenter.y
            );

            canvas.drawBitmap(mWaitingBmp, mMatrix, mPaint);
        }

        protected float updateDegree() {
            return ((mProgress % ONE_CYCLE_TIME) / ONE_CYCLE_TIME) * 360f;
        }
    }

    private class ListeningAnimationDrawer extends WaitingAnimationDrawer {
        private SpeakingAnimationDrawer mSpeakingAnimationDrawer;
        private int mDrawerPadding = 0;

        public ListeningAnimationDrawer(Bitmap waitingBmp) {
            super(waitingBmp);
            mDrawerPadding = (int) DensityUtil.dip2px(getContext(), 32);
            mSpeakingAnimationDrawer = new SpeakingAnimationDrawer(0.36f, false);

//            setTranslateFromCenter(0f, -(mSpeakingAnimationDrawer.getRoundRectMaxHeight() + mDrawerPadding/2f));
            mSpeakingAnimationDrawer.setTranslateFromCenter(0f, mBmpRenderSize / 2f + mDrawerPadding);
            addChainDrawer(mSpeakingAnimationDrawer);
        }

        @Override
        public void draw(Canvas canvas) {
            super.draw(canvas);
            mSpeakingAnimationDrawer.draw(canvas);
        }
    }


    private class ListeningToThinkAnimationDrawer extends WaitingAnimationDrawer {
        private long mAnimationDuration = 500;
        private long mHalfAnimationDuration = mAnimationDuration / 2;
        private int mBigCircleRadius = 0;
        private int mSmallCircleRadius = 0;
        private int mSmallCircleMargin = 0;
        private PointF mDrawerCenterPos = new PointF();

        public ListeningToThinkAnimationDrawer() {
            super(null);
            mBigCircleRadius = (int) (mBmpRenderSize / 2.0);
        }

        public void setSmallCircleMargin(int smallCircleMargin) {
            this.mSmallCircleMargin = smallCircleMargin;
        }

        public void setSmallCircleRadius(int smallCircleRadius) {
            this.mSmallCircleRadius = smallCircleRadius;
        }

        public void setDrawerCenterPos(float posX, float posY) {
            this.mDrawerCenterPos.x = posX;
            this.mDrawerCenterPos.y = posY;
        }

        @Override
        public void draw(Canvas canvas) {
            if (mProgress > 0 && mProgress <= mAnimationDuration) {
                if (mProgress <= mHalfAnimationDuration) {
                    float scaleSpeed = (mBigCircleRadius - mSmallCircleRadius) * 1.0f / mHalfAnimationDuration; // 半径缩小速度
                    float radius = mBigCircleRadius - scaleSpeed * mProgress;

                    canvas.drawCircle(mDrawerCenterPos.x, mDrawerCenterPos.y, radius, mPaint);
                } else {
                    float translateSpeed = (mSmallCircleRadius * 2 + mSmallCircleMargin) * 1.0f / (mAnimationDuration - mHalfAnimationDuration);
                    float translateDistance = (mProgress - mHalfAnimationDuration) * translateSpeed;

                    // 中间
                    canvas.drawCircle(mDrawerCenterPos.x, mDrawerCenterPos.y, mSmallCircleRadius, mPaint);
                    // 左边
                    canvas.drawCircle(mDrawerCenterPos.x - translateDistance, mDrawerCenterPos.y, mSmallCircleRadius, mPaint);
                    // 右边
                    canvas.drawCircle(mDrawerCenterPos.x + translateDistance, mDrawerCenterPos.y, mSmallCircleRadius, mPaint);
                }
            }
        }

        public long getAnimationDuration() {
            return mAnimationDuration;
        }
    }

    private class ThinkingAnimationDrawer extends AnimationDrawer {

        protected int mRadiusSmall = 0;
        protected int mRadiusBig = 0;
        protected int mMargin = 0;
        protected PointF[] mCirclePositionArray = null;

        private long animationCycle = 1000;
        private ListeningToThinkAnimationDrawer mListeningToThinkingAnimationDrawer = null;

        public ThinkingAnimationDrawer() {
            super();

            mRadiusSmall = (int) DensityUtil.dip2px(getContext(), 16);
            mRadiusBig = (int) DensityUtil.dip2px(getContext(), 21);
            mMargin = (int) DensityUtil.dip2px(getContext(), 10);
            mListeningToThinkingAnimationDrawer = new ListeningToThinkAnimationDrawer();
            mListeningToThinkingAnimationDrawer.setSmallCircleRadius(mRadiusSmall);
            mListeningToThinkingAnimationDrawer.setSmallCircleMargin(mMargin);
            addChainDrawer(mListeningToThinkingAnimationDrawer);
        }

        @Override
        public void draw(Canvas canvas) {
            // 绘制过度动画
            if (mProgress <= mListeningToThinkingAnimationDrawer.getAnimationDuration()) {
                mListeningToThinkingAnimationDrawer.setDrawerCenterPos(canvas.getWidth() / 2.0f, canvas.getHeight() / 2.0f);
                mListeningToThinkingAnimationDrawer.draw(canvas);
                return;
            }

            initPosition(canvas);

            long animationProgress = (mProgress - mListeningToThinkingAnimationDrawer.getAnimationDuration()) % animationCycle;
            long stepDuration = animationCycle / 5;
            int bigScaleIndex = -1;
            int smallScaleIndex = -1;
            long scaleProgress = 0;
            if (animationProgress > 0 && animationProgress <= stepDuration) {
                bigScaleIndex = 0;
                smallScaleIndex = -1;
                scaleProgress = animationProgress;
            } else if (animationProgress > stepDuration && animationProgress <= stepDuration * 2) {
                bigScaleIndex = 1;
                smallScaleIndex = 0;
                scaleProgress = animationProgress - stepDuration;
            } else if (animationProgress > stepDuration * 2 && animationProgress <= stepDuration * 3) {
                bigScaleIndex = 2;
                smallScaleIndex = 1;
                scaleProgress = animationProgress - stepDuration * 2;
            } else if (animationProgress > stepDuration * 3 && animationProgress <= stepDuration * 4) {
                bigScaleIndex = -1;
                smallScaleIndex = 2;
                scaleProgress = animationProgress - stepDuration * 3;
            } else if (animationProgress > stepDuration * 4 && animationProgress <= animationCycle) {
                bigScaleIndex = -1;
                smallScaleIndex = -1;
                scaleProgress = animationProgress - stepDuration * 4;
            }

            for (int i = 0; i < 3; i++) {
                PointF pos = mCirclePositionArray[i];
                float radius = mRadiusSmall;
                if (i == bigScaleIndex) {
                    radius += (scaleProgress * 1.0f / stepDuration) * (mRadiusBig - mRadiusSmall);
                } else if (i == smallScaleIndex) {
                    radius += ((stepDuration - scaleProgress) * 1.0f / stepDuration) * (mRadiusBig - mRadiusSmall);
                }
                canvas.drawCircle(pos.x, pos.y, radius, mPaint);
            }
        }

        private void initPosition(Canvas canvas) {
            if (null == mCirclePositionArray) {
                mCirclePositionArray = new PointF[3];
                mCirclePositionArray[1] = new PointF();
                mCirclePositionArray[1].x = canvas.getWidth() / 2.0f;
                mCirclePositionArray[1].y = canvas.getHeight() / 2.0f;

                mCirclePositionArray[0] = new PointF();
                mCirclePositionArray[0].x = mCirclePositionArray[1].x - mMargin - mRadiusSmall * 2;
                mCirclePositionArray[0].y = mCirclePositionArray[1].y;

                mCirclePositionArray[2] = new PointF();
                mCirclePositionArray[2].x = mCirclePositionArray[1].x + mMargin + mRadiusSmall * 2;
                mCirclePositionArray[2].y = mCirclePositionArray[1].y;
            }
        }
    }

    private class ThinkingToSpeakingDrawer extends AnimationDrawer {
        private long mAnimationDuration = 500;
        private long mScaleAnimationDuration = 100;
        private int mThinkingSmallCircleRadius = 0;
        private int mThinkingSmallCircleMargin = 0;
        private int mSpeakingSmallCircleRadius = 0;
        private int mSpeakingSmallCircleMargin = 0;
        private int mSpeakingSmallCircleNum = 0;
        private PointF mDrawerCenterPos = new PointF();

        public ThinkingToSpeakingDrawer() {
            super();

            mThinkingSmallCircleRadius = (int) DensityUtil.dip2px(getContext(), 16);
            mThinkingSmallCircleMargin = (int) DensityUtil.dip2px(getContext(), 10);
        }

        public void setSpeakingSmallCircleRadius(int speakingSmallCircleRadius) {
            this.mSpeakingSmallCircleRadius = speakingSmallCircleRadius;
        }

        public void setSpeakingSmallCircleMargin(int speakingSmallCircleMargin) {
            this.mSpeakingSmallCircleMargin = speakingSmallCircleMargin;
        }

        public void setSpeakingSmallCircleNum(int speakingSmallCircleNum) {
            this.mSpeakingSmallCircleNum = speakingSmallCircleNum;
        }

        public void setDrawerCenterPos(float posX, float posY) {
            this.mDrawerCenterPos.x = posX;
            this.mDrawerCenterPos.y = posY;
        }

        @Override
        public void draw(Canvas canvas) {
            if (mProgress > 0 && mProgress <= mAnimationDuration) {
                float thinkingSmallCircleXArray[] = new float[3];
                thinkingSmallCircleXArray[1] = mDrawerCenterPos.x;
                thinkingSmallCircleXArray[0] = thinkingSmallCircleXArray[1] - mThinkingSmallCircleRadius * 2 - mThinkingSmallCircleMargin;
                thinkingSmallCircleXArray[2] = thinkingSmallCircleXArray[1] + mThinkingSmallCircleRadius * 2 + mThinkingSmallCircleMargin;

                float speakingSmallCircleXArray[] = new float[mSpeakingSmallCircleNum];
                float drawerLeftX = mDrawerCenterPos.x - ((mSpeakingSmallCircleNum * mSpeakingSmallCircleRadius * 2) + mSpeakingSmallCircleNum * mSpeakingSmallCircleMargin) / 2f;
                for (int i = 0; i < mSpeakingSmallCircleNum; i++) {
                    speakingSmallCircleXArray[i] = drawerLeftX + mSpeakingSmallCircleRadius * 2 * i + mSpeakingSmallCircleMargin * i;
                }

                if (mProgress <= mScaleAnimationDuration) {
                    float scaleSpeed = (mThinkingSmallCircleRadius - mSpeakingSmallCircleRadius) * 1.0f / mScaleAnimationDuration;
                    float radius = mThinkingSmallCircleRadius - scaleSpeed * mProgress;

                    for (int i = 0; i < 3; i++) {
                        canvas.drawCircle(thinkingSmallCircleXArray[i], mDrawerCenterPos.y, radius, mPaint);
                    }
                } else {
                    int sperateIndex = mSpeakingSmallCircleNum / 3;
                    for (int i = 0; i < mSpeakingSmallCircleNum; i++) {
                        float fromX = thinkingSmallCircleXArray[1];
                        if (i < sperateIndex) {
                            fromX = thinkingSmallCircleXArray[0];
                        } else if (i >= (mSpeakingSmallCircleNum - sperateIndex)) {
                            fromX = thinkingSmallCircleXArray[2];
                        }
                        float toX = speakingSmallCircleXArray[i];
                        float translateSpeed = (toX - fromX) * 1f / (mAnimationDuration - mScaleAnimationDuration);
                        float translateDistance = (mProgress - mScaleAnimationDuration) * translateSpeed;

                        canvas.drawCircle(fromX + translateDistance, mDrawerCenterPos.y, mSpeakingSmallCircleRadius, mPaint);
                    }
                }
            }
        }

        public long getAnimationDuration() {
            return mAnimationDuration;
        }
    }

    private class SpeakingAnimationDrawer extends AnimationDrawer {
        protected int mRoundRectWidth;// = DensityUtil.dip2px(16);
        protected int mRoundRectMaxHeight;// = DensityUtil.dip2px(80);
        protected int mRoundRectMinHeight;// = DensityUtil.dip2px(16);
        protected int mRoundRectMaxBoost;// = mRoundRectMaxHeight-mRoundRectMinHeight;
        protected int mRoundRectMargin;// = DensityUtil.dip2px(10);
        protected int mRoundRectRadius;// = DensityUtil.dip2px(8);

        protected int mRoundRectNum = 10;

        protected float mScale = 1.0f;
        protected RectF mRoundRectF[] = new RectF[mRoundRectNum];

        protected float mRoundRectSpeed[];

        private class RoundRectStatus {
            long beginTimes = 0;
            float targetHeight = 0;
            float currentHeight = 0;
        }

        protected RoundRectStatus mRoundRectStatus[] = new RoundRectStatus[mRoundRectNum];

        private ThinkingToSpeakingDrawer mThinkingToSpeakingDrawer = null;
        private boolean mUseTransition = false;

        public SpeakingAnimationDrawer(float scale, boolean useTransition) {
            super();
//            Log.i("SpeakingAnimationDrawer@" + hashCode(), "construct");

            mUseTransition = useTransition;
            mThinkingToSpeakingDrawer = new ThinkingToSpeakingDrawer();
            addChainDrawer(mThinkingToSpeakingDrawer);

            mScale = scale;
            updateSizeParam();
            for (int i = 0; i < mRoundRectNum; i++) {
                mRoundRectF[i] = new RectF();
                mRoundRectStatus[i] = new RoundRectStatus();
            }
        }

        @Override
        public void draw(Canvas canvas) {
            // 过渡动画
            if (mUseTransition && mProgress <= mThinkingToSpeakingDrawer.getAnimationDuration()) {
                mThinkingToSpeakingDrawer.setDrawerCenterPos(
                        canvas.getWidth() / 2f + mTranslateFromCenter.x,
                        canvas.getHeight() / 2f + mTranslateFromCenter.y - mRoundRectMinHeight / 2f
                );
                mThinkingToSpeakingDrawer.draw(canvas);
                return;
            }

            long progress = mUseTransition ? mProgress - mThinkingToSpeakingDrawer.getAnimationDuration() : mProgress;
            updateRoundRect(progress, canvas);

            for (int i = 0; i < mRoundRectNum; i++) {
                canvas.drawRoundRect(mRoundRectF[i], mRoundRectRadius, mRoundRectRadius, mPaint);
            }
        }

        public int getRoundRectMaxHeight() {
            return mRoundRectMaxHeight;
        }

        private void updateSizeParam() {
            mRoundRectWidth = (int) (DensityUtil.dip2px(getContext(), 16) * mScale);
            mRoundRectMaxHeight = (int) (DensityUtil.dip2px(getContext(), 80) * mScale);
            mRoundRectMinHeight = (int) (DensityUtil.dip2px(getContext(), 16) * mScale);
            mRoundRectMaxBoost = mRoundRectMaxHeight - mRoundRectMinHeight;
            mRoundRectMargin = (int) (DensityUtil.dip2px(getContext(), 10) * mScale);
            mRoundRectRadius = (int) (DensityUtil.dip2px(getContext(), 8) * mScale);

            mRoundRectSpeed = new float[]{
                    mRoundRectMaxBoost / 474f,
                    mRoundRectMaxBoost / 433f,
                    mRoundRectMaxBoost / 407f,
                    mRoundRectMaxBoost / 458f,
                    mRoundRectMaxBoost / 400f,
                    mRoundRectMaxBoost / 427f,
                    mRoundRectMaxBoost / 441f,
                    mRoundRectMaxBoost / 419f,
                    mRoundRectMaxBoost / 487f,
                    mRoundRectMaxBoost / 442f
            };

            if (mUseTransition) {
                mThinkingToSpeakingDrawer.setSpeakingSmallCircleRadius(mRoundRectWidth / 2);
                mThinkingToSpeakingDrawer.setSpeakingSmallCircleMargin(mRoundRectMargin);
                mThinkingToSpeakingDrawer.setSpeakingSmallCircleNum(mRoundRectNum);
            }
        }

        private int calculateDrawerWidth() {
            return mRoundRectNum * mRoundRectWidth + (mRoundRectNum - 1) * mRoundRectMargin;
        }

        private void updateRoundRect(long progress, Canvas canvas) {
            int drawerWidth = calculateDrawerWidth();

            float drawerLeft = (canvas.getWidth() - drawerWidth) / 2f + mTranslateFromCenter.x;
            float drawerBottom = canvas.getHeight() / 2f + mTranslateFromCenter.y;

            for (int i = 0; i < mRoundRectNum; i++) {
                float speed = mRoundRectSpeed[i];
                float distance = 0.0f;
                int state = 0;
                RoundRectStatus roundRectStatus = mRoundRectStatus[i];
                if (roundRectStatus.beginTimes == 0) {
                    roundRectStatus.targetHeight = (float) (mRoundRectMinHeight + Math.random() * mRoundRectMaxBoost);
                    roundRectStatus.currentHeight = mRoundRectMinHeight;
                    roundRectStatus.beginTimes = progress;
                    state = 1;
                } else {
                    distance = speed * (progress - roundRectStatus.beginTimes);
                    if (mRoundRectMinHeight + distance <= roundRectStatus.targetHeight) {
                        roundRectStatus.currentHeight = mRoundRectMinHeight + distance;
                    } else {
                        roundRectStatus.currentHeight = roundRectStatus.targetHeight - (mRoundRectMinHeight + distance - roundRectStatus.targetHeight);
                    }
                    if (roundRectStatus.currentHeight <= mRoundRectMinHeight) {
                        roundRectStatus.beginTimes = 0;
                        roundRectStatus.currentHeight = mRoundRectMinHeight;
                    }
                    state = 2;
                }

                mRoundRectF[i].left = drawerLeft + mRoundRectWidth * i + i * mRoundRectMargin;
                mRoundRectF[i].top = drawerBottom - roundRectStatus.currentHeight;
                mRoundRectF[i].right = mRoundRectF[i].left + mRoundRectWidth;
                mRoundRectF[i].bottom = drawerBottom;
//                if (i == 0) {
//                    Log.i("SpeakingAnimationDrawer@" + hashCode(), "updateRoundRect: " + mRoundRectF[i]  + ", current@" + roundRectStatus.hashCode() + ": " + roundRectStatus.currentHeight + ", target: " + roundRectStatus.targetHeight +
//                            ", speed: " + speed + ", distance: " + distance + ", state: " + state
//                    );
//                }
            }
        }
    }
}
