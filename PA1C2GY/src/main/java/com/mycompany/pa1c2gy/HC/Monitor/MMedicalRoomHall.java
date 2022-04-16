/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pa1c2gy.HC.Monitor;

import com.mycompany.pa1c2gy.HC.FIFO.EvFIFO;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author joaoc
 */
public class MMedicalRoomHall implements IMedicalRoomHall_Patient{
    /** Reentrant Lock for synchronization */
    private final ReentrantLock rl;
    /** FIFO */
    private final EvFIFO fifo;
    
    private boolean stop;
    private boolean end;
    private boolean arrived;
    private int MDT;
    
    // New state to patient
    private String new_state;

    
    public MMedicalRoomHall(){
        this.fifo = new EvFIFO(1);
        rl = new ReentrantLock(true);
        this.arrived = false;
        MDT = 1000;
    }
    
    public void start() {
        System.out.println("Evaluation start");
        try{
            rl.lock();
            stop = false;
            fifo.resetFIFO();
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
            state = "PaymentHall";
        } finally{
            rl.unlock();
        }

        return state;
    }

    @Override
    public void evaluate() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            System.out.println(ex.toString());
        }
        try{
            rl.lock();
            //new_state = state;
            String Id = this.fifo.get();
            System.out.println("EVAL GET: "+Id);
            
            
        } finally{
            rl.unlock();
        }
   
    }

}
