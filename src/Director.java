import org.w3c.dom.*;
import java.util.*;

public class Director
{
    /** A string constant for the relative filepath to files*/
    public static final String fileStart = "src\\";
    /** A string constant for the file extension for xml files*/
    public static final String fileEnd = ".xml";
    /** A string constant for the noun declension file*/
    public static final String storedDec = "Declensions";
    /** A string constant for the adjective declension file*/
    public static final String storedAdjDec = "AdjDeclensions";
    /** A full list of words for the current part of speech*/
    private static ArrayList<Word> dictionary;
    /** A list of words up next to be used for the game*/
    private static ArrayList<Word> currents;

    /**
     * Starts the main GUI after loading the correct words from the given POS
     * @param selected the POS to use for starting the game
     */
    public static void play(POS selected)
    {
        loadDictionary(selected);
        TestScreen game = new TestScreen(selected, dictionary.get(0));
    }

    /**
     * Creates the dictionary and the set of current words
     * @param selected the POS for the dictionary's words
     */
    public static void loadDictionary(POS selected)
    {
        dictionary = setup(selected);
        currents = (ArrayList<Word>) dictionary.clone();
    }
    /**
     * Generates the word endings and words themselves for the current POS
     * @param current the current POS in the game
     * @return the list of words created
     */
    public static ArrayList<Word> setup(POS current)
    {
        switch(current)
        {
            case NOUN:
                Document endings = Generator.parseXML(fileStart + storedDec + fileEnd);
                Generator.formEndings(endings);
                Document doc = Generator.parseXML(fileStart + current.getText() + fileEnd);
                return new ArrayList<Word>(Generator.formNouns(doc));
            case ADJECTIVE:
                endings = Generator.parseXML(fileStart + storedAdjDec + fileEnd);
                Generator.formEndings(endings);
                doc = Generator.parseXML(fileStart + current.getText() + fileEnd);
                return new ArrayList<Word>(Generator.formAdjs(doc));
            case VERB:
        }
        throw new IllegalArgumentException("Sorry, Verb is not set up yet");
    }

    /**
     * Selects the current nouns based on gender and declension constraints
     * @param chosenGen the constraining gender
     * @param chosenDec the constraining declension
     * @return the first noun in currents
     */
    public static Noun chooseNouns(Gender chosenGen, Declension chosenDec)
    {
        currents.clear();
        if(dictionary.get(0) instanceof Noun)
        {
            for(Word w : dictionary)
            {
                Noun target = (Noun) w;
                if(target.getDec() == chosenDec)
                {
                    if(target.getGender() == chosenGen)
                        currents.add(target);
                    if((target.getGender()!=Gender.NEUT && chosenGen==Gender.HUM) || chosenGen == Gender.ANY)
                        currents.add(target);
                }
            }
        }
        if(currents.size()==0)
            return null;
        return (Noun) currents.get(0);
    }

    /**
     * Selects the current adjectives based on declension constraints
     * @param chosenDec the constraining declension
     * @return the first adjective in currents
     */
    public static Adjective chooseAdjs(Declension chosenDec)
    {
        currents.clear();
        if(dictionary.get(0) instanceof Adjective)
        {
            for(Word w : dictionary)
            {
                Adjective target = (Adjective) w;
                if(target.getDec() == chosenDec)
                {
                    currents.add(target);
                }
            }
        }
        if(currents.size()==0)
            return null;
        return (Adjective) currents.get(0);
    }

    /**
     * Updates the word list (currents) by removing the first word (if there're still words there)
     * @return the next word up
     */
    public static Word updateWord()
    {
        if(currents.size()==0)
            return null;
        currents.remove(0);
        if(currents.size()==0)
            return null;
        return currents.get(0);
    }
}
