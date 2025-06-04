from hypothesis import given, strategies as st, settings
from datetime import datetime, date
import os

class JUnitUtilizadorTestGenerator:
    def __init__(self, path="../Projeto/generatorTest/Utilizadores/", filename="UtilizadorGenTest.java"):
        self.path = path
        self.filename = filename
        self.filepath = os.path.join(path, filename)
        self.contador = 1

    @staticmethod
    @st.composite
    def gen_nome(draw):
        nomes = ["João", "Maria", "Pedro", "Ana", "Carlos", "Sofia", "Miguel", "Beatriz", 
                "António", "Catarina", "Francisco", "Inês", "Manuel", "Rita", "José", "Sara"]
        return draw(st.sampled_from(nomes))

    @staticmethod
    @st.composite
    def gen_morada(draw):
        ruas = ["Rua das Flores", "Avenida Central", "Travessa do Sol", "Largo da Paz", 
               "Rua Nova", "Avenida da Liberdade", "Rua do Porto", "Praça da República"]
        numero = draw(st.integers(1, 999))
        rua = draw(st.sampled_from(ruas))
        return f"{rua}, {numero}"

    @staticmethod
    @st.composite
    def gen_email(draw):
        usuarios = ["joao", "maria", "pedro", "ana", "carlos", "sofia", "test", "user"]
        dominios = ["gmail.com", "hotmail.com", "yahoo.com", "example.com", "test.pt"]
        usuario = draw(st.sampled_from(usuarios))
        dominio = draw(st.sampled_from(dominios))
        numero = draw(st.integers(1, 999))
        return f"{usuario}{numero}@{dominio}"

    @staticmethod
    @st.composite
    def gen_freq_cardiaca(draw):
        return draw(st.integers(40, 200))

    @staticmethod
    @st.composite
    def gen_peso(draw):
        return draw(st.integers(30, 200))

    @staticmethod
    @st.composite
    def gen_altura(draw):
        return draw(st.integers(120, 220))

    @staticmethod
    @st.composite
    def gen_data_nascimento(draw):
        year = draw(st.integers(1950, 2005))
        month = draw(st.integers(1, 12))
        max_day = 31 if month in [1,3,5,7,8,10,12] else 30 if month != 2 else (29 if year % 4 == 0 else 28)
        day = draw(st.integers(1, max_day))
        return date(year, month, day)

    @staticmethod
    @st.composite
    def gen_genero(draw):
        return draw(st.sampled_from(['M', 'F']))

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
        return '''package Projeto.test.Users;

import Projeto.Utilizador;
import org.junit.jupiter.api.*;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class UtilizadorGenConcreta extends Utilizador {
    public UtilizadorGenConcreta() { super(); }
    
    public UtilizadorGenConcreta(String nome, String morada, String email, int freqCardiaca, int peso, int altura, LocalDate dataNascimento, char genero) {
        super(nome, morada, email, freqCardiaca, peso, altura, dataNascimento, genero);
    }
    
    public UtilizadorGenConcreta(Utilizador utilizador) { super(utilizador); }

    
    public UtilizadorGenConcreto(Projeto.test.Utilizador.UtilizadorGenConcreto utilizadorGenConcreto, LocalDate dataInicio, LocalDate dataFim) {}
    
    @Override
    public Object clone() { return new UtilizadorGenConcreta(this); }
    
    @Override
    public Object utilizadorNumPeriodo(LocalDate dataI, LocalDate dataF) { return null; }
    
    @Override
    public double getFatorMultiplicativo() { return 1.0; }
}

class UtilizadorGenTest {

    @BeforeEach 
    void setUp() { 
        new UtilizadorGenConcreta().setProximoCodigo(1); 
    }
'''

    def gerar_metodo_teste(self, tipo, *args):
        nome = f"test{tipo}_Generated_{self.contador}"
        
        if tipo == "ConstrutorParametrizado":
            nome_str, morada_str, email_str, freq, peso, altura, data_nasc, genero = args
            return f'''
    @Test
    void {nome}() {{
        String nome = "{nome_str}";
        String morada = "{morada_str}";
        String email = "{email_str}";
        int freqCardiaca = {freq};
        int peso = {peso};
        int altura = {altura};
        LocalDate dataNascimento = LocalDate.of({data_nasc.year}, {data_nasc.month}, {data_nasc.day});
        char genero = '{genero}';
        
        Utilizador u = new UtilizadorGenConcreta(nome, morada, email, freqCardiaca, peso, altura, dataNascimento, genero);
        
        assertNotNull(u);
        assertEquals(nome, u.getNome());
        assertEquals(morada, u.getMorada());
        assertEquals(email, u.getEmail());
        assertEquals(freqCardiaca, u.getFreqCardiaca());
        assertEquals(peso, u.getPeso());
        assertEquals(altura, u.getAltura());
        assertEquals(dataNascimento, u.getDataNascimento());
        assertEquals(genero, u.getGenero());
        assertTrue(u.getCodUtilizador() > 0);
    }}'''

        elif tipo == "ConstrutorCopia":
            nome_str, morada_str, email_str, freq, peso, altura, data_nasc, genero = args
            return f'''
    @Test
    void {nome}() {{
        Utilizador original = new UtilizadorGenConcreta("{nome_str}", "{morada_str}", "{email_str}", 
                                                        {freq}, {peso}, {altura}, 
                                                        LocalDate.of({data_nasc.year}, {data_nasc.month}, {data_nasc.day}), '{genero}');
        Utilizador copia = new UtilizadorGenConcreta(original);
        
        assertNotNull(copia);
        assertEquals(original.getNome(), copia.getNome());
        assertEquals(original.getMorada(), copia.getMorada());
        assertEquals(original.getEmail(), copia.getEmail());
        assertEquals(original.getFreqCardiaca(), copia.getFreqCardiaca());
        assertEquals(original.getPeso(), copia.getPeso());
        assertEquals(original.getAltura(), copia.getAltura());
        assertEquals(original.getDataNascimento(), copia.getDataNascimento());
        assertEquals(original.getGenero(), copia.getGenero());
        assertEquals(original.getCodUtilizador(), copia.getCodUtilizador());
    }}'''

        elif tipo == "GettersSetters":
            nome_str, morada_str, email_str, freq, peso, altura, data_nasc, genero = args
            novo_nome = nome_str + "_Novo"
            nova_morada = morada_str + "_Nova"
            novo_email = "novo_" + email_str
            return f'''
    @Test
    void {nome}() {{
        Utilizador u = new UtilizadorGenConcreta();
        
        u.setNome("{novo_nome}");
        assertEquals("{novo_nome}", u.getNome());
        
        u.setMorada("{nova_morada}");
        assertEquals("{nova_morada}", u.getMorada());
        
        u.setEmail("{novo_email}");
        assertEquals("{novo_email}", u.getEmail());
        
        u.setFreqCardiaca({freq});
        assertEquals({freq}, u.getFreqCardiaca());
        
        u.setPeso({peso});
        assertEquals({peso}, u.getPeso());
        
        u.setAltura({altura});
        assertEquals({altura}, u.getAltura());
        
        LocalDate novaData = LocalDate.of({data_nasc.year}, {data_nasc.month}, {data_nasc.day});
        u.setDataNascimento(novaData);
        assertEquals(novaData, u.getDataNascimento());
        
        u.setGenero('{genero}');
        assertEquals('{genero}', u.getGenero());
    }}'''

        elif tipo == "BMR":
            nome_str, morada_str, email_str, freq, peso, altura, data_nasc, genero = args
            # Calcular BMR esperado
            idade = 2025 - data_nasc.year  # Aproximação da idade
            s = 5 if genero == 'M' else -161 if genero == 'F' else 0
            bmr_esperado = 10 * peso + 6.25 * altura + 5 * idade + s
            
            return f'''
    @Test
    void {nome}() {{
        Utilizador u = new UtilizadorGenConcreta("{nome_str}", "{morada_str}", "{email_str}", 
                                                 {freq}, {peso}, {altura}, 
                                                 LocalDate.of({data_nasc.year}, {data_nasc.month}, {data_nasc.day}), '{genero}');
        double bmrEsperado = {bmr_esperado:.1f};
        assertEquals(bmrEsperado, u.getBMR(), 1.0); // Delta de 1.0 para compensar aproximações
    }}'''

        elif tipo == "Equals":
            nome1, morada1, email1, freq1, peso1, altura1, data1, genero1 = args[:8]
            nome2, morada2, email2, freq2, peso2, altura2, data2, genero2 = args[8:]
            
            # Determinar se devem ser iguais (todos os campos iguais)
            sao_iguais = (nome1 == nome2 and morada1 == morada2 and email1 == email2 and 
                         freq1 == freq2 and peso1 == peso2 and altura1 == altura2 and 
                         data1 == data2 and genero1 == genero2)
            
            expectativa = "assertTrue" if sao_iguais else "assertFalse"
            
            return f'''
    @Test
    void {nome}() {{
        Utilizador u1 = new UtilizadorGenConcreta("{nome1}", "{morada1}", "{email1}", 
                                                  {freq1}, {peso1}, {altura1}, 
                                                  LocalDate.of({data1.year}, {data1.month}, {data1.day}), '{genero1}');
        // Forçar mesmo código para teste de equals
        u1.setCodUtilizador(1);
        
        Utilizador u2 = new UtilizadorGenConcreta("{nome2}", "{morada2}", "{email2}", 
                                                  {freq2}, {peso2}, {altura2}, 
                                                  LocalDate.of({data2.year}, {data2.month}, {data2.day}), '{genero2}');
        u2.setCodUtilizador(1);
        
        {expectativa}(u1.equals(u2));
    }}'''

        elif tipo == "Clone":
            nome_str, morada_str, email_str, freq, peso, altura, data_nasc, genero = args
            return f'''
    @Test
    void {nome}() {{
        Utilizador original = new UtilizadorGenConcreta("{nome_str}", "{morada_str}", "{email_str}", 
                                                        {freq}, {peso}, {altura}, 
                                                        LocalDate.of({data_nasc.year}, {data_nasc.month}, {data_nasc.day}), '{genero}');
        Utilizador clone = (Utilizador) original.clone();
        
        assertNotNull(clone);
        assertEquals(original, clone);
        assertNotSame(original, clone); // Verificar que são objetos diferentes
    }}'''

    def gerar_todos_os_testes(self):
        self.criar_estrutura_arquivo()
        self._test_construtor_parametrizado()
        self._test_construtor_copia()
        self._test_getters_setters()
        self._test_bmr()
        self._test_equals()
        self._test_clone()
        self.fechar_arquivo()
        print(f"{self.contador-1} testes gerados em {self.filepath}")

    @settings(max_examples=5)
    @given(gen_nome(), gen_morada(), gen_email(), gen_freq_cardiaca(), 
           gen_peso(), gen_altura(), gen_data_nascimento(), gen_genero())
    def _test_construtor_parametrizado(self, nome, morada, email, freq, peso, altura, data_nasc, genero):
        metodo = self.gerar_metodo_teste("ConstrutorParametrizado", nome, morada, email, freq, peso, altura, data_nasc, genero)
        self.adicionar_metodo(metodo)

    @settings(max_examples=5)
    @given(gen_nome(), gen_morada(), gen_email(), gen_freq_cardiaca(), 
           gen_peso(), gen_altura(), gen_data_nascimento(), gen_genero())
    def _test_construtor_copia(self, nome, morada, email, freq, peso, altura, data_nasc, genero):
        metodo = self.gerar_metodo_teste("ConstrutorCopia", nome, morada, email, freq, peso, altura, data_nasc, genero)
        self.adicionar_metodo(metodo)

    @settings(max_examples=5)
    @given(gen_nome(), gen_morada(), gen_email(), gen_freq_cardiaca(), 
           gen_peso(), gen_altura(), gen_data_nascimento(), gen_genero())
    def _test_getters_setters(self, nome, morada, email, freq, peso, altura, data_nasc, genero):
        metodo = self.gerar_metodo_teste("GettersSetters", nome, morada, email, freq, peso, altura, data_nasc, genero)
        self.adicionar_metodo(metodo)

    @settings(max_examples=5)
    @given(gen_nome(), gen_morada(), gen_email(), gen_freq_cardiaca(), 
           gen_peso(), gen_altura(), gen_data_nascimento(), gen_genero())
    def _test_bmr(self, nome, morada, email, freq, peso, altura, data_nasc, genero):
        metodo = self.gerar_metodo_teste("BMR", nome, morada, email, freq, peso, altura, data_nasc, genero)
        self.adicionar_metodo(metodo)

    @settings(max_examples=5)
    @given(gen_nome(), gen_morada(), gen_email(), gen_freq_cardiaca(), 
           gen_peso(), gen_altura(), gen_data_nascimento(), gen_genero(),
           gen_nome(), gen_morada(), gen_email(), gen_freq_cardiaca(), 
           gen_peso(), gen_altura(), gen_data_nascimento(), gen_genero())
    def _test_equals(self, nome1, morada1, email1, freq1, peso1, altura1, data1, genero1,
                           nome2, morada2, email2, freq2, peso2, altura2, data2, genero2):
        metodo = self.gerar_metodo_teste("Equals", nome1, morada1, email1, freq1, peso1, altura1, data1, genero1,
                                                  nome2, morada2, email2, freq2, peso2, altura2, data2, genero2)
        self.adicionar_metodo(metodo)

    @settings(max_examples=5)
    @given(gen_nome(), gen_morada(), gen_email(), gen_freq_cardiaca(), 
           gen_peso(), gen_altura(), gen_data_nascimento(), gen_genero())
    def _test_clone(self, nome, morada, email, freq, peso, altura, data_nasc, genero):
        metodo = self.gerar_metodo_teste("Clone", nome, morada, email, freq, peso, altura, data_nasc, genero)
        self.adicionar_metodo(metodo)

if __name__ == "__main__":
    gen = JUnitUtilizadorTestGenerator()
    gen.gerar_todos_os_testes()