package com.chrynan.reactions;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by chRyNaN on 2/26/2016.
 */
public class ReactionView extends ViewGroup {
    private static final String TAG = ReactionView.class.getSimpleName();
    //It seems the ReactionView in Facebook has a set size. If you switch to landscape mode the size stays the same, icons and all.
    //So, I should set these to hard coded values
    public static final int DEFAULT_WIDTH = 275;
    public static final int DEFAULT_HEIGHT = 40;
    private float vWidth;
    private float vHeight;
    private float xPad;
    private float yPad;
    private float bPad;
    private float smallIconSize;
    private float regIconSize;
    private float largeIconSize;
    private float currentIconSize;
    private float iconTextSize;
    private float distance; //How far from the selected button do we show this view.
    //The last selected positions
    private float sX;
    private float sY;
    private float xStart;
    private float yStart;
    private float cornerSize;

    private float availableWidth, availableHeight;

    private Path backgroundPath;
    private RectF backgroundRect;
    private Paint backgroundPaint;

    private RoundedView background;
    private IconView like;
    private IconView love;
    private IconView laugh;
    private IconView wow;
    private IconView sad;
    private IconView angry;

    public ReactionView(Context context) {
        super(context);
        init(context, null);
    }

    public ReactionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ReactionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ReactionView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        //Set paint up to draw the background
        sX = 0f;
        sY = 0f;
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(Color.parseColor("#FFFFFF"));
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setAlpha(230);
        addIcons(context);
    }

    private void addIcons(Context context){
        background = new RoundedView(context);
        like = new IconView(context, ReactionPopup.Reaction.LIKE);
        love = new IconView(context, ReactionPopup.Reaction.LOVE);
        laugh = new IconView(context, ReactionPopup.Reaction.LAUGH);
        wow = new IconView(context, ReactionPopup.Reaction.WOW);
        sad = new IconView(context, ReactionPopup.Reaction.SAD);
        angry = new IconView(context, ReactionPopup.Reaction.ANGRY);
        addView(background);
        addView(like);
        Log.d(TAG, "addView called on like");
        addView(love);
        Log.d(TAG, "addView called on love");
        addView(laugh);
        Log.d(TAG, "addView called on laugh");
        addView(wow);
        addView(sad);
        addView(angry);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH){
        super.onSizeChanged(w, h, oldW, oldH);
        Log.d(TAG, "onSizeChanged: oldW = " + oldW + "; oldH = " + oldH + "; w = " + w + "; h = " + h);
        availableWidth = w;
        availableHeight = h;

        vWidth = ViewUtils.toPixel(DEFAULT_WIDTH, getContext());
        xPad = (getPaddingLeft() + getPaddingRight() <= 0) ? ViewUtils.toPixel(8, getContext()) :
                (getPaddingLeft() + getPaddingRight()) / 2;
        yPad = (getPaddingTop() + getPaddingBottom() <= 0) ? xPad :
                (getPaddingTop() + getPaddingBottom()) / 2;
        bPad = xPad / 2;
        regIconSize = (vWidth - (2 * xPad + 5 * bPad)) / 6;
        vHeight = regIconSize + (2 * yPad);
        distance = vHeight;
        largeIconSize = 2 * regIconSize;
        smallIconSize = ((vWidth - (2 * xPad + 5 * bPad)) - largeIconSize) / 5;
        currentIconSize = regIconSize;
        iconTextSize = ViewUtils.toPixel(16, getContext());

        cornerSize = xPad + currentIconSize / 2;
        float centerX = ViewUtils.getCenterPosition(0, availableWidth);
        xStart = (sX + vWidth >= (centerX - (vWidth / 2))) ? (centerX - (vWidth / 2) + cornerSize) : sX + cornerSize;
        yStart = ((sY - (distance + vHeight)) < 0) ? (sY + distance) : (sY - (distance + vHeight));

        setPadding((int) xPad, (int) yPad, (int) xPad, (int) yPad);
    }

    @Override
    protected void dispatchDraw(Canvas canvas){
        Log.d(TAG, "dispatchDraw");
        /* Rather than drawing the background here I created a custom View for the background.
         * That way it should be easier to handle the animations for the background.
         * Keeping the code for now just incase I choose to revert back to this approach.
        //Draw the background rounded rectangle
        backgroundPath = new Path();
        backgroundPath.moveTo(xStart, yStart);
        //Top line between curves
        backgroundPath.lineTo(xStart + (vWidth - (2 * cornerSize)), yStart);
        //First curve, right side
        backgroundRect = new RectF();
        backgroundRect.left = (xStart + (vWidth) - (2 * cornerSize));
        backgroundRect.right = backgroundRect.left + cornerSize;
        backgroundRect.top = yStart;
        backgroundRect.bottom = yStart + vHeight;
        backgroundPath.arcTo(backgroundRect, 270, 180);
        //Bottom line between curves
        backgroundPath.lineTo(xStart, yStart + vHeight);
        //Second curve, left side
        backgroundRect.left = xStart - cornerSize;
        backgroundRect.right = xStart;
        backgroundRect.top = yStart;
        backgroundRect.bottom = yStart + vHeight;
        backgroundPath.arcTo(backgroundRect, 90, 180);
        backgroundPath.close();
        canvas.drawPath(backgroundPath, backgroundPaint);
        */
        //Draw the text if needed
        for(int i = 0; i < getChildCount(); i++){
            if(getChildAt(i) instanceof IconView) {
                final IconView child = (IconView) getChildAt(i);
                if (child.getMode() == IconView.Mode.LARGE) {
                    RectF rect = child.getTextBackgroundRectF(0, availableWidth, 0, availableHeight);
                    //Draw the text background
                    canvas.drawRect(child.getTextBackgroundRectF(0, availableWidth, 0, availableHeight),
                            child.getTextBackgroundPaint());
                    //Draw the text
                    RectF textRect = child.getTextRectF(0, availableWidth, 0, availableHeight);
                    canvas.drawText(child.getText(), textRect.left,
                            textRect.top + child.getTextHeight(), child.getTextPaint());
                }
            }
        }
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG, "onLayout: l = " + l + "; t = " + t + "; r = " + r + "; b = " + b);
        //If there are small icon sizes adjust the height of the background to them
        background.setPadding((int) xPad, (int) yPad, (int) xPad, (int) yPad);
        if(getSmallestChildMode() == IconView.Mode.SMALL){
            vHeight = (int) (smallIconSize + 2 * yPad);
            yStart = yStart + (regIconSize - smallIconSize);
        }
        final int count = getChildCount();
        //To animate the size changes
        AnimatorSet animSet = new AnimatorSet();
        ValueAnimator prevAnim = null;
        ValueAnimator tempAnim = null;
        for(int i = 0; i < count; i++){
            if(getChildAt(i) instanceof IconView) {
                final IconView child = (IconView) getChildAt(i);
                if (child.getVisibility() != GONE) {
                    int s;
                    switch (child.getMode()) {
                        case SMALL:
                            s = (int) smallIconSize;
                            break;
                        default:
                        case MEDIUM:
                            s = (int) regIconSize;
                            break;
                        case LARGE:
                            s = (int) largeIconSize;
                            break;
                    }
                    tempAnim = ValueAnimator.ofFloat((float) child.getCurrentSize(), (float) s);
                    tempAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float v = (float) animation.getAnimatedValue();
                            //Slow, think of another way to do this
                            float prevX = 0f;
                            for (int i = 1; i < indexOfChild(child); i++) {
                                prevX = prevX + getChildAt(i).getWidth() + bPad;
                            }
                            int left, right, top, bottom;
                            left = (int) (xStart - (regIconSize / 2) + prevX);
                            right = (int) (left + v);
                            bottom = (int) (yStart + vHeight - yPad);
                            top = (int) (bottom - v);
                            child.layout(left, top, right, bottom);
                        }
                    });
                    if (prevAnim != null) {
                        animSet.play(tempAnim).with(prevAnim);
                    }
                    prevAnim = tempAnim;
                }
            }else if(getChildAt(i) instanceof RoundedView){
                final RoundedView child = (RoundedView) getChildAt(i);
                Log.d(TAG, "onLayout: xStart = " + xStart + "; cornerSize = " + cornerSize);
                ValueAnimator heightAnim = ValueAnimator.ofFloat(child.getY(), yStart);
                heightAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        child.layout(Math.round(xStart - cornerSize), Math.round(yStart),
                                Math.round(xStart + vWidth - cornerSize), Math.round(yStart + vHeight));
                    }
                });
                heightAnim.setDuration(250);
                heightAnim.start();
            }
        }
        animSet.setDuration(100);
        animSet.start();
    }

    private void setChildToLarge(IconView child){
        Log.d(TAG, "setChildToLarge");
        IconView view;
        for(int i = 0; i < getChildCount(); i++){
            if(getChildAt(i) instanceof IconView) {
                view = (IconView) getChildAt(i);
                view.setMode(IconView.Mode.SMALL);
            }
        }
        child.setMode(IconView.Mode.LARGE);
        requestLayout();
    }

    private void resetChildrenToNormalSize(){
        IconView view;
        for(int i = 0; i < getChildCount(); i++){
            if(getChildAt(i) instanceof IconView) {
                view = (IconView) getChildAt(i);
                view.setMode(IconView.Mode.MEDIUM);
            }
        }
        requestLayout();
    }

    public IconView.Mode getSmallestChildMode(){
        IconView.Mode mode = IconView.Mode.LARGE;
        IconView view;
        for(int i = 0; i < getChildCount(); i++){
            if(getChildAt(i) instanceof IconView) {
                view = (IconView) getChildAt(i);
                if (view.getMode().compareTo(mode) < 0) {
                    mode = view.getMode();
                }
            }
        }
        return mode;
    }

    public IconView.Mode getLargestChildMode(){
        IconView.Mode mode = IconView.Mode.SMALL;
        IconView view;
        for(int i = 0; i < getChildCount(); i++){
            if(getChildAt(i) instanceof IconView) {
                view = (IconView) getChildAt(i);
                if (view.getMode().compareTo(mode) > 0) {
                    mode = view.getMode();
                }
            }
        }
        return mode;
    }

    public IconView getSmallestChild(){
        IconView.Mode mode = IconView.Mode.LARGE;
        IconView view, smallestView = null;
        for(int i = 0; i < getChildCount(); i++){
            view = (IconView) getChildAt(i);
            if(view.getMode().compareTo(mode) < 0){
                mode = view.getMode();
                smallestView = view;
            }
        }
        return smallestView;
    }

    public IconView getLargestChild(){
        IconView.Mode mode = IconView.Mode.SMALL;
        IconView view, largestView = null;
        for(int i = 0; i < getChildCount(); i++){
            view = (IconView) getChildAt(i);
            if(view.getMode().compareTo(mode) > 0){
                mode = view.getMode();
                largestView = view;
            }
        }
        return largestView;
    }

}
