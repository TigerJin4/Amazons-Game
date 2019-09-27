package amazons;

import java.util.Iterator;

import static amazons.Piece.*;


/** A Player that automatically generates moves.
 *  @author Wenhan Jin
 */
class AI extends Player {

    /** A position magnitude indicating a win (for white if positive, black
     *  if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 1;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new AI with no piece or controller (intended to produce
     *  a template). */
    AI() {
        this(null, null);
    }

    /** A new AI playing PIECE under control of CONTROLLER. */
    AI(Piece piece, Controller controller) {
        super(piece, controller);
    }

    @Override
    Player create(Piece piece, Controller controller) {
        return new AI(piece, controller);
    }

    @Override
    String myMove() {
        Move move = findMove();
        if (move == null) {
            return "null";
        } else {
            _controller.reportMove(move);
            return move.toString();
        }
    }

    /** Return a move for me from the current position, assuming there
     *  is a move. */
    private Move findMove() {
        Board b = new Board(board());
        if (_myPiece == WHITE) {
            findMove(b, maxDepth(b), true, 1, -INFTY, INFTY);
        } else {
            findMove(b, maxDepth(b), true, -1, -INFTY, INFTY);
        }
        return _lastFoundMove;
    }

    /** The move found by the last call to one of the ...FindMove methods
     *  below. */
    private Move _lastFoundMove;

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _lastFoundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _lastMoveFound. */
    private int findMove(Board board, int depth, boolean saveMove, int sense,
                         int alpha, int beta) {
        if (depth == 0 || board.winner() != null) {
            return staticScore(board);
        } else if (sense == 1) {
            Move bestmove = null;
            int bestval = -INFTY;
            Iterator<Move> legal = board.legalMoves(WHITE);
            while (legal.hasNext()) {
                Move m = legal.next();
                board.makeMove(m);
                int respondingScore = findMove(board,
                        depth - 1, false, -1, alpha, beta);
                board.undo();
                if (respondingScore > bestval) {
                    bestmove = m;
                    bestval = respondingScore;
                    alpha = Math.max(alpha, bestval);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            if (saveMove) {
                _lastFoundMove = bestmove;
            }
            if (bestmove == null) {
                return -WINNING_VALUE;
            }
            return bestval;
        } else {
            Move bestmove = null;
            int bestval = INFTY;
            Iterator<Move> legal = board.legalMoves(BLACK);
            while (legal.hasNext()) {
                Move m = legal.next();
                board.makeMove(m);
                int respondingScore = findMove(board,
                        depth - 1, false, 1, alpha, beta);
                board.undo();
                if (respondingScore < bestval) {
                    bestmove = m;
                    bestval = respondingScore;
                    alpha = Math.min(alpha, bestval);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            if (saveMove) {
                _lastFoundMove = bestmove;
            }
            if (bestmove == null) {
                return WINNING_VALUE;
            }
            return bestval;
        }
    }

    /** Number of moves that can result in the same basket of depth.  */
    private static final int DIVISOR = 30;

    /** The initial depth of the game tree.  */
    private static final int INITIAL = 1;
    /** Return a heuristically determined maximum search depth
     *  based on characteristics of BOARD. */
    private int maxDepth(Board board) {
        int N = board.numMoves();
        return N / DIVISOR + INITIAL;
    }

    /** Return a heuristic value for BOARD. */
    private int staticScore(Board board) {
        Piece winner = board.winner();
        if (winner == BLACK) {
            return -WINNING_VALUE;
        } else if (winner == WHITE) {
            return WINNING_VALUE;
        } else {
            int positivePoint = 0;
            int negativePoint = 0;
            for (Square s : board.get().keySet()) {
                if (board.get(s) == WHITE) {
                    Iterator<Square> reachable = board.reachableFrom(s, s);
                    while (reachable.hasNext()) {
                        positivePoint++;
                        reachable.next();
                    }
                }
            }
            for (Square s : board.get().keySet()) {
                if (board.get(s) == BLACK) {
                    Iterator<Square> reachable = board.reachableFrom(s, s);
                    while (reachable.hasNext()) {
                        negativePoint--;
                        reachable.next();
                    }
                }
            }
            return positivePoint + negativePoint;
        }
    }
}
