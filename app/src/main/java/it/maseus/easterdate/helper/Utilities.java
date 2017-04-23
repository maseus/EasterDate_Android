package it.maseus.easterdate.helper;

import it.maseus.easterdate.R;
import it.maseus.easterdate.application.MainApplication;
import it.maseus.easterdate.enums.GaussRange;
import it.maseus.easterdate.enums.UserAlert;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Classe per la gestione di metodi utili.
 */
@SuppressLint("StaticFieldLeak")
public class Utilities
{
    //Activity di riferimento.
    private static Activity activity;

    /**
     * Metodo costruttore.
     * @param activity Activity di riferimento.
     */
    public Utilities(Activity activity)
    {
        //Indica l'Activity di riferimento.
        Utilities.activity = activity;
    }

    /**
     * Metodo per la modifica delle orientazioni consentite per l'applicazione.
     */
    public void setOrientation()
    {
        //Applica gli orientamenti previsti dal dispositivo.
        activity.setRequestedOrientation(MainApplication.isTablet ? ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * Metodo per la verifica dello stato della connessione di Rete.
     * @return Flag indicativo della presenza di connessione di Rete.
     */
    public boolean isOnline()
    {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }

    /**
     * Metodo per la visualizzazione di una notifica Toast.
     * @param alert Identificatore del messaggio desiderato.
     * @param duration Identificatore della durata desiderata.
     */
    public void showToast(UserAlert alert, final int duration)
    {
        //Identifica il messaggio da visualizzare.
        final String message = activity.getString(UserAlert.msgForAlert(alert.getAlertId()));

        //Gestisce la visualizzazione sul Thread principale.
        activity.runOnUiThread(new Runnable()
        {
            /**
             * {@inheritDoc}
             */
            @Override
            public void run()
            {
                Toast.makeText(activity, biggerText(message), duration).show();
            }
        });
    }

    /**
     * Metodo per l'incremento delle dimensioni di un testo.
     * @param text Testo da modificare.
     * @return Testo risultato della modifica.
     */
    private SpannableStringBuilder biggerText(String text)
    {
        float newDimension = (MainApplication.isTablet ? 1.3f : 1.0f);

        //Applica il ridimensionamento richiesto.
        SpannableStringBuilder message = new SpannableStringBuilder(text);
        message.setSpan(new RelativeSizeSpan(newDimension), 0, text.length(), 0);

        //Ritorna il testo con le nuove dimensioni.
        return message;
    }

    /**
     * Metodo per interpretazione di una stringa come numero intero.
     * @param input Stringa da interpretare.
     * @return Risultato dell'interpretazione richiesta.
     */
    public static int parseInt(String input)
    {
        int intNumber;

        try
        {
            intNumber = Integer.parseInt(input);
        }
        catch(NumberFormatException e)
        {
            return 0;
        }

        return intNumber;
    }

    /**
     * Metodo per la chiusura della tastiera sul campo indicato.
     * @param view Campo di testo di riferimento.
     */
    public void closeKeyboard(View view)
    {
        //Inizializza l'oggetto responsabile.
        InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        //Realizza l'operazione richiesta.
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Metodo finalizzato alla visualizzazione di un sito Web.
     * @param url URL del sito Web.
     */
    public void openUrl(String url)
    {
        //Verifica il corretto formato dell'URL di riferimento.
        if (!url.startsWith("http://") && !url.startsWith("https://")) url = "http://".concat(url);

        try
        {
            //Prepara la visualizzazione nel browser.
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);

            //Lancia il servizio responsabile.
            intent.setData(Uri.parse(url));
            activity.startActivity(intent);
        }
        catch (ActivityNotFoundException e)
        {
            e.printStackTrace();

            //Visualizza una notifica di errore.
            Utilities utilities = new Utilities(activity);
            utilities.showToast(UserAlert.NO_SUPPORT_ALERT, Toast.LENGTH_LONG);
        }
    }

    /**
     * Metodo per apertura di un contatto Skype.
     * @param skypeName Nome Skype del contatto.
     */
    public void openSkype(String skypeName)
    {
        try
        {
            //Predispone la visualizzazione su Skype.
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);

            //Lancia il servizio responsabile.
            intent.setData(Uri.parse("skype:" + skypeName));
            activity.startActivity(intent);
        }
        catch (ActivityNotFoundException e)
        {
            e.printStackTrace();

            //Visualizza una notifica di errore.
            Utilities utilities = new Utilities(activity);
            utilities.showToast(UserAlert.NO_SUPPORT_ALERT, Toast.LENGTH_LONG);
        }
    }

    /**
     * Metodo per invio di un'e-mail all'indirizzo indicato.
     * @param mailAddress Indirizzo e-mail del destinatario.
     */
    public void sendMail(String mailAddress)
    {
        try
        {
            //Predispone l'invio dell'e-mail.
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);

            //Lancia il servizio responsabile.
            intent.setData(Uri.parse("mailto:" + mailAddress));
            activity.startActivity(intent);
        }
        catch (ActivityNotFoundException e)
        {
            e.printStackTrace();

            //Visualizza una notifica di errore.
            Utilities utilities = new Utilities(activity);
            utilities.showToast(UserAlert.NO_SUPPORT_ALERT, Toast.LENGTH_LONG);
        }
    }

    /**
     * Metodo di istanza per verifica di non nullita' di una stringa.
     * @param string Stringa di riferimento.
     * @return Esito della verifica richiesta.
     */
    public static boolean notNull(String string)
    {
        //Ritorna l'esito della verifica richiesta.
        return (string != null && !string.equalsIgnoreCase(""));
    }

    /**
     * Metodo per l'eliminazione di spazi bianchi sulla destra della stringa indicata.
     * @param input Stringa da elaborare.
     * @return Stringa risultato dell'elaborazione.
     */
    private static String trimRight(String input)
    {
        //Verifica stringa vuota.
        if (input.equalsIgnoreCase("")) return input;

        //Inizia la scansione dall'ultimo carattere della stringa.
        int i = input.length() - 1;

        //Scansiona la stringa fino al primo carattere.
        while (i >= 0 && Character.isWhitespace(input.charAt(i))) i--;

        //Ritorna il risultato dell'operazione.
        return input.substring(0, i + 1);
    }

    /**
     * Metodo per l'eliminazione di spazi bianchi sulla sinistra della stringa indicata.
     * @param input Stringa da elaborare.
     * @return Stringa risultato dell'elaborazione.
     */
    private static String trimLeft(String input)
    {
        //Verifica stringa vuota.
        if (input.equalsIgnoreCase("")) return input;

        //Inizia la scansione dal primo carattere della stringa.
        int i = 0;

        //Scansiona la stringa fino all'ultimo carattere.
        while (i < input.length() && Character.isWhitespace(input.charAt(i))) i++;

        //Ritorna il risultato dell'operazione.
        return input.substring(i);
    }

    /**
     * Metodo per l'eliminazione di spazi bianchi sulla destra e sulla sinistra della stringa indicata.
     * @param input Stringa da elaborare.
     * @return Stringa risultato dell'elaborazione.
     */
    public static String trimSides(String input)
    {
        //Verifica stringa vuota.
        if (input.equalsIgnoreCase("")) return input;

        //Applica l'operazione sulla destra.
        input = trimRight(input);

        //Applica l'operazione sulla sinistra.
        input = trimLeft(input);

        //Ritorna il risultato dell'operazione.
        return input;
    }

    /**
     * Metodo per verifica di appartenenza di un valore ad un intervallo.
     * @param start Valore iniziale dell'intervallo.
     * @param end Valore finale dell'intervallo.
     * @param value Valore da verificare.
     * @return Esito della verifica richiesta.
     */
    private boolean isBetween(int start, int end, int value)
    {
        //Ritorna l'esito della verifica richiesta.
        return (value >= start && value <= end);
    }

    /**
     * Metodo per il calcolo dei parametri per l'algoritmo di Gauss.
     * @param year Anno di riferimento.
     * @return Oggetto contenente i parametri richiesti.
     */
    private JSONObject gaussParams(int year)
    {
        //Inizializza l'oggetto da restituire.
        JSONObject gaussParams = new JSONObject();

        //Inizializza le variabili di riferimento.
        int mParam = -1;
        int nParam = -1;

        //Verifica l'intervallo di riferimento nella tabella di Gauss.
        if (isBetween(GaussRange.RANGE_01.getStartYear(), GaussRange.RANGE_01.getEndYear(), year))
        {
            mParam = GaussRange.RANGE_01.getMParam();
            nParam = GaussRange.RANGE_01.getNParam();
        }
        else if (isBetween(GaussRange.RANGE_02.getStartYear(), GaussRange.RANGE_02.getEndYear(), year))
        {
            mParam = GaussRange.RANGE_02.getMParam();
            nParam = GaussRange.RANGE_02.getNParam();
        }
        else if (isBetween(GaussRange.RANGE_03.getStartYear(), GaussRange.RANGE_03.getEndYear(), year))
        {
            mParam = GaussRange.RANGE_03.getMParam();
            nParam = GaussRange.RANGE_03.getNParam();
        }
        else if (isBetween(GaussRange.RANGE_04.getStartYear(), GaussRange.RANGE_04.getEndYear(), year))
        {
            mParam = GaussRange.RANGE_04.getMParam();
            nParam = GaussRange.RANGE_04.getNParam();
        }
        else if (isBetween(GaussRange.RANGE_05.getStartYear(), GaussRange.RANGE_05.getEndYear(), year))
        {
            mParam = GaussRange.RANGE_05.getMParam();
            nParam = GaussRange.RANGE_05.getNParam();
        }
        else if (isBetween(GaussRange.RANGE_06.getStartYear(), GaussRange.RANGE_06.getEndYear(), year))
        {
            mParam = GaussRange.RANGE_06.getMParam();
            nParam = GaussRange.RANGE_06.getNParam();
        }
        else if (isBetween(GaussRange.RANGE_07.getStartYear(), GaussRange.RANGE_07.getEndYear(), year))
        {
            mParam = GaussRange.RANGE_07.getMParam();
            nParam = GaussRange.RANGE_07.getNParam();
        }
        else if (isBetween(GaussRange.RANGE_08.getStartYear(), GaussRange.RANGE_08.getEndYear(), year))
        {
            mParam = GaussRange.RANGE_08.getMParam();
            nParam = GaussRange.RANGE_08.getNParam();
        }
        else if (isBetween(GaussRange.RANGE_09.getStartYear(), GaussRange.RANGE_09.getEndYear(), year))
        {
            mParam = GaussRange.RANGE_09.getMParam();
            nParam = GaussRange.RANGE_09.getNParam();
        }
        else if (isBetween(GaussRange.RANGE_10.getStartYear(), GaussRange.RANGE_10.getEndYear(), year))
        {
            mParam = GaussRange.RANGE_10.getMParam();
            nParam = GaussRange.RANGE_10.getNParam();
        }

        try
        {
            //Compone l'oggetto finale.
            gaussParams.put("mParam", mParam);
            gaussParams.put("nParam", nParam);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //Restituisce l'oggetto creato.
        return gaussParams;
    }

    /**
     * Metodo per applicazione dell'algoritmo di Gauss al calcolo della data della Pasqua cristiana.
     * @param year Anno di riferimento.
     * @return Data della Pasqua cristiana come calcolata.
     */
    public String gaussAlgorithm(int year)
    {
        //Inizializza la stringa finale.
        String easterDate = "";

        //Inizializza i parametri iniziali.
        int mParam = -1;
        int nParam = -1;

        //Inizializza i parametri di lavoro.
        int aParam;
        int bParam;
        int cParam;
        int dParam;
        int eParam;

        try
        {
            //Calcola i parametri per l'algoritmo di Gauss.
            JSONObject gaussParams = gaussParams(year);
            if (gaussParams.has("mParam")) mParam = gaussParams.getInt("mParam");
            if (gaussParams.has("nParam")) nParam = gaussParams.getInt("nParam");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        //Verifica parametri validi.
        if (mParam != -1 && nParam != -1)
        {
            //Calcola i parametri di lavoro.
            aParam = year % 4;
            bParam = year % 7;
            cParam = year % 19;
            dParam = (19 * cParam + mParam) % 30;
            eParam = (2 * aParam + 4 * bParam + 6 * dParam + nParam) % 7;

            //Calcola una prima data.
            if (dParam + eParam <= 9) easterDate = String.format(activity.getString(R.string.date_1), 22 + dParam + eParam);
            else easterDate = String.format(activity.getString(R.string.date_2), dParam + eParam - 9);

            //Verifica eventuali eccezioni.
            if (easterDate.equalsIgnoreCase(activity.getString(R.string.exception_1)) && dParam == 29 && eParam == 6) easterDate = String.format(activity.getString(R.string.date_2), 19);
            else if (easterDate.equalsIgnoreCase(activity.getString(R.string.exception_2)) && cParam > 10 && dParam == 28 && eParam == 6) easterDate = String.format(activity.getString(R.string.date_2), 18);
        }

        //Ritorna la data calcolata.
        return easterDate;
    }
}