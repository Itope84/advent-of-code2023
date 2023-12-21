import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Day3 {
    public static void main(String[] args) {
        System.out.println("Part 1: " + getResults());
    }

    static int getResults() {
        ArrayList<String> rows = new ArrayList<String>();
        int sum = 0;

        try {
            File inputFile = new File("./day3.txt");

            Scanner reader = new Scanner(inputFile);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                rows.add(line);
            }
            reader.close();

            for (int i = 0; i < rows.size(); i++) {
                // The simplest solution is to go through all the characters in each row, find a
                // full number, check the box around it to see if it has a symbol around it, if
                // it does, we add it to the sum, and then we continue with the row until the
                // end. Bonus point is no regexes.

                String currentNumStr = "";
                String row = rows.get(i);
                for (int j = 0; j < row.length(); j++) {
                    char c = row.charAt(j);

                    if (!Character.isDigit(c))
                        continue;

                    // if we're still here, it is a digit.
                    currentNumStr += c;
                    // if the next character is a digit, we don't yet have the complete number,
                    // hence we simply continue down the row.
                    if (j + 1 < row.length() && Character.isDigit(row.charAt(j + 1)))
                        continue;

                    // if we're still here, we now have a complete number. We want to check the box
                    // around it and find out if it has a symbol around it.
                    // The box around it starts at row i - 1, char j - 1, and ends at row i + 1.
                    // Because of diagonals, we start ar the column before the first column in the
                    // currentNumStr. That is obtained by j - currentNumStr.length(). We end at j +
                    // 1 since j will be the last character in the currentNumStr.

                    for (int x = Math.max(i - 1, 0); x <= Math.min(i + 1, rows.size() - 1); x++) {
                        System.out.println("x: " + x);
                        String boundingBoxRow = rows.get(x);

                        int startBoxCol = Math.max(j - currentNumStr.length(), 0);
                        for (int y = startBoxCol; y <= Math.min(j + 1, boundingBoxRow.length() - 1); y++) {
                            char boxChar = boundingBoxRow.charAt(y);
                            // if it is not a number and is not a ".", then it is a symbol surrounding the
                            // currentNumStr amd currentNumStr is a valid "part number"
                            if (!Character.isDigit(boxChar) && boxChar != '.') {
                                // So we add it to the sum.
                                sum += Integer.parseInt(currentNumStr);
                                break;
                            }
                        }
                    }
                    // reset the currentNumStr
                    currentNumStr = "";
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return sum;
    }
}

class PartNumber {
}