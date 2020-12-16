import java.awt.*;
import java.util.ArrayList;
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
    private Territory source;
    private Territory target;
    private int tempTerCount = TER_COUNT;
    private int nextPlayerIndex;
    private int ctr = 0;
    // RPS
    // Continent List
    enum GameMode {
        TerAllocationMode, TroopAllocationMode, SoldierAllocationMode, AttackMode, FortifyMode1, FortifyMode2
    }
    private GameMode mode = GameMode.TerAllocationMode;

    public RiskGame(ArrayList<Player> players, Territory[] territories, RiskView riskView) {
         this.players = players;
         this.territories = territories;
         this.riskView = riskView;
         for (Player p : players)
            p.setPlayerCount(players.size());
         curPlayerId = 0;
         gamePhase = 0;
         playerCount = players.size();
         cards = null;   // for now
         isGameOver = false;
         // Continents
         // RPS
    }

    public Player play() {
        //startTerAlloc();
        startInitialization();

        //riskView.addTroopCountSelector(1);
        /**
        while (!isGameOver) {
            Player curPlayer = players.get(curPlayerId);
            startSoldierAlloc(curPlayer);
            startAttack(curPlayer);
            startFortify(curPlayer);
            update();
            curPlayerId = (curPlayerId + 1) % playerCount;
        }
         **/
        return null;
    }

    public void startInitialization() {
        startTerAlloc();
    }

    public void startTerAlloc() {
        if( mode == GameMode.TerAllocationMode) {
            riskView.setTerritoryColor(players.get(0).getColor());

            riskView.setOnMouseClicked(e -> {
                //check which territory was clicked for
                Territory territoryClicked = riskView.getClickedTerritory();
                if(territoryClicked != null && territoryClicked.getOwnerId() == -1) {
                    System.out.println(territoryClicked.getName());
                    Player curPlayer = players.get(curPlayerId);

                    System.out.println(curPlayerId);
                    territoryClicked.setOwnerId(curPlayerId);

                    curPlayer.decreaseTroop(1);
                    curPlayer.setTerCount(curPlayer.getTerCount() + 1);
                    nextTurn();

                    riskView.setTerritoryColor(players.get(curPlayerId).getColor());
                    tempTerCount--;
                }

                if(tempTerCount <= 0) {
                    System.out.println("aa");
                    mode = GameMode.SoldierAllocationMode;
                    riskView.setTerritoryClicked(false);
                    riskView.setTerritoryMode(mode);
                    startSoldierAlloc();
                }
            });
        }
    }

    public void setMode(GameMode mode) {
        this.mode = mode;
    }

    public void startSoldierAlloc() {
        if(mode == GameMode.SoldierAllocationMode) {
            System.out.println("içerdeyiz");
            riskView.setTerritoryMode(GameMode.SoldierAllocationMode);
            Player curPlayer = players.get(curPlayerId);
            riskView.setOnMouseClicked(e -> {
                System.out.println("içerdeyiz evet");
                Territory territoryClicked = riskView.getClickedTerritory();
                int noOfTroops = curPlayer.getTroopCount();
                if( territoryClicked != null && territoryClicked.getOwnerId() == curPlayer.getId()) {
                    System.out.println("benim mekan");
                    riskView.addTroopCountSelector(noOfTroops);
                    int noOfTroopsToBeAllocatedTemp = 0;
                    //////////////////////////////////////////////////////////////////////
                    //number of troops to be allocated is got from the player with a listener
                    //                                                                  //
                    //////////////////////////////////////////////////////////////////////
                    territoryClicked.setTroopCount(noOfTroopsToBeAllocatedTemp);
                    curPlayer.decreaseTroop(noOfTroopsToBeAllocatedTemp);
                    noOfTroops = noOfTroops - noOfTroopsToBeAllocatedTemp;
                    curPlayer.setTroopCount(noOfTroops);
                }
                if(noOfTroops == 0) {
                    setMode(GameMode.AttackMode);
                }
            });
        }
    }

    public void startAttack(Player player) {
         //////////////////////////////////
         ///         TO DO              ///
         //////////////////////////////////
    }

    public void startFortify(Player player) {
        curPlayerId = player.getId();
        if(mode == GameMode.FortifyMode1) { //checking the current mode
            System.out.println("mode 1");
            riskView.setOnMouseClicked((e) -> {
                source = riskView.getClickedTerritory(); //source territory is assigned
                ctr++; //counter is incremented to stay in the listeners for a certain amount of time
                System.out.println("1 " + source.getName());
                //riskView.addTroopCountSelector();
            }
            );

            riskView.setOnMouseReleased((e)->{
                    if (source != null /*&& source.getOwnerId() != -1*/) {
                        System.out.println("entered");
                        mode = GameMode.FortifyMode2;
                        setMode(mode);
                        ctr++;
                    }
                    if( ctr == 2)
                        continueFortify(player); //second phase of fortify is called for the same player
            });
        }


    }
    public void continueFortify(Player player) {
        ctr = 0; //ctr is reseted again to be able to terminate the listener
        int troopToTransfer = 5; //temporary
        if(mode == GameMode.FortifyMode2){ //checking the current mode
            System.out.println("mode 2");
            riskView.setOnMouseClicked(e->{
                target = riskView.getClickedTerritory(); //target territory is assigned

                System.out.println("2 " +target.getName());

                if(target != null && target.getOwnerId() != -1){
                    if(source.getTroopCount() >= troopToTransfer) { //check if the selected troop count exceeds
                                                                    // available troops in the territory or not
                        if ((source.getOwnerId() == target.getOwnerId())) {
                            source.setTroopCount(source.getTroopCount() - troopToTransfer);
                            target.setTroopCount(target.getTroopCount() + troopToTransfer);

                            //riskView.removeTroopCountSelector();
                            mode = GameMode.SoldierAllocationMode;
                            setMode(mode);
                            nextTurn(); //next player is found
                            ctr++;
                            if(ctr == 1){} //to terminate the listener
                                //startSoldierAlloc(players.get(curPlayerId)); //soldier allocation is initiated for
                                                                             //next player
                        }
                    }
                    else{ //reselect the number of soldiers

                    }
                }
                else
                {
                    mode = GameMode.FortifyMode1;
                    setMode(mode);
                    ctr++;
                    if(ctr == 1)
                        startFortify(player); //if the player fails to select his/her own territory first
                                              //phase of the fortify is called again
                }
            });
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

    public void nextTurn() {
        curPlayerId = (curPlayerId + 1) % playerCount;
    }
}