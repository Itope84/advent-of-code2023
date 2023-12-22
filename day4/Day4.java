package day4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Day4 {
    public static void main(String[] args) {
        System.out.println("Part 1: " + getResults());
    }

    public static int getResults() {
        ArrayList<Card> cards = new ArrayList<Card>();

        try {
            // Should probably make these first 4 lines into a reusable function by now.
            File inputFile = new File("./day4/day4.txt");

            Scanner reader = new Scanner(inputFile);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                cards.add(new Card(line));
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return getPart1Results(cards);
    }

    public static int getPart1Results(ArrayList<Card> cards) {
        int sum = 0;
        for (Card card : cards) {
            sum += card.getScore();
        }
        return sum;
    }
}

class Card {
    String rowStr;
    String[] winningNumbers;
    String[] ownedNumbers;

    Card(String rowStr) {
        this.rowStr = rowStr;
        String[] splitById = rowStr.trim().split(":");
        // split by the pipe character
        String[] splitMainSection = splitById[1].trim().split("[|]");
        // split by one or more spaces
        this.winningNumbers = splitMainSection[0].trim().split("[ ]+");
        // split by one or more spaces
        this.ownedNumbers = splitMainSection[1].trim().split("[ ]+");

        // System.out.println("Winning numbers: " + Arrays.asList(this.ownedNumbers));
    }

    public ArrayList<String> getOwnedWinningNumbers() {
        ArrayList<String> ownedWinningNumbers = new ArrayList<String>();

        for (String ownedNumber : this.ownedNumbers) {
            if (Arrays.asList(this.winningNumbers).contains(ownedNumber)) {
                ownedWinningNumbers.add(ownedNumber);
            }
        }

        return ownedWinningNumbers;
    }

    /**
     * Returns the score of the card. The score is calculated by 2 to the power of
     * number of owned winning numbers - 1, as lond there is at least one winning
     * card
     * 
     * @return
     */
    public int getScore() {
        ArrayList<String> ownedWinningNumbers = this.getOwnedWinningNumbers();

        return ownedWinningNumbers.size() > 0 ? (int) (Math.pow(2, ownedWinningNumbers.size() - 1)) : 0;
    }
}