from hypothesis import given, strategies as st, settings
from datetime import datetime, time
import os

class JUnitBicepCurlsTestGenerator:
    def __init__(self, path="../Projeto/generatorTest/exercises/", filename="BicepCurlsGenTest.java"):
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
        return draw(st.integers(1, 200))

    @staticmethod
    @st.composite
    def gen_peso(draw):
        return draw(st.floats(1.0, 150.0))

    @staticmethod
    @st.composite
    def gen_consumo_calorias(draw):
        return draw(st.floats(1.0, 1000.0))

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
        return '''package Projeto.test.exercises;

import Projeto.*;
import org.junit.jupiter.api.*;
import java.time.*;
import static org.junit.jupiter.api.Assertions.*;

class BicepCurlsGenTest {

    @BeforeEach 
    void setUp() { 
        new BicepCurls().setProximoCodigo(1); 
    }
'''

    def gerar_metodo_teste(self, tipo, *args):
        nome = f"test{tipo}_Generated_{self.contador}"
        
        if tipo == "ConstrutorVazio":
            return f'''
    @Test
    void {nome}() {{
        BicepCurls b = new BicepCurls();
        assertNotNull(b);
        assertNotNull(b.getDataRealizacao());
        assertNotNull(b.getTempo());
        assertEquals(0, b.getFreqCardiaca());
        assertEquals(0, b.getRepeticoes());
        assertEquals(0.0, b.getPeso(), 0.01);
        assertTrue(b.getCodAtividade() > 0);
    }}'''
        
        elif tipo == "ConstrutorParametrizado":
            return f'''
    @Test
    void {nome}() {{
        LocalDateTime d = LocalDateTime.of({args[0].year}, {args[0].month}, {args[0].day}, {args[0].hour}, {args[0].minute});
        LocalTime t = LocalTime.of({args[1].hour}, {args[1].minute});
        int f = {args[2]};
        int r = {args[3]};
        double p = {args[4]:.2f};
        BicepCurls b = new BicepCurls(d, t, f, r, p);
        assertNotNull(b);
        assertEquals(d, b.getDataRealizacao());
        assertEquals(t, b.getTempo());
        assertEquals(f, b.getFreqCardiaca());
        assertEquals(r, b.getRepeticoes());
        assertEquals(p, b.getPeso(), 0.01);
        assertTrue(b.getCodAtividade() > 0);
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
        BicepCurls original = new BicepCurls(d, t, f, r, p);
        BicepCurls copia = new BicepCurls(original);
        assertNotNull(copia);
        assertEquals(original.getDataRealizacao(), copia.getDataRealizacao());
        assertEquals(original.getTempo(), copia.getTempo());
        assertEquals(original.getFreqCardiaca(), copia.getFreqCardiaca());
        assertEquals(original.getRepeticoes(), copia.getRepeticoes());
        assertEquals(original.getPeso(), copia.getPeso(), 0.01);
        assertNotEquals(original.getCodAtividade(), copia.getCodAtividade());
    }}'''
        
        elif tipo == "Equals":
            d1, t1, f1, r1, p1, d2, t2, f2, r2, p2 = args
            cmp = "assertTrue" if (d1 == d2 and t1 == t2 and f1 == f2 and r1 == r2 and abs(p1 - p2) < 0.01) else "assertFalse"
            return f'''
    @Test
    void {nome}() {{
        LocalDateTime d1 = LocalDateTime.of({d1.year}, {d1.month}, {d1.day}, {d1.hour}, {d1.minute});
        LocalTime t1 = LocalTime.of({t1.hour}, {t1.minute});
        LocalDateTime d2 = LocalDateTime.of({d2.year}, {d2.month}, {d2.day}, {d2.hour}, {d2.minute});
        LocalTime t2 = LocalTime.of({t2.hour}, {t2.minute});
        BicepCurls b1 = new BicepCurls(d1, t1, {f1}, {r1}, {p1:.2f});
        BicepCurls b2 = new BicepCurls(d2, t2, {f2}, {r2}, {p2:.2f});
        {cmp}(b1.equals(b2));
    }}'''
        
        elif tipo == "Clone":
            return f'''
    @Test
    void {nome}() {{
        LocalDateTime d = LocalDateTime.of({args[0].year}, {args[0].month}, {args[0].day}, {args[0].hour}, {args[0].minute});
        LocalTime t = LocalTime.of({args[1].hour}, {args[1].minute});
        int f = {args[2]};
        int r = {args[3]};
        double p = {args[4]:.2f};
        BicepCurls original = new BicepCurls(d, t, f, r, p);
        BicepCurls clone = (BicepCurls) original.clone();
        assertNotNull(clone);
        assertNotSame(original, clone);
        assertEquals(original, clone);
        assertEquals(original.getRepeticoes(), clone.getRepeticoes());
        assertEquals(original.getPeso(), clone.getPeso(), 0.01);
    }}'''
        
        elif tipo == "ConsumoCalorias":
            return f'''
    @Test
    void {nome}() {{
        LocalDateTime d = LocalDateTime.of({args[0].year}, {args[0].month}, {args[0].day}, {args[0].hour}, {args[0].minute});
        LocalTime t = LocalTime.of({args[1].hour}, {args[1].minute});
        int f = {args[2]};
        int r = {args[3]};
        double p = {args[4]:.2f};
        BicepCurls atividade = new BicepCurls(d, t, f, r, p);
        Utilizador utilizador = new UtilizadorAmador();
        utilizador.addAtividade(atividade);
        
        double consumo = atividade.consumoCalorias(utilizador);
        assertTrue(consumo > 0, "Consumo de calorias deve ser positivo");
        
        // Verificar se os fatores estão sendo aplicados corretamente
        double fatorReps = atividade.getFatorRepeticoes(0.5, 0.4);
        double fatorPeso = atividade.getFatorPeso(utilizador, 0.2, 0.4);
        double fatorFreq = atividade.getFatorFreqCardiaca(utilizador);
        double segundos = t.toSecondOfDay();
        double expected = 4 * (utilizador.getFatorMultiplicativo() + fatorReps + fatorPeso + fatorFreq)
                * utilizador.getBMR() / (24 * 60 * 60) * segundos;
        assertEquals(expected, consumo, 0.01);
    }}'''
        
        elif tipo == "GeraAtividade":
            return f'''
    @Test
    void {nome}() {{
        BicepCurls bicepCurls = new BicepCurls();
        Utilizador utilizador = new UtilizadorAmador();
        double consumoCalorias = {args[0]:.2f};
        
        Atividade atividade = bicepCurls.geraAtividade(utilizador, consumoCalorias);
        
        assertNotNull(atividade);
        assertInstanceOf(atividade instanceof BicepCurls);
        assertTrue(atividade.getTempo().toSecondOfDay() > 0);
        assertEquals(0, atividade.getFreqCardiaca());
        
        BicepCurls bicepGen = (BicepCurls) atividade;
        assertTrue(bicepGen.getRepeticoes() > 0);
        assertTrue(bicepGen.getPeso() > 0);
        assertEquals(utilizador.getPeso(), bicepGen.getPeso(), 0.01);
    }}'''
        
        elif tipo == "ToString":
            return f'''
    @Test
    void {nome}() {{
        LocalDateTime d = LocalDateTime.of({args[0].year}, {args[0].month}, {args[0].day}, {args[0].hour}, {args[0].minute});
        LocalTime t = LocalTime.of({args[1].hour}, {args[1].minute});
        int f = {args[2]};
        int r = {args[3]};
        double p = {args[4]:.2f};
        BicepCurls atividade = new BicepCurls(d, t, f, r, p);
        
        String resultado = atividade.toString();
        assertNotNull(resultado);
        assertTrue(resultado.contains("Bicep curls"), "ToString deve conter 'Bicep curls'");
        assertTrue(resultado.length() > 0, "ToString não deve estar vazio");
    }}'''

    def gerar_todos_os_testes(self):
        self.criar_estrutura_arquivo()
        self._test_construtor_vazio()
        self._test_construtor_parametrizado()
        self._test_construtor_copia()
        self._test_equals()
        self._test_clone()
        self._test_consumo_calorias()
        self._test_gera_atividade()
        self._test_to_string()
        self.fechar_arquivo()
        print(f"{self.contador-1} testes gerados em {self.filepath}")

    @settings(max_examples=5)
    @given(st.just(None))
    def _test_construtor_vazio(self, _):
        metodo = self.gerar_metodo_teste("ConstrutorVazio")
        self.adicionar_metodo(metodo)

    @settings(max_examples=10)
    @given(gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_repeticoes(), gen_peso())
    def _test_construtor_parametrizado(self, data, tempo, freq, reps, peso):
        metodo = self.gerar_metodo_teste("ConstrutorParametrizado", data, tempo, freq, reps, peso)
        self.adicionar_metodo(metodo)

    @settings(max_examples=10)
    @given(gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_repeticoes(), gen_peso())
    def _test_construtor_copia(self, data, tempo, freq, reps, peso):
        metodo = self.gerar_metodo_teste("ConstrutorCopia", data, tempo, freq, reps, peso)
        self.adicionar_metodo(metodo)

    @settings(max_examples=10)
    @given(gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_repeticoes(), gen_peso(),
           gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_repeticoes(), gen_peso())
    def _test_equals(self, d1, t1, f1, r1, p1, d2, t2, f2, r2, p2):
        metodo = self.gerar_metodo_teste("Equals", d1, t1, f1, r1, p1, d2, t2, f2, r2, p2)
        self.adicionar_metodo(metodo)

    @settings(max_examples=10)
    @given(gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_repeticoes(), gen_peso())
    def _test_clone(self, data, tempo, freq, reps, peso):
        metodo = self.gerar_metodo_teste("Clone", data, tempo, freq, reps, peso)
        self.adicionar_metodo(metodo)

    @settings(max_examples=10)
    @given(gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_repeticoes(), gen_peso())
    def _test_consumo_calorias(self, data, tempo, freq, reps, peso):
        metodo = self.gerar_metodo_teste("ConsumoCalorias", data, tempo, freq, reps, peso)
        self.adicionar_metodo(metodo)

    @settings(max_examples=10)
    @given(gen_consumo_calorias())
    def _test_gera_atividade(self, consumo):
        metodo = self.gerar_metodo_teste("GeraAtividade", consumo)
        self.adicionar_metodo(metodo)

    @settings(max_examples=10)
    @given(gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_repeticoes(), gen_peso())
    def _test_to_string(self, data, tempo, freq, reps, peso):
        metodo = self.gerar_metodo_teste("ToString", data, tempo, freq, reps, peso)
        self.adicionar_metodo(metodo)

if __name__ == "__main__":
    gen = JUnitBicepCurlsTestGenerator()
    gen.gerar_todos_os_testes()