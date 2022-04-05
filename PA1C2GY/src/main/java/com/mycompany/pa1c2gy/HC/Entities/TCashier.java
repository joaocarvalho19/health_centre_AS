/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 * Thread representing one Entity
 */
package com.mycompany.pa1c2gy.HC.Entities;

import com.mycompany.pa1c2gy.HC.Monitor.ISharedRegion1_Cashier;
import com.mycompany.pa1c2gy.HC.Monitor.ISharedRegion2_Cashier;

/**
 *
 * @author user
 */
public class TCashier extends Thread {
    
    private final int tE1Id;
    private final ISharedRegion1_Cashier sr1;
    private final ISharedRegion2_Cashier sr2;
    
    public TCashier(int tE1Id, ISharedRegion1_Cashier sr1, ISharedRegion2_Cashier sr2 ) {
        this.tE1Id = tE1Id;
        this.sr1 = sr1;
        this.sr2 = sr2;
    }
    @Override
    public void run() {
        // state machine for the TCashier (Thread)
        // for example
        sr2.d1();
        sr2.d2();
        sr1.b1();
        sr1.b2();
    }
}
