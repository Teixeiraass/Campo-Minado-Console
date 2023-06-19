package br.com.cod3r.cm.modelo;

import java.util.ArrayList;
import java.util.List;

import br.com.cod3r.cm.excecao.ExplosaoException;

public class Campo {
    
    private boolean minado = false;
    private boolean aberto = false;
    private boolean marcado = false;

    private final int linha;
    private final int coluna;

    private List<Campo> vizinhos = new ArrayList<>();

    Campo(int linha, int coluna){
        this.linha = linha;
        this.coluna = coluna;
    }

    //Esse metodo cria os vizinhos
    boolean adicionarVizinho(Campo vizinho){

        //Esses 3 booleans servem para garantir que nao vai ser adicionado vizinho na msm linha e coluna que foi instanciado o campo
        boolean linhaDiferente = linha != vizinho.linha;    //Se a linha que foi dada no construtor for diferente da passada no parametro entao sao linhas diferentes
        boolean colunaDiferente = coluna != vizinho.coluna; //Se a coluna passada no contrutor for diferente da passada no parametro, as colunas sao diferentes
        boolean diagonal = linhaDiferente && colunaDiferente;   //Caso a coluna e a linha forem diferentes entao diagonal vira true, ou seja tem diagonal

        //Esse 3 int retorna o delta
        int deltaLinha = Math.abs(linha - vizinho.linha);
        int deltaColuna = Math.abs(coluna - vizinho.coluna);
        int deltaGeral = deltaColuna + deltaLinha;

        //Caso o deltaGeral seja 1 e  nao tenha diagonal vai ser adicionado um vizinho
        if(deltaGeral == 1 && !diagonal){
            vizinhos.add(vizinho);
            return true;
        }else if(deltaGeral == 2 && diagonal){ //Caso o delta seja dois e tenha diagonal tambem adiciona um vizinho
            vizinhos.add(vizinho);
            return true;
        }else return false; //Se nao atender os requisitos significa que o valor passado no parametro na oe vizinho, ou seja nao esta perto do valor passado no contrutor
    }

    //So alterna se ta marcado com a bandeirinha ou nao
    void alternarMarcacao(){
        if(!aberto){
            marcado = !marcado;
        }
    }

    boolean abrir(){
        if(!aberto && !marcado){
            aberto = true;
            //se o campo aberto estiver minado lança uma exceção
            if(minado){
                throw new ExplosaoException();
            }
            //se a vizinhaça for segura ele faz um loop, pega o valor desse loop e usa recursividade para ir abrindo esse valor
            if(vizinhancaSegura()){
                vizinhos.forEach(v -> v.abrir());
            }
            return true;
        }
        return false;
    }

    //Checa se nao tem bomba na vizinhanca
    boolean vizinhancaSegura(){
        return vizinhos.stream().noneMatch(v -> v.minado); //ve se na array dos vizinhos nao tem nenhum valor minado nele, se tiver alguma mina nos vizinhos ele nao abre esse vizinho
    }

    void minar(){
        if(!minado){
            minado = true;
        }
    }

    public boolean isMarcado(){
        return marcado;
    }

    public boolean isAberto(){
        return aberto;
    }

    void setAberto(boolean aberto){
        this.aberto = aberto;
    }

    public boolean isMinado(){
        return minado;
    }

    public int getLinha(){
        return linha;
    }

    public int getColuna(){
        return coluna;
    }

    boolean objetivoAlcançado(){
        boolean desvendado = !minado && aberto;
        boolean protegido = minado && marcado;

        return desvendado || protegido;
    }

    long minasNaVizinhanças(){
        return vizinhos.stream().filter(v -> v.minado).count(); //Esse metodo pega a array vizinhos, e usa o filter para filtrar os vizinhos que estao minados, usa o cout para contar quantos vizinhos tem minado na vizinhança
    }

    void reiniciar(){
        aberto = false;
        minado = false;
        marcado = false;
    }

    public String toString(){
        if(marcado){
            return "x";
        }else if(aberto && minado){
            return "*";
        }else if(aberto && minasNaVizinhanças() > 0){
            return Long.toString(minasNaVizinhanças()); //mostra a quantidade de minas na vizinhança, toString para transformar em string um metodo que retorna long
        }else if(aberto){
            return " ";
        }else return "?";
    }

}
