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
    private int idxPut = 0;
    private int idxGet = 0;
    private int countAdults = 0;
    private int countChildren = 0;
    
    private final String fifo[];
    private final int size;
    private final ReentrantLock rl;
    private final Condition cNotFull;
    private final Condition cNotEmpty;
        

    
    public FIFO(int size) {
        this.size = size;
        fifo = new String[ size ];
        rl = new ReentrantLock();
        cNotEmpty = rl.newCondition();
        cNotFull = rl.newCondition();
    }
    @Override
    public void put( String value ) {
        if(value.contains("A")){
            try {
                rl.lock();
                while ( adultsIsFull() )
                    cNotFull.await();
                fifo[ idxPut ] = value;
                //idxPut++;
                idxPut = (++idxPut) % size;
                countAdults++;
                cNotEmpty.signal();
            } catch ( InterruptedException ex ) {}
            finally {
                rl.unlock();
        
            }
        }
        if(value.contains("C")){
            try {
                rl.lock();
                while ( childrenIsFull() )
                    cNotFull.await();
                fifo[ idxPut ] = value;
                //idxPut++;
                idxPut = (++idxPut) % size;
                countChildren++;
                cNotEmpty.signal();
            } catch ( InterruptedException ex ) {}
            finally {
                rl.unlock();
            }
        }
    }
    @Override
    public String get() {
        for (int i = 0; i < fifo.length; i++) {
  
            // accessing each element of array
            String x = fifo[i];
            System.out.print(x + " ");
        }
        try{
            rl.lock();
            try {
                while ( isEmpty() )
                    cNotEmpty.await();
            } catch( InterruptedException ex ) {}
            idxGet = idxGet % size;
            String data = fifo[idxGet++];
            if(data.contains("C")){
                countChildren--;
            }
            else{countAdults--;}
            if(adultsIsFull() || childrenIsFull()){cNotFull.signal();}
            return data;
        }
        finally {
            rl.unlock();
        }
    }
    
    private boolean adultsIsFull() {
        return countAdults == size/2;
    }
    
    private boolean childrenIsFull() {
        return countChildren == size/2;
    }

    private boolean isEmpty() {
        return countAdults+countChildren == 0;
    }
    
}
