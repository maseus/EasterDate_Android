package it.maseus.easterdate.application;

import it.maseus.easterdate.R;

import android.app.Application;

/**
 * Classe di riferimento per l'intera applicazione.
 */
@SuppressWarnings("deprecation")
public class MainApplication extends Application
{
    //Flag per tipo di dispositivo.
    public static boolean isTablet;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate()
    {
        //Invocato il metodo della superclasse.
        super.onCreate();

        //Verifica il tipo di dispositivo in uso.
        isTablet = getResources().getBoolean(R.bool.tablet);
    }
}