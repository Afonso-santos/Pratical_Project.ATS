from hypothesis import given, strategies as st, settings
from datetime import datetime, time
import os

class JUnitAtivDistAltimetriaTestGenerator:
    def __init__(self, path="../Projeto/generatorTest/Atividades/", filename="AtivDistAltimetriaGenTest.java"):
        self.path = path
        self.filename = filename
        self.filepath = os.path.join(path, filename)
        self.contador = 1

    @staticmethod
    @st.composite
    def gen_data(draw):
        year = draw(st.integers(2020, 2030))
        month = draw(st.integers(1, 12))
        max_day = 31 if month in [1,3,5,7,8,10,12] else 30 if month != 2 else (29 if year % 4 == 0 else 28)
        day = draw(st.integers(1, max_day))
        hour = draw(st.integers(0, 23))
        minute = draw(st.integers(0, 59))
        return datetime(year, month, day, hour, minute)

    @staticmethod
    @st.composite
    def gen_tempo(draw):
        return time(draw(st.integers(0, 5)), draw(st.integers(0, 59)))

    @staticmethod
    @st.composite
    def gen_freq_cardiaca(draw):
        return draw(st.integers(50, 200))

    @staticmethod
    @st.composite
    def gen_distancia(draw):
        return draw(st.floats(min_value=0.1, max_value=100.0, allow_nan=False, allow_infinity=False))

    @staticmethod
    @st.composite
    def gen_altimetria(draw):
        return draw(st.floats(min_value=-500.0, max_value=3000.0, allow_nan=False, allow_infinity=False))

    def criar_estrutura_arquivo(self):
        os.makedirs(self.path, exist_ok=True)
        with open(self.filepath, "w", encoding="utf-8") as f:
            f.write(self._codigo_inicial())

    def fechar_arquivo(self):
        with open(self.filepath, "a", encoding="utf-8") as f:
            f.write("\n}")

    def adicionar_metodo(self, metodo_codigo):
        with open(self.filepath, "a", encoding="utf-8") as f:
            f.write(metodo_codigo)
        self.contador += 1

    def _codigo_inicial(self):
        return '''package Projeto.test.Atividades;

import Projeto.*;
import org.junit.jupiter.api.*;
import java.time.*;
import static org.junit.jupiter.api.Assertions.*;

class AtivDistAltimetriaGenConcreta extends AtivDistAltimetria {
    public AtivDistAltimetriaGenConcreta() { super(); }
    public AtivDistAltimetriaGenConcreta(LocalDateTime d, LocalTime t, int f, double dist, double alt) { super(d, t, f, dist, alt); }
    public AtivDistAltimetriaGenConcreta(AtivDistAltimetria a) { super(a); }
    public double consumoCalorias(Utilizador u) { return 250.0; }
    public Atividade geraAtividade(Utilizador u, double c) { return new AtivDistAltimetriaGenConcreta(this); }
    public Object clone() { return new AtivDistAltimetriaGenConcreta(this); }
}

class AtivDistAltimetriaGenTest {

    @BeforeEach void setUp() { new AtivDistAltimetriaGenConcreta().setProximoCodigo(1); }
'''

    def gerar_metodo_teste(self, tipo, *args):
        nome = f"test{tipo}_Generated_{self.contador}"
        
        if tipo == "ConstrutorParametrizado":
            return f'''
    @Test
    void {nome}() {{
        LocalDateTime d = LocalDateTime.of({args[0].year}, {args[0].month}, {args[0].day}, {args[0].hour}, {args[0].minute});
        LocalTime t = LocalTime.of({args[1].hour}, {args[1].minute});
        int f = {args[2]};
        double dist = {args[3]:.2f};
        double alt = {args[4]:.2f};
        AtivDistAltimetria a = new AtivDistAltimetriaGenConcreta(d, t, f, dist, alt);
        assertNotNull(a);
        assertEquals(d, a.getDataRealizacao());
        assertEquals(t, a.getTempo());
        assertEquals(f, a.getFreqCardiaca());
        assertEquals(dist, a.getDistancia(), 0.001);
        assertEquals(alt, a.getAltimetria(), 0.001);
        assertTrue(a.getCodAtividade() > 0);
    }}'''

        elif tipo == "ConstrutorCopia":
            return f'''
    @Test
    void {nome}() {{
        LocalDateTime d = LocalDateTime.of({args[0].year}, {args[0].month}, {args[0].day}, {args[0].hour}, {args[0].minute});
        LocalTime t = LocalTime.of({args[1].hour}, {args[1].minute});
        int f = {args[2]};
        double dist = {args[3]:.2f};
        double alt = {args[4]:.2f};
        AtivDistAltimetria a = new AtivDistAltimetriaGenConcreta(d, t, f, dist, alt);
        AtivDistAltimetria c = new AtivDistAltimetriaGenConcreta(a);
        assertNotNull(c);
        assertEquals(a.getDataRealizacao(), c.getDataRealizacao());
        assertEquals(a.getTempo(), c.getTempo());
        assertEquals(a.getFreqCardiaca(), c.getFreqCardiaca());
        assertEquals(a.getDistancia(), c.getDistancia(), 0.001);
        assertEquals(a.getAltimetria(), c.getAltimetria(), 0.001);
        assertNotEquals(a.getCodAtividade(), c.getCodAtividade());
    }}'''

        elif tipo == "ConstrutorDefault":
            return f'''
    @Test
    void {nome}() {{
        AtivDistAltimetria a = new AtivDistAltimetriaGenConcreta();
        assertNotNull(a);
        assertNotNull(a.getDataRealizacao());
        assertNotNull(a.getTempo());
        assertEquals(0, a.getFreqCardiaca());
        assertEquals(0.0, a.getDistancia(), 0.001);
        assertEquals(0.0, a.getAltimetria(), 0.001);
        assertTrue(a.getCodAtividade() > 0);
    }}'''

        elif tipo == "SetGetDistancia":
            return f'''
    @Test
    void {nome}() {{
        AtivDistAltimetria a = new AtivDistAltimetriaGenConcreta();
        double dist = {args[0]:.2f};
        a.setDistancia(dist);
        assertEquals(dist, a.getDistancia(), 0.001);
    }}'''

        elif tipo == "SetGetAltimetria":
            return f'''
    @Test
    void {nome}() {{
        AtivDistAltimetria a = new AtivDistAltimetriaGenConcreta();
        double alt = {args[0]:.2f};
        a.setAltimetria(alt);
        assertEquals(alt, a.getAltimetria(), 0.001);
    }}'''

        elif tipo == "FatorAltimetria":
            return f'''
    @Test
    void {nome}() {{
        AtivDistAltimetria a = new AtivDistAltimetriaGenConcreta();
        double alt = {args[0]:.2f};
        a.setAltimetria(alt);
        double fator = a.getFatorAltimetria();
        double expected = alt * 0.0005; // Assumindo que o fator é altimetria * 0.0005
        assertEquals(expected, fator, 0.001);
    }}'''

        elif tipo == "Equals":
            d1, t1, f1, dist1, alt1, d2, t2, f2, dist2, alt2 = args
            cmp = "assertTrue" if (d1 == d2 and t1 == t2 and f1 == f2 and abs(dist1 - dist2) < 0.001 and abs(alt1 - alt2) < 0.001) else "assertFalse"
            return f'''
    @Test
    void {nome}() {{
        LocalDateTime d1 = LocalDateTime.of({d1.year}, {d1.month}, {d1.day}, {d1.hour}, {d1.minute});
        LocalTime t1 = LocalTime.of({t1.hour}, {t1.minute});
        LocalDateTime d2 = LocalDateTime.of({d2.year}, {d2.month}, {d2.day}, {d2.hour}, {d2.minute});
        LocalTime t2 = LocalTime.of({t2.hour}, {t2.minute});
        AtivDistAltimetria a1 = new AtivDistAltimetriaGenConcreta(d1, t1, {f1}, {dist1:.2f}, {alt1:.2f});
        AtivDistAltimetria a2 = new AtivDistAltimetriaGenConcreta(d2, t2, {f2}, {dist2:.2f}, {alt2:.2f});
        {cmp}(a1.equals(a2));
    }}'''

        elif tipo == "CompareTo":
            d1, _, _, _, _, d2, _, _, _, _ = args
            sign = lambda x: (x > 0) - (x < 0)
            expected = sign((d1 - d2).total_seconds())
            return f'''
    @Test
    void {nome}() {{
        LocalDateTime d1 = LocalDateTime.of({d1.year}, {d1.month}, {d1.day}, {d1.hour}, {d1.minute});
        LocalDateTime d2 = LocalDateTime.of({d2.year}, {d2.month}, {d2.day}, {d2.hour}, {d2.minute});
        AtivDistAltimetria a1 = new AtivDistAltimetriaGenConcreta(d1, LocalTime.of(0,0), 60, 10.0, 100.0);
        AtivDistAltimetria a2 = new AtivDistAltimetriaGenConcreta(d2, LocalTime.of(0,0), 60, 10.0, 100.0);
        int result = a1.compareTo(a2);
        assertEquals(Integer.signum({expected}), Integer.signum(result));
    }}'''

        elif tipo == "ToStringTest":
            return f'''
    @Test
    void {nome}() {{
        LocalDateTime d = LocalDateTime.of({args[0].year}, {args[0].month}, {args[0].day}, {args[0].hour}, {args[0].minute});
        LocalTime t = LocalTime.of({args[1].hour}, {args[1].minute});
        int f = {args[2]};
        double dist = {args[3]:.2f};
        double alt = {args[4]:.2f};
        AtivDistAltimetria a = new AtivDistAltimetriaGenConcreta(d, t, f, dist, alt);
        String result = a.toString();
        assertNotNull(result);
        assertTrue(result.contains("Distancia") || result.contains("Distância"));
        assertTrue(result.contains("Altimetria"));
        assertTrue(result.contains(String.valueOf(f)));
        assertTrue(result.contains(String.valueOf(dist)));
        assertTrue(result.contains(String.valueOf(alt)));
    }}'''

        elif tipo == "CloneTest":
            return f'''
    @Test
    void {nome}() {{
        LocalDateTime d = LocalDateTime.of({args[0].year}, {args[0].month}, {args[0].day}, {args[0].hour}, {args[0].minute});
        LocalTime t = LocalTime.of({args[1].hour}, {args[1].minute});
        int f = {args[2]};
        double dist = {args[3]:.2f};
        double alt = {args[4]:.2f};
        AtivDistAltimetria a = new AtivDistAltimetriaGenConcreta(d, t, f, dist, alt);
        AtivDistAltimetria cloned = (AtivDistAltimetria) a.clone();
        assertNotNull(cloned);
        assertEquals(a.getDataRealizacao(), cloned.getDataRealizacao());
        assertEquals(a.getTempo(), cloned.getTempo());
        assertEquals(a.getFreqCardiaca(), cloned.getFreqCardiaca());
        assertEquals(a.getDistancia(), cloned.getDistancia(), 0.001);
        assertEquals(a.getAltimetria(), cloned.getAltimetria(), 0.001);
        assertNotEquals(a.getCodAtividade(), cloned.getCodAtividade());
    }}'''

        elif tipo == "EqualsNull":
            return f'''
    @Test
    void {nome}() {{
        AtivDistAltimetria a = new AtivDistAltimetriaGenConcreta();
        assertFalse(a.equals(null));
        assertFalse(a.equals(new Object()));
        assertTrue(a.equals(a));
    }}'''

    def gerar_todos_os_testes(self):
        self.criar_estrutura_arquivo()
        self._test_construtor_default()
        self._test_construtor()
        self._test_copia()
        self._test_set_get_distancia()
        self._test_set_get_altimetria()
        self._test_fator_altimetria()
        self._test_equals()
        self._test_equals_null()
        self._test_compare()
        self._test_toString()
        self._test_clone()
        self.fechar_arquivo()
        print(f"{self.contador-1} testes gerados em {self.filepath}")

    @settings(max_examples=5)
    @given(st.just(None))
    def _test_construtor_default(self, _):
        metodo = self.gerar_metodo_teste("ConstrutorDefault")
        self.adicionar_metodo(metodo)

    @settings(max_examples=8)
    @given(gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_distancia(), gen_altimetria())
    def _test_construtor(self, data, tempo, freq, dist, alt):
        metodo = self.gerar_metodo_teste("ConstrutorParametrizado", data, tempo, freq, dist, alt)
        self.adicionar_metodo(metodo)

    @settings(max_examples=8)
    @given(gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_distancia(), gen_altimetria())
    def _test_copia(self, data, tempo, freq, dist, alt):
        metodo = self.gerar_metodo_teste("ConstrutorCopia", data, tempo, freq, dist, alt)
        self.adicionar_metodo(metodo)

    @settings(max_examples=8)
    @given(gen_distancia())
    def _test_set_get_distancia(self, dist):
        metodo = self.gerar_metodo_teste("SetGetDistancia", dist)
        self.adicionar_metodo(metodo)

    @settings(max_examples=8)
    @given(gen_altimetria())
    def _test_set_get_altimetria(self, alt):
        metodo = self.gerar_metodo_teste("SetGetAltimetria", alt)
        self.adicionar_metodo(metodo)

    @settings(max_examples=8)
    @given(gen_altimetria())
    def _test_fator_altimetria(self, alt):
        metodo = self.gerar_metodo_teste("FatorAltimetria", alt)
        self.adicionar_metodo(metodo)

    @settings(max_examples=8)
    @given(gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_distancia(), gen_altimetria(),
           gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_distancia(), gen_altimetria())
    def _test_equals(self, d1, t1, f1, dist1, alt1, d2, t2, f2, dist2, alt2):
        metodo = self.gerar_metodo_teste("Equals", d1, t1, f1, dist1, alt1, d2, t2, f2, dist2, alt2)
        self.adicionar_metodo(metodo)

    @settings(max_examples=3)
    @given(st.just(None))
    def _test_equals_null(self, _):
        metodo = self.gerar_metodo_teste("EqualsNull")
        self.adicionar_metodo(metodo)

    @settings(max_examples=8)
    @given(gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_distancia(), gen_altimetria(),
           gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_distancia(), gen_altimetria())
    def _test_compare(self, d1, t1, f1, dist1, alt1, d2, t2, f2, dist2, alt2):
        metodo = self.gerar_metodo_teste("CompareTo", d1, t1, f1, dist1, alt1, d2, t2, f2, dist2, alt2)
        self.adicionar_metodo(metodo)

    @settings(max_examples=5)
    @given(gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_distancia(), gen_altimetria())
    def _test_toString(self, data, tempo, freq, dist, alt):
        metodo = self.gerar_metodo_teste("ToStringTest", data, tempo, freq, dist, alt)
        self.adicionar_metodo(metodo)

    @settings(max_examples=5)
    @given(gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_distancia(), gen_altimetria())
    def _test_clone(self, data, tempo, freq, dist, alt):
        metodo = self.gerar_metodo_teste("CloneTest", data, tempo, freq, dist, alt)
        self.adicionar_metodo(metodo)

if __name__ == "__main__":
    gen = JUnitAtivDistAltimetriaTestGenerator()
    gen.gerar_todos_os_testes()