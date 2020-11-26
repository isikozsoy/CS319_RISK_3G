import java.util.ArrayList;
import java.util.Collections;

public class Cards {
    ArrayList<Card> cards;

    //creates a set of cards with the specified number of infantry, cavalry, cannon and joker cards
    public Cards() {
        //create the arraylist
        cards = new ArrayList<Card>();

        //add appropriate number of infantry cards
        for (int i = 0; i < 15; i++) {
            Card infantry = new Card(cardType.INFANTRY);
            cards.add(infantry);
        }

        //add appropriate number of cavalry cards
        for (int i = 0; i < 15; i++) {
            Card cavalry = new Card(cardType.CAVALRY);
            cards.add(cavalry);
        }

        //add appropriate number of cannon cards
        for (int i = 0; i < 15; i++) {
            Card cannon = new Card(cardType.CANNON);
            cards.add(cannon);
        }

        //add appropriate number of joker cards
        for (int i = 0; i < 10; i++) {
            Card joker = new Card(cardType.JOKER);
            cards.add(joker);
        }

        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        Card card = cards.get(0);
        cards.remove(0);
        return card;
    }

    public void addCard(cardType type) {
        Card toBeAdded = new Card(type);
        cards.add(toBeAdded);
        shuffle();
    }
}