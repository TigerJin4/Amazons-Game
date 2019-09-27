package amazons;
import org.junit.Test;
import static org.junit.Assert.*;
import ucb.junit.textui;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import static amazons.Square.sq;


/** Junit tests for our Board iterators.
 *  @author
 */
public class IteratorTests {

    /** Run the JUnit tests in this package. */
    public static void main(String[] ignored) {
        textui.runClasses(IteratorTests.class);
    }

    /** Tests reachableFromIterator to make sure it returns all reachable
     *  Squares. This method may need to be changed based on
     *   your implementation. */
    @Test
    public void testReachableFrom() {
        Board b = new Board();
        buildBoard(b, REACHABLEFROMTESTBOARD);
        int numSquares = 0;
        Set<Square> squares = new HashSet<>();
        Iterator<Square> reachableFrom = b.reachableFrom(Square.sq(5, 4), null);
        while (reachableFrom.hasNext()) {
            Square s = reachableFrom.next();
            System.out.print(s);
            System.out.println(REACHABLEFROMTESTSQUARES.contains(s));
            assertTrue(REACHABLEFROMTESTSQUARES.contains(s));
            numSquares += 1;
            squares.add(s);
        }
        assertEquals(REACHABLEFROMTESTSQUARES.size(), numSquares);
        assertEquals(REACHABLEFROMTESTSQUARES.size(), squares.size());
    }



    @Test
    public void testReachableFrom1() {
        Board b = new Board();
        buildBoard(b, LEGALMOVETESTBOARD);
        int numSquares = 0;
        Set<Square> squares = new HashSet<>();
        Iterator<Square> reachableFrom = b.reachableFrom(Square.sq(0, 9), null);
        while (reachableFrom.hasNext()) {
            Square s = reachableFrom.next();
            System.out.print(s);
            numSquares += 1;
            squares.add(s);
        }
    }
    /** Tests legalMovesIterator to make sure it returns all legal Moves.
     *  This method needs to be finished and may need to be changed
     *  based on your implementation. */
    @Test
    public void test1() {
        Board b = new Board();
        buildBoard(b, LEGALMOVETESTBOARD);
        System.out.println(b.isUnblockedMove(sq("a10"), sq("b9"), null));
    }

    @Test
    public void testLegalMoves() {
        Board b = new Board();
        buildBoard(b, LEGALMOVETESTBOARD);
        int numMoves = 0;
        Set<Move> moves = new HashSet<>();
        Iterator<Move> legalMoves = b.legalMoves(Piece.WHITE);
        while (legalMoves.hasNext()) {
            Move m = legalMoves.next();
            System.out.println(m);
            assertTrue(LEGALMOVETESTMOVES.contains(m));
            numMoves += 1;
            moves.add(m);
        }
        assertEquals(LEGALMOVETESTMOVES.size(), numMoves);
        assertEquals(LEGALMOVETESTMOVES.size(), moves.size());
    }

    @Test
    public void testLegalMoves2() {
        Board b = new Board();
        buildBoard(b, LEGALMOVETESTBOARD2);
        int numMoves = 0;
        Set<Move> moves = new HashSet<>();
        Iterator<Move> legalMoves = b.legalMoves(Piece.WHITE);
        while (legalMoves.hasNext()) {
            Move m = legalMoves.next();
            System.out.println(m);
            numMoves += 1;
            moves.add(m);
        }
    }

    @Test
    public void testMoves() {
        Board b = new Board();
        buildBoard(b, REACHABLEFROMTESTBOARD);
        int numMoves = 0;
        Iterator<Move> legalMoves = b.legalMoves(Piece.WHITE);
        while (numMoves < 8 && legalMoves.hasNext()) {
            Move m = legalMoves.next();
            System.out.println(m);
            b.makeMove(m);
            System.out.println(b);
            b.undo();
            System.out.println(b);
            numMoves += 1;
        }
    }

    @Test
    public void checkUndo2() {
        Board b = new Board();
        Move oneMove = Move.mv(Square.sq(0, 3),
                Square.sq(1, 3), Square.sq(0, 3));
        b.makeMove(oneMove);
        b.undo();
        System.out.println(b);
    }


    private void buildBoard(Board b, Piece[][] target) {
        for (int col = 0; col < Board.SIZE; col++) {
            for (int row = Board.SIZE - 1; row >= 0; row--) {
                Piece piece = target[Board.SIZE - row - 1][col];
                b.put(piece, Square.sq(col, row));
            }
        }
        System.out.println(b);
    }

    static final Piece E = Piece.EMPTY;

    static final Piece W = Piece.WHITE;

    static final Piece B = Piece.BLACK;

    static final Piece S = Piece.SPEAR;

    static final Piece[][] REACHABLEFROMTESTBOARD =
        {
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, W, W },
            { E, E, E, E, E, E, E, S, E, S },
            { E, E, E, S, S, S, S, E, E, S },
            { E, E, E, S, E, E, E, E, B, E },
            { E, E, E, S, E, W, E, E, B, E },
            { E, E, E, S, S, S, B, W, B, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
        };

    static final Set<Square> REACHABLEFROMTESTSQUARES =
            new HashSet<>(Arrays.asList(
                    Square.sq(5, 5),
                    Square.sq(4, 5),
                    Square.sq(4, 4),
                    Square.sq(6, 4),
                    Square.sq(7, 4),
                    Square.sq(6, 5),
                    Square.sq(7, 6),
                    Square.sq(8, 7)));

    static final Piece[][] LEGALMOVETESTBOARD =
        {
            { W, S, S, E, E, E, E, S, S, E },
            { E, S, E, E, E, E, E, S, W, S },
            { E, S, E, E, E, E, E, S, S, S },
            { S, S, E, S, S, S, S, E, E, S },
            { S, S, E, S, S, S, S, E, B, E },
            { S, S, E, S, S, S, S, E, B, E },
            { S, S, E, S, S, S, B, S, B, E },
            { S, S, E, E, E, E, E, E, E, E },
            { S, S, S, S, E, E, S, S, S, E },
            { E, W, E, S, E, E, S, W, S, E },
        };

    static final Piece[][] LEGALMOVETESTBOARD2 =
        {
            { E, W, S, S, S, S, S, S, S, S },
            { S, S, W, S, W, S, S, S, W, S },
            { S, S, W, W, S, S, S, W, S, S },
            { S, S, S, S, S, S, S, S, S, S },
            { S, S, S, S, S, S, S, S, B, B },
            { S, S, S, S, S, S, S, S, B, S },
            { S, S, S, S, S, S, B, S, B, S },
            { S, S, S, S, S, S, S, S, S, S },
            { S, S, S, S, S, S, S, S, S, S },
            { S, S, S, S, S, S, S, W, S, S },
        };

    static final Set<Move> LEGALMOVETESTMOVES =
            new HashSet<>(Arrays.asList(
                    Move.mv(sq(0, 9), sq(0, 8), sq(0, 9)),
                    Move.mv(sq(0, 9), sq(0, 8), sq(0, 7)),
                    Move.mv(sq(0, 9), sq(0, 7), sq(0, 9)),
                    Move.mv(sq(0, 9), sq(0, 7), sq(0, 8)),
                    Move.mv(sq(1, 0), sq(0, 0), sq(1, 0)),
                    Move.mv(sq(1, 0), sq(2, 0), sq(1, 0)),
                    Move.mv(sq(1, 0), sq(2, 0), sq(0, 0)),
                    Move.mv(sq(1, 0), sq(0, 0), sq(2, 0)),
                    Move.mv(sq(8, 8), sq(9, 9), sq(8, 8))));

}
