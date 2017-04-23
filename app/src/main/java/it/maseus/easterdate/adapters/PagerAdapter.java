package it.maseus.easterdate.adapters;

import it.maseus.easterdate.R;
import it.maseus.easterdate.enums.AppTab;
import it.maseus.easterdate.interfaces.Constants;
import it.maseus.easterdate.fragments.MainFragment;
import it.maseus.easterdate.fragments.InfoFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Classe per la gestione personalizzata del Paging tra i Fragment.
 */
public class PagerAdapter extends FragmentPagerAdapter
{
    //Icone da visualizzare.
    private int[] icons = new int[]{
            R.drawable.tab1_background,
            R.drawable.tab2_background
    };

    /**
     * Metodo costruttore.
     * @param fm Oggetto per la gestione dei Fragment.
     */
    //TODO
    public PagerAdapter(FragmentManager fm)
    {
        //Invocato il costruttore della superclasse.
        super(fm);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Fragment getItem(int index)
    {
        //Determina il Fragment da associare alla pagina indicata.
        AppTab appTab = AppTab.fromValue(index);
        switch (appTab)
        {
            case MAIN:
                return new MainFragment();

            case INFO:
                return new InfoFragment();
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCount()
    {
        //Ritorna il numero di pagine gestite.
        return Constants.TABS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CharSequence getPageTitle(int position)
    {
        return null;
    }

    /**
     * Metodo getter per l'icona alla posizione indicata.
     * @param position Indice dell'icona da visualizzare.
     * @return Identificatore della risorsa grafica di riferimento.
     */
    int getDrawableId(int position)
    {
        return icons[position];
    }
}