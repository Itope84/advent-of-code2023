package day3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Day3 {
    public static void main(String[] args) {
        System.out.println("Part 1: " + getResults());
        System.out.println("Part 2: " + getResults(true));
    }

    static int getResults() {
        return getResults(false);
    }

    static int getResults(boolean isPart2) {
        ArrayList<Row> rows = new ArrayList<Row>();

        try {
            File inputFile = new File("./day3.txt");

            Scanner reader = new Scanner(inputFile);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                Row row = new Row(line, rows.size() > 0 ? rows.get(rows.size() - 1) : null);
                // update prev row's next row to be this row.
                if (rows.size() > 0) {
                    rows.get(rows.size() - 1).setNextRow(row);
                }
                rows.add(row);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return isPart2 ? getPart2Results(rows) : getPart1Results(rows);
    }

    static int getPart1Results(ArrayList<Row> rows) {
        int sum = 0;
        for (Row row : rows) {
            for (PartNumber partNumber : row.getPartNumbers()) {
                Character adjacentSymbol = partNumber.getSomeAdjacentSymbol();
                if (adjacentSymbol != null)
                    sum += partNumber.number;
            }
        }
        return sum;
    }

    static int getPart2Results(ArrayList<Row> rows) {
        // for part 2, we do the same thing, but in reverse. We find a symbol and then
        // find adjacent numbers.
        int sum = 0;
        for (Row row : rows) {
            ArrayList<Integer> starPositions = row.getStarPositions();

            for (Integer pos : starPositions) {
                // look in the prev, current and next rows for the PartNumbers and find all
                // PartNumbers adjacent to the star.
                Row[] rowsToCheck = { row.prevRow, row, row.nextRow };
                ArrayList<Integer> adjacentPartNumbers = new ArrayList<Integer>();
                for (Row r : rowsToCheck) {
                    if (r != null) {
                        for (PartNumber partNumber : r.getPartNumbers()) {
                            // the number is adjacent if the endCol is pos - 1 or startCol is pos + 1 or
                            // startCol <= pos && endCol >= pos (that last part won't be true for numbers on
                            // the same row as the star, but that's fine)
                            if (partNumber.startCol == pos + 1 || partNumber.endCol == pos - 1
                                    || (partNumber.startCol <= pos && partNumber.endCol >= pos)) {
                                adjacentPartNumbers.add(partNumber.number);
                            }
                        }
                    }
                }

                // A gear is any * symbol that is adjacent to exactly two part numbers. Its gear
                // ratio is the result of multiplying those two numbers together.

                if (adjacentPartNumbers.size() == 2) {
                    sum += adjacentPartNumbers.get(0) * adjacentPartNumbers.get(1);
                }
            }
        }
        return sum;
    }
}

// for fun, let's represent a Row with a class. That way we can simply store
// prevRow and nextRow as it's properties. This does increase the space used but
// space complexity is still linear and so it's fine. This is kind of similar to
// a Tree structure.
class Row {
    /**
     * nullable
     */
    Row prevRow;
    /**
     * nullable. (We can use @Nullable annotation here, but those require
     * external classes)
     */
    Row nextRow;
    String rowStr;
    // should be a getter.
    // ArrayList<PartNumber> partNumbers;

    public Row(String rowStr) {
        this(rowStr, null);
        // get part numbers from the rowstr.
        // this.partNumbers = new ArrayList<PartNumber>();
    }

    public Row(String rowStr, Row prevRow) {
        this(rowStr, prevRow, null);
    }

    public Row(String rowStr, Row prevRow, Row nextRow) {
        this.rowStr = rowStr;
        this.prevRow = prevRow;
        this.nextRow = nextRow;
        // get part numbers from the rowstr.
        // this.partNumbers = new ArrayList<PartNumber>();
    }

    public void setNextRow(Row nextRow) {
        this.nextRow = nextRow;
    }

    /**
     * Gets the positions of the stars in this row.
     */
    public ArrayList<Integer> getStarPositions() {
        ArrayList<Integer> starPositions = new ArrayList<Integer>();
        for (int i = 0; i < this.rowStr.length(); i++) {
            if (this.rowStr.charAt(i) == '*')
                starPositions.add(i);
        }
        return starPositions;
    }

    public ArrayList<PartNumber> getPartNumbers() {
        ArrayList<PartNumber> partNumbers = new ArrayList<PartNumber>();

        String currentNumStr = "";
        for (int j = 0; j < this.rowStr.length(); j++) {
            char c = this.rowStr.charAt(j);

            if (!Character.isDigit(c))
                continue;

            // if we're still here, it is a digit.
            currentNumStr += c;

            // if the next character is a digit, we don't yet have the complete number,
            // hence we simply continue down the row.
            if (j + 1 < this.rowStr.length() && Character.isDigit(this.rowStr.charAt(j + 1)))
                continue;
            // Otherwise, we have a complete number. Add it to partNumbers and reset
            // currentNumStr.
            partNumbers.add(new PartNumber(Integer.parseInt(currentNumStr), j - currentNumStr.length() + 1, j, this));
            currentNumStr = "";
        }

        return partNumbers;
    }
}

class PartNumber {
    int number;
    int startCol;
    int endCol;
    Row row;

    public PartNumber(int number, int startCol, int endCol, Row row) {
        this.number = number;
        this.startCol = startCol;
        this.endCol = endCol;
        this.row = row;
    }

    /**
     * Gets some symbol adjacent to the part number.
     * This method exits after the first adjacent symbol is found.
     */
    public Character getSomeAdjacentSymbol() {
        // check the current row, look for a symbol at either startCol - 1 or endCol +
        // 1.
        if (this.endCol + 1 < this.row.rowStr.length() && this.row.rowStr.charAt(this.endCol + 1) != '.')
            return this.row.rowStr.charAt(this.endCol + 1);

        if (this.startCol > 0 && this.row.rowStr.charAt(this.startCol - 1) != '.')
            return this.row.rowStr.charAt(this.startCol - 1);

        // if those aren't symbols, check the prev and next row, look for a symbol from
        // startCol - 1 to endCol + 1 to account for diagonals.
        Row[] boundingRows = { this.row.prevRow, this.row.nextRow };
        for (Row r : boundingRows) {
            if (r != null) {
                String rowStr = r.rowStr;
                // The Math.max and Math.min are to ensure we don't go out of bounds.
                for (int i = Math.max(startCol - 1, 0); i <= Math.min(endCol + 1, rowStr.length() - 1); i++) {
                    char c = rowStr.charAt(i);
                    if (!Character.isDigit(c) && c != '.')
                        return c;
                }
            }
        }

        return null;
    }
}