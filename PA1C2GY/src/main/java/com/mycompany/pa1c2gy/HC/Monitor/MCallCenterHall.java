/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pa1c2gy.HC.Monitor;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author joaoc
 */
public class MCallCenterHall implements ICallCenterHall_ControlCentre, ICallCenterHall_CallCenter, ICallCenterHall_Patient{
    /** Reentrant Lock for synchronization */
    private final ReentrantLock rl;


    private int numPatientsEntranceHall; 
    
    private int numPatientsWaitingHall; 
    
    private int numAdultsMedicalHall;
    private int numChildrenMedicalHall;

    private boolean evaluationHallsHasEmptySpace;
    
    private boolean entranceHallHasEmptySpace;
    
    private int[] emptySpacesEvaluationHall;
    
    private int emptyAdultSpacesEntranceHall;
    
    private int emptyChildrenSpacesEntranceHall;
    
    private boolean suspend;

    private boolean stop;

    private boolean end;

    private boolean isAuto;

    private boolean allowPatient;


    private final int sizeEntranceHall;
    private Condition move;

    
    public MCallCenterHall(int sizeEntranceHall, boolean isAuto){
        rl = new ReentrantLock(true);
        this.sizeEntranceHall = sizeEntranceHall;
        
        evaluationHallsHasEmptySpace = true;
        entranceHallHasEmptySpace = true;
        emptyAdultSpacesEntranceHall = sizeEntranceHall/2;
        emptyChildrenSpacesEntranceHall = sizeEntranceHall/2;
        numPatientsEntranceHall = 0;
        numPatientsWaitingHall = 0;
        this.numAdultsMedicalHall = 0;
        this.numChildrenMedicalHall = 0;        

        emptySpacesEvaluationHall = new int[4];
        for (int i = 0; i < 4; i++)
            this.emptySpacesEvaluationHall[i] = 1;     
        
        suspend = false;
        stop = false;
        end = false;
        this.isAuto = isAuto;
        this.allowPatient = false;
        this.move = rl.newCondition();
    }
    
    @Override
    public String process() {
        String res = "";
        try{
            
            rl.lock();
            while(((numPatientsEntranceHall == 0 || !evaluationHallsHasEmptySpace) && (numPatientsWaitingHall == 0)) || suspend || stop || (isAuto == false && allowPatient == false) )
                move.await(); 
            
            while(this.numAdultsMedicalHall + this.numChildrenMedicalHall == 2 || suspend || stop || (isAuto == false && allowPatient == false) ){
                move.await();    
            }
            if(!isAuto)
                allowPatient = false;
            else{
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    System.err.println(ex.toString());
                }
            }
            if(numPatientsEntranceHall != 0)
                for (int i = 0; i < emptySpacesEvaluationHall.length; i++) { 
                        if(emptySpacesEvaluationHall[i] > 0){ 
                            emptySpacesEvaluationHall[i] -= 1; 
                            switch(i){
                                case 0: res = "EvHall_1";
                                    break;
                                case 1: res = "EvHall_2";
                                    break;
                                case 2: res = "EvHall_3";
                                    break;
                                case 3: res = "EvHall_4";
                                    break;
                            }
                            return res;
                        } 
                
            }
            // Updates the flag that indicates if there is any free space on the evaluation Hall
            evaluationHallsHasEmptySpace = false;
            for (int i = 0; i < emptySpacesEvaluationHall.length; i++)
                if(emptySpacesEvaluationHall[i] > 0){
                    evaluationHallsHasEmptySpace = true;
                    break;
            }
            
            if(numPatientsWaitingHall > 0 && numPatientsEntranceHall == 0){
                return "MedicalWait";
            }
            
        } catch(InterruptedException ex){
            System.err.println(ex.toString());
        } finally{
            rl.unlock();
        }
        return res;
    }
    @Override
    public void start() {
        try{
            rl.lock();
            suspend = false;
            stop = false;
            move.signal();
        } finally{
            rl.unlock();
        }
    }
    
    @Override
    public void suspend() {
        try{
            rl.lock();
            suspend = true;
            stop = false;
            move.signal();
        } finally{
            rl.unlock();
        }
    }

    @Override
    public void resume() {
        try{
            rl.lock();
            suspend = false;
            stop = false;
            move.signal();
        } finally{
            rl.unlock();
        }
    }

    @Override
    public void stop() {
        try{
            rl.lock();
            stop = true;
            suspend = false;
            entranceHallHasEmptySpace = true;
            numPatientsEntranceHall = 0;
            numPatientsWaitingHall = 0;
            this.numAdultsMedicalHall = 0;
            this.numChildrenMedicalHall = 0;  
        } finally{
            rl.unlock();
        }
    }

    @Override
    public void end() {
    }

    @Override
    public void auto() {
        System.out.print("Auto!!");
        try{
            rl.lock();
            isAuto = true;
            move.signal();
        } finally{
            rl.unlock();
        }
    }

    @Override
    public void manual() {
        System.out.print("Manual!!");
        try{
            rl.lock();
            isAuto = false;
            move.signal();
        } finally{
            rl.unlock();
        }
    }

    @Override
    public void allowMove() {
        try{
            rl.lock();
            allowPatient = true;
            move.signal();
        } finally{
            rl.unlock();
        }
    }
    
    @Override
    public void updateEntranceSlots(int n){
        try{
            rl.lock();
            numPatientsEntranceHall += n; // Update number of available spaces
            move.signal();
        } finally{
            rl.unlock();
        }    
    }
    
    @Override
    public void updateWaitingSlots(){
        try{
            rl.lock();
            numPatientsWaitingHall += 1; // Update number of available spaces
            move.signal();
        } finally{
            rl.unlock();
        }    
    }
    
    @Override
    public void updateMedicalHallSlots(String type, int n){
        try{
            rl.lock();
            if(type.equals("C")){
                numChildrenMedicalHall += n;
                
            }
            else if(type.equals("A")){
                numAdultsMedicalHall += n;
            }
            move.signal();
        } finally{
            rl.unlock();
        }    
    }

    @Override
    public void evaluationHallFreeSlot(int numEval) {
            try{
                rl.lock();
                evaluationHallsHasEmptySpace = true;
                emptySpacesEvaluationHall[numEval] += 1; // Update number of available spaces
                move.signal();
            } finally{
                rl.unlock();
            }    }
}
