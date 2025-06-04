from hypothesis import given, strategies as st, settings
from datetime import datetime, time, date
import os
import string

class JUnitMenuTestGenerator:
    def __init__(self, path="../Projeto/generatorTest/", filename="MenuGenTest.java"):
        self.path = path
        self.filename = filename
        self.filepath = os.path.join(path, filename)
        self.contador = 1

    @staticmethod
    @st.composite
    def gen_opcoes_menu(draw):
        """Gera um array de opções válidas para o menu"""
        # Primeiro elemento é sempre o nome do menu
        nome_menu = draw(st.text(min_size=3, max_size=20, alphabet=st.characters(whitelist_categories=('Lu', 'Ll', 'Zs'))))
        
        # Número de opções do menu (2 a 6 opções + nome + sair)
        num_opcoes = draw(st.integers(2, 6))
        opcoes = [nome_menu.strip()]
        
        for i in range(num_opcoes):
            opcao = draw(st.text(min_size=3, max_size=30, alphabet=st.characters(whitelist_categories=('Lu', 'Ll', 'Zs', 'Nd'))))
            opcoes.append(opcao.strip())
        
        # Última opção é sempre "Sair"
        opcoes.append("Sair")
        return opcoes

    @staticmethod
    @st.composite
    def gen_mensagem_valida(draw):
        """Gera uma mensagem válida para os métodos de input"""
        return draw(st.text(min_size=5, max_size=50, alphabet=st.characters(whitelist_categories=('Lu', 'Ll', 'Zs', 'Nd', 'Po'))))

    @staticmethod
    @st.composite
    def gen_string_input(draw):
        """Gera string de input para testes"""
        return draw(st.text(min_size=1, max_size=100, alphabet=st.characters(whitelist_categories=('Lu', 'Ll', 'Zs', 'Nd', 'Po'))))

    @staticmethod
    @st.composite
    def gen_int_valido(draw):
        """Gera um inteiro válido para testes"""
        return draw(st.integers(-1000000, 1000000))

    @staticmethod
    @st.composite
    def gen_double_valido(draw):
        """Gera um double válido para testes"""
        return draw(st.floats(min_value=-999999.99, max_value=999999.99, allow_nan=False, allow_infinity=False))

    @staticmethod
    @st.composite
    def gen_data_valida(draw):
        """Gera uma data válida no formato dd/MM/yyyy"""
        year = draw(st.integers(1900, 2100))
        month = draw(st.integers(1, 12))
        max_day = 31 if month in [1,3,5,7,8,10,12] else 30 if month != 2 else (29 if year % 4 == 0 else 28)
        day = draw(st.integers(1, max_day))
        return f"{day:02d}/{month:02d}/{year}"

    @staticmethod
    @st.composite
    def gen_tempo_valido(draw):
        """Gera um tempo válido no formato HH:mm ou HH:mm:ss"""
        hour = draw(st.integers(0, 23))
        minute = draw(st.integers(0, 59))
        second = draw(st.integers(0, 59))
        formato = draw(st.sampled_from(['HH:mm', 'HH:mm:ss']))
        
        if formato == 'HH:mm':
            return f"{hour:02d}:{minute:02d}"
        else:
            return f"{hour:02d}:{minute:02d}:{second:02d}"

    @staticmethod
    @st.composite
    def gen_data_hora_valida(draw):
        """Gera uma data e hora válida no formato dd/MM/yyyy HH:mm:ss"""
        year = draw(st.integers(1900, 2100))
        month = draw(st.integers(1, 12))
        max_day = 31 if month in [1,3,5,7,8,10,12] else 30 if month != 2 else (29 if year % 4 == 0 else 28)
        day = draw(st.integers(1, max_day))
        hour = draw(st.integers(0, 23))
        minute = draw(st.integers(0, 59))
        second = draw(st.integers(0, 59))
        return f"{day:02d}/{month:02d}/{year} {hour:02d}:{minute:02d}:{second:02d}"

    def criar_estrutura_arquivo(self):
        """Cria a estrutura inicial do arquivo de teste"""
        os.makedirs(self.path, exist_ok=True)
        with open(self.filepath, "w", encoding="utf-8") as f:
            f.write(self._codigo_inicial())

    def fechar_arquivo(self):
        """Fecha o arquivo de teste"""
        with open(self.filepath, "a", encoding="utf-8") as f:
            f.write("\n}")

    def adicionar_metodo(self, metodo_codigo):
        """Adiciona um método de teste ao arquivo"""
        with open(self.filepath, "a", encoding="utf-8") as f:
            f.write(metodo_codigo)
        self.contador += 1

    def _codigo_inicial(self):
        """Retorna o código inicial do arquivo de teste"""
        return '''package Projeto.test;

import Projeto.*;
import org.junit.jupiter.api.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

class MenuGenTest {

    private Menu menu;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        // Captura a saída do System.out para testes
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        // Restaura a saída original
        System.setOut(originalOut);
    }

    private String getOutput() {
        return outputStream.toString();
    }
'''

    def gerar_metodo_teste(self, tipo, *args):
        """Gera métodos de teste baseados no tipo"""
        nome = f"test{tipo}_Generated_{self.contador}"
        
        if tipo == "ConstrucaoMenu":
            opcoes = args[0]
            opcoes_str = '", "'.join(opcoes)
            return f'''
    @Test
    void {nome}() {{
        String[] opcoes = {{"{opcoes_str}"}};
        Menu menu = new Menu(opcoes);
        assertNotNull(menu);
        assertEquals(-1, menu.getOpcao());
    }}'''

        elif tipo == "GetOpcaoInicial":
            opcoes = args[0]
            opcoes_str = '", "'.join(opcoes)
            return f'''
    @Test
    void {nome}() {{
        String[] opcoes = {{"{opcoes_str}"}};
        Menu menu = new Menu(opcoes);
        assertEquals(-1, menu.getOpcao());
    }}'''

        elif tipo == "PedeStringComEntrada":
            mensagem, entrada = args
            # Escapa aspas na mensagem e entrada
            mensagem_escaped = mensagem.replace('"', '\\"')
            entrada_escaped = entrada.replace('"', '\\"')
            return f'''
    @Test
    void {nome}() {{
        String[] opcoes = {{"Menu Teste", "Opcao 1", "Sair"}};
        Menu menu = new Menu(opcoes);
        
        // Simula entrada do utilizador
        String input = "{entrada_escaped}\\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        String resultado = menu.pedeString("{mensagem_escaped}");
        assertEquals("{entrada_escaped}", resultado);
    }}'''

        elif tipo == "PedeIntComEntrada":
            mensagem, valor = args
            mensagem_escaped = mensagem.replace('"', '\\"')
            return f'''
    @Test
    void {nome}() {{
        String[] opcoes = {{"Menu Teste", "Opcao 1", "Sair"}};
        Menu menu = new Menu(opcoes);
        
        // Simula entrada do utilizador
        String input = "{valor}\\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        int resultado = menu.pedeInt("{mensagem_escaped}");
        assertEquals({valor}, resultado);
    }}'''

        elif tipo == "PedeDoubleComEntrada":
            mensagem, valor = args
            mensagem_escaped = mensagem.replace('"', '\\"')
            return f'''
    @Test
    void {nome}() {{
        String[] opcoes = {{"Menu Teste", "Opcao 1", "Sair"}};
        Menu menu = new Menu(opcoes);
        
        // Simula entrada do utilizador
        String input = "{valor}\\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        double resultado = menu.pedeDouble("{mensagem_escaped}");
        assertEquals({valor}, resultado, 0.001);
    }}'''

        elif tipo == "PedeDataComEntrada":
            mensagem, data_str = args
            mensagem_escaped = mensagem.replace('"', '\\"')
            parts = data_str.split('/')
            return f'''
    @Test
    void {nome}() {{
        String[] opcoes = {{"Menu Teste", "Opcao 1", "Sair"}};
        Menu menu = new Menu(opcoes);
        
        // Simula entrada do utilizador
        String input = "{data_str}\\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        LocalDate resultado = menu.pedeData("{mensagem_escaped}");
        LocalDate esperado = LocalDate.of({parts[2]}, {int(parts[1])}, {int(parts[0])});
        assertEquals(esperado, resultado);
    }}'''

        elif tipo == "PedeTempoComEntrada":
            mensagem, tempo_str = args
            mensagem_escaped = mensagem.replace('"', '\\"')
            return f'''
    @Test
    void {nome}() {{
        String[] opcoes = {{"Menu Teste", "Opcao 1", "Sair"}};
        Menu menu = new Menu(opcoes);
        
        // Simula entrada do utilizador
        String input = "{tempo_str}\\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        LocalTime resultado = menu.pedeTempo("{mensagem_escaped}");
        LocalTime esperado = LocalTime.parse("{tempo_str}");
        assertEquals(esperado, resultado);
    }}'''

        elif tipo == "PedeDataHoraComEntrada":
            mensagem, data_hora_str = args
            mensagem_escaped = mensagem.replace('"', '\\"')
            return f'''
    @Test
    void {nome}() {{
        String[] opcoes = {{"Menu Teste", "Opcao 1", "Sair"}};
        Menu menu = new Menu(opcoes);
        
        // Simula entrada do utilizador
        String input = "{data_hora_str}\\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        LocalDateTime resultado = menu.pedeDataHora("{mensagem_escaped}");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime esperado = LocalDateTime.parse("{data_hora_str}", formatter);
        assertEquals(esperado, resultado);
    }}'''

        elif tipo == "PedeIntComEntradaInvalida":
            mensagem = args[0]
            mensagem_escaped = mensagem.replace('"', '\\"')
            return f'''
    @Test
    void {nome}() {{
        String[] opcoes = {{"Menu Teste", "Opcao 1", "Sair"}};
        Menu menu = new Menu(opcoes);
        
        // Simula entrada inválida seguida de entrada válida
        String input = "abc\\n42\\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        int resultado = menu.pedeInt("{mensagem_escaped}");
        assertEquals(42, resultado);
        
        String output = getOutput();
        assertTrue(output.contains("Inserir um número inteiro"));
    }}'''

        elif tipo == "PedeDoubleComEntradaInvalida":
            mensagem = args[0]
            mensagem_escaped = mensagem.replace('"', '\\"')
            return f'''
    @Test
    void {nome}() {{
        String[] opcoes = {{"Menu Teste", "Opcao 1", "Sair"}};
        Menu menu = new Menu(opcoes);
        
        // Simula entrada inválida seguida de entrada válida
        String input = "abc\\n3.14\\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        double resultado = menu.pedeDouble("{mensagem_escaped}");
        assertEquals(3.14, resultado, 0.001);
        
        String output = getOutput();
        assertTrue(output.contains("Inserir um número"));
    }}'''

        elif tipo == "PedeDataComEntradaInvalida":
            mensagem, data_valida = args
            mensagem_escaped = mensagem.replace('"', '\\"')
            parts = data_valida.split('/')
            return f'''
    @Test
    void {nome}() {{
        String[] opcoes = {{"Menu Teste", "Opcao 1", "Sair"}};
        Menu menu = new Menu(opcoes);
        
        // Simula entrada inválida seguida de entrada válida
        String input = "data_invalida\\n{data_valida}\\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        LocalDate resultado = menu.pedeData("{mensagem_escaped}");
        LocalDate esperado = LocalDate.of({parts[2]}, {int(parts[1])}, {int(parts[0])});
        assertEquals(esperado, resultado);
        
        String output = getOutput();
        assertTrue(output.contains("Inserir uma data no formato dia/mês/ano"));
    }}'''

    def gerar_todos_os_testes(self):
        """Gera todos os tipos de testes"""
        self.criar_estrutura_arquivo()
        self._test_construcao_menu()
        self._test_get_opcao_inicial()
        self._test_pede_string_com_entrada()
        self._test_pede_int_com_entrada()
        self._test_pede_double_com_entrada()
        self._test_pede_data_com_entrada()
        self._test_pede_tempo_com_entrada()
        self._test_pede_data_hora_com_entrada()
        self._test_pede_int_com_entrada_invalida()
        self._test_pede_double_com_entrada_invalida()
        self._test_pede_data_com_entrada_invalida()
        self.fechar_arquivo()
        print(f"{self.contador-1} testes gerados em {self.filepath}")

    @settings(max_examples=8)
    @given(gen_opcoes_menu())
    def _test_construcao_menu(self, opcoes):
        metodo = self.gerar_metodo_teste("ConstrucaoMenu", opcoes)
        self.adicionar_metodo(metodo)

    @settings(max_examples=8)
    @given(gen_opcoes_menu())
    def _test_get_opcao_inicial(self, opcoes):
        metodo = self.gerar_metodo_teste("GetOpcaoInicial", opcoes)
        self.adicionar_metodo(metodo)

    @settings(max_examples=10)
    @given(gen_mensagem_valida(), gen_string_input())
    def _test_pede_string_com_entrada(self, mensagem, entrada):
        metodo = self.gerar_metodo_teste("PedeStringComEntrada", mensagem, entrada)
        self.adicionar_metodo(metodo)

    @settings(max_examples=10)
    @given(gen_mensagem_valida(), gen_int_valido())
    def _test_pede_int_com_entrada(self, mensagem, valor):
        metodo = self.gerar_metodo_teste("PedeIntComEntrada", mensagem, valor)
        self.adicionar_metodo(metodo)

    @settings(max_examples=10)
    @given(gen_mensagem_valida(), gen_double_valido())
    def _test_pede_double_com_entrada(self, mensagem, valor):
        metodo = self.gerar_metodo_teste("PedeDoubleComEntrada", mensagem, valor)
        self.adicionar_metodo(metodo)

    @settings(max_examples=8)
    @given(gen_mensagem_valida(), gen_data_valida())
    def _test_pede_data_com_entrada(self, mensagem, data_str):
        metodo = self.gerar_metodo_teste("PedeDataComEntrada", mensagem, data_str)
        self.adicionar_metodo(metodo)

    @settings(max_examples=8)
    @given(gen_mensagem_valida(), gen_tempo_valido())
    def _test_pede_tempo_com_entrada(self, mensagem, tempo_str):
        metodo = self.gerar_metodo_teste("PedeTempoComEntrada", mensagem, tempo_str)
        self.adicionar_metodo(metodo)

    @settings(max_examples=6)
    @given(gen_mensagem_valida(), gen_data_hora_valida())
    def _test_pede_data_hora_com_entrada(self, mensagem, data_hora_str):
        metodo = self.gerar_metodo_teste("PedeDataHoraComEntrada", mensagem, data_hora_str)
        self.adicionar_metodo(metodo)

    @settings(max_examples=5)
    @given(gen_mensagem_valida())
    def _test_pede_int_com_entrada_invalida(self, mensagem):
        metodo = self.gerar_metodo_teste("PedeIntComEntradaInvalida", mensagem)
        self.adicionar_metodo(metodo)

    @settings(max_examples=5)
    @given(gen_mensagem_valida())
    def _test_pede_double_com_entrada_invalida(self, mensagem):
        metodo = self.gerar_metodo_teste("PedeDoubleComEntradaInvalida", mensagem)
        self.adicionar_metodo(metodo)

    @settings(max_examples=5)
    @given(gen_mensagem_valida(), gen_data_valida())
    def _test_pede_data_com_entrada_invalida(self, mensagem, data_valida):
        metodo = self.gerar_metodo_teste("PedeDataComEntradaInvalida", mensagem, data_valida)
        self.adicionar_metodo(metodo)

if __name__ == "__main__":
    gen = JUnitMenuTestGenerator()
    gen.gerar_todos_os_testes()
