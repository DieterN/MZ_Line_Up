package calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import main.Statics;
import team.Player;
import team.Position;
import team.Skill;
import team.Team;

public class LUCOptimized extends LineUpCalculator{

	private List<Player> centers = new ArrayList<Player>();
	
	public LUCOptimized(Team team, int maxAge){
		super(team, maxAge);
		getCenters();
	}

	private void getCenters() {
		List<Player> top = team.getBestCenters(maxAge);
		double[] averages = getAverages(top);
		double avgRt = averages[12];
		System.out.println("CenterRating: " + avgRt*1.01);
		for(int i = 0; i < top.size(); i++){
			Player p = top.get(i);
			if(i < (Statics.threeLines ? 6 : 8) || p.getC() > avgRt) {
				centers.add(p);
				System.out.println(p.getName());
			}
		}
	}

	@Override
	protected void createRandomLineUp() {
		List<Player> players = team.getPlayers(maxAge);
		addCenters(players);
		int i = 0;
		int maxPlayers = 16;
		if(Statics.threeLines){
			maxPlayers = 12;
		}
		for(Player p : players){
			if (i == 0){
				lineUp.addPlayer(p, Position.GOALKEEPER);
				addGK(p);
			} else if (i <= maxPlayers && i % 2 == 1) {
				lineUp.addPlayer(p, Position.DEFENDER);
				addDC(p);
			} else if (i <= maxPlayers && i % 2 == 0){
				lineUp.addPlayer(p, Position.WINGER);
				addW(p);
			} else {
				lineUp.addPlayer(p, Position.NOT_SELECTED);
				addN(p);
			}
			i++;
		}
		lineUp.printLineUpPerPosition(false);
	}
	
	private void addCenters(List<Player> players) {
		int i = 0;
		for(Player p : centers) {
			if(i == 4 || (i == 3 && Statics.threeLines)) {
				break;
			}
			lineUp.addPlayer(p, Position.CENTER);
			addC(p);
			players.remove(p);
			i++;
		}
	}

	@Override
	protected void determineLines() {
		List<Player> defenders = Player.sortPlayerListDescending(lineUp.getDefenders(),Skill.DEFENDER);
		List<Player> centers = Player.sortPlayerListDescending(lineUp.getCenters(),Skill.CENTER);
		List<Player> wingers = Player.sortPlayerListDescending(lineUp.getWingers(),Skill.WINGER);
		for(int i=1; i<=4; i++) {
			double coefficient = (1.02 - i*0.01);
			double st = determineStaminaForLine(defenders, centers, wingers, coefficient);
			defenders = addToLine(defenders,st,getAverages(defenders)[11],coefficient,Position.DEFENDER,2,i);
			centers = addToLine(centers,st,getAverages(centers)[12],coefficient,Position.CENTER,1,i);
			wingers = addToLine(wingers,st,getAverages(wingers)[13],coefficient,Position.WINGER,2,i);
			if(i == 3 && Statics.threeLines) {
				break;
			}
		}
	}
	
	private double determineStaminaForLine(List<Player> defenders, List<Player> centers, List<Player> wingers, double coefficient) {
		double defenders_st = determineStaminaForLinePerPosition(defenders,2,getAverages(defenders)[11]*coefficient);
		double centers_st = determineStaminaForLinePerPosition(centers,1,getAverages(centers)[12]*coefficient);
		double wingers_st = determineStaminaForLinePerPosition(wingers,2,getAverages(wingers)[13]*coefficient);
		return Math.min(defenders_st, Math.min(centers_st, wingers_st)) - 0.25;
	}

	private double determineStaminaForLinePerPosition(List<Player> players, int perLine, double rating) {
		List<Player> sortedPlayers = new ArrayList<Player>();
		sortedPlayers.addAll(players);
	    sortedPlayers = Player.sortPlayerListDescending(sortedPlayers,Skill.STAMINA);
	    int playersInLine = 0;
		for(Player p : sortedPlayers) {
			if(p.getScoreForPosition(lineUp.getPlayerPosition(p)) > rating) {
				playersInLine++;
				if(perLine == playersInLine) {
					return p.getSt();
				}
			}
		}
		return determineStaminaForLinePerPosition(sortedPlayers, perLine, rating*0.99);
	}
	
	private List<Player> addToLine(List<Player> players, double st, double rating, double coefficient, Position pos, int perLine, int line){
		List<Player> returnList = new ArrayList<Player>();
		returnList.addAll(players);
		List<Player> addToLine = new ArrayList<Player>();
		int lineCount = 0;
		double coeff_rating = rating*coefficient;
	    for(Player p : players){
	    	if(lineCount == perLine){
	    		break;
	    	}
    		if(lineCount < perLine && p.getScoreForPosition(pos) >= coeff_rating && p.getSt() >= st){
    			addToLine.add(p);
    			returnList.remove(p);
    			lineCount++;
    		}
	    }
	    if (!(lineCount == perLine)){
	    	returnList = addToLine(players, st, rating, coefficient-0.01, pos, perLine, line);
	    }
	    else{
	    	for(Player p : addToLine) {
		    	lineUp.addPlayerToLine(p,line);	    		
	    	}
	    }
	    return returnList;
	}
    
	@Override
	protected List<Player> updateMaxValues(Map<Player, Double> map1, Map<Player, Double> map2
			                   , Map<Player, Double> nMap1, Map<Player, Double> nMap2){
		List<Player> playerList = super.updateMaxValues(map1, map2, nMap1, nMap2);
		double centerPower = 0.0;
		if(!playerList.isEmpty()) {
			Player p1 = playerList.get(0);
			Player p2 = playerList.get(1);
			Player p3 = playerList.get(2);
			Player p4 = playerList.get(3);
			if(lineUp.getPlayerPosition(p1) == Position.CENTER) {
				if(p4 != null) {
					centerPower = p4.getC() - p1.getC();
				} else {
					centerPower = p2.getC() - p1.getC();
				}
			} else if(lineUp.getPlayerPosition(p2) == Position.CENTER) {
				if(p3 != null) {
					centerPower = p3.getC() - p2.getC();
				} else {
					centerPower = p1.getC() - p2.getC();
				}
			}
			double value = maxValues.get(playerList);
			maxValues.put(playerList, (value + centerPower));
		}
		return playerList;
	}

	@Override
	protected void addGK(Player p){
		gk2dc.put(p,p.getDC() - p.getGK());
		gk2w.put(p,p.getW()   - p.getGK());
		gk2n.put(p,p.getN()   - p.getGK());
		if(centers.contains(p)) {
			gk2c.put(p,p.getC()  - p.getGK());
		}else {
			gk2c.put(p,-10.0);						   
		}
	}

	@Override
	protected void addDC(Player p){
		dc2gk.put(p, p.getGK() - p.getDC());
		dc2w.put(p,  p.getW()  - p.getDC());
		dc2n.put(p,  p.getN()  - p.getDC());	
		if(centers.contains(p)) {
			dc2c.put(p,  p.getC()  - p.getDC());
		}else {
			dc2c.put(p,-10.0);			
		}
	}

	@Override
	protected void addC(Player p){
		c2gk.put(p, p.getGK() - p.getC());
		c2dc.put(p, p.getDC() - p.getC());
		c2w.put(p,  p.getW()  - p.getC());
		c2n.put(p,  p.getN()  - p.getC());			
	}

	@Override
	protected void addW(Player p){
		w2gk.put(p, p.getGK() - p.getW());
		w2dc.put(p, p.getDC() - p.getW());
		w2n.put(p,  p.getN()  - p.getW());	
		if(centers.contains(p)) {
			w2c.put(p,  p.getC()  - p.getW());
		}else {
			w2c.put(p,-10.0);			
		}		
	}
		
	@Override
	protected void addN(Player p){
		n2gk.put(p, p.getGK() - p.getN());
		n2dc.put(p, p.getDC() - p.getN());
		n2w.put(p,  p.getW()  - p.getN());	
		if(centers.contains(p)) {
			n2c.put(p,  p.getC()  - p.getN());
		}else {
			n2c.put(p,-10.0);			
		}				
	}
}
