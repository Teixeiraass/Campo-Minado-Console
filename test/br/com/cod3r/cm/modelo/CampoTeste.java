package br.com.cod3r.cm.modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.cod3r.cm.excecao.ExplosaoException;

public class CampoTeste {

    private Campo campo;

    @BeforeEach
    void iniciarCampo(){
        campo = new Campo(3, 3);
    }

    @Test
    void testeVizinhoDistancia1Esquerda(){
        Campo vizinho = new Campo(3, 2);
        boolean resultado = campo.adicionarVizinho(vizinho);
        assertTrue(resultado);
    }

    @Test
    void testeVizinhoDistancia1Direita(){
        Campo vizinho = new Campo(3, 4);
        boolean resultado = campo.adicionarVizinho(vizinho);
        assertTrue(resultado);
    }

    @Test
    void testeVizinhoDistancia1EmCima(){
        Campo vizinho = new Campo(2, 3);
        boolean resultado = campo.adicionarVizinho(vizinho);
        assertTrue(resultado);
    }

    @Test
    void testeVizinhoDistancia1EmBaixo(){
        Campo vizinho = new Campo(4, 3);
        boolean resultado = campo.adicionarVizinho(vizinho);
        assertTrue(resultado);
    }

    @Test
    void testeVizinhoDistancia2Diagonal(){
        Campo vizinho = new Campo(2, 2);
        boolean resultado = campo.adicionarVizinho(vizinho);
        assertTrue(resultado);
    }

    @Test
    void testeNaoVizinho(){
        Campo vizinho = new Campo(1, 1);
        boolean resultado = campo.adicionarVizinho(vizinho);
        assertFalse(resultado);
    }

    @Test
    void testeValorPadraoAtributoMarcado(){
        boolean resultado = campo.isMarcado();
        assertFalse(resultado);
    }

    @Test
    void testeAlternarMarcacao(){
        campo.alternarMarcacao();
        assertTrue(campo.isMarcado());
    }

    @Test
    void testeAlternarMarcacaoDuasChamadas(){
        campo.alternarMarcacao();
        campo.alternarMarcacao();
        assertFalse(campo.isMarcado());
    }

    @Test
    void testeAlternarMarcacaoCampoAberto(){
        campo.abrir();
        campo.alternarMarcacao();
        assertFalse(campo.isMarcado());
    }

    @Test
    void TesteAbrirNaoMinadoNaoMarcado(){
        assertTrue(campo.abrir());
    }

    @Test
    void TesteAbrirNaoMinadoMarcado(){
        campo.alternarMarcacao();  
        assertFalse(campo.abrir());
    }

    @Test
    void TesteAbrirMinadoMarcado(){
        campo.alternarMarcacao();  
        campo.minar();
        assertFalse(campo.abrir());
    }

    @Test
    void TesteAbrirMinadoNaoMarcado(){
        campo.minar();
        assertThrows(ExplosaoException.class, () -> {
            campo.abrir();
        });//assertThrows serve para testar quando for retornado uma exceção, voce chama o metodo, passa o erro que espera ser gerado, e cria uma funcao lambda para botar o teste dentro dela, no erro tem o .class por que essa Exceção e personalizada, eu criei ela no package excecao
    }

    @Test
    void TesteAbrirComVizinhos1(){
        
        Campo campo11 = new Campo(1, 1);    //Cria dois vizinhos
        Campo campo22 = new Campo(2, 2);

        campo22.adicionarVizinho(campo11); //O campo22 adiciona na array dos vizinhos o campo11

        campo.adicionarVizinho(campo22); //O campo22 é adicionado na array dos vizinhos
        campo.abrir(); //Abri o campo

        assertTrue(campo22.isAberto() && campo11.isAberto());   //Os dois campos tem que ser aberto, por que eles nao tem uma minha e nao foram marcado, como o abrir assim que e executado ve se os vizinhos estao "limpos" ele viu que esses dois estao, entao ele abriu os dois
    }

    @Test
    void TesteAbrirComVizinhos2(){
        
        Campo campo11 = new Campo(1, 1);    
        Campo campo12 = new Campo(1, 2);
        Campo campo22 = new Campo(2, 2);

        campo22.adicionarVizinho(campo12);
        campo22.adicionarVizinho(campo11); 
        campo.adicionarVizinho(campo22); 

        campo12.minar();    //Nesse caso o campo 12 esta minado, ou seja nao vai mais abrir casas, sendo assim a casa nao consegue abrir no campo 1, 1 por que ele e vizinho do campo 1, 2
        campo.abrir(); 

        assertTrue(campo22.isAberto() && !campo11.isAberto());  
    }

    @Test
    void testeObjetivoAlcançadoAbertoNaoMinado(){
        campo.abrir();
        //campo.minar();
        assertTrue(campo.objetivoAlcançado());
    }

    @Test
    void testeObjetivoAlcançadoMinadoMarcado(){
        campo.minar();
        campo.alternarMarcacao();
        assertTrue(campo.objetivoAlcançado());
    }

}
