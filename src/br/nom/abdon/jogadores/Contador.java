package br.nom.abdon.jogadores;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.StringJoiner;

import br.nom.abdon.domino.Numero;
import br.nom.abdon.domino.Pedra;
import java.util.function.Consumer;

/**
 *
 * @author bruno
 */
public class Contador implements Comparable<Contador> {
    
    private final EnumMap<Numero,Integer> reg;
    private Pedra pedra;
    
    public Contador(final EnumSet<Pedra> mao){
        this(new EnumMap<>(Numero.class));
        mao.stream().forEach((p) -> {contabiliza(p);});
    }

    private Contador(EnumMap<Numero, Integer> mapa) {
        this.reg = mapa;
    }
    
    private void contabiliza(final Pedra pedra) {
        prosNumero(this::contabiliza, pedra);
    }

    private void contabiliza(final Numero numero){
        this.reg.merge(numero, 1 , Integer::sum);
    }

    private void contabilizaJogada(final Pedra pedra) {
        this.pedra = pedra;
        prosNumero(this::contabilizaJogada, pedra);
    }
    
    private void contabilizaJogada(final Numero numero) {
        this.reg.compute(numero, (n,v) -> {return v-1;});
    }
    
    private void prosNumero(Consumer<Numero> consumer, Pedra pedra){
        consumer.accept(pedra.getPrimeiroNumero());
        if(!pedra.isCarroca()){
            consumer.accept(pedra.getSegundoNumero());
        }
    }

    public Contador projete(Pedra pedra) {
        System.out.println("projetando jogavel " + pedra);
        Contador contador = new Contador(this.reg.clone());
        contador.contabilizaJogada(pedra);
        return contador;
    }

    @Override
    public int compareTo(Contador that) {
        
        final Comparator<Integer> naturalOrder = Comparator.naturalOrder();
        int result = 0;
        final Integer maior = reg.values().stream().max(naturalOrder).get();
        
        for (int i = 0; i < maior && result == 0; i++) {
            result = (int) (this.conta(i) - that.conta(i));
        }
        
        System.out.println(this);    
        System.out.println(that);    
        System.out.println(result);    

        return result;
    }

    private long conta(int i) {
        return this.reg.values().stream().filter(x -> {return x == i;}).count();
    }
    
    public Pedra getPedra(){
        return pedra;
    }

    @Override
    public String toString() {
        
        StringJoiner joiner = new StringJoiner(",");
        
        reg.entrySet().stream().forEach(e -> { 
            String entstr = "<" + e.getKey() + ","  + e.getValue() + ">";
            joiner.add(entstr);
        });
        
        return "[" + this.pedra + "]" + joiner.toString();
    }
}
