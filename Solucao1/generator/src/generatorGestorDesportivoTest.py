from hypothesis import given, strategies as st, settings
from datetime import datetime, time, date
import os

class JUnitGestorDesportivoTestGenerator:
    def __init__(self, path="../Projeto/generatorTest/", filename="GestorDesportivoGenTest.java"):
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
    def gen_utilizador_data(draw):
        nome = draw(st.text(min_size=1, max_size=20, alphabet=st.characters(whitelist_categories=('Lu', 'Ll'))))
        morada = draw(st.text(min_size=1, max_size=50, alphabet=st.characters(whitelist_categories=('Lu', 'Ll', 'Nd', 'Pc'))))
        email = draw(st.emails())
        freq_cardiaca = draw(st.integers(50, 200))
        peso = draw(st.integers(40, 150))
        altura = draw(st.integers(150, 220))
        data_nascimento = draw(JUnitGestorDesportivoTestGenerator.gen_data())
        genero = draw(st.sampled_from(['M', 'F']))
        tipo = draw(st.integers(1, 3))
        return nome, morada, email, freq_cardiaca, peso, altura, data_nascimento, genero, tipo

    @staticmethod
    @st.composite
    def gen_atividade_dist_data(draw):
        realizacao = draw(JUnitGestorDesportivoTestGenerator.gen_data_time())
        tempo = draw(JUnitGestorDesportivoTestGenerator.gen_tempo())
        freq_cardiaca = draw(st.integers(50, 200))
        distancia = draw(st.floats(min_value=0.1, max_value=100.0, allow_nan=False, allow_infinity=False))
        tipo = draw(st.integers(1, 2))  # 1=Corrida, 2=Ciclismo
        return realizacao, tempo, freq_cardiaca, distancia, tipo

    @staticmethod
    @st.composite
    def gen_atividade_dist_alt_data(draw):
        realizacao = draw(JUnitGestorDesportivoTestGenerator.gen_data_time())
        tempo = draw(JUnitGestorDesportivoTestGenerator.gen_tempo())
        freq_cardiaca = draw(st.integers(50, 200))
        distancia = draw(st.floats(min_value=0.1, max_value=100.0, allow_nan=False, allow_infinity=False))
        altimetria = draw(st.floats(min_value=0.0, max_value=3000.0, allow_nan=False, allow_infinity=False))
        tipo = draw(st.integers(3, 4))  # 3=Trail, 4=Btt
        return realizacao, tempo, freq_cardiaca, distancia, altimetria, tipo

    @staticmethod
    @st.composite
    def gen_atividade_rep_data(draw):
        realizacao = draw(JUnitGestorDesportivoTestGenerator.gen_data_time())
        tempo = draw(JUnitGestorDesportivoTestGenerator.gen_tempo())
        freq_cardiaca = draw(st.integers(50, 200))
        repeticoes = draw(st.integers(1, 100))
        tipo = draw(st.integers(5, 6))  # 5=Flexoes, 6=Abdominais
        return realizacao, tempo, freq_cardiaca, repeticoes, tipo

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
        return '''package Projeto.test;

import Projeto.*;
import org.junit.jupiter.api.*;
import java.time.*;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

class GestorDesportivoGenTest {

    private GestorDesportivo gestor;

    @BeforeEach
    void setUp() {
        gestor = new GestorDesportivo();
    }
'''

    def gerar_metodo_teste(self, tipo, *args):
        nome = f"test{tipo}_Generated_{self.contador}"
        
        if tipo == "AddUtilizadorAndShow":
            nome_user, morada, email, freq, peso, altura, data_nasc, genero, tipo_user = args
            return f'''
    @Test
    void {nome}() {{
        LocalDate dataNascimento = LocalDate.of({data_nasc.year}, {data_nasc.month}, {data_nasc.day});
        int cod = gestor.addUtilizador("{nome_user}", "{morada}", "{email}", {freq}, {peso}, {altura}, dataNascimento, '{genero}', {tipo_user});
        assertTrue(gestor.existeUtilizador(cod));
        String info = gestor.showUtilizador(cod);
        assertTrue(info.contains("{nome_user}"));
        assertFalse(info.contains("Nao existe utilizador"));
    }}'''

        elif tipo == "AddAtividadeDistAndShow":
            realizacao, tempo, freq, distancia, tipo_ativ = args
            ativ_nome = "Corrida" if tipo_ativ == 1 else "Ciclismo"
            return f'''
    @Test
    void {nome}() {{
        LocalDateTime realizacao = LocalDateTime.of({realizacao.year}, {realizacao.month}, {realizacao.day}, {realizacao.hour}, {realizacao.minute});
        LocalTime tempo = LocalTime.of({tempo.hour}, {tempo.minute});
        int cod = gestor.addAtivDist(realizacao, tempo, {freq}, {distancia}, {tipo_ativ});
        assertTrue(gestor.existeAtividade(cod));
        String info = gestor.showAtividade(cod);
        assertTrue(info.contains("{ativ_nome}"));
        assertFalse(info.contains("Nao existe atividade"));
    }}'''

        elif tipo == "AddAtividadeDistAltAndShow":
            realizacao, tempo, freq, distancia, altimetria, tipo_ativ = args
            ativ_nome = "Trail" if tipo_ativ == 3 else "Btt"
            return f'''
    @Test
    void {nome}() {{
        LocalDateTime realizacao = LocalDateTime.of({realizacao.year}, {realizacao.month}, {realizacao.day}, {realizacao.hour}, {realizacao.minute});
        LocalTime tempo = LocalTime.of({tempo.hour}, {tempo.minute});
        int cod = gestor.addAtivDistAlt(realizacao, tempo, {freq}, {distancia}, {altimetria}, {tipo_ativ});
        assertTrue(gestor.existeAtividade(cod));
        String info = gestor.showAtividade(cod);
        assertTrue(info.contains("{ativ_nome}"));
        assertFalse(info.contains("Nao existe atividade"));
    }}'''

        elif tipo == "AddAtividadeRepAndShow":
            realizacao, tempo, freq, repeticoes, tipo_ativ = args
            ativ_nome = "Flexoes" if tipo_ativ == 5 else "Abdominais"
            return f'''
    @Test
    void {nome}() {{
        LocalDateTime realizacao = LocalDateTime.of({realizacao.year}, {realizacao.month}, {realizacao.day}, {realizacao.hour}, {realizacao.minute});
        LocalTime tempo = LocalTime.of({tempo.hour}, {tempo.minute});
        int cod = gestor.addAtivRep(realizacao, tempo, {freq}, {repeticoes}, {tipo_ativ});
        assertTrue(gestor.existeAtividade(cod));
        String info = gestor.showAtividade(cod);
        assertTrue(info.contains("{ativ_nome}"));
        assertFalse(info.contains("Nao existe atividade"));
    }}'''

        elif tipo == "PlanoTreinoAddAndShow":
            data_plano = args[0]
            return f'''
    @Test
    void {nome}() {{
        LocalDate data = LocalDate.of({data_plano.year}, {data_plano.month}, {data_plano.day});
        int planoCod = gestor.addPlanoTreino(data);
        assertTrue(gestor.existePlano(planoCod));
        String info = gestor.showPlanoTreino(planoCod);
        assertTrue(info.contains("Plano"));
        assertFalse(info.contains("Nao existe plano"));
    }}'''

        elif tipo == "RegistaAtividade":
            nome_user, morada, email, freq_u, peso, altura, data_nasc, genero, tipo_user, realizacao, tempo, freq_a, distancia, tipo_ativ = args
            return f'''
    @Test
    void {nome}() {{
        // Criar utilizador
        LocalDate dataNascimento = LocalDate.of({data_nasc.year}, {data_nasc.month}, {data_nasc.day});
        int codU = gestor.addUtilizador("{nome_user}", "{morada}", "{email}", {freq_u}, {peso}, {altura}, dataNascimento, '{genero}', {tipo_user});
        
        // Criar atividade
        LocalDateTime realizacao = LocalDateTime.of({realizacao.year}, {realizacao.month}, {realizacao.day}, {realizacao.hour}, {realizacao.minute});
        LocalTime tempo = LocalTime.of({tempo.hour}, {tempo.minute});
        int codA = gestor.addAtivDist(realizacao, tempo, {freq_a}, {distancia}, {tipo_ativ});
        
        // Registar atividade no utilizador
        gestor.registaAtividade(codU, codA);
        String atividades = gestor.atividadesUtilizador(codU);
        assertNotNull(atividades);
        assertFalse(atividades.isEmpty());
    }}'''

        elif tipo == "RegistaPlanoTreino":
            nome_user, morada, email, freq_u, peso, altura, data_nasc, genero, tipo_user, data_plano = args
            return f'''
    @Test
    void {nome}() {{
        // Criar utilizador
        LocalDate dataNascimento = LocalDate.of({data_nasc.year}, {data_nasc.month}, {data_nasc.day});
        int codU = gestor.addUtilizador("{nome_user}", "{morada}", "{email}", {freq_u}, {peso}, {altura}, dataNascimento, '{genero}', {tipo_user});
        
        // Criar plano de treino
        LocalDate dataPlano = LocalDate.of({data_plano.year}, {data_plano.month}, {data_plano.day});
        int codP = gestor.addPlanoTreino(dataPlano);
        
        // Registar plano no utilizador
        gestor.registaPlanoTreino(codU, codP);
        assertTrue(gestor.existeUtilizador(codU));
        assertTrue(gestor.existePlano(codP));
    }}'''

        elif tipo == "GuardaEstadoCarregaEstado":
            nome_user, morada, email, freq, peso, altura, data_nasc, genero, tipo_user = args
            return f'''
    @Test
    void {nome}() {{
        try {{
            LocalDate dataNascimento = LocalDate.of({data_nasc.year}, {data_nasc.month}, {data_nasc.day});
            int cod = gestor.addUtilizador("{nome_user}", "{morada}", "{email}", {freq}, {peso}, {altura}, dataNascimento, '{genero}', {tipo_user});
            
            String filename = "test_state_{self.contador}.ser";
            gestor.guardaEstado(filename);
            
            GestorDesportivo carregado = gestor.carregaEstado(filename);
            assertNotNull(carregado);
            assertTrue(carregado.existeUtilizador(cod));
        }} catch (IOException | ClassNotFoundException e) {{
            fail("Erro ao guardar/carregar estado: " + e.getMessage());
        }}
    }}'''

        elif tipo == "ExisteUtilizadorNaoExistente":
            codigo_inexistente = args[0]
            return f'''
    @Test
    void {nome}() {{
        int codigoInexistente = {codigo_inexistente};
        assertFalse(gestor.existeUtilizador(codigoInexistente));
        String info = gestor.showUtilizador(codigoInexistente);
        assertTrue(info.contains("Nao existe utilizador"));
    }}'''

        elif tipo == "ExisteAtividadeNaoExistente":
            codigo_inexistente = args[0]
            return f'''
    @Test
    void {nome}() {{
        int codigoInexistente = {codigo_inexistente};
        assertFalse(gestor.existeAtividade(codigoInexistente));
        String info = gestor.showAtividade(codigoInexistente);
        assertTrue(info.contains("Nao existe atividade"));
    }}'''

        elif tipo == "ExistePlanoNaoExistente":
            codigo_inexistente = args[0]
            return f'''
    @Test
    void {nome}() {{
        int codigoInexistente = {codigo_inexistente};
        assertFalse(gestor.existePlano(codigoInexistente));
        String info = gestor.showPlanoTreino(codigoInexistente);
        assertTrue(info.contains("Nao existe plano"));
    }}'''

    def gerar_todos_os_testes(self):
        self.criar_estrutura_arquivo()
        self._test_add_utilizador_and_show()
        self._test_add_atividade_dist_and_show()
        self._test_add_atividade_dist_alt_and_show()
        self._test_add_atividade_rep_and_show()
        self._test_plano_treino_add_and_show()
        self._test_regista_atividade()
        self._test_regista_plano_treino()
        self._test_guarda_estado_carrega_estado()
        self._test_existe_utilizador_nao_existente()
        self._test_existe_atividade_nao_existente()
        self._test_existe_plano_nao_existente()
        self.fechar_arquivo()
        print(f"{self.contador-1} testes gerados em {self.filepath}")

    @settings(max_examples=10)
    @given(gen_utilizador_data())
    def _test_add_utilizador_and_show(self, dados_utilizador):
        metodo = self.gerar_metodo_teste("AddUtilizadorAndShow", *dados_utilizador)
        self.adicionar_metodo(metodo)

    @settings(max_examples=10)
    @given(gen_atividade_dist_data())
    def _test_add_atividade_dist_and_show(self, dados_atividade):
        metodo = self.gerar_metodo_teste("AddAtividadeDistAndShow", *dados_atividade)
        self.adicionar_metodo(metodo)

    @settings(max_examples=8)
    @given(gen_atividade_dist_alt_data())
    def _test_add_atividade_dist_alt_and_show(self, dados_atividade):
        metodo = self.gerar_metodo_teste("AddAtividadeDistAltAndShow", *dados_atividade)
        self.adicionar_metodo(metodo)

    @settings(max_examples=8)
    @given(gen_atividade_rep_data())
    def _test_add_atividade_rep_and_show(self, dados_atividade):
        metodo = self.gerar_metodo_teste("AddAtividadeRepAndShow", *dados_atividade)
        self.adicionar_metodo(metodo)

    @settings(max_examples=8)
    @given(gen_data())
    def _test_plano_treino_add_and_show(self, data_plano):
        metodo = self.gerar_metodo_teste("PlanoTreinoAddAndShow", data_plano)
        self.adicionar_metodo(metodo)

    @settings(max_examples=8)
    @given(gen_utilizador_data(), gen_atividade_dist_data())
    def _test_regista_atividade(self, dados_utilizador, dados_atividade):
        dados_combinados = dados_utilizador + dados_atividade
        metodo = self.gerar_metodo_teste("RegistaAtividade", *dados_combinados)
        self.adicionar_metodo(metodo)

    @settings(max_examples=8)
    @given(gen_utilizador_data(), gen_data())
    def _test_regista_plano_treino(self, dados_utilizador, data_plano):
        dados_combinados = dados_utilizador + (data_plano,)
        metodo = self.gerar_metodo_teste("RegistaPlanoTreino", *dados_combinados)
        self.adicionar_metodo(metodo)

    @settings(max_examples=8)
    @given(gen_utilizador_data())
    def _test_guarda_estado_carrega_estado(self, dados_utilizador):
        metodo = self.gerar_metodo_teste("GuardaEstadoCarregaEstado", *dados_utilizador)
        self.adicionar_metodo(metodo)

    @settings(max_examples=5)
    @given(st.integers(min_value=9999, max_value=99999))
    def _test_existe_utilizador_nao_existente(self, codigo_inexistente):
        metodo = self.gerar_metodo_teste("ExisteUtilizadorNaoExistente", codigo_inexistente)
        self.adicionar_metodo(metodo)

    @settings(max_examples=5)
    @given(st.integers(min_value=9999, max_value=99999))
    def _test_existe_atividade_nao_existente(self, codigo_inexistente):
        metodo = self.gerar_metodo_teste("ExisteAtividadeNaoExistente", codigo_inexistente)
        self.adicionar_metodo(metodo)

    @settings(max_examples=5)
    @given(st.integers(min_value=9999, max_value=99999))
    def _test_existe_plano_nao_existente(self, codigo_inexistente):
        metodo = self.gerar_metodo_teste("ExistePlanoNaoExistente", codigo_inexistente)
        self.adicionar_metodo(metodo)

if __name__ == "__main__":
    gen = JUnitGestorDesportivoTestGenerator()
    gen.gerar_todos_os_testes()