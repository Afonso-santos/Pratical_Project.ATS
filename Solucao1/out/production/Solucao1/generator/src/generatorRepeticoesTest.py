from hypothesis import given, strategies as st, settings
from datetime import datetime, time
import os

class JUnitAtivRepeticoesTestGenerator:
    def __init__(self, path="../Projeto/generatorTest/Atividades/", filename="AtivRepeticoesGenTest.java"):
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
    def gen_repeticoes(draw):
        return draw(st.integers(1, 500))

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

class AtivRepeticoesGenConcreta extends AtivRepeticoes {
    public AtivRepeticoesGenConcreta() { super(); }
    public AtivRepeticoesGenConcreta(LocalDateTime d, LocalTime t, int f, int r) { super(d, t, f, r); }
    public AtivRepeticoesGenConcreta(AtivRepeticoes a) { super(a); }
    public double consumoCalorias(Utilizador u) { return 150.0; }
    public Atividade geraAtividade(Utilizador u, double c) { return new AtivRepeticoesGenConcreta(this); }
    public Object clone() { return new AtivRepeticoesGenConcreta(this); }
}

class AtivRepeticoesGenTest {

    @BeforeEach void setUp() { new AtivRepeticoesGenConcreta().setProximoCodigo(1); }
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
        int r = {args[3]};
        AtivRepeticoes a = new AtivRepeticoesGenConcreta(d, t, f, r);
        assertNotNull(a);
        assertEquals(d, a.getDataRealizacao());
        assertEquals(t, a.getTempo());
        assertEquals(f, a.getFreqCardiaca());
        assertEquals(r, a.getRepeticoes());
        assertTrue(a.getCodAtividade() > 0);
    }}'''

        elif tipo == "ConstrutorCopia":
            return f'''
    @Test
    void {nome}() {{
        LocalDateTime d = LocalDateTime.of({args[0].year}, {args[0].month}, {args[0].day}, {args[0].hour}, {args[0].minute});
        LocalTime t = LocalTime.of({args[1].hour}, {args[1].minute});
        int f = {args[2]};
        int r = {args[3]};
        AtivRepeticoes a = new AtivRepeticoesGenConcreta(d, t, f, r);
        AtivRepeticoes c = new AtivRepeticoesGenConcreta(a);
        assertNotNull(c);
        assertEquals(a.getDataRealizacao(), c.getDataRealizacao());
        assertEquals(a.getTempo(), c.getTempo());
        assertEquals(a.getFreqCardiaca(), c.getFreqCardiaca());
        assertEquals(a.getRepeticoes(), c.getRepeticoes());
        assertNotEquals(a.getCodAtividade(), c.getCodAtividade());
    }}'''

        elif tipo == "SetGetRepeticoes":
            return f'''
    @Test
    void {nome}() {{
        AtivRepeticoes a = new AtivRepeticoesGenConcreta();
        int r = {args[0]};
        a.setRepeticoes(r);
        assertEquals(r, a.getRepeticoes());
    }}'''

        elif tipo == "FatorRepeticoes":
            return f'''
    @Test
    void {nome}() {{
        LocalDateTime d = LocalDateTime.of({args[0].year}, {args[0].month}, {args[0].day}, {args[0].hour}, {args[0].minute});
        LocalTime t = LocalTime.of({args[1].hour}, {args[1].minute});
        int r = {args[2]};
        AtivRepeticoes a = new AtivRepeticoesGenConcreta(d, t, 100, r);
        a.setTempo(t);
        double valorNulo = 1.5;
        double valorIncremento = 2.0;
        double fator = a.getFatorRepeticoes(valorNulo, valorIncremento);
        assertTrue(fator >= 0.0);
        if (t.toSecondOfDay() > 0) {{
            double expected = (r / (double)t.toSecondOfDay() - valorNulo) * valorIncremento;
            assertEquals(expected, fator, 0.001);
        }} else {{
            assertEquals(0.0, fator, 0.001);
        }}
    }}'''

        elif tipo == "Equals":
            d1, t1, f1, r1, d2, t2, f2, r2 = args
            cmp = "assertTrue" if (d1 == d2 and t1 == t2 and f1 == f2 and r1 == r2) else "assertFalse"
            return f'''
    @Test
    void {nome}() {{
        LocalDateTime d1 = LocalDateTime.of({d1.year}, {d1.month}, {d1.day}, {d1.hour}, {d1.minute});
        LocalTime t1 = LocalTime.of({t1.hour}, {t1.minute});
        LocalDateTime d2 = LocalDateTime.of({d2.year}, {d2.month}, {d2.day}, {d2.hour}, {d2.minute});
        LocalTime t2 = LocalTime.of({t2.hour}, {t2.minute});
        AtivRepeticoes a1 = new AtivRepeticoesGenConcreta(d1, t1, {f1}, {r1});
        AtivRepeticoes a2 = new AtivRepeticoesGenConcreta(d2, t2, {f2}, {r2});
        {cmp}(a1.equals(a2));
    }}'''

        elif tipo == "CompareTo":
            d1, _, _, _, d2, _, _, _ = args
            sign = lambda x: (x > 0) - (x < 0)
            expected = sign((d1 - d2).total_seconds())
            return f'''
    @Test
    void {nome}() {{
        LocalDateTime d1 = LocalDateTime.of({d1.year}, {d1.month}, {d1.day}, {d1.hour}, {d1.minute});
        LocalDateTime d2 = LocalDateTime.of({d2.year}, {d2.month}, {d2.day}, {d2.hour}, {d2.minute});
        AtivRepeticoes a1 = new AtivRepeticoesGenConcreta(d1, LocalTime.of(0,0), 60, 10);
        AtivRepeticoes a2 = new AtivRepeticoesGenConcreta(d2, LocalTime.of(0,0), 60, 10);
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
        int r = {args[3]};
        AtivRepeticoes a = new AtivRepeticoesGenConcreta(d, t, f, r);
        String result = a.toString();
        assertNotNull(result);
        assertTrue(result.contains("Repeticoes"));
        assertTrue(result.contains(String.valueOf(r)));
        assertTrue(result.contains(String.valueOf(f)));
    }}'''

    def gerar_todos_os_testes(self):
        self.criar_estrutura_arquivo()
        self._test_construtor()
        self._test_copia()
        self._test_set_get_repeticoes()
        self._test_fator_repeticoes()
        self._test_equals()
        self._test_compare()
        self._test_toString()
        self.fechar_arquivo()
        print(f"{self.contador-1} testes gerados em {self.filepath}")

    @settings(max_examples=8)
    @given(gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_repeticoes())
    def _test_construtor(self, data, tempo, freq, reps):
        metodo = self.gerar_metodo_teste("ConstrutorParametrizado", data, tempo, freq, reps)
        self.adicionar_metodo(metodo)

    @settings(max_examples=8)
    @given(gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_repeticoes())
    def _test_copia(self, data, tempo, freq, reps):
        metodo = self.gerar_metodo_teste("ConstrutorCopia", data, tempo, freq, reps)
        self.adicionar_metodo(metodo)

    @settings(max_examples=8)
    @given(gen_repeticoes())
    def _test_set_get_repeticoes(self, reps):
        metodo = self.gerar_metodo_teste("SetGetRepeticoes", reps)
        self.adicionar_metodo(metodo)

    @settings(max_examples=8)
    @given(gen_data(), gen_tempo(), gen_repeticoes())
    def _test_fator_repeticoes(self, data, tempo, reps):
        metodo = self.gerar_metodo_teste("FatorRepeticoes", data, tempo, reps)
        self.adicionar_metodo(metodo)

    @settings(max_examples=8)
    @given(gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_repeticoes(),
           gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_repeticoes())
    def _test_equals(self, d1, t1, f1, r1, d2, t2, f2, r2):
        metodo = self.gerar_metodo_teste("Equals", d1, t1, f1, r1, d2, t2, f2, r2)
        self.adicionar_metodo(metodo)

    @settings(max_examples=8)
    @given(gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_repeticoes(),
           gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_repeticoes())
    def _test_compare(self, d1, t1, f1, r1, d2, t2, f2, r2):
        metodo = self.gerar_metodo_teste("CompareTo", d1, t1, f1, r1, d2, t2, f2, r2)
        self.adicionar_metodo(metodo)

    @settings(max_examples=5)
    @given(gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_repeticoes())
    def _test_toString(self, data, tempo, freq, reps):
        metodo = self.gerar_metodo_teste("ToStringTest", data, tempo, freq, reps)
        self.adicionar_metodo(metodo)

if __name__ == "__main__":
    gen = JUnitAtivRepeticoesTestGenerator()
    gen.gerar_todos_os_testes()