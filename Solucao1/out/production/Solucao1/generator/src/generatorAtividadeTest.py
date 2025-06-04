from hypothesis import given, strategies as st, settings
from datetime import datetime, time
import os

class JUnitTestGenerator:
    def __init__(self, path="../Projeto/generatorTest/Atividades/", filename="AtividadeGenTest.java"):
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

class AtividadeGenConcreta extends Atividade {
    public AtividadeGenConcreta() { super(); }
    public AtividadeGenConcreta(LocalDateTime d, LocalTime t, int f) { super(d, t, f); }
    public AtividadeGenConcreta(Atividade a) { super(a); }
    public double consumoCalorias(Utilizador u) { return 100.0; }
    public Atividade geraAtividade(Utilizador u, double c) { return new AtividadeGenConcreta(this); }
    public Object clone() { return new AtividadeGenConcreta(this); }
}

class AtividadeGenTest {

    @BeforeEach void setUp() { new AtividadeGenConcreta().setProximoCodigo(1); }
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
        Atividade a = new AtividadeGenConcreta(d, t, f);
        assertNotNull(a);
        assertEquals(d, a.getDataRealizacao());
        assertEquals(t, a.getTempo());
        assertEquals(f, a.getFreqCardiaca());
        assertTrue(a.getCodAtividade() > 0);
    }}'''
        elif tipo == "ConstrutorCopia":
            return f'''
    @Test
    void {nome}() {{
        LocalDateTime d = LocalDateTime.of({args[0].year}, {args[0].month}, {args[0].day}, {args[0].hour}, {args[0].minute});
        LocalTime t = LocalTime.of({args[1].hour}, {args[1].minute});
        int f = {args[2]};
        Atividade a = new AtividadeGenConcreta(d, t, f);
        Atividade c = new AtividadeGenConcreta(a);
        assertNotNull(c);
        assertEquals(a.getDataRealizacao(), c.getDataRealizacao());
        assertEquals(a.getTempo(), c.getTempo());
        assertEquals(a.getFreqCardiaca(), c.getFreqCardiaca());
        assertNotEquals(a.getCodAtividade(), c.getCodAtividade());
    }}'''
        elif tipo == "Equals":
            d1, t1, f1, d2, t2, f2 = args
            cmp = "assertTrue" if (d1 == d2 and t1 == t2 and f1 == f2) else "assertFalse"
            return f'''
    @Test
    void {nome}() {{
        LocalDateTime d1 = LocalDateTime.of({d1.year}, {d1.month}, {d1.day}, {d1.hour}, {d1.minute});
        LocalTime t1 = LocalTime.of({t1.hour}, {t1.minute});
        LocalDateTime d2 = LocalDateTime.of({d2.year}, {d2.month}, {d2.day}, {d2.hour}, {d2.minute});
        LocalTime t2 = LocalTime.of({t2.hour}, {t2.minute});
        Atividade a1 = new AtividadeGenConcreta(d1, t1, {f1});
        Atividade a2 = new AtividadeGenConcreta(d2, t2, {f2});
        {cmp}(a1.equals(a2));
    }}'''
        elif tipo == "CompareTo":
            d1, _, _, d2, _, _ = args
            sign = lambda x: (x > 0) - (x < 0)
            expected = sign((d1 - d2).total_seconds())
            return f'''
    @Test
    void {nome}() {{
        LocalDateTime d1 = LocalDateTime.of({d1.year}, {d1.month}, {d1.day}, {d1.hour}, {d1.minute});
        LocalDateTime d2 = LocalDateTime.of({d2.year}, {d2.month}, {d2.day}, {d2.hour}, {d2.minute});
        Atividade a1 = new AtividadeGenConcreta(d1, LocalTime.of(0,0), 60);
        Atividade a2 = new AtividadeGenConcreta(d2, LocalTime.of(0,0), 60);
        int result = a1.compareTo(a2);
        assertEquals(Integer.signum({expected}), Integer.signum(result));
    }}'''

    def gerar_todos_os_testes(self):
        self.criar_estrutura_arquivo()
        self._test_construtor()
        self._test_copia()
        self._test_equals()
        self._test_compare()
        self.fechar_arquivo()
        print(f"{self.contador-1} testes gerados em {self.filepath}")

    @settings(max_examples=10)
    @given(gen_data(), gen_tempo(), gen_freq_cardiaca())
    def _test_construtor(self, data, tempo, freq):
        metodo = self.gerar_metodo_teste("ConstrutorParametrizado", data, tempo, freq)
        self.adicionar_metodo(metodo)

    @settings(max_examples=10)
    @given(gen_data(), gen_tempo(), gen_freq_cardiaca())
    def _test_copia(self, data, tempo, freq):
        metodo = self.gerar_metodo_teste("ConstrutorCopia", data, tempo, freq)
        self.adicionar_metodo(metodo)

    @settings(max_examples=10)
    @given(gen_data(), gen_tempo(), gen_freq_cardiaca(),
           gen_data(), gen_tempo(), gen_freq_cardiaca())
    def _test_equals(self, d1, t1, f1, d2, t2, f2):
        metodo = self.gerar_metodo_teste("Equals", d1, t1, f1, d2, t2, f2)
        self.adicionar_metodo(metodo)

    @settings(max_examples=10)
    @given(gen_data(), gen_tempo(), gen_freq_cardiaca(),
           gen_data(), gen_tempo(), gen_freq_cardiaca())
    def _test_compare(self, d1, t1, f1, d2, t2, f2):
        metodo = self.gerar_metodo_teste("CompareTo", d1, t1, f1, d2, t2, f2)
        self.adicionar_metodo(metodo)

if __name__ == "__main__":
    gen = JUnitTestGenerator()
    gen.gerar_todos_os_testes()