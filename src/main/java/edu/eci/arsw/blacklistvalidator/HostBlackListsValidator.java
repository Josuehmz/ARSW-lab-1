/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hcadavid
 */
public class HostBlackListsValidator {

    private static final int BLACK_LIST_ALARM_COUNT=5;
    
    /**
     * Check the given host's IP address in all the available black lists,
     * and report it as NOT Trustworthy when such IP was reported in at least
     * BLACK_LIST_ALARM_COUNT lists, or as Trustworthy in any other case.
     * The search is not exhaustive: When the number of occurrences is equal to
     * BLACK_LIST_ALARM_COUNT, the search is finished, the host reported as
     * NOT Trustworthy, and the list of the five blacklists returned.
     * @param ipaddress suspicious host's IP address.
     * @return  Blacklists numbers where the given host's IP address was found.
     */
    public List<Integer> checkHost(String ipaddress){
        
        LinkedList<Integer> blackListOcurrences=new LinkedList<>();
        
        int ocurrencesCount=0;
        
        HostBlacklistsDataSourceFacade skds=HostBlacklistsDataSourceFacade.getInstance();
        
        int checkedListsCount=0;
        
        for (int i=0;i<skds.getRegisteredServersCount() && ocurrencesCount<BLACK_LIST_ALARM_COUNT;i++){
            checkedListsCount++;
            
            if (skds.isInBlackListServer(i, ipaddress)){
                
                blackListOcurrences.add(i);
                
                ocurrencesCount++;
            }
        }
        
        if (ocurrencesCount>=BLACK_LIST_ALARM_COUNT){
            skds.reportAsNotTrustworthy(ipaddress);
        }
        else{
            skds.reportAsTrustworthy(ipaddress);
        }                
        
        LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}", new Object[]{checkedListsCount, skds.getRegisteredServersCount()});
        
        return blackListOcurrences;
    }
    
    /**
     * Check the given host's IP address in all the available black lists using N threads,
     * and report it as NOT Trustworthy when such IP was reported in at least
     * BLACK_LIST_ALARM_COUNT lists, or as Trustworthy in any other case.
     * @param ipaddress suspicious host's IP address.
     * @param N number of threads to use for parallel search.
     * @return Blacklists numbers where the given host's IP address was found.
     */
    public List<Integer> checkHost(String ipaddress, int N){
        
        LinkedList<Integer> blackListOccurrences = new LinkedList<>();
        
        HostBlacklistsDataSourceFacade skds = HostBlacklistsDataSourceFacade.getInstance();
        
        int totalServers = skds.getRegisteredServersCount();
        int serversPerThread = totalServers / N;
        
        
        BlackListThread[] threads = new BlackListThread[N];
        
        
        for (int i = 0; i < N; i++) {
            int startIndex = i * serversPerThread;
            int endIndex;
            
           
            if (i == N - 1) {
                endIndex = totalServers - 1;
            } else {
                endIndex = (i + 1) * serversPerThread - 1;
            }
            
            threads[i] = new BlackListThread(ipaddress, startIndex, endIndex, skds);
            threads[i].start();
        }
        
       
        try {
            for (int i = 0; i < N; i++) {
                threads[i].join();
            }
        } catch (InterruptedException e) {
            LOG.log(Level.SEVERE, "Thread was interrupted", e);
        }
        
        
        int totalOccurrences = 0;
        int totalCheckedLists = 0;
        
        for (int i = 0; i < N; i++) {
            totalOccurrences += threads[i].getOccurrencesCount();
            totalCheckedLists += threads[i].getCheckedListsCount();
            blackListOccurrences.addAll(threads[i].getBlackListOccurrences());
        }
        
        
        if (totalOccurrences >= BLACK_LIST_ALARM_COUNT) {
            skds.reportAsNotTrustworthy(ipaddress);
        } else {
            skds.reportAsTrustworthy(ipaddress);
        }
        
        
        LOG.log(Level.INFO, "Checked Black Lists:{0} of {1}", new Object[]{totalCheckedLists, totalServers});
        
        return blackListOccurrences;
    }
    
    
    private static final Logger LOG = Logger.getLogger(HostBlackListsValidator.class.getName());
    
    

}
