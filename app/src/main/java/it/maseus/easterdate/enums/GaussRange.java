package it.maseus.easterdate.enums;

/**
 * Enumerazione degli intervalli nella tabella dell'algoritmo di Gauss.
 */
@SuppressWarnings("unused")
public enum GaussRange
{
    RANGE_01(0, 34, 1582, 15, 6),
    RANGE_02(1, 1583, 1699, 22, 2),
    RANGE_03(2, 1700, 1799, 23, 3),
    RANGE_04(3, 1800, 1899, 23, 4),
    RANGE_05(4, 1900, 1999, 24, 5),
    RANGE_06(5, 2000, 2099, 24, 5),
    RANGE_07(6, 2100, 2199, 24, 6),
    RANGE_08(7, 2200, 2299, 25, 0),
    RANGE_09(8, 2300, 2399, 26, 1),
    RANGE_10(9, 2400, 2499, 25, 1);

    //Identificatore univoco dell'intervallo.
    private int rangeId;

    //Anno iniziale dell'intervallo.
    private int startYear;

    //Anno finale dell'intervallo.
    private int endYear;

    //Parametro m.
    private int mParam;

    //Parametro n.
    private int nParam;

    /**
     * Metodo costruttore.
     * @param rangeId Identificatore univoco dell'intervallo.
     * @param startYear Anno iniziale dell'intervallo.
     * @param endYear Anno finale dell'intervallo.
     * @param mParam Parametro m.
     * @param nParam Parametro n.
     */
    GaussRange(int rangeId, int startYear, int endYear, int mParam, int nParam)
    {
        //Inizializza i campi di riferimento.
        setRangeId(rangeId);
        setStartYear(startYear);
        setEndYear(endYear);
        setMParam(mParam);
        setNParam(nParam);
    }

    /**
     * Metodo setter per l'identificatore univoco dell'intervallo.
     * @param rangeId Identificatore univoco dell'intervallo.
     */
    private void setRangeId(int rangeId)
    {
        this.rangeId = rangeId;
    }

    /**
     * Metodo setter per l'anno iniziale dell'intervallo.
     * @param startYear Anno iniziale dell'intervallo.
     */
    private void setStartYear(int startYear)
    {
        this.startYear = startYear;
    }

    /**
     * Metodo setter per l'anno finale dell'intervallo.
     * @param endYear Anno finale dell'intervallo.
     */
    private void setEndYear(int endYear)
    {
        this.endYear = endYear;
    }

    /**
     * Metodo setter per il parametro m.
     * @param mParam Parametro m.
     */
    private void setMParam(int mParam)
    {
        this.mParam = mParam;
    }

    /**
     * Metodo setter per il parametro n.
     * @param nParam Parametro n.
     */
    private void setNParam(int nParam)
    {
        this.nParam = nParam;
    }

    /**
     * Metodo getter per l'identificatore univoco dell'intervallo.
     * @return Identificatore univoco dell'intervallo.
     */
    public int getRangeId()
    {
        return this.rangeId;
    }

    /**
     * Metodo getter per l'anno iniziale dell'intervallo.
     * @return Anno iniziale dell'intervallo.
     */
    public int getStartYear()
    {
        return startYear;
    }

    /**
     * Metodo getter per l'anno finale dell'intervallo.
     * @return Anno finale dell'intervallo.
     */
    public int getEndYear()
    {
        return endYear;
    }

    /**
     * Metodo getter per il parametro m.
     * @return Parametro m.
     */
    public int getMParam()
    {
        return mParam;
    }

    /**
     * Metodo getter per il parametro n.
     * @return Parametro n.
     */
    public int getNParam()
    {
        return nParam;
    }

    /**
     * Metodo getter dell'intervallo di cui si specifica l'identificatore.
     * @param rangeId Identificatore univoco dell'intervallo.
     * @return Intervallo corrispondente all'identificatore specificato.
     */
    public static GaussRange fromValue(int rangeId)
    {
        //Cerca il parametro di interesse.
        for (GaussRange param : GaussRange.values())
        {
            if (param.getRangeId() == rangeId) return param;
        }

        //Ricerca senza successo.
        throw new IllegalArgumentException("Range not found!");
    }
}