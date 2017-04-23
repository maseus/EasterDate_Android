package it.maseus.easterdate.activities;

import it.maseus.easterdate.R;
import it.maseus.easterdate.helper.Utilities;
import it.maseus.easterdate.interfaces.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

/**
 * Activity per la visualizzazione di una schermata di avvio.
 */
public class SplashActivity extends Activity
{
    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        //Invocato il metodo della superclasse.
        super.onCreate(savedInstanceState);

        //Richiamo layout XML di riferimento.
        setContentView(R.layout.splash);

        //Inizializza il punto di accesso ai metodi utili.
        Utilities utilities = new Utilities(this);

        //Applica le orientazioni consentite.
        utilities.setOrientation();

        //Lancia l'applicazione principale.
        startApplication();
    }

    /**
     * Metodo per il lancio dell'applicazione principale.
     */
    private void startApplication()
    {
        //Apertura differita dell'applicazione principale.
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable()
        {
            /**
             * {@inheritDoc}
             */
            @Override
            public void run()
            {
                //Chiude la schermata di avvio.
                closeSplash();
            }
        }, Constants.REFRESH_DELAY);
    }

    /**
     * Metodo per la chiusura della schermata di lancio.
     */
    private void closeSplash()
    {
        //Lancia la schermata principale.
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        //Termina la schermata di lancio.
        SplashActivity.this.finish();
    }
}