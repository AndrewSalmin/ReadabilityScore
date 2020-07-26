package readability;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    private static String readFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

    private static void removePunctuationMarks(String[] words) {
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (isPunctuationMark(word.charAt(word.length() - 1))) {
                StringBuilder modifiedWord = new StringBuilder();
                for (int j = 0; j < word.length() - 1; j++) {
                    modifiedWord.append(word.charAt(j));
                }
                words[i] = modifiedWord.toString();
            }
        }
    }

    private static boolean isPunctuationMark(char character) {
        return character == ',' || character == ';' || character == ':' || character == '.' || character == '!' || character == '?';
    }

    private static int countCharacters(String text) {
        int characters = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) != ' ' && text.charAt(i) != '\n' && text.charAt(i) != '\t') {
                characters++;
            }
        }
        return characters;
    }

    private static int countSyllables(String word) {
        int vowels = 0;
        boolean skipped = false;
        for (int i = 0; i < word.length(); i++) {
            if (isVowel(word.charAt(i))) {
                if (i == 0) {
                    vowels++;
                } else {
                    if (!isVowel(word.charAt(i - 1))) {
                        vowels++;
                    } else {
                        if (skipped) {
                            vowels++;
                            skipped = false;
                        } else {
                            skipped = true;
                        }
                    }
                }
            } else {
                if (skipped) {
                    skipped = false;
                }
            }
        }

        if (word.charAt(word.length() - 1) == 'e') {
            vowels--;
        }

        if (vowels > 0) {
            return vowels;
        } else {
            return 1;
        }
    }

    private static boolean isVowel(char character) {
        return character == 'a' || character == 'e' || character == 'i' || character == 'o' || character == 'u' || character == 'y' || character == 'A' || character == 'E' || character == 'I' || character == 'O' || character == 'U' || character == 'Y';
    }

    private static double calculateScoreARI(int characters, int words, int sentences) {
        double scoreARI = 4.71 * characters / words + 0.5 * words / sentences - 21.43;
        scoreARI = (double) Math.round(scoreARI * 100) / 100;
        return scoreARI;
    }

    private static double calculateScoreFK(int words, int sentences, int syllables) {
        double scoreFK = 0.39 * words / sentences + 11.8 * syllables / words - 15.59;
        scoreFK = (double) Math.round(scoreFK * 100) / 100;
        return scoreFK;
    }

    private static double calculateScoreSMOG(int polysyllables, int sentences) {
        double scoreSMOG = 1.043 * Math.sqrt(polysyllables * 30.0 / sentences) + 3.1291;
        scoreSMOG = (double) Math.round(scoreSMOG * 100) / 100;
        return scoreSMOG;
    }

    private static double calculateScoreCL(int characters, int words, int sentences) {
        double L = (double) characters / words * 100;
        double S = (double) sentences / words * 100;
        double scoreCL = 0.0588 * L - 0.296 * S - 15.8;
        scoreCL = (double) Math.round(scoreCL * 100) / 100;
        return scoreCL;
    }

    private static void printScoreARI(double scoreARI) {
        System.out.println("Automated Readability Index: " + scoreARI + " (about " + defineAge(scoreARI) + " year olds).");
    }

    private static void printScoreFK(double scoreFK) {
        System.out.println("Flesch–Kincaid readability tests: " + scoreFK + " (about " + defineAge(scoreFK) + " year olds).");
    }

    private static void printScoreSMOG(double scoreSMOG) {
        System.out.println("Simple Measure of Gobbledygook: " + scoreSMOG + " (about " + defineAge(scoreSMOG) + " year olds).");
    }

    private static void printScoreCL(double scoreCL) {
        System.out.println("Coleman–Liau index: " + scoreCL + " (about " + defineAge(scoreCL) + " year olds).");
    }

    private static String defineAge(double score) {
        String age = null;
        switch ((int) Math.round(score)) {
            case 1:
                age = "6";
                break;
            case 2:
                age = "7";
                break;
            case 3:
                age = "9";
                break;
            case 4:
                age = "10";
                break;
            case 5:
                age = "11";
                break;
            case 6:
                age = "12";
                break;
            case 7:
                age = "13";
                break;
            case 8:
                age = "14";
                break;
            case 9:
                age = "15";
                break;
            case 10:
                age = "16";
                break;
            case 11:
                age = "17";
                break;
            case 12:
                age = "18";
                break;
            case 13:
            case 14:
                age = "24";
                break;
        }
        return age;
    }

    public static void main(String[] args) {
        String filePath = args[0];
        String text = null;
        try {
            text = readFileAsString(filePath);
        } catch (IOException e) {
            System.out.println("Cannot read file: " + e.getMessage());
        }

        assert text != null;
        String[] words = text.split(" ");
        removePunctuationMarks(words);
        int numberOfWords = words.length;

        String[] sentences = text.split("[.!?]\\s");
        int numberOfSentences = sentences.length;

        int numberOfCharacters = countCharacters(text);

        int numberOfSyllables = 0;
        for (String word : words) {
            numberOfSyllables += countSyllables(word);
        }

        int numberOfPolysyllables = 0;
        for (String word : words) {
            if (countSyllables(word) > 2) {
                numberOfPolysyllables++;
            }
        }

        System.out.println("The text is:");
        System.out.println(text);
        System.out.println();
        System.out.println("Words: " + numberOfWords);
        System.out.println("Sentences: " + numberOfSentences);
        System.out.println("Characters: " + numberOfCharacters);
        System.out.println("Syllables: " + numberOfSyllables);
        System.out.println("Polysyllables: " + numberOfPolysyllables);
        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        System.out.println();

        switch (input) {
            case "ARI":
                double scoreARI = calculateScoreARI(numberOfCharacters, numberOfWords, numberOfSentences);
                printScoreARI(scoreARI);
                break;
            case "FK":
                double scoreFK = calculateScoreFK(numberOfWords, numberOfSentences, numberOfSyllables);
                printScoreFK(scoreFK);
                break;
            case "SMOG":
                double scoreSMOG = calculateScoreSMOG(numberOfPolysyllables, numberOfSentences);
                printScoreSMOG(scoreSMOG);
                break;
            case "CL":
                double scoreCL = calculateScoreCL(numberOfCharacters, numberOfWords, numberOfSentences);
                printScoreCL(scoreCL);
                break;
            case "all":
                scoreARI = calculateScoreARI(numberOfCharacters, numberOfWords, numberOfSentences);
                printScoreARI(scoreARI);
                scoreFK = calculateScoreFK(numberOfWords, numberOfSentences, numberOfSyllables);
                printScoreFK(scoreFK);
                scoreSMOG = calculateScoreSMOG(numberOfPolysyllables, numberOfSentences);
                printScoreSMOG(scoreSMOG);
                scoreCL = calculateScoreCL(numberOfCharacters, numberOfWords, numberOfSentences);
                printScoreCL(scoreCL);
                System.out.println();
                double averageAge = (Double.parseDouble(defineAge(scoreARI)) + Double.parseDouble(defineAge(scoreFK)) + Double.parseDouble(defineAge(scoreSMOG)) + Double.parseDouble(defineAge(scoreCL))) / 4;
                averageAge = (double) Math.round(averageAge * 100) / 100;
                System.out.println("This text should be understood in average by " + averageAge + " year olds.");
                break;
        }
    }
}