from hypothesis import given, strategies as st, settings
from datetime import datetime, time
import os

class JUnitAtivRepsPesoTestGenerator:
    def __init__(self, path="../Projeto/generatorTest/Atividades/", filename="AtivRepsPesoGenTest.java"):
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

    @staticmethod
    @st.composite
    def gen_peso(draw):
        return draw(st.floats(min_value=0.0, max_value=300.0, allow_nan=False, allow_infinity=False))

    @staticmethod
    @st.composite
    def gen_peso_utilizador(draw):
        return draw(st.floats(min_value=40.0, max_value=150.0, allow_nan=False, allow_infinity=False))

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

class AtivRepsPesoGenConcreta extends AtivRepsPeso {
    public AtivRepsPesoGenConcreta() { super(); }
    public AtivRepsPesoGenConcreta(LocalDateTime d, LocalTime t, int f, int r, double p) { super(d, t, f, r, p); }
    public AtivRepsPesoGenConcreta(AtivRepsPeso a) { super(a); }
    public double consumoCalorias(Utilizador u) { return 200.0; }
    public Atividade geraAtividade(Utilizador u, double c) { return new AtivRepsPesoGenConcreta(this); }
    public Object clone() { return new AtivRepsPesoGenConcreta(this); }
}

class AtivRepsPesoGenTest {

    @BeforeEach void setUp() { new AtivRepsPesoGenConcreta().setProximoCodigo(1); }
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
        double p = {args[4]:.2f};
        AtivRepsPeso a = new AtivRepsPesoGenConcreta(d, t, f, r, p);
        assertNotNull(a);
        assertEquals(d, a.getDataRealizacao());
        assertEquals(t, a.getTempo());
        assertEquals(f, a.getFreqCardiaca());
        assertEquals(r, a.getRepeticoes());
        assertEquals(p, a.getPeso(), 0.001);
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
        double p = {args[4]:.2f};
        AtivRepsPeso a = new AtivRepsPesoGenConcreta(d, t, f, r, p);
        AtivRepsPeso c = new AtivRepsPesoGenConcreta(a);
        assertNotNull(c);
        assertEquals(a.getDataRealizacao(), c.getDataRealizacao());
        assertEquals(a.getTempo(), c.getTempo());
        assertEquals(a.getFreqCardiaca(), c.getFreqCardiaca());
        assertEquals(a.getRepeticoes(), c.getRepeticoes());
        assertEquals(a.getPeso(), c.getPeso(), 0.001);
        assertNotEquals(a.getCodAtividade(), c.getCodAtividade());
    }}'''

        elif tipo == "SetGetPeso":
            return f'''
    @Test
    void {nome}() {{
        AtivRepsPeso a = new AtivRepsPesoGenConcreta();
        double p = {args[0]:.2f};
        a.setPeso(p);
        assertEquals(p, a.getPeso(), 0.001);
    }}'''

        elif tipo == "FatorPeso":
            return f'''
    @Test
    void {nome}() {{
        LocalDateTime d = LocalDateTime.of({args[0].year}, {args[0].month}, {args[0].day}, {args[0].hour}, {args[0].minute});
        LocalTime t = LocalTime.of({args[1].hour}, {args[1].minute});
        double pesoAtiv = {args[2]:.2f};
        double pesoUser = {args[3]:.2f};
        AtivRepsPeso a = new AtivRepsPesoGenConcreta(d, t, 100, 10, pesoAtiv);
        Utilizador u = new UtilizadorAmador();
        u.setPeso(pesoUser);
        double valorNulo = 1.0;
        double valorIncremento = 2.0;
        double fator = a.getFatorPeso(u, valorNulo, valorIncremento);
        if (pesoUser > 0) {{
            double razaoPeso = pesoAtiv / pesoUser;
            double expected = (razaoPeso - valorNulo) * valorIncremento;
            assertEquals(expected, fator, 0.001);
        }} else {{
            // Se peso do utilizador é 0, pode haver comportamento especial
            assertTrue(fator >= -1000.0 && fator <= 1000.0); // Verificação básica de sanidade
        }}
    }}'''

        elif tipo == "Equals":
            d1, t1, f1, r1, p1, d2, t2, f2, r2, p2 = args
            cmp = "assertTrue" if (d1 == d2 and t1 == t2 and f1 == f2 and r1 == r2 and abs(p1 - p2) < 0.001) else "assertFalse"
            return f'''
    @Test
    void {nome}() {{
        LocalDateTime d1 = LocalDateTime.of({d1.year}, {d1.month}, {d1.day}, {d1.hour}, {d1.minute});
        LocalTime t1 = LocalTime.of({t1.hour}, {t1.minute});
        LocalDateTime d2 = LocalDateTime.of({d2.year}, {d2.month}, {d2.day}, {d2.hour}, {d2.minute});
        LocalTime t2 = LocalTime.of({t2.hour}, {t2.minute});
        AtivRepsPeso a1 = new AtivRepsPesoGenConcreta(d1, t1, {f1}, {r1}, {p1:.2f});
        AtivRepsPeso a2 = new AtivRepsPesoGenConcreta(d2, t2, {f2}, {r2}, {p2:.2f});
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
        AtivRepsPeso a1 = new AtivRepsPesoGenConcreta(d1, LocalTime.of(0,0), 60, 10, 50.0);
        AtivRepsPeso a2 = new AtivRepsPesoGenConcreta(d2, LocalTime.of(0,0), 60, 10, 50.0);
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
        double p = {args[4]:.2f};
        AtivRepsPeso a = new AtivRepsPesoGenConcreta(d, t, f, r, p);
        String result = a.toString();
        assertNotNull(result);
        assertTrue(result.contains("Repetições") || result.contains("Repeticoes"));
        assertTrue(result.contains("Peso"));
        assertTrue(result.contains(String.valueOf(r)));
        assertTrue(result.contains(String.valueOf(f)));
        assertTrue(result.contains(String.valueOf(p)));
    }}'''

        elif tipo == "CloneTest":
            return f'''
    @Test
    void {nome}() {{
        LocalDateTime d = LocalDateTime.of({args[0].year}, {args[0].month}, {args[0].day}, {args[0].hour}, {args[0].minute});
        LocalTime t = LocalTime.of({args[1].hour}, {args[1].minute});
        int f = {args[2]};
        int r = {args[3]};
        double p = {args[4]:.2f};
        AtivRepsPeso a = new AtivRepsPesoGenConcreta(d, t, f, r, p);
        AtivRepsPeso cloned = (AtivRepsPeso) a.clone();
        assertNotNull(cloned);
        assertEquals(a.getDataRealizacao(), cloned.getDataRealizacao());
        assertEquals(a.getTempo(), cloned.getTempo());
        assertEquals(a.getFreqCardiaca(), cloned.getFreqCardiaca());
        assertEquals(a.getRepeticoes(), cloned.getRepeticoes());
        assertEquals(a.getPeso(), cloned.getPeso(), 0.001);
        assertNotEquals(a.getCodAtividade(), cloned.getCodAtividade());
    }}'''

    def gerar_todos_os_testes(self):
        self.criar_estrutura_arquivo()
        self._test_construtor()
        self._test_copia()
        self._test_set_get_peso()
        self._test_fator_peso()
        self._test_equals()
        self._test_compare()
        self._test_toString()
        self._test_clone()
        self.fechar_arquivo()
        print(f"{self.contador-1} testes gerados em {self.filepath}")

    @settings(max_examples=8)
    @given(gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_repeticoes(), gen_peso())
    def _test_construtor(self, data, tempo, freq, reps, peso):
        metodo = self.gerar_metodo_teste("ConstrutorParametrizado", data, tempo, freq, reps, peso)
        self.adicionar_metodo(metodo)

    @settings(max_examples=8)
    @given(gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_repeticoes(), gen_peso())
    def _test_copia(self, data, tempo, freq, reps, peso):
        metodo = self.gerar_metodo_teste("ConstrutorCopia", data, tempo, freq, reps, peso)
        self.adicionar_metodo(metodo)

    @settings(max_examples=8)
    @given(gen_peso())
    def _test_set_get_peso(self, peso):
        metodo = self.gerar_metodo_teste("SetGetPeso", peso)
        self.adicionar_metodo(metodo)

    @settings(max_examples=8)
    @given(gen_data(), gen_tempo(), gen_peso(), gen_peso_utilizador())
    def _test_fator_peso(self, data, tempo, peso_ativ, peso_user):
        metodo = self.gerar_metodo_teste("FatorPeso", data, tempo, peso_ativ, peso_user)
        self.adicionar_metodo(metodo)

    @settings(max_examples=8)
    @given(gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_repeticoes(), gen_peso(),
           gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_repeticoes(), gen_peso())
    def _test_equals(self, d1, t1, f1, r1, p1, d2, t2, f2, r2, p2):
        metodo = self.gerar_metodo_teste("Equals", d1, t1, f1, r1, p1, d2, t2, f2, r2, p2)
        self.adicionar_metodo(metodo)

    @settings(max_examples=8)
    @given(gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_repeticoes(), gen_peso(),
           gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_repeticoes(), gen_peso())
    def _test_compare(self, d1, t1, f1, r1, p1, d2, t2, f2, r2, p2):
        metodo = self.gerar_metodo_teste("CompareTo", d1, t1, f1, r1, p1, d2, t2, f2, r2, p2)
        self.adicionar_metodo(metodo)

    @settings(max_examples=5)
    @given(gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_repeticoes(), gen_peso())
    def _test_toString(self, data, tempo, freq, reps, peso):
        metodo = self.gerar_metodo_teste("ToStringTest", data, tempo, freq, reps, peso)
        self.adicionar_metodo(metodo)

    @settings(max_examples=5)
    @given(gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_repeticoes(), gen_peso())
    def _test_clone(self, data, tempo, freq, reps, peso):
        metodo = self.gerar_metodo_teste("CloneTest", data, tempo, freq, reps, peso)
        self.adicionar_metodo(metodo)

if __name__ == "__main__":
    gen = JUnitAtivRepsPesoTestGenerator()
    gen.gerar_todos_os_testes()