/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridge.controller;

import static bridge.controller.GameController.scores;
import bridge.data.card;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.scene.control.Alert;

/**
 *
 * @author varungoel
 */
public class MoveController {

    ArrayList<String> ranks;

    /**
     * Method to check if a card of the same suit exists in a given hand
     *
     * @param cards is the hand of cards
     * @param cardToCompareTo is the current card which we want to compare
     * against
     * @return
     */
    public boolean hasSameSuit(ArrayList<card> cards, card cardToCompareTo) {
        for (card Card : cards) {
            if (Card.getSuit() == cardToCompareTo.getSuit()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Decides the best card to throw from a hand of cards given the card to
     * compare to
     *
     * @param cards
     * @param cardToCompareTo
     * @return
     */
    public card bestCardToThrow(ArrayList<card> cards, card cardToCompareTo) {
        ranks = new ArrayList<>(Arrays.asList("2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"));

        //set the current max to the thrown card
        card maxCard = cardToCompareTo;
        //smallest card
        card minCard = cardToCompareTo;

        //check to see if card of same suit exists
        boolean sameSuitExists = hasSameSuit(cards, cardToCompareTo);

        //if we have a card of the same suit
        if (sameSuitExists) {
            String maxRank = cardToCompareTo.getRank();
            String minRank = cardToCompareTo.getRank();
            for (card Card : cards) {
                //if it's the same suit
                if (Card.getSuit() == cardToCompareTo.getSuit()) {
                    //if the rank is higher than the max rank
                    if (ranks.indexOf(Card.getRank()) > ranks.indexOf(maxRank)) {
                        maxCard = Card;
                        maxRank = Card.getRank();
                    }
                    //if the rank is lower than the lowest rank
                    if (ranks.indexOf(Card.getRank()) < ranks.indexOf(minRank)) {
                        minCard = Card;
                        minRank = Card.getRank();
                    }
                }
            }
            //if a card of higher rank exists
            if (!maxCard.getRank().equals(cardToCompareTo.getRank())) {
                return maxCard;
            } //else, return the card of the same suit with the lowest rank
            else {
                return minCard;
            }
            //return toReturn;
        } else {
            //set the min card to the first card in hand
            minCard = cards.get(0);
            //get it's rank
            String minRank = minCard.getRank();
            for (card Card : cards) {
                //if the rank is lower than the lowest rank
                if (ranks.indexOf(Card.getRank()) < ranks.indexOf(minRank)) {
                    minCard = Card;
                    minRank = Card.getRank();
                }

            }
            return minCard;
        }
    }

    /**
     * Method that decides if the current card is better than the max card
     *
     * @param currentCard
     * @param max
     * @return
     */
    public boolean isBetter(card currentCard, card max) {
        ranks = new ArrayList<>(Arrays.asList("2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"));
        return currentCard.getSuit() == max.getSuit() && ranks.indexOf(currentCard.getRank()) > ranks.indexOf(max.getRank());
    }

    public boolean moveIsLegit(card cardToThrow, ArrayList<card> cardsInHand, card maxCard) {
        if (hasSameSuit(cardsInHand, maxCard)) {
            if (cardToThrow.getSuit() == maxCard.getSuit()) {
                return true;
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Move");
                alert.setContentText("Card from the suit must be thrown!");
                alert.showAndWait();
                return false;
            }
        }
        return true;
    }

    /**
     * Get the better out of two cards
     *
     * @param cardOne
     * @param cardTwo
     * @return
     */
    public card getBetterCard(card cardOne, card cardTwo) {
        if (isBetter(cardOne, cardTwo)) {
            return cardOne;
        }
        return cardTwo;
    }

    /**
     * Sends the card for the first turn
     *
     * @param cards
     * @return
     */
    public card sendFirstCard(ArrayList<card> cards) {
        ranks = new ArrayList<>(Arrays.asList("2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"));

        //int maxCardIndex = 0;
        card maxCard = cards.get(0);

        for (card Card : cards) {
            if (ranks.indexOf(Card.getRank()) > ranks.indexOf(maxCard.getRank())) {
                maxCard = Card;
            }
        }
        return maxCard;
    }

}
