package br.com.cod3r.cm.visao;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

import br.com.cod3r.cm.excecao.ExplosaoException;
import br.com.cod3r.cm.excecao.SairException;
import br.com.cod3r.cm.modelo.Tabuleiro;

public class TabuleiroConsole {
    
    private Tabuleiro tabuleiro;
    private Scanner scan = new Scanner(System.in);

    public TabuleiroConsole(Tabuleiro tabuleiro){
        this.tabuleiro = tabuleiro;

        executarJogo();
    }

    private void executarJogo() {
        try{
            boolean continuar = true;
            
            while(continuar){
                cicloDoJogo();
                
                System.out.println("Outra partida? (S/n)");
                String resposta = scan.nextLine();

                if("n".equalsIgnoreCase(resposta)){
                    continuar = false;
                }else{
                    tabuleiro.reiniciar();
                }
            }
        }catch(SairException e){
            System.out.println("Você saiu do jogo");
        }finally{
            scan.close();
        }
    }

    private void cicloDoJogo() {
        try{

            while(!tabuleiro.objetivoAlcançado()){
                System.out.println(tabuleiro);

                String digitado = capturarValorDigitado("Digite (x, y)");
                Iterator<Integer> xy = Arrays.stream(digitado.split(",")).map(e -> Integer.parseInt(e.trim())).iterator(); //separa os dois valores digitados em uma array, e armazena eles em um Iterator

                digitado = capturarValorDigitado("1 - abrir ou 2 - (Des)Marcar: ");
                if("1".equals(digitado)){
                    tabuleiro.abrirCampo(xy.next(), xy.next());
                }else if("2".equals(digitado)){
                    tabuleiro.MarcarCampo(xy.next(), xy.next());
                }
            }

            System.out.println(tabuleiro);
            System.out.println("Você ganhou!");
        }catch(ExplosaoException e){
            System.out.println(tabuleiro);
            System.out.println("Você perdeu!");
        }
    }

    private String capturarValorDigitado(String texto){
        System.out.print(texto);
        String digitado = scan.nextLine();

        if("sair".equalsIgnoreCase(digitado)){
            throw new SairException();
        }

        return digitado;
    }

}
