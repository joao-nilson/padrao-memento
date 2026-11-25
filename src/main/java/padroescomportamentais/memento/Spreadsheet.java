package padroescomportamentais.memento;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class Spreadsheet {

    private final Map<String, String> cells = new TreeMap<>();

    public void setFormula(String address, String formula) {
        cells.put(address, formula);
    }

    public String getFormula(String address) {
        return cells.get(address);
    }

    public Map<String, String> getCells() {
        return Collections.unmodifiableMap(cells);
    }

    public Memento findAndReplace(String find, String replacement) {
        if (find == null || find.isEmpty()) {
            throw new IllegalArgumentException("Texto de busca inválido");
        }
        Map<String, String> affected = new TreeMap<>();
        for (Map.Entry<String, String> cell : cells.entrySet()) {
            if (cell.getValue().contains(find)) {
                affected.put(cell.getKey(), cell.getValue());
            }
        }
        for (String address : affected.keySet()) {
            cells.put(address, cells.get(address).replace(find, replacement));
        }
        return new Memento(affected);
    }

    public void restore(Memento memento) {
        cells.putAll(memento.cells);
    }

    public static class Memento {

        private final Map<String, String> cells;

        private Memento(Map<String, String> cells) {
            this.cells = cells;
        }

        public int size() {
            return cells.size();
        }
    }
}
