from hypothesis import given, strategies as st, settings
from datetime import datetime, time
import os

class JUnitTrailTestGenerator:
    def __init__(self, path="../Projeto/generatorTest/exercises/", filename="TrailGenTest.java"):
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
        return time(draw(st.integers(0, 8)), draw(st.integers(0, 59)))

    @staticmethod
    @st.composite
    def gen_freq_cardiaca(draw):
        return draw(st.integers(60, 220))

    @staticmethod
    @st.composite  
    def gen_distancia(draw):
        return draw(st.floats(0.5, 100.0))

    @staticmethod
    @st.composite
    def gen_altimetria(draw):
        return draw(st.floats(0.0, 3000.0))

    @staticmethod
    @st.composite
    def gen_consumo_calorias(draw):
        return draw(st.floats(50.0, 2000.0))

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

class TrailGenTest {

    @BeforeEach 
    void setUp() { 
        new Trail().setProximoCodigo(1); 
    }
'''

    def gerar_metodo_teste(self, tipo, *args):
        nome = f"test{tipo}_Generated_{self.contador}"
        
        if tipo == "ConstrutorVazio":
            return f'''
    @Test
    void {nome}() {{
        Trail trail = new Trail();
        assertNotNull(trail);
        assertNotNull(trail.getDataRealizacao());
        assertNotNull(trail.getTempo());
        assertEquals(0, trail.getFreqCardiaca());
        assertEquals(0.0, trail.getDistancia(), 0.01);
        assertEquals(0.0, trail.getAltimetria(), 0.01);
        assertTrue(trail.getCodAtividade() > 0);
    }}'''
        
        elif tipo == "ConstrutorParametrizado":
            return f'''
    @Test
    void {nome}() {{
        LocalDateTime d = LocalDateTime.of({args[0].year}, {args[0].month}, {args[0].day}, {args[0].hour}, {args[0].minute});
        LocalTime t = LocalTime.of({args[1].hour}, {args[1].minute});
        int freq = {args[2]};
        double distancia = {args[3]:.2f};
        double altimetria = {args[4]:.2f};
        Trail trail = new Trail(d, t, freq, distancia, altimetria);
        assertNotNull(trail);
        assertEquals(d, trail.getDataRealizacao());
        assertEquals(t, trail.getTempo());
        assertEquals(freq, trail.getFreqCardiaca());
        assertEquals(distancia, trail.getDistancia(), 0.01);
        assertEquals(altimetria, trail.getAltimetria(), 0.01);
        assertTrue(trail.getCodAtividade() > 0);
    }}'''
        
        elif tipo == "ConstrutorCopia":
            return f'''
    @Test
    void {nome}() {{
        LocalDateTime d = LocalDateTime.of({args[0].year}, {args[0].month}, {args[0].day}, {args[0].hour}, {args[0].minute});
        LocalTime t = LocalTime.of({args[1].hour}, {args[1].minute});
        int freq = {args[2]};
        double distancia = {args[3]:.2f};
        double altimetria = {args[4]:.2f};
        Trail original = new Trail(d, t, freq, distancia, altimetria);
        Trail copia = new Trail(original);
        assertNotNull(copia);
        assertEquals(original.getDataRealizacao(), copia.getDataRealizacao());
        assertEquals(original.getTempo(), copia.getTempo());
        assertEquals(original.getFreqCardiaca(), copia.getFreqCardiaca());
        assertEquals(original.getDistancia(), copia.getDistancia(), 0.01);
        assertEquals(original.getAltimetria(), copia.getAltimetria(), 0.01);
        assertNotEquals(original.getCodAtividade(), copia.getCodAtividade());
    }}'''
        
        elif tipo == "Equals":
            d1, t1, f1, dist1, alt1, d2, t2, f2, dist2, alt2 = args
            cmp = "assertTrue" if (d1 == d2 and t1 == t2 and f1 == f2 and abs(dist1 - dist2) < 0.01 and abs(alt1 - alt2) < 0.01) else "assertFalse"
            return f'''
    @Test
    void {nome}() {{
        LocalDateTime d1 = LocalDateTime.of({d1.year}, {d1.month}, {d1.day}, {d1.hour}, {d1.minute});
        LocalTime t1 = LocalTime.of({t1.hour}, {t1.minute});
        LocalDateTime d2 = LocalDateTime.of({d2.year}, {d2.month}, {d2.day}, {d2.hour}, {d2.minute});
        LocalTime t2 = LocalTime.of({t2.hour}, {t2.minute});
        Trail trail1 = new Trail(d1, t1, {f1}, {dist1:.2f}, {alt1:.2f});
        Trail trail2 = new Trail(d2, t2, {f2}, {dist2:.2f}, {alt2:.2f});
        {cmp}(trail1.equals(trail2));
    }}'''
        
        elif tipo == "Clone":
            return f'''
    @Test
    void {nome}() {{
        LocalDateTime d = LocalDateTime.of({args[0].year}, {args[0].month}, {args[0].day}, {args[0].hour}, {args[0].minute});
        LocalTime t = LocalTime.of({args[1].hour}, {args[1].minute});
        int freq = {args[2]};
        double distancia = {args[3]:.2f};
        double altimetria = {args[4]:.2f};
        Trail original = new Trail(d, t, freq, distancia, altimetria);
        Trail clone = (Trail) original.clone();
        assertNotNull(clone);
        assertNotSame(original, clone);
        assertEquals(original, clone);
        assertEquals(original.getDistancia(), clone.getDistancia(), 0.01);
        assertEquals(original.getAltimetria(), clone.getAltimetria(), 0.01);
    }}'''
        
        elif tipo == "GetFatorHard":
            return f'''
    @Test
    void {nome}() {{
        LocalDateTime d = LocalDateTime.of({args[0].year}, {args[0].month}, {args[0].day}, {args[0].hour}, {args[0].minute});
        LocalTime t = LocalTime.of({args[1].hour}, {args[1].minute});
        int freq = {args[2]};
        double distancia = {args[3]:.2f};
        double altimetria = {args[4]:.2f};
        Trail trail = new Trail(d, t, freq, distancia, altimetria);
        
        double fatorHard = trail.getFatorHard();
        assertTrue(fatorHard >= 1.15, "Fator hard mínimo deve ser 1.15");
        
        if (altimetria <= 1000) {{
            assertEquals(1.15, fatorHard, 0.01, "Fator hard para altimetria <= 1000 deve ser 1.15");
        }} else if (altimetria <= 2000) {{
            assertEquals(1.25, fatorHard, 0.01, "Fator hard para 1000 < altimetria <= 2000 deve ser 1.25");
        }} else {{
            assertEquals(1.35, fatorHard, 0.01, "Fator hard para altimetria > 2000 deve ser 1.35");
        }}
    }}'''
        
        elif tipo == "ConsumoCalorias":
            return f'''
    @Test
    void {nome}() {{
        LocalDateTime d = LocalDateTime.of({args[0].year}, {args[0].month}, {args[0].day}, {args[0].hour}, {args[0].minute});
        LocalTime t = LocalTime.of({args[1].hour}, {args[1].minute});
        int freq = {args[2]};
        double distancia = {args[3]:.2f};
        double altimetria = {args[4]:.2f};
        Trail trail = new Trail(d, t, freq, distancia, altimetria);
        UtilizadorAmador utilizador = new UtilizadorAmador();
        utilizador.addAtividade(trail);
        
        double consumo = trail.consumoCalorias(utilizador);
        assertTrue(consumo > 0, "Consumo de calorias deve ser positivo");
        
        // Verificar se o cálculo está correto baseado na fórmula da classe
        // MET = 10 para Trail
        double fatorVelocidade = trail.getFatorVelocidade(2.2, 0.22);
        double fatorFreq = trail.getFatorFreqCardiaca(utilizador);
        double fatorAltimetria = trail.getFatorAltimetria();
        double fatorHard = trail.getFatorHard();
        double segundos = t.toSecondOfDay();
        double expected = 10 * (utilizador.getFatorMultiplicativo() + fatorVelocidade + fatorFreq + fatorAltimetria) 
                         * utilizador.getBMR() / (24 * 60 * 60) * fatorHard * segundos;
        assertEquals(expected, consumo, 0.01);
    }}'''
        
        elif tipo == "GeraAtividade":
            return f'''
    @Test
    void {nome}() {{
        Trail trail = new Trail();
        UtilizadorAmador utilizador = new UtilizadorAmador();
        double consumoCalorias = {args[0]:.2f};
        
        Atividade atividade = trail.geraAtividade(utilizador, consumoCalorias);
        
        assertNotNull(atividade);
        assertTrue(atividade instanceof Trail);
        assertTrue(atividade.getTempo().toSecondOfDay() > 0);
        assertEquals(0, atividade.getFreqCardiaca());
        
        Trail trailGen = (Trail) atividade;
        assertTrue(trailGen.getDistancia() > 0);
        assertTrue(trailGen.getAltimetria() >= 0);
        
        // Verificar se a distância é calculada corretamente (tempo * 2.2)
        int tempoSegundos = atividade.getTempo().toSecondOfDay();
        double distanciaEsperada = tempoSegundos * 2.2;
        assertEquals(distanciaEsperada, trailGen.getDistancia(), 0.01);
        
        // Verificar se a altimetria é razoável (80% do máximo histórico ou 0 se não há histórico)
        assertTrue(trailGen.getAltimetria() >= 0);
        assertTrue(trailGen.getAltimetria() <= 3000); // Limite razoável para altimetria
    }}'''
        
        elif tipo == "ToString":
            return f'''
    @Test
    void {nome}() {{
        LocalDateTime d = LocalDateTime.of({args[0].year}, {args[0].month}, {args[0].day}, {args[0].hour}, {args[0].minute});
        LocalTime t = LocalTime.of({args[1].hour}, {args[1].minute});
        int freq = {args[2]};
        double distancia = {args[3]:.2f};
        double altimetria = {args[4]:.2f};
        Trail trail = new Trail(d, t, freq, distancia, altimetria);
        
        String resultado = trail.toString();
        assertNotNull(resultado);
        assertTrue(resultado.contains("Trail"), "ToString deve conter 'Trail'");
        assertTrue(resultado.length() > 0, "ToString não deve estar vazio");
    }}'''

    def gerar_todos_os_testes(self):
        self.criar_estrutura_arquivo()
        self._test_construtor_vazio()
        self._test_construtor_parametrizado()
        self._test_construtor_copia()
        self._test_equals()
        self._test_clone()
        self._test_get_fator_hard()
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
    @given(gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_distancia(), gen_altimetria())
    def _test_construtor_parametrizado(self, data, tempo, freq, distancia, altimetria):
        metodo = self.gerar_metodo_teste("ConstrutorParametrizado", data, tempo, freq, distancia, altimetria)
        self.adicionar_metodo(metodo)

    @settings(max_examples=10)
    @given(gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_distancia(), gen_altimetria())
    def _test_construtor_copia(self, data, tempo, freq, distancia, altimetria):
        metodo = self.gerar_metodo_teste("ConstrutorCopia", data, tempo, freq, distancia, altimetria)
        self.adicionar_metodo(metodo)

    @settings(max_examples=10)
    @given(gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_distancia(), gen_altimetria(),
           gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_distancia(), gen_altimetria())
    def _test_equals(self, d1, t1, f1, dist1, alt1, d2, t2, f2, dist2, alt2):
        metodo = self.gerar_metodo_teste("Equals", d1, t1, f1, dist1, alt1, d2, t2, f2, dist2, alt2)
        self.adicionar_metodo(metodo)

    @settings(max_examples=10)
    @given(gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_distancia(), gen_altimetria())
    def _test_clone(self, data, tempo, freq, distancia, altimetria):
        metodo = self.gerar_metodo_teste("Clone", data, tempo, freq, distancia, altimetria)
        self.adicionar_metodo(metodo)

    @settings(max_examples=15)
    @given(gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_distancia(), gen_altimetria())
    def _test_get_fator_hard(self, data, tempo, freq, distancia, altimetria):
        metodo = self.gerar_metodo_teste("GetFatorHard", data, tempo, freq, distancia, altimetria)
        self.adicionar_metodo(metodo)

    @settings(max_examples=10)
    @given(gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_distancia(), gen_altimetria())
    def _test_consumo_calorias(self, data, tempo, freq, distancia, altimetria):
        metodo = self.gerar_metodo_teste("ConsumoCalorias", data, tempo, freq, distancia, altimetria)
        self.adicionar_metodo(metodo)

    @settings(max_examples=8)
    @given(gen_consumo_calorias())
    def _test_gera_atividade(self, consumo):
        metodo = self.gerar_metodo_teste("GeraAtividade", consumo)
        self.adicionar_metodo(metodo)

    @settings(max_examples=10)
    @given(gen_data(), gen_tempo(), gen_freq_cardiaca(), gen_distancia(), gen_altimetria())
    def _test_to_string(self, data, tempo, freq, distancia, altimetria):
        metodo = self.gerar_metodo_teste("ToString", data, tempo, freq, distancia, altimetria)
        self.adicionar_metodo(metodo)

if __name__ == "__main__":
    gen = JUnitTrailTestGenerator()
    gen.gerar_todos_os_testes()