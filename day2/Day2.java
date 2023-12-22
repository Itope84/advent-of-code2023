package day2;

import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.util.List;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.HashMap;

public class Day2 {
  static int getResults() {
    return getResults(false);
  }

  static int getPart2Results(ArrayList<Game> games) {
    int sum = 0;

    for (Game game : games) {
      sum += game.getPower();
    }

    return sum;
  }

  static int getPart1Results(ArrayList<Game> games) {
    int idSum = 0;

    for (Game game : games) {
      if (game.isPossible()) {
        idSum += game.id;
      }
    }

    return idSum;
  }

  static int getResults(Boolean isPartTwo) {
    ArrayList<Game> games = new ArrayList<Game>();

    try {
      File inputFile = new File("./day2/day2.txt");

      Scanner reader = new Scanner(inputFile);
      while (reader.hasNextLine()) {
        String line = reader.nextLine();
        Game game = new Game(line);

        games.add(game);
      }
      reader.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    return isPartTwo ? getPart2Results(games) : getPart1Results(games);
  }
}

class Game {
  int id;
  ArrayList<GameSet> gameSets;

  Game(String inputRow) {
    String[] splitByIdAndInfo = inputRow.split(": ");
    String idPart = splitByIdAndInfo[0];
    String rest = splitByIdAndInfo[1];

    /**
     * Avoiding the use of regular expressions to make this easier to read for
     * people unfamiliar with regexes
     */
    // idPart looks like "Game X". Split by "Game " will give us the array ["",
    // "X"]. So we take the arr[1] and parse into integer
    String idString = idPart.split("Game ")[1];
    this.id = Integer.parseInt(idString);

    // Alternatively, we could use regex which is more flexible
    // Pattern pattern = Pattern.compile("Game (\\d+)");
    // Matcher matcher = pattern.matcher(idPart);
    // matcher.find();
    // this.id = Integer.parseInt(matcher.group(1));

    // We also split the rest of the string by "; "
    String[] splitBySemicolon = rest.split("; ");
    this.gameSets = new ArrayList<>();
    for (String subset : splitBySemicolon) {
      this.gameSets.add(new GameSet(subset));
    }
  }

  public Boolean isPossible() {
    for (GameSet gameSet : this.gameSets) {
      if (!gameSet.isPossible()) {
        return false;
      }
    }
    return true;
  }

  private Map<Color, Integer> getMinimumPossibleCubeSet() {
    // the minimum possible cubeset to make the game possible is the maximum number
    // of the respective colors in this gameset.

    Map<Color, Integer> cubeset = new HashMap<>() {
      {
        put(Color.red, 0);
        put(Color.green, 0);
        put(Color.blue, 0);
      }
    };

    // iterate through all gamesets
    for (GameSet gameset : this.gameSets) {
      for (Color color : cubeset.keySet()) {
        if (gameset.getColorCount(color) > cubeset.get(color)) {
          cubeset.put(color, gameset.getColorCount(color));
        }
      }
    }

    return cubeset;
  }

  public int getPower() {
    int product = 1;

    Map<Color, Integer> cubeset = this.getMinimumPossibleCubeSet();

    for (Color color : cubeset.keySet()) {
      product *= cubeset.get(color);
    }

    return product;
  }
}

enum Color {
  red,
  green,
  blue
}

class GameSet {
  // for easy access, we'll store the counts in a hashmap.
  private Map<String, Integer> colorCountMap = new HashMap<>() {
    {
      put("red", 0);
      put("green", 0);
      put("blue", 0);
    }
  };

  // we could just use string and make sure the strings never go beyond
  // red/green/blue, but we'll use the Color enum to make it more structured.
  public int getColorCount(Color color) {
    return colorCountMap.get(color.name());
  }

  GameSet(String setStr) {
    String[] splitByComma = setStr.split(", ");
    // Now we have something like: ["3 blue", "2 green", "6 red"]
    // we just check the color of each one and assign the count
    for (String colorStmt : splitByComma) {
      String[] splitBySpace = colorStmt.split(" ");
      int count = Integer.parseInt(splitBySpace[0]);
      String color = splitBySpace[1];
      this.colorCountMap.put(color, count);
    }
  }

  public Boolean isPossible() {
    Map<String, Integer> maxColorCountMap = new HashMap<>() {
      {
        put("red", 12);
        put("green", 13);
        put("blue", 14);
      }
    };

    for (String color : this.colorCountMap.keySet()) {
      if (this.colorCountMap.get(color) > maxColorCountMap.get(color)) {
        return false;
      }
    }
    return true;
  }
}