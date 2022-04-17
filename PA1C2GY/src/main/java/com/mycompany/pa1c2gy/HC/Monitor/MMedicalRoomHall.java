/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pa1c2gy.HC.Monitor;

import com.mycompany.pa1c2gy.HC.FIFO.EvFIFO;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
/**
 *
 * @author joaoc
 */
public class MMedicalRoomHall implements IMedicalRoomHall_Patient{
    /** Reentrant Lock for synchronization */
    private final ReentrantLock rl;
    
    private boolean stop;
    private boolean suspend;
    private boolean arrived;
    private int MDT;
    private Condition leave;
    
    
    public MMedicalRoomHall(int MDT){
        rl = new ReentrantLock(true);
        this.arrived = false;
        this.MDT = MDT;
        leave = rl.newCondition();
    }
    
    public void start() {
        try{
            rl.lock();
            stop = false;
            suspend = false;
        } finally{
            rl.unlock();
        }
    }
    
    public void stop() {
        try{
            rl.lock();
            stop = false;
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
        try {
            Thread.sleep(MDT);
        } catch (InterruptedException ex) {
            System.out.println(ex.toString());
        }
        try{
            rl.lock();
            while(suspend)
                leave.await();
            state = "PaymentHall";
        } catch ( InterruptedException ex ) {
            System.err.println(ex.toString());
        } finally{
            rl.unlock();
        }

        return state;
    }


}
