package sistema.managers;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sistema.enums.customEnums.Categoria;
import sistema.exceptions.RangoValoracionInvalidoException;
import sistema.ranking.Ranking;

import java.util.List;

public class RankingManagerTest {
    private RankingManager rankingManager;

    @BeforeEach
    public void setUp() {
        rankingManager = new RankingManager();
    }

    @Test
    public void testAñadirValoracion() throws RangoValoracionInvalidoException {
        Ranking ranking = new Ranking(4, new Categoria("Servicio"), "Muy buen servicio");
        rankingManager.añadirValoracion(ranking);
        assertEquals(1, rankingManager.getValoraciones().size());
    }

    @Test
    public void testGetValoracionesPorCategoria() throws RangoValoracionInvalidoException {
        rankingManager.añadirValoracion(new Ranking(5, new Categoria("Limpieza"), "Limpio y ordenado"));
        rankingManager.añadirValoracion(new Ranking(3, new Categoria("Servicio"), "Servicio promedio"));
        rankingManager.añadirValoracion(new Ranking(1, new Categoria("Limpieza"), "Mugroso"));
        assertEquals(2, rankingManager.getValoracionesPorCategoria(new Categoria("Limpieza")).size());
    }

    @Test
    public void testCalcularPromedioValoraciones() throws RangoValoracionInvalidoException {
        rankingManager.añadirValoracion(new Ranking(1, new Categoria("Limpieza"), "Limpio y ordenado"));
        rankingManager.añadirValoracion(new Ranking(3, new Categoria("Servicio"), "Servicio promedio"));
        assertEquals(2.0, rankingManager.getPromedioRanking());
    }

    @Test
    public void testCalcularPromedioPorCategoria() throws RangoValoracionInvalidoException {
        rankingManager.añadirValoracion(new Ranking(5, new Categoria("Limpieza"), "Excelente limpieza"));
        rankingManager.añadirValoracion(new Ranking(4, new Categoria("Limpieza"), "Buena limpieza"));
        assertEquals(4.5, rankingManager.getPromedioValoracionesPorCategoria(new Categoria("Limpieza")));
    }

    @Test
    public void testGetComentarios() throws RangoValoracionInvalidoException {
        rankingManager.añadirValoracion(new Ranking(5, new Categoria("Limpieza"), "Muy limpio"));
        rankingManager.añadirValoracion(new Ranking(4, new Categoria("Servicio"), "Buen servicio"));
        List<String> comentarios = rankingManager.getComentarios();
        assertEquals(2, comentarios.size());
        assertTrue(comentarios.contains("Muy limpio"));
        assertTrue(comentarios.contains("Buen servicio"));
    }
}
