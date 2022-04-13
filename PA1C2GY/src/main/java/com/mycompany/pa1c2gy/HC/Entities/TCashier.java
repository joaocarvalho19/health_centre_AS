/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 * Thread representing one Entity
 */
package com.mycompany.pa1c2gy.HC.Entities;

import com.mycompany.pa1c2gy.HC.Monitor.ICallCenterHall_CallCenter;

/**
 *
 * @author user
 */
public class TCashier extends Thread {
    
    private final int tE1Id;
    private final ICallCenterHall_CallCenter sr1;
    
    public TCashier(int tE1Id, ICallCenterHall_CallCenter sr1) {
        this.tE1Id = tE1Id;
        this.sr1 = sr1;
    }
    @Override
    public void run() {
        // state machine for the TCashier (Thread)
        // for example
    }
}
