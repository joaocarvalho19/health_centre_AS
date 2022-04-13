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
public class EvFIFO implements IFIFO{
    private int idxPut = 0;
    private int idxGet = 0;
    private int count = 0;
    
    private final String fifo[];
    private final int size;
    private final ReentrantLock rl;
    private final Condition cNotFull;
    private final Condition cNotEmpty;

    
    public EvFIFO(int size) {
        this.size = size;
        fifo = new String[ size ];
        rl = new ReentrantLock();
        cNotEmpty = rl.newCondition();
        cNotFull = rl.newCondition();
    }
    @Override
    public void put( String value ) {


            try {
                rl.lock();
                while ( isFull() )
                    cNotFull.await();
                fifo[ idxPut ] = value;
                //idxPut++;
                idxPut = (++idxPut) % size;
                count++;
                cNotEmpty.signal();
            } catch ( InterruptedException ex ) {}
            finally {
                rl.unlock();
            }
        
    }
    @Override
    public String get() {
        try{
            rl.lock();
            try {
                while ( isEmpty() )
                    cNotEmpty.await();
            } catch( InterruptedException ex ) {}
            idxGet = idxGet % size;
            String data = fifo[idxGet++];
            count--;

            if(isFull()){
                cNotFull.signal();
            }
            return data;
        }
        finally {
            rl.unlock();
        }
    }
    
    private boolean isFull() {
        return count == size;
    }

    private boolean isEmpty() {
        return count == 0;
    }
}
