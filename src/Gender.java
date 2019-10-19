public enum Gender
{
    MASC("Masculine"), FEM("Feminine"), NEUT("Neuter"), ANY("Any"), HUM("Human");
    /**The text version of this enum*/
    private final String text;

    /**
     * Creates a Gender enum with text
     * @param gen the text component of the enum
     */
    Gender(String gen)
    {
        text = gen;
    }

    /**
     * Returns a Gender from its String version
     * @param testText the potential enum's String
     * @return the Gender retrieved
     */
    public static Gender getGen(String testText)
    {
        for(Gender g : Gender.values())
        {
            if(g.text.equalsIgnoreCase(testText))
                return g;
        }
        throw new IllegalArgumentException();
    }

    /**
     * Checks whether a String is equal to the enum's String
     * @param test the String to test
     * @return whether they're equal
     */
    public boolean equals(String test)
    {
        return test.equalsIgnoreCase(text);
    }

    /**
     * Returns the enums' texts
     * @return the String representations of the enums
     */
    public static String[] textValues()
    {
        String[] texts = new String[Gender.values().length];
        for (int i = 0; i < Gender.values().length; i++)
        {
            texts[i] = Gender.values()[i].text;
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
