package it.maseus.easterdate.enums;

import it.maseus.easterdate.R;

/**
 * Enumerazione possibili notifiche per l'utente.
 */
@SuppressWarnings("unused")
public enum UserAlert
{
    NO_NETWORK_ALERT(0, R.string.no_network_alert),
    WRONG_YEAR_ALERT(1, R.string.wrong_year_alert),
    NO_SUPPORT_ALERT(2, R.string.no_support_alert);

    //Identificatore univoco della notifica.
    private int alertId;

    //Messaggio visualizzato nella notifica.
    private int alertMsg;

    /**
     * Metodo costruttore.
     * @param alertId Identificatore univoco della notifica.
     * @param alertMsg Messaggio visualizzato nella notifica.
     */
    UserAlert(int alertId, int alertMsg)
    {
        //Richiamo ai metodi setter.
        setAlertId(alertId);
        setAlertMsg(alertMsg);
    }

    /**
     * Metodo setter per l'identificatore univoco della notifica.
     * @param alertId Identificatore univoco della notifica.
     */
    public void setAlertId(int alertId)
    {
        this.alertId = alertId;
    }

    /**
     * Metodo setter per il messaggio visualizzato nella notifica.
     * @param alertMsg Messaggio visualizzato nella notifica.
     */
    public void setAlertMsg(int alertMsg)
    {
        this.alertMsg = alertMsg;
    }

    /**
     * Metodo getter per l'identificatore univoco della notifica.
     * @return Identificatore univoco della notifica.
     */
    public int getAlertId()
    {
        return this.alertId;
    }

    /**
     * Metoto getter per il messaggio visualizzato nella notifica.
     * @return Messaggio visualizzato nella notifica.
     */
    public int getAlertMsg()
    {
        return this.alertMsg;
    }

    /**
     * Metodo getter del messaggio visualizzato nella notifica indicata.
     * @param alertId Identificatore univoco della notifica di interesse.
     * @return Messaggio visualizzato nella notifica di interesse.
     */
    public static int msgForAlert(int alertId)
    {
        for (UserAlert alert : UserAlert.values())
        {
            if (alert.getAlertId() == alertId) return alert.getAlertMsg();
        }

        throw new IllegalArgumentException("Alert not found!");
    }
}