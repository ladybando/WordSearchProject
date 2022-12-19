import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FinalProject_WordSearch extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        GameBoard board = new GameBoard();
        board.startGame();
        Scene scene = new Scene(board);
        stage.setTitle("Word Search");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    class WordSearch extends BorderPane {
        public String wordSelected;
        public String wordList;
        String[][] wordBoard;
        StackPane[][] stackPanes;
        Button enter = new Button("Enter");

        public void displayBoard() {

        }

        public static void highlightSelectedWord() {
            //set new color when clicked
        }

        public static void isSelected() {
            //returns true if clicked
        }

    }

    class GameBoard extends BorderPane {
        //sets the number of wins to write to GameStats
        private int wins, row, column;
        //tracks letter location on board
        int charLocation;
        //actual game board
        private char gameBoard[][] = new char[9][9];
        //sets the size of the board
        private int boardSize = 9;
        //all possible letters to populate game board
        private final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        GameDictionary dictionary = new GameDictionary();
        ArrayList<String> words = dictionary.getDictionaryList();
        StackPane[][] stackPanes;
        GridPane grid;
        Button enter = new Button("Enter");

        public void startGame() {
            buildGameBoard();
            //fillTheBlanks(row, column);
        }

        public void buildGameBoard() {
            //Random value generator
            Random rand = new Random();
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            hBox.setPadding(new Insets(5));
            setTop(hBox);

            grid = new GridPane();
            row = rand.nextInt(boardSize);
            column = rand.nextInt(boardSize);
            stackPanes = new StackPane[boardSize][boardSize];
            for (int i = 0; i < gameBoard.length; i++) {
                   for (int j = 0; j < gameBoard[i].length; j++) {
                    gameBoard[i][j] = dictionary.getValueAt(i).charAt(charLocation);
                    Label label = new Label(" " + gameBoard[i][j] + " ");
                    Label alphabetLabel = fillTheBlanks(i, j);
                    label.setWrapText(false);
                    label.setFont(Font.font(20.0));
                    stackPanes[i][j] = new StackPane(label, alphabetLabel);
                    stackPanes[i][j].setStyle("-fx-border-color: black;" +
                            "-fx-border-radius: 3");
                    grid.add(stackPanes[i][j], j, i);
                }
            }

            grid.setHgap(0.10);
            grid.setVgap(0.10);
            grid.setAlignment(Pos.CENTER);
            grid.setPadding(new Insets(10));
            grid.setStyle("-fx-border-color: black");

            setMargin(grid, new Insets(20));

            setCenter(grid);
        }

        /**
         * Fill remaining blank spaces with random letters
         */
        public Label fillTheBlanks(int row, int column) {
            Random rand = new Random();
            Label label = new Label();
            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    if (gameBoard[row][column] == ' ') {
                        //fills spaces without words with a random letter
                        gameBoard[row][column] = ALPHABET.charAt(rand.nextInt(ALPHABET.length()));
                        label = new Label(" " + gameBoard[row][column] + " ");
                    }
                }
            }
            return label;
        }

        public boolean checkContains(String word) {
            return dictionary.isInDictionary(word);
        }

        public void removeFoundWord(int position) {
            words.remove(position);
        }

        public int totalWins() {
            return wins;
        }
    }

    class GameDictionary {
        public ArrayList<String> dictionaryList = new ArrayList<>();

        public ArrayList<String> getDictionaryList() {
            try {
                dictionaryList = readDictionary();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return this.dictionaryList;
        }

        public boolean isInDictionary(String word) {
            return dictionaryList.contains(word);
        }

        public String getValueAt(int position) {
            return dictionaryList.get(position);
        }

        public void clearDictionaryList() {
            dictionaryList.clear();
        }

        public ArrayList<String> readDictionary() throws IOException {
            //list that holds strings of a file to use to build array
            List<String> wordsList = new ArrayList<>();
            // load data from file
            try (
                    FileReader wordsFile = new FileReader("fantasyWordSearch.txt")
            ) {
                // Create a string to store each character to form word
                String words = "";
                char characters;
                // check for end of file
                while (wordsFile.ready()) {
                    characters = (char) wordsFile.read();
                    // Used to specify delimiters in file
                    if (characters == '\n' || characters == ' ' || characters == ',') {
                        // Store each string in arraylist
                        wordsList.add(words);
                        // clear content in string for words to build array
                        words = "";
                    } else {
                        // appending each character to string if current character is not delimiter
                        words += characters;
                    }
                }
                dictionaryList.add(words);
            }
            return dictionaryList;
        }
    }

    class GameStates {
        public void writeStats() throws IOException {
            GameBoard board = new GameBoard();
            int wins = board.totalWins();
            try (
                    FileOutputStream winsOutput = new FileOutputStream("/wins.dat")
            ) {
                for (int i = 0; i < 10; i++) {
                    winsOutput.write(wins);
                }
            }
        }
    }
}