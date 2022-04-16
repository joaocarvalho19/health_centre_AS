/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pa1c2gy.HC.Monitor;

/**
 *
 * @author joaoc
 */
public interface ICallCenterHall_Patient {
    /**
     * Indicates that a new slot is available on the entrance hall.
     */
    public void updateEntranceSlots(int n);
    
    public void updateMedicalHallSlots(String type, int n);
    
    public void updateWaitingSlots();
    /**
     * Indicates that a new slot is available on a corridor hall.
     * @param numCorridor corridor hall number
     */
    public void evaluationHallFreeSlot(int numEval);
}
