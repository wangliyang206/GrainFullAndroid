package com.zqw.mobile.grainfull.mvp.ui.widget.colorpicker;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zqw.mobile.grainfull.R;
import com.zqw.mobile.grainfull.app.utils.ColorsUtil;
import com.zqw.mobile.grainfull.mvp.ui.widget.colorpicker.builder.ColorWheelRendererBuilder;
import com.zqw.mobile.grainfull.mvp.ui.widget.colorpicker.builder.PaintBuilder;
import com.zqw.mobile.grainfull.mvp.ui.widget.colorpicker.renderer.ColorWheelRenderOption;
import com.zqw.mobile.grainfull.mvp.ui.widget.colorpicker.renderer.ColorWheelRenderer;
import com.zqw.mobile.grainfull.mvp.ui.widget.colorpicker.slider.AlphaSlider;
import com.zqw.mobile.grainfull.mvp.ui.widget.colorpicker.slider.LightnessSlider;

import java.util.ArrayList;


/**
 * 颜色选择器视图
 *
 * @author vondear
 * @date 2018/6/11 11:36:40 整合修改
 */
public class ColorPickerView extends View {
    private static final float STROKE_RATIO = 2f;

    private Bitmap colorWheel;
    private Canvas colorWheelCanvas;
    private int density = 10;

    private float lightness = 1;
    private float alpha = 1;
    private final int backgroundColor = 0x00000000;

    private Integer[] initialColors = new Integer[]{null, null, null, null, null};
    private int colorSelection = 0;
    private Integer initialColor;
    private Integer pickerTextColor;
    private final Paint colorWheelFill = PaintBuilder.newPaint().color(0).build();
    private final Paint selectorStroke1 = PaintBuilder.newPaint().color(0xffffffff).build();
    private final Paint selectorStroke2 = PaintBuilder.newPaint().color(0xff000000).build();
    private final Paint alphaPatternPaint = PaintBuilder.newPaint().build();
    private ColorCircle currentColorCircle;

    private final ArrayList<OnColorChangedListener> colorChangedListeners = new ArrayList<>();
    private final ArrayList<OnColorSelectedListener> listeners = new ArrayList<>();

    private LightnessSlider lightnessSlider;
    private AlphaSlider alphaSlider;
    private EditText colorEdit;
    private LinearLayout colorPreview;
    private ColorWheelRenderer renderer;
    private final TextWatcher colorTextChange = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                int color = Color.parseColor(s.toString());

                // set the color without changing the edit text preventing stack overflow
                setColor(color, false);
            } catch (Exception e) {

            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
    private int alphaSliderViewId, lightnessSliderViewId;

    public ColorPickerView(Context context) {
        super(context);
        initWith(context, null);
    }

    public ColorPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWith(context, attrs);
    }

    public ColorPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWith(context, attrs);
    }

    @TargetApi(21)
    public ColorPickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initWith(context, attrs);
    }

    private void initWith(Context context, AttributeSet attrs) {
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorPickerPreference);

        density = typedArray.getInt(R.styleable.ColorPickerPreference_density, 10);
        initialColor = typedArray.getInt(R.styleable.ColorPickerPreference_initialColor, 0xffffffff);

        pickerTextColor = typedArray.getInt(R.styleable.ColorPickerPreference_pickerColorEditTextColor, 0xffffffff);

        WHEEL_TYPE wheelType = WHEEL_TYPE.indexOf(typedArray.getInt(R.styleable.ColorPickerPreference_wheelType, 0));
        ColorWheelRenderer renderer = ColorWheelRendererBuilder.getRenderer(wheelType);

        alphaSliderViewId = typedArray.getResourceId(R.styleable.ColorPickerPreference_alphaSliderView, 0);
        lightnessSliderViewId = typedArray.getResourceId(R.styleable.ColorPickerPreference_lightnessSliderView, 0);

        setRenderer(renderer);
        setDensity(density);
        setInitialColor(initialColor, true);

        typedArray.recycle();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        updateColorWheel();
        currentColorCircle = findNearestByColor(initialColor);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (alphaSliderViewId != 0) {
            setAlphaSlider((AlphaSlider) getRootView().findViewById(alphaSliderViewId));
        }
        if (lightnessSliderViewId != 0) {
            setLightnessSlider((LightnessSlider) getRootView().findViewById(lightnessSliderViewId));
        }

        updateColorWheel();
        currentColorCircle = findNearestByColor(initialColor);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateColorWheel();
    }

    private void updateColorWheel() {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        if (height < width) {
            width = height;
        }
        if (width <= 0) {
            return;
        }
        if (colorWheel == null) {
            colorWheel = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
            colorWheelCanvas = new Canvas(colorWheel);
            alphaPatternPaint.setShader(PaintBuilder.createAlphaPatternShader(8));
        }
        drawColorWheel();
        invalidate();
    }

    private void drawColorWheel() {
        colorWheelCanvas.drawColor(0, PorterDuff.Mode.CLEAR);

        if (renderer == null) {
            return;
        }

        float half = colorWheelCanvas.getWidth() / 2f;
        float strokeWidth = STROKE_RATIO * (1f + ColorWheelRenderer.GAP_PERCENTAGE);
        float maxRadius = half - strokeWidth - half / density;
        float cSize = maxRadius / (density - 1) / 2;

        ColorWheelRenderOption colorWheelRenderOption = renderer.getRenderOption();
        colorWheelRenderOption.density = this.density;
        colorWheelRenderOption.maxRadius = maxRadius;
        colorWheelRenderOption.cSize = cSize;
        colorWheelRenderOption.strokeWidth = strokeWidth;
        colorWheelRenderOption.alpha = alpha;
        colorWheelRenderOption.lightness = lightness;
        colorWheelRenderOption.targetCanvas = colorWheelCanvas;

        renderer.initWith(colorWheelRenderOption);
        renderer.draw();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = 0;
        switch (widthMode) {
            case MeasureSpec.UNSPECIFIED:
                width = widthMeasureSpec;
                break;
            case MeasureSpec.AT_MOST:
                width = MeasureSpec.getSize(widthMeasureSpec);
                break;
            case MeasureSpec.EXACTLY:
                width = MeasureSpec.getSize(widthMeasureSpec);
                break;
            default:
                break;
        }

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = 0;
        if (heightMode == MeasureSpec.UNSPECIFIED) {
            height = widthMeasureSpec;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        } else if (widthMode == MeasureSpec.EXACTLY) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        }
        int squareDimen = width;
        if (height < width) {
            squareDimen = height;
        }
        setMeasuredDimension(squareDimen, squareDimen);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE: {
                int lastSelectedColor = getSelectedColor();
                currentColorCircle = findNearestByPosition(event.getX(), event.getY());
                int selectedColor = getSelectedColor();

                callOnColorChangedListeners(lastSelectedColor, selectedColor);

                initialColor = selectedColor;
                setColorToSliders(selectedColor);
                invalidate();
                break;
            }
            case MotionEvent.ACTION_UP: {
                int selectedColor = getSelectedColor();
                if (listeners != null) {
                    for (OnColorSelectedListener listener : listeners) {
                        try {
                            listener.onColorSelected(selectedColor);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                setColorToSliders(selectedColor);
                setColorText(selectedColor);
                setColorPreviewColor(selectedColor);
                invalidate();
                break;
            }
            default:
                break;
        }
        return true;
    }

    protected void callOnColorChangedListeners(int oldColor, int newColor) {
        if (colorChangedListeners != null && oldColor != newColor) {
            for (OnColorChangedListener listener : colorChangedListeners) {
                try {
                    listener.onColorChanged(newColor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(backgroundColor);
        if (colorWheel != null) {
            canvas.drawBitmap(colorWheel, 0, 0, null);
        }
        if (currentColorCircle != null) {
            float maxRadius = canvas.getWidth() / 2f - STROKE_RATIO * (1f + ColorWheelRenderer.GAP_PERCENTAGE);
            float size = maxRadius / density / 2;
            colorWheelFill.setColor(Color.HSVToColor(currentColorCircle.getHsvWithLightness(this.lightness)));
            colorWheelFill.setAlpha((int) (alpha * 0xff));
            canvas.drawCircle(currentColorCircle.getX(), currentColorCircle.getY(), size * STROKE_RATIO, selectorStroke1);
            canvas.drawCircle(currentColorCircle.getX(), currentColorCircle.getY(), size * (1 + (STROKE_RATIO - 1) / 2), selectorStroke2);

            canvas.drawCircle(currentColorCircle.getX(), currentColorCircle.getY(), size, alphaPatternPaint);
            canvas.drawCircle(currentColorCircle.getX(), currentColorCircle.getY(), size, colorWheelFill);
        }
    }

    private ColorCircle findNearestByPosition(float x, float y) {
        ColorCircle near = null;
        double minDist = Double.MAX_VALUE;

        for (ColorCircle colorCircle : renderer.getColorCircleList()) {
            double dist = colorCircle.sqDist(x, y);
            if (minDist > dist) {
                minDist = dist;
                near = colorCircle;
            }
        }

        return near;
    }

    private ColorCircle findNearestByColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        ColorCircle near = null;
        double minDiff = Double.MAX_VALUE;
        double x = hsv[1] * Math.cos(hsv[0] * Math.PI / 180);
        double y = hsv[1] * Math.sin(hsv[0] * Math.PI / 180);

        for (ColorCircle colorCircle : renderer.getColorCircleList()) {
            float[] hsv1 = colorCircle.getHsv();
            double x1 = hsv1[1] * Math.cos(hsv1[0] * Math.PI / 180);
            double y1 = hsv1[1] * Math.sin(hsv1[0] * Math.PI / 180);
            double dx = x - x1;
            double dy = y - y1;
            double dist = dx * dx + dy * dy;
            if (dist < minDiff) {
                minDiff = dist;
                near = colorCircle;
            }
        }

        return near;
    }

    public int getSelectedColor() {
        int color = 0;
        if (currentColorCircle != null) {
            color = ColorsUtil.colorAtLightness(currentColorCircle.getColor(), this.lightness);
        }
        return ColorsUtil.adjustAlpha(this.alpha, color);
    }

    public void setSelectedColor(int previewNumber) {
        if (initialColors == null || initialColors.length < previewNumber) {
            return;
        }
        this.colorSelection = previewNumber;
        setHighlightedColor(previewNumber);
        Integer color = initialColors[previewNumber];
        if (color == null) {
            return;
        }
        setColor(color, true);
    }

    public Integer[] getAllColors() {
        return initialColors;
    }

    public void setInitialColors(Integer[] colors, int selectedColor) {
        this.initialColors = colors;
        this.colorSelection = selectedColor;
        Integer initialColor = this.initialColors[this.colorSelection];
        if (initialColor == null) {
            initialColor = 0xffffffff;
        }
        setInitialColor(initialColor, true);
    }

    public void setInitialColor(int color, boolean updateText) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);

        this.alpha = ColorsUtil.getAlphaPercent(color);
        this.lightness = hsv[2];
        this.initialColors[this.colorSelection] = color;
        this.initialColor = color;
        setColorPreviewColor(color);
        setColorToSliders(color);
        if (this.colorEdit != null && updateText) {
            setColorText(color);
        }
        currentColorCircle = findNearestByColor(color);
    }

    public void setLightness(float lightness) {
        int lastSelectedColor = getSelectedColor();

        this.lightness = lightness;
        this.initialColor = Color.HSVToColor(ColorsUtil.alphaValueAsInt(this.alpha), currentColorCircle.getHsvWithLightness(lightness));
        if (this.colorEdit != null) {
            this.colorEdit.setText(ColorsUtil.getHexString(this.initialColor, this.alphaSlider != null));
        }
        if (this.alphaSlider != null && this.initialColor != null) {
            this.alphaSlider.setColor(this.initialColor);
        }

        callOnColorChangedListeners(lastSelectedColor, this.initialColor);

        updateColorWheel();
        invalidate();
    }

    public void setColor(int color, boolean updateText) {
        setInitialColor(color, updateText);
        updateColorWheel();
        invalidate();
    }

    public void setAlphaValue(float alpha) {
        int lastSelectedColor = getSelectedColor();

        this.alpha = alpha;
        this.initialColor = Color.HSVToColor(ColorsUtil.alphaValueAsInt(this.alpha), currentColorCircle.getHsvWithLightness(this.lightness));
        if (this.colorEdit != null) {
            this.colorEdit.setText(ColorsUtil.getHexString(this.initialColor, this.alphaSlider != null));
        }
        if (this.lightnessSlider != null && this.initialColor != null) {
            this.lightnessSlider.setColor(this.initialColor);
        }

        callOnColorChangedListeners(lastSelectedColor, this.initialColor);

        updateColorWheel();
        invalidate();
    }

    public void addOnColorChangedListener(OnColorChangedListener listener) {
        this.colorChangedListeners.add(listener);
    }

    public void addOnColorSelectedListener(OnColorSelectedListener listener) {
        this.listeners.add(listener);
    }

    public void setLightnessSlider(LightnessSlider lightnessSlider) {
        this.lightnessSlider = lightnessSlider;
        if (lightnessSlider != null) {
            this.lightnessSlider.setColorPicker(this);
            this.lightnessSlider.setColor(getSelectedColor());
        }
    }

    public void setAlphaSlider(AlphaSlider alphaSlider) {
        this.alphaSlider = alphaSlider;
        if (alphaSlider != null) {
            this.alphaSlider.setColorPicker(this);
            this.alphaSlider.setColor(getSelectedColor());
        }
    }

    public void setColorEdit(EditText colorEdit) {
        this.colorEdit = colorEdit;
        if (this.colorEdit != null) {
            this.colorEdit.setVisibility(View.VISIBLE);
            this.colorEdit.addTextChangedListener(colorTextChange);
            setColorEditTextColor(pickerTextColor);
        }
    }

    public void setColorEditTextColor(int argb) {
        this.pickerTextColor = argb;
        if (colorEdit != null) {
            colorEdit.setTextColor(argb);
        }
    }

    public void setDensity(int density) {
        this.density = Math.max(2, density);
        invalidate();
    }

    public void setRenderer(ColorWheelRenderer renderer) {
        this.renderer = renderer;
        invalidate();
    }

    public void setColorPreview(LinearLayout colorPreview, Integer selectedColor) {
        if (colorPreview == null) {
            return;
        }
        this.colorPreview = colorPreview;
        if (selectedColor == null) {
            selectedColor = 0;
        }
        int children = colorPreview.getChildCount();
        if (children == 0 || colorPreview.getVisibility() != View.VISIBLE) {
            return;
        }

        for (int i = 0; i < children; i++) {
            View childView = colorPreview.getChildAt(i);
            if (!(childView instanceof LinearLayout)) {
                continue;
            }
            LinearLayout childLayout = (LinearLayout) childView;
            if (i == selectedColor) {
                childLayout.setBackgroundColor(Color.WHITE);
            }
            ImageView childImage = childLayout.findViewById(R.id.image_preview);
            childImage.setClickable(true);
            childImage.setTag(i);
            childImage.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v == null) {
                        return;
                    }
                    Object tag = v.getTag();
                    if (tag == null || !(tag instanceof Integer)) {
                        return;
                    }
                    setSelectedColor((int) tag);
                }
            });
        }
    }

    private void setHighlightedColor(int previewNumber) {
        int children = colorPreview.getChildCount();
        if (children == 0 || colorPreview.getVisibility() != View.VISIBLE) {
            return;
        }

        for (int i = 0; i < children; i++) {
            View childView = colorPreview.getChildAt(i);
            if (!(childView instanceof LinearLayout)) {
                continue;
            }
            LinearLayout childLayout = (LinearLayout) childView;
            if (i == previewNumber) {
                childLayout.setBackgroundColor(Color.WHITE);
            } else {
                childLayout.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    private void setColorPreviewColor(int newColor) {
        if (colorPreview == null || initialColors == null || colorSelection > initialColors.length || initialColors[colorSelection] == null) {
            return;
        }

        int children = colorPreview.getChildCount();
        if (children == 0 || colorPreview.getVisibility() != View.VISIBLE) {
            return;
        }

        View childView = colorPreview.getChildAt(colorSelection);
        if (!(childView instanceof LinearLayout)) {
            return;
        }
        LinearLayout childLayout = (LinearLayout) childView;
        ImageView childImage = childLayout.findViewById(R.id.image_preview);
        childImage.setImageDrawable(new CircleColorDrawable(newColor));
    }

    private void setColorText(int argb) {
        if (colorEdit == null) {
            return;
        }
        colorEdit.setText(ColorsUtil.getHexString(argb, this.alphaSlider != null));
    }

    private void setColorToSliders(int selectedColor) {
        if (lightnessSlider != null) {
            lightnessSlider.setColor(selectedColor);
        }
        if (alphaSlider != null) {
            alphaSlider.setColor(selectedColor);
        }
    }

    public enum WHEEL_TYPE {
        //花心
        FLOWER,
        //圆形
        CIRCLE;

        public static WHEEL_TYPE indexOf(int index) {
            switch (index) {
                case 0:
                    return FLOWER;
                case 1:
                    return CIRCLE;
                default:
                    return FLOWER;
            }
        }
    }
}