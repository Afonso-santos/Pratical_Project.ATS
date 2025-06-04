from hypothesis import given, strategies as st, settings
from datetime import datetime, date
import os
import string

class UtilizadorProfissionalJUnitTestGenerator:
    def __init__(self, path="../Projeto/generatorTest/Utilizador/", filename="UtilizadorProfissionalGenTest.java"):
        self.path = path
        self.filename = filename
        self.filepath = os.path.join(path, filename)
        self.contador = 1

    @staticmethod
    @st.composite
    def gen_nome(draw):
        """Gera nomes válidos"""
        nomes = ["João", "Maria", "Pedro", "Ana", "Carlos", "Luisa", "Miguel", "Sofia", "Rui", "Catarina"]
        return draw(st.sampled_from(nomes))

    @staticmethod
    @st.composite
    def gen_morada(draw):
        """Gera moradas válidas"""
        ruas = ["Rua das Flores", "Avenida da Liberdade", "Travessa do Sol", "Largo da Paz"]
        numero = draw(st.integers(1, 999))
        rua = draw(st.sampled_from(ruas))
        return f"{rua}, {numero}"

    @staticmethod
    @st.composite
    def gen_email(draw):
        """Gera emails válidos"""
        dominios = ["gmail.com", "hotmail.com", "yahoo.com", "outlook.com"]
        username = draw(st.text(alphabet=string.ascii_lowercase, min_size=3, max_size=10))
        dominio = draw(st.sampled_from(dominios))
        return f"{username}@{dominio}"

    @staticmethod
    @st.composite
    def gen_freq_cardiaca(draw):
        """Gera frequência cardíaca válida (40-200 bpm)"""
        return draw(st.integers(40, 200))

    @staticmethod
    @st.composite
    def gen_peso(draw):
        """Gera peso válido (30-200 kg)"""
        return draw(st.integers(30, 200))

    @staticmethod
    @st.composite
    def gen_altura(draw):
        """Gera altura válida (100-250 cm)"""
        return draw(st.integers(100, 250))

    @staticmethod
    @st.composite
    def gen_data_nascimento(draw):
        """Gera data de nascimento válida (1950-2010)"""
        year = draw(st.integers(1950, 2010))
        month = draw(st.integers(1, 12))
        max_day = 31 if month in [1,3,5,7,8,10,12] else 30 if month != 2 else (29 if year % 4 == 0 else 28)
        day = draw(st.integers(1, max_day))
        return date(year, month, day)

    @staticmethod
    @st.composite
    def gen_genero(draw):
        """Gera gênero válido"""
        return draw(st.sampled_from(['M', 'F']))

    @staticmethod
    @st.composite
    def gen_data_periodo(draw):
        """Gera duas datas para teste de período"""
        year1 = draw(st.integers(2020, 2024))
        month1 = draw(st.integers(1, 12))
        max_day1 = 31 if month1 in [1,3,5,7,8,10,12] else 30 if month1 != 2 else (29 if year1 % 4 == 0 else 28)
        day1 = draw(st.integers(1, max_day1))
        data_inicio = date(year1, month1, day1)
        
        # Data fim deve ser posterior à data início
        year2 = draw(st.integers(year1, 2024))
        month2 = draw(st.integers(1 if year2 == year1 else 1, 12))
        max_day2 = 31 if month2 in [1,3,5,7,8,10,12] else 30 if month2 != 2 else (29 if year2 % 4 == 0 else 28)
        min_day2 = day1 + 1 if (year2 == year1 and month2 == month1) else 1
        day2 = draw(st.integers(min_day2, max_day2))
        data_fim = date(year2, month2, day2)
        
        return data_inicio, data_fim

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
        return '''package Projeto.test.UtilizadorProfissional;

import Projeto.*;
import org.junit.jupiter.api.*;
import java.time.*;
import static org.junit.jupiter.api.Assertions.*;

class UtilizadorProfissionalGenTest {

    @BeforeEach 
    void setUp() { 
        new UtilizadorProfissional().setProximoCodigo(1); 
    }
'''

    def gerar_metodo_teste(self, tipo, *args):
        nome = f"test{tipo}_Generated_{self.contador}"
        
        if tipo == "ConstrutorVazio":
            return f'''
    @Test
    void {nome}() {{
        UtilizadorProfissional u = new UtilizadorProfissional();
        
        assertNotNull(u);
        assertEquals(1.5, u.getFatorMultiplicativo(), 0.01);
        assertTrue(u.getCodUtilizador() > 0);
    }}'''

        elif tipo == "ConstrutorParametrizado":
            nome_val, morada_val, email_val, freq_val, peso_val, altura_val, data_val, genero_val = args
            return f'''
    @Test
    void {nome}() {{
        String nome = "{nome_val}";
        String morada = "{morada_val}";
        String email = "{email_val}";
        int freqCardiaca = {freq_val};
        int peso = {peso_val};
        int altura = {altura_val};
        LocalDate dataNascimento = LocalDate.of({data_val.year}, {data_val.month}, {data_val.day});
        char genero = '{genero_val}';
        
        UtilizadorProfissional u = new UtilizadorProfissional(nome, morada, email, freqCardiaca, peso, altura, dataNascimento, genero);
        
        assertNotNull(u);
        assertEquals(nome, u.getNome());
        assertEquals(morada, u.getMorada());
        assertEquals(email, u.getEmail());
        assertEquals(freqCardiaca, u.getFreqCardiaca());
        assertEquals(peso, u.getPeso(), 0.1);
        assertEquals(altura, u.getAltura());
        assertEquals(dataNascimento, u.getDataNascimento());
        assertEquals(genero, u.getGenero());
        assertEquals(1.5, u.getFatorMultiplicativo(), 0.01);
        assertTrue(u.getCodUtilizador() > 0);
    }}'''

        elif tipo == "ConstrutorCopia":
            nome_val, morada_val, email_val, freq_val, peso_val, altura_val, data_val, genero_val = args
            return f'''
    @Test
    void {nome}() {{
        String nome = "{nome_val}";
        String morada = "{morada_val}";
        String email = "{email_val}";
        int freqCardiaca = {freq_val};
        int peso = {peso_val};
        int altura = {altura_val};
        LocalDate dataNascimento = LocalDate.of({data_val.year}, {data_val.month}, {data_val.day});
        char genero = '{genero_val}';
        
        UtilizadorProfissional original = new UtilizadorProfissional(nome, morada, email, freqCardiaca, peso, altura, dataNascimento, genero);
        UtilizadorProfissional copia = new UtilizadorProfissional(original);
        
        assertNotNull(copia);
        assertEquals(original.getNome(), copia.getNome());
        assertEquals(original.getMorada(), copia.getMorada());
        assertEquals(original.getEmail(), copia.getEmail());
        assertEquals(original.getFreqCardiaca(), copia.getFreqCardiaca());
        assertEquals(original.getPeso(), copia.getPeso(), 0.1);
        assertEquals(original.getAltura(), copia.getAltura());
        assertEquals(original.getDataNascimento(), copia.getDataNascimento());
        assertEquals(original.getGenero(), copia.getGenero());
        assertEquals(original.getCodUtilizador(), copia.getCodUtilizador());
        assertEquals(1.5, copia.getFatorMultiplicativo(), 0.01);
    }}'''

        elif tipo == "ConstrutorPeriodo":
            nome_val, morada_val, email_val, freq_val, peso_val, altura_val, data_val, genero_val, data_inicio, data_fim = args
            return f'''
    @Test
    void {nome}() {{
        String nome = "{nome_val}";
        String morada = "{morada_val}";
        String email = "{email_val}";
        int freqCardiaca = {freq_val};
        int peso = {peso_val};
        int altura = {altura_val};
        LocalDate dataNascimento = LocalDate.of({data_val.year}, {data_val.month}, {data_val.day});
        char genero = '{genero_val}';
        LocalDate dataInicio = LocalDate.of({data_inicio.year}, {data_inicio.month}, {data_inicio.day});
        LocalDate dataFim = LocalDate.of({data_fim.year}, {data_fim.month}, {data_fim.day});
        
        UtilizadorProfissional original = new UtilizadorProfissional(nome, morada, email, freqCardiaca, peso, altura, dataNascimento, genero);
        UtilizadorProfissional periodo = new UtilizadorProfissional(original, dataInicio, dataFim);
        
        assertNotNull(periodo);
        assertEquals(original.getNome(), periodo.getNome());
        assertEquals(original.getMorada(), periodo.getMorada());
        assertEquals(original.getEmail(), periodo.getEmail());
        assertEquals(1.5, periodo.getFatorMultiplicativo(), 0.01);
    }}'''

        elif tipo == "Equals":
            args1, args2 = args[0], args[1]
            nome1, morada1, email1, freq1, peso1, altura1, data1, genero1 = args1
            nome2, morada2, email2, freq2, peso2, altura2, data2, genero2 = args2
            
            # Determina se devem ser iguais
            sao_iguais = (nome1 == nome2 and morada1 == morada2 and email1 == email2 and 
                         freq1 == freq2 and peso1 == peso2 and altura1 == altura2 and 
                         data1 == data2 and genero1 == genero2)
            
            assertion = "assertTrue" if sao_iguais else "assertFalse"
            
            return f'''
    @Test
    void {nome}() {{
        UtilizadorProfissional u1 = new UtilizadorProfissional("{nome1}", "{morada1}", "{email1}", {freq1}, {peso1}, {altura1}, 
                                                 LocalDate.of({data1.year}, {data1.month}, {data1.day}), '{genero1}');
        UtilizadorProfissional u2 = new UtilizadorProfissional("{nome2}", "{morada2}", "{email2}", {freq2}, {peso2}, {altura2}, 
                                                 LocalDate.of({data2.year}, {data2.month}, {data2.day}), '{genero2}');
        
        // Reset códigos para comparação válida
        u1.setCodUtilizador(1);
        u2.setCodUtilizador(1);
        
        {assertion}(u1.equals(u2));
    }}'''

        elif tipo == "GetFatorMultiplicativo":
            return f'''
    @Test
    void {nome}() {{
        UtilizadorProfissional u = new UtilizadorProfissional();
        
        assertEquals(1.5, u.getFatorMultiplicativo(), 0.01);
    }}'''

        elif tipo == "Clone":
            nome_val, morada_val, email_val, freq_val, peso_val, altura_val, data_val, genero_val = args
            return f'''
    @Test
    void {nome}() {{
        UtilizadorProfissional original = new UtilizadorProfissional("{nome_val}", "{morada_val}", "{email_val}", 
                                                      {freq_val}, {peso_val}, {altura_val}, 
                                                      LocalDate.of({data_val.year}, {data_val.month}, {data_val.day}), '{genero_val}');
        
        UtilizadorProfissional clone = (UtilizadorProfissional) original.clone();
        
        assertNotNull(clone);
        assertEquals(original.getNome(), clone.getNome());
        assertEquals(original.getMorada(), clone.getMorada());
        assertEquals(original.getEmail(), clone.getEmail());
        assertEquals(original.getFreqCardiaca(), clone.getFreqCardiaca());
        assertEquals(original.getPeso(), clone.getPeso(), 0.1);
        assertEquals(original.getAltura(), clone.getAltura());
        assertEquals(original.getDataNascimento(), clone.getDataNascimento());
        assertEquals(original.getGenero(), clone.getGenero());
        assertEquals(original.getCodUtilizador(), clone.getCodUtilizador());
        assertEquals(1.5, clone.getFatorMultiplicativo(), 0.01);
        assertNotSame(original, clone);
    }}'''

        elif tipo == "UtilizadorNumPeriodo":
            nome_val, morada_val, email_val, freq_val, peso_val, altura_val, data_val, genero_val, data_inicio, data_fim = args
            return f'''
    @Test
    void {nome}() {{
        UtilizadorProfissional original = new UtilizadorProfissional("{nome_val}", "{morada_val}", "{email_val}", 
                                                      {freq_val}, {peso_val}, {altura_val}, 
                                                      LocalDate.of({data_val.year}, {data_val.month}, {data_val.day}), '{genero_val}');
        LocalDate dataInicio = LocalDate.of({data_inicio.year}, {data_inicio.month}, {data_inicio.day});
        LocalDate dataFim = LocalDate.of({data_fim.year}, {data_fim.month}, {data_fim.day});
        
        UtilizadorProfissional periodo = (UtilizadorProfissional) original.utilizadorNumPeriodo(dataInicio, dataFim);
        
        assertNotNull(periodo);
        assertEquals(original.getNome(), periodo.getNome());
        assertEquals(original.getMorada(), periodo.getMorada());
        assertEquals(original.getEmail(), periodo.getEmail());
        assertEquals(1.5, periodo.getFatorMultiplicativo(), 0.01);
        assertNotSame(original, periodo);
    }}'''

        elif tipo == "ToString":
            nome_val, morada_val, email_val, freq_val, peso_val, altura_val, data_val, genero_val = args
            return f'''
    @Test
    void {nome}() {{
        UtilizadorProfissional u = new UtilizadorProfissional("{nome_val}", "{morada_val}", "{email_val}", 
                                                    {freq_val}, {peso_val}, {altura_val}, 
                                                    LocalDate.of({data_val.year}, {data_val.month}, {data_val.day}), '{genero_val}');
        
        String resultado = u.toString();
        
        assertNotNull(resultado);
        assertTrue(resultado.contains("Profissional"));
        assertTrue(resultado.contains("{nome_val}"));
        assertTrue(resultado.contains("{email_val}"));
    }}'''

    def gerar_todos_os_testes(self):
        self.criar_estrutura_arquivo()
        self._test_construtor_vazio()
        self._test_construtor_parametrizado()
        self._test_construtor_copia()
        self._test_construtor_periodo()
        self._test_equals()
        self._test_get_fator_multiplicativo()
        self._test_clone()
        self._test_utilizador_num_periodo()
        self._test_to_string()
        self.fechar_arquivo()
        print(f"{self.contador-1} testes gerados em {self.filepath}")

    @settings(max_examples=5)
    @given(st.just(None))
    def _test_construtor_vazio(self, dummy):
        metodo = self.gerar_metodo_teste("ConstrutorVazio")
        self.adicionar_metodo(metodo)

    @settings(max_examples=8)
    @given(gen_nome(), gen_morada(), gen_email(), gen_freq_cardiaca(), 
           gen_peso(), gen_altura(), gen_data_nascimento(), gen_genero())
    def _test_construtor_parametrizado(self, nome, morada, email, freq, peso, altura, data, genero):
        metodo = self.gerar_metodo_teste("ConstrutorParametrizado", nome, morada, email, freq, peso, altura, data, genero)
        self.adicionar_metodo(metodo)

    @settings(max_examples=8)
    @given(gen_nome(), gen_morada(), gen_email(), gen_freq_cardiaca(), 
           gen_peso(), gen_altura(), gen_data_nascimento(), gen_genero())
    def _test_construtor_copia(self, nome, morada, email, freq, peso, altura, data, genero):
        metodo = self.gerar_metodo_teste("ConstrutorCopia", nome, morada, email, freq, peso, altura, data, genero)
        self.adicionar_metodo(metodo)

    @settings(max_examples=6)
    @given(gen_nome(), gen_morada(), gen_email(), gen_freq_cardiaca(), 
           gen_peso(), gen_altura(), gen_data_nascimento(), gen_genero(), gen_data_periodo())
    def _test_construtor_periodo(self, nome, morada, email, freq, peso, altura, data, genero, datas_periodo):
        data_inicio, data_fim = datas_periodo
        metodo = self.gerar_metodo_teste("ConstrutorPeriodo", nome, morada, email, freq, peso, altura, data, genero, data_inicio, data_fim)
        self.adicionar_metodo(metodo)

    @settings(max_examples=8)
    @given(st.tuples(gen_nome(), gen_morada(), gen_email(), gen_freq_cardiaca(), 
                     gen_peso(), gen_altura(), gen_data_nascimento(), gen_genero()),
           st.tuples(gen_nome(), gen_morada(), gen_email(), gen_freq_cardiaca(), 
                     gen_peso(), gen_altura(), gen_data_nascimento(), gen_genero()))
    def _test_equals(self, args1, args2):
        metodo = self.gerar_metodo_teste("Equals", args1, args2)
        self.adicionar_metodo(metodo)

    @settings(max_examples=3)
    @given(st.just(None))
    def _test_get_fator_multiplicativo(self, dummy):
        metodo = self.gerar_metodo_teste("GetFatorMultiplicativo")
        self.adicionar_metodo(metodo)

    @settings(max_examples=6)
    @given(gen_nome(), gen_morada(), gen_email(), gen_freq_cardiaca(), 
           gen_peso(), gen_altura(), gen_data_nascimento(), gen_genero())
    def _test_clone(self, nome, morada, email, freq, peso, altura, data, genero):
        metodo = self.gerar_metodo_teste("Clone", nome, morada, email, freq, peso, altura, data, genero)
        self.adicionar_metodo(metodo)

    @settings(max_examples=6)
    @given(gen_nome(), gen_morada(), gen_email(), gen_freq_cardiaca(), 
           gen_peso(), gen_altura(), gen_data_nascimento(), gen_genero(), gen_data_periodo())
    def _test_utilizador_num_periodo(self, nome, morada, email, freq, peso, altura, data, genero, datas_periodo):
        data_inicio, data_fim = datas_periodo
        metodo = self.gerar_metodo_teste("UtilizadorNumPeriodo", nome, morada, email, freq, peso, altura, data, genero, data_inicio, data_fim)
        self.adicionar_metodo(metodo)

    @settings(max_examples=6)
    @given(gen_nome(), gen_morada(), gen_email(), gen_freq_cardiaca(), 
           gen_peso(), gen_altura(), gen_data_nascimento(), gen_genero())
    def _test_to_string(self, nome, morada, email, freq, peso, altura, data, genero):
        metodo = self.gerar_metodo_teste("ToString", nome, morada, email, freq, peso, altura, data, genero)
        self.adicionar_metodo(metodo)

if __name__ == "__main__":
    gen = UtilizadorProfissionalJUnitTestGenerator()
    gen.gerar_todos_os_testes()