import java.util.ArrayList;
import java.util.Collections;

public class Cards {
    ArrayList<Card> cards;

    //creates a set of cards with the specified number of infantry, cavalry, cannon and joker cards
    public Cards(int noOfInfantry, int noOfCavalry, int noOfCannon, int noOfJoker) {
        //create the arraylist
        cards = new ArrayList<Card>();

        //add appropriate number of infantry cards
        for (int i = 0; i < noOfInfantry; i++) {
            Card infantry = new Card(cardType.INFANTRY);
            cards.add(infantry);
        }

        //add appropriate number of cavalry cards
        for (int i = 0; i < noOfCavalry; i++) {
            Card cavalry = new Card(cardType.CAVALRY);
            cards.add(cavalry);
        }

        //add appropriate number of cannon cards
        for (int i = 0; i < noOfCannon; i++) {
            Card cannon = new Card(cardType.CANNON);
            cards.add(cannon);
        }

        //add appropriate number of joker cards
        for (int i = 0; i < noOfJoker; i++) {
            Card joker = new Card(cardType.JOKER);
            cards.add(joker);
        }

        shuffle();
    }

    public Cards() {
        new Cards(40, 12, 8, 2);
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
