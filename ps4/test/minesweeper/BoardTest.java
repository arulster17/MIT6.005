/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;

/**
 * TODO: Description
 */
public class BoardTest {
    
    // TODO: Testing strategy
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    // TODO: Tests
    @Test
    public void InitTests() {
        Board initBoard = new Board(5, 7);
        
        String boardState = 
                  "- - - - -\n"
                + "- - - - -\n"
                + "- - - - -\n"
                + "- - - - -\n"
                + "- - - - -\n"
                + "- - - - -\n"
                + "- - - - -";
        assertEquals("default board initialization failed", boardState, initBoard.look());
        File f = new File("./test/minesweeper/boardtest.txt");
        Board board = new Board(f);
        
        //System.out.println(board.bombBoard[0][0]);

        board.dig(-1, 1);
        board.dig(1, -1);
        board.dig(-1, -1);
        board.dig(1, 7);
        board.dig(5, 1);
        board.dig(5, 7);

        board.flag(-1, 1);
        board.flag(1, -1);
        board.flag(-1, -1);
        board.flag(1, 7);
        board.flag(5, 1);
        board.flag(5, 7);
        
        board.deflag(-1, 1);
        board.deflag(1, -1);
        board.deflag(-1, -1);
        board.deflag(1, 7);
        board.deflag(5, 1);
        board.deflag(5, 7);
        
        assertEquals("out of bounds commands are failing", boardState, board.look());
        board.flag(0, 0);
        board.flag(1, 0);
        board.flag(2, 0);
        board.flag(0, 1);
        board.flag(2, 1);
        board.flag(0, 2);
        board.flag(1, 2);
        board.flag(2, 2);
        
        boardState = 
                  "F F F - -\n"
                + "F - F - -\n"
                + "F F F - -\n"
                + "- - - - -\n"
                + "- - - - -\n"
                + "- - - - -\n"
                + "- - - - -";
        assertEquals("flagging failed", boardState, board.look());
        
        board.dig(4, 5);
        //assume 1 bomb in center and 1 above SW corner
        boardState = 
                  "F F F    \n"
                + "F - F    \n"
                + "F F F 1  \n"
                + "- - - 1  \n"
                + "- 2 1 1  \n"
                + "- 1      \n"
                + "- 1      ";
        
        
        assertEquals("dig expansion failed", boardState, board.look());
        
        
        board.deflag(0, 0);
        board.deflag(1, 0);
        board.deflag(2, 0);
        board.deflag(0, 1);
        board.deflag(2, 1);
        board.deflag(0, 2);
        board.deflag(1, 2);
        board.deflag(2, 2);
        boardState = 
                "- - -    \n"
              + "- - -    \n"
              + "- - - 1  \n"
              + "- - - 1  \n"
              + "- 2 1 1  \n"
              + "- 1      \n"
              + "- 1      ";
        
        assertEquals("deflagging failed", boardState, board.look());
        board.dig(0, 0);
        boardState = 
                "         \n"
              + "         \n"
              + "  1 1 1  \n"
              + "  1 - 1  \n"
              + "1 2 1 1  \n"
              + "- 1      \n"
              + "- 1      ";
        assertEquals("second dig expansion failed", boardState, board.look());
        
        board.dig(2, 3);
        
        boardState = 
                "         \n"
              + "         \n"
              + "         \n"
              + "         \n"
              + "1 1      \n"
              + "- 1      \n"
              + "- 1      ";
        //System.out.println(board.look());
        assertEquals("digging bomb did not process properly", boardState, board.look());
        
        
    }
    
}
