package com.github.abdonia.jogadores;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.github.abdonia.domino.Numero;
import com.github.abdonia.domino.Pedra;

/**
 *
 * @author Bruno Abdon
 */
public class Contador implements Comparable<Contador> {

    private static final Function<Entry<Numero,Integer>,String> STRINGFY_ENTRY =
        e -> "<" + e.getKey() + ","  + e.getValue() + ">";

    private final EnumMap<Numero,Integer> reg;
    private Pedra pedra;
    
    public Contador(final EnumSet<Pedra> mao){
        this(new EnumMap<>(Numero.class));
        mao.stream().forEach(this::contabilizaPedra);
    }

    private Contador(final EnumMap<Numero, Integer> mapa) {
        this.reg = mapa;
    }
    
    private void contabilizaPedra(final Pedra pedra) {
        this.prosNumero(this::contabilizaNumero, pedra);
    }

    private void contabilizaNumero(final Numero numero){
        this.reg.merge(numero, 1 , Integer::sum);
    }

    private void contabilizaJogada(final Pedra pedra) {
        this.pedra = pedra;
        this.prosNumero(this::contabilizaJogada, pedra);
    }
    
    private void contabilizaJogada(final Numero numero) {
        this.reg.compute(numero, (n,v) -> {return v-1;});
    }
    
    private void prosNumero(final Consumer<Numero> consumer, final Pedra pedra){
        consumer.accept(pedra.getPrimeiroNumero());
        if(!pedra.isCarroca()){
            consumer.accept(pedra.getSegundoNumero());
        }
    }

    public Contador projete(final Pedra pedra) {
        final Contador contador = new Contador(this.reg.clone());
        contador.contabilizaJogada(pedra);
        return contador;
    }

    @Override
    public int compareTo(final Contador that) {
 
        int result = 0;
        final Integer maior = 
            reg.values().parallelStream().max(Integer::compare).get();
        
        for (int i = 0; i < maior && result == 0; i++) {
            result = (int) (this.conta(i) - that.conta(i));
        }
        
        if(result == 0){
            final Pedra pedraAqui = this.getPedra();
            final Pedra pedraAli = that.getPedra();

            final boolean carrocaAqui = pedraAqui.isCarroca();
            final boolean carrocaAli = pedraAli.isCarroca();

            result = carrocaAqui != carrocaAli 
                ? carrocaAqui ? -1 : 1
                : pedraAli.compareTo(pedraAqui);
        }

        return result;
    }

    private long conta(final int i) {
        return 
            this.reg.values()
                    .parallelStream()
                    .filter(x -> {return x == i;})
                    .count();
    }

    public Pedra getPedra(){
        return pedra;
    }

    @Override
    public String toString() {
        return 
            reg.entrySet()
                .stream()
                .map(STRINGFY_ENTRY)
                .collect(Collectors.joining(",", "[", "]"));
    }
}
