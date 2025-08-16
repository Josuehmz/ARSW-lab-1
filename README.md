
### Escuela Colombiana de Ingeniería
### Arquitecturas de Software - ARSW
## Laboratorio 1 Josué Hernandez
1. ¿Como cambia la sálida al modificar start() con run()?
   Los resultados pasan de ser impresos al azar a salir en el orden del ciclo
   Esto sucede débido a que start() hace que los hilos se ejecuten concurrentemente, por otro lado run() lo ejecuta como cualquier otro método sin aprovechar la concurrencia
