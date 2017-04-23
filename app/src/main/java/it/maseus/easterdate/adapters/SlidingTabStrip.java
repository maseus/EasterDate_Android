package it.maseus.easterdate.adapters;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Classe per la definizione di un layout per la TabBar personalizzata.
 */
@SuppressWarnings("deprecation")
class SlidingTabStrip extends LinearLayout
{
    //Parametri dimensionali predefiniti di riferimento.
    private static final int DEFAULT_BOTTOM_BORDER_THICKNESS_DIPS = 0;
    private static final byte DEFAULT_BOTTOM_BORDER_COLOR_ALPHA = 0x26;
    private static final int SELECTED_INDICATOR_THICKNESS_DIPS = 3;
    private static final int DEFAULT_SELECTED_INDICATOR_COLOR = 0xFF33B5E5;

    private final int mBottomBorderThickness;
    private final Paint mBottomBorderPaint;

    private final int mSelectedIndicatorThickness;
    private final Paint mSelectedIndicatorPaint;

    private int mSelectedPosition;
    private float mSelectionOffset;

    private SlidingTabLayout.TabColorizer mCustomTabColorizer;
    private final SimpleTabColorizer mDefaultTabColorizer;

    /**
     * Metodo costruttore.
     * @param context Contesto di esecuzione.
     */
    SlidingTabStrip(Context context)
    {
        //Invocato il costruttore della superclasse.
        this(context, null);
    }

    /**
     * Metodo costruttore.
     * @param context Contesto di esecuzione.
     * @param attrs Attributi di riferimento.
     */
    SlidingTabStrip(Context context, AttributeSet attrs)
    {
        //Invocato il costruttore della superclasse.
        super(context, attrs);

        setWillNotDraw(false);
        final float density = getResources().getDisplayMetrics().density;

        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.colorForeground, outValue, true);
        final int themeForegroundColor =  outValue.data;
        int defaultBottomBorderColor = setColorAlpha(themeForegroundColor, DEFAULT_BOTTOM_BORDER_COLOR_ALPHA);

        mDefaultTabColorizer = new SimpleTabColorizer();
        mDefaultTabColorizer.setIndicatorColors(DEFAULT_SELECTED_INDICATOR_COLOR);

        mBottomBorderThickness = (int) (DEFAULT_BOTTOM_BORDER_THICKNESS_DIPS * density);
        mBottomBorderPaint = new Paint();
        mBottomBorderPaint.setColor(defaultBottomBorderColor);

        mSelectedIndicatorThickness = (int) (SELECTED_INDICATOR_THICKNESS_DIPS * density);
        mSelectedIndicatorPaint = new Paint();
    }

    /**
     * Metodo per l'inizializzazione delle colorazioni da utilizzare.
     * @param customTabColorizer Oggetto con informazioni sulle colorazioni da utilizzare.
     */
    void setCustomTabColorizer(SlidingTabLayout.TabColorizer customTabColorizer)
    {
        mCustomTabColorizer = customTabColorizer;
        invalidate();
    }

    /**
     * Metodo per l'inizializzazione dei colori delle Tab selezionate.
     * @param colors Identificatori dei colori da utilizzare.
     */
    void setSelectedIndicatorColors(int... colors)
    {
        mCustomTabColorizer = null;
        mDefaultTabColorizer.setIndicatorColors(colors);
        invalidate();
    }

    /**
     * Metodo per la gestione di variazioni nella selezione delle pagine.
     * @param position Posizione di riferimento.
     * @param positionOffset Offset rispetto alla posizione di riferimento.
     */
    void onViewPagerPageChanged(int position, float positionOffset)
    {
        mSelectedPosition = position;
        mSelectionOffset = positionOffset;
        invalidate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDraw(Canvas canvas)
    {
        final int height = getHeight();
        final int childCount = getChildCount();
        final SlidingTabLayout.TabColorizer tabColorizer = mCustomTabColorizer != null ? mCustomTabColorizer : mDefaultTabColorizer;

        if (childCount > 0)
        {
            View selectedTitle = getChildAt(mSelectedPosition);
            int left = selectedTitle.getLeft();
            int right = selectedTitle.getRight();
            int color = tabColorizer.getIndicatorColor(mSelectedPosition);

            if (mSelectionOffset > 0f && mSelectedPosition < (getChildCount() - 1))
            {
                int nextColor = tabColorizer.getIndicatorColor(mSelectedPosition + 1);
                if (color != nextColor) color = blendColors(nextColor, color, mSelectionOffset);

                View nextTitle = getChildAt(mSelectedPosition + 1);
                left = (int) (mSelectionOffset * nextTitle.getLeft() + (1.0f - mSelectionOffset) * left);
                right = (int) (mSelectionOffset * nextTitle.getRight() + (1.0f - mSelectionOffset) * right);
            }

            mSelectedIndicatorPaint.setColor(color);
            canvas.drawRect(left, height - mSelectedIndicatorThickness, right, height, mSelectedIndicatorPaint);
        }

        canvas.drawRect(0, height - mBottomBorderThickness, getWidth(), height, mBottomBorderPaint);
    }

    /**
     * Metodo per l'applicazione di una trasparenza al colore indicato.
     * @param color Identificatore del colore di riferimento.
     * @param alpha Valore della trasparenza da applicare.
     * @return Identificatore del colore trasformato come richiesto.
     */
    private static int setColorAlpha(int color, byte alpha)
    {
        //Applica e ritorna la trasformazione richiesta.
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
    }

    /**
     * Metodo per la miscelazione dei colori indicati.
     * @param color1 Primo colore di riferimento.
     * @param color2 Secondo colore di riferimento.
     * @param ratio Rapporto di miscelazione.
     * @return Identificatore del colore ottenuto dalla miscelazione.
     */
    private static int blendColors(int color1, int color2, float ratio)
    {
        final float inverseRation = 1.0f - ratio;
        float r = (Color.red(color1) * ratio) + (Color.red(color2) * inverseRation);
        float g = (Color.green(color1) * ratio) + (Color.green(color2) * inverseRation);
        float b = (Color.blue(color1) * ratio) + (Color.blue(color2) * inverseRation);
        return Color.rgb((int) r, (int) g, (int) b);
    }

    /**
     * Classe per la gestione della colorazione delle Tab.
     */
    private static class SimpleTabColorizer implements SlidingTabLayout.TabColorizer
    {
        //Identificatori dei colori da utilizzare.
        private int[] mIndicatorColors;

        /**
         * {@inheritDoc}
         */
        @Override
        public final int getIndicatorColor(int position)
        {
            return mIndicatorColors[position % mIndicatorColors.length];
        }

        /**
         * Metodo setter per i colori da utilizzare.
         * @param colors Identificatori dei colori da utilizzare.
         */
        void setIndicatorColors(int... colors)
        {
            mIndicatorColors = colors;
        }
    }
}