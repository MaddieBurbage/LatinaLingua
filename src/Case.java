public enum Case
{
    NOM("Nominative"), GEN("Genitive"), DAT("Dative"), ACC("Accusative"), ABL("Ablative"), VOC("Vocative");
    /**The text version of this enum*/
    private final String text;

    /**
     * Creates a Case enum with text
     * @param caseStr the text component of the enum
     */
    Case(String caseStr)
    {
        text = caseStr;
    }

    /**
     * Returns a Case from its String version
     * @param testText the potential enum's String
     * @return the Case retrieved
     */
    public static Case getCase(String testText)
    {
        for(Case c : Case.values())
        {
            if(c.text.equalsIgnoreCase(testText))
                return c;
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
     * Returns the enums' texts
     * @return the String representations of the enums
     */
    public static String[] getTexts()
    {
        String[] texts = new String[Case.values().length];
        for(int i = 0; i<Case.values().length; i++)
        {
            texts[i] = Case.values()[i].getText();
        }
        return texts;
    }
}
