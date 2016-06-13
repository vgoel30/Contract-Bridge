/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridge.controller;

import bridge.data.card;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author varungoel
 */
public class SetUpController {

    /**
     * Generates the deck of playing cards (shuffled)
     *
     * @return
     */
    public ArrayList<card> generateDeck() {
        //Create a list for the suits 
        char[] suits = {'D', 'S', 'C', 'H'};
        //Create a list for the ranks
        ArrayList<String> ranks = new ArrayList<>(Arrays.asList("2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"));

        ArrayList<card> cards = new ArrayList<>();

        int j, i;

        char suit;
        String rank;
        card playingCard;

        for (j = 0; j < 4; j++) {
            for (i = 0; i < 13; i++) {
                //get the suit and rank
                suit = suits[j];
                rank = ranks.get(i);
                //get the playing card
                playingCard = new card(suit, rank);
                cards.add(playingCard);
            }
        }
        cards = shuffle(cards);
        return cards;
    }

    /**
     * Shuffles a deck of cards
     *
     * @param deck is the deck of cards
     * @return the shuffled deck
     */
    public ArrayList<card> shuffle(ArrayList<card> deck) {
        int len = deck.size();
        card temp;
        for (int i = 0; i < len; i++) {
            int randIndex = (int) (len * Math.random());
            temp = deck.get(i);
            deck.set(i, deck.get(randIndex));
            deck.set(randIndex, temp);
        }
        return deck;
    }

    /**
     * Adds 13 cards from the deck to a user's list of cards
     *
     * @param playerList is the user's list of cards
     * @param source is the deck of cards
     * @param start is the starting index of cards to distribute
     * @param end is the ending index of cards to distribute
     */
    public void addCards(ArrayList<card> playerList, ArrayList<card> source, int start, int end) {
        playerList.removeAll(playerList);
        for (int i = start; i < end; i++) {
            playerList.add(source.get(i));
        }
    }
    
    /**
     * Distributes the shuffled deck to the 4 players
     * @param deck is the shuffled deck 
     * @param playerOne is the player one's cards
     * @param playerTwo is the player two's cards
     * @param playerThree is the player three's cards
     * @param playerFour is the player four's cards
     */
    public void distributeCards(ArrayList<card> deck, ArrayList<card> playerOne, ArrayList<card> playerTwo, ArrayList<card> playerThree,ArrayList<card> playerFour){
        //Distribute the shuffled deck to each player
	addCards(playerOne, deck, 0,13); 
        addCards(playerTwo, deck, 13,26);
	addCards(playerThree, deck, 26,39); 
        addCards(playerFour, deck, 39,52);
    }
    
}
