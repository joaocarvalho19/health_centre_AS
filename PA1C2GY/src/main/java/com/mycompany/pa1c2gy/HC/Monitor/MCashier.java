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
public class MCashier implements ICashier_Patient, ICashier_Cashier{
    private int PYT;
    private final ReentrantLock rl;
    private StdFIFO fifo;
    private boolean isSuspended;
    private boolean stop;
    private boolean end;
    private int numPatients;
        
    public MCashier(){
        this.fifo = new StdFIFO(1);
        this.rl = new ReentrantLock(true);
        this.end = false;
        this.isSuspended = false;
        this.stop = false;
        PYT = 1000;
        numPatients = 0;
    }
    @Override
    public String pay(String patientId) {
        String state = null;
        numPatients++;
        this.fifo.put(patientId);
        try{
            rl.lock();
            state = "Finish";
        } finally{
            rl.unlock();
        }
        numPatients--;
        return state;    }

    @Override
    public void acceptPayment() {
        try {
            Thread.sleep(PYT);
        } catch (InterruptedException ex) {
            System.out.println(ex.toString());
        }
        try{
                rl.lock();
                
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
