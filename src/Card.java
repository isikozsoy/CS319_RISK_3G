enum cardType {
    INFANTRY, CAVALRY, CANNON, JOKER
}

public class Card {
    cardType type;

    public Card(cardType type) {
        this.type = type;
    }

    public cardType getCardType() {
        return type;
    }
}
