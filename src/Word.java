public abstract class Word
{
    /**The Word's base form*/
    private final String base;
    /**The Word's stem (root for each form)*/
    private final String stem;
    /**The Word's english translation*/
    private final String translation;

    /**
     * Constructs a word instance
     * @param base the base form of the word
     * @param stem the word's stem for its forms
     * @param translation the word's english translation
     */
    public Word(String base, String stem, String translation)
    {
        this.base = base;
        this.stem = stem;
        this.translation = translation;
    }

    /**
     * Returns the  String form of the word
     * @return the String representation of the word
     */
    public abstract String toString();

    /**
     * Checks if two words are equal
     * @param other the object to compare it to
     * @return whether they're equal
     */
    public abstract boolean equals(Object other);

    /**
     * Check if an array of forms is equal to the word's forms
     * @param tries the array of forms to compare
     * @return whether they're equal
     */
    public abstract boolean checkForms(String[][] tries);

    /**
     * Returns the correct forms of a word where there are errors
     * @param tries the forms to compare
     * @return an array with the corrected forms
     */
    public abstract String[][] wrongForms(String[][] tries);

    /**
     * Return the word's base form
     * @return the base form of the word
     */
    public String getBase()
    {
        return base;
    }

    /**
     * Return the word's stem
     * @return the word's stem, the root of its forms
     */
    public String getStem()
    {
        return stem;
    }

    /**
     * Return the word's english translation
     * @return the english translation
     */
    public String getTranslation()
    {
        return translation;
    }
}

