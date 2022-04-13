/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.pa1c2gy.HC.Monitor;

/**
 *
 * @author joaoc
 */
public interface IControlCentre {
    /**
    * Start a simulation
    */
    public void startSimulation( int NoA, int NoC, int NoS);
    /**
    * Suspends the Simulation.
    */
    public void suspendSimulation();
    /**
    * Resumes the Simulation.
    */
    public void resumeSimulation();
     /**
    * Stops the Simulation.
    */
    public void stopSimulation();    
    
    public void endSimulation();
    
    public void managerAllowMove();

}
