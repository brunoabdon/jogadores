package br.nom.abdon.jogadores;

import java.util.function.Predicate;
import br.nom.abdon.domino.Mesa;
import br.nom.abdon.domino.Numero;
import br.nom.abdon.domino.Pedra;

/**
 *
 * @author bruno
 */
public class DominoUtil {

    public static final Predicate<Pedra> jogavel(Mesa mesa){
        return mesa.taVazia() 
                ? p -> {return true;}
                : p -> jogavel(p, mesa);
    }
    
    public static final boolean jogavel(Pedra p, Mesa mesa){
        return mesa.taVazia()
                || jogavel(p, p.getPrimeiroNumero(), p.getSegundoNumero());
    }

    public static final boolean jogavel(Pedra p, Numero n1, Numero n2){
        return p.temNumero(n1) || p.temNumero(n2);
    }

    public static final Pedra aMaiorCarroca(Iterable<Pedra> mao) {
        Pedra maiorCarroca = null;
        for(Pedra pedra : mao) {
            if(pedra.isCarroca()) {
                if(maiorCarroca == null 
                    || (pedra.compareTo(maiorCarroca) >= 1)){
                    maiorCarroca = pedra;
                }
            }
        }
        
        return maiorCarroca;
    }
}
