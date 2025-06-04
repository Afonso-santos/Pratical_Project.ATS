from hypothesis import given, strategies as st, settings
from datetime import datetime, time
import os

class JUnitAtivDistanciaTestGenerator:
    def __init__(self, path="../Projeto/generatorTest/Atividades/", filename="AtivDistanciaGenTest.java"):
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
        return draw(st.floats(min_value=0.0, max_value=100.0, allow_nan=False, allow_infinity=False))

    @staticmethod
    @st.composite
    def gen_distancia_com_negativos(draw):
        return draw(st.floats(min_value=-50.0, max_value=100.0, allow_nan=False, allow_infinity=False))

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

class AtivDistanciaGenConcreta extends AtivDistancia {
    public AtivDistanciaGenConcreta() { super(); }
    public AtivDistanciaGenConcreta(LocalDateTime d, LocalTime t, int f, double dist) { super(d, t, f, dist); }
    public AtivDistanciaGenConcreta(AtivDistancia a) { super(a); }
    public double consumoCalorias(Utilizador u) { return 200.0; }
    public Atividade geraAtividade(Utilizador u, double c) { return new AtivDistanciaGenConcreta(this); }
    public Object clone() { return new AtivDistanciaGenConcreta(this); }
}

class AtivDistanciaGenTest {

    @BeforeEach void setUp() { new AtivDistanciaGenConcreta().setProximoCodigo(1); }
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
        double dist = {args[3]};
        AtivDistancia a = new AtivDistanciaGenConcreta(d, t, f, dist);
        assertNotNull(a);
        assertEquals(d, a.getDataRealizacao());
        assertEquals(t, a.getTempo());
        assertEquals(f, a.getFreqCardiaca());
        assertEquals(dist, a.getDistancia(), 0.001);
        assertTrue(a.getCodAtividade() > 0);
    }}'''

        elif tipo == "ConstrutorCopia":
            return f'''
    @Test
    void {nome}() {{
        LocalDateTime d = LocalDateTime.of({args[0].year}, {args[0].month}, {args[0].day}, {args[0].hour}, {args[0].minute});
        LocalTime t = LocalTime.of({args[1].hour}, {args[1].minute});
        int f = {args[2]};
        double dist = {args[3]};
        AtivDistancia a = new AtivDistanciaGenConcreta(d, t, f, dist);
        AtivDistancia c = new AtivDistanciaGenConcreta(a);
        assertNotNull(c);
        assertEquals(a.getDataRealizacao(), c.getDataRealizacao());
        assertEquals(a.getTempo(), c.getTempo());
        assertEquals(a.getFreqCardiaca(), c.getFreqCardiaca());
        assertEquals(a.getDistancia(), c.getDistancia(), 0.001);
        assertNotEquals(a.getCodAtividade(), c.getCodAtividade());
    }}'''

        elif tipo == "SetGetDistancia":
            return f'''
    @Test
    void {nome}() {{
        AtivDistancia a = new AtivDistanciaGenConcreta();
        double dist = {args[0]};
        a.setDistancia(dist);
        if (dist >= 0) {{
            assertEquals(dist, a.getDistancia(), 0.001);
        }} else {{
            assertEquals(0.0, a.getDistancia(), 0.001);
        }}
    }}'''

        elif tipo == "GetVelocidade":
            return f'''
    @Test
    void {nome}() {{
        LocalDateTime d = LocalDateTime.of({args[0].year}, {args[0].month}, {args[0].day}, {args[0].hour}, {args[0].minute});
        LocalTime t = LocalTime.of({args[1].hour}, {args[1].minute});
        double dist = {args[2]};
        AtivDistancia a = new AtivDistanciaGenConcreta(d, t, 100, dist);
        double expectedVelocidade = dist / t.toSecondOfDay();
        assertEquals(expectedVelocidade, a.getVelocidade(), 0.001);
    }}'''

        elif tipo == "FatorVelocidade":
            return f'''
    @Test
    void {nome}() {{
        LocalDateTime d = LocalDateTime.of({args[0].year}, {args[0].month}, {args[0].day}, {args[0].hour}, {args[0].minute});
        LocalTime t = LocalTime.of({args[1].hour}, {args[1].minute});
        double dist = {args[2]};
        AtivDistancia a = new AtivDistanciaGenConcreta(d, t, 100, dist);
        double valorNulo = 0.5;
        double valorIncremento = 1.5;
        double fator = a.getFatorVelocidade(valorNulo, valorIncremento);
        assertTrue(fator >= 0.0);
        if (t.toSecondOfDay() > 0) {{
            double expectedFactor = (dist / t.toSecondOfDay() - valorNulo) * valorIncremento;
            assertEquals(expectedFactor, fator, 0.001);
        }}
    }}'''

        elif tipo == "SetDistanciaNegativa":
            return f'''
    @Test
    void {nome}() {{
        AtivDistancia a = new AtivDistanciaGenConcreta();
        double distanciaNegativa = {args[0]};
        a.setDistancia(distanciaNegativa);
        assertEquals(0.0, a.getDistancia(), 0.001);
    }}'''

        elif tipo == "Equals":
            d1, t1, f1, dist1, d2, t2, f2, dist2 = args
            cmp = "assertTrue" if (d1 == d2 and t1 == t2 and f1 == f2 and abs(dist1 - dist2) < 0.001) else "assertFalse"
            return f'''
    @Test
    void {nome}() {{
        LocalDateTime d1 = LocalDateTime.of({d1.year}, {d1.month}, {d1.day}, {d1.hour}, {d1.minute});
        LocalTime t1 = LocalTime.of({t1.hour}, {t1.minute});
        LocalDateTime d2 = LocalDateTime.of({d2.year}, {d2.month}, {d2.day}, {d2.hour}, {d2.minute});
        LocalTime t2 = LocalTime.of({t2.hour}, {t2.minute});
        AtivDistancia a1 = new AtivDistanciaGenConcreta(d1, t1, {f1}, {dist1});
        AtivDistancia a2 = new AtivDistanciaGenConcreta(d2, t2, {f2}, {dist2});
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
        AtivDistancia a1 = new AtivDistanciaGenConcreta(d1, LocalTime.of(0,30), 60, 10.0);
        AtivDistancia a2 = new AtivDistanciaGenConcreta(d2, LocalTime.of(0,30), 60, 10.0);
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
        double dist = {args[3]};
        AtivDistancia a = new AtivDistanciaGenConcreta(d, t, f, dist);
        String result = a.toString();
        assertNotNull(result);
        assertTrue(result.contains("Distancia"));
        assertTrue(result.contains(String.valueOf(dist)));
        assertTrue(result.contains("metros"));
    }}'''

        elif tipo == "EqualsNull":
            return f'''
    @Test
    void {nome}() {{
        LocalDateTime d = LocalDateTime.of({args[0].year}, {args[0].month}, {args[0].day}, {args[0].hour}, {args[0].minute});
        LocalTime t = LocalTime.of({args[1].hour}, {args[1].minute});
        AtivDistancia a = new AtivDistanciaGenConcreta(d, t, {args[2]}, {args[3]});
        assertFalse(a.equals(null));
        assertTrue(a.equals(a));
        assertFalse(a.equals("String diferente"));
    }}'''

    def gerar_todos_os_testes(self):
        self.criar_estrutura_arquivo()
        self._test_construtor()
        self._test_copia()
        self._test_set_get_distancia()
        self._test_set_distancia_negativa()
        self._test_get_velocidade()
        self._test_fator_velocidade()
        self._test_equals()
        self._test_compare()
        self._test_toString()
        self._test_equals_null()
        self.fechar_arquivo()
        print(f"{self.contador-1} testes gerados em {self.filepath}")

    @settings(max_examples=8)
    @given(gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_distancia())
    def _test_construtor(self, data, tempo, freq, distancia):
        metodo = self.gerar_metodo_teste("ConstrutorParametrizado", data, tempo, freq, distancia)
        self.adicionar_metodo(metodo)

    @settings(max_examples=8)
    @given(gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_distancia())
    def _test_copia(self, data, tempo, freq, distancia):
        metodo = self.gerar_metodo_teste("ConstrutorCopia", data, tempo, freq, distancia)
        self.adicionar_metodo(metodo)

    @settings(max_examples=8)
    @given(gen_distancia_com_negativos())
    def _test_set_get_distancia(self, distancia):
        metodo = self.gerar_metodo_teste("SetGetDistancia", distancia)
        self.adicionar_metodo(metodo)

    @settings(max_examples=5)
    @given(st.floats(min_value=-100.0, max_value=-0.1, allow_nan=False, allow_infinity=False))
    def _test_set_distancia_negativa(self, distancia_negativa):
        metodo = self.gerar_metodo_teste("SetDistanciaNegativa", distancia_negativa)
        self.adicionar_metodo(metodo)

    @settings(max_examples=8)
    @given(gen_data(), gen_tempo(), gen_distancia())
    def _test_get_velocidade(self, data, tempo, distancia):
        # Evitar tempo zero para não ter divisão por zero
        if tempo.hour == 0 and tempo.minute == 0:
            tempo = time(0, 1)  # pelo menos 1 minuto
        metodo = self.gerar_metodo_teste("GetVelocidade", data, tempo, distancia)
        self.adicionar_metodo(metodo)

    @settings(max_examples=8)
    @given(gen_data(), gen_tempo(), gen_distancia())
    def _test_fator_velocidade(self, data, tempo, distancia):
        # Evitar tempo zero para não ter divisão por zero
        if tempo.hour == 0 and tempo.minute == 0:
            tempo = time(0, 1)  # pelo menos 1 minuto
        metodo = self.gerar_metodo_teste("FatorVelocidade", data, tempo, distancia)
        self.adicionar_metodo(metodo)

    @settings(max_examples=8)
    @given(gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_distancia(),
           gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_distancia())
    def _test_equals(self, d1, t1, f1, dist1, d2, t2, f2, dist2):
        metodo = self.gerar_metodo_teste("Equals", d1, t1, f1, dist1, d2, t2, f2, dist2)
        self.adicionar_metodo(metodo)

    @settings(max_examples=8)
    @given(gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_distancia(),
           gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_distancia())
    def _test_compare(self, d1, t1, f1, dist1, d2, t2, f2, dist2):
        metodo = self.gerar_metodo_teste("CompareTo", d1, t1, f1, dist1, d2, t2, f2, dist2)
        self.adicionar_metodo(metodo)

    @settings(max_examples=5)
    @given(gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_distancia())
    def _test_toString(self, data, tempo, freq, distancia):
        metodo = self.gerar_metodo_teste("ToStringTest", data, tempo, freq, distancia)
        self.adicionar_metodo(metodo)

    @settings(max_examples=5)
    @given(gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_distancia())
    def _test_equals_null(self, data, tempo, freq, distancia):
        metodo = self.gerar_metodo_teste("EqualsNull", data, tempo, freq, distancia)
        self.adicionar_metodo(metodo)

if __name__ == "__main__":
    gen = JUnitAtivDistanciaTestGenerator()
    gen.gerar_todos_os_testes()