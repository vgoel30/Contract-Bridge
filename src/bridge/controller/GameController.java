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

    SetUpController setUpController;
    ViewController viewController;
    //playing card deck
    static ArrayList<card> deck;

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
     * @param viewController
     * @return
     * @throws java.lang.InterruptedException
     */
    public void handleCardClick(card cardClicked, HBox playerCardsContainer, HBox middleSpace, ViewController viewController){
        MoveController moveController = new MoveController();

        //the first turn
        if (totalTurns++ == 0) {
            System.out.println("HERE");
            playerOneMove(cardClicked, playerCardsContainer, middleSpace, viewController, moveController);
        } else {

            if (totalTurns == 12) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Winner: " + scores[0] + " " + scores[1] + " " + scores[2] + " " + scores[3]);

                alert.showAndWait();
            }
        }
    }

    public void playerOneMove(card cardClicked, HBox playerCardsContainer, HBox middleSpace, ViewController viewController, MoveController moveController){
        int indexToRemove = 0;
        int counter = 0;
        //the best card in the round
        card bestCard;
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

        card playerTwoCard = moveController.bestCardToThrow(playerTwoCardsInPlay, cardClicked);
        playerTwoCardsInPlay.remove(playerTwoCard);
        viewController.putCardInMiddle(playerTwoCard, middleSpace);

        bestCard = moveController.getBetterCard(cardClicked, playerTwoCard);

        card playerThreeCard = moveController.bestCardToThrow(playerThreeCardsInPlay, bestCard);
        playerThreeCardsInPlay.remove(playerThreeCard);
        viewController.putCardInMiddle(playerThreeCard, middleSpace);

        bestCard = moveController.getBetterCard(bestCard, playerThreeCard);

        card playerFourCard = moveController.bestCardToThrow(playerFourCardsInPlay, bestCard);
        playerFourCardsInPlay.remove(playerFourCard);
        viewController.putCardInMiddle(playerFourCard, middleSpace);

        bestCard = moveController.getBetterCard(bestCard, playerFourCard);

        //decide the round winner
        roundWinner = decideRoundWinner(bestCard, scores);
        gameTable.getNextRoundContainer().setVisible(true);
        gameTable.getWinnerText().setText(roundWinner + " wins the round!");
    }
    
    public void proceedToNextRound(){
        gameTable.getNextRoundContainer().setVisible(false);
    }

//    public void playerTwoMove(card cardClicked, HBox playerCardsContainer, HBox middleSpace, ViewController viewController, MoveController moveController) throws InterruptedException {
//        int indexToRemove = 0;
//        int counter = 0;
//        card bestCard;
//
//        //player two goes first
//        card playerTwoCard = moveController.sendFirstCard(playerTwoCardsInPlay);
//        playerTwoCardsInPlay.remove(playerTwoCard);
//        viewController.putCardInMiddle(playerTwoCard, middleSpace);
//        //assign the best card to the card played by player two
//        bestCard = playerTwoCard;
//
//        //player three goes next
//        card playerThreeCard = moveController.bestCardToThrow(playerThreeCardsInPlay, bestCard);
//        playerThreeCardsInPlay.remove(playerThreeCard);
//        viewController.putCardInMiddle(playerThreeCard, middleSpace);
//
//        bestCard = moveController.getBetterCard(bestCard, playerThreeCard);
//
//        card playerFourCard = moveController.bestCardToThrow(playerFourCardsInPlay, bestCard);
//        playerFourCardsInPlay.remove(playerFourCard);
//        viewController.putCardInMiddle(playerFourCard, middleSpace);
//
//        bestCard = moveController.getBetterCard(bestCard, playerFourCard);
//
//        for (Node playerCard : playerCardsContainer.getChildren()) {
//            ImageView currentCardView = (ImageView) playerCard;
//
//            if (currentCardView.getId().equals(cardClicked.toString())) {
//                indexToRemove = counter;
//            }
//            counter++;
//        }
//        playerCardsContainer.getChildren().remove(indexToRemove);
//        viewController.putCardInMiddle(cardClicked, middleSpace);
//
//        //remove the card that the user has played
//        playerOneCardsInPlay.remove(cardClicked);
//
//        bestCard = moveController.getBetterCard(bestCard, cardClicked);
//
//        //decide the round winner
//        roundWinner = decideRoundWinner(bestCard, scores);
//    }
//
//    public void playerThreeMove(card cardClicked, HBox playerCardsContainer, HBox middleSpace, ViewController viewController, MoveController moveController) throws InterruptedException {
//        int indexToRemove = 0;
//        int counter = 0;
//        card bestCard;
//
//        //player three goes first
//        card playerThreeCard = moveController.sendFirstCard(playerThreeCardsInPlay);
//        playerThreeCardsInPlay.remove(playerThreeCard);
//        viewController.putCardInMiddle(playerThreeCard, middleSpace);
//        //assign the best card to the card played by player two
//        bestCard = playerThreeCard;
//
//        //player 4 is next
//        card playerFourCard = moveController.bestCardToThrow(playerFourCardsInPlay, bestCard);
//        playerFourCardsInPlay.remove(playerFourCard);
//        viewController.putCardInMiddle(playerFourCard, middleSpace);
//
//        bestCard = moveController.getBetterCard(bestCard, playerFourCard);
//
//        //player 2 goes next
//        card playerTwoCard = moveController.bestCardToThrow(playerTwoCardsInPlay, bestCard);
//        playerTwoCardsInPlay.remove(playerTwoCard);
//        viewController.putCardInMiddle(playerTwoCard, middleSpace);
//
//        bestCard = moveController.getBetterCard(bestCard, playerTwoCard);
//
//        for (Node playerCard : playerCardsContainer.getChildren()) {
//            ImageView currentCardView = (ImageView) playerCard;
//
//            if (currentCardView.getId().equals(cardClicked.toString())) {
//                indexToRemove = counter;
//            }
//            counter++;
//        }
//        playerCardsContainer.getChildren().remove(indexToRemove);
//        viewController.putCardInMiddle(cardClicked, middleSpace);
//
//        //remove the card that the user has played
//        playerOneCardsInPlay.remove(cardClicked);
//
//        bestCard = moveController.getBetterCard(bestCard, cardClicked);
//
//        //decide the round winner
//        roundWinner = decideRoundWinner(bestCard, scores);
//    }
//
//    public void playerFourMove(card cardClicked, HBox playerCardsContainer, HBox middleSpace, ViewController viewController, MoveController moveController) throws InterruptedException {
//        int indexToRemove = 0;
//        int counter = 0;
//        card bestCard;
//
//        //player four goes first
//        card playerFourCard = moveController.sendFirstCard(playerFourCardsInPlay);
//        playerFourCardsInPlay.remove(playerFourCard);
//        viewController.putCardInMiddle(playerFourCard, middleSpace);
//        //assign the best card to the card played by player two
//        bestCard = playerFourCard;
//
//        //the player goes next
//        for (Node playerCard : playerCardsContainer.getChildren()) {
//            ImageView currentCardView = (ImageView) playerCard;
//
//            if (currentCardView.getId().equals(cardClicked.toString())) {
//                indexToRemove = counter;
//            }
//            counter++;
//        }
//        playerCardsContainer.getChildren().remove(indexToRemove);
//        viewController.putCardInMiddle(cardClicked, middleSpace);
//
//        //remove the card that the user has played
//        playerOneCardsInPlay.remove(cardClicked);
//
//        bestCard = moveController.getBetterCard(bestCard, cardClicked);
//
//        //player 2 goes next
//        card playerTwoCard = moveController.bestCardToThrow(playerTwoCardsInPlay, bestCard);
//        playerTwoCardsInPlay.remove(playerTwoCard);
//        viewController.putCardInMiddle(playerTwoCard, middleSpace);
//
//        bestCard = moveController.getBetterCard(bestCard, playerTwoCard);
//
//        //player 3 is next
//        card playerThreeCard = moveController.bestCardToThrow(playerThreeCardsInPlay, bestCard);
//        playerThreeCardsInPlay.remove(playerThreeCard);
//        viewController.putCardInMiddle(playerThreeCard, middleSpace);
//
//        bestCard = moveController.getBetterCard(bestCard, playerThreeCard);
//
//        //decide the round winner
//        roundWinner = decideRoundWinner(bestCard, scores);
//    }

    public String decideRoundWinner(card bestCard, int[] scores) {
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

        for (int score : scores) {
            score = 0;
        }

        getTableReady();
    }

    public void empty(ArrayList list) {
        list.removeAll(list);
    }

}
