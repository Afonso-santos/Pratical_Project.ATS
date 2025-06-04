from hypothesis import given, strategies as st, settings
from datetime import datetime, date, time
import os
import random

class PlanoTreinoTestGenerator:
    def __init__(self, path="../Projeto/generatorTest/", filename="PlanoTreinoGenTest.java"):
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
        return date(year, month, day)

    @staticmethod
    @st.composite
    def gen_data_time(draw):
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
    def gen_iteracoes(draw):
        return draw(st.integers(1, 10))

    @staticmethod
    @st.composite
    def gen_freq_cardiaca(draw):
        return draw(st.integers(50, 200))

    @staticmethod
    @st.composite  
    def gen_distancia(draw):
        return draw(st.floats(0.5, 50.0))

    @staticmethod
    @st.composite
    def gen_repeticoes(draw):
        return draw(st.integers(5, 100))

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
        return '''package Projeto.test.PlanoTreino;

import Projeto.*;
import org.junit.jupiter.api.*;
import java.time.*;
import java.util.*;
import java.util.function.Predicate;
import static org.junit.jupiter.api.Assertions.*;

class PlanoTreinoGenTest {

    @BeforeEach 
    void setUp() { 
        new PlanoTreino().setProximoCodigo(1); 
    }
'''

    def gerar_metodo_teste(self, tipo, *args):
        nome = f"test{tipo}_Generated_{self.contador}"
        
        if tipo == "ConstrutorParametrizado":
            data = args[0]
            return f'''
    @Test
    void {nome}() {{
        LocalDate d = LocalDate.of({data.year}, {data.month}, {data.day});
        PlanoTreino p = new PlanoTreino(d);
        assertNotNull(p);
        assertEquals(d, p.getDataRealizacao());
        assertTrue(p.getCodPlano() > 0);
        assertTrue(p.getAtividades().isEmpty());
    }}'''
        
        elif tipo == "ConstrutorCopia":
            data = args[0]
            return f'''
    @Test
    void {nome}() {{
        LocalDate d = LocalDate.of({data.year}, {data.month}, {data.day});
        PlanoTreino original = new PlanoTreino(d);
        PlanoTreino copia = new PlanoTreino(original);
        assertNotNull(copia);
        assertEquals(original.getDataRealizacao(), copia.getDataRealizacao());
        assertEquals(original.getCodPlano(), copia.getCodPlano());
        assertEquals(original.getAtividades().size(), copia.getAtividades().size());
    }}'''
        
        elif tipo == "AddAtividade":
            data_plano, data_atividade, tempo, freq, distancia, iteracoes = args
            return f'''
    @Test
    void {nome}() {{
        LocalDate dp = LocalDate.of({data_plano.year}, {data_plano.month}, {data_plano.day});
        LocalDateTime da = LocalDateTime.of({data_atividade.year}, {data_atividade.month}, {data_atividade.day}, {data_atividade.hour}, {data_atividade.minute});
        LocalTime t = LocalTime.of({tempo.hour}, {tempo.minute});
        PlanoTreino p = new PlanoTreino(dp);
        Atividade a = new Corrida(da, t, {freq}, {distancia:.2f});
        p.addAtividade(a, {iteracoes});
        assertEquals(1, p.getAtividades().size());
    }}'''
        
        elif tipo == "CaloriasDispendidas":
            data_plano, data_atividade, tempo, freq, distancia, iteracoes = args
            return f'''
    @Test
    void {nome}() {{
        LocalDate dp = LocalDate.of({data_plano.year}, {data_plano.month}, {data_plano.day});
        LocalDateTime da = LocalDateTime.of({data_atividade.year}, {data_atividade.month}, {data_atividade.day}, {data_atividade.hour}, {data_atividade.minute});
        LocalTime t = LocalTime.of({tempo.hour}, {tempo.minute});
        PlanoTreino p = new PlanoTreino(dp);
        Utilizador u = new UtilizadorAmador("Teste", "Cidade", "teste@mail.com", 70, 75, 175, LocalDate.of(2000, 1, 1), 'M');
        Atividade a = new Corrida(da, t, {freq}, {distancia:.2f});
        p.addAtividade(a, {iteracoes});
        double calorias = p.caloriasDispendidas(u);
        assertTrue(calorias != 0);
    }}'''
        
        elif tipo == "CompareTo":
            data1, data2 = args
            expected = "assertTrue" if data1 < data2 else "assertFalse" if data1 > data2 else "assertEquals(0,"
            return f'''
    @Test
    void {nome}() {{
        LocalDate d1 = LocalDate.of({data1.year}, {data1.month}, {data1.day});
        LocalDate d2 = LocalDate.of({data2.year}, {data2.month}, {data2.day});
        PlanoTreino p1 = new PlanoTreino(d1);
        PlanoTreino p2 = new PlanoTreino(d2);
        int result = p1.compareTo(p2);

        {'assertEquals(0, result);' if expected == 'assertEquals' else 'assertTrue(result < 0);' if expected == 'assertTrue' else 'assertFalse(result > 0);'}

    }}'''
        
        elif tipo == "GetAtividadesNumPeriodo":
            data_plano, data_atividade, tempo, freq, distancia, data_inicio, data_fim = args
            # Verificar se a atividade está no período
            atividade_no_periodo = data_inicio <= data_atividade.date() <= data_fim
            expected_size = 1 if atividade_no_periodo else 0
            return f'''
    @Test
    void {nome}() {{
        LocalDate dp = LocalDate.of({data_plano.year}, {data_plano.month}, {data_plano.day});
        LocalDateTime da = LocalDateTime.of({data_atividade.year}, {data_atividade.month}, {data_atividade.day}, {data_atividade.hour}, {data_atividade.minute});
        LocalTime t = LocalTime.of({tempo.hour}, {tempo.minute});
        LocalDate inicio = LocalDate.of({data_inicio.year}, {data_inicio.month}, {data_inicio.day});
        LocalDate fim = LocalDate.of({data_fim.year}, {data_fim.month}, {data_fim.day});
        PlanoTreino p = new PlanoTreino(dp);
        Atividade a = new Corrida(da, t, {freq}, {distancia:.2f});
        p.addAtividade(a, 1);
        List<?> atividades = p.getAtividadesNumPeriodo(inicio, fim);
        assertEquals({expected_size}, atividades.size());
    }}'''
        
        elif tipo == "AtividadesQueRespeitamP":
            data_plano, data_atividade1, tempo1, freq1, distancia1, data_atividade2, tempo2, freq2, repeticoes = args
            return f'''
    @Test
    void {nome}() {{
        LocalDate dp = LocalDate.of({data_plano.year}, {data_plano.month}, {data_plano.day});
        LocalDateTime da1 = LocalDateTime.of({data_atividade1.year}, {data_atividade1.month}, {data_atividade1.day}, {data_atividade1.hour}, {data_atividade1.minute});
        LocalDateTime da2 = LocalDateTime.of({data_atividade2.year}, {data_atividade2.month}, {data_atividade2.day}, {data_atividade2.hour}, {data_atividade2.minute});
        LocalTime t1 = LocalTime.of({tempo1.hour}, {tempo1.minute});
        LocalTime t2 = LocalTime.of({tempo2.hour}, {tempo2.minute});
        PlanoTreino p = new PlanoTreino(dp);
        Atividade a1 = new Corrida(da1, t1, {freq1}, {distancia1:.2f});
        Atividade a2 = new Abdominais(da2, t2, {freq2}, {repeticoes});
        p.addAtividade(a1, 2);
        p.addAtividade(a2, 1);
        Predicate<Atividade> predicate = a -> a instanceof Corrida;
        List<Atividade> result = p.atividadesQueRespeitamP(da1.toLocalDate().minusDays(1), da2.toLocalDate().plusDays(1), predicate);
        assertEquals(2, result.size());
    }}'''

    def gerar_todos_os_testes(self):
        self.criar_estrutura_arquivo()
        self._test_construtor_parametrizado()
        self._test_construtor_copia()
        self._test_add_atividade()
        self._test_calorias_dispendidas()
        self._test_compare_to()
        self._test_get_atividades_num_periodo()
        self._test_atividades_que_respeitam_p()
        self.fechar_arquivo()
        print(f"{self.contador-1} testes gerados em {self.filepath}")

    @settings(max_examples=5)
    @given(gen_data())
    def _test_construtor_parametrizado(self, data):
        metodo = self.gerar_metodo_teste("ConstrutorParametrizado", data)
        self.adicionar_metodo(metodo)

    @settings(max_examples=5)
    @given(gen_data())
    def _test_construtor_copia(self, data):
        metodo = self.gerar_metodo_teste("ConstrutorCopia", data)
        self.adicionar_metodo(metodo)

    @settings(max_examples=8)
    @given(gen_data(), gen_data_time(), gen_tempo(), gen_freq_cardiaca(), gen_distancia(), gen_iteracoes())
    def _test_add_atividade(self, data_plano, data_atividade, tempo, freq, distancia, iteracoes):
        metodo = self.gerar_metodo_teste("AddAtividade", data_plano, data_atividade, tempo, freq, distancia, iteracoes)
        self.adicionar_metodo(metodo)

    @settings(max_examples=6)
    @given(gen_data(), gen_data_time(), gen_tempo(), gen_freq_cardiaca(), gen_distancia(), gen_iteracoes())
    def _test_calorias_dispendidas(self, data_plano, data_atividade, tempo, freq, distancia, iteracoes):
        metodo = self.gerar_metodo_teste("CaloriasDispendidas", data_plano, data_atividade, tempo, freq, distancia, iteracoes)
        self.adicionar_metodo(metodo)

    @settings(max_examples=8)
    @given(gen_data(), gen_data())
    def _test_compare_to(self, data1, data2):
        metodo = self.gerar_metodo_teste("CompareTo", data1, data2)
        self.adicionar_metodo(metodo)

    @settings(max_examples=6)
    @given(gen_data(), gen_data_time(), gen_tempo(), gen_freq_cardiaca(), gen_distancia(), gen_data(), gen_data())
    def _test_get_atividades_num_periodo(self, data_plano, data_atividade, tempo, freq, distancia, data_inicio, data_fim):
        # Garantir que data_inicio <= data_fim
        if data_inicio > data_fim:
            data_inicio, data_fim = data_fim, data_inicio
        metodo = self.gerar_metodo_teste("GetAtividadesNumPeriodo", data_plano, data_atividade, tempo, freq, distancia, data_inicio, data_fim)
        self.adicionar_metodo(metodo)

    @settings(max_examples=5)
    @given(gen_data(), gen_data_time(), gen_tempo(), gen_freq_cardiaca(), gen_distancia(), 
           gen_data_time(), gen_tempo(), gen_freq_cardiaca(), gen_repeticoes())
    def _test_atividades_que_respeitam_p(self, data_plano, data_atividade1, tempo1, freq1, distancia1, 
                                       data_atividade2, tempo2, freq2, repeticoes):
        metodo = self.gerar_metodo_teste("AtividadesQueRespeitamP", data_plano, data_atividade1, tempo1, freq1, distancia1,
                                       data_atividade2, tempo2, freq2, repeticoes)
        self.adicionar_metodo(metodo)

if __name__ == "__main__":
    gen = PlanoTreinoTestGenerator()
    gen.gerar_todos_os_testes()