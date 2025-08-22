/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.threads;

/**
 *
 * @author Josué Hernandez
 */

public class CountThread extends Thread{
    private int A, B;
    public CountThread (int A, int B){
        this.A= A;
        this.B= B; 
    }
/**
 * Método que recorre el ciclo de A a B y lo imprime
 */
    @Override
    public void run(){
        for (int i = A; i <= B; i++){
            System.out.println(i);
            try {
                Thread.sleep(100);
            }catch( InterruptedException e){
                e.printStackTrace();
            }

        }

    }
}
