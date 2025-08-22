package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;
import java.util.List;

/**
 * Thread para buscar un host en un segmento específico de listas negras
 * @author Josué Hernandez
 */
public class BlackListThread extends Thread {
    
    private static final int BLACK_LIST_ALARM_COUNT = 5;
    
    private String ipaddress;
    private int startIndex;
    private int endIndex;
    private HostBlacklistsDataSourceFacade skds;
    
    private LinkedList<Integer> blackListOccurrences;
    private int occurrencesCount;
    private int checkedListsCount;
    
    public BlackListThread(String ipaddress, int startIndex, int endIndex, HostBlacklistsDataSourceFacade skds) {
        this.ipaddress = ipaddress;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.skds = skds;
        this.blackListOccurrences = new LinkedList<>();
        this.occurrencesCount = 0;
        this.checkedListsCount = 0;
    }
    
    @Override
    public void run() {
        for (int i = startIndex; i <= endIndex; i++) {
            checkedListsCount++;
            
            if (skds.isInBlackListServer(i, ipaddress)) {
                blackListOccurrences.add(i);
                occurrencesCount++;
                
                
                if (occurrencesCount >= BLACK_LIST_ALARM_COUNT) {
                    break;
                }
            }
        }
    }
    
    /**
     * Método para consultar cuántas ocurrencias encontró este hilo
     * @return número de ocurrencias encontradas
     */
    public int getOccurrencesCount() {
        return occurrencesCount;
    }
    
    /**
     * Método para obtener la lista de números de listas negras donde se encontró el host
     * @return lista de números de listas negras
     */
    public List<Integer> getBlackListOccurrences() {
        return blackListOccurrences;
    }
    
    /**
     * Método para obtener cuántas listas fueron revisadas por este hilo
     * @return número de listas revisadas
     */
    public int getCheckedListsCount() {
        return checkedListsCount;
    }
} 