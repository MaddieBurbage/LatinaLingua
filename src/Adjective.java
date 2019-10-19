import java.util.Arrays;

public class Adjective extends Word
{
    /**group is the Declension of this adjective*/
    private final Declension group;
    /**forms contains the forms for each case, gender, and number*/
    private String[][][] forms;

    /**
     * Constructer to make a new Adjective instance
     * @param base is the base, masculine nominative singular form of the adjective
     * @param stem is the root part of the adjective that endings are added to for each form
     * @param translation is the english definition of the adjective
     * @param group is the adjective's declension
     */
    public Adjective(String base, String stem, String translation, Declension group)
    {
        super(base, stem, translation);
        this.group = group;
        makeForms();
    }

    /**
     * Returns a string form of the adjective
     * @return the string definition
     */
    public String toString()
    {
        return getBase() + " is a "  + group + " declension adjective meaning " + getTranslation();
    }

    /**
     * makeForms generates each form of the adjective from the declension's preferred endings and the adjective's stem
     */
    private void makeForms()
    {
        String[][][] endings = Generator.endings[group.ordinal()];
        forms = new String[Gender.ANY.ordinal()][Number.values().length][Case.values().length];

        for(int i = 0; i<forms.length; i++)
        {
            for(int j = 0; j<endings[i].length; j++)
            {
                for(int k = 0; k<endings[i][j].length; k++)
                {
                    if(endings[i][j][k]==null)
                        forms[i][j][k] = getBase();
                    else
                        forms[i][j][k] = getStem() + endings[i][j][k];
                }
            }
        }
    }

    /**
     * Checks if two adjectives are equal
     * @param other the other object (needs to be a adjective for equality)
     * @return whether they're equal
     */
    public boolean equals(Object other)
    {
        if(other instanceof Adjective)
        {
            Adjective testee = (Adjective) other;
            if(formsEqual(testee))
            {
                return getBase().equals(testee.getBase()) && getTranslation().equals(testee.getTranslation());
            }
        }
        return false;
    }

    /**
     * Checks if two adjectives have equal forms
     * @param other the other adjective
     * @return whether their forms are equal
     */
    public boolean formsEqual(Adjective other)
    {
        return Arrays.deepEquals(forms, other.getForms());
    }

    /**
     * Checks if the adjective's forms match an array of forms
     * @param tries the forms to check vs the adjective's
     * @return whether the forms are equal
     */
    public boolean checkForms(String[][] tries)
    {
        for(int gen = 0; gen< forms.length; gen++)
        {
            for(int num = 0; num< forms[gen].length; num++)
            {
                for(int cas = 0; cas< forms[gen][num].length; cas++)
                {
                    if(!tries[gen*Case.values().length+cas][num].equals(forms[gen][num][cas]))
                        return false;
                }
            }
        }
        return true;
    }

    /**
     * wrongForms returns an array of the forms that don't match for the adjective's forms and the tries array
     * @param tries the forms to check vs the adjective's
     * @return the array containing any corrected forms
     */
    public String[][] wrongForms(String[][] tries)
    {
        String[][] errors = new String[Gender.ANY.ordinal()*Case.values().length][Number.values().length];
        for(int gen = 0; gen< forms.length; gen++)
        {
            for(int num = 0; num< forms[gen].length; num++)
            {
                for(int cas = 0; cas< forms[gen][num].length; cas++)
                {
                    if(!tries[gen*Case.values().length+cas][num].equals(forms[gen][num][cas]))
                        errors[gen*Case.values().length+cas][num] = forms[gen][num][cas];
                }
            }
        }
        return errors;
    }

    /**
     * Returns the adjective's forms
     * @return the String forms of the adjective
     */
    public String[][][] getForms()
    {
        return forms;
    }

    /**
     * Returns the adjective's declension
     * @return the declension
     */
    public Declension getDec()
    {
        return group;
    }
}
