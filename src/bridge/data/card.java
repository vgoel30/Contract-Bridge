/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridge.data;

/**
 *
 * @author varungoel
 */
public class card {
    
    char suit;
    String rank;

    public char getSuit() {
        return suit;
    }

    public void setSuit(char suit) {
        this.suit = suit;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
    
    public card(char suit, String rank){
        this.suit = suit;
        this.rank = rank;
    }

    @Override
    public String toString() {
        return suit+rank;
    }
    
}
