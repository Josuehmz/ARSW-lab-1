/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import java.util.List;

/**
 *
 * @author hcadavid
 */
public class Main {
    
    public static void main(String a[]){
        HostBlackListsValidator hblv = new HostBlackListsValidator();
        
        System.out.println("=== PRUEBA SECUENCIAL (método original) ===");
        long startTime = System.currentTimeMillis();
        List<Integer> blackListOccurrences = hblv.checkHost("200.24.34.55");
        long endTime = System.currentTimeMillis();
        System.out.println("The host was found in the following blacklists: " + blackListOccurrences);
        System.out.println("Tiempo secuencial: " + (endTime - startTime) + " ms");
        System.out.println();
        
        System.out.println("=== PRUEBA PARALELA con 4 hilos ===");
        startTime = System.currentTimeMillis();
        blackListOccurrences = hblv.checkHost("200.24.34.55", 4);
        endTime = System.currentTimeMillis();
        System.out.println("The host was found in the following blacklists: " + blackListOccurrences);
        System.out.println("Tiempo paralelo (4 hilos): " + (endTime - startTime) + " ms");
        System.out.println();
        
        System.out.println("=== PRUEBA con IP dispersa (202.24.34.55) - 8 hilos ===");
        startTime = System.currentTimeMillis();
        blackListOccurrences = hblv.checkHost("202.24.34.55", 8);
        endTime = System.currentTimeMillis();
        System.out.println("The host was found in the following blacklists: " + blackListOccurrences);
        System.out.println("Tiempo paralelo (8 hilos): " + (endTime - startTime) + " ms");
        System.out.println();
        
        System.out.println("=== PRUEBA con IP no maliciosa (212.24.24.55) - 6 hilos ===");
        startTime = System.currentTimeMillis();
        blackListOccurrences = hblv.checkHost("212.24.24.55", 6);
        endTime = System.currentTimeMillis();
        System.out.println("The host was found in the following blacklists: " + blackListOccurrences);
        System.out.println("Tiempo paralelo (6 hilos): " + (endTime - startTime) + " ms");
    }
}
