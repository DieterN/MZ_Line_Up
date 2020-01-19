package calculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.Statics;
import team.LineUp;
import team.Player;
import team.Position;
import team.Team;

public abstract class LineUpCalculator {
	
	protected Map<List<Player>, Double> maxValues = new HashMap<List<Player>, Double>();
	
	protected Map<Player, Double> gk2dc = new HashMap<Player, Double>();
	protected Map<Player, Double> dc2gk = new HashMap<Player, Double>();
	protected Map<Player, Double> gk2c = new HashMap<Player, Double>();
	protected Map<Player, Double> c2gk = new HashMap<Player, Double>();
	protected Map<Player, Double> gk2w = new HashMap<Player, Double>();
	protected Map<Player, Double> w2gk = new HashMap<Player, Double>();
	protected Map<Player, Double> gk2n = new HashMap<Player, Double>();
	protected Map<Player, Double> n2gk = new HashMap<Player, Double>();
	protected Map<Player, Double> dc2c = new HashMap<Player, Double>();
	protected Map<Player, Double> c2dc = new HashMap<Player, Double>();
	protected Map<Player, Double> dc2w = new HashMap<Player, Double>();
	protected Map<Player, Double> w2dc = new HashMap<Player, Double>();
	protected Map<Player, Double> dc2n = new HashMap<Player, Double>();
	protected Map<Player, Double> n2dc = new HashMap<Player, Double>();
	protected Map<Player, Double> c2w = new HashMap<Player, Double>();
	protected Map<Player, Double> w2c = new HashMap<Player, Double>();
	protected Map<Player, Double> c2n = new HashMap<Player, Double>();
	protected Map<Player, Double> n2c = new HashMap<Player, Double>();
	protected Map<Player, Double> w2n = new HashMap<Player, Double>();
	protected Map<Player, Double> n2w = new HashMap<Player, Double>();
		
	protected int maxAge;
	protected Team team;
	protected LineUp lineUp;
	
	public LineUpCalculator(Team team, int maxAge){
		this.maxAge = maxAge;
		this.team = team;
		System.out.println("Players size: " + team.getPlayers(maxAge).size());
		if(team.getPlayers(maxAge).size() < 21){
			Statics.threeLines = true;
		}
		this.lineUp = new LineUp();
	}
	
	public LineUp calculate() {
		createRandomLineUp();
//		lineUp.printLineUpPerPosition(false);
		System.out.println("");
		int i = 0;
		while(i < Statics.nbOfIterations){
			List<Player> switchPlayers = determineBestMove();
			if (switchPlayers.isEmpty()){
				break;
			} else {
				System.out.println("1:" + lineUp.getPlayerPosition(switchPlayers.get(0)) + " " + switchPlayers.get(0).getName());
				System.out.println("2:" + lineUp.getPlayerPosition(switchPlayers.get(1)) + " " + switchPlayers.get(1).getName());
				if(switchPlayers.get(2) != null)
				   System.out.println("3:" + lineUp.getPlayerPosition(switchPlayers.get(2)) + " " + switchPlayers.get(2).getName());
				if(switchPlayers.get(3) != null)
					System.out.println("4:" + lineUp.getPlayerPosition(switchPlayers.get(3)) + " " + switchPlayers.get(3).getName());
			}
			changePositions(switchPlayers);
			System.out.println("New score: " + lineUp.getScore());
			System.out.println("");
			i++;
		}
		System.out.println("Nb of changes: " + i);
		determineLines();
		return lineUp;
	}

	protected abstract void createRandomLineUp();

	protected abstract void determineLines();

	protected double[] getAverages(List<Player> playerList) {
		double[] averages = new double[15];
		int size = playerList.size();
		for(Player p : playerList){
			averages[0] += p.getPI()/size;
			averages[1] += p.getPo()/size;
			averages[2] += p.getSk()/size;
			averages[3] += p.getPa()/size;
			averages[4] += p.getQu()/size;
			averages[5] += p.getSh()/size;
			averages[6] += p.getKe()/size;
			averages[7] += p.getPC()/size;
			averages[8] += p.getCh()/size;
			averages[9] += p.getSt()/size;
			averages[10] += p.getGK()/size;
			averages[11] += p.getDC()/size;
			averages[12] += p.getC()/size;
			averages[13] += p.getW()/size;
			averages[14] += p.getTotalPucks()/size;
		}
		return averages;
	}
	
	protected double getAdjustedAvgStamina(List<Player> playerList, double avgSt, int min) {
		int counter = 0;
		for (Player p : playerList){
			if (p.getSt() > avgSt){
				counter ++;
			}
		}
		if (counter >= min){
			return avgSt;
		}
		else{
			return getAdjustedAvgStamina(playerList, avgSt - 1, min);
		}			
	}

	protected List<Player> determineBestMove(){
		this.maxValues.clear();
		updateMaxValues(gk2dc, dc2gk, n2dc, n2gk);
		updateMaxValues(gk2c, c2gk, n2c, n2gk);
		updateMaxValues(gk2w, w2gk, n2w, n2gk);
		updateMaxValues(gk2n, n2gk);
		updateMaxValues(dc2c, c2dc, n2c, n2dc);
		updateMaxValues(dc2w, w2dc, n2w, n2dc);
		updateMaxValues(dc2n, n2dc);
		updateMaxValues(c2w, w2c, n2w, n2c);
		updateMaxValues(c2n, n2c);
		updateMaxValues(w2n, n2w);
		return analyseMaxValues();
	}
		
	private List<Player> analyseMaxValues(){
    	double max = -100.0;
		List<Player> maxPlayerList = new ArrayList<Player>();
		for(List<Player> l : maxValues.keySet()){
			double lValue = maxValues.get(l);
			if(lValue > max){
				max = lValue;
				maxPlayerList = l;
			}
		}
		if(max <= 0){
			maxPlayerList.clear();
		} else {
			System.out.println("Wissel (" + max + ") :");
		}
		return maxPlayerList;
	}
	
    private void updateMaxValues(Map<Player, Double> map1, Map<Player, Double> map2){
    	updateMaxValues(map1, map2, null, null);
    }
    
	protected List<Player> updateMaxValues(Map<Player, Double> map1, Map<Player, Double> map2
			                   , Map<Player, Double> nMap1, Map<Player, Double> nMap2){
		List<Player> playerList = new ArrayList<Player>();
		if(!map1.isEmpty() && !map2.isEmpty()) {
			double value = 0.0;
			Tuple<Player,Double> maxTuple = getMaxTuple(map1);
			playerList.add(maxTuple.getX());
			value += maxTuple.getY();
			maxTuple = getMaxTuple(map2);
			playerList.add(maxTuple.getX());
			value += maxTuple.getY();
			if(nMap1 != null){
				value += getBestNotSelectedSwap(playerList, nMap1, nMap2);
			} else{
				playerList.add(null);
				playerList.add(null);
			}
			this.maxValues.put(playerList, value);
		}
		return playerList;
	}
	
	private Tuple<Player,Double> getMaxTuple(Map<Player, Double> map1){
		double tempMax = Double.NEGATIVE_INFINITY;
		Player tempMaxPlayer = null;
		for(Player p : map1.keySet()){
			double pValue = map1.get(p);
			if(pValue > tempMax){
				tempMax = pValue;
				tempMaxPlayer = p;
			}
		}
		return new Tuple<Player,Double>(tempMaxPlayer,tempMax); 
	}
	
	private double getBestNotSelectedSwap(List<Player> players, Map<Player, Double> nMap1, Map<Player, Double> nMap2) {
		int returnValue = 0;
		Player player1 = players.get(0);
		Player player2 = players.get(1);
		Position pos1 = lineUp.getPlayerPosition(player1);
		Position pos2 = lineUp.getPlayerPosition(player2);
		returnValue += getSwapValue(players, player1, pos2, nMap1);
		returnValue += getSwapValue(players, player2, pos1, nMap2);
		return returnValue;
	}
	
	private double getSwapValue(List<Player> players, Player p, Position pos, Map<Player, Double> nMap1){
		int returnValue = 0;
		Tuple<Player, Double> maxTuple1 = getMaxTuple(nMap1);
		if(p.getScoreForPosition(pos) < maxTuple1.getY()){
			returnValue += (maxTuple1.getY() - p.getScoreForPosition(pos));
			players.add(maxTuple1.getX());
		}
		else{
			players.add(null);
		}
		return returnValue;
	}
	
	protected void changePositions(List<Player> players){
		changePosition(players.get(0),players.get(1));
		if(players.get(2) != null)
			changePosition(players.get(0),players.get(2));
		if(players.get(3) != null)
			changePosition(players.get(1),players.get(3));
	}
	
	private void changePosition(Player player1, Player player2){
		Position pos1 = lineUp.getPlayerPosition(player1);
		Position pos2 = lineUp.getPlayerPosition(player2);
		switch(pos1){
		   case GOALKEEPER : 
			   	removeGK(player1);
			   	addGK(player2);
		   	 	break;
		   case DEFENDER : 
			   	removeDC(player1);
			   	addDC(player2);
		   	 	break;
		   case CENTER : 
			   	removeC(player1);
			   	addC(player2);
			   	break;
		   case WINGER : 
			   	removeW(player1);
			   	addW(player2);
		   	 	break;
		   case NOT_SELECTED : 
			   	removeN(player1);
			   	addN(player2);
		   	 	break;
		}
		switch(pos2){
		   case GOALKEEPER : 
			   	removeGK(player2);
			   	addGK(player1);
		   	 	break;
		   case DEFENDER : 
			   	removeDC(player2);
			   	addDC(player1);
		   	 	break;
		   case CENTER : 
			   	removeC(player2);
			   	addC(player1);
			   	break;
		   case WINGER : 
			   	removeW(player2);
			   	addW(player1);
		   	 	break;
		   case NOT_SELECTED : 
			   	removeN(player2);
			   	addN(player1);
		   	 	break;
		}
		lineUp.switchPlayers(player1,player2);
	}
	
	protected void addGK(Player p){
		gk2dc.put(p,p.getDC() - p.getGK());
		gk2c.put( p,p.getC()  - p.getGK());
		gk2w.put( p,p.getW()  - p.getGK());
		gk2n.put( p,p.getN()  - p.getGK());
	}
	
	protected void addDC(Player p){
		dc2gk.put(p, p.getGK() - p.getDC());
		dc2c.put(p,  p.getC()  - p.getDC());
		dc2w.put(p,  p.getW()  - p.getDC());
		dc2n.put(p,  p.getN()  - p.getDC());	
	}
	
	protected void addC(Player p){
		c2gk.put(p, p.getGK() - p.getC());
		c2dc.put(p, p.getDC() - p.getC());
		c2w.put(p,  p.getW()  - p.getC());
		c2n.put(p,  p.getN()  - p.getC());			
	}
	
	protected void addW(Player p){
		w2gk.put(p, p.getGK() - p.getW());
		w2dc.put(p, p.getDC() - p.getW());
		w2c.put(p,  p.getC()  - p.getW());
		w2n.put(p,  p.getN()  - p.getW());			
	}
		
	protected void addN(Player p){
		n2gk.put(p, p.getGK() - p.getN());
		n2dc.put(p, p.getDC() - p.getN());
		n2c.put(p,  p.getC()  - p.getN());
		n2w.put(p,  p.getW()  - p.getN());			
	}
	
	protected void removeGK(Player p){
		gk2dc.remove(p);
		gk2c.remove(p);
		gk2w.remove(p);
		gk2n.remove(p);
	}
	
	protected void removeDC(Player p){
		dc2gk.remove(p);
		dc2c.remove(p);
		dc2w.remove(p);
		dc2n.remove(p);
	}
	
	protected void removeC(Player p){
		c2gk.remove(p);
		c2dc.remove(p);
		c2w.remove(p);
		c2n.remove(p);
	}
	
	protected void removeW(Player p){	
		w2gk.remove(p);
		w2dc.remove(p);
		w2c.remove(p);
		w2n.remove(p);
	}
		
	protected void removeN(Player p){		
		n2gk.remove(p);
		n2dc.remove(p);
		n2c.remove(p);
		n2w.remove(p);
	}
}
