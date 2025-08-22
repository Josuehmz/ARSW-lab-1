package edu.eci.arsw.blacklistvalidator;

import java.util.List;
import java.util.Scanner;

/**
 * Clase para evaluar el desempeno del Black List Validator
 * con diferentes numeros de hilos segun los requerimientos de la Parte III
 * @author Josue Hernandez
 */
public class PerformanceTest {
    
    private static final String DISPERSED_IP = "202.24.34.55";
    
    public static void main(String[] args) {
        HostBlackListsValidator validator = new HostBlackListsValidator();
        int cores = Runtime.getRuntime().availableProcessors();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("===========================================");
        System.out.println("EVALUACION DE DESEMPEÑO - BLACK LIST VALIDATOR");
        System.out.println("===========================================");
        System.out.println("Nucleos detectados: " + cores);
        System.out.println("IP de prueba: " + DISPERSED_IP);
        System.out.println("===========================================");
        
        ExperimentConfig[] experiments = {
            new ExperimentConfig(1, "Un solo hilo"),
            new ExperimentConfig(cores, cores + " hilos (nucleos)"),
            new ExperimentConfig(cores * 2, (cores * 2) + " hilos (2x nucleos)"),
            new ExperimentConfig(50, "50 hilos"),
            new ExperimentConfig(100, "100 hilos")
        };
        
        while (true) {
            System.out.println("\nSeleccione un experimento:");
            System.out.println("1. " + experiments[0].description);
            System.out.println("2. " + experiments[1].description);
            System.out.println("3. " + experiments[2].description);
            System.out.println("4. " + experiments[3].description);
            System.out.println("5. " + experiments[4].description);
            System.out.println("6. Ejecutar todos automaticamente");
            System.out.println("0. Salir");
            System.out.print("Opcion: ");
            
            int option = scanner.nextInt();
            
            if (option == 0) {
                System.out.println("Programa terminado.");
                break;
            } else if (option >= 1 && option <= 5) {
                runExperiment(validator, experiments[option - 1], option);
            } else if (option == 6) {
                System.out.println("\nEjecutando todos los experimentos...\n");
                for (int i = 0; i < experiments.length; i++) {
                    runExperiment(validator, experiments[i], i + 1);
                    if (i < experiments.length - 1) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                System.out.println("Opcion no valida.");
            }
        }
        scanner.close();
    }
    
    private static void runExperiment(HostBlackListsValidator validator, ExperimentConfig config, int experimentNumber) {
        System.out.println("=== EXPERIMENTO " + experimentNumber + ": " + config.description + " ===");
        
        long startTime = System.currentTimeMillis();
        
        List<Integer> results;
        if (config.threadCount == 1) {
            results = validator.checkHost(DISPERSED_IP);
        } else {
            results = validator.checkHost(DISPERSED_IP, config.threadCount);
        }
        
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        
        System.out.println("Tiempo: " + executionTime + " ms");
        System.out.println("Ocurrencias: " + results.size());
        System.out.println();
    }
    
    static class ExperimentConfig {
        int threadCount;
        String description;
        
        ExperimentConfig(int threadCount, String description) {
            this.threadCount = threadCount;
            this.description = description;
        }
    }
}
