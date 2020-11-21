import java.util.*; //deneme

public class RockPaperScissorsGame{
    private static int noOfP1SoldiersPriv;
    private static int noOfP2SoldiersPriv;

    public static int play(//char p1Choice, char p2Choice,
                    int noOfP1Soldiers, int noOfP2Soldiers)
    {
        if( noOfP1Soldiers <= 0 || noOfP2Soldiers <= 0)
            return -1; //invalid entry

        int winner = -1; // Initially we assume round is draw

        while( noOfP1Soldiers > 0 && noOfP2Soldiers > 0) // game is played until one of the players have no soldiers
        {
            Scanner scan = new Scanner(System.in); //deneme
            char p1Choice = scan.next().charAt(0); //deneme
            char p2Choice = scan.next().charAt(0); //deneme

            winner = compare(p1Choice, p2Choice); // 1 if Player 1 wins, 2 if Player 2 wins, -1 if draw

            if(winner == -1) // Draw
                winner = compare(p1Choice, p2Choice);
            if(winner == 1)  // Player 1 wins
                noOfP2Soldiers--;
            if(winner == 2) // Player 2 wins
                noOfP1Soldiers--;
        }
        noOfP1SoldiersPriv = noOfP1Soldiers;
        noOfP2SoldiersPriv = noOfP2Soldiers;

        if( noOfP1Soldiers == 0)
            return 1;

        return 2;
    }

    public static int compare(char p1Choice, char p2Choice)
    {
        // 'A' or 'a' or '1' = Rock
        // 'S' or 's' or '2' = Paper
        // 'D' or 'd' or '3' = Scissors

        if( ((p1Choice == 'a' || p1Choice == 'A') && p2Choice == '3')
                || ((p1Choice == 's' || p1Choice == 'S') && p2Choice == '1')
                ||((p1Choice == 'd' || p1Choice == 'D') && p2Choice == '2'))
            return 1; // Player 1 wins
        if( ((p1Choice == 'd' || p1Choice == 'D') && p2Choice == '1')
                || ((p1Choice == 'a' || p1Choice == 'A') && p2Choice == '2')
                ||((p1Choice == 's' || p1Choice == 'S') && p2Choice == '3'))
            return 2; // Player 2 wins
        return -1; // Draw
    }

    public static int getNoOfP1SoldiersPriv() {
        return noOfP1SoldiersPriv;
    }

    public void setNoOfP1SoldiersPriv(int noOfP1SoldiersPriv) {
        RockPaperScissorsGame.noOfP1SoldiersPriv = noOfP1SoldiersPriv;
    }

    public static int getNoOfP2SoldiersPriv() {
        return noOfP2SoldiersPriv;
    }

    public void setNoOfP2SoldiersPriv(int noOfP2SoldiersPriv) {
        RockPaperScissorsGame.noOfP2SoldiersPriv = noOfP2SoldiersPriv;
    }

    public static void main(String[] args)
    {
        play( 3, 5);

        System.out.println(getNoOfP1SoldiersPriv());
        System.out.println(getNoOfP2SoldiersPriv());
    }
}