/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper;

import java.util.Arrays;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader; 
import java.io.IOException; 

/**
 * Board class that handles all operations on the minesweeper board
 */
public class Board {
    
    /**
     * Abstraction function: represents a minesweeper board with each square's status in statusBoard and posession of bomb in bombBoard
     * 
     * Rep Invariants: 
     * - bombCount >= 0
     * - statusBoard[i][j] is UNTOUCHED, FLAGGED, DUG, or COUNT
     * - bombCount = number of trues in bombBoard
     * - dimensions of statusBoard and bombBoard are identical
     * 
     * Rep Exposure:
     * - all variables are private
     * - bombCount is immutable
     * - bombBoard is never returned or shown to client
     * - statusBoard is copied when returned to client to protect reference
     * 
     * Thread Safety Agreement:
     * - mutator methods dig, flag, deflag, digNoBomb are under a synchronized lock
     * - all accesses to the fields are guarded by sychronized
     */
    
    private final String[][] statusBoard;
    // -     -> untouched
    // F     -> flagged
    //       -> dug & 0 neighbors 
    // COUNT -> dug & COUNT neighbors
    
    public final boolean[][] bombBoard;
    // true  -> bomb present
    // false -> bomb not present
    
    
    private int bombCount;
    
    //display customization options
    private final String DUG = " ";
    private final String FLAGGED = "F";
    private final String UNTOUCHED = "-";
    
    /**
     * assert that the representation invariants still hold
     */
    private void checkRep() {
        boolean cellCheck;
        String[] arr = {"1", "2", "3", "4", "5", "6", "7", "8"};
        List<String> list = Arrays.asList(arr);
        assert statusBoard.length == bombBoard.length : "bad board";
        assert statusBoard[0].length == bombBoard[0].length : "bad board";
        String cell;
        int checkBombCount = 0;
        
        for(int i = 0; i < statusBoard[0].length; i++) {
            for(int j = 0; j < statusBoard.length; j++) {
                cell = statusBoard[j][i];
                cellCheck = false;
                cellCheck |= (cell.equals(UNTOUCHED));
                cellCheck |= (cell.equals(FLAGGED));
                cellCheck |= (cell.equals(DUG));
                cellCheck |= (list.contains(cell));
                assert cellCheck : "bad cell";
                
                if(bombBoard[j][i]) {
                    checkBombCount++;
                }
                
            }
        }
        assert this.bombCount == checkBombCount : "bad bomb count";
    }
    
    /**
     * creates a new board object with dimension parameters
     * @param width number of columns in the board
     * @param height number of rows in the board
     */
    public Board(int width, int height) {
        statusBoard = new String[height][width];
        bombBoard = new boolean[height][width];
        bombCount = 0;
        
        //Arrays.fill(statusBoard, "-");
        
        for(int i = 0; i < statusBoard.length; i++) {
            for(int j = 0; j < statusBoard[0].length; j++) {
                statusBoard[i][j] = UNTOUCHED;
                if(Math.random() <= .25) {
                    bombBoard[i][j] = true;
                    bombCount++;
                }
            }
        }
        
        
        
        checkRep();
    }
    
    /**
     * creates a new board object from a given text file
     * @param textFile text file to be used
     */
    public Board(File boardFile) {
        bombCount = 0;
        
        int width = -1;
        int height = -1;
        boolean[][] fakeBombBoard = new boolean[1][1];
        String[] values;
        BufferedReader br = null;
        try {
            FileReader f = new FileReader(boardFile);
            br = new BufferedReader(f);
            String dimensions = br.readLine();
            width = Integer.valueOf(dimensions.split(" ")[0]);
            height = Integer.valueOf(dimensions.split(" ")[1]);
            fakeBombBoard = new boolean[height][width];
            for(int i = 0; i < height; i++) {
                values = br.readLine().split(" ");
                if(values.length != width) { throw new RuntimeException(); }
                for(int j = 0; j < width; j++) {
                    if(values[j].equals("1")) {
                        fakeBombBoard[i][j] = true;
                        bombCount ++;
                    }
                }
                
            }
            
        }
        catch (IOException ioe ){
            
            ioe.printStackTrace();
        }
        finally {
            
            statusBoard = new String[height][width];
            bombBoard = fakeBombBoard;
            for(int i = 0; i < statusBoard.length; i++) {
                for(int j = 0; j < statusBoard[0].length; j++) {
                    statusBoard[i][j] = UNTOUCHED;
                }
            }
            
            try {
                br.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        checkRep();
    }
    
    /**
     * helper method that counts how many bombs surround a certain square
     * @param x x-coord of target square
     * @param y y-coord of target square
     * @return number of neighboring squares with bombs
     */
    private int findSurroundingBombCount(int x, int y) {
        int total = 0;
        for(int i = -1; i <= 1; i++) {
            for(int j = -1; j <= 1; j++) {
                if(i == 0 && j == 0) { continue; }
                if( !outOfBounds(x+i, y+j)) {
                    if(bombBoard[y+j][x+i]) {
                        
                        total += 1;
                    }
                }
            }
        }
        return total;
    }
    
    /**
     * digs out all squares surrounding a target square that have no neighbors with bombs (like when a large patch of empty squares appears)
     * @param x x-coord of target square
     * @param y y-coord of target square
     */
    private synchronized void digNoBomb(int x, int y) {
        for(int i = -1; i <= 1; i++) {
            for(int j = -1; j <= 1; j++) {
                
                if(i == 0 && j == 0) { continue; }
                if( !outOfBounds(x+j, y+i) ) {
                    String[] arr = {"1", "2", "3", "4", "5", "6", "7", "8"};
                    List<String> list = Arrays.asList(arr);
                    if(!statusBoard[y+i][x+j].equals(UNTOUCHED) && !list.contains(statusBoard[y+i][x+j])) { continue; }
                    int bombs = findSurroundingBombCount(x+j, y+i);
                    if(bombs == 0) {
                        statusBoard[y+i][x+j] = DUG;
                        digNoBomb(x+j, y+i);
                    }
                    else {
                        statusBoard[y+i][x+j] = Integer.toString(bombs);
                    }
                    
                
                }
                else {
                }
            }
        }
        checkRep();
        
    }
    
    public int[] getDimensions() {
        int[] a = new int[2];
        a[0] = statusBoard[0].length;
        a[1] = statusBoard.length;
        return a;
    }
    
    
    /**
     * digs out all squares surrounding a target square that have no neighbors with bombs (like when a large patch of empty squares appears)
     * @return copy of statusBoard to show 
     */
    
    public String look() {
        String result = "";
        result += statusBoard[0][0].toString();
        //System.out.print("0" + i);
        for(int j = 1; j < statusBoard[0].length; j++) {
            result += " " + statusBoard[0][j].toString();
            //System.out.print(" " + i + j);
        }
        
        for(int i = 1; i < statusBoard.length; i++) {
            result += "\n";
            result += statusBoard[i][0].toString();
            //System.out.print("0" + i);
            for(int j = 1; j < statusBoard[0].length; j++) {
                result += " " + statusBoard[i][j].toString();
                //System.out.print(" " + i + j);
            }
            
        }
        return result;
    }
    
    /*
    
    public String[][] look() {
        //status board is width 3 height 4
        // String[3][4] -> sts
        String[][] result = new String[statusBoard.length][statusBoard[0].length];
        for(int i = 0; i < statusBoard.length; i++) {
            for(int j = 0; j < statusBoard[0].length; j++) {
                result[i][j] = statusBoard[i][j];
            }
        }
        return result;
    }
    */
    
    /**
     * digs out the square at (x, y)
     * @param x x-coordinate of square, requires 0 <= x <= width
     * @param y y-coordinate of square, requires 0 <= y <= height
     * @return string flag to indicate steps for MinesweeperServer class
     */
    public synchronized String dig(int x, int y) {
        // not untouched
        
        String result = "BOARD";
        if(outOfBounds(x, y) || !statusBoard[y][x].equals(UNTOUCHED)) {
            checkRep();
            return result;
        }
        
        int bombs = findSurroundingBombCount(x, y);
        
        if(bombBoard[y][x]) {
            bombBoard[y][x] = false;
            result = "BOOM";
            
            bombCount --;
            if(bombs == 0) {
                
                statusBoard[y][x] = DUG;
                digNoBomb(x, y);
            }
            else {
                statusBoard[y][x] = Integer.toString(bombs);
            }
            
        }
        // untouched and no bomb
        // check if 0
        else {
            if(bombs == 0) {
                statusBoard[y][x] = DUG;
                digNoBomb(x, y);
            }
            else {
                statusBoard[y][x] = Integer.toString(bombs);
            }
        }
        checkRep();
        return result;
        
        
        
    }
    
    /**
     * flags the square at (x, y)
     * @param x x-coordinate of square, requires 0 <= x <= width
     * @param y y-coordinate of square, requires 0 <= y <= height
     * @return string flag to indicate steps for MinesweeperServer class
     */
    public synchronized String flag(int x, int y) {
        
        // not untouched
        String result = "BOARD";
        if(outOfBounds(x, y) || !statusBoard[y][x].equals(UNTOUCHED)) {
            return result;
        }
        statusBoard[y][x] = FLAGGED;
        checkRep();
        return result;
        
    }
    /**
     * deflags the square at (x, y)
     * @param x x-coordinate of square, requires 0 <= x <= width
     * @param y y-coordinate of square, requires 0 <= y <= height
     * @return string flag to indicate steps for MinesweeperServer class
     */
    public synchronized String deflag(int x, int y) {
        // not flagged
        String result = "BOARD";
        if(outOfBounds(x, y) || !statusBoard[y][x].equals(FLAGGED)) {
            return result;
        }
        statusBoard[y][x] = UNTOUCHED;
        checkRep();
        return result;
        
    }
    
    private boolean outOfBounds(int x, int y) {
        return (x < 0) || (x >= statusBoard[0].length) || (y < 0) || (y >= statusBoard.length);
    }
    
}
