public enum Declension
{
    FIRST("1st"),
    SECOND("2nd"),
    THIRD("3rd"),
    FOURTH("4th"),
    FIFTH("5th"),
    THIRDI("3rdi");
    /**The text version of this enum*/
    private final String text;

    /**
     * Creates a Declension enum with text
     * @param dec the text component of the enum
     */
    Declension(String dec)
    {
        text = dec;
    }

    /**
     * Returns a Declension from its String version
     * @param testText the potential enum's String
     * @return the Declension retrieved
     */
    public static Declension getDec(String testText)
    {
        for (Declension d : Declension.values())
        {
            if (d.text.equalsIgnoreCase(testText))
                return d;
        }
        throw new IllegalArgumentException();
    }

    /**
     * Returns the enums' texts
     * @return the String representations of the enums
     */
    public static String[] textValues()
    {
        String[] texts = new String[Declension.values().length];
        for (int i = 0; i < Declension.values().length; i++)
        {
            texts[i] = Declension.values()[i].text;
        }
        return texts;
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
