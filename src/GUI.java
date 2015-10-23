import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.Glow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.Observable;
import java.util.Observer;


/**
 * Created by Chris on 5/7/2015.
 *
 * GUI for Connect 4
 * GUI based off GameModel
 *
 * NOTE: AI FUNCTIONALITY DOES NOT WORK
 */
public class GUI extends Application implements Observer {
    private final static int w = 7;
    private final static int h = 6;
    private static int player1 = 1;
    private static int player2 = 2;
    private static int winner = 3;

    private static Color PLAYER_1_COLOR;
    private static Color PLAYER_2_COLOR;
    private boolean COLOR_ERROR = false;

    //private GameModel game;
    private GameModel game;

    private Stage stage;
    private BorderPane pane;
    private GridPane grid;
    private Text status;

    public static boolean full;
    public static boolean colFull;

    /**
     * Initializes GameModel
     */
    public void init(){
        //For Standard GameModel
        //this.game = new GameModel();

        //For AI GameModel
        this.game = new GameModel();
        game.addObserver(this);
        full = false;
        colFull = false;
    }

    /**
     * Starts the GUI
     * @param stage
     */
    public void start(Stage stage){
        this.stage = stage;
        settings();
    }

    /**
     * Creates the buttons
     * @return the buttons
     */
    private HBox makeButtonPane() {
        HBox buttons = new HBox();

        for (int i = 1; i <= w; i++) {
            int col = i;
            Button button = new Button("" + col + "");
            button.setPrefWidth(100);
            button.setOnAction(event -> {game.selectColumn(game.getPlayer(), col);});
            buttons.getChildren().add(button);
        }

        return buttons;
    }

    /**
     * Creates the grid of circles
     * @return the grid
     */
    public GridPane makeGridPane(){
        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-color: yellow;");

        for (int i = h; i > 0; i--){
            for (int j = 0; j < w; j++){
                Circle circle = new Circle(50, Color.WHITE);
                circle.setStroke(Color.BLACK);
                grid.add(circle, j, i-1);
            }
        }

        return grid;
    }

    /**
     * Creates the end of game popup
     * @param bool true if someone won, false if the board is full
     */
    public void popup(boolean bool){
        Stage popup = new Stage();
        FlowPane pane = new FlowPane();
        VBox box = new VBox();
        HBox buttons = new HBox();

        //Sets the text for the end of game popup
        Text result;
        if (bool) {
            result = new Text("GAME OVER! PLAYER " + game.getWinningPlayer() + " WINS!");
            result.setFont(Font.font("Verdana", FontWeight.BOLD, 40));
        } else {
            result = new Text("GAME OVER! BOARD IS FULL!");
            result.setFont(Font.font("Verdana", FontWeight.BOLD, 40));
        }

        Text newGame = new Text("Would you like to play another game?");
        newGame.setFont(Font.font(35));

        //Yes - would like to play another game
        Button yes = new Button("YES");
        yes.setOnAction(event -> {
            init();
            game.clearBoard();
            enableButtons();
            popup.close();
        });
        yes.setPrefWidth(100);
        yes.setLineSpacing(100);

        //No - would not like to play another game
        Button no = new Button("NO");
        no.setOnAction(event -> {System.exit(0);});
        no.setPrefWidth(100);
        no.setLineSpacing(100);

        //Spacing for yes and no
        buttons.setSpacing(50);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(yes, no);

        //Centers the buttons
        box.setSpacing(35);
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(result, newGame, buttons);

        pane.setAlignment(Pos.CENTER);
        pane.getChildren().add(box);

        //Sets size of popup
        pane.setPrefSize(730, 250);

        popup.setTitle("GAME OVER");
        popup.setScene(new Scene(pane));
        popup.show();
    }

    /**
     * updates the GUI based off the GameModel
     * @param o GameModel
     * @param arg
     */
    public void update(Observable o, Object arg){
        boolean over = false;

        if (colFull == true){
            this.status.setFill(Color.RED);
            this.status.setText("COLUMN IS FULL. PLAYER " + game.getPlayer() + " PLEASE TRY AGAIN.");
        } else {
            this.status.setFill(Color.BLACK);
            this.status.setText("Player " + game.getPlayer() + "'s turn!");
        }

        for (Node i : grid.getChildren()) {
            int idx = grid.getChildren().indexOf(i);
            Circle x = (Circle) i;

            //Set circle to empty
            if (game.getBoard().values[idx] == 0) {
                x.setFill(Color.WHITE);
            }
            //Set circle to player 1 color
            if (game.getBoard().values[idx] == player1) {
                x.setFill(PLAYER_1_COLOR);
            }
            //Set circle to player 2 color
            if (game.getBoard().values[idx] == player2) {
                x.setFill(PLAYER_2_COLOR);
            }
            //Set circle to green when a player has won
            if (game.getBoard().values[idx] == winner) {
                x.setFill(Color.GREEN);
                x.setEffect(new Glow(500));
                this.status.setText("GAME OVER! PLAYER " + game.getWinningPlayer() + " WINS!");
                over = true;
            }
        }

        colFull = false;

        if (over){
            disableButtons();
            popup(true);
        }
        if (full){
            disableButtons();
            popup(false);
        }

        if (game.computerMove(game.getPlayer())){
            disableButtons();
        } else {
            enableButtons();
        }
    }

    /**
     * Enables the buttons
     * Allows them to be clickable
     */
    public void enableButtons(){
        FlowPane pane = ((FlowPane)((BorderPane) this.pane.getCenter()).getTop());
        HBox box = (HBox)pane.getChildren().get(0);
        for (Node button: box.getChildren()){
            Button k = (Button) button;
            k.setDisable(false);
        }
    }

    /**
     * Disables the buttons
     * Makes the buttons unclickable
     */
    public void disableButtons(){
        FlowPane pane = ((FlowPane)((BorderPane) this.pane.getCenter()).getTop());
        HBox box = (HBox)pane.getChildren().get(0);
        for (Node button: box.getChildren()){
            Button k = (Button) button;
            k.setDisable(true);
        }
    }

    /**
     * Displays the settings window
     */
    public void settings(){
        Stage settings = new Stage();

        BorderPane pane = new BorderPane();

        //top
        FlowPane top = new FlowPane();
        Text header = new Text("WELCOME TO CONNECT 4!");
        header.setFont(Font.font("Verdana", FontWeight.BOLD, 50));
        top.getChildren().add(header);
        top.setAlignment(Pos.CENTER);
        top.setPrefSize(1000, 150);
        pane.setTop(top);

        //Center
        VBox center = new VBox();

        HBox options = new HBox();
        options.setSpacing(100);

        //Options for player 1
        VBox player1box = new VBox();
        player1box.setSpacing(25);
        final ToggleGroup player1 = new ToggleGroup();

        //Options for player 2
        VBox player2box = new VBox();
        player2box.setSpacing(25);
        final ToggleGroup player2 = new ToggleGroup();

        Text player1text = new Text("Player 1");
        player1text.setTextAlignment(TextAlignment.CENTER);

        RadioButton human1 = new RadioButton("Human");
        //human1.setOnAction(event -> {game.initializeHuman(1);});
        human1.setToggleGroup(player1);
        human1.setSelected(true);

        RadioButton computer1 = new RadioButton("Computer");
        computer1.setOnAction(event -> {game.initializeAI(1);});
        computer1.setToggleGroup(player1);

        player1box.getChildren().addAll(player1text, human1, computer1);

        Text player2text = new Text("Player 2");
        player2text.setTextAlignment(TextAlignment.CENTER);

        RadioButton human2 = new RadioButton("Human");
        //human2.setOnAction(event -> {game.initializeHuman(2);});
        human2.setToggleGroup(player2);
        human2.setSelected(true);

        RadioButton computer2 = new RadioButton("Computer");
        computer2.setOnAction(event -> {
            game.initializeAI(2);
        });
        computer2.setToggleGroup(player2);

        player2box.getChildren().addAll(player2text,human2, computer2);

        Text player1color = new Text("\n\nColor");
        Text player1space = new Text("\n");
        Text player2color = new Text("\n\nColor");
        Text player2space = new Text("\n");

        //Colors
        ObservableList<String> colors = FXCollections.observableArrayList("Red", "Black", "Blue", "Yellow", "Pink", "Purple");

        final ComboBox comboBox1 = new ComboBox(colors);
        comboBox1.setValue("Red");
        final ComboBox comboBox2 = new ComboBox(colors);
        comboBox2.setValue("Black");

        player1box.getChildren().addAll(player1color, comboBox1, player1space);
        player2box.getChildren().addAll(player2color, comboBox2, player2space);

        options.getChildren().addAll(player1box, player2box);
        options.setAlignment(Pos.CENTER);

        Text ERROR_MESSAGE = new Text("Please select two different colors!\n");
        ERROR_MESSAGE.setFill(Color.RED);
        ERROR_MESSAGE.setFont(Font.font("Verdana", FontWeight.BOLD, 35));

        center.getChildren().add(options);
        if (COLOR_ERROR){
            center.getChildren().add(ERROR_MESSAGE);
        }
        center.setAlignment(Pos.CENTER);
        pane.setCenter(center);

        //bottom
        FlowPane bot = new FlowPane();

        Button enter = new Button("Ok");
        enter.setOnAction(event -> {
            setPlayer1Color(comboBox1.getValue());
            setPlayer2Color(comboBox2.getValue());
            if (PLAYER_1_COLOR.equals(PLAYER_2_COLOR)){
                COLOR_ERROR = true;
                settings();
            } else {
                COLOR_ERROR = false;
                displayMain();
            }
            settings.close();
        });
        enter.setPrefSize(200, 50);
        bot.getChildren().add(enter);
        bot.setAlignment(Pos.TOP_CENTER);
        bot.setPrefSize(100, 100);
        pane.setBottom(bot);

        pane.setPrefSize(800,500);

        settings.setTitle("CONNECT 4");
        settings.setScene(new Scene(pane));
        settings.show();

    }

    /**
     * Displays the main board
     */
    public void displayMain(){
        BorderPane pane = new BorderPane();

        //top
        FlowPane topPane = new FlowPane();
        this.status = new Text("Player " + game.getPlayer() + "'s turn!");
        topPane.setAlignment(Pos.CENTER);
        topPane.getChildren().add(status);
        pane.setTop(topPane);

        //center
        BorderPane innerPane = new BorderPane();

        FlowPane top = new FlowPane();
        HBox buttons = makeButtonPane();
        top.setAlignment(Pos.CENTER);
        top.getChildren().add(buttons);
        innerPane.setTop(top);

        FlowPane center = new FlowPane();
        grid = makeGridPane();
        center.setAlignment(Pos.CENTER);
        center.getChildren().add(grid);
        innerPane.setCenter(center);

        pane.setCenter(innerPane);

        //bottom
        FlowPane bot = new FlowPane();
        Button reset = new Button("RESET");
        reset.setStyle("-fx-base:  #4169E1");
        reset.setPrefWidth(720);

        reset.setOnAction(event -> {
            game.clearBoard();
        });

        bot.setAlignment(Pos.CENTER);
        bot.getChildren().add(reset);
        pane.setBottom(bot);

        this.pane = pane;

        stage.setTitle("Connect 4");
        stage.setScene(new Scene(pane));
        stage.show();
    }

    /**
     * Sets player 1's color
     * @param o String representing the color
     */
    public void setPlayer1Color(Object o){
        String color = o.toString();

        if (color.equals("Red")){
            PLAYER_1_COLOR = Color.RED;
        }
        if (color.equals("Black")){
            PLAYER_1_COLOR = Color.BLACK;
        }
        if (color.equals("Blue")){
            PLAYER_1_COLOR = Color.BLUE;
        }
        if (color.equals("Yellow")){
            PLAYER_1_COLOR = Color.YELLOW;
        }
        if (color.equals("Pink")){
            PLAYER_1_COLOR = Color.DEEPPINK;
        }
        if (color.equals("Purple")){
            PLAYER_1_COLOR = Color.PURPLE;
        }
    }

    /**
     * Sets player 2's color
     * @param o String representing the color
     */
    public void setPlayer2Color(Object o){
        String color = o.toString();

        if (color.equals("Red")){
            PLAYER_2_COLOR = Color.RED;
        }
        if (color.equals("Black")){
            PLAYER_2_COLOR = Color.BLACK;
        }
        if (color.equals("Blue")){
            PLAYER_2_COLOR = Color.BLUE;
        }
        if (color.equals("Yellow")){
            PLAYER_2_COLOR = Color.YELLOW;
        }
        if (color.equals("Pink")){
            PLAYER_2_COLOR = Color.DEEPPINK;
        }
        if (color.equals("Purple")){
            PLAYER_2_COLOR = Color.PURPLE;
        }
    }

    public static void main(String[] args){
        Application.launch(args);
    }
}
