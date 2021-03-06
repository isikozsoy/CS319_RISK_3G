import java.util.HashMap;
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
    private HashMap<Integer, String> allianceReq;
    private int troopCount;

    // Constructor
    Player(String name, int id, String targetCont, String color) {
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
    }

    public void setPlayerCount(int playerCount) {
        if (playerCount == 2) {
            troopCount = 40;
        }
        else if(playerCount == 3)
            troopCount = 35;
        else if(playerCount == 4)
            troopCount = 30;
        else if(playerCount == 5)
            troopCount = 25;
        else if(playerCount == 6)
            troopCount = 20;

        allies = new boolean[playerCount];
        allianceReq = new HashMap<Integer, String>();
        for (int i = 0; i < playerCount; i++)
        {
            allies[i] = false; // We will ignore his own id
        }
    }

    public void makePlayerNonExistent() {
        color = "gray";
        id = -1;
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

    public HashMap<Integer, String> getAllianceReq() {
        return allianceReq;
    }

    public void setAllianceReq(HashMap<Integer, String> allianceReq) {
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

    public void setId( int id) {
        this.id = id;
    }

    // Checks whether the given player is an ally or not using the given player object.
    public boolean isAlly(Player player) {
        return allies[player.id];
    }
    /**
     // Checks whether a player is an ally or not using the given player ID.
     public boolean isAlly(int playerId) {
     return allies[playerId];
     }
     **/
    // Makes the given player an ally.
    public void addAlly(int playerId) {
        allies[playerId] = true;
    }

    // Removes the given player from allies.
    public void removeAlly(int playerId) {
        allies[playerId] = false;
    }

    // Adds the alliance request.
    public void addAllianceReq(int playerId, String playerName) {
        allianceReq.put(playerId, playerName);
    }

    // Calculates the troop count that player earns.
    public void updateTroopCount() {
        troopCount = (terCount / 3) + (contCount * 3);
        if(isTargetTaken)
            troopCount += 5;
    }

    public void decreaseTroop( int usedTroopCount) {
        troopCount -= usedTroopCount;
    }

    @Override
    public String toString() {
        return "Player{" +
                "color='" + color + '\'' +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }

    public boolean exchangeInfantry() {
        if (cards[0] >= 3)  { //exchanging three infantry cards
            cards[0] = cards[0] - 3;
            troopCount = troopCount + 3; //exchanged infantry
            return true;
        }
        else if (cards[0] == 2 && cards[3] >= 1) { //exchanging two infantry and one joker
            cards[0] = 0;
            cards[3] = cards[3] - 1;
            troopCount = troopCount + 3; //exchanged infantry
            return true;
        }
        else if (cards[0] == 1 && cards[3] >= 2) { //exchanging one infantry and two joker
            cards[0] = 0;
            cards[3] = cards[3] - 2;
            troopCount = troopCount + 3; //exchanged infantry
            return true;
        }
        else if (cards[3] >= 3) { //exchanging three joker
            cards[3] = cards[3] - 3;
            troopCount = troopCount + 3; //exchanged infantry
            return true;
        }
        return false; //cannot exchange infantry
    }

    public boolean exchangeCavalry() {
        if (cards[1] >= 3)  { //exchanging three cavalry cards
            cards[1] = cards[1] - 3;
            troopCount = troopCount + 4; //exchanged cavalry
            return true;
        }
        else if (cards[1] == 2 && cards[3] >= 1) { //exchanging two cavalry and one joker
            cards[1] = 0;
            cards[3] = cards[3] - 1;
            troopCount = troopCount + 4; //exchanged cavalry
            return true;
        }
        else if (cards[1] == 1 && cards[3] >= 2) { //exchanging one cavalry and two joker
            cards[1] = 0;
            cards[3] = cards[3] - 2;
            troopCount = troopCount + 4; //exchanged cavalry
            return true;
        }
        else if (cards[3] >= 3) { //exchanging three joker
            cards[3] = cards[3] - 3;
            troopCount = troopCount + 4; //exchanged cavalry
            return true;
        }
        return false; //cannot exchange cavalry
    }

    public boolean exchangeCannon() {
        if (cards[2] >= 3)  { //exchanging three cannon
            cards[2] = cards[2] - 3;
            troopCount = troopCount + 5; //exchanged cannon
            return true;
        }
        else if (cards[2] == 2 && cards[3] >= 1) { //exchanging two cannon and one joker
            cards[2] = 0;
            cards[3] = cards[3] - 1;
            troopCount = troopCount + 5; //exchanged cannon
            return true;
        }
        else if (cards[2] == 1 && cards[3] >= 2) { //exchanging one cannon and two joker
            cards[2] = 0;
            cards[3] = cards[3] - 2;
            troopCount = troopCount + 5; //exchanged cannon
            return true;
        }
        else if (cards[3] >= 3) { //exchanging three joker
            cards[3] = cards[3] - 3;
            troopCount = troopCount + 5; //exchanged cannon
            return true;
        }
        return false; //cannot exchange cannon
    }

    public boolean exchangeMixed() {
        if (cards[0] >= 1 && cards[1] >= 1 && cards[2] >= 1) { //exchanging one of each
            cards[0] = cards[0] - 1;
            cards[1] = cards[1] - 1;
            cards[2] = cards[2] - 1;
            troopCount = troopCount + 6; //exchanged mix
            return true;
        }
        else if (cards[0] >= 1 && cards[1] >= 1 && cards[3] >= 1) { //no cannon and one joker
            cards[0] = cards[0] - 1;
            cards[1] = cards[1] - 1;
            cards[3] = cards[3] - 1;
            troopCount = troopCount + 6; //exchanged mix
            return true;
        }
        else if (cards[0] >= 1 && cards[2] >= 1 && cards[3] >= 1) { //no cavalry and one joker
            cards[0] = cards[0] - 1;
            cards[2] = cards[2] - 1;
            cards[3] = cards[3] - 1;
            troopCount = troopCount + 6; //exchanged mix
            return true;
        }
        else if (cards[1] >= 1 && cards[2] >= 1 && cards[3] >= 1) { //no infantry and one joker
            cards[1] = cards[1] - 1;
            cards[2] = cards[2] - 1;
            cards[3] = cards[3] - 1;
            troopCount = troopCount + 6; //exchanged mix
            return true;
        }
        else if (cards[0] >= 1 && cards[3] >= 2) { //only infantry and two joker
            cards[0] = cards[0] - 1;
            cards[3] = cards[3] - 2;
            troopCount = troopCount + 6; //exchanged mix
            return true;
        }
        else if (cards[1] >= 1 && cards[3] >= 2) { //only cavalry and two joker
            cards[1] = cards[1] - 1;
            cards[3] = cards[3] - 2;
            troopCount = troopCount + 6; //exchanged mix
            return true;
        }
        else if (cards[2] >= 1 && cards[3] >= 2) { //only cannon and two joker
            cards[2] = cards[2] - 1;
            cards[3] = cards[3] - 2;
            troopCount = troopCount + 6; //exchanged mix
            return true;
        }
        else if (cards[3] >= 3) { //exchanging three joker
            cards[3] = cards[3] - 3;
            troopCount = troopCount + 6; //exchanged mixed
            return true;
        }
        return false; //cannot exchange mixed
    }
}