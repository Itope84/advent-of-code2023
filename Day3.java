import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Day3 {
    static int getResults() {
        ArrayList<String[]> rows = new ArrayList<String[]>();

        try {
            File inputFile = new File("./day2.txt");

            Scanner reader = new Scanner(inputFile);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                rows.add(line.split("."));

                // Now we have something like ["467", "", "", "114" ...] containing empty
                // strings, numbers or dots
            }
            reader.close();

            for (int i = 0; i < rows.size(); i++) {
                String[] row = rows.get(i);
                for (int j = 0; j < row.length; j++) {
                    String cell = row[j];
                    if (cell == ".") {
                        row[j] = "0";
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return 0;
    }
}

class PartNumber {
}