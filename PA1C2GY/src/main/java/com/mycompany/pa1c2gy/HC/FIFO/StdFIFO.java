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
public class StdFIFO  implements IFIFO{
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
    private int count = 0;
    private boolean suspend;
    private boolean removeAll;

    
    public StdFIFO( int size){
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
    }
    
    // Entrada no fifo. O NOTA: o thead Thread pode ficar bloqueado à espera de 
    // autorização para sair do fifo
    @Override
    public void put( String pId ) {
        try {
                rl.lock();
                while ( count == size)
                    cFull.await();
                if(removeAll)
                    return;
                int idx = idxIn;
                idxIn = (++idxIn) % size;
                this.patientId[ idx ] = pId;
                if ( count == 0 )
                    cEmpty.signal();

                count++;
                while ( !leave[ idx ] || suspend)
                    cStay[ idx ].await();

                leave[ idx ] = false;
                cLeaving.signal();

                if ( count == size)
                    cFull.signal();
                count--;

        } catch ( InterruptedException ex ) {
            System.err.println(ex.toString());
        } finally {
            rl.unlock();
        }
    }
    @Override
    public String get() {
        String idOut= null;
        try {
            rl.lock();
            if(removeAll)
                return "";
            while ( count == 0  || suspend)
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
            if(count > 0)
                return false;
        } finally {
            rl.unlock();
        }
        return true;
    }

}
