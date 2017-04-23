package it.maseus.easterdate.enums;

/**
 * Enumerazione delle Tab dell'applicazione principale.
 */
public enum AppTab
{
    MAIN(0),
    INFO(1);

    //Identificatore univoco della Tab.
    private int tabId;

    /**
     * Metodo costruttore.
     * @param tabId Identificatore univoco della Tab.
     */
    AppTab(int tabId)
    {
        //Invocato il corretto metodo setter.
        setTabId(tabId);
    }

    /**
     * Metodo setter per l'identificatore univoco della Tab.
     * @param tabId Identificatore univoco della Tab.
     */
    private void setTabId(int tabId)
    {
        this.tabId = tabId;
    }

    /**
     * Metodo getter per l'identificatore univoco della Tab.
     * @return Identificatore univoco della Tab.
     */
    public int getTabId()
    {
        return this.tabId;
    }

    /**
     * Metodo getter della Tab di cui si specifica l'identificatore.
     * @param tabId Identificatore univoco della Tab.
     * @return Tab corrispondente all'identificatore specificato.
     */
    public static AppTab fromValue(int tabId)
    {
        //Cerca il parametro di interesse.
        for (AppTab param : AppTab.values())
        {
            if (param.getTabId() == tabId) return param;
        }

        //Ricerca senza successo.
        throw new IllegalArgumentException("Tab not found!");
    }
}