import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player {
    final Random random=new Random();
    private String color;

    // Properties
    private String name;
    private int id;
    private int terCount;
    private int contCount;
    private String targetCont; // String will become Continent class
    private boolean isTargetTaken;
    private int[] cards;
    private boolean cardDeserved;
    private boolean[] allies;
    private List<Integer> allianceReq;
    private int troopCount;

    // Constructor
    Player(String name, int id, String targetCont, int playerCount, String color) {
        this.color = color;

        this.name = name;
        this.id = id;
        terCount = 0;
        contCount = 0;
        cards = new int[4];  //4 is the number of card types
        for(int i = 0; i < 4; i++)
            cards[i] = 0;
        cardDeserved = false;
        this.targetCont = targetCont;
        isTargetTaken = false;
        troopCount = 30;  // This will be determined aaccording to the number of player.
        allies = new boolean[playerCount];
        allianceReq = new ArrayList<Integer>();
        for (int i = 0; i < playerCount; i++)
        {
            allies[i] = false; // We will ignore his own id
        }
    }

    // Getters & Setters
    public String getColor() {
        return color;
    }

    public void setColor( String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getTerCount() {
        return terCount;
    }

    public void setTerCount(int terCount) {
        this.terCount = terCount;
    }

    public int getContCount() {
        return contCount;
    }

    public void setContCount(int contCount) {
        this.contCount = contCount;
    }

    public String getTargetCont() {
        return targetCont;
    }

    public void setTargetCont(String targetCont) {
        this.targetCont = targetCont;
    }

    public boolean isTargetTaken() {
        return isTargetTaken;
    }

    public void setTargetTaken(boolean targetTaken) {
        isTargetTaken = targetTaken;
    }

    public int[] getCards() {
        return cards;
    }

    public void setCards(int[] cards) {
        this.cards = cards;
    }

    public boolean isCardDeserved() {
        return cardDeserved;
    }

    public void setCardDeserved(boolean cardDeserved) {
        this.cardDeserved = cardDeserved;
    }

    public boolean[] getAllies() {
        return allies;
    }

    public void setAllies(boolean[] allies) {
        this.allies = allies;
    }

    public List<Integer> getAllianceReq() {
        return allianceReq;
    }

    public void setAllianceReq(List<Integer> allianceReq) {
        this.allianceReq = allianceReq;
    }

    public int getTroopCount() {
        return troopCount;
    }

    public void setTroopCount(int troopCount) {
        this.troopCount = troopCount;
    }

    // Functional Methods

    // Adds the selected card to the player's cards.
    public void addCard (Card card) {
        if(card.getCardType() == cardType.INFANTRY)
            cards[0]++;
        else if(card.getCardType() == cardType.CAVALRY)
            cards[1]++;
        else if(card.getCardType() == cardType.CANNON)
            cards[2]++;
        else
            cards[3]++;
    }

    // Checks whether the given player is an ally or not.
    public boolean isAlly(int playerId) {
        return allies[playerId];
    }

    // Makes the given player an ally.
    public void addAlly(int playerId) {
        allies[playerId] = true;
    }

    // Removes the given player from allies.
    public void removeAlly(int playerId) {
        allies[playerId] = false;
    }

    // Adds the alliance request.
    public void addAllianceReq(int playerId) {
        allianceReq.add(playerId);
    }

    // Calculates the troop count that player earns.
    public void updateTroopCount() {
        troopCount = (terCount / 3) + (contCount * 3);
        if(isTargetTaken)
            troopCount += 5;
    }

    public void useCards(int[] cards) {
        for (int i = 0; i < 4; i++)
            this.cards[i] -= cards[i];
    }

    @Override
    public String toString() {
        return "Player{" +
                "color='" + color + '\'' +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}