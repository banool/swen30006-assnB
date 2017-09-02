package strategies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import automail.MailItem;
import automail.PriorityMailItem;

public class MailPool implements IMailPool{
	
	public static final String PRIORITY_POOL = "PRIORITY_POOL";
	public static final String NON_PRIORITY_POOL = "NON_PRIORITY_POOL";
	
	private ArrayList<MailItem> nonPriorityPool;
	private ArrayList<MailItem> priorityPool;
	
	public MailPool(){
		nonPriorityPool = new ArrayList<MailItem>();
		priorityPool = new ArrayList<MailItem>();
	}
	
	public int getPriorityPoolSize(){
		return priorityPool.size();
	}

	public int getNonPriorityPoolSize() {
		return nonPriorityPool.size();
	}

	public void addToPool(MailItem mailItem) {
		// Check whether it has a priority or not
		if(mailItem instanceof PriorityMailItem){
			// Add to priority items
			priorityPool.add(mailItem);
			priorityPool.sort(new PriorityComparer());

		}
		else{
			// Add to nonpriority items
			nonPriorityPool.add(mailItem);
			nonPriorityPool.sort(new NonPriorityComparer());
		}
	}
	
	public MailItem getNonPriorityMail(){
		if(getNonPriorityPoolSize() > 0){
			return nonPriorityPool.remove(0);
		}
		else{
			return null;
		}
	}
	
	public MailItem getHighestPriorityMail(){
		if(getPriorityPoolSize() > 0){
			return priorityPool.remove(0);
		}
		else{
			return null;
		}
		
	}
	
	public MailItem getBestMail(int FloorFrom, int FloorTo) {
		
		ArrayList<MailItem> tempPriority = new ArrayList<MailItem>();
		
		// Check if there are any priority mail within the range
		for(MailItem m : priorityPool){
			if(isWithinRange(m,FloorFrom,FloorTo)){
				tempPriority.add(m);
			}
		}
		
		// If there is already something in priority then return it as the best mail
		if(tempPriority.size() > 0){
			// Since priorityPool is already sorted, that means items being added are already sorted with the
			// highest priority being in the front of the arraylist
			
			return tempPriority.get(0);
		}
		else{

			ArrayList<MailItem> tempNonPriority = new ArrayList<MailItem>();
			// Try the same thing with nonPriorityPool
			for(MailItem m : nonPriorityPool){
				if(isWithinRange(m,FloorFrom,FloorTo)){
					tempNonPriority.add(m);
				}
			}
			if(tempNonPriority.size() > 0){
				return tempNonPriority.get(0);
			}
		}
		
		return null;
	}
	
	private boolean isWithinRange(MailItem m, int FloorFrom, int FloorTo){

		if(m.getDestFloor() <= FloorTo && m.getDestFloor() >= FloorFrom){
			return true;
		}
		else{
			return false;
		}
		

	}

	/**
	 * Comparator classes and helper method
	 *
	 */
	private class PriorityComparer implements Comparator<MailItem> {
		// Compare Priority level, if they are the same, try comparing arrival time
		public int compare(MailItem m1, MailItem m2){
			if(((PriorityMailItem)m1).getPriorityLevel() > ((PriorityMailItem)m2).getPriorityLevel()){
				return -1;
			}
			else if(((PriorityMailItem)m1).getPriorityLevel() == ((PriorityMailItem)m2).getPriorityLevel()){
				return compareArrival(m1,m2);
			}
			else{
				return 1;
			}
		}
	}
	
	private class NonPriorityComparer implements Comparator<MailItem>{
		
		// Compare arrival time
		public int compare(MailItem m1, MailItem m2){
			return compareArrival(m1,m2);
		}
	}
	
	public static int compareArrival(MailItem m1, MailItem m2){
		if(m1.getArrivalTime() < m2.getArrivalTime()){
			return -1;
		}
		else if(m1.getArrivalTime() == m2.getArrivalTime()){
			return 0;
		}
		else{
			return 1;
		}
	}
	
	
}
