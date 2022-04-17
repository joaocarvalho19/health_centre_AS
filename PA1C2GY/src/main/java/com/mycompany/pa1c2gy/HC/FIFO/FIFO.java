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
public class FIFO implements IFIFO{
    // lock para acesso à área partilhada 
    private final ReentrantLock rl = new ReentrantLock( true );

    private final String patientId[];
    private final Condition cStay[];
    private final Condition cEnter[];
    private final Condition cFull;
    private final Condition cEmpty;
    private final Condition cLeaving;
    private final boolean leave[];
    private final int size;
    
    private int idxIn;
    private int idxOut;
    
    private int adultsCount = 0;
    private int childrenCount = 0;

    private boolean suspend;
    private boolean removeAll;
    private final boolean order;
    
    public FIFO( int size, boolean order) {
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
        
    }
    
    public FIFO( int size ) {
        this(size, false);
    }
    
    // Entrada no fifo. O NOTA: o thead pode ficar bloqueado à espera de 
    // autorização para sair do fifo
    @Override
    public void put( String pId ) {
        if(pId.contains("A")){
            try {
                rl.lock();
                while ( adultsCount == size/2)
                    cFull.await();
                if(removeAll)
                    return;

                
                int idx = idxIn;
                idxIn = (++idxIn) % size;
                this.patientId[ idx ] = pId;

                if ( adultsCount == 0 )
                    cEmpty.signal();

                adultsCount++;

                while ( !leave[ idx ] || suspend)
                    cStay[ idx ].await();

                leave[ idx ] = false;
                cLeaving.signal();

                if ( adultsCount == size/2 )
                    cFull.signal();
                adultsCount--;

            } catch ( InterruptedException ex ) {
                System.err.println(ex.toString());
            } finally {
                rl.unlock();
            }
            // Thread a sair não só do fifo como tb a permitir q outros
            // threads possam entrar na zona crítica
        }
        if(pId.contains("C")){
            try {
                rl.lock();
                while ( childrenCount == size/2)
                    cFull.await();
                if(removeAll)
                    return;


                int idx = idxIn;
                idxIn = (++idxIn) % size;
                this.patientId[ idx ] = pId;


                if ( childrenCount == 0 )
                    cEmpty.signal();

                childrenCount++;
                while ( !leave[ idx ] || suspend)

                    cStay[ idx ].await();

                leave[ idx ] = false;

                cLeaving.signal();

                if ( childrenCount == size/2 )
                    cFull.signal();
                childrenCount--;

            } catch ( InterruptedException ex ) {
                System.err.println(ex.toString());
            } finally {
                rl.unlock();
            }
            // Thread a sair não só do fifo como tb a permitir q outros
            // threads possam entrar na zona crítica
        
        }
   
    }
    // acordar a thread que há mais tempo está no fifo (sem ter sido desbloqueado!)
    @Override
    public String get() {
        String idOut= null;
        try {
            rl.lock();
            if(removeAll)
                return "";
            while ( childrenCount+adultsCount == 0  || suspend)
                cEmpty.await();
            int idx = idxOut;
            idOut = patientId[idxOut];
            idxOut = (++idxOut) % size; 
            leave[ idx ] = true;
            cStay[ idx ].signal();
            while ( leave[ idx ] == true || suspend)
                cLeaving.await();  

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
