import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class RiskGame {

    private final int TER_COUNT = 42;
    private List<Player> players;
    private Territory[] territories;
    private int curPlayerId;
    private int gamePhase;
    private int playerCount;
    private Cards cards;
    private boolean isGameOver;
    private RiskView riskView;
    private int tempTerCount = TER_COUNT;
    private HashSet<Territory> attackableTer;
    RockPaperScissorsGame rpsGame;
    private Territory target;
    private Territory source;

    // RPS
    // Continent List
    enum GameMode {
        TroopAllocationMode, SoldierAllocationMode, AttackMode, FortifyMode
    }
    private GameMode mode = GameMode.TroopAllocationMode;

    public RiskGame(ArrayList<Player> players, RiskView riskView) {
         this.players = players;
         territories = new Territory[42];
         for (int i = 0; i < territories.length; i++) {
             territories[i] = new Territory(i);
         }
         this.riskView = riskView;

         curPlayerId = 0;
         gamePhase = 0;
         playerCount = players.size();
         cards = null;   // for now
         isGameOver = false;
         rpsGame = new RockPaperScissorsGame();
        addNeighbors();
        // Continents
         // RPS
    }

    final public void addNeighbors() {

        territories[0].addNeighbor(territories[1]);
        territories[0].addNeighbor(territories[3]);
        territories[0].addNeighbor(territories[37]);

        territories[1].addNeighbor(territories[0]);
        territories[1].addNeighbor(territories[3]);
        territories[1].addNeighbor(territories[4]);
        territories[1].addNeighbor(territories[2]);

        territories[2].addNeighbor(territories[5]);
        territories[2].addNeighbor(territories[1]);
        territories[2].addNeighbor(territories[4]);
        territories[2].addNeighbor(territories[19]);

        territories[3].addNeighbor(territories[0]);
        territories[3].addNeighbor(territories[1]);
        territories[3].addNeighbor(territories[4]);
        territories[3].addNeighbor(territories[6]);

        territories[4].addNeighbor(territories[3]);
        territories[4].addNeighbor(territories[1]);
        territories[4].addNeighbor(territories[7]);
        territories[4].addNeighbor(territories[6]);
        territories[4].addNeighbor(territories[5]);
        territories[4].addNeighbor(territories[2]);

        territories[5].addNeighbor(territories[4]);
        territories[5].addNeighbor(territories[7]);
        territories[5].addNeighbor(territories[2]);

        territories[6].addNeighbor(territories[7]);
        territories[6].addNeighbor(territories[4]);
        territories[6].addNeighbor(territories[8]);
        territories[6].addNeighbor(territories[3]);

        territories[7].addNeighbor(territories[6]);
        territories[7].addNeighbor(territories[4]);
        territories[7].addNeighbor(territories[5]);
        territories[7].addNeighbor(territories[8]);

        territories[8].addNeighbor(territories[6]);
        territories[8].addNeighbor(territories[7]);
        territories[8].addNeighbor(territories[9]);

        territories[9].addNeighbor(territories[11]);
        territories[9].addNeighbor(territories[4]);
        territories[9].addNeighbor(territories[10]);

        territories[10].addNeighbor(territories[9]);
        territories[10].addNeighbor(territories[12]);
        territories[10].addNeighbor(territories[11]);

        territories[11].addNeighbor(territories[9]);
        territories[11].addNeighbor(territories[12]);
        territories[11].addNeighbor(territories[10]);
        territories[11].addNeighbor(territories[13]);

        territories[12].addNeighbor(territories[11]);
        territories[12].addNeighbor(territories[10]);

        territories[13].addNeighbor(territories[16]);
        territories[13].addNeighbor(territories[11]);
        territories[13].addNeighbor(territories[24]);
        territories[13].addNeighbor(territories[14]);
        territories[13].addNeighbor(territories[15]);

        territories[14].addNeighbor(territories[24]);
        territories[14].addNeighbor(territories[41]);
        territories[14].addNeighbor(territories[13]);
        territories[14].addNeighbor(territories[15]);

        territories[15].addNeighbor(territories[16]);
        territories[15].addNeighbor(territories[41]);
        territories[15].addNeighbor(territories[13]);
        territories[15].addNeighbor(territories[15]);

        territories[16].addNeighbor(territories[17]);
        territories[16].addNeighbor(territories[13]);
        territories[16].addNeighbor(territories[15]);

        territories[17].addNeighbor(territories[18]);
        territories[17].addNeighbor(territories[16]);
        territories[17].addNeighbor(territories[15]);

        territories[18].addNeighbor(territories[17]);
        territories[18].addNeighbor(territories[15]);

        territories[19].addNeighbor(territories[2]);
        territories[19].addNeighbor(territories[22]);
        territories[19].addNeighbor(territories[20]);

        territories[20].addNeighbor(territories[19]);
        territories[20].addNeighbor(territories[23]);
        territories[20].addNeighbor(territories[21]);

        territories[21].addNeighbor(territories[23]);
        territories[21].addNeighbor(territories[24]);
        territories[21].addNeighbor(territories[20]);
        territories[21].addNeighbor(territories[41]);
        territories[21].addNeighbor(territories[39]);
        territories[21].addNeighbor(territories[40]);

        territories[22].addNeighbor(territories[23]);
        territories[22].addNeighbor(territories[25]);
        territories[22].addNeighbor(territories[20]);
        territories[22].addNeighbor(territories[19]);

        territories[23].addNeighbor(territories[22]);
        territories[23].addNeighbor(territories[25]);
        territories[23].addNeighbor(territories[20]);
        territories[23].addNeighbor(territories[24]);
        territories[23].addNeighbor(territories[21]);

        territories[24].addNeighbor(territories[13]);
        territories[24].addNeighbor(territories[14]);
        territories[24].addNeighbor(territories[21]);
        territories[24].addNeighbor(territories[23]);
        territories[24].addNeighbor(territories[25]);
        territories[24].addNeighbor(territories[41]);

        territories[25].addNeighbor(territories[22]);
        territories[25].addNeighbor(territories[23]);
        territories[25].addNeighbor(territories[24]);

        territories[26].addNeighbor(territories[30]);
        territories[26].addNeighbor(territories[27]);
        territories[26].addNeighbor(territories[28]);
        territories[26].addNeighbor(territories[29]);

        territories[27].addNeighbor(territories[26]);
        territories[27].addNeighbor(territories[27]);
        territories[27].addNeighbor(territories[28]);
        territories[27].addNeighbor(territories[29]);

        territories[28].addNeighbor(territories[26]);
        territories[28].addNeighbor(territories[27]);
        territories[28].addNeighbor(territories[29]);

        territories[29].addNeighbor(territories[26]);
        territories[29].addNeighbor(territories[27]);
        territories[29].addNeighbor(territories[28]);

        territories[30].addNeighbor(territories[31]);
        territories[30].addNeighbor(territories[32]);
        territories[30].addNeighbor(territories[26]);

        territories[31].addNeighbor(territories[30]);
        territories[31].addNeighbor(territories[32]);
        territories[31].addNeighbor(territories[41]);
        territories[31].addNeighbor(territories[39]);

        territories[32].addNeighbor(territories[31]);
        territories[32].addNeighbor(territories[30]);
        territories[32].addNeighbor(territories[39]);
        territories[32].addNeighbor(territories[40]);
        territories[32].addNeighbor(territories[38]);
        territories[32].addNeighbor(territories[33]);

        territories[33].addNeighbor(territories[32]);
        territories[33].addNeighbor(territories[30]);
        territories[33].addNeighbor(territories[35]);
        territories[33].addNeighbor(territories[37]);
        territories[33].addNeighbor(territories[34]);

        territories[34].addNeighbor(territories[37]);
        territories[34].addNeighbor(territories[33]);

        territories[35].addNeighbor(territories[36]);
        territories[35].addNeighbor(territories[38]);
        territories[35].addNeighbor(territories[33]);
        territories[35].addNeighbor(territories[37]);

        territories[36].addNeighbor(territories[38]);
        territories[36].addNeighbor(territories[35]);
        territories[36].addNeighbor(territories[37]);

        territories[37].addNeighbor(territories[0]);
        territories[37].addNeighbor(territories[36]);
        territories[37].addNeighbor(territories[33]);
        territories[37].addNeighbor(territories[34]);

        territories[38].addNeighbor(territories[40]);
        territories[38].addNeighbor(territories[32]);
        territories[38].addNeighbor(territories[33]);
        territories[38].addNeighbor(territories[35]);
        territories[38].addNeighbor(territories[36]);

        territories[39].addNeighbor(territories[40]);
        territories[39].addNeighbor(territories[41]);
        territories[39].addNeighbor(territories[31]);
        territories[39].addNeighbor(territories[32]);
        territories[39].addNeighbor(territories[24]);
        territories[39].addNeighbor(territories[21]);

        territories[40].addNeighbor(territories[38]);
        territories[40].addNeighbor(territories[32]);
        territories[40].addNeighbor(territories[39]);
        territories[40].addNeighbor(territories[21]);

        territories[41].addNeighbor(territories[24]);
        territories[41].addNeighbor(territories[21]);
        territories[41].addNeighbor(territories[14]);
        territories[41].addNeighbor(territories[31]);
        territories[41].addNeighbor(territories[39]);
        territories[41].addNeighbor(territories[15]);
    }

    public void loadAttackableTer(int territoryId) {
        attackableTer = territories[territoryId].searchForAttackable();
    }

    public boolean isAttackable(int territoryId) {
        return attackableTer.contains(territories[territoryId]);
    }

    public int getCurPlayerId() {
        return curPlayerId;
    }


    public int getTerritoryOwnerId(int territoryId) {
        return territories[territoryId].getOwner().getId();
    }

    public void startTerAlloc(int territoryId) {

/*        riskView.setTerritoryColor(players.get(0).getColor());
        riskView.addTroopsLeft(players.get(0));*/

        //check which territory was clicked for
        Territory territoryClicked = territories[territoryId];
        if (territoryClicked != null && territoryClicked.getOwner() == null) {

            territoryClicked.setTroopCount(5);
            Player curPlayer = players.get(curPlayerId);

            territoryClicked.setOwner(players.get(curPlayerId));

            curPlayer.decreaseTroop(1);
            curPlayer.setTerCount(curPlayer.getTerCount() + 1);


            riskView.setTerritoryColor(players.get(curPlayerId).getColor(), territoryId);
            curPlayerId = (curPlayerId + 1) % playerCount;
            riskView.addTroopsLeft(players.get(curPlayerId));
            tempTerCount--;
        }

        if (tempTerCount <= 0) {
            System.out.println("aa");
            mode = GameMode.AttackMode;
            riskView.setTerritoryClicked(false);
            riskView.setTerritoryMode(mode);
        }
    }

    public void setMode(GameMode mode) {
        this.mode = mode;
    }


    public void startAttack(int troopCount) {

        if (mode != GameMode.AttackMode || troopCount < 1) {
            return;
        }

        target = territories[riskView.getTarget().getTerritoryId()];
        source = territories[riskView.getSource().getTerritoryId()];
        int sourceTroopCount = source.getTroopCount();

        if(sourceTroopCount <= troopCount) {
            return;
        }

        source.setTroopCount(sourceTroopCount - troopCount);

        riskView.setOnKeyPressed(new RPSGame(troopCount));

        riskView.displayRPSView();
    }

    public void startFortify(Player player) {
         Territory tempSource = null;
         Territory tempTarget = null;
         int troopToTransfer = 5;
         if(tempSource.getOwner().getId() == curPlayerId && tempTarget.getOwner().getId() == curPlayerId) {
             tempSource.setTroopCount(tempSource.getTroopCount() - troopToTransfer);
             tempTarget.setTroopCount(tempTarget.getTroopCount() + troopToTransfer);
         }
    }

    //Builds an airport to the territory whose id is given.
    public void buildAirport(int territoryId) {
        Territory territory = territories[territoryId];
        territory.setHasAirport(true);
        territory = new AirportDecorator(territory);
        territories[territoryId] = territory;
    }

    public void update() {
        //////////////////////////////////
        ///         TO DO              ///
        //////////////////////////////////
    }

    class RPSGame implements EventHandler<KeyEvent> {

        private char p1Choice;
        private char p2Choice;
        private boolean p1Chose;
        private boolean p2Chose;
        private int noOfP1Soldiers;
        private int noOfP2Soldiers;
        private boolean gameOver;
        private RPSView rpsView;

        public RPSGame(int noOfP1Soldiers){
            this.noOfP1Soldiers = noOfP1Soldiers;
            this.noOfP2Soldiers = target.getTroopCount();
            this.rpsView = riskView.getRpsView();
            rpsView.updateTroopCounts(noOfP1Soldiers, noOfP2Soldiers);
        }

        @Override
        public void handle(KeyEvent keyEvent) {
            if(gameOver || keyEvent.getText().length() < 1) {
                return;
            }
            char choice = keyEvent.getText().charAt(0);
            if(!p1Chose && (choice == 'a' || choice == 's' || choice == 'd')) {
                p1Choice = choice;
                p1Chose = true;
            }
            else if(!p2Chose && (choice == '1' || choice == '2' || choice == '3')) {
                p2Choice = choice;
                p2Chose = true;
            }

            if(p1Chose && p2Chose) {
                reset();
                playRPS();
            }
        }

        public void playRPS(){

            int winner = compare();

            //Align will be fixed.
            //rpsView.updateWinner(winner);

            if(winner == 0 && noOfP1Soldiers == 1 && noOfP2Soldiers == 1) {
                reset();
                return;
            }

            if (winner != 1) {
                noOfP1Soldiers--;
            }

            if (winner != 2) {
                noOfP2Soldiers--;
            }

            rpsView.updateTroopCounts(noOfP1Soldiers, noOfP2Soldiers);

            if(noOfP1Soldiers > 0 && noOfP2Soldiers > 0) {
                reset();
                return;
            }

            System.out.println("The game is over!");

            gameOver = true;
            applyGameResult();
        }

        public int compare()
        {
            // 'a' or '1' = Rock
            // 's' or '2' = Paper
            // 'd' or '3' = Scissors

            if( (p1Choice == 'a' && p2Choice == '3')
                    || (p1Choice == 's' && p2Choice == '1')
                    ||(p1Choice == 'd' && p2Choice == '2'))
                return 1; // Player 1 wins
            if((p1Choice == 'd' && p2Choice == '1')
                    || (p1Choice == 'a' && p2Choice == '2')
                    ||(p1Choice == 's' && p2Choice == '3'))
                return 2; // Player 2 wins
            return 0; // Draw
        }

        public void reset() {
            p1Chose = false;
            p2Chose = false;
        }

        public void applyGameResult() {

            if(noOfP2Soldiers == 0) {
                //The target has been occupied.
                target.setTroopCount(noOfP1Soldiers);
                target.setOwner(players.get(curPlayerId));
                attackableTer.remove(target);
                riskView.getTarget().setColor(players.get(curPlayerId).getColor());
                players.get(curPlayerId).setCardDeserved(true);
            }
            else {
                target.setTroopCount(noOfP2Soldiers);
            }

            Text targetTerritoryText = riskView.getTarget().getTerritoryText();
            Text sourceTerritoryText = riskView.getSource().getTerritoryText();

            sourceTerritoryText.setText("" + source.getTroopCount());
            targetTerritoryText.setText("" + target.getTroopCount());

            riskView.removeRPSView();
            riskView.removeEventHandler(KeyEvent.ANY, this);
        }
    }
}