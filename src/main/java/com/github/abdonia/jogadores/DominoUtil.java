package com.github.abdonia.jogadores;

import java.util.Collection;
import java.util.function.Predicate;

import com.github.abdonia.domino.Mesa;
import com.github.abdonia.domino.Pedra;

/**
 * Métodos auxiliares.
 * 
 * @author Bruno Abdon
 */
public class DominoUtil {

    public static final Predicate<Pedra> jogavel(final Mesa mesa){
        return mesa.getPedras().isEmpty()
            ? p -> true
            : p -> p.temNumero(mesa.getNumeroEsquerda()) 
                    || p.temNumero(mesa.getNumeroDireita());
    }

    public static Pedra aMaiorCarroca(final Collection<Pedra> pedras) {
        return pedras
                .parallelStream()
                .filter(Pedra::isCarroca)
                .max(Pedra::compareTo)
                .get();
    }
}