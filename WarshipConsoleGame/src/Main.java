import java.util.Scanner;

public class Main {
    static final int BOARD_SIZE = 30;
    static final int MAX_SHIPS = 6;
    static int shipCounter1 = 0;
    static int shipCounter2 = 0;
    static int sizeCounter1 = 0;
    static int sizeCounter2 = 0;
    static int player;

    static class Ship {
        int size;  // number of masts
        boolean horizontal;  // true if the ship is horizontal, false if vertical
        int x;  // ship's x position (if horizontal == true) or y position (if horizontal == false)
        int y;  // ship's y position (if horizontal == true) or x position (if horizontal == false)
    }

    // 2-dimensional array representing shots
    static char[][] player1ShooterBoard = new char[BOARD_SIZE][BOARD_SIZE];
    static char[][] player2ShooterBoard = new char[BOARD_SIZE][BOARD_SIZE];

    // 2-dimensional array representing the board
    static int[][] player1Board = new int[BOARD_SIZE][BOARD_SIZE];
    static int[][] player2Board = new int[BOARD_SIZE][BOARD_SIZE];

    // array to store our ships
    static Ship[] player1Ships = new Ship[MAX_SHIPS];

    // array to store opponent's ships
    static Ship[] player2Ships = new Ship[MAX_SHIPS];

    // filling the shooter board with asterisks
    static void createShooterBoard(char[][] shooterBoard) {
        // Fill shooterBoard with asterisks
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                shooterBoard[i][j] = '*';
            }
        }
    }

    static void Shoot(char[][] shooterBoard, int[][] playerBoard) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter column and row for the shot, both ranging from 0 to 29:");
        int column = scanner.nextInt();
        int row = scanner.nextInt();
        if (playerBoard[row][column] == 0) {
            shooterBoard[row][column] = 'O';
            System.out.println("Miss");
        } else {
            shooterBoard[row][column] = 'X';
            System.out.println("Hit");
        }
    }

    // function responsible for ending the game
    static void finish(char[][] shooterBoard, int sizeCounter, int playerNumber) {
        int shootCounter = 0;

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (shooterBoard[i][j] == 'X') {
                    shootCounter++;
                }
            }
        }

        if (shootCounter == sizeCounter) {
            System.out.println("Player " + playerNumber + " wins");
            System.out.println("Winner's shooting board:");
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    System.out.print(shooterBoard[i][j] + " ");
                }
                System.out.println();
            }
            System.exit(1);
        }
    }

    // function for manually placing ships
    static void manuallyPlaceShips(int[][] tab) {
        int sizeCounter;
        int shipCounter;
        if (player == 1) {
            shipCounter = shipCounter1;
            sizeCounter = sizeCounter1;
        } else {
            shipCounter = shipCounter2;
            sizeCounter = sizeCounter2;
        }
        while (shipCounter != 6) {
            System.out.println("Manual ship placement");
            System.out.print("Enter ship size (2-5 masts): ");
            Scanner scanner = new Scanner(System.in);
            int size = scanner.nextInt();
            if (size < 2 || size > 5) {
                System.out.println("Invalid size");
                return;
            }
            System.out.print("Enter the x position of the start of the ship: ");
            int x = scanner.nextInt();
            System.out.print("Enter the y position of the start of the ship: ");
            int y = scanner.nextInt();
            System.out.print("Enter whether the ship should be vertical (1) or horizontal (0): ");
            boolean horizontal = scanner.nextBoolean();

            // checking if the ship fits on the board and doesn't collide with other ships
            if (x < 0 || x >= BOARD_SIZE || y < 0 || y >= BOARD_SIZE || (horizontal && x + size > BOARD_SIZE) ||
                    (!horizontal && y + size > BOARD_SIZE)) {
                System.out.println("Ship is off the board");
                return;
            }
            for (int i = 0; i < size; i++) {
                if (tab[x + (horizontal ? i : 0)][y + (horizontal ? 0 : i)] != 0) {
                    System.out.println("Ship collides with another");
                    return;
                }
            }

            // placing the ship on the board
            Ship newShip = new Ship();
            newShip.size = size;
            newShip.horizontal = horizontal;
            newShip.x = x;
            newShip.y = y;
            for (int i = 0; i < size; i++) {
                tab[x + (horizontal ? i : 0)][y + (horizontal ? 0 : i)] = size;
            }
            System.out.println("Ship placed");
            shipCounter++;
            sizeCounter += size;
            if (player == 1) {
                shipCounter1 = shipCounter;
                sizeCounter1 = sizeCounter;
            } else {
                shipCounter2 = shipCounter;
                sizeCounter2 = sizeCounter;
            }
        }
    }

    // function for automatically placing ships
    static void automaticallyPlaceShips(int[][] tab, Ship[] playerShips, int playerNumber) {
        System.out.println("Automatic ship placement");
        int sizeCounter;
        if (player == 1) {
            sizeCounter = sizeCounter1;
        } else {
            sizeCounter = sizeCounter2;
        }
        for (int i = 0; i < MAX_SHIPS; i++) {
            Ship newShip = new Ship();
            newShip.size = (int) (Math.random() * 4 + 2); // random ship size from 2 to 5 masts
            newShip.horizontal = Math.random() < 0.5; // random horizontal or vertical orientation
            newShip.x = (int) (Math.random() * BOARD_SIZE);
            newShip.y = (int) (Math.random() * BOARD_SIZE);

            // checking if the ship fits on the board and doesn't collide with other ships
            if (newShip.x < 0 || newShip.x >= BOARD_SIZE || newShip.y < 0 || newShip.y >= BOARD_SIZE ||
                    (newShip.horizontal && newShip.x + newShip.size > BOARD_SIZE) ||
                    (!newShip.horizontal && newShip.y + newShip.size > BOARD_SIZE)) {
                // if the ship doesn't fit on the board, generate again
                i--;
                continue;
            }
            boolean collision = false;
            for (int j = 0; j < newShip.size; j++) {
                if (tab[newShip.x + (newShip.horizontal ? j : 0)][newShip.y + (newShip.horizontal ? 0 : j)] != 0) {
                    // if the ship collides with another, generate again
                    collision = true;
                    break;
                }
            }
            if (collision) {
                i--;
                continue;
            }
            if (player == 1) {
                sizeCounter1 += newShip.size;
            } else {
                sizeCounter2 += newShip.size;
            }
            // placing the ship on the board
            playerShips[i] = newShip;
            for (int j = 0; j < newShip.size; j++) {
                tab[newShip.x + (newShip.horizontal ? j : 0)][newShip.y + (newShip.horizontal ? 0 : j)] = newShip.size;
            }
        }
        System.out.println("Ships placed");
    }

    // function to verify ship placement (automatic or manual)
    static boolean verifyShipPlacement(Ship[] playerShips) {
        // checking if all ships are placed on the board
        for (int i = 0; i < MAX_SHIPS; i++) {
            if (playerShips[i].size == 0) {
                System.out.println("Not all ships are placed on the board");
                return false;
            }
        }

        // checking if ships collide with each other
        for (int i = 0; i < MAX_SHIPS; i++) {
            for (int j = 0; j < playerShips[i].size; j++) {
                int x = playerShips[i].x + (playerShips[i].horizontal ? j : 0);
                int y = playerShips[i].y + (playerShips[i].horizontal ? 0 : j);
                for (int k = 0; k < MAX_SHIPS; k++) {
                    if (k == i) continue;
                    for (int l = 0; l < playerShips[k].size; l++) {
                        int x2 = playerShips[k].x + (playerShips[k].horizontal ? l : 0);
                        int y2 = playerShips[k].y + (playerShips[k].horizontal ? 0 : l);
                        if (x == x2 && y == y2) {
                            System.out.println("Ships collide with each other");
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    static void playGame() {
        createShooterBoard(player1ShooterBoard);
        createShooterBoard(player2ShooterBoard);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Player 1");
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    System.out.print(player1ShooterBoard[i][j] + " ");
                }
                System.out.println();
            }
            System.out.println("Enter coordinates for the shot:");
            Shoot(player1ShooterBoard, player2Board);
            finish(player1ShooterBoard, sizeCounter2, 1);
            System.out.println("Player 2");
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    System.out.print(player2ShooterBoard[i][j] + " ");
                }
                System.out.println();
            }
            System.out.println("Enter coordinates for the shot:");
            Shoot(player2ShooterBoard, player1Board);
            finish(player2ShooterBoard, sizeCounter1, 2);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("Choose an option:");
            System.out.println("1. Manually place ships");
            System.out.println("2. Automatically place ships");
            System.out.println("3. Start the game");
            System.out.println("4. End the game");
            System.out.println("5. Display ships");
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Enter the player number whose ships you want to place (1-2):");
                    player = scanner.nextInt();
                    if (player == 1) {
                        manuallyPlaceShips(player1Board);
                    } else {
                        manuallyPlaceShips(player2Board);
                    }
                    break;
                case 2:
                    System.out.println("Enter the player number whose ships you want to place automatically (1-2):");
                    player = scanner.nextInt();
                    if (player == 1) {
                        automaticallyPlaceShips(player1Board, player1Ships, 1);
                    } else {
                        automaticallyPlaceShips(player2Board, player2Ships, 2);
                    }
                    break;
                case 3:
                    if (verifyShipPlacement(player1Ships) && verifyShipPlacement(player2Ships)) {
                        playGame();
                    } else {
                        System.out.println("Incorrect ship placement");
                    }
                    break;
                case 4:
                    System.out.println("Goodbye!");
                    break;
                case 5:
                    System.out.println("Enter which player's ships you want to display:");
                    player = scanner.nextInt();
                    if (player == 1) {
                        for (int i = 0; i < BOARD_SIZE; i++) {
                            for (int j = 0; j < BOARD_SIZE; j++) {
                                System.out.print(player1Board[i][j] + " ");
                            }
                            System.out.println();
                        }
                    } else {
                        for (int i = 0; i < BOARD_SIZE; i++) {
                            for (int j = 0; j < BOARD_SIZE; j++) {
                                System.out.print(player2Board[i][j] + " ");
                            }
                            System.out.println();
                        }
                    }
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        } while (choice != 4);

    }
}
