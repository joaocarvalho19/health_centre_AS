/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.pa1c2gy.HC.FIFO;

/**
 *
 * @author joaoc
 */
public interface ITESTFIFO {
    public void put( String patientId );
    public String get();
    public void suspend();
    public void resume();
    public void removeAll();
    public void resetFIFO();
    public boolean isEmpty();
}
