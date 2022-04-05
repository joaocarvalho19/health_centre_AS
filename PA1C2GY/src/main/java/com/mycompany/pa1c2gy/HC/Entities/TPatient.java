/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 * Thread representing one Entity
 */
package com.mycompany.pa1c2gy.HC.Entities;

import com.mycompany.pa1c2gy.HC.Monitor.ISharedRegion1_Patient;
import com.mycompany.pa1c2gy.HC.Monitor.ISharedRegion2_Patient;

/**
 *
 * @author user
 */
public class TPatient extends Thread {

    private final int tE1Id;                    // Thread Id
    private final ISharedRegion1_Patient sr1;
    private final ISharedRegion2_Patient sr2;
    
    
    public TPatient(int tE1Id, ISharedRegion1_Patient sr1, ISharedRegion2_Patient sr2 ) {
        this.tE1Id = tE1Id;
        this.sr1 = sr1;
        this.sr2 = sr2;
    }
    @Override
    public void run() {
        // state machine for the Entity1 (Thread)
        // for example
        System.out.println(tE1Id);
        sr1.a1();
        sr2.c2();
        sr2.c1();
        sr1.a2();
    }
}
