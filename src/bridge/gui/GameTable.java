/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridge.gui;

import bridge.controller.GameController;
import bridge.controller.SetUpController;
import bridge.controller.ViewController;
import bridge.data.card;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author varungoel
 */
public class GameTable {

    GameController gameController;
    ViewController viewController;
    SetUpController setUpController;

    BorderPane tableDeck;

    VBox leftSpace;
    VBox rightSpace;
    
    HBox topSpace;
    
    VBox nextRoundContainer;
    Text winnerText;
    Button nextRoundButton;
    
    HBox bottomSpace;
    HBox middleSpace;

    ImageView firstCard;
    ImageView secondCard;
    ImageView thirdCard;
    ImageView fourthCard;
    ImageView fifthCard;
    ImageView sixthCard;
    ImageView seventhCard;
    ImageView eighthCard;
    ImageView ninthCard;
    ImageView tenthCard;
    ImageView eleventhCard;
    ImageView twelfthCard;
    ImageView thirteenthCard;

    ImageView firstPlayerCard;
    ImageView secondPlayerCard;
    ImageView thirdPlayerCard;
    ImageView fourthPlayerCard;
    //all the image views in the middle panel
    ImageView firstPlayedCard;
    ImageView secondPlayedCard;
    ImageView thirdPlayedCard;
    ImageView fourthPlayedCard;

    public void layoutGUI() {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        tableDeck = new BorderPane();

        gameController = new GameController();
        setUpController = new SetUpController();
        viewController = new ViewController();

        leftSpace = new VBox();
        leftSpace.setMinSize(250, 250);
        tableDeck.setLeft(leftSpace);

        Image rearCard = new Image("./pictures/card_rear.png");
        ImageView leftView = new ImageView(rearCard);
        leftView.setFitHeight(200);
        leftView.setFitWidth(175);
        leftView.setRotate(90);
        leftSpace.getChildren().add(leftView);
        VBox.setMargin(leftView, new Insets(70, 0, 0, 35));

        topSpace = new HBox();
        topSpace.setMinHeight(180);
        topSpace.setMaxHeight(180);
        tableDeck.setTop(topSpace);
        
        winnerText = new Text("WINNER");
        nextRoundButton = new Button("Next Round");
        
        nextRoundContainer = new VBox(winnerText,nextRoundButton);
        VBox.setMargin(winnerText, new Insets(70, 0, 0, 50));
        VBox.setMargin(nextRoundButton, new Insets(20, 0, 0, 50));
        
        topSpace.getChildren().add(nextRoundContainer);
        nextRoundContainer.setVisible(false);
        
        nextRoundButton.setOnAction(e-> {
            gameController.proceedToNextRound();
        });

        ImageView topView = new ImageView(rearCard);
        topView.setFitHeight(170);
        topView.setFitWidth(148);
        topView.setRotate(180);
        topSpace.getChildren().add(topView);
        HBox.setMargin(topView, new Insets(10, 50, 0, screenBounds.getWidth() / 2.8));

        rightSpace = new VBox();
        rightSpace.setMinSize(250, 250);
        tableDeck.setRight(rightSpace);
        ImageView rightView = new ImageView(rearCard);
        rightView.setFitHeight(200);
        rightView.setFitWidth(175);
        rightView.setRotate(270);
        rightSpace.getChildren().add(rightView);
        
        VBox.setMargin(rightView, new Insets(70, 0, 0, 35));
        
        bottomSpace = new HBox();
        bottomSpace.setMinHeight(175);
        bottomSpace.setMaxHeight(175);
        tableDeck.setBottom(bottomSpace);

        middleSpace = new HBox();
        tableDeck.setCenter(middleSpace);
        
        
        initStyle();
    }
    
    public void initStyle(){
        middleSpace.setStyle("-fx-background-color: #006442");
        leftSpace.setStyle("-fx-background-color: #26A65B");
        rightSpace.setStyle("-fx-background-color: #26A65B");
        topSpace.setStyle("-fx-background-color: #26A65B");
        bottomSpace.setStyle("-fx-background-color: #26A65B");
        tableDeck.setStyle("-fx-background-color: #26A65B");
        
        winnerText.setStyle("-fx-font-size: 22px");
    }

    /**
     * Layouts the user's cards and arranges them on the game table
     *
     * @param playerCards is the list of cards that the user has
     */
    public void layoutCards(ArrayList<card> playerCards) {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        char suit;
        String rank;

        String cardImageName;
        Image cardImage;
        ImageView cardImageView;

        //int cardNumber = 0;
        for (card playerCard : playerCards) {
            suit = playerCard.getSuit();
            rank = playerCard.getRank();
            //the card's image name
            cardImageName = "./pictures/" + suit + rank + ".png";
            cardImage = new Image(cardImageName);
            cardImageView = new ImageView(cardImage);
            //set the dimensions of the image view
            cardImageView.setFitWidth(screenBounds.getWidth() / 16.5);
            cardImageView.setFitHeight(cardImageView.getFitWidth() * 1.5);
            //put the card in the bottom space
            bottomSpace.getChildren().add(cardImageView);
            HBox.setMargin(cardImageView, new Insets(20, 5, -10, 15));
            //set the ID of the image view for the event handler for card selection
            String imageViewID = "" + playerCard.toString();
            //set the ID of the image view
            cardImageView.setId(imageViewID);

            //attach event handler
            cardImageView.setOnMouseClicked(e -> {
                    gameController.handleCardClick(playerCard, bottomSpace, middleSpace, viewController);
                
            });
        }

    }

    public BorderPane getTableDeck() {
        return tableDeck;
    }

    public VBox getNextRoundContainer() {
        return nextRoundContainer;
    }
    
    public Text getWinnerText(){
        return winnerText;
    }

}
