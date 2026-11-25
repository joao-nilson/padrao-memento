package padroescomportamentais.memento;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EditHistoryTest {

    private Spreadsheet spreadsheet;
    private EditHistory history;

    @BeforeEach
    void setUp() {
        spreadsheet = new Spreadsheet();
        spreadsheet.setFormula("A1", "=SUM(B1:B3)");
        spreadsheet.setFormula("A2", "=B1*2");
        spreadsheet.setFormula("A3", "Total");
        spreadsheet.setFormula("A4", "=C1+C2");
        history = new EditHistory();
    }

    @Test
    void deveSubstituirFormulasAfetadas() {
        int alteradas = history.findAndReplace(spreadsheet, "B1", "D1");

        assertEquals(2, alteradas);
        assertEquals("=SUM(D1:B3)", spreadsheet.getFormula("A1"));
        assertEquals("=D1*2", spreadsheet.getFormula("A2"));
    }

    @Test
    void naoDeveAlterarCelulasNaoAfetadas() {
        history.findAndReplace(spreadsheet, "B1", "D1");

        assertEquals("Total", spreadsheet.getFormula("A3"));
        assertEquals("=C1+C2", spreadsheet.getFormula("A4"));
    }

    @Test
    void deveDesfazerSubstituicaoEmUmPasso() {
        history.findAndReplace(spreadsheet, "B1", "D1");

        assertTrue(history.undo(spreadsheet));

        assertEquals("=SUM(B1:B3)", spreadsheet.getFormula("A1"));
        assertEquals("=B1*2", spreadsheet.getFormula("A2"));
        assertEquals("Total", spreadsheet.getFormula("A3"));
        assertEquals("=C1+C2", spreadsheet.getFormula("A4"));
    }

    @Test
    void deveGuardarMementoApenasDasCelulasAfetadas() {
        Spreadsheet.Memento memento = spreadsheet.findAndReplace("C1", "E1");

        assertEquals(1, memento.size());
    }

    @Test
    void devePreservarEdicoesPosterioresEmCelulasNaoAfetadas() {
        history.findAndReplace(spreadsheet, "B1", "D1");
        spreadsheet.setFormula("A4", "=C1*10");

        history.undo(spreadsheet);

        assertEquals("=C1*10", spreadsheet.getFormula("A4"));
        assertEquals("=B1*2", spreadsheet.getFormula("A2"));
    }

    @Test
    void deveDesfazerSubstituicoesEmOrdemInversa() {
        history.findAndReplace(spreadsheet, "B1", "D1");
        history.findAndReplace(spreadsheet, "C1", "E1");

        history.undo(spreadsheet);
        assertEquals("=C1+C2", spreadsheet.getFormula("A4"));
        assertEquals("=D1*2", spreadsheet.getFormula("A2"));

        history.undo(spreadsheet);
        assertEquals("=B1*2", spreadsheet.getFormula("A2"));
    }

    @Test
    void naoDeveRegistrarHistoricoQuandoNadaForEncontrado() {
        history.findAndReplace(spreadsheet, "B1", "D1");

        assertEquals(0, history.findAndReplace(spreadsheet, "ZZ", "YY"));

        history.undo(spreadsheet);
        assertEquals("=B1*2", spreadsheet.getFormula("A2"));
        assertFalse(history.undo(spreadsheet));
    }

    @Test
    void deveRetornarFalsoAoDesfazerSemHistorico() {
        assertFalse(history.undo(spreadsheet));
    }

    @Test
    void deveRetornarExcecaoTextoDeBuscaInvalido() {
        try {
            history.findAndReplace(spreadsheet, "", "X");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Texto de busca inválido", e.getMessage());
        }
    }

}
