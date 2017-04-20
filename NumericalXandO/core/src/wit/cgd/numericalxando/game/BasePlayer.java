package wit.cgd.numericalxando.game;

import com.badlogic.gdx.utils.Array;

public abstract class BasePlayer {

    public boolean human;
    public int mySymbol, opponentSymbol;
    public Array<Integer> myNumbers, oppentNumbers;
    public String name;
    public Board board;
    public int skill;
    public int choice;

    public BasePlayer(Board board, int symbol) {
        this.board = board;
        myNumbers = new Array<Integer>();
        oppentNumbers = new Array<Integer>();
        setSymbol(symbol);
        human = false;
        choice = 0;
    }

    public void setSymbol(int symbol) {

        // Creates number arrays for both opponent and my numbers
        if ( (symbol == board.X)) {
            for (int x = 1; x < 10; x++)
                if (x % 2 == 0) oppentNumbers.add(x);
                else myNumbers.add(x);
            mySymbol = board.X;
            opponentSymbol = board.O;
        } else {
            for (int x = 1; x < 10; x++)
                if (x % 2 == 0) myNumbers.add(x);
                else oppentNumbers.add(x);
            mySymbol = board.O;
            opponentSymbol = board.X;
        }
    }

    public void remove(int number) {

        /**
         * Removes given number, not index
         */
        
        int index = -1, loop = 0;

        while (loop < myNumbers.size) {
            if (myNumbers.get(loop) == number) {
                index = loop;
                break;
            }
            loop++;
        }

        if (index != -1) {
            myNumbers.removeIndex(index);
        }
    }

    public boolean valid(int number) {

        // checks if given number(value) is in my numbers
        for (int x = 0; x < myNumbers.size; x++) {
            if (myNumbers.get(x) == number) return true;
        }

        return false;
    }

    public abstract int move();

}
