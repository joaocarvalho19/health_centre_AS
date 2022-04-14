/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pa1c2gy.HC.FIFO;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author joaoc
 */
public class TESTFIFO implements ITESTFIFO{
    // lock para acesso à área partilhada 
    private final ReentrantLock rl = new ReentrantLock( true );

    // array para ids dos customers
    private final String patientId[];
    // array para Condition de bloqueio (uma por customer)
    private final Condition cStay[];
    // array para Condition de entrar no fifo por ordem (uma por customer)
    private final Condition cEnter[];
    // Conditio  de bloqueio de fifo cheio
    private final Condition cFull;
    // Condition de bloqueio de fifo vazio
    private final Condition cEmpty;
    // Condition de bloqueio para aguardar saída do fifo
    private final Condition cLeaving;
    // array para condição de bloqueio de cada customer
    private final boolean leave[];
    // número máximo de customer (dimensão dos arrays)
    private final int size;
    
    // próxima posição de escrita no fifo
    private int idxIn;
    // próxima posição de leitura no fifo
    private int idxOut;
    // número de customers no fifo
    private int count = 0;
    
    private int adultsCount = 0;
    private int childrenCount = 0;

    // último id a entrar no fifo
    private int lastIdIn = -1;
    // flag que indica se simulação está suspensa
    private boolean suspend;
    // flag que indica se está em curso a operação de remover todos elementos do fifo
    private boolean removeAll;
    // flag que indica se é necessário os IDs serem sequenciais na entrada do fifo
    private final boolean order;
    
    public TESTFIFO( int size, boolean order) {
        this.size = size;
        patientId = new String[ size ];
        cStay = new Condition[ size ];
        cEnter = new Condition[ size ];
        leave = new boolean[ size ];
        for ( int i = 0; i < size; i++ ) {
            cStay[ i ] = rl.newCondition();
            cEnter[ i ] = rl.newCondition();
            leave[ i ] = false;
        }
        cFull = rl.newCondition();
        cEmpty = rl.newCondition();
        cLeaving = rl.newCondition();
        idxIn = 0;
        idxOut = 0; 
        suspend = false;
        removeAll = false;
        this.order = order;
        this.lastIdIn = -1;
        
    }
    
    public TESTFIFO( int size ) {
        this(size, false);
    }
    
    // Entrada no fifo. O NOTA: o thead Customer pode ficar bloqueado à espera de 
    // autorização para sair do fifo
    @Override
    public void in( String pId ) {
        if(pId.contains("A")){
            try {
                // garantir acesso em exclusividade
                rl.lock();
                // se fifo cheio, espera na Condition cFull
                while ( adultsCount == size/2)
                    cFull.await();
                if(removeAll)
                    return;

                // se for necessário manter a ordem de entrada, aguarda a sua vez
                /*if(order){
                    int intId = Integer.parseInt(patientId.split("A")[1]);
                    while((lastIdIn + 1) != intId)
                        cEnter[intId].await();
                }*/
                // usar variável local e incrementar apontador de escrita
                int idx = idxIn;
                idxIn = (++idxIn) % size;
                // inserir customer no fifo
                this.patientId[ idx ] = pId;
                //this.lastIdIn = patientId;

                // o fifo poderá estar vazio, pelo q neste caso a Customer poderá
                // estar à espera q um Customer chegue. Necessério avisar Manager
                // q se enconra em espera na Condition cEmpty
                if ( adultsCount == 0 )
                    cEmpty.signal();

                // incrementar número customers no fifo
                adultsCount++;
                // se for necessário manter a ordem, acorda Customer com o próximo ID (se estiver à espera)
               /*if(order)
                    cEnter[lastIdIn + 1].signal();*/
                // ciclo à espera de autorização para sair do fifo
                while ( !leave[ idx ] || suspend)
                    // qd se faz await, permite-se q outros thread tenham acesso
                    // à zona protegida pelo lock
                    cStay[ idx ].await();

                // Customer selecionado está a sair do fifo

                // atualizar variável de bloqueio
                leave[ idx ] = false;
                // avisar Manager que Customer vai sair. Manager espera na
                // Condition cLeaving
                cLeaving.signal();

                // se fifo estava cheio, acordar Customer q esteja à espera de entrar
                if ( adultsCount == size/2 )
                    cFull.signal();
                // decrementar número de customers no fifo
                adultsCount--;

            } catch ( InterruptedException ex ) {
                System.err.println(ex.toString());
            } finally {
                rl.unlock();
            }
            // Customer a sair não só do fifo como tb a permitir q outros
            // threads possam entrar na zona crítica
        }
        if(pId.contains("C")){
            try {
                // garantir acesso em exclusividade
                rl.lock();
                // se fifo cheio, espera na Condition cFull
                while ( childrenCount == size/2)
                    cFull.await();
                if(removeAll)
                    return;

                // se for necessário manter a ordem de entrada, aguarda a sua vez
                /*if(order){
                    int intId = Integer.parseInt(patientId.split("A")[1]);
                    while((lastIdIn + 1) != intId)
                        cEnter[intId].await();
                }*/
                // usar variável local e incrementar apontador de escrita
                int idx = idxIn;
                idxIn = (++idxIn) % size;
                // inserir customer no fifo
                this.patientId[ idx ] = pId;
                //this.lastIdIn = patientId;

                // o fifo poderá estar vazio, pelo q neste caso a Customer poderá
                // estar à espera q um Customer chegue. Necessério avisar Manager
                // q se enconra em espera na Condition cEmpty
                if ( childrenCount == 0 )
                    cEmpty.signal();

                // incrementar número customers no fifo
                childrenCount++;
                // se for necessário manter a ordem, acorda Customer com o próximo ID (se estiver à espera)
                /*if(order)
                    cEnter[lastIdIn + 1].signal();*/
                // ciclo à espera de autorização para sair do fifo
                while ( !leave[ idx ] || suspend)
                    // qd se faz await, permite-se q outros thread tenham acesso
                    // à zona protegida pelo lock
                    cStay[ idx ].await();

                // Customer selecionado está a sair do fifo

                // atualizar variável de bloqueio
                leave[ idx ] = false;
                // avisar Manager que Customer vai sair. Manager espera na
                // Condition cLeaving
                cLeaving.signal();

                // se fifo estava cheio, acordar Customer q esteja à espera de entrar
                if ( childrenCount == size/2 )
                    cFull.signal();
                // decrementar número de customers no fifo
                childrenCount--;

            } catch ( InterruptedException ex ) {
                System.err.println(ex.toString());
            } finally {
                rl.unlock();
            }
            // Customer a sair não só do fifo como tb a permitir q outros
            // threads possam entrar na zona crítica
        
        }
   
    }
    // acordar o customer q há mais tempo está no fifo (sem ter sido desbloqueado!)
    @Override
    public String out() {
        String idOut= null;
        try {
            rl.lock();
            if(removeAll)
                return "";
            // se fifo vazio, espera
            while ( childrenCount+adultsCount == 0  || suspend)
                cEmpty.await();
            int idx = idxOut;
            idOut = patientId[idxOut];
            // atualizar idxOut
            idxOut = (++idxOut) % size; 
            // autorizar a saída do customer q há mais tempo está no fifo
            leave[ idx ] = true;
            // acordar o customer
            cStay[ idx ].signal();
            // aguardar até q Customer saia do fifo
            while ( leave[ idx ] == true || suspend)
                // qd se faz await, permite-se q outros thread tenham acesso
                // à zona protegida pelo lock
                cLeaving.await();  
            /*if(idOut.contains("C")){
                childrenCount--;
            }
            else{adultsCount--;}*/
        } catch ( InterruptedException ex ) {
            System.err.println(ex.toString());
        } finally {
            rl.unlock();
        }
        
        return idOut;
    }

    @Override
    public void suspend() {
        try {
            rl.lock();
            suspend = true;
        } finally {
            rl.unlock();
        }
    }

    @Override
    public void resume() {
        try {
            rl.lock();
            suspend = false;
            for (int i = 0; i < size; i++)
                cStay[i].signal();
            cFull.signal();
            cEmpty.signal();
            cLeaving.signal();
        } finally {
            rl.unlock();
        }
    }

    @Override
    public void removeAll() {
        try {
            rl.lock();
            suspend = false;
            removeAll = true;
            for (int i = 0; i < size; i++) {
                leave[i] = true;
                cStay[i].signal();
            }
        } finally {
            rl.unlock();
        }
    }

    @Override
    public void resetFIFO() {
        try {
            rl.lock();
            for ( int i = 0; i < size; i++ )
                leave[ i ] = false;
            idxIn = 0;
            idxOut = 0; 
            lastIdIn = -1;
            suspend = false;
            removeAll = false;
        } finally {
            rl.unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        try {
            rl.lock();
            if(childrenCount+adultsCount  > 0)
                return false;
        } finally {
            rl.unlock();
        }
        return true;
    }
    public void printFIFIO(){
        for (int i = 0; i < patientId.length; i++) {
  
            // accessing each element of array
            String x = patientId[i];
            System.out.print(x + " ");
        }
    }
}
