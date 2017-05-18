package com.github.abdonia.jogadores;

import java.util.Collection;
import java.util.function.Predicate;

import com.github.abdonia.domino.Mesa;
import com.github.abdonia.domino.Numero;
import com.github.abdonia.domino.Pedra;

/**
 *
 * @author bruno
 */
public class DominoUtil {

    public static final Predicate<Pedra> jogavel(final Mesa mesa){
        return mesa.taVazia() 
            ? p -> {return true;}
            : p -> jogavel(p,mesa.getNumeroEsquerda(),mesa.getNumeroDireita());
    }
    
    public static final boolean jogavel(final Pedra p, final Mesa mesa){
        return jogavel(mesa).test(p);
    }

    public static final boolean jogavel(
            final Pedra p, final Numero n1, final Numero n2){
        return p.temNumero(n1) || p.temNumero(n2);
    }

    public static Pedra aMaiorCarroca(final Collection<Pedra> pedras) {
        return pedras
                .parallelStream()
                .filter(Pedra::isCarroca)
                .max(Pedra::compareTo)
                .get();
    }
}
