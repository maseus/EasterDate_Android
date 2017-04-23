package it.maseus.easterdate.enums;

/**
 * Enumerazione dei profili informativi sull'autore.
 */
public enum AuthorBadge
{
    FACEBOOK(0),
    LINKEDIN(1),
    GITHUB(2),
    SKYPE(3),
    MAIL(4);

    //Identificatore univoco del profilo.
    private int badgeId;

    /**
     * Metodo costruttore.
     * @param badgeId Identificatore univoco del profilo.
     */
    AuthorBadge(int badgeId)
    {
        //Invocato il corretto metodo setter.
        setBadgeId(badgeId);
    }

    /**
     * Metodo setter per l'identificatore univoco del profilo.
     * @param badgeId Identificatore univoco del profilo.
     */
    private void setBadgeId(int badgeId)
    {
        this.badgeId = badgeId;
    }

    /**
     * Metodo getter per l'identificatore univoco del profilo.
     * @return Identificatore univoco del profilo.
     */
    public int getBadgeId()
    {
        return this.badgeId;
    }

    /**
     * Metodo getter del profilo di cui si specifica l'identificatore.
     * @param badgeId Identificatore univoco del profilo.
     * @return Profilo corrispondente all'identificatore specificato.
     */
    public static AuthorBadge fromValue(int badgeId)
    {
        //Cerca il parametro di interesse.
        for (AuthorBadge param : AuthorBadge.values())
        {
            if (param.getBadgeId() == badgeId) return param;
        }

        //Ricerca senza successo.
        throw new IllegalArgumentException("Profile not found!");
    }
}