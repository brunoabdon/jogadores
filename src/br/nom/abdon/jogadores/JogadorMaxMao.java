package br.nom.abdon.jogadores;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.function.Function;

import br.nom.abdon.domino.Jogada;
import br.nom.abdon.domino.Jogador;
import br.nom.abdon.domino.Lado;
import br.nom.abdon.domino.Mesa;
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
        
        if (mesa.taVazia() && !perguntouSeEuQueriaJogar) {
            jogada = joga(DominoUtil.aMaiorCarroca(mao),Lado.ESQUERDO);
        } else {

            final Function<Contador, Jogada> joga = 
                c-> {
                    Pedra p = c.getPedra();
                    Lado l = p.temNumero(mesa.getNumeroEsquerda()) 
                            ? Lado.ESQUERDO 
                            : Lado.DIREITO;
                    this.contador = c;
                    return joga(p, l);
                };
            
            jogada = 
                this.mao.stream()
                .filter(DominoUtil.jogavel(mesa))
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

    private Jogada joga(Pedra pedra,Lado lado){
        mao.remove(pedra);
        return new Jogada(pedra,lado);

    }
}