package amazons;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/** A Player that takes input as text commands from the standard input.
 *  @author Wenhan Jin
 */
class TextPlayer extends Player {

    /** A new TextPlayer with no piece or controller (intended to produce
     *  a template). */
    TextPlayer() {
        this(null, null);
    }

    /** A new TextPlayer playing PIECE under control of CONTROLLER. */
    private TextPlayer(Piece piece, Controller controller) {
        super(piece, controller);
    }

    @Override
    Player create(Piece piece, Controller controller) {
        return new TextPlayer(piece, controller);
    }

    @Override
    String myMove() {
        while (true) {
            String line = _controller.readLine();
            if (line == null) {
                return "quit";
            } else if (!validline(line)) {
                _controller.reportError("Invalid move. "
                        + "Please try again.");
                continue;
            } else {
                return line;
            }
        }
    }

    /** Returns a boolean checking if S is a validline. */
    boolean validline(String s) {
        for (Matcher mat: _matches) {
            mat.reset(s);
            if (mat.matches()) {
                return true;
            }
        }
        return false;
    }

    /** An array of matching pattern commands. */
    private Matcher[] _matches = new Matcher[] {
            Pattern.compile("quit$").matcher(""),
            Pattern.compile("seed\\s+(\\d+)$").matcher(""),
            Pattern.compile("dump$").matcher(""),
            Pattern.compile("new$").matcher(""),
            Pattern.compile("auto\\s+(?i)(white|black)$").matcher(""),
            Pattern.compile("manual\\s+(?i)(white|black)$").matcher(""),
            Pattern.compile("[a-j][1-9]0?-"
                    + "[a-j][1-9]0?\\([a-j][1-9]0?\\)").matcher(""),
            Pattern.compile("[a-j]([1-9]|10)\\s+"
                    + "[a-j]([1-9]|10)\\s+[a-j]([1-9]|10)$").matcher(""),
            Pattern.compile("null$").matcher(""),
    };
}
