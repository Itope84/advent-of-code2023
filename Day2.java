import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.util.List;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.HashMap;

/**
Instructions:

You're launched high into the atmosphere! The apex of your trajectory just barely reaches the surface of a large island floating in the sky. You gently land in a fluffy pile of leaves. It's quite cold, but you don't see much snow. An Elf runs over to greet you.

The Elf explains that you've arrived at Snow Island and apologizes for the lack of snow. He'll be happy to explain the situation, but it's a bit of a walk, so you have some time. They don't get many visitors up here; would you like to play a game in the meantime?

As you walk, the Elf shows you a small bag and some cubes which are either red, green, or blue. Each time you play this game, he will hide a secret number of cubes of each color in the bag, and your goal is to figure out information about the number of cubes.

To get information, once a bag has been loaded with cubes, the Elf will reach into the bag, grab a handful of random cubes, show them to you, and then put them back in the bag. He'll do this a few times per game.

You play several games and record the information from each game (your puzzle input). Each game is listed with its ID number (like the 11 in Game 11: ...) followed by a semicolon-separated list of subsets of cubes that were revealed from the bag (like 3 red, 5 green, 4 blue).

For example, the record of a few games might look like this:

Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
In game 1, three sets of cubes are revealed from the bag (and then put back again). The first set is 3 blue cubes and 4 red cubes; the second set is 1 red cube, 2 green cubes, and 6 blue cubes; the third set is only 2 green cubes.

The Elf would first like to know which games would have been possible if the bag contained only 12 red cubes, 13 green cubes, and 14 blue cubes?

In the example above, games 1, 2, and 5 would have been possible if the bag had been loaded with that configuration. However, game 3 would have been impossible because at one point the Elf showed you 20 red cubes at once; similarly, game 4 would also have been impossible because the Elf showed you 15 blue cubes at once. If you add up the IDs of the games that would have been possible, you get 8.

Determine which games would have been possible if the bag had been loaded with only 12 red cubes, 13 green cubes, and 14 blue cubes. What is the sum of the IDs of those games?

--- Part Two ---
The Elf says they've stopped producing snow because they aren't getting any water! He isn't sure why the water stopped; however, he can show you how to get to the water source to check it out for yourself. It's just up ahead!

As you continue your walk, the Elf poses a second question: in each game you played, what is the fewest number of cubes of each color that could have been in the bag to make the game possible?

Again consider the example games from earlier:

Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
In game 1, the game could have been played with as few as 4 red, 2 green, and 6 blue cubes. If any color had even one fewer cube, the game would have been impossible.
Game 2 could have been played with a minimum of 1 red, 3 green, and 4 blue cubes.
Game 3 must have been played with at least 20 red, 13 green, and 6 blue cubes.
Game 4 required at least 14 red, 3 green, and 15 blue cubes.
Game 5 needed no fewer than 6 red, 3 green, and 2 blue cubes in the bag.
The power of a set of cubes is equal to the numbers of red, green, and blue cubes multiplied together. The power of the minimum set of cubes in game 1 is 48. In games 2-5 it was 12, 1560, 630, and 36, respectively. Adding up these five powers produces the sum 2286.

For each game, find the minimum set of cubes that must have been present. What is the sum of the power of these sets?
*/

public class Day2 {
  static int getResults() {
    return getResults(false);
  }

  static int getPart2Results(ArrayList<Game> games) {
    int sum = 0;

    for(Game game: games) {
      sum += game.getPower();
    }

    return sum;
  }

  static int getPart1Results(ArrayList<Game> games) {
    int idSum = 0;

    for(Game game: games) {
      if(game.isPossible()) {
        idSum += game.id;
      }
    }

    return idSum;
  }
  
  static int getResults(Boolean isPartTwo) {
    ArrayList<Game> games = new ArrayList<Game>();
    
    try {
      File inputFile = new File("./day2.txt");

      Scanner reader = new Scanner(inputFile);
      while (reader.hasNextLine()) {
        String line = reader.nextLine();
        Game game = new Game(line);

        games.add(game);
      }
      reader.close();
    } catch(FileNotFoundException e) {
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
    Avoiding the use of regular expressions to make this easier to read for people unfamiliar with regexes
    */
    // idPart looks like "Game X". Split by "Game " will give us the array ["", "X"]. So we take the arr[1] and parse into integer
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
    for(String subset: splitBySemicolon) {
      this.gameSets.add(new GameSet(subset));
    }
  }

  public Boolean isPossible() {
    for (GameSet gameSet: this.gameSets) {
      if (!gameSet.isPossible()) {
        return false;
      }
    }
    return true;
  }

  private Map<Color, Integer> getMinimumPossibleCubeSet() {
    // the minimum possible cubeset to make the game possible is the maximum number of the respective colors in this gameset.

    Map<Color, Integer> cubeset = new HashMap<>(){{
      put(Color.red, 0);
      put(Color.green, 0);
      put(Color.blue, 0);
    }};

    // iterate through all gamesets
    for(GameSet gameset: this.gameSets) {
      for(Color color: cubeset.keySet()) {
        if(gameset.getColorCount(color) > cubeset.get(color)) {
          cubeset.put(color, gameset.getColorCount(color));
        }
      }
    }

    return cubeset;
  }

  public int getPower() {
    int product = 1;

    Map<Color, Integer> cubeset = this.getMinimumPossibleCubeSet();

    for(Color color: cubeset.keySet()) {
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
  private Map<String, Integer> colorCountMap = new HashMap<>() {{
      put("red", 0);
      put("green", 0);
      put("blue", 0);
  }};

  // we could just use string and make sure the strings never go beyond red/green/blue, but we'll use the Color enum to make it more structured.
  public int getColorCount(Color color) {
    return colorCountMap.get(color.name());
  }

  GameSet(String setStr) {
    String[] splitByComma = setStr.split(", ");
    // Now we have something like: ["3 blue", "2 green", "6 red"]
    // we just check the color of each one and assign the count
    for (String colorStmt: splitByComma) {
      String[] splitBySpace = colorStmt.split(" ");
      int count = Integer.parseInt(splitBySpace[0]);
      String color = splitBySpace[1];
      this.colorCountMap.put(color, count);
    }
  }

  public Boolean isPossible() {
    Map<String, Integer> maxColorCountMap = new HashMap<>() {{
        put("red", 12);
        put("green", 13);
        put("blue", 14);
    }};

    for (String color: this.colorCountMap.keySet()) {
      if (this.colorCountMap.get(color) > maxColorCountMap.get(color)) {
        return false;
      }
    }
    return true;
  }
}