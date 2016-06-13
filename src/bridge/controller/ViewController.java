/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bridge.controller;

import bridge.data.card;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 *
 * @author varungoel
 */
public class ViewController {

    public void putCardInMiddle(card cardClicked, HBox middleSpace) throws InterruptedException {
        if (middleSpace.getChildren().size() != 4) {
            insertCardImage(cardClicked, middleSpace);
        } 
        //remove all the old cards if we already have 4 cards
        else {
            middleSpace.getChildren().removeAll(middleSpace.getChildren());
            insertCardImage(cardClicked, middleSpace);
        }
    }

    public void insertCardImage(card cardClicked, HBox middleSpace) throws InterruptedException {
        Image wow = new Image("./pictures/" + cardClicked.toString() + ".png");
        ImageView view = new ImageView(wow);
        view.setFitWidth(100);
        view.setFitHeight(150);
        middleSpace.getChildren().add(view);
        HBox.setMargin(view, new Insets(125, 0, 0, 90));
        Thread.sleep(150);
    }

    public void resetMiddleCards(HBox middleSpace) {
        if (middleSpace.getChildren().size() == 4) {
            for (Node view : middleSpace.getChildren()) {
                ((ImageView) view).setVisible(false);
            }
        }
    }
}
