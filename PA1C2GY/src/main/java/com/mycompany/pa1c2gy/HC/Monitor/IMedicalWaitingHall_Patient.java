/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.pa1c2gy.HC.Monitor;

/**
 *
 * @author joaoc
 */
public interface IMedicalWaitingHall_Patient {
    public String enter(String patientId);
    public void patientLeave(String type, int n);
}
