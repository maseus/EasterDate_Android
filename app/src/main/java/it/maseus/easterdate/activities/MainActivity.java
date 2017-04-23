package it.maseus.easterdate.activities;

import it.maseus.easterdate.R;
import it.maseus.easterdate.fragments.MainFragment;
import it.maseus.easterdate.helper.Utilities;
import it.maseus.easterdate.adapters.PagerAdapter;
import it.maseus.easterdate.adapters.SlidingTabLayout;
import it.maseus.easterdate.interfaces.Colors;
import it.maseus.easterdate.interfaces.Constants;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.Window;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Activity per la gestione delle schermate principali.
 */
@SuppressLint("CommitPrefEdits, StaticFieldLeak")
public class MainActivity extends AppCompatActivity
{
    //Stack gestore della navigazione tra schermate.
    public static ArrayList<Integer> tabStack = new ArrayList<>();

    //Flag utile a gestire la navigazione tra Tab.
    public static boolean tabSwitch;

    //Elementi del layout principale.
    ViewPager pager;
    public static PagerAdapter adapter;
    public static SlidingTabLayout tabs;

    //Punto di accesso alle informazioni condivise.
    SharedPreferences prefs;
    Editor editor;

    //Punto di accesso ai metodi utili.
    Utilities utilities;

    //Riferimento all'Activity corrente.
    public static Activity mainActivity;

    //Listener di interazione con le Tab dell'applicazione principale.
    private OnPageChangeListener tabListener = new OnPageChangeListener()
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
        {
            //Chiude la tastiera eventualmente aperta.
            utilities.closeKeyboard(MainFragment.yearField);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onPageScrollStateChanged(int state)
        {
            //TODO
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onPageSelected(int position)
        {
            //Chiude la tastiera eventualmente aperta.
            utilities.closeKeyboard(MainFragment.yearField);
        }
    };

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //Invocato il metodo della superclasse.
        super.onCreate(savedInstanceState);

        //Aggancio al layout di riferimento.
        setContentView(R.layout.main);

        //Registra la prima tab in testa allo stack.
        tabStack.add(0);

        //Inizializza il punto di accesso ai metodi utili.
        utilities = new Utilities(this);

        //Applica le orientazioni consentite.
        utilities.setOrientation();

        //Inizializza il punto di accesso alle informazioni condivise.
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();

        //Riferimento alla schermata corrente.
        mainActivity = this;

        //Inizializza il layout personalizzato.
        adapter =  new PagerAdapter(getSupportFragmentManager());
        pager = (ViewPager) findViewById(R.id.mainPager);
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(Constants.TABS);
        tabs = (SlidingTabLayout) findViewById(R.id.tabbar);
        tabs.setDistributeEvenly(true);

        //Configura l'aspetto delle Tab.
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer()
        {
            /**
             * {@inheritDoc}
             */
            @Override
            public int getIndicatorColor(int position)
            {
                return Color.parseColor(Colors.MAIN_COLOR);
            }
        });

        //Associa il gestore delle azioni di Paging.
        tabs.setViewPager(pager);
        tabs.setOnPageChangeListener(tabListener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu)
    {
        //Considera eventi associati ad Action Bar ed Options Menu.
        if ((featureId == Window.FEATURE_ACTION_BAR || featureId == Window.FEATURE_OPTIONS_PANEL) && menu != null)
        {
            if (menu.getClass().getSimpleName().equals("MenuBuilder"))
            {
                try
                {
                    //Visualizza le icone associate alle voci nel menu delle opzioni.
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                }
                catch(NoSuchMethodException e)
                {
                    e.printStackTrace();
                }
                catch(Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        }

        //Invocato il metodo della superclasse.
        return super.onMenuOpened(featureId, menu);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBackPressed()
    {
        //Gestisce la navigazione a ritroso tra le tab.
        if (tabs.getSelectedItem() == 0) MainActivity.this.finish();
        else if (tabSwitch)
        {
            //Aggiornamento dinamico dello stack di navigazione.
            int stackCount = tabStack.size();
            if (stackCount > 0)
            {
                int lastTab = tabStack.get(stackCount - 1);
                tabs.selectItem(lastTab);
                lastTab = tabStack.get(stackCount - 2);
                tabs.selectItem(lastTab);
                tabStack.remove(stackCount - 1);
                tabStack.remove(stackCount - 2);
            }
            else tabs.selectItem(0);
        }
    }
}