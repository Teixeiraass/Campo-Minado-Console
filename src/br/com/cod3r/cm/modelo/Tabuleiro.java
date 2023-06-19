package br.com.cod3r.cm.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import br.com.cod3r.cm.excecao.ExplosaoException;

public class Tabuleiro {
    private int linhas;
    private int colunas;   
    private int minas;
    
    private final List<Campo> campos = new ArrayList<>();

    public Tabuleiro(int linhas, int colunas, int minas){
        this.linhas = linhas;
        this.colunas = colunas;
        this.minas = minas;

        gerarCampos();
        associarVizinhos();
        sortearMinas();
    }

    public void abrirCampo(int linha, int coluna){
        try{
            campos.parallelStream()
            .filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
            .findFirst()   //vai retornar o primeiro valor que encontrou
            .ifPresent(c -> c.abrir());
        }catch(ExplosaoException e){
            campos.forEach(c -> c.setAberto(true));
            throw e;
        }
    }

    public void MarcarCampo(int linha, int coluna){
        campos.parallelStream()
        .filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
        .findFirst()   //vai retornar o primeiro valor que encontrou
        .ifPresent(c -> c.alternarMarcacao());
    }

    //Esse metodo cria as linhas e colunas com o for, entao vai ser adicionado na array Campo 
    private void gerarCampos() {
        for(int l = 0; l < linhas; l++){
            for(int c = 0; c < colunas; c++){
                campos.add(new Campo(l, c));
            }
        }
    }

    //esse for vai percorrer a lista duas vezes para associar os vizinhos, o c1 passa pelos indices da array como se fosse o i, entao o j passa a lista toda adicionando vizinhos para o indice que esta em i, e assim por diante
    private void associarVizinhos() {
        for(Campo c1: campos){  
            for(Campo c2: campos){
                c1.adicionarVizinho(c2);
            }
        }
    }

    //exemplo metodo acima 
    /*
     * [i,V,j, , , , , ]
     * [V,V, , , , , , ]
     * [               ]
     * Note que o j vai passar pela lista toda, quando ele chegar em baixo e do lado do i ele vai dizer que e vizinhos, feito todo esse processo o i vai para coluna dois, ai se repete dnv dando vizinho para coluna 2
     */

    private void sortearMinas() {
        long minasArmadas = 0;

        Predicate<Campo> minado = c -> c.isMinado();

        do{
            int aleatorio = (int) (Math.random() * campos.size()); //Cria o numero aleatorio
            campos.get(aleatorio).minar(); //aqui ele usa o get no indice aleatorio, e mina esse indice
            minasArmadas = campos.stream().filter(minado).count(); //Faz a verificação de campos minados, ele usa o filter para filtrar quais parametros estao com minas e usa o count para contar quantos tem, no inicio vai ser sempre 0 pq nao tera mina no campo
        }while(minasArmadas < minas);
    }

    public boolean objetivoAlcançado(){
        return campos.stream().allMatch(c -> c.objetivoAlcançado()); //Aqui ele verifica se todos os campo dao match, no objetivo alcaçado
    }

    public void reiniciar(){
        campos.stream().forEach(c -> c.reiniciar()); //Aqui ele passa por toda a lista e reinicia todos os campos com o metodo reinciar que foi criado na classe campo 
        sortearMinas();
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append(" ");
        for(int c = 0; c < colunas; c++){
            sb.append(" ");
            sb.append(c);
            sb.append(" ");
        }
        sb.append("\n");

        int i = 0;
        for(int l = 0; l < linhas; l++){
            sb.append(l);
            for(int c = 0; c < colunas; c++){
                sb.append(" ");
                sb.append(campos.get(i));
                sb.append(" ");
                i++;
            }
            sb.append("\n");
        }

        return sb.toString();
    }

}