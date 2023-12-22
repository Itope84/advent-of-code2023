package day1;

import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.util.List;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.HashMap;

public class Day1 {
  static void getResults() {
    getResults(false);
  }

  static void getResults(Boolean isPartTwo) {
    List<Integer> nums = new ArrayList<>();

    try {
      File inputFile = new File("./day1.txt");

      Scanner reader = new Scanner(inputFile);
      while (reader.hasNextLine()) {
        String data = reader.nextLine();
        nums.add(isPartTwo ? getPartTwoCalibrationValueFromLine(data) : getCalibrationValueFromLine(data));
      }
      reader.close();

      System.out.println(sum(nums));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  public static int getCalibrationValueFromLine(String line) {
    String numStr = "";
    // from the front
    for (int i = 0; i < line.length(); i++) {
      if (Character.isDigit(line.charAt(i))) {
        numStr += line.charAt(i);
        break;
      }
    }

    // from the back
    for (int i = line.length() - 1; i >= 0; i--) {
      if (Character.isDigit(line.charAt(i))) {
        numStr += line.charAt(i);
        break;
      }
    }

    return Integer.parseInt(numStr);
  }

  public static int sum(List<Integer> nums) {
    int sum = 0;
    for (int n : nums) {
      sum += n;
    }

    return sum;
  }

  public static int getPartTwoCalibrationValueFromLine(String line) {
    /**
     * In the part 2, some digits are actually spelled out. To get the correct
     * values, we need to find the actual first and last digits, whether spelled out
     * or written as a digit.
     */

    Map<String, Integer> wordDigitMap = new HashMap<>() {
      {
        put("one", 1);
        put("two", 2);
        put("three", 3);
        put("four", 4);
        put("five", 5);
        put("six", 6);
        put("seven", 7);
        put("eight", 8);
        put("nine", 9);
        put("zero", 0);
      }
    };

    String digits = "";

    // Loop through the characters, and a if it is a digit, replace last character
    // in digits with it. If it is not, just find substr from 0 to this point.

    // check if the substr ends with any of the keys of wordDigitMap and get its
    // digit

    for (int i = 0; i < line.length(); i++) {
      if (Character.isDigit(line.charAt(i))) {
        digits += line.charAt(i);
      } else {
        String currSubstr = line.substring(0, i + 1);
        // go through the keys see if currSubstr ends with any of them
        // if it does, add the digit to digits
        for (String key : wordDigitMap.keySet()) {
          if (currSubstr.endsWith(key)) {
            digits += wordDigitMap.get(key);
            break;
          }
        }
      }
    }

    String startEndDigits = "" + digits.charAt(0) + digits.charAt(digits.length() - 1);
    return Integer.parseInt(startEndDigits);
  }
}
