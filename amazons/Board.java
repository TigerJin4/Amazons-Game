package amazons;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;

import static amazons.Piece.WHITE;
import static amazons.Piece.BLACK;
import static amazons.Piece.EMPTY;
import static amazons.Piece.SPEAR;
import static amazons.Move.mv;


/** The state of an Amazons Game.
 *  @author Wenhan Jin
 */
class Board {

    /** The number of squares on a side of the board. */
    static final int SIZE = 10;

    /** Board is represented by a Hashmap with square as key
     * and piece as item. */
    private HashMap<Square, Piece> board = new HashMap<>();

    /** numMove keeps track of the number of move of this board. */
    private int numMove = 0;

    /** moveRecord keeps track of the moves of this board. */
    private ArrayList<Square[]> moverecord = new ArrayList<>();

    /** Initializes a game board with SIZE squares on a side in the
     *  initial position. */
    Board() {
        init();
    }

    /** Initializes a copy of MODEL. */
    Board(Board model) {
        copy(model);
    }

    /** Copies MODEL into me. */
    void copy(Board model) {
        board = new HashMap<>();
        for (Square key : model.board.keySet()) {
            Piece piece = model.board.get(key);
            board.put(key, piece);
        }
        _turn = model._turn;
        numMove = model.numMove;
        moverecord = model.moverecord;
        _winner = model._winner;
    }
    /** W1 coord. */
    static final int W1 = 3;
    /** W2 coord. */
    static final int W2 = 6;
    /** W3 coord. */
    static final int W3 = 30;
    /** W4 coord. */
    static final int W4 = 39;
    /** B1 coord. */
    static final int B1 = 60;
    /** B2 coord. */
    static final int B2 = 69;
    /** B3 coord. */
    static final int B3 = 93;
    /** B4 coord. */
    static final int B4 = 96;

    /** Clears the board to the initial position. */
    void init() {
        for (int i = 0; i < SIZE * SIZE; i++) {
            if (i == W1 || i == W2 || i == W3 || i == W4) {
                board.put(Square.sq(i), WHITE);
            } else if (i == B1 || i == B2 || i == B3 || i == B4) {
                board.put(Square.sq(i), BLACK);
            } else {
                board.put(Square.sq(i), EMPTY);
            }
        }
        _turn = WHITE;
        numMove = 0;
        _winner = EMPTY;
        moverecord = new ArrayList<>();
    }

    /** Return the Piece whose move it is (WHITE or BLACK). */
    Piece turn() {
        return _turn;
    }

    /** Return the number of moves (that have not been undone) for this
     *  board. */
    int numMoves() {
        return numMove;
    }

    /** Return the winner in the current position, or null if the game is
     *  not yet finished. */
    Piece winner() {
        Iterator<Move> legal = legalMoves(_turn);
        if (!legal.hasNext()) {
            return _turn.opponent();
        } else {
            return null;
        }
    }

    /** Return the hashmap of this board. */
    final HashMap<Square, Piece> get() {
        return board;
    }

    /** Return the contents the square at S. */
    final Piece get(Square s) {
        return board.get(s);
    }

    /** Return the contents of the square at (COL, ROW), where
     *  0 <= COL, ROW <= 9. */
    final Piece get(int col, int row) {
        return get(Square.sq(col, row));
    }

    /** Return the contents of the square at COL ROW. */
    final Piece get(char col, char row) {
        return get(col - 'a', row - '1');
    }

    /** Set square S to P. */
    final void put(Piece p, Square s) {
        board.put(s, p);
    }

    /** Set square (COL, ROW) to P. */
    final void put(Piece p, int col, int row) {
        put(p, Square.sq(col, row));
        _winner = EMPTY;
    }

    /** Set square COL ROW to P. */
    final void put(Piece p, char col, char row) {
        put(p, col - 'a', row - '1');
    }

    /** Return true iff FROM - TO is an unblocked queen move on the current
     *  board, ignoring the contents of ASEMPTY, if it is encountered.
     *  For this to be true, FROM-TO must be a queen move and the
     *  squares along it, other than FROM and ASEMPTY, must be
     *  empty. ASEMPTY may be null, in which case it has no effect. */
    boolean isUnblockedMove(Square from, Square to, Square asEmpty) {
        if (from.isQueenMove(to) && (board.get(to) == EMPTY || to == asEmpty)) {
            int dir = from.direction(to);
            for (int i = 1;
                 i <= Math.max((Math.abs(from.col() - to.col())),
                         Math.abs(from.row() - to.row()));
                 i++) {
                if (board.get(from.queenMove(dir, i)) != EMPTY
                        && from.queenMove(dir, i) != asEmpty) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /** Return true iff FROM is a valid starting square for a move. */
    boolean isLegal(Square from) {
        return board.get(from) == _turn;
    }

    /** Return true iff FROM-TO (ASEMPTY) is a valid first
     * part of move, ignoring spear throwing. */
    boolean isLegal(Square from, Square to, Square asEmpty) {
        if (asEmpty != null) {
            return isLegal(from)
                    && (board.get(to) == EMPTY || to == asEmpty)
                    && isUnblockedMove(from, to, asEmpty);
        } else {
            return isLegal(from)
                    && (board.get(to) == EMPTY)
                    && isUnblockedMove(from, to, asEmpty);
        }
    }

    /** Return true iff FROM-TO(SPEAR) (ASEMPTY) is a legal move in the current
     *  position. */
    boolean isLegal(Square from, Square to, Square spear, Square asEmpty) {
        return isLegal(from, to, asEmpty)
                && ((board.get(to) == EMPTY) || (spear == asEmpty))
                && isUnblockedMove(to, spear, from);
    }

    /** Return true iff MOVE (ASEMPTY) is a legal move in the current
     *  position. */
    boolean isLegal(Move move, Square asEmpty) {
        return isLegal(move.from(), move.to(), move.spear(), asEmpty);
    }

    /** Move FROM-TO(SPEAR), assuming this is a legal move. */
    void makeMove(Square from, Square to, Square spear) {
        Square[] prev = {from, to, spear};
        moverecord.add(prev);
        Piece queen = get(from);
        board.put(from, EMPTY);
        board.put(to, queen);
        board.put(spear, SPEAR);
        numMove++;
        _turn = _turn.opponent();
    }

    /** Move according to MOVE, assuming it is a legal move. */
    void makeMove(Move move) {
        makeMove(move.from(), move.to(), move.spear());
    }

    /** Undo one move.  Has no effect on the initial board. */
    void undo() {
        if (moverecord.size() > 0) {
            Piece queen = get(moverecord.get(numMove - 1)[1]);
            board.put(moverecord.get(numMove - 1)[2], EMPTY);
            board.put(moverecord.get(numMove - 1)[1], EMPTY);
            board.put(moverecord.get(numMove - 1)[0], queen);
            moverecord.remove(numMove - 1);
            numMove--;
            _turn = _turn.opponent();
        }
    }

    /** Return an Iterator over the Squares that are reachable by an
     *  unblocked queen move from FROM. Does not pay attention to what
     *  piece (if any) is on FROM, nor to whether the game is finished.
     *  Treats square ASEMPTY (if non-null) as if it were EMPTY.  (This
     *  feature is useful when looking for Moves, because after moving a
     *  piece, one wants to treat the Square it came from as empty for
     *  purposes of spear throwing.) */
    Iterator<Square> reachableFrom(Square from, Square asEmpty) {
        return new ReachableFromIterator(from, asEmpty);
    }

    /** Return an Iterator over all legal moves on the current board. */
    Iterator<Move> legalMoves() {
        return new LegalMoveIterator(_turn);
    }

    /** Return an Iterator over all legal moves on the current board for
     *  SIDE (regardless of whose turn it is). */
    Iterator<Move> legalMoves(Piece side) {
        return new LegalMoveIterator(side);
    }

    /** An iterator used by reachableFrom. */
    private class ReachableFromIterator implements Iterator<Square> {

        /** Iterator of all squares reachable by queen move from FROM,
         *  treating ASEMPTY as empty. */
        ReachableFromIterator(Square from, Square asEmpty) {
            _from = from;
            _dir = 0;
            _steps = 1;
            _asEmpty = asEmpty;
            toNext();
        }

        @Override
        public boolean hasNext() {
            return _dir < 8;
        }


        @Override
        public Square next() {
            Square s = _from.queenMove(_dir, _steps);
            _steps++;
            toNext();
            return s;
        }

        /** Advance _dir and _steps, so that the next valid Square is
         *  _steps steps whilein direction _dir from _from. */
        private void toNext() {
            while (!isUnblockedMove(_from,
                    _from.queenMove(_dir, _steps), _asEmpty)) {
                _dir++;
                _steps = 1;
                if (_dir == 8) {
                    break;
                }
            }
        }

        /** Starting square. */
        private Square _from;
        /** Current direction. */
        private int _dir;
        /** Current distance. */
        private int _steps;
        /** Square treated as empty. */
        private Square _asEmpty;
    }

    /** An iterator used by legalMoves. */
    private class LegalMoveIterator implements Iterator<Move> {

        /** Number of legal moves. */
        private int num = 0;

        /** NUM returns the variable num. */
        public int num() {
            return num;
        }

        /** All legal moves for SIDE (WHITE or BLACK). */
        LegalMoveIterator(Piece side) {
            _startingSquares = Square.iterator();
            _spearThrows = NO_SQUARES;
            _pieceMoves = NO_SQUARES;
            _fromPiece = side;
            if (_startingSquares.hasNext()) {
                Square s = _startingSquares.next();
                while (board.get(s) != _fromPiece
                        && _startingSquares.hasNext()) {
                    s = _startingSquares.next();
                }
                _start = s;
                _pieceMoves = new ReachableFromIterator(_start, null);
            }
        }

        @Override
        public boolean hasNext() {
            if (!_spearThrows.hasNext()) {
                if (!_pieceMoves.hasNext()) {
                    if (!_startingSquares.hasNext()) {
                        return false;
                    } else {
                        num = num + 1;
                        toNext();
                        return hasNext();
                    }
                } else {
                    toNext();
                    return hasNext();
                }
            }
            _next = mv(_start, _nextSquare, _spearThrows.next());
            return true;
        }

        @Override
        public Move next() {
            return _next;
        }

        /** Advance so that the next valid Move is
         *  _start-_nextSquare(sp), where sp is the next value of
         *  _spearThrows. */
        private void toNext() {
            if (_pieceMoves.hasNext()) {
                _nextSquare = _pieceMoves.next();
                _spearThrows = new ReachableFromIterator(_nextSquare, _start);
                if (!_spearThrows.hasNext()) {
                    toNext();
                }
            } else if (_startingSquares.hasNext()) {
                Square s = _startingSquares.next();
                while (board.get(s) != _fromPiece
                        && _startingSquares.hasNext()) {
                    s = _startingSquares.next();
                }
                if (board.get(s) == _fromPiece) {
                    _start = s;
                    _pieceMoves = new ReachableFromIterator(_start, _start);
                    if (!_pieceMoves.hasNext()) {
                        toNext();
                    } else {
                        _nextSquare = _pieceMoves.next();
                        _spearThrows =
                                new ReachableFromIterator(_nextSquare, _start);
                    }
                }
            }
        }

        /** Color of side whose moves we are iterating. */
        private Piece _fromPiece;
        /** Current starting square. */
        private Square _start;
        /** Remaining starting squares to consider. */
        private Iterator<Square> _startingSquares;
        /** Current piece's new position. */
        private Square _nextSquare;
        /** Remaining moves from _start to consider. */
        private Iterator<Square> _pieceMoves;
        /** Remaining spear throws from _piece to consider. */
        private Iterator<Square> _spearThrows;
        /** Record of the next move. */
        private Move _next;
    }

    @Override
    public String toString() {
        String result = "";
        for (int i = 9; i > -1; i--) {
            String row = "  ";
            for (int j = 0; j < 10; j++) {
                Square cell = Square.sq(j, i);
                row += " " + board.get(cell).toString();
            }
            result += row + "\n";
        }
        return result;
    }

    /** An empty iterator for initialization. */
    private static final Iterator<Square> NO_SQUARES =
        Collections.emptyIterator();

    /** Piece whose turn it is (BLACK or WHITE). */
    private Piece _turn;
    /** Cached value of winner on this board, or EMPTY if it has not been
     *  computed. */
    private Piece _winner;
}
