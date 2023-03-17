// Theodore Ingberman
// 3/13/2023
// CS 145
// Assignment 3 
// this class represents the methods and variables
// behind the tictactoe game

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class TicTacToe { // start of TicTacToe class

    ArrayList<Integer> playerPositions = new ArrayList<Integer>();
    // list of the players choses positions
    ArrayList<Integer> cpuPositions = new ArrayList<Integer>();
    // list of the CPUs chosen positions

    private char[][] board = 
        {{'|',' ','|',' ','|',' ','|'},
        {'=', '=','+','=','+','=', '='},
        {'|',' ','|',' ','|',' ','|'},
        {'=', '=','+','=','+','=', '='},
        {'|',' ','|',' ','|',' ','|'}}; 
    //array of the game board

    public void play() { // start of play method
        Scanner input = new Scanner(System.in);
        printBoard();

        //while loop that runs until the game is over
        while(true) { // start of while loop
            System.out.println("Type your desired position (1-9):");
            int position = input.nextInt();
            // choice of where to input X
                while(playerPositions.contains(position) 
                || cpuPositions.contains(position)) { // start of while 
                // checks if the position is empty 
                    System.out.println("type an empty square");
                    position = input.nextInt();
                } // end of while

            

            choosePosition(position, "player");
            printBoard();

            String gameResult = checkWinner();
                if(gameResult.length() > 0) { // start of if 
                    System.out.println(gameResult);
                    break;
                } // end of if
                
            Random r = new Random();
            int cpuPosition = r.nextInt(9)+ 1;
                while(playerPositions.contains(cpuPosition) 
                || cpuPositions.contains(cpuPosition)) { // start of while
                // checks if the position is empty
                    cpuPosition = r.nextInt(9)+ 1;
                } // end of while

            choosePosition(cpuPosition, "cpu");
            printBoard();

                if(gameResult.length() > 0) { // start of if
                    System.out.println(gameResult);
                    break;
                } // end of if          
        } // end of while
    } // end of play method

    public void choosePosition(int position, String user) { 
        // start of choosePosition method

        char symbol;

        if(user.equals("player")) { // start of if
        // if its the players turn, set symbol to X
            symbol = 'X';
            playerPositions.add(position);
        // add the position to playerPositions arraylist
        } else {
            symbol = 'O';
            cpuPositions.add(position);
        } // end of if 
        
        // switch case for addding into the symbol into the 
        // game board char array
        switch(position) { // start of switch case 
            case 1:
                board[0][1] = symbol;
                break;
            case 2:
                board[0][3] = symbol;
                break;
            case 3:
                board[0][5] = symbol;
                break;
            case 4:
                board[2][1] = symbol;
                break;
            case 5:
                board[2][3] = symbol;
                break;
            case 6:
                board[2][5] = symbol;
                break;
            case 7:
                board[4][1] = symbol;
                break;
            case 8:
                board[4][3] = symbol;
                break;
            case 9:
                board[4][5] = symbol;
                break;
            default:
                break;
        } // end of switch case
    } // end of choosePosition method 

    public void printBoard() { // start of printBoard method
        for(char[] row : board) { // start of for each
            for(char c : row) { // start of for each 2 
                System.out.print(c);
            } // end of for each 2 
            System.out.println();
        } // end of for each
        System.out.println();
    } // end of printBoard method 

    public String checkWinner() { // start of checkWinner method

        // lists for the possible winning combinations
        List topRow = Arrays.asList(1,2,3);
        List middleRow = Arrays.asList(4,5,6);
        List bottomRow = Arrays.asList(7,8,9);
        List leftColumn = Arrays.asList(1,4,7);
        List middleColumn = Arrays.asList(2,5,8);
        List rightColumn = Arrays.asList(3,6,9);
        List firstDiag = Arrays.asList(1,5,9);
        List secondDiag = Arrays.asList(7,5,3);
        
        // list of all those lists
        List<List> winningCombos = new ArrayList<List>();
        winningCombos.add(topRow);
        winningCombos.add(middleRow);
        winningCombos.add(bottomRow);
        winningCombos.add(leftColumn);
        winningCombos.add(middleColumn);
        winningCombos.add(rightColumn);
        winningCombos.add(firstDiag);
        winningCombos.add(secondDiag);
        
        // iterates through the lists and checks if either player has won
        for(List l : winningCombos) { // start of for each 
            if(playerPositions.containsAll(l)) { // start of if 
                return "you win!";
            } else if(cpuPositions.containsAll(l)) {
                return "cpu wins";
            } else if(playerPositions.size()+cpuPositions.size() == 9) {
                return "stalemate";
            } // end of if
        } // end of for each

        return "";        
    } // end of checkWinner method
} // end of TicTacToe class
