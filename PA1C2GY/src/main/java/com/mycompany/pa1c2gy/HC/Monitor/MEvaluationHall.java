/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pa1c2gy.HC.Monitor;

import com.mycompany.pa1c2gy.HC.FIFO.EvFIFO;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author joaoc
 */
public class MEvaluationHall implements IEvaluationHall_Patient, IEvaluationHall_CallCenter, IEvaluationHall_Nurse{
    /** Reentrant Lock for synchronization */
    private final ReentrantLock rl;
    /** FIFO */
    private final EvFIFO fifo;
    /** the simulation has stopped */
    private boolean stop;
    /** the simulation has ended */
    private boolean end;
    
    // New state to patient when leaving
    private String new_state;

    
    public MEvaluationHall(int numPatients){
        this.fifo = new EvFIFO(numPatients);
        rl = new ReentrantLock(true);

    }
    public void start() {
        System.out.println("Evaluation start");
        try{
            rl.lock();
            stop = false;
            //fifo.resetFIFO();
        } finally{
            rl.unlock();
        }
    }

    @Override
    public String enter(String patientId) {
        String state = null;
        this.fifo.put(patientId);
        try{
            rl.lock();
            state = new_state;
        } finally{
            rl.unlock();
        }

        return state;
    }

    @Override
    public String evaluate() {
        String state = null;
        try{
            rl.lock();
            state = new_state;
        } finally{
            rl.unlock();
        }

        return state;
    }

}
