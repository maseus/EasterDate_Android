package it.maseus.easterdate.activities;

import it.maseus.easterdate.R;
import it.maseus.easterdate.enums.UserAlert;
import it.maseus.easterdate.helper.Utilities;
import it.maseus.easterdate.interfaces.Colors;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity per la visualizzazione della pagina Web dell'algoritmo di Gauss.
 */
@SuppressWarnings("deprecation")
@SuppressLint("SetJavaScriptEnabled")
public class AlgorithmActivity extends AppCompatActivity
{
    //Toolbar principale.
    Toolbar algorithmToolbar;
    ImageView backButton;

    //Testo visualizzato.
    private WebView algorithmContent;

    //Progresso della comunicazione con il server.
    ProgressDialog serverProgress;

    //Punto di accesso ai metodi utili.
    Utilities utilities;

    //Client di riferimento per la WebView principale.
    private WebViewClient webViewClient = new WebViewClient()
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            view.loadUrl(url);
            return false;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onPageFinished(WebView view, String url)
        {
            //Invocato il metodo della superclasse.
            super.onPageFinished(view, url);

            //Ferma l'indicatore di comunicazione con il server.
            if (serverProgress != null && serverProgress.isShowing()) serverProgress.dismiss();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
        {
            //Ferma l'indicatore di comunicazione con il server.
            if (serverProgress != null && serverProgress.isShowing()) serverProgress.dismiss();

            //Visualizza una notifica di errore.
            utilities.showToast(UserAlert.NO_NETWORK_ALERT, Toast.LENGTH_SHORT);
        }
    };

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        //Invocato il metodo della superclasse.
        super.onCreate(savedInstanceState);

        //Inflate del layout XML di riferimento.
        setContentView(R.layout.algorithm);

        //Inizializza il punto di accesso ai metodi utili.
        utilities = new Utilities(this);

        //Applica le orientazioni consentite.
        utilities.setOrientation();

        //Inizializza la Toolbar principale.
        algorithmToolbar = (Toolbar) findViewById(R.id.algorithmToolbar);
        setSupportActionBar(algorithmToolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);
        algorithmToolbar.setPadding(5, 0, 0, 0);
        algorithmToolbar.setContentInsetsAbsolute(0, 0);
        TextView toolbarTitle = (TextView) algorithmToolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(getString(R.string.algorithm_title));

        //Inizializza il pulsante per il ritorno alla schermata precedente.
        backButton = (ImageView) algorithmToolbar.findViewById(R.id.backButton);
        backButton.setOnClickListener(new OnClickListener()
        {
            /**
             * {@inheritDoc}
             */
            @Override
            public void onClick(View v)
            {
                //Termina l'Activity corrente.
                AlgorithmActivity.this.finish();
            }
        });

        //Inizializza il campo per il contenuto da visualizzare.
        algorithmContent = (WebView) findViewById(R.id.algorithmContent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onResume()
    {
        //Invocato il metodo della superclasse.
        super.onResume();

        //Aggiornamento del layout.
        initLayout();
    }

    /**
     * Metodo per l'inizializzazione del layout principale.
     */
    private void initLayout()
    {
        //Verifica la presenza di connessione di Rete.
        if (utilities.isOnline())
        {
            if (serverProgress == null || !serverProgress.isShowing())
            {
                serverProgress = new ProgressDialog(AlgorithmActivity.this);
                serverProgress.show();
                serverProgress.setContentView(R.layout.server_progress);
                serverProgress.setCancelable(false);
                serverProgress.setCanceledOnTouchOutside(false);
                if (serverProgress.getWindow() != null) serverProgress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                ((ProgressBar) serverProgress.findViewById(R.id.serverProgress))
                                             .getIndeterminateDrawable()
                                             .setColorFilter(Color.parseColor(Colors.MAIN_COLOR), PorterDuff.Mode.SRC_IN);
            }

            WebSettings webSettings = algorithmContent.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setSupportMultipleWindows(true);

            algorithmContent.setWebChromeClient(new WebChromeClient());
            algorithmContent.setWebViewClient(webViewClient);
            algorithmContent.loadUrl(getString(R.string.wikipedia_page));
        }
        //Notifica l'assenza di connessione di Rete.
        else utilities.showToast(UserAlert.NO_NETWORK_ALERT, Toast.LENGTH_LONG);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        //Gestisce la navigazione nel sito e nell'applicazione.
        if ((keyCode == KeyEvent.KEYCODE_BACK) && algorithmContent.canGoBack())
        {
            algorithmContent.goBack();
            return true;
        }
        //Altrimenti ritorna alla schermata precedente.
        else AlgorithmActivity.this.finish();

        return false;
    }
}