public enum POS
{
    ADJECTIVE("Adjectives"), NOUN("Nouns"), VERB("Verbs");
    /**The text version of this enum*/
    private final String text;

    /**
     * Creates an enum with text
     * @param label the text component of the enum
     */
    POS(String label)
    {
        text = label;
    }

    /**
     * Returns a POS from its String version
     * @param testText the potential enum's String
     * @return the POS retrieved
     */
    public static POS getPOS(String testText)
    {
        for (POS p : POS.values())
        {
            if (p.text.equalsIgnoreCase(testText))
                return p;
        }
        throw new IllegalArgumentException();
    }

    /**
     * Returns the enum's text
     * @return the String representation of the enum
     */
    public String getText()
    {
        return text;
    }

}
