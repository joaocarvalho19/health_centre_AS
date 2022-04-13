/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.pa1c2gy.HC.Monitor;

/**
 *
 * @author joaoc
 */
public interface ICallCenterHall_ControlCentre {
    public void start();

    public void suspend();

    public void resume();

    public void stop();

    public void end();

    public void auto(int timeout);

    public void manual();

    public void allowMove();
}
