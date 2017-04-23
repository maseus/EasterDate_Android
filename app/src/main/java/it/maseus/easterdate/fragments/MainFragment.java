package it.maseus.easterdate.fragments;

import it.maseus.easterdate.R;
import it.maseus.easterdate.activities.MainActivity;
import it.maseus.easterdate.enums.UserAlert;
import it.maseus.easterdate.helper.Utilities;
import it.maseus.easterdate.interfaces.Constants;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

/**
 * Fragment principale per il calcolo richiesto.
 */
@SuppressLint("StaticFieldLeak")
public class MainFragment extends Fragment
{
    //Layout principale di riferimento.
    public View view;

    //Campo di testo per l'inserimento dell'anno.
    public static TextInputEditText yearField;

    //Pulsante per il calcolo richiesto.
    Button computeButton;

    //Layout contenente il risultato del calcolo richiesto.
    LinearLayout computeResult;

    //Etichetta per il risultato del calcolo richiesto.
    TextView easterDate;

    //Punto di accesso ai metodi utili.
    Utilities utilities;

    //Listener di interazione con il pulsante per il calcolo richiesto.
    private OnClickListener computeListener = new OnClickListener()
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public void onClick(View v)
        {
            //Verifica l'inserimento di un anno.
            if (Utilities.notNull(yearField.getText().toString()))
            {
                //Gestisce l'anno indicato.
                handleYear();
            }
            //Notifica il mancato inserimento di un anno.
            else
            {
                utilities.showToast(UserAlert.WRONG_YEAR_ALERT, Toast.LENGTH_LONG);
                computeResult.setVisibility(View.GONE);
            }
        }
    };

    //Listener di variazioni nel campo per l'inserimento dell'anno.
    private TextWatcher yearWatcher = new TextWatcher()
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
            //TODO
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void afterTextChanged(Editable s)
        {
            //TODO
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            //Nasconde l'ultimo risultato alla cancellazione dell'anno inserito.
            if (!Utilities.notNull(yearField.getText().toString())) computeResult.setVisibility(View.GONE);
        }
    };

    //Listener di interazione con la tastiera sul campo per l'inserimento dell'anno.
    private OnEditorActionListener keyboardListener = new OnEditorActionListener()
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
        {
            //Gestisce l'interazione con il pulsante di conferma.
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE))
            {
                //Verifica l'inserimento di un anno.
                if (Utilities.notNull(yearField.getText().toString()))
                {
                    //Gestisce l'anno indicato.
                    handleYear();
                }
                //Notifica il mancato inserimento di un anno.
                else
                {
                    utilities.showToast(UserAlert.WRONG_YEAR_ALERT, Toast.LENGTH_LONG);
                    computeResult.setVisibility(View.GONE);
                }

                //Operazione correttamente gestita.
                return true;
            }

            //Operazione non gestita.
            return false;
        }
    };

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //Inflate del layout XML di riferimento.
        view = inflater.inflate(R.layout.main_frag, container, false);

        //Gestione dello stack di navigazione.
        MainActivity.tabSwitch = true;

        //Inizializza il punto di accesso ai metodi utili.
        if (isAdded()) utilities = new Utilities(getActivity());

        //Inizializza i componenti del layout.
        initLayout();

        //Ritorna il layout per il Fragment.
        return view;
    }

    /**
     * Metodo per inizializzazione dei componenti del layout.
     */
    private void initLayout()
    {
        //Inizializza il campo di testo per l'inserimento dell'anno.
        yearField = (TextInputEditText) view.findViewById(R.id.yearField);
        yearField.setOnEditorActionListener(keyboardListener);
        yearField.addTextChangedListener(yearWatcher);

        //Inizializza il pulsante per il calcolo richiesto.
        computeButton = (Button) view.findViewById(R.id.computeButton);
        computeButton.setOnClickListener(computeListener);

        //Inizializza il layout contenente il risultato richiesto.
        computeResult = (LinearLayout) view.findViewById(R.id.computeResult);
        computeResult.setVisibility(View.GONE);
        easterDate = (TextView) view.findViewById(R.id.easterDate);
    }

    /**
     * Metodo per la gestione dell'anno indicato.
     */
    private void handleYear()
    {
        //Legge l'anno indicato dall'utente.
        String yearString = Utilities.trimSides(yearField.getText().toString());
        int year = (Utilities.notNull(yearString) ? Utilities.parseInt(yearString) : 0);

        //Verifica il rispetto dei limiti consentiti.
        if (year >= Constants.MIN_YEAR && year <= Constants.MAX_YEAR)
        {
            //Chiude la tastiera eventualmente aperta.
            utilities.closeKeyboard(yearField);

            //Applica l'algoritmo previsto.
            String easter = utilities.gaussAlgorithm(year);

            //Verifica una data non nulla.
            if (Utilities.notNull(easter))
            {
                //Visualizza la data calcolata.
                easterDate.setText(easter);
                computeResult.setVisibility(View.VISIBLE);
            }
            else computeResult.setVisibility(View.GONE);
        }
        //Notifica il mancato rispetto dei limiti consentiti.
        else
        {
            utilities.showToast(UserAlert.WRONG_YEAR_ALERT, Toast.LENGTH_LONG);
            computeResult.setVisibility(View.GONE);
        }
    }
}
