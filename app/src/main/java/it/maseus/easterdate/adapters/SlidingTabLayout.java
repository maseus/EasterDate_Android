package it.maseus.easterdate.adapters;

import it.maseus.easterdate.application.MainApplication;
import it.maseus.easterdate.activities.MainActivity;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;

/**
 * Classe per la gestione di una TabBar personalizzata.
 */
@SuppressWarnings("deprecation, unused")
public class SlidingTabLayout extends HorizontalScrollView
{
    //Indice della Tab selezionata.
    private int selectedTab;

    //Parametri predefiniti per il dimensionamento delle Tab.
    private static final int TITLE_OFFSET_DIPS = 24;
    private static final int TAB_VIEW_PADDING_DIPS = 5;
    private static final int TAB_VIEW_TEXT_SIZE_SP = 12;

    //Offset del titolo visualizzato.
    private int mTitleOffset;

    //Identificatore del layout della Tab.
    int mTabViewLayoutId;

    //Identificatore del testo della Tab.
    int mTabViewTextViewId;

    //Flag per distribuzione uniforme delle Tab.
    boolean mDistributeEvenly;

    //Pager di riferimento.
    private ViewPager mViewPager;

    //Testi da visualizzare.
    private SparseArray<String> mContentDescriptions = new SparseArray<>();

    //Gestore di interazione con il Pager principale.
    private OnPageChangeListener mViewPagerPageChangeListener;

    //Layout principale di riferimento.
    private final SlidingTabStrip mTabStrip;

    /**
     * Metodo costruttore.
     * @param context Contesto di esecuzione.
     */
    public SlidingTabLayout(Context context)
    {
        //Invocato il costruttore della superclasse.
        this(context, null);
    }

    /**
     * Metodo costruttore.
     * @param context Contesto di esecuzione.
     * @param attrs Attributi di riferimento.
     */
    public SlidingTabLayout(Context context, AttributeSet attrs)
    {
        //Invocato il costruttore della superclasse.
        this(context, attrs, 0);
    }

    /**
     * Metodo costruttore.
     * @param context Contesto di esecuzione.
     * @param attrs Attributi di riferimento.
     * @param defStyle Stile di riferimento.
     */
    public SlidingTabLayout(Context context, AttributeSet attrs, int defStyle)
    {
        //Invocato il costruttore della superclasse.
        super(context, attrs, defStyle);

        //Disabilita lo scorrimento orizzontale.
        setHorizontalScrollBarEnabled(false);

        //Le Tab occupano tutta la larghezza disponibile.
        setFillViewport(true);

        //Personalizzazione dell'aspetto generale.
        mTitleOffset = (int) (TITLE_OFFSET_DIPS * getResources().getDisplayMetrics().density);
        mTabStrip = new SlidingTabStrip(context);
        addView(mTabStrip, LayoutParams.MATCH_PARENT, MainApplication.isTablet ? 100 : LayoutParams.WRAP_CONTENT);
    }

    /**
     * Metodo per la personalizzazione del colore delle Tab.
     * @param tabColorizer Oggetto per la colorazione delle Tab.
     */
    public void setCustomTabColorizer(TabColorizer tabColorizer)
    {
        //Applica le colorazioni richieste.
        mTabStrip.setCustomTabColorizer(tabColorizer);
    }

    /**
     * Metodo per la gestione del tipo di distribuzione delle Tab.
     * @param distributeEvenly Flag per distribuzione uniforme.
     */
    public void setDistributeEvenly(boolean distributeEvenly)
    {
        //Valorizza il flag di riferimento.
        mDistributeEvenly = distributeEvenly;
    }

    /**
     * Metodo per la colorazione degli indicatori delle Tab selezionate.
     * @param colors Identificatori dei colori da utilizzare.
     */
    public void setSelectedIndicatorColors(int... colors)
    {
        //Applica le colorazioni richieste.
        mTabStrip.setSelectedIndicatorColors(colors);
    }

    /**
     * Metodo per l'associazione di un listener di interazione con il Pager principale.
     * @param listener Listener di interazione da associare.
     */
    public void setOnPageChangeListener(OnPageChangeListener listener)
    {
        //Valorizza il listener di riferimento.
        mViewPagerPageChangeListener = listener;
    }

    /**
     * Metodo per l'inizializzazione delle risorse del layout personalizzato.
     * @param layoutResId Identificatore del layout della Tab.
     * @param textViewId Identificatore del testo della Tab.
     */
    public void setCustomTabView(int layoutResId, int textViewId)
    {
        //Valorizza gli identificatori di riferimento.
        mTabViewLayoutId = layoutResId;
        mTabViewTextViewId = textViewId;
    }

    /**
     * Metodo per l'inizializzazione del Pager principale.
     * @param viewPager Pager principale.
     */
    public void setViewPager(ViewPager viewPager)
    {
        //Azzera il layout principale.
        mTabStrip.removeAllViews();

        //Valorizza il Pager principale.
        mViewPager = viewPager;
        if (viewPager != null)
        {
            //Associa un listener di interazione con il Pager.
            viewPager.setOnPageChangeListener(new InternalViewPagerListener());

            //Popola le Tab di riferimento.
            populateTabStrip();
        }
    }

    /**
     * Metodo per la creazione di un layout testuale predefinito per le Tab.
     * @param context Contesto di esecuzione.
     * @return Controllo testuale predefinito.
     */
    protected TextView createDefaultTabView(Context context)
    {
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, TAB_VIEW_TEXT_SIZE_SP);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        textView.setBackgroundResource(outValue.resourceId);
        textView.setAllCaps(true);

        int padding = (int) (TAB_VIEW_PADDING_DIPS * getResources().getDisplayMetrics().density);
        textView.setPadding(padding, padding, padding, padding);

        return textView;
    }

    /**
     * Metodo per la creazione di un layout con immagine predefinito per le Tab.
     * @param context Contesto di esecuzione.
     * @return Immagine predefinita.
     */
    protected ImageView createDefaultImageView(Context context)
    {
        ImageView imageView = new ImageView(context);

        int padding = (int) (TAB_VIEW_PADDING_DIPS * getResources().getDisplayMetrics().density);
        imageView.setPadding(padding, padding, padding, padding);

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f / mViewPager.getAdapter().getCount());
        imageView.setLayoutParams(param);

        return imageView;
    }

    /**
     * Metodo per il popolamento delle Tab.
     */
    private void populateTabStrip()
    {
        final PagerAdapter adapter = (PagerAdapter) mViewPager.getAdapter();
        final OnClickListener tabClickListener = new TabClickListener();

        for (int i = 0; i < adapter.getCount(); i++)
        {
            View tabView;
            ImageView tabIconView = null;

            tabView = createDefaultImageView(getContext());
            if (ImageView.class.isInstance(tabView)) tabIconView = (ImageView) tabView;

            if (tabIconView != null) tabIconView.setImageDrawable(getResources().getDrawable(adapter.getDrawableId(i)));
            if (mViewPager.getCurrentItem() == i && tabIconView != null) tabIconView.setSelected(true);
            tabView.setOnClickListener(tabClickListener);

            mTabStrip.addView(tabView);
        }
    }

    public void setContentDescription(int i, String desc)
    {
        mContentDescriptions.put(i, desc);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        if (mViewPager != null) scrollToTab(mViewPager.getCurrentItem(), 0);
    }

    /**
     * Metodo per la gestione dello scorrimento su specifica Tab.
     * @param tabIndex Indice della Tab di riferimento.
     * @param positionOffset Offset posizionale.
     */
    private void scrollToTab(int tabIndex, int positionOffset)
    {
        final int tabStripChildCount = mTabStrip.getChildCount();
        if (tabStripChildCount == 0 || tabIndex < 0 || tabIndex >= tabStripChildCount) return;

        View selectedChild = mTabStrip.getChildAt(tabIndex);
        if (selectedChild != null)
        {
            int targetScrollX = selectedChild.getLeft() + positionOffset;
            if (tabIndex > 0 || positionOffset > 0) targetScrollX -= mTitleOffset;
            scrollTo(targetScrollX, 0);
        }
    }

    /**
     * Metodo getter per la Tab selezionata.
     * @return Indice della Tab selezionata.
     */
    public int getSelectedItem()
    {
        return selectedTab;
    }

    /**
     * Metodo per la selezione della Tab indicata.
     * @param item Indice della Tab da selezionare.
     */
    public void selectItem(int item)
    {
        mViewPager.setCurrentItem(item);
    }

    /**
     * Listener di interazione con il Pager principale.
     */
    private class InternalViewPagerListener implements OnPageChangeListener
    {
        //Stato di scroll.
        private int mScrollState;

        /**
         * {@inheritDoc}
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
        {
            int tabStripChildCount = mTabStrip.getChildCount();
            if ((tabStripChildCount == 0) || (position < 0) || (position >= tabStripChildCount)) return;

            mTabStrip.onViewPagerPageChanged(position, positionOffset);

            View selectedTitle = mTabStrip.getChildAt(position);
            int extraOffset = ((selectedTitle != null) ? (int) (positionOffset * selectedTitle.getWidth()) : 0);
            scrollToTab(position, extraOffset);

            if (mViewPagerPageChangeListener != null) mViewPagerPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onPageScrollStateChanged(int state)
        {
            mScrollState = state;
            if (mViewPagerPageChangeListener != null) mViewPagerPageChangeListener.onPageScrollStateChanged(state);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onPageSelected(int position)
        {
            for (int i = 0; i < mTabStrip.getChildCount(); i++) mTabStrip.getChildAt(i).setSelected(false);
            mTabStrip.getChildAt(position).setSelected(true);

            if (mScrollState == ViewPager.SCROLL_STATE_IDLE)
            {
                mTabStrip.onViewPagerPageChanged(position, 0f);
                scrollToTab(position, 0);
            }

            if (mViewPagerPageChangeListener != null) mViewPagerPageChangeListener.onPageSelected(position);
            selectedTab = mViewPager.getCurrentItem();
            MainActivity.tabStack.add(position);
        }
    }

    /**
     * Listener di interazione con le Tab di riferimento.
     */
    private class TabClickListener implements OnClickListener
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public void onClick(View v)
        {
            for (int i = 0; i < mTabStrip.getChildCount(); i++)
            {
                if (v == mTabStrip.getChildAt(i))
                {
                    mViewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    }

    /**
     * Interfaccia per la gestione della colorazione delle Tab.
     * {@link #setCustomTabColorizer(TabColorizer)}.
     */
    public interface TabColorizer
    {
        /**
         * Metodo getter del colore dell'indicatore principale.
         * @param position Indice della Tab di riferimento.
         * @return Colore dell'indicatore della Tab indicata.
         */
        int getIndicatorColor(int position);
    }
}