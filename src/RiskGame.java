import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RiskGame {
    private final int TER_COUNT = 42;
    private final int AIRPORT_COST = 5;
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
    private Territory sourceTer;
    private int playerCounterForEachTurn = 0;
    private List<Request> requests;

    // RPS
    // Continent List
    enum GameMode {
        TerAllocationMode, SoldierAllocationMode, SoldierAllocationModeContinued, AttackMode, FortifyMode
    }
    private GameMode mode;

    public RiskGame(ArrayList<Player> players, Territory[] territories, RiskView riskView) {
        for (Player p : players)
            p.setPlayerCount(players.size());
         this.players = players;
         this.territories = territories;
         this.riskView = riskView;

         curPlayerId = 0;
         gamePhase = 0;
         playerCount = players.size();
         cards = null;   // for now
         isGameOver = false;
         mode = GameMode.TerAllocationMode;
         // Continents
         // RPS
        requests = new ArrayList<>();
    }

    public Player play() {
        startInitialization();
        //riskView.addAlliancePane();
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
            riskView.getCardsButton().setOnMouseClicked(e -> {
                riskView.addCardExchangePane();
                riskView.setCardExchangeInfo(players.get(curPlayerId));
            });
            riskView.setTerritoryColor(players.get(0).getColor());
            riskView.addTroopsLeft(players.get(0));

            riskView.setOnMouseClicked(e -> {
                //check which territory was clicked for
                ClickableTerritory clickableTerritory = riskView.getClickableTerritory();
                if(clickableTerritory != null && clickableTerritory.getAssociatedTerritory() != null && clickableTerritory.getAssociatedTerritory().getOwnerId() == -1) {
                    Player curPlayer = players.get(curPlayerId);

                    clickableTerritory.getAssociatedTerritory().setOwnerId(curPlayerId);

                    curPlayer.decreaseTroop(1);
                    curPlayer.setTerCount(curPlayer.getTerCount() + 1);
                    nextTurn();

                    riskView.addTroopsLeft(players.get(curPlayerId));

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
            riskView.setTerritoryMode(GameMode.SoldierAllocationMode);
            Player curPlayer = players.get(curPlayerId);
            int noOfTroops = curPlayer.getTroopCount();

            //below takes the selected territory from the original map source
            //if the selected territory is not null and is one of the current player's territories, RiskView adds
            //troop selection screen and from here on we will be taken to another GameMode method of the game
            riskView.setOnMouseClicked(e -> {
                ClickableTerritory clickableTerritory = riskView.getClickableTerritory();
                if( clickableTerritory != null && clickableTerritory.getAssociatedTerritory() != null && clickableTerritory.getAssociatedTerritory().getOwnerId() == curPlayer.getId()) {
                    riskView.addTroopCountSelector(noOfTroops);
                    riskView.setMaxCountSelection(curPlayer.getTroopCount());
                    mode = GameMode.SoldierAllocationModeContinued;
                    continueSoldierAllocation(clickableTerritory);
                }
            });
        }
    }

    //resumes the main soldier allocation method, basically this phase goes back and forth between these two
    public void continueSoldierAllocation(ClickableTerritory clickableTerritory) {
        if( mode == GameMode.SoldierAllocationModeContinued) {
            Player curPlayer = players.get(curPlayerId);
            //the methods below each call specific buttons of RiskView instead of assigning mouse listeners to
            // riskView itself, like we have been doing. This is because RiskView and each button listener take inputs
            // separately, so if I were to click on "place" then I would need to click on another space in the window
            // for functions defined below to take place, in a scenario where riskView is assigned listeners.
            // That is why, here we are assigning mouse listeners to each button
            riskView.getBackButton().setOnMouseClicked(e -> {
                //goes back to the original Soldier Allocation function to take a territory as input again
                riskView.removeTroopCountSelector();
                mode = GameMode.SoldierAllocationMode;
                startSoldierAlloc();
            });
            riskView.getPlaceButton().setOnMouseClicked(e -> {
                //sets selected troop integer, and places it in the territory specified itself
                int selectedTroop = riskView.getSelectedTroop();
                clickableTerritory.getAssociatedTerritory().addTroop(selectedTroop);
                curPlayer.decreaseTroop(selectedTroop);
                riskView.removeTroopCountSelector();
                riskView.updateTroopsCount(curPlayer);
                riskView.updateTerTroopCount(clickableTerritory, selectedTroop);

                //sets the number to appear between more and less buttons
                //riskView.setMaxCountSelection(curPlayer.getTroopCount());
                if (curPlayer.getTroopCount() <= 0) {
                    playerCounterForEachTurn++;
                    nextTurn();
                    riskView.updatePlayerBar(players.get(curPlayerId));
                    riskView.updateTroopsCount(players.get(curPlayerId));

                    setMode(GameMode.SoldierAllocationMode);
                    startSoldierAlloc();

                    if( playerCounterForEachTurn >= playerCount) {
                        setMode(GameMode.AttackMode);
                    }
                }
            });
        }
    }

    public void startAttack(Player player) {
         //////////////////////////////////
         ///         TO DO              ///
         //////////////////////////////////
    }

    /*public void startFortify(Player player) {
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
                    if (source != null *//*&& source.getOwnerId() != -1*//*) {
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
                target = riskView.getClickableTerritory(); //target territory is assigned

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
    }*/

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

    public void sendAllianceRequest(Player source, Player target) //to send an alliance req. with
                                                                    // source and target players
    {
        if(!source.isAlly(target))
        //A player can send the request only in the SoldierAllocationMode, AttackMode, and FortifyMode
        {
            Request request = new Request(source, target); // new request is created
            requests.add(request); //request is added to the arraylist containing requests
            target.addAllianceReq(source.getId(), source.getName());
        }
    }

    public void interactWithRequest(Player player, boolean choice) //to interact with an alliance; choice:(reject or accept)
    {
        if(mode == GameMode.SoldierAllocationMode || mode == GameMode.SoldierAllocationModeContinued)
        //A player can interact with a request only in the SoldierAllocationMode
        {
            for(Request r:requests) { //the arraylist for requests is traversed
                if(r.target == player) { //it is checked if the request is targeted to the player
                    if(choice) //if the target player wants to accept the request
                        player.addAlly(r.source.getId()); //the source player is added as an ally
                    if(!choice) //if the target player doesn't want to accept the request
                        requests.remove(r); //the request is deleted
                    break;
                }
            }
        }
    }

    public void cancelAlliance(Player source, Player target) //to cancel an existing alliance
    {
        if(mode == GameMode.SoldierAllocationMode || mode == GameMode.SoldierAllocationModeContinued) {
            //A player can cancel an alliance only in the SoldierAllocationMode
            for(Request r: requests)//the arraylist for requests is traversed (we got help from to
                                    // requests hold the alliances too)
            {
                if(r.source == source && r.target == target) //if the parameters are true
                {
                    requests.remove(r); //the alliance is removed from the requests

                    //the alliance is removed from the both players
                    source.removeAlly(target.getId());
                    target.removeAlly(source.getId());

                    break;
                }
            }
        }
    }

    public Enum<GameMode> getMode()
    {
        return mode;
    }

    public int getCurPlayerId()
    {
        return curPlayerId;
    }

    public ArrayList<Player> getPlayers()
    {
        return (ArrayList<Player>) players;
    }

    /*
        REQUEST CLASS FOR ALLIANCES
    */
    class Request{
        Player source;
        Player target;

        public Request(Player source, Player target)
        {
            this.source = source;
            this.target = target;
        }

        Player getSource()
        {
            return source;
        }

        Player getTarget(){
            return target;
        }
    }
}