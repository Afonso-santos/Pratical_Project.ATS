from hypothesis import given, strategies as st, settings
from datetime import datetime, date
import os
import string

class UtilizadorAmadorJUnitTestGenerator:
    def __init__(self, path="../Projeto/generatorTest/Utilizador/", filename="UtilizadorAmadorGenTest.java"):
        self.path = path
        self.filename = filename
        self.filepath = os.path.join(path, filename)
        self.contador = 1

    @staticmethod
    @st.composite
    def gen_nome(draw):
        """Gera nomes válidos"""
        nomes = ["João", "Maria", "Pedro", "Ana", "Carlos", "Luisa", "Miguel", "Sofia", "Rui", "Catarina",
                "Bruno", "Rita", "André", "Inês", "Tiago", "Mariana", "Ricardo", "Joana"]
        return draw(st.sampled_from(nomes))

    @staticmethod
    @st.composite
    def gen_morada(draw):
        """Gera moradas válidas"""
        ruas = ["Rua das Flores", "Avenida da Liberdade", "Travessa do Sol", "Largo da Paz", 
               "Rua do Comércio", "Avenida Central", "Praça da República", "Rua Nova"]
        numero = draw(st.integers(1, 999))
        rua = draw(st.sampled_from(ruas))
        return f"{rua}, {numero}"

    @staticmethod
    @st.composite
    def gen_email(draw):
        """Gera emails válidos"""
        dominios = ["gmail.com", "hotmail.com", "yahoo.com", "outlook.com", "sapo.pt", "live.com"]
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
        """Gera par de datas para períodos (início e fim)"""
        year_inicio = draw(st.integers(2020, 2024))
        month_inicio = draw(st.integers(1, 12))
        max_day_inicio = 31 if month_inicio in [1,3,5,7,8,10,12] else 30 if month_inicio != 2 else (29 if year_inicio % 4 == 0 else 28)
        day_inicio = draw(st.integers(1, max_day_inicio))
        
        data_inicio = date(year_inicio, month_inicio, day_inicio)
        
        # Data fim deve ser após data início
        year_fim = draw(st.integers(year_inicio, 2025))
        if year_fim == year_inicio:
            month_fim = draw(st.integers(month_inicio, 12))
            if month_fim == month_inicio:
                day_fim = draw(st.integers(day_inicio, max_day_inicio))
            else:
                max_day_fim = 31 if month_fim in [1,3,5,7,8,10,12] else 30 if month_fim != 2 else (29 if year_fim % 4 == 0 else 28)
                day_fim = draw(st.integers(1, max_day_fim))
        else:
            month_fim = draw(st.integers(1, 12))
            max_day_fim = 31 if month_fim in [1,3,5,7,8,10,12] else 30 if month_fim != 2 else (29 if year_fim % 4 == 0 else 28)
            day_fim = draw(st.integers(1, max_day_fim))
        
        data_fim = date(year_fim, month_fim, day_fim)
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
        return '''package Projeto.test.UtilizadorAmador;

import Projeto.*;
import org.junit.jupiter.api.*;
import java.time.*;
import static org.junit.jupiter.api.Assertions.*;

class UtilizadorAmadorGenTest {

    @BeforeEach 
    void setUp() { 
        new UtilizadorAmador().setProximoCodigo(1); 
    }
'''

    def gerar_metodo_teste(self, tipo, *args):
        nome = f"test{tipo}_Generated_{self.contador}"
        
        if tipo == "ConstrutorVazio":
            return f'''
    @Test
    void {nome}() {{
        UtilizadorAmador u = new UtilizadorAmador();
        
        assertNotNull(u);
        assertEquals("", u.getNome());
        assertEquals("", u.getMorada());
        assertEquals("", u.getEmail());
        assertEquals(0, u.getFreqCardiaca());
        assertEquals(0.0, u.getPeso(), 0.1);
        assertEquals(0, u.getAltura());
        assertEquals(LocalDate.now(), u.getDataNascimento());
        assertEquals((char)0, u.getGenero());
        assertEquals(1.0, u.getFatorMultiplicativo(), 0.1);
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
        
        UtilizadorAmador u = new UtilizadorAmador(nome, morada, email, freqCardiaca, peso, altura, dataNascimento, genero);
        
        assertNotNull(u);
        assertEquals(nome, u.getNome());
        assertEquals(morada, u.getMorada());
        assertEquals(email, u.getEmail());
        assertEquals(freqCardiaca, u.getFreqCardiaca());
        assertEquals(peso, u.getPeso(), 0.1);
        assertEquals(altura, u.getAltura());
        assertEquals(dataNascimento, u.getDataNascimento());
        assertEquals(genero, u.getGenero());
        assertEquals(1.0, u.getFatorMultiplicativo(), 0.1);
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
        
        UtilizadorAmador original = new UtilizadorAmador(nome, morada, email, freqCardiaca, peso, altura, dataNascimento, genero);
        UtilizadorAmador copia = new UtilizadorAmador(original);
        
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
        assertEquals(1.0, copia.getFatorMultiplicativo(), 0.1);
    }}'''

        elif tipo == "ConstrutorComPeriodo":
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
        
        UtilizadorAmador original = new UtilizadorAmador(nome, morada, email, freqCardiaca, peso, altura, dataNascimento, genero);
        UtilizadorAmador periodo = new UtilizadorAmador(original, dataInicio, dataFim);
        
        assertNotNull(periodo);
        assertEquals(original.getNome(), periodo.getNome());
        assertEquals(original.getMorada(), periodo.getMorada());
        assertEquals(original.getEmail(), periodo.getEmail());
        assertEquals(original.getFreqCardiaca(), periodo.getFreqCardiaca());
        assertEquals(original.getPeso(), periodo.getPeso(), 0.1);
        assertEquals(original.getAltura(), periodo.getAltura());
        assertEquals(original.getDataNascimento(), periodo.getDataNascimento());
        assertEquals(original.getGenero(), periodo.getGenero());
        assertEquals(1.0, periodo.getFatorMultiplicativo(), 0.1);
    }}'''

        elif tipo == "GetFatorMultiplicativo":
            nome_val, morada_val, email_val, freq_val, peso_val, altura_val, data_val, genero_val = args
            return f'''
    @Test
    void {nome}() {{
        UtilizadorAmador u = new UtilizadorAmador("{nome_val}", "{morada_val}", "{email_val}", 
                                                {freq_val}, {peso_val}, {altura_val}, 
                                                LocalDate.of({data_val.year}, {data_val.month}, {data_val.day}), 
                                                '{genero_val}');
        
        assertEquals(1.0, u.getFatorMultiplicativo(), 0.001);
    }}'''

        elif tipo == "Equals":
            args1, args2 = args[0], args[1]
            nome1, morada1, email1, freq1, peso1, altura1, data1, genero1 = args1
            nome2, morada2, email2, freq2, peso2, altura2, data2, genero2 = args2
            
            # Determina se devem ser iguais (baseado no equals da superclasse)
            sao_iguais = (nome1 == nome2 and morada1 == morada2 and email1 == email2 and 
                         freq1 == freq2 and peso1 == peso2 and altura1 == altura2 and 
                         data1 == data2 and genero1 == genero2)
            
            assertion = "assertTrue" if sao_iguais else "assertFalse"
            
            return f'''
    @Test
    void {nome}() {{
        UtilizadorAmador u1 = new UtilizadorAmador("{nome1}", "{morada1}", "{email1}", {freq1}, {peso1}, {altura1}, 
                                                  LocalDate.of({data1.year}, {data1.month}, {data1.day}), '{genero1}');
        UtilizadorAmador u2 = new UtilizadorAmador("{nome2}", "{morada2}", "{email2}", {freq2}, {peso2}, {altura2}, 
                                                  LocalDate.of({data2.year}, {data2.month}, {data2.day}), '{genero2}');
        
        // Reset códigos para comparação válida
        u1.setCodUtilizador(1);
        u2.setCodUtilizador(1);
        
        {assertion}(u1.equals(u2));
    }}'''

        elif tipo == "Clone":
            nome_val, morada_val, email_val, freq_val, peso_val, altura_val, data_val, genero_val = args
            return f'''
    @Test
    void {nome}() {{
        UtilizadorAmador original = new UtilizadorAmador("{nome_val}", "{morada_val}", "{email_val}", 
                                                        {freq_val}, {peso_val}, {altura_val}, 
                                                        LocalDate.of({data_val.year}, {data_val.month}, {data_val.day}), 
                                                        '{genero_val}');
        
        UtilizadorAmador clonado = (UtilizadorAmador) original.clone();
        
        assertNotNull(clonado);
        assertNotSame(original, clonado);
        assertEquals(original.getNome(), clonado.getNome());
        assertEquals(original.getMorada(), clonado.getMorada());
        assertEquals(original.getEmail(), clonado.getEmail());
        assertEquals(original.getFreqCardiaca(), clonado.getFreqCardiaca());
        assertEquals(original.getPeso(), clonado.getPeso(), 0.1);
        assertEquals(original.getAltura(), clonado.getAltura());
        assertEquals(original.getDataNascimento(), clonado.getDataNascimento());
        assertEquals(original.getGenero(), clonado.getGenero());
        assertEquals(original.getFatorMultiplicativo(), clonado.getFatorMultiplicativo(), 0.1);
        assertTrue(clonado instanceof UtilizadorAmador);
    }}'''

        elif tipo == "UtilizadorNumPeriodo":
            nome_val, morada_val, email_val, freq_val, peso_val, altura_val, data_val, genero_val, data_inicio, data_fim = args
            return f'''
    @Test
    void {nome}() {{
        UtilizadorAmador original = new UtilizadorAmador("{nome_val}", "{morada_val}", "{email_val}", 
                                                        {freq_val}, {peso_val}, {altura_val}, 
                                                        LocalDate.of({data_val.year}, {data_val.month}, {data_val.day}), 
                                                        '{genero_val}');
        
        LocalDate dataInicio = LocalDate.of({data_inicio.year}, {data_inicio.month}, {data_inicio.day});
        LocalDate dataFim = LocalDate.of({data_fim.year}, {data_fim.month}, {data_fim.day});
        
        UtilizadorAmador periodo = (UtilizadorAmador) original.utilizadorNumPeriodo(dataInicio, dataFim);
        
        assertNotNull(periodo);
        assertNotSame(original, periodo);
        assertEquals(original.getNome(), periodo.getNome());
        assertEquals(original.getMorada(), periodo.getMorada());
        assertEquals(original.getEmail(), periodo.getEmail());
        assertEquals(original.getFreqCardiaca(), periodo.getFreqCardiaca());
        assertEquals(original.getPeso(), periodo.getPeso(), 0.1);
        assertEquals(original.getAltura(), periodo.getAltura());
        assertEquals(original.getDataNascimento(), periodo.getDataNascimento());
        assertEquals(original.getGenero(), periodo.getGenero());
        assertTrue(periodo instanceof UtilizadorAmador);
    }}'''

    def gerar_todos_os_testes(self):
        self.criar_estrutura_arquivo()
        self._test_construtor_vazio()
        self._test_construtor_parametrizado()
        self._test_construtor_copia()
        self._test_construtor_com_periodo()
        self._test_get_fator_multiplicativo()
        self._test_equals()
        self._test_clone()
        self._test_utilizador_num_periodo()
        self.fechar_arquivo()
        print(f"{self.contador-1} testes gerados em {self.filepath}")

    @settings(max_examples=3)
    @given(st.just(None))
    def _test_construtor_vazio(self, _):
        metodo = self.gerar_metodo_teste("ConstrutorVazio")
        self.adicionar_metodo(metodo)

    @settings(max_examples=8)
    @given(gen_nome(), gen_morada(), gen_email(), gen_freq_cardiaca(), 
           gen_peso(), gen_altura(), gen_data_nascimento(), gen_genero())
    def _test_construtor_parametrizado(self, nome, morada, email, freq, peso, altura, data, genero):
        metodo = self.gerar_metodo_teste("ConstrutorParametrizado", nome, morada, email, freq, peso, altura, data, genero)
        self.adicionar_metodo(metodo)

    @settings(max_examples=6)
    @given(gen_nome(), gen_morada(), gen_email(), gen_freq_cardiaca(), 
           gen_peso(), gen_altura(), gen_data_nascimento(), gen_genero())
    def _test_construtor_copia(self, nome, morada, email, freq, peso, altura, data, genero):
        metodo = self.gerar_metodo_teste("ConstrutorCopia", nome, morada, email, freq, peso, altura, data, genero)
        self.adicionar_metodo(metodo)

    @settings(max_examples=6)
    @given(gen_nome(), gen_morada(), gen_email(), gen_freq_cardiaca(), 
           gen_peso(), gen_altura(), gen_data_nascimento(), gen_genero(), gen_data_periodo())
    def _test_construtor_com_periodo(self, nome, morada, email, freq, peso, altura, data, genero, periodo):
        data_inicio, data_fim = periodo
        metodo = self.gerar_metodo_teste("ConstrutorComPeriodo", nome, morada, email, freq, peso, altura, data, genero, data_inicio, data_fim)
        self.adicionar_metodo(metodo)

    @settings(max_examples=5)
    @given(gen_nome(), gen_morada(), gen_email(), gen_freq_cardiaca(), 
           gen_peso(), gen_altura(), gen_data_nascimento(), gen_genero())
    def _test_get_fator_multiplicativo(self, nome, morada, email, freq, peso, altura, data, genero):
        metodo = self.gerar_metodo_teste("GetFatorMultiplicativo", nome, morada, email, freq, peso, altura, data, genero)
        self.adicionar_metodo(metodo)

    @settings(max_examples=8)
    @given(st.tuples(gen_nome(), gen_morada(), gen_email(), gen_freq_cardiaca(), 
                     gen_peso(), gen_altura(), gen_data_nascimento(), gen_genero()),
           st.tuples(gen_nome(), gen_morada(), gen_email(), gen_freq_cardiaca(), 
                     gen_peso(), gen_altura(), gen_data_nascimento(), gen_genero()))
    def _test_equals(self, args1, args2):
        metodo = self.gerar_metodo_teste("Equals", args1, args2)
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
    def _test_utilizador_num_periodo(self, nome, morada, email, freq, peso, altura, data, genero, periodo):
        data_inicio, data_fim = periodo
        metodo = self.gerar_metodo_teste("UtilizadorNumPeriodo", nome, morada, email, freq, peso, altura, data, genero, data_inicio, data_fim)
        self.adicionar_metodo(metodo)

if __name__ == "__main__":
    gen = UtilizadorAmadorJUnitTestGenerator()
    gen.gerar_todos_os_testes()