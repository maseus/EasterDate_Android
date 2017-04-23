package it.maseus.easterdate.fragments;

import it.maseus.easterdate.R;
import it.maseus.easterdate.activities.AlgorithmActivity;
import it.maseus.easterdate.activities.MainActivity;
import it.maseus.easterdate.enums.AuthorBadge;
import it.maseus.easterdate.enums.UserAlert;
import it.maseus.easterdate.helper.Utilities;
import it.maseus.easterdate.interfaces.Constants;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Fragment con informazioni su applicazione ed autore.
 */
public class InfoFragment extends Fragment
{
    //Layout principale di riferimento.
    public View view;

    //Badge del profilo Facebook dell'autore.
    ImageButton facebookBadge;

    //Badge del profilo LinkedIn dell'autore.
    ImageButton linkedinBadge;

    //Badge del profilo GitHub dell'autore.
    ImageButton githubBadge;

    //Badge del contatto Skype dell'autore.
    ImageButton skypeBadge;

    //Badge del contatto e-mail dell'autore.
    ImageButton mailBadge;

    //Link alla pagina Wikipedia dell'algoritmo di Gauss.
    Button algorithmLink;

    //Punto di accesso ai metodi utili.
    Utilities utilities;

    //Listener di interazione con il link per la pagina dell'algoritmo di Gauss.
    private OnClickListener algorithmListener = new OnClickListener()
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public void onClick(View v)
        {
            //Apre la relativa schermata.
            showWebPage();
        }
    };

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //Inflate del layout XML di riferimento.
        view = inflater.inflate(R.layout.info_frag, container, false);

        //Gestione dello stack di navigazione.
        MainActivity.tabSwitch = true;

        //Inizializza il punto di accesso ai metodi utili.
        if (isAdded()) utilities = new Utilities(getActivity());

        //Inizializza i pulsanti di riferimento.
        initButtons();

        //Ritorna il layout per il Fragment.
        return view;
    }

    /**
     * Metodo per inizializzazione dei pulsanti di riferimento.
     */
    private void initButtons()
    {
        //Inizializza il pulsante per il profilo Facebook.
        facebookBadge = (ImageButton) view.findViewById(R.id.facebookBadge);
        facebookBadge.setOnClickListener(new BadgeListener(AuthorBadge.FACEBOOK));

        //Inizializza il pulsante per il profilo LinkedIn.
        linkedinBadge = (ImageButton) view.findViewById(R.id.linkedinBadge);
        linkedinBadge.setOnClickListener(new BadgeListener(AuthorBadge.LINKEDIN));

        //Inizializza il pulsante per il profilo GitHub.
        githubBadge = (ImageButton) view.findViewById(R.id.githubBadge);
        githubBadge.setOnClickListener(new BadgeListener(AuthorBadge.GITHUB));

        //Inizializza il pulsante per il profilo Skype.
        skypeBadge = (ImageButton) view.findViewById(R.id.skypeBadge);
        skypeBadge.setOnClickListener(new BadgeListener(AuthorBadge.SKYPE));

        //Inizializza il pulsante per il contatto e-mail.
        mailBadge = (ImageButton) view.findViewById(R.id.mailBadge);
        mailBadge.setOnClickListener(new BadgeListener(AuthorBadge.MAIL));

        //Inizializza il link alla pagina Web dell'algoritmo.
        algorithmLink = (Button) view.findViewById(R.id.algorithmLink);
        algorithmLink.setOnClickListener(algorithmListener);
    }

    /**
     * Metodo per l'apertura della pagina Web dell'algoritmo.
     */
    private void showWebPage()
    {
        //Verifica la presenza di connessione di Rete.
        if (utilities.isOnline())
        {
            //Apre la relativa schermata.
            Intent intent = new Intent(getActivity(), AlgorithmActivity.class);
            startActivity(intent);
        }
        //Notifica l'assenza di connessione di Rete.
        else utilities.showToast(UserAlert.NO_NETWORK_ALERT, Toast.LENGTH_LONG);
    }

    /**
     * Metodo gestore dell'interazione con un badge dell'autore.
     * @param authorBadge Identificatore del badge dell'autore.
     */
    private void handleBadge(AuthorBadge authorBadge)
    {
        //Verifica la presenza di connessione di Rete.
        if (utilities.isOnline())
        {
            //Verifica il profilo selezionato.
            switch (authorBadge)
            {
                //Profilo Facebook.
                case FACEBOOK:

                    utilities.openUrl(Constants.FACEBOOK_URL);
                    break;

                //Profilo LinkedIn.
                case LINKEDIN:

                    utilities.openUrl(Constants.LINKEDIN_URL);
                    break;

                //Profilo GitHub.
                case GITHUB:

                    utilities.openUrl(Constants.GITHUB_URL);
                    break;

                //Profilo Skype.
                case SKYPE:

                    utilities.openSkype(Constants.SKYPE_NAME);
                    break;

                //Contatto e-mail.
                case MAIL:

                    utilities.sendMail(Constants.MAIL_ADDRESS);
                    break;
            }
        }
        //Notifica l'assenza di connessione di Rete.
        else utilities.showToast(UserAlert.NO_NETWORK_ALERT, Toast.LENGTH_LONG);
    }

    /**
     * Listener di interazione con i badge dei profili dell'autore.
     */
    private class BadgeListener implements OnClickListener
    {
        //Identificatore del badge selezionato.
        private AuthorBadge badgeType;

        /**
         * Metodo costruttore.
         * @param badgeType Identificatore del badge selezionato.
         */
        BadgeListener(AuthorBadge badgeType)
        {
            //Inizializza l'identificatore del badge selezionato.
            this.badgeType = badgeType;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onClick(View v)
        {
            //Gestisce la selezione del profilo richiesto.
            handleBadge(this.badgeType);
        }
    }
}
