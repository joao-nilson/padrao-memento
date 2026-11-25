package padroescomportamentais.memento;

import java.util.ArrayDeque;
import java.util.Deque;

public class EditHistory {

    private final Deque<Spreadsheet.Memento> history = new ArrayDeque<>();

    public int findAndReplace(Spreadsheet spreadsheet, String find, String replacement) {
        Spreadsheet.Memento memento = spreadsheet.findAndReplace(find, replacement);
        if (memento.size() > 0) {
            history.push(memento);
        }
        return memento.size();
    }

    public boolean undo(Spreadsheet spreadsheet) {
        if (history.isEmpty()) {
            return false;
        }
        spreadsheet.restore(history.pop());
        return true;
    }
}
