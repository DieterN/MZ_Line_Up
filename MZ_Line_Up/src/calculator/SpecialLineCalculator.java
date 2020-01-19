package calculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.Statics;
import team.LineUp;
import team.Player;
import team.Position;
import team.Skill;

public class SpecialLineCalculator {

	private Map<List<Player>, Double> maxValues = new HashMap<List<Player>, Double>();
	
	private Map<Player, Double> dc2w = new HashMap<Player, Double>();
	private Map<Player, Double> w2dc = new HashMap<Player, Double>();
	private Map<Player, Double> dc2n = new HashMap<Player, Double>();
	private Map<Player, Double> n2dc = new HashMap<Player, Double>();
	private Map<Player, Double> w2n = new HashMap<Player, Double>();
	private Map<Player, Double> n2w = new HashMap<Player, Double>();
		
	private List<Player> playerList = new ArrayList<Player>();
	private LineUp lineUp;
	
	public SpecialLineCalculator(LineUp lineUp){
		this.lineUp = new LineUp();
		this.playerList.addAll(lineUp.getDefenders());
		this.playerList.addAll(lineUp.getCenters());
		this.playerList.addAll(lineUp.getWingers());
	}
	
	public LineUp calculate(){
		createRandomLineUp();
		int i = 0;
		while(i < Statics.nbOfIterations){
			List<Player> switchPlayers = determineBestMove();
			if (switchPlayers.isEmpty()){
				break;
			} else {
			}
			changePosition(switchPlayers);
			i++;
		}
		determineLines();
		return lineUp;
	}

	private void determineLines() {
		List<Player> wingers = lineUp.getWingers();
		List<Player> defenders = lineUp.getDefenders();
		orderPowerPlay(wingers);
		orderBoxPlay(defenders);
	}

	private void orderPowerPlay(List<Player> wingers) {
		List<Player> sortedWingersC = Player.sortPlayerListDescending(wingers, Skill.CENTER);
		List<Player> sortedWingersPC = Player.sortPlayerListDescending(sortedWingersC, Skill.PUCKCONTROL);
		addPowerPlayCenters(sortedWingersPC);
		List<Player> sortedWingersSh = Player.sortPlayerListDescending(sortedWingersPC, Skill.SHOOTING);
		addPlayersToLines(sortedWingersSh.subList(0, 4), Skill.WINGER, Position.DEFENDER, 1, 2);
		addPlayersToLines(sortedWingersSh.subList(4, 8), Skill.WINGER, Position.WINGER, 1, 2);
	}

	private void orderBoxPlay(List<Player> defenders) {
		List<Player> sortedDefendersC = Player.sortPlayerListDescending(defenders, Skill.CENTER);
		List<Player> sortedDefendersPC = Player.sortPlayerListDescending(sortedDefendersC, Skill.PUCKCONTROL);
		addPlayersToLines(sortedDefendersPC.subList(0, 4), Skill.WINGER, Position.WINGER, 3, 4);
		addPlayersToLines(sortedDefendersPC.subList(4, 8), Skill.WINGER, Position.DEFENDER, 3, 4);
	}
	
	private void addPowerPlayCenters(List<Player> sortedWingers) {
		Player p1 = sortedWingers.get(0);
		Player p2 = sortedWingers.get(1);
		lineUp.addPlayer(p1,Position.CENTER);
		lineUp.addPlayer(p2,Position.CENTER);
		if(p1.getW() >= p2.getW()){
			lineUp.addPlayerToLine1(p1);
			lineUp.addPlayerToLine2(p2);
		} else{
			lineUp.addPlayerToLine1(p2);
			lineUp.addPlayerToLine2(p1);			
		}
		sortedWingers.remove(p1);
		sortedWingers.remove(p2);
	}
	
	private void addPlayersToLines(List<Player> players, Skill skill, Position pos, int line, int line2) {
		List<Player> sortedWingersW = Player.sortPlayerListDescending(players, skill);
		int i = 0;
		for(Player p : sortedWingersW){
			lineUp.addPlayer(p, pos);
			if (i == 0 || i == 3){
				lineUp.addPlayerToLine(p, line);
			} else {
				lineUp.addPlayerToLine(p, line2);				
			}
			i++;
		}
	}

	private void createRandomLineUp() {
		int i = 0;
		for(Player p : playerList){
			if (i < 8) {
				lineUp.addPlayer(p, Position.DEFENDER);
				addDC(p);
			} else if (i < 18){
				lineUp.addPlayer(p, Position.WINGER);
				addW(p);
			} else {
				lineUp.addPlayer(p, Position.NOT_SELECTED);
				addN(p);
			}
			i++;
		}
	}
	
	private List<Player> determineBestMove(){
		this.maxValues.clear();
		updateMaxValues(dc2w, w2dc, n2w, n2dc);
		updateMaxValues(dc2n, n2dc);
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
		}
		return maxPlayerList;
	}
	
    private void updateMaxValues(Map<Player, Double> map1, Map<Player, Double> map2){
    	updateMaxValues(map1, map2, null, null);
    }
	
	private void updateMaxValues(Map<Player, Double> map1, Map<Player, Double> map2
                               , Map<Player, Double> nMap1, Map<Player, Double> nMap2){
		double value = 0.0;
		List<Player> playerList = new ArrayList<Player>();
		Tuple<Player,Double> maxTuple = getMaxTuple(map1);
		playerList.add(maxTuple.getX());
		value += maxTuple.getY();
		maxTuple = getMaxTuple(map2);
		playerList.add(maxTuple.getX());
		value += maxTuple.getY();
		if(nMap1 != null){
			value += getBestNotSelectedSwap(playerList, nMap1, nMap2);
		}
		this.maxValues.put(playerList, value);
	}
	
	private Tuple<Player,Double> getMaxTuple(Map<Player, Double> map1){
		double tempMax = -10.0;
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
		returnValue += getSwapValue(player1, pos2, nMap1);
		returnValue += getSwapValue(player2, pos1, nMap2);
		return returnValue;
	}
	
	private double getSwapValue(Player p, Position pos, Map<Player, Double> nMap1){
		int returnValue = 0;
		Tuple<Player, Double> maxTuple1 = getMaxTuple(nMap1);
		if(p.getScoreForPosition(pos) < maxTuple1.getY()){
			returnValue += (maxTuple1.getY() - p.getScoreForPosition(pos));
		}
		return returnValue;
	}
	
	private void changePosition(List<Player> players){
		Player player1 = players.get(0);
		Player player2 = players.get(1);
		Position pos1 = lineUp.getPlayerPosition(player1);
		Position pos2 = lineUp.getPlayerPosition(player2);
		switch(pos1){
		   case DEFENDER : 
			   	removeDC(player1);
			   	addDC(player2);
		   	 	break;
		   case WINGER : 
			   	removeW(player1);
			   	addW(player2);
		   	 	break;
		   case NOT_SELECTED : 
			   	removeN(player1);
			   	addN(player2);
		   	 	break;
		default:
			    break;
		}
		switch(pos2){
		   case DEFENDER : 
			   	removeDC(player2);
			   	addDC(player1);
		   	 	break;
		   case WINGER : 
			   	removeW(player2);
			   	addW(player1);
		   	 	break;
		   case NOT_SELECTED : 
			   	removeN(player2);
			   	addN(player1);
		   	 	break;
		default:
			    break;
		}
		lineUp.switchPlayers(player1,player2);
	}

	private void addDC(Player p){
		dc2w.put(p,  p.getW()  - p.getDC());
		dc2n.put(p,  p.getN()  - p.getDC());	
	}
	
	private void addW(Player p){
		w2dc.put(p, p.getDC() - p.getW());
		w2n.put(p,  p.getN()  - p.getW());			
	}
		
	private void addN(Player p){
		n2dc.put(p, p.getDC() - p.getN());
		n2w.put(p,  p.getW()  - p.getN());			
	}
	
	private void removeDC(Player p){
		dc2w.remove(p);
		dc2n.remove(p);
	}
	
	private void removeW(Player p){	
		w2dc.remove(p);
		w2n.remove(p);
	}
		
	private void removeN(Player p){		
		n2dc.remove(p);
		n2w.remove(p);
	}
}
