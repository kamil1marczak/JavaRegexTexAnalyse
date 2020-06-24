package readability;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static Scanner scanner = new Scanner(System.in);
    public static String readable = "";
    public static double charactersNumber = 0;
    public static double wordsNumber = 0;
    public static double sentencesNumber = 0;
    public static int syllablesNumber = 0;
    public static int polySyllablesNumber = 0;

    public static String readFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

    public static void main(String[] args) {

        String pathToFile = args[0];
        try {
            readable = readFileAsString(pathToFile);
        } catch (IOException e) {
            System.out.println("Cannot read file: " + e.getMessage());
        }

        Pattern patternCharacters = Pattern.compile("\\S");
        Matcher matcherCharacters = patternCharacters.matcher(readable);
        charactersNumber = (double) matcherCharacters.results().count();

        String[] sentences = readable.split("\\s*[.!?]\\s*");

        for (String sentence : sentences) {
            sentencesNumber += 1;
            String[] words = sentence.split("[ \\s]+");
            for (String word : words) {
                wordsNumber += 1;

                String wordRegex = word.replaceAll("e$", "b");

                String patternString = "[aeiouy]+";
                Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(wordRegex);
                int counter = (int) matcher.results().count();
                switch (counter) {
                    case 0:
                    case 1:
                        syllablesNumber += 1;
                        break;
                    case 2:
                        syllablesNumber += 2;
                        break;
                    default:
                        syllablesNumber += counter;
                        polySyllablesNumber += 1;
                        break;
                }
            }
        }

        ReadableAnalyser analyser = new ReadableAnalyser();

        System.out.println("The text is:");
        System.out.println(readable + "\n");
        System.out.println("Words: " + (int) wordsNumber);
        System.out.println("Sentences: " + (int) sentencesNumber);
        System.out.println("Characters: " + (int) charactersNumber);
        System.out.println("Syllables: " + syllablesNumber);
        System.out.println("Polysyllables: " + polySyllablesNumber);
        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        String scoreType = scanner.nextLine();

        switch (scoreType) {
            case "ARI":
                System.out.println("Automated Readability Index: " + analyser.getScoreARI() + " (about "+ analyser.age(analyser.getScoreARI())  +" year olds).");
                break;
            case "FK":
                System.out.println("Flesch–Kincaid readability tests: " + analyser.getScoreFK() + " (about "+ analyser.age(analyser.getScoreFK())  +" year olds).");
                break;
            case "SMOG":
                System.out.println("Simple Measure of Gobbledygook: " + analyser.getScoreSMOG() + " (about "+ analyser.age(analyser.getScoreSMOG())  +" year olds).");
                break;
            case "CL":
                System.out.println("Coleman–Liau index: " + analyser.getScoreCL() + " (about "+ analyser.age(analyser.getScoreCL())  +" year olds).");
                break;
            case "all":
                System.out.println("Automated Readability Index: " + analyser.getScoreARI() + " (about "+ analyser.age(analyser.getScoreARI())  +" year olds).");
                System.out.println("Flesch–Kincaid readability tests: " + analyser.getScoreFK() + " (about "+ analyser.age(analyser.getScoreFK())  +" year olds).");
                System.out.println("Simple Measure of Gobbledygook: " + analyser.getScoreSMOG() + " (about "+ analyser.age(analyser.getScoreSMOG())  +" year olds).");
                System.out.println("Coleman–Liau index: " + analyser.getScoreCL() + " (about "+ analyser.age(analyser.getScoreCL())  +" year olds).");
                break;
        }
    }
}

class ReadableAnalyser {

    public static float charactersNumber = (float) readability.Main.charactersNumber;
    public static float wordsNumber = (float) readability.Main.wordsNumber;
    public static float sentencesNumber = (float) readability.Main.sentencesNumber;
    public static float syllablesNumber = (float) readability.Main.syllablesNumber;
    public static float polySyllablesNumber = (float) readability.Main.polySyllablesNumber;
    public static float scoreARI;
    public static float scoreFK;
    public static float scoreSMOG;
    public static float scoreCL;

    ReadableAnalyser() {
        scoreARI = (float) (4.71 * (charactersNumber / wordsNumber) + 0.5 * (wordsNumber / sentencesNumber) - 21.43);
        scoreFK = (float) (0.39 * (wordsNumber/sentencesNumber) + 11.8 * (syllablesNumber/wordsNumber) - 15.59 );
        scoreSMOG = (float) (1.043 * Math.sqrt(polySyllablesNumber * (30 / sentencesNumber)) + 3.1291);
        scoreCL = (float) (0.0588 * 100 * (charactersNumber/wordsNumber) - 0.296 * 100 * (sentencesNumber / wordsNumber) - 15.8);
    }

    float getScoreARI() {
        return (float) (Math.round(scoreARI * 100) / 100.00);
    }

    float getScoreFK() {
        return (float) (Math.round(scoreFK * 100) / 100.00);
    }

    float getScoreSMOG() {
        return (float) (Math.round(scoreSMOG * 100) / 100.00);
    }

    float getScoreCL() {
        return (float) (Math.round(scoreCL * 100) / 100.00);
    }

    protected String age(float score) {

        Dictionary<Integer, String> age = new Hashtable<>();
        age.put(1, "6");
        age.put(2, "7");
        age.put(3, "9");
        age.put(4, "10");
        age.put(5, "11");
        age.put(6, "12");
        age.put(7, "13");
        age.put(8, "14");
        age.put(9, "15");
        age.put(10, "16");
        age.put(11, "17");
        age.put(12, "18");
        age.put(13, "24");
        age.put(14, "24 +");

        int scoreInt = Math.round(score);

        return age.get(scoreInt);

    }

}
