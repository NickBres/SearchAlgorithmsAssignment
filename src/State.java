public class Board {

    int[] vector;

    public Board(int[] initialState){
        this.vector = initialState.clone();
    }

    public void printBoard() {
        for (int i = 0; i < vector.length; i++) {
            // Translate integers back to characters for printing
            String symbol = switch (vector[i]) {
                case 10 -> "R";
                case 3 -> "G";
                case 1 -> "B";
                case 0 -> "_";
                case -1 -> "X";
                default -> "?"; // For safety
            };
            System.out.print(symbol + " ");
            if ((i + 1) % 3 == 0) { // Print a new line after each row
                System.out.println();
            }
        }
    }





}
