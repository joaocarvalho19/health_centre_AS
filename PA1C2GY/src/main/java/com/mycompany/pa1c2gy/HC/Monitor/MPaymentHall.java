/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pa1c2gy.HC.Monitor;
import com.mycompany.pa1c2gy.HC.FIFO.StdFIFO;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author joaoc
 */
public class MPaymentHall implements IPaymentHall_Patient, IPaymentHall_Cashier{
    /** Reentrant Lock for synchronization */
    private final ReentrantLock rl;
    /** FIFO for customers */
    private final StdFIFO fifo;
    /** flag indicating that the simulation has stopped */
    private boolean stop;
    /** flag indicating that the simulation has ended */
    private boolean suspend;
    
    private String new_state;
    
    private int numPatients;
    
    private final Condition leave;

    /**
     * Shared area payment hall instantiation.
     * @param maxCustomers size of the payment hall
     */
    public MPaymentHall(int size) {
        this.fifo = new StdFIFO(size);
        rl = new ReentrantLock(true);
        stop = false;
        suspend = false;
        numPatients = 0;
        leave = rl.newCondition();
    }
    
    public void start() {
        try{
            rl.lock();
            stop = false;
            fifo.resetFIFO();
        } finally{
            rl.unlock();
        }
    }
    
    public void stop() {
        try{
            rl.lock();
            stop = true;
            fifo.resetFIFO();
        } finally{
            rl.unlock();
        }
    }
    
    public void suspend() {
        try{
            rl.lock();
            suspend = true;
            leave.signal();
        } finally{
            rl.unlock();
        }
    }
    
    public void resume() {
        try{
            rl.lock();
            suspend = false;
            leave.signal();
        } finally{
            rl.unlock();
        }
    }

    @Override
    public String enter(String patientId) {
        String state = null;
        numPatients++;
        this.fifo.put(patientId);
        System.out.println("Saiu!!");
        try{
            rl.lock();
            if(stop){return "Stop";}
            while(suspend)
                leave.await();
            state = new_state;
            
        } catch ( InterruptedException ex ) {
            System.err.println(ex.toString());
        } finally{
            rl.unlock();
        }
        numPatients--;
        return state;
    }

    
    @Override
    public void acceptPatient() {
        
        try{
            rl.lock();
            new_state = "Cashier";
        } finally{
            rl.unlock();
        }
        this.fifo.get();
    }
    
    @Override
    public boolean hasPatient(){
        return (numPatients > 0);
    }
}
