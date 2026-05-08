package com.jldevelopers.beforeafter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * ------------------------------------------------------------------------
 * BeforeAfterImageView
 * ------------------------------------------------------------------------
 *
 * A reusable custom compare slider view used for:
 *
 * 1. Showing original image
 * 2. Showing AI/generated image
 * 3. Dragging left/right to compare
 * 4. Playing intro animation
 * 5. Playing automatic preview animation
 *
 * This view keeps ALL compare logic outside the Activity
 * making ConvertActivity much cleaner and easier to maintain.
 *
 * ------------------------------------------------------------------------
 * FEATURES
 * ------------------------------------------------------------------------
 *
 * ✅ Drag compare
 * ✅ Animated reveal
 * ✅ Professional slider handle
 * ✅ Left/right arrows
 * ✅ Smooth clipping
 * ✅ Reusable in any screen
 * ✅ Touch feedback animation
 * ✅ Haptic feedback
 * ✅ Reset support
 *
 * ------------------------------------------------------------------------
 * USAGE
 * ------------------------------------------------------------------------
 *
 * XML:
 *
 * <com.yourpackage.BeforeAfterImageView
 *      android:id="@+id/beforeAfterView"
 *      android:layout_width="match_parent"
 *      android:layout_height="300dp"/>
 *
 *
 * Activity:
 *
 * beforeAfterView.setOriginalBitmap(bitmap);
 * beforeAfterView.setResultDrawable(drawable);
 *
 * beforeAfterView.playRevealAnimation();
 * beforeAfterView.playCompareAnimation();
 *
 * ------------------------------------------------------------------------
 */

public class BeforeAfterImageView extends FrameLayout {

    /* --------------------------------------------------------------------
     * IMAGE VIEWS
     * -------------------------------------------------------------------- */

    // Original image
    private ImageView originalImage;

    // AI/generated/result image
    private ImageView resultImage;

    /* --------------------------------------------------------------------
     * SLIDER UI
     * -------------------------------------------------------------------- */

    // Entire draggable slider layout
    private LinearLayout dividerContainer;

    // Circle drag handle
    private FrameLayout dragHandle;

    /* --------------------------------------------------------------------
     * ANIMATION
     * -------------------------------------------------------------------- */

    // Compare animation reference
    // Stored to avoid memory leaks
    private ValueAnimator compareAnimator;

    // Current reveal position (0f → 1f)
    private float revealPosition = 0.5f;

    /* --------------------------------------------------------------------
     * CONSTRUCTORS
     * -------------------------------------------------------------------- */

    public BeforeAfterImageView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public BeforeAfterImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BeforeAfterImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /* --------------------------------------------------------------------
     * INITIALIZATION
     * -------------------------------------------------------------------- */

    private void init(Context context) {

        // Inflate custom layout
        LayoutInflater.from(context).inflate(R.layout.view_before_after, this, true);

        // Prevent child clipping
        setClipChildren(false);
        setClipToPadding(false);

        // Find views
        originalImage = findViewById(R.id.originalImage);
        resultImage = findViewById(R.id.resultImage);

        dividerContainer = findViewById(R.id.dividerContainer);
        dragHandle = findViewById(R.id.dragHandle);

        // Setup touch handling
        setupTouch();
    }

    /* --------------------------------------------------------------------
     * TOUCH HANDLING
     * -------------------------------------------------------------------- */

    /**
     * Handles user dragging
     */
    @SuppressLint("ClickableViewAccessibility")
    private void setupTouch() {
        setOnTouchListener((v, event) -> {
            switch (event.getAction()) {

                /* --------------------------------------------------------
                 * USER STARTED TOUCHING
                 * -------------------------------------------------------- */
                case MotionEvent.ACTION_DOWN:

                    // Small vibration feedback
                    performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                    // Animate handle slightly larger
                    animateHandlePressed();

                    return true;

                /* --------------------------------------------------------
                 * USER DRAGGING
                 * -------------------------------------------------------- */
                case MotionEvent.ACTION_MOVE:
                    float x = event.getX();

                    // Prevent overflow left
                    if (x < 0) x = 0;

                    // Prevent overflow right
                    if (x > getWidth()) x = getWidth();

                    // Update reveal
                    updateReveal(x);

                    return true;

                /* --------------------------------------------------------
                 * USER RELEASED TOUCH
                 * -------------------------------------------------------- */
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:

                    // Return handle to normal size
                    animateHandleReleased();

                    return true;
            }

            return false;
        });
    }

    /* --------------------------------------------------------------------
     * REVEAL LOGIC
     * -------------------------------------------------------------------- */

    /**
     * Updates compare reveal
     *
     * Example:
     *
     * x = 50%
     *
     * LEFT SIDE:
     * AI image visible
     *
     * RIGHT SIDE:
     * Original image visible
     */
    private void updateReveal(float x) {

        // Store reveal progress
        revealPosition = x / getWidth();

        // Clip result image
        resultImage.setClipBounds(new Rect(0, 0, (int) x, getHeight()));

        // Move divider
        dividerContainer.setX(x - (dividerContainer.getWidth() / 2f));
    }

    /* --------------------------------------------------------------------
     * IMAGE SETTERS
     * -------------------------------------------------------------------- */

    /**
     * Sets original image
     */
    public void setOriginalBitmap(Bitmap bitmap) {
        originalImage.setImageBitmap(bitmap);
    }

    /**
     * Sets generated/result image
     */
    public void setResultDrawable(Drawable drawable) {
        resultImage.setImageDrawable(drawable);
    }

    public Drawable getResultDrawable() {
        return resultImage.getDrawable();
    }

    @Nullable
    public Bitmap getResultBitmap() {

        Drawable drawable = resultImage.getDrawable();

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        return null;
    }

    /* --------------------------------------------------------------------
     * RESET
     * -------------------------------------------------------------------- */

    /**
     * Clears all images
     */
    public void clear() {
        originalImage.setImageDrawable(null);
        resultImage.setImageDrawable(null);

        // Reset reveal to center
        post(() -> updateReveal(getWidth() / 2f));
    }

    /* --------------------------------------------------------------------
     * RESULT INTRO ANIMATION
     * -------------------------------------------------------------------- */

    /**
     * Small scale fade animation
     * when result image first appears
     */
    public void playRevealAnimation() {
        resultImage.setScaleX(0.85f);
        resultImage.setScaleY(0.85f);
        resultImage.setAlpha(0f);

        resultImage.animate()
                .scaleX(1f)
                .scaleY(1f)
                .alpha(1f)
                .setDuration(400)
                .start();
    }

    /* --------------------------------------------------------------------
     * AUTO COMPARE ANIMATION
     * -------------------------------------------------------------------- */

    /**
     * Automatically moves compare slider
     * left → right → center
     *
     * Gives user preview of compare feature
     */
    public void playCompareAnimation() {
        post(() -> {
            // Cancel old animation
            if (compareAnimator != null) {
                compareAnimator.cancel();
            }

            int width = getWidth();

            // Disable touch during animation
            setEnabled(false);

            compareAnimator = ValueAnimator.ofFloat(0, width, width / 2f);
            compareAnimator.setDuration(1800);
            compareAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

            // Animation frame updates
            compareAnimator.addUpdateListener(animation -> {
                float x = (float) animation.getAnimatedValue();
                updateReveal(x);
            });

            // Animation finished
            compareAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    // Re-enable touch
                    setEnabled(true);
                    // Keep centered
                    updateReveal(width / 2f);
                }
            });
            compareAnimator.start();
        });
    }

    /* --------------------------------------------------------------------
     * HANDLE PRESS ANIMATION
     * -------------------------------------------------------------------- */

    /**
     * Slightly enlarge handle when pressed
     */
    private void animateHandlePressed() {
        dragHandle.animate()
                .scaleX(1.15f)
                .scaleY(1.15f)
                .setDuration(120)
                .start();
    }

    /**
     * Return handle to normal size
     */
    private void animateHandleReleased() {
        dragHandle.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(120)
                .start();
    }

    /* --------------------------------------------------------------------
     * CLEANUP
     * -------------------------------------------------------------------- */

    /**
     * Prevent memory leaks
     */
    @Override
    protected void onDetachedFromWindow() {
        // Cancel animation
        if (compareAnimator != null) {
            compareAnimator.cancel();
        }
        super.onDetachedFromWindow();
    }
}