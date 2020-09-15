package readability;

import java.io.IOException;
import java.util.Scanner;
import java.io.File;

public class Main {

    public static void main(String[] args) {
        String text;

        try (Scanner scanner = new Scanner(new File(args[0]))) {
            text = collectText(scanner);
        } catch (Exception e) {
            System.out.println("Failed to load file.");
            return;
        }

        System.out.println("The text is:");
        System.out.println(text);

        int characters = countCharacters(text);
        int words = countWords(text);
        int sentences = countSentences(text);
        int syllables = countSyllables(text);
        int polysyllables = countPolySyllablesWord(text);

        double score = 4.71 * characters / words + 0.5 * words / sentences - 21.43;

        System.out.println("Words: " + words);
        System.out.println("Sentences: " + sentences);
        System.out.println("Characters: " + characters);
        System.out.println("Syllables: " + syllables);
        System.out.println("Polysyllables: " + polysyllables);

        double ARI = countARI(characters, words, sentences);
        double FK = countFK(words, sentences, syllables);
        double SMOG = countSMOG(polysyllables, sentences);
        double CL = countCl(100 *characters / words, 100 * sentences / words);

        double average = (ARI + FK + SMOG + CL) / 4.0;


        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        Scanner scan = new Scanner(System.in);
        System.out.println();

        switch (scan.next()) {
            case "ARI":
                System.out.println("Automated Readability Index: " + ARI + " (about " + getAges(ARI) + " year olds).");
                break;
            case "FK":
                System.out.println("Flesch–Kincaid readability tests: " + FK + " (about " + getAges(FK) + " year olds).");
                break;
            case "SMOG":
                System.out.println("Simple Measure of Gobbledygook: " + SMOG + " (about " + getAges(SMOG) + " year olds).");
                break;
            case "CL":
                System.out.println("Coleman–Liau index: " + CL + " (about " + getAges(CL) + " year olds).");
                break;
            case "all":
                System.out.println("Automated Readability Index: " + ARI + " (about " + getAges(ARI) + " year olds).");
                System.out.println("Flesch–Kincaid readability tests: " + FK + " (about " + getAges(FK) + " year olds).");
                System.out.println("Simple Measure of Gobbledygook: " + SMOG + " (about " + getAges(SMOG) + " year olds).");
                System.out.println("Coleman–Liau index: " + CL + " (about " + getAges(CL) + " year olds).");
                System.out.println();
                System.out.println("This text should be understood in average by " + average + " year olds.");
                break;
            default:
                System.out.println("error");
                return;
        }

        //System.out.println("The text should be understood by " + years + " year olds.");
    }

    public static String collectText(Scanner scanner) {
        StringBuilder result = new StringBuilder();
        while (scanner.hasNextLine()) {
            result.append(scanner.nextLine());
            result.append(" ");
        }
        return result.toString();
    }

    public static int countCharacters(String text) {
        int result = 0;
        for (String word : text.split("\\s")) {
            result += word.length();
        }
        return result;
    }

    public static int countWords(String text) {
        return text.split(" ").length;
    }

    public static int countSentences(String text) {
        return text.split("[.?!] ").length;
    }

    public static boolean isVowel(char c) {
        return ((Character)c).toString().matches("[aeiouyAEIOUY]");
    }

    public static int getSyllablesInWord(String word) {
        int result = 0;
        for (int i = 0; i < word.length(); ++i) {
            if (!isVowel(word.charAt(i))) {
                continue;
            }
            if (i >= 1 && isVowel(word.charAt(i - 1)) || i == word.length() - 1 && word.charAt(i) == 'e') {
                continue;
            }
            ++result;
        }
        result = result == 0 ? 1 : result;
        return result;
    }

    public static int countSyllables(String text) {
        int result = 0;
        for (String word : text.split("[.?! ]+")) {
            result += getSyllablesInWord(word);
        }
        return result;
    }

    public static int countPolySyllablesWord(String text) {
        int result = 0;
        for (String word : text.split("[.?! ]+")) {
            int syllables = getSyllablesInWord(word);
            result += (syllables >= 3) ? 1 : 0;
        }
        return result;
    }

    public static double countARI (int characters, int words, int sentences) {
        return 4.71 * characters / words + 0.5 * words / sentences - 21.43;
    }

    public static double countFK (int words, int sentences, int syllables) {
        return 0.39 * words / sentences + 11.8 * syllables / words - 15.59;
    }

    public static double countSMOG(int polysyllables, int sentences) {
        return 1.043 * Math.sqrt(polysyllables * 30.0 / sentences) + 3.1291;
    }

    public static double countCl(double l, double s) {
        return 0.0588 * l - 0.296 * s - 15.8;
    }

    public static int getAges(double score) {
        switch ((int)score + 1) {
            case 1:
                return 6;
            case 2:
                return 7;
            case 3:
                return 9;
            case 4:
                return 10;
            case 5:
                return 11;
            case 6:
                return 12;
            case 7:
                return 13;
            case 8:
                return 14;
            case 9:
                return 15;
            case 10:
                return 16;
            case 11:
                return 17;
            case 12:
                return 18;
            case 13:
                return 19;
            case 14:
                return 24;
            default:
                return 0;
        }
    }
}
