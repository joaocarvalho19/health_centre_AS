/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 * Thread representing one Entity
 */
package com.mycompany.pa1c2gy.HC.Entities;

import com.mycompany.pa1c2gy.HC.Monitor.ICashier_Cashier;
import com.mycompany.pa1c2gy.HC.Monitor.IPaymentHall_Cashier;

/**
 *
 * @author user
 */
public class TCashier extends Thread {
    
    private final IPaymentHall_Cashier ipaymentHall;
    private final ICashier_Cashier icashier;

    public TCashier(IPaymentHall_Cashier ipaymentHall, ICashier_Cashier icashier) {
        this.ipaymentHall = ipaymentHall;
        this.icashier = icashier;
    }
    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                System.out.println(ex.toString());
            }
            
            if(icashier.hasPatient()){
                icashier.acceptPayment();
            }
            else{
                if(ipaymentHall.hasPatient()){
                    ipaymentHall.acceptPatient();
                }
            }
            
        }
    }
}
