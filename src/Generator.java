import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import java.util.*;
import java.io.*;

public class Generator
{
    /** The array of word endings read from the xml*/
    public static String[][][][] endings;

    /**
     * Creates a Document object from an xml file
     * @param relPath the path to the xml file
     * @return the Document
     */
    public static Document parseXML(String relPath)
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        File f = new File(relPath);
        Document doc;
        try
        {
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(f);
            return doc;
        } catch (ParserConfigurationException | SAXException | IOException e)
        {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    /**
     * Makes an array of some words' properties as read from xml Word nodes
     * @param doc The XML document
     * @return the array of word properties
     */
    public static String[][] trackWords(Document doc)
    {
        NodeList words = doc.getElementsByTagName("Word"); //all the words
        NodeList wordProps; //all the word properties
        String[][] allProps = new String[words.getLength()][]; //all the properties, total
        String[] properties; //word properties as strings
        for (int i = 0; i < words.getLength(); i++)
        {
            words.item(i).normalize();
            wordProps = words.item(i).getChildNodes();
            cleanUp(wordProps);
            properties = new String[wordProps.getLength()];
            for (int j = 0; j < wordProps.getLength(); j++)
            {
                Node next = wordProps.item(j).getFirstChild();
                if (next != null)
                {
                    properties[j] = next.getTextContent().trim();
                }
            }
            allProps[i] = properties;
        }
        return allProps;
    }

    /**
     * Forms all the Noun objects contained in an XML document
     * @param doc the XML Document
     * @return an arraylist of the Nouns
     */
    public static ArrayList<Noun> formNouns(Document doc)
    {
        ArrayList<Noun> words = new ArrayList<Noun>();
        String[][] allProps = trackWords(doc);
        for (String[] props : allProps)
        {
            words.add(new Noun(props[0], props[2], props[4], Gender.getGen(props[3]), Declension.getDec(props[1])));
        }
        return words;
    }

    /**
     * Forms all the Adjective objects contained in an XML document
     * @param doc the XML Document
     * @return an arraylist of the Adjectives
     */
    public static ArrayList<Adjective> formAdjs(Document doc)
    {
        ArrayList<Adjective> words = new ArrayList<Adjective>();
        String[][] allProps = trackWords(doc);
        for (String[] props : allProps)
        {
            words.add(new Adjective(props[0], props[2], props[3], Declension.getDec(props[1])));
        }
        return words;
    }

    /**
     * Forms the ending rules for a POS as read from its XML Document
     * @param doc the XML Document containing the endings
     */
    public static void formEndings(Document doc)
    {
        NodeList declensions = doc.getElementsByTagName("Group"); //all the declensions
        NodeList declensionProps; //all the declension's properties
        endings = new String[Declension.values().length][Gender.HUM.ordinal()][][]; //all the properties, total
        for (int i = 0; i < declensions.getLength(); i++) //each declension
        {
            declensions.item(i).normalize();
            declensionProps = declensions.item(i).getChildNodes();
            cleanUp(declensionProps);
            int dec = Declension.getDec(declensionProps.item(0).getFirstChild().getNodeValue().trim()).ordinal();
            for (int j = 1; j < declensionProps.getLength(); j++) //each gender
            {
                Node current = declensionProps.item(j);
                NodeList numbers = current.getChildNodes();
                cleanUp(numbers);
                String gender = current.getNodeName();
                if (Gender.HUM.equals(gender) || Gender.ANY.equals(gender))
                {
                    endings[dec][Gender.MASC.ordinal()] = craftArray(numbers);
                    endings[dec][Gender.FEM.ordinal()] = craftArray(numbers);
                    if (Gender.ANY.equals(gender))
                    {
                        endings[dec][Gender.NEUT.ordinal()] = craftArray(numbers);
                    }
                } else
                {
                    int gen = Gender.getGen(gender).ordinal();
                    endings[dec][gen] = craftArray(numbers);
                }
            }
        }
    }

    /**
     * Forms a String array with the deep-level text contents of a node
     * @param numbers The list of parent nodes (which are Number nodes)
     * @return the 2d String array of data stored in the sub-nodes
     */
    public static String[][] craftArray(NodeList numbers)
    {
        String[][] forms = new String[numbers.getLength()][];
        for (int i = 0; i < numbers.getLength(); i++)//for each number
        {
            Node number = numbers.item(i);
            NodeList cases = number.getChildNodes();
            cleanUp(cases);
            forms[i] = new String[Declension.values().length];
            for (int j = 0; j < number.getChildNodes().getLength(); j++)//for each case
            {
                Node wordCase = cases.item(j);
                if (wordCase != null && wordCase.getFirstChild() != null)
                {
                    int cas = Case.getCase(wordCase.getNodeName()).ordinal();
                    forms[i][cas] = wordCase.getFirstChild().getTextContent().trim();
                }
            }
        }
        return forms;
    }

    /**
     * Removes whitespace and newline nodes from a node list
     * @param focus the node list to tidy up
     */
    public static void cleanUp(NodeList focus)
    {
        for (int i = 0; i < focus.getLength(); i++)
        {
            if (focus.item(i).getFirstChild() == null)
            {
                focus.item(i).getParentNode().removeChild(focus.item(i));
                i--;
            }
        }
    }
}