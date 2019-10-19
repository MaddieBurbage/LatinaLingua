import java.util.Arrays;

public class Noun extends Word
{
    /**gender is the Gender of this noun*/
    private final Gender gender;
    /**group is the Declension of this noun*/
    private final Declension group;
    /**forms contains the forms for each case and number*/
    private String[][] forms;

    /**
     * Constructer to make a new Noun instance
     * @param base is the base, nominative singular form of the noun
     * @param stem is the root part of the noun that endings are added to for each form
     * @param translation is the english definition of the noun
     * @param gender is the noun's gender
     * @param group is the noun's declension
     */
    public Noun(String base, String stem, String translation, Gender gender, Declension group)
    {
        super(base, stem, translation);
        this.gender = gender;
        this.group = group;
        makeForms();
    }

    /**
     * Returns a string form of the noun
     * @return the string definition
     */
    public String toString()
    {
        return getBase() + " is a " + gender + " " + group + " declension noun meaning " + getTranslation();
    }

    /**
     * makeForms generates each form of the noun from the declension's preferred endings and the noun's stem
     */
    private void makeForms()
    {
        String[][] endings = Generator.endings[group.ordinal()][gender.ordinal()];
        forms = new String[Number.values().length][Case.values().length];
        for (int i = 0; i < endings.length; i++)
        {
            for (int j = 0; j < endings[i].length; j++)
            {
                if (endings[i][j] == null)
                    forms[i][j] = getBase();
                else
                    forms[i][j] = getStem() + endings[i][j];
            }
        }
    }

    /**
     * Checks if two nouns are equal
     * @param other the other object (needs to be a noun for equality)
     * @return whether they're equal
     */
    public boolean equals(Object other)
    {
        if (other instanceof Noun)
        {
            Noun testee = (Noun) other;
            if (formsEqual(testee))
            {
                return getBase().equals(testee.getBase()) && getTranslation().equals(testee.getTranslation());
            }
        }
        return false;
    }

    /**
     * Checks if two nouns have equal forms
     * @param other the other noun
     * @return whether their forms are equal
     */
    public boolean formsEqual(Noun other)
    {
        return Arrays.deepEquals(forms, other.getForms());
    }

    /**
     * Checks if the noun's forms match an array of forms
     * @param tries the forms to check vs the noun's
     * @return whether the forms are equal
     */
    public boolean checkForms(String[][] tries)
    {
        for (int row = 0; row < tries.length; row++)
        {
            for (int col = 0; col < tries[row].length; col++)
            {
                if (!tries[row][col].equals(forms[col][row]))
                    return false;
            }
        }
        return true;
    }

    /**
     * wrongForms returns an array of the corrected forms that don't match in the noun's forms and the tries array
     * @param tries the forms to check vs the noun's
     * @return the array containing any corrected forms
     */
    public String[][] wrongForms(String[][] tries)
    {
        String[][] errors = new String[Case.values().length][Number.values().length];
        for (int row = 0; row < tries.length; row++)
        {
            for (int col = 0; col < tries[row].length; col++)
            {
                if (!tries[row][col].equals(forms[col][row]))
                    errors[row][col] = forms[col][row];
            }
        }
        return errors;
    }

    /**
     * Returns the noun's forms
     * @return the String forms of the noun
     */
    public String[][] getForms()
    {
        return forms;
    }

    /**
     * Returns the noun's Gender
     * @return the gender
     */
    public Gender getGender()
    {
        return gender;
    }

    /**
     * Returns the noun's declension
     * @return the declension
     */
    public Declension getDec()
    {
        return group;
    }
}
