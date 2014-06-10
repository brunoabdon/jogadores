package br.nom.abdon.jogadores;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.function.Function;
import java.util.function.Predicate;

import br.nom.abdon.domino.Jogada;
import br.nom.abdon.domino.Jogador;
import br.nom.abdon.domino.Lado;
import br.nom.abdon.domino.Mesa;
import br.nom.abdon.domino.Numero;
import br.nom.abdon.domino.Pedra;

/**
 * @author bruno
 */
public class JogadorMaxMao implements Jogador{

    private EnumSet<Pedra> mao;
    private Contador contador;
        
    private boolean perguntouSeEuQueriaJogar;
    
    private final static Comparator<Contador> comp = Comparator.naturalOrder();
    
    @Override
    public void recebeMao(Pedra ... pedras) {
        this.mao = 
                EnumSet.of(
                        pedras[0], 
                        pedras[1], 
                        pedras[2], 
                        pedras[3], 
                        pedras[4], 
                        pedras[5]);
        this.contador = new Contador(mao);
        
        perguntouSeEuQueriaJogar = false;
        
    }

    @Override
    public Jogada joga(Mesa mesa) {
        
        final Jogada jogada;
        
        final boolean primeiraRodada = mesa.taVazia();
        
        if (primeiraRodada && !perguntouSeEuQueriaJogar) {
            jogada = aMaiorCarroca();
        } else {
            final Numero cabecaEsquerda = mesa.getNumeroEsquerda();
            final Numero cabecaDireita = mesa.getNumeroDireita();

            final Predicate<Pedra> jogavel = 
                primeiraRodada 
                    ? p -> {return true;}
                    : p -> {return jogavel(p, cabecaEsquerda, cabecaDireita);
            };
            
            final Function<Contador, Jogada> joga = 
                c-> {
                    Pedra p = c.getPedra();
                    Lado l = p.temNumero(cabecaEsquerda) ? Lado.ESQUERDO : Lado.DIREITO;
                    this.contador = c;
                    return joga(p, l);
                };
            
            jogada = 
                this.mao.stream()
                .filter(jogavel)
                .map(contador::projete)
                .min(comp)
                .map(joga).orElse(Jogada.TOQUE);
            
        }
         return jogada;
    }

    @Override
    public int vontadeDeComecar() {
        perguntouSeEuQueriaJogar  = true;
        return 5;
    }

    private Jogada aMaiorCarroca() {
        Pedra maiorCarroca = null;
        for(Pedra pedra : mao) {
            if(pedra.isCarroca()) {
                if(maiorCarroca == null 
                    || (pedra.compareTo(maiorCarroca) >= 1)){
                    maiorCarroca = pedra;
                }
            }
        }
        
        return joga(maiorCarroca,Lado.ESQUERDO);
    }
    
    private Jogada joga(Pedra pedra,Lado lado){
        mao.remove(pedra);
        return new Jogada(pedra,lado);

    }


    private static final boolean jogavel(Pedra p, Numero n1, Numero n2){
        return p.temNumero(n1) || p.temNumero(n2);
    }
    
}
