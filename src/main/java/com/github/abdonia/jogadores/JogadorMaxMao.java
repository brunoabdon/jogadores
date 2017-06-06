package com.github.abdonia.jogadores;

import java.util.EnumSet;
import java.util.function.Function;

import com.github.abdonia.domino.Jogada;
import com.github.abdonia.domino.Jogador;
import com.github.abdonia.domino.Lado;
import com.github.abdonia.domino.Mesa;
import com.github.abdonia.domino.Pedra;
import com.github.abdonia.domino.Vontade;

/**
 * @author bruno
 */
public class JogadorMaxMao implements Jogador{

    private Mesa mesa;
    private EnumSet<Pedra> mao;
    private Contador contador;
        
    private boolean perguntouSeEuQueriaJogar;
    
    private Function<Contador, Jogada> joga;
    
    @Override
    public void recebeMao(
            final Pedra pedra1,
            final Pedra pedra2,
            final Pedra pedra3,
            final Pedra pedra4,
            final Pedra pedra5,
            final Pedra pedra6) {
        
        this.mao = EnumSet.of(pedra1, pedra2, pedra3, pedra4, pedra5, pedra6);

        this.contador = new Contador(this.mao);
        
        this.joga = 
            c-> {
                final Pedra p = c.getPedra();
                final Lado l = p.temNumero(mesa.getNumeroEsquerda()) 
                                ? Lado.ESQUERDO 
                                : Lado.DIREITO;
                this.contador = c;
                return joga(p, l);
            };
        
        this.perguntouSeEuQueriaJogar = false;
    }

    @Override
    public Jogada joga() {
        return mesa.getPedras().isEmpty() && !perguntouSeEuQueriaJogar
            ? joga(DominoUtil.aMaiorCarroca(mao),Lado.ESQUERDO)
            : this.mao.parallelStream()
                .filter(DominoUtil.jogavel(mesa))
                .map(contador::projete)
                .min(Contador::compareTo)
                .map(joga)
                .orElse(Jogada.TOQUE);
    }

    @Override
    public Vontade getVontadeDeComecar() {
        perguntouSeEuQueriaJogar = true;
        return Vontade.TANTO_FAZ;
    }

    private Jogada joga(final Pedra pedra, final Lado lado){
        this.mao.remove(pedra);
        return Jogada.de(pedra,lado);
    }

    @Override
    public void sentaNaMesa(final Mesa mesa, final int cadeira) {
        this.mesa = mesa;
    }
}