import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RiskGame {
    private final int TER_COUNT = 42;
    private List<Player> players;
    private List<Territory> territories;
    private int curPlayerId;
    private int gamePhase;
    private int playerCount;
    private Cards cards;
    private boolean isGameOver;
    // RPS
    // Continent List

 public RiskGame(ArrayList<Player> players, ArrayList<Territory> territories) {
     this.players = players;
     this.territories = territories;
     curPlayerId = 0;
     gamePhase = 0;
     playerCount = players.size();
     cards = null;   // for now
     // isGameOver = false;
     // Contients
     // RPS
 }

    public Player play() {

        startTerAlloc();
        while (!isGameOver) {
            Player curPlayer = players.get(curPlayerId);
            startSoldierAlloc(curPlayer);
            startAttack(curPlayer);
            startFortify(curPlayer);
            curPlayerId = (curPlayerId + 1) % playerCount;
        }
        return null;
    }

    public void startTerAlloc() {
        Territory tempTer = new Territory("Turkiyeeem", null);
        int tempTerCount = TER_COUNT;
        while(tempTerCount > 0) {
            Player curPlayer = players.get(curPlayerId);
            if(tempTer.getOwnerId() == -1) {
                tempTer.setOwnerId(curPlayerId);
                curPlayer.decreaseTroop(1);
                curPlayer.setTerCount(curPlayer.getTerCount() + 1);
                tempTerCount = tempTerCount - 1;
                curPlayerId = (curPlayerId + 1) % playerCount;
            }
        }
    }

    public void startSoldierAlloc(Player player) {
     int noOfTroops = player.getTroopCount();
     while(noOfTroops > 0) {
         Territory territoryTemp = new Territory("deneme", null);
         int noOfTroopsToBeAllocatedTemp = 0;
         //number of troops to be allocated is got from the player with a listener
         //                                                                  //
         ///////////////////////////////////////////////////////////////////////

         //clicked territory got from the player with a listener
         //                                                   //
         ///////////////////////////////////////////////////////

         if( territoryTemp.getOwnerId() == player.getId()) {
            territoryTemp.setTroopCount(noOfTroopsToBeAllocatedTemp);
            player.decreaseTroop(noOfTroopsToBeAllocatedTemp);
            noOfTroops = noOfTroops - noOfTroopsToBeAllocatedTemp;
         }
     }
     player.setTroopCount(0);
    }

    public void startAttack(Player player) {
     //////////////////////////////////
     ///         TO DO              ///
     //////////////////////////////////
    }

    public void startFortify(Player player) {
     Territory tempSource = null;
     Territory tempTarget = null;
     int troopToTransfer = 5;
     if(tempSource.getOwnerId() == curPlayerId && tempTarget.getOwnerId() == curPlayerId) {
         tempSource.setTroopCount(tempSource.getTroopCount() - troopToTransfer);
         tempTarget.setTroopCount(tempTarget.getTroopCount() + troopToTransfer);
     }
    }

    public void update() {
        //////////////////////////////////
        ///         TO DO              ///
        //////////////////////////////////

    }
}