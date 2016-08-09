package com.github.abdonia.jogadores;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.function.Function;

import com.github.abdonia.domino.Jogada;
import com.github.abdonia.domino.Jogador;
import com.github.abdonia.domino.Lado;
import com.github.abdonia.domino.Mesa;
import com.github.abdonia.domino.Pedra;

/**
 * @author bruno
 */
public class JogadorMaxMao implements Jogador{

    private Mesa mesa;
    private EnumSet<Pedra> mao;
    private Contador contador;
        
    private boolean perguntouSeEuQueriaJogar;
    
    private Function<Contador, Jogada> joga;
    
    private final static Comparator<Contador> COMP = Comparator.naturalOrder();
    
    @Override
    public void recebeMao(Pedra ... pedras) {
        this.mao = EnumSet.of(
            pedras[0], pedras[1], pedras[2], pedras[3], pedras[4], pedras[5]
        );
        
        this.contador = new Contador(mao);
        
        this.joga = 
            c-> {
                Pedra p = c.getPedra();
                Lado l = p.temNumero(mesa.getNumeroEsquerda()) 
                        ? Lado.ESQUERDO 
                        : Lado.DIREITO;
                this.contador = c;
                return joga(p, l);
            };
        
        this.perguntouSeEuQueriaJogar = false;
    }

    @Override
    public Jogada joga() {
        return mesa.taVazia() && !perguntouSeEuQueriaJogar
            ? joga(DominoUtil.aMaiorCarroca(mao),Lado.ESQUERDO)
            : this.mao.stream()
                .filter(DominoUtil.jogavel(mesa))
                .map(contador::projete)
                .min(COMP)
                .map(joga)
                .orElse(Jogada.TOQUE);
    }

    @Override
    public int vontadeDeComecar() {
        perguntouSeEuQueriaJogar = true;
        return 5;
    }

    private Jogada joga(Pedra pedra, Lado lado){
        this.mao.remove(pedra);
        return Jogada.jogada(pedra,lado);
    }

    @Override
    public void sentaNaMesa(Mesa mesa, int cadeira) {
        this.mesa = mesa;
    }
}