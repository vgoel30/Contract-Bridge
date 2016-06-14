/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridge.controller;

import bridge.data.card;
import bridge.gui.GameTable;
import java.util.ArrayList;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 *
 * @author varungoel
 */
public class GameController extends Application {

    static int totalTurns = 0;

    ArrayList<String> ranks;

    static GameTable gameTable;

    static SetUpController setUpController;
    static ViewController viewController;

    static MoveController moveController;

    //playing card deck
    static ArrayList<card> deck;

    static card bestCard;

    static ArrayList<card> playerOneCards;
    static ArrayList<card> playerOneCardsInPlay;

    static ArrayList<card> playerTwoCards;
    static ArrayList<card> playerTwoCardsInPlay;

    static ArrayList<card> playerThreeCards;
    static ArrayList<card> playerThreeCardsInPlay;

    static ArrayList<card> playerFourCards;
    static ArrayList<card> playerFourCardsInPlay;

    static String roundWinner;

    static int[] scores;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        getTableReady();

        Scene scene = new Scene(gameTable.getTableDeck(), 1170, 700);
        primaryStage.setScene(scene);

        primaryStage.show();

    }

    public void getTableReady() {
        gameTable = new GameTable();
        setUpController = new SetUpController();
        viewController = new ViewController();
        moveController = new MoveController();

        //keep track of all cards; for scoring
        playerFourCards = new ArrayList<>();
        playerThreeCards = new ArrayList<>();
        playerTwoCards = new ArrayList<>();
        playerOneCards = new ArrayList<>();
        //keep track of current cards in play
        playerFourCardsInPlay = new ArrayList<>();
        playerThreeCardsInPlay = new ArrayList<>();
        playerTwoCardsInPlay = new ArrayList<>();
        playerOneCardsInPlay = new ArrayList<>();

        scores = new int[4];

        scores[0] = 0;
        scores[1] = 0;
        scores[2] = 0;
        scores[3] = 0;

        gameTable.layoutGUI();

        //get the shuffled deck of cards
        deck = setUpController.shuffle(setUpController.generateDeck());
        //distribute cards to each player
        setUpController.distributeCards(deck, playerOneCards, playerTwoCards, playerThreeCards, playerFourCards);

        playerOneCardsInPlay.addAll(playerOneCards);
        playerTwoCardsInPlay.addAll(playerTwoCards);
        playerThreeCardsInPlay.addAll(playerThreeCards);
        playerFourCardsInPlay.addAll(playerFourCards);

        //set up the player cards on the table
        gameTable.layoutCards(playerOneCards);
    }

    /**
     * Handles a card click and removes the card from the view
     *
     * @param cardClicked is the clicked card
     * @param playerCardsContainer is the container showing the user's cards
     * @param middleSpace
     */
    public void handleCardClick(card cardClicked, HBox playerCardsContainer, HBox middleSpace) {
        if (!gameTable.getNextRoundContainer().isVisible()) {
            //the first roud, P1 goes first
            if (totalTurns++ == 0) {
                playerOneMoveFirst(cardClicked, playerCardsContainer, middleSpace);
            } else {
                //if P2 won the last round
                if (roundWinner.equals("P2")) {
                    //the card clicked must be a legitimate move set
                    if (moveController.moveIsLegit(cardClicked, playerOneCardsInPlay, bestCard)) {
                        playerOneMoveLast(cardClicked, playerCardsContainer, middleSpace);
                    }
                } //if P3 won the last round
                else if (roundWinner.equals("P3")) {
                    //the card clicked must be a legitimate move set
                    if (moveController.moveIsLegit(cardClicked, playerOneCardsInPlay, bestCard)) {
                        playerOneMoveThird(cardClicked, playerCardsContainer, middleSpace);
                    }
                } //if P4 won the last round
                else if (roundWinner.equals("P4")) {
                    //the card clicked must be a legitimate move set
                    if (moveController.moveIsLegit(cardClicked, playerOneCardsInPlay, bestCard)) {
                        playerOneMoveSecond(cardClicked, playerCardsContainer, middleSpace);
                    }
                } //if P1 won the last round
                else if (roundWinner.equals("P1")) {
                    playerOneMoveFirst(cardClicked, playerCardsContainer, middleSpace);

                }

                if (totalTurns == 13) {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Scores: " + scores[0] + " " + scores[1] + " " + scores[2] + " " + scores[3]);
                    alert.showAndWait();
                    resetGame();
                }
            }
        }
    }

    public void playerOneMoveFirst(card cardClicked, HBox playerCardsContainer, HBox middleSpace) {
        gameTable.getNextRoundContainer().setVisible(false);
        int indexToRemove = 0;
        int counter = 0;
        for (Node playerCard : playerCardsContainer.getChildren()) {
            ImageView currentCardView = (ImageView) playerCard;

            if (currentCardView.getId().equals(cardClicked.toString())) {
                indexToRemove = counter;
            }
            counter++;
        }
        playerCardsContainer.getChildren().remove(indexToRemove);
        viewController.putCardInMiddle(cardClicked, middleSpace);

        //remove the card that the user has played
        playerOneCardsInPlay.remove(cardClicked);
        bestCard = cardClicked;

        card playerTwoCard = moveController.bestCardToThrow(playerTwoCardsInPlay, cardClicked);
        playerTwoCardsInPlay.remove(playerTwoCard);
        viewController.putCardInMiddle(playerTwoCard, middleSpace);

        bestCard = moveController.getBetterCard(playerTwoCard, bestCard);

        card playerThreeCard = moveController.bestCardToThrow(playerThreeCardsInPlay, bestCard);
        playerThreeCardsInPlay.remove(playerThreeCard);
        viewController.putCardInMiddle(playerThreeCard, middleSpace);

        bestCard = moveController.getBetterCard(playerThreeCard, bestCard);

        card playerFourCard = moveController.bestCardToThrow(playerFourCardsInPlay, bestCard);
        playerFourCardsInPlay.remove(playerFourCard);
        viewController.putCardInMiddle(playerFourCard, middleSpace);

        bestCard = moveController.getBetterCard(playerFourCard, bestCard);

        //System.out.println("BEST CARD 1 : " + bestCard);
        //decide the round winner
        roundWinner = decideRoundWinner();
        //show the winner and the button to proceed to the next round
        gameTable.getNextRoundContainer().setVisible(true);
        //display the winner
        gameTable.getWinnerText().setText(roundWinner + " wins round " + totalTurns + "!");
    }

    public void playerOneMoveSecond(card cardClicked, HBox playerCardsContainer, HBox middleSpace) {
        int indexToRemove = 0;
        int counter = 0;
        for (Node playerCard : playerCardsContainer.getChildren()) {
            ImageView currentCardView = (ImageView) playerCard;

            if (currentCardView.getId().equals(cardClicked.toString())) {
                indexToRemove = counter;
            }
            counter++;
        }
        playerCardsContainer.getChildren().remove(indexToRemove);
        viewController.putCardInMiddle(cardClicked, middleSpace);

        //find the best card
        bestCard = moveController.getBetterCard(cardClicked, bestCard);

        //remove the card that the user has played
        playerOneCardsInPlay.remove(cardClicked);

        card playerTwoCard = moveController.bestCardToThrow(playerTwoCardsInPlay, cardClicked);
        playerTwoCardsInPlay.remove(playerTwoCard);
        viewController.putCardInMiddle(playerTwoCard, middleSpace);

        bestCard = moveController.getBetterCard(playerTwoCard, bestCard);

        card playerThreeCard = moveController.bestCardToThrow(playerThreeCardsInPlay, bestCard);
        playerThreeCardsInPlay.remove(playerThreeCard);
        viewController.putCardInMiddle(playerThreeCard, middleSpace);

        bestCard = moveController.getBetterCard(playerThreeCard, bestCard);

        //System.out.println("BEST CARD 4 : " + bestCard);
        //decide the round winner
        roundWinner = decideRoundWinner();
        //show the winner and the button to proceed to the next round
        gameTable.getNextRoundContainer().setVisible(true);
        //display the winner
        gameTable.getWinnerText().setText(roundWinner + " wins round " + totalTurns + "!");
    }

    public void playerOneMoveThird(card cardClicked, HBox playerCardsContainer, HBox middleSpace) {
        int indexToRemove = 0;
        int counter = 0;
        for (Node playerCard : playerCardsContainer.getChildren()) {
            ImageView currentCardView = (ImageView) playerCard;

            if (currentCardView.getId().equals(cardClicked.toString())) {
                indexToRemove = counter;
            }
            counter++;
        }
        playerCardsContainer.getChildren().remove(indexToRemove);
        viewController.putCardInMiddle(cardClicked, middleSpace);

        //remove the card that the user has played
        playerOneCardsInPlay.remove(cardClicked);

        bestCard = moveController.getBetterCard(cardClicked, bestCard);

        //second player goes next
        card playerTwoCard = moveController.bestCardToThrow(playerTwoCardsInPlay, bestCard);
        playerTwoCardsInPlay.remove(playerTwoCard);
        viewController.putCardInMiddle(playerTwoCard, middleSpace);

        bestCard = moveController.getBetterCard(playerTwoCard, bestCard);

        //decide the round winner
        roundWinner = decideRoundWinner();
        //show the winner and the button to proceed to the next round
        gameTable.getNextRoundContainer().setVisible(true);
        //display the winner
        gameTable.getWinnerText().setText(roundWinner + " wins round " + totalTurns + "!");
    }

    public void playerOneMoveLast(card cardClicked, HBox playerCardsContainer, HBox middleSpace) {
        int indexToRemove = 0;
        int counter = 0;
        for (Node playerCard : playerCardsContainer.getChildren()) {
            ImageView currentCardView = (ImageView) playerCard;

            if (currentCardView.getId().equals(cardClicked.toString())) {
                indexToRemove = counter;
            }
            counter++;
        }
        playerCardsContainer.getChildren().remove(indexToRemove);
        viewController.putCardInMiddle(cardClicked, middleSpace);

        //remove the card that the user has played
        playerOneCardsInPlay.remove(cardClicked);

        bestCard = moveController.getBetterCard(cardClicked, bestCard);

        //System.out.println("BEST CARD 2 : " + bestCard);
        //decide the round winner
        roundWinner = decideRoundWinner();
        //show the winner and the button to proceed to the next round
        gameTable.getNextRoundContainer().setVisible(true);
        //display the winner
        gameTable.getWinnerText().setText(roundWinner + " wins round " + totalTurns + "!");
    }

    public void proceedToNextRound(HBox middleSpace) {
        if (totalTurns < 13) {

            gameTable.getNextRoundContainer().setVisible(false);
            gameTable.emptyMiddleDeck();

            //if player 2 won the round
            if (roundWinner.equals("P2")) {
                playerTwoMoveFirst(middleSpace);
            }
            //if player 3 won the round
            if (roundWinner.equals("P3")) {
                playerThreeMoveFirst(middleSpace);
            }
            //if player 4 won the round
            if (roundWinner.equals("P4")) {
                playerFourMoveFirst(middleSpace);
            }
        }
    }

    public void playerTwoMoveFirst(HBox middleSpace) {
        //player two goes first
        card playerTwoCard = moveController.sendFirstCard(playerTwoCardsInPlay);
        playerTwoCardsInPlay.remove(playerTwoCard);
        viewController.putCardInMiddle(playerTwoCard, middleSpace);
        //assign the best card to the card played by player two
        bestCard = playerTwoCard;

        //player three goes next
        card playerThreeCard = moveController.bestCardToThrow(playerThreeCardsInPlay, bestCard);
        playerThreeCardsInPlay.remove(playerThreeCard);
        viewController.putCardInMiddle(playerThreeCard, middleSpace);
        bestCard = moveController.getBetterCard(playerThreeCard, bestCard);

        card playerFourCard = moveController.bestCardToThrow(playerFourCardsInPlay, bestCard);
        playerFourCardsInPlay.remove(playerFourCard);
        viewController.putCardInMiddle(playerFourCard, middleSpace);

        bestCard = moveController.getBetterCard(playerFourCard, bestCard);

    }

    public void playerThreeMoveFirst(HBox middleSpace) {
        //player three goes first
        card playerThreeCard = moveController.bestCardToThrow(playerThreeCardsInPlay, bestCard);
        playerThreeCardsInPlay.remove(playerThreeCard);
        viewController.putCardInMiddle(playerThreeCard, middleSpace);
        bestCard = playerThreeCard;
        //player four goes next
        card playerFourCard = moveController.bestCardToThrow(playerFourCardsInPlay, bestCard);
        playerFourCardsInPlay.remove(playerFourCard);
        viewController.putCardInMiddle(playerFourCard, middleSpace);

        bestCard = moveController.getBetterCard(playerFourCard, bestCard);

    }

    public void playerFourMoveFirst(HBox middleSpace) {
        //player four goes first
        card playerFourCard = moveController.bestCardToThrow(playerFourCardsInPlay, bestCard);
        playerFourCardsInPlay.remove(playerFourCard);
        viewController.putCardInMiddle(playerFourCard, middleSpace);
        bestCard = playerFourCard;

    }

    public String decideRoundWinner() {
        if (playerOneCards.indexOf(bestCard) != -1) {
            scores[0] = scores[0] + 1;
            return "P1";
        }
        if (playerTwoCards.indexOf(bestCard) != -1) {
            scores[1] = scores[1] + 1;
            return "P2";
        }
        if (playerThreeCards.indexOf(bestCard) != -1) {
            scores[2] = scores[2] + 1;
            return "P3";
        }
        scores[3] = scores[3] + 1;
        return "P4";
    }

    public void resetGame() {
        totalTurns = 0;

        empty(deck);

        empty(playerOneCards);
        empty(playerOneCardsInPlay);

        empty(playerTwoCards);
        empty(playerTwoCardsInPlay);

        empty(playerThreeCards);
        empty(playerThreeCardsInPlay);

        empty(playerFourCards);
        empty(playerFourCardsInPlay);

        gameTable.emptyMiddleDeck();

        for (int score : scores) {
            score = 0;
        }

        gameTable.layoutGUI();
    }

    public void empty(ArrayList list) {
        list.removeAll(list);
    }

}
