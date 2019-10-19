public enum Number
{
    SINGULAR("Singular"), PLURAL("Plural");
    /**The text version of this enum*/
    private final String text;

    /**
     * Creates a Number enum with text
     * @param num the text component of the enum
     */
    Number(String num)
    {
        text = num;
    }

    /**
     * Returns a Number from its String version
     * @param testText the potential enum's String
     * @return the Number retrieved
     */
    public static Number getNum(String testText)
    {
        for(Number n : Number.values())
        {
            if(n.text.equalsIgnoreCase(testText))
                return n;
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

    /**
     * Returns all the enums' texts
     * @return the String representations of the enums
     */
    public static String[] getTexts()
    {
        String[] texts = new String[Number.values().length];
        for(int i = 0; i<Number.values().length; i++)
        {
            texts[i] = Number.values()[i].getText();
        }
        return texts;
    }
}
