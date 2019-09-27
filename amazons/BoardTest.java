package amazons;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

import static amazons.Piece.*;
import static amazons.Move.mv;
import static amazons.Square.*;

public class BoardTest {

    private static List<Square> initalWhites =
            Arrays.asList(sq("d1"), sq("g1"), sq("a4"), sq("j4"));
    private static List<Square> initalBlacks =
            Arrays.asList(sq("d10"), sq("g10"), sq("a7"), sq("j7"));
    /** A board model used for testing,
     * in the status represent by the printout below,
     * ===
     *    - - - B - - B - - -
     *    - - - - - - - - - -
     *    S S S - S S S S S S
     *    - - B S - - - - - B
     *    - - - - - - - - - -
     *    - - - - - - - - - -
     *    W - - - - - - - - W
     *    - - - - - - S - - -
     *    - - - - - - - - - -
     *    - - - W - - W - - -
     * ===
     * */
    private static final Board TESTMODEL1 = new Board();
    /** A board model used for testing,
     * in the status represent by the printout below,
     * ===
     *    S S S B S S B - - S
     *    B S - S - S S S S S
     *    - S S S S S S S - S
     *    S S S S S S S S - B
     *    S S S S S S S S S S
     *    - - - - - - - - - -
     *    W - - - - - - - - W
     *    - - - - - - - - - -
     *    - - - - - - - - - -
     *    - - - W - - W - - -
     * ===
     * */
    private static final Board TESTMODEL2 = new Board();
    private static final String INIT_BOARD_STATE = String.format(
                    "   - - - B - - B - - -%n"
                            + "   - - - - - - - - - -%n"
                            + "   - - - - - - - - - -%n"
                            + "   B - - - - - - - - B%n"
                            + "   - - - - - - - - - -%n"
                            + "   - - - - - - - - - -%n"
                            + "   W - - - - - - - - W%n"
                            + "   - - - - - - - - - -%n"
                            + "   - - - - - - - - - -%n"
                            + "   - - - W - - W - - -%n");

    private static final String SMILE = String.format(
                    "   - - - - - - - - - -%n"
                            + "   - S S S - - S S S -%n"
                            + "   - S - S - - S - S -%n"
                            + "   - S S S - - S S S -%n"
                            + "   - - - - - - - - - -%n"
                            + "   - - - - - - - - - -%n"
                            + "   - - W - - - - W - -%n"
                            + "   - - - W W W W - - -%n"
                            + "   - - - - - - - - - -%n"
                            + "   - - - - - - - - - -%n");
    static {
        TESTMODEL1.put(SPEAR, sq("d7"));
        TESTMODEL1.put(SPEAR, sq("g3"));
        TESTMODEL1.put(EMPTY, sq("a7"));
        TESTMODEL1.put(BLACK, sq("c7"));
        for (int i = 70; i < 80; i++) {
            TESTMODEL1.put(SPEAR, sq(i));
        }
        TESTMODEL1.put(EMPTY, sq("d8"));
        for (int i = 50; i <= 99; i++) {
            TESTMODEL2.put(SPEAR, sq(i));
        }
        TESTMODEL2.put(BLACK, sq("d10"));
        TESTMODEL2.put(BLACK, sq("g10"));
        TESTMODEL2.put(BLACK, sq("a9"));
        TESTMODEL2.put(BLACK, sq("j7"));
        TESTMODEL2.put(EMPTY, sq("c9"));
        TESTMODEL2.put(EMPTY, sq("e9"));
        TESTMODEL2.put(EMPTY, sq("h10"));
        TESTMODEL2.put(EMPTY, sq("i10"));
        TESTMODEL2.put(EMPTY, sq("a8"));
        TESTMODEL2.put(EMPTY, sq("i7"));
        TESTMODEL2.put(EMPTY, sq("i8"));
    }

    private void makeSmile(Board b) {
        b.put(EMPTY, Square.sq(0, 3));
        b.put(EMPTY, Square.sq(0, 6));
        b.put(EMPTY, Square.sq(9, 3));
        b.put(EMPTY, Square.sq(9, 6));
        b.put(EMPTY, Square.sq(3, 0));
        b.put(EMPTY, Square.sq(3, 9));
        b.put(EMPTY, Square.sq(6, 0));
        b.put(EMPTY, Square.sq(6, 9));
        for (int col = 1; col < 4; col += 1) {
            for (int row = 6; row < 9; row += 1) {
                b.put(SPEAR, Square.sq(col, row));
            }
        }
        b.put(EMPTY, Square.sq(2, 7));
        for (int col = 6; col < 9; col += 1) {
            for (int row = 6; row < 9; row += 1) {
                b.put(SPEAR, Square.sq(col, row));
            }
        }
        b.put(EMPTY, Square.sq(7, 7));
        for (int lip = 3; lip < 7; lip += 1) {
            b.put(WHITE, Square.sq(lip, 2));
        }
        b.put(WHITE, Square.sq(2, 3));
        b.put(WHITE, Square.sq(7, 3));
    }

    @Test
    public void testInit() {
        Board test = new Board();

        assertEquals(WHITE, test.turn());

        assertEquals(WHITE, test.get(sq("d1")));
        assertEquals(WHITE, test.get('g', '1'));
        assertEquals(WHITE, test.get(0, 3));
        assertEquals(WHITE, test.get(sq("j4")));

        for (int i = 0; i < 100; i++) {
            if (initalWhites.contains(sq(i))) {
                assertEquals(WHITE, test.get(sq(i)));
            } else if (initalBlacks.contains(sq(i))) {
                assertEquals(BLACK, test.get(sq(i)));
            } else {
                assertEquals(EMPTY, test.get(sq(1)));
            }
        }
    }

    @Test
    public void testCopy() {
        Board model = new Board();
        model.put(SPEAR, sq("f9"));
        model.put(SPEAR, sq(3, 6));
        model.put(SPEAR, sq("d", "5"));
        Board test = new Board(model);

        assertEquals(SPEAR, test.get(sq("f9")));
        assertEquals(SPEAR, test.get(sq("d7")));
        assertEquals(SPEAR, test.get(sq("d5")));

        model.put(EMPTY, sq("f9"));
        assertEquals(SPEAR, test.get(sq("f9")));
    }

    @Test
    public void testUnbloackedMove() {
        Board test1 = new Board(TESTMODEL1);

        assertTrue(test1.isUnblockedMove(sq("d1"), sq("d5"), null));
        assertTrue(test1.isUnblockedMove(sq("d1"), sq("d9"), sq("d7")));
        assertTrue(test1.isUnblockedMove(sq("d1"), sq("f3"), null));

        assertFalse(test1.isUnblockedMove(sq("a4"), sq("d5"), null));
        assertFalse(test1.isUnblockedMove(sq("a4"), sq("e8"), null));
    }

    @Test
    public void testIsLegal() {
        Board test1 = new Board(TESTMODEL1);

        assertTrue(test1.isLegal(sq("a4")));
        assertFalse(test1.isLegal(sq("a3")));
        assertFalse(test1.isLegal(sq("d10")));

        assertTrue(test1.isLegal(sq("a4"), sq("e4"), null));
        assertTrue(test1.isLegal(sq("a4"), sq("c6"), null));
        assertFalse(test1.isLegal(sq("a4"), sq("j4"), null));
        assertFalse(test1.isLegal(sq("b4"), sq("e4"), null));

        assertFalse(test1.isLegal(sq("a4"), sq("f10"), null));
        assertFalse(test1.isLegal(sq("a4"), sq("f10"), null));

        assertTrue(test1.isLegal(sq("d1"), sq("d5"), sq("d1"), null));
        assertTrue(test1.isLegal(sq("d1"), sq("d3"), sq("f3"), null));
        assertFalse(test1.isLegal(sq("d1"), sq("e3"), sq("d1"), null));
        assertFalse(test1.isLegal(sq("d1"), sq("d3"), sq("f2"), null));
        assertFalse(test1.isLegal(sq("d1"), sq("d3"), sq("h3"), null));

        assertTrue(test1.isLegal(mv(sq("d1"), sq("d5"), sq("d1")), null));
        assertTrue(test1.isLegal(mv(sq("d1"), sq("d3"), sq("f3")), null));
        assertFalse(test1.isLegal(mv(sq("d1"), sq("e3"), sq("d1")), null));
        assertFalse(test1.isLegal(mv(sq("d1"), sq("d3"), sq("f2")), null));
        assertFalse(test1.isLegal(mv(sq("d1"), sq("d3"), sq("h3")), null));
    }

    @Test
    public void testMakeMove() {
        Board test = new Board(TESTMODEL1);

        test.makeMove(sq("d1"), sq("d5"), sq("d1"));
        assertEquals(SPEAR, test.get(sq("d1")));
        assertEquals(WHITE, test.get(sq("d5")));
        assertFalse(test.isLegal(sq("d5"), sq("d3"), sq("d2"), null));

        assertTrue(test.isLegal(mv(sq("c7"), sq("c6"), sq("e6")), null));
        test.makeMove(mv(sq("c7"), sq("c6"), sq("e6")));
        assertEquals(EMPTY, test.get(sq("c7")));
        assertEquals(BLACK, test.get(sq("c6")));
        assertEquals(SPEAR, test.get(sq("e6")));
        assertFalse(test.isLegal(sq("c5"), sq("c3"), sq("c1"), null));
        assertEquals(2, test.numMoves());

        test.undo();
        assertEquals(1, test.numMoves());
        assertEquals(BLACK, test.get(sq("c7")));
        assertEquals(EMPTY, test.get(sq("c6")));
        assertEquals(EMPTY, test.get(sq("e6")));
        assertTrue(test.isLegal(mv(sq("c7"), sq("c6"), sq("e6")), null));
    }

    @Test
    public void testReachableFrom() {
        Board test = new Board(TESTMODEL1);
        HashSet<Square> expected = new HashSet<>();
        HashSet<Square> actual = new HashSet<>();
        for (int i = 82; i <= 84; i++) {
            expected.add(sq(i));
        }
        for (int i = 90; i <= 99; i++) {
            expected.add(sq(i));
        }
        expected.remove(sq(93));
        expected.add(sq(73));
        Iterator<Square> iter = test.reachableFrom(sq("d10"), sq("g10"));
        while (iter.hasNext()) {
            actual.add(iter.next());
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testLegalMoves() {
        Board test = new Board(TESTMODEL2);
        HashSet<Move> expected = new HashSet<>();
        HashSet<Move> actual = new HashSet<>();
        expected.add(mv(sq("a9"), sq("a8"), sq("a9")));
        expected.add(mv(sq("d10"), sq("c9"), sq("d10")));
        expected.add(mv(sq("d10"), sq("e9"), sq("d10")));
        expected.add(mv(sq("g10"), sq("h10"), sq("g10")));
        expected.add(mv(sq("g10"), sq("h10"), sq("i10")));
        expected.add(mv(sq("g10"), sq("i10"), sq("g10")));
        expected.add(mv(sq("g10"), sq("i10"), sq("h10")));
        expected.add(mv(sq("j7"), sq("i7"), sq("j7")));
        expected.add(mv(sq("j7"), sq("i7"), sq("i8")));
        expected.add(mv(sq("j7"), sq("i8"), sq("j7")));
        expected.add(mv(sq("j7"), sq("i8"), sq("i7")));
        Iterator<Move> iter = test.legalMoves(BLACK);
        while (iter.hasNext()) {
            actual.add(iter.next());
        }
        assertEquals(expected, actual);
    }

    /** Tests basic correctness of put and get on the initialized board. */
    @Test
    public void testBasicPutGet() {
        Board b = new Board();
        b.put(BLACK, Square.sq(3, 5));
        assertEquals(b.get(3, 5), BLACK);
        b.put(WHITE, Square.sq(9, 9));
        assertEquals(b.get(9, 9), WHITE);
        b.put(EMPTY, Square.sq(3, 5));
        assertEquals(b.get(3, 5), EMPTY);
    }

    /** Tests toString for initial board state and a smiling board state. :) */
    @Test
    public void testToString() {
        Board b = new Board();
        assertEquals(INIT_BOARD_STATE, b.toString());
        makeSmile(b);
        assertEquals(SMILE, b.toString());
    }
    @Test
    public void isLegalTest() {
        Board b = new Board();
        Move move = mv(Square.sq(3, 0), Square.sq(3, 1), Square.sq(3, 0));
        System.out.println(b.isLegal(move, null));
    }
}
