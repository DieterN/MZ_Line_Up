package team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.Statics;

public class LineUp {
	
	private Map<Player,Position> players = new HashMap<Player,Position>();
	private List<Player> line1 = new ArrayList<Player>();
	private List<Player> line2 = new ArrayList<Player>();
	private List<Player> line3 = new ArrayList<Player>();
	private List<Player> line4 = new ArrayList<Player>();
		
	public LineUp(){
		
	}
	
	public Map<Player,Position> getLineUp(){
		return players;
	}
		
	public List<Player> getLine1() {
		return line1;
	}

	public void setLine1(List<Player> line1) {
		this.line1 = line1;
	}

	public void addPlayerToLine1(Player player) {
		line1.add(player);
	}

	public List<Player> getLine2() {
		return line2;
	}

	public void addPlayerToLine2(Player player) {
		line2.add(player);
	}

	public void setLine2(List<Player> line2) {
		this.line2 = line2;
	}

	public List<Player> getLine3() {
		return line3;
	}

	public void addPlayerToLine3(Player player) {
		line3.add(player);
	}

	public void setLine3(List<Player> line3) {
		this.line3 = line3;
	}

	public List<Player> getLine4() {
		return line4;
	}

	public void addPlayerToLine4(Player player) {
		line4.add(player);
	}

	public void setLine4(List<Player> line4) {
		this.line4 = line4;
	}

	public Position getPlayerPosition(Player player){
		return players.get(player);
	}
	
	public List<Player> getGoalkeepers(){
		List<Player> goalkeepers = new ArrayList<Player>();
		for(Player p : players.keySet()){
			if (players.get(p) == Position.GOALKEEPER){
				goalkeepers.add(p);
			}
		}
		return goalkeepers;
	}
	
	public List<Player> getDefenders(){
		List<Player> defenders = new ArrayList<Player>();
		for(Player p : players.keySet()){
			if (players.get(p) == Position.DEFENDER){
				defenders.add(p);
			}
		}
		return defenders;
	}
	
	public List<Player> getCenters(){
		List<Player> centers = new ArrayList<Player>();
		for(Player p : players.keySet()){
			if (players.get(p) == Position.CENTER){
				centers.add(p);
			}
		}
		return centers;
	}
	
	public List<Player> getWingers(){
		List<Player> wingers = new ArrayList<Player>();
		for(Player p : players.keySet()){
			if (players.get(p) == Position.WINGER){
				wingers.add(p);
			}
		}
		return wingers;
	}
	
	public List<Player> getNotSelected(){
		List<Player> notSelected = new ArrayList<Player>();
		for(Player p : players.keySet()){
			if (players.get(p) == Position.NOT_SELECTED){
				notSelected.add(p);
			}
		}
		return notSelected;
	}
	
	public void addPlayer(Player player, Position position){
		players.put(player,position);
	}
	
	public void deletePlayer(Player player){
		players.remove(player);
	}

	public void switchPlayers(Player player1, Player player2) {
		Position pos1 = getPlayerPosition(player1);
		Position pos2 = getPlayerPosition(player2);
		players.put(player1, pos2);
		players.put(player2, pos1);
	}
	
	public double getScore(){
		return getScore(this.players);
	}
	
	public double getScore(Map<Player,Position> players){
		double score = 0.0;
		for(Player p : players.keySet()){
			score += p.getScoreForPosition(players.get(p));
		}
		return Statics.round(score, 2);
	}
	
	public void printLineUpPerPosition(boolean not_selected){
		for(Player p : getGoalkeepers()){
			System.out.println("GK: " + p.getNumber() + " " +  p.getName() + " " + p.getGK());
		}
		for(Player p : getDefenders()){
			System.out.println("DC: " + p.getNumber() + " " +  p.getName() + " " + p.getDC());
		}
		for(Player p : getCenters()){
			System.out.println("C: " + p.getNumber() + " " + p.getName() + " " + p.getC());
		}
		for(Player p : getWingers()){
			System.out.println("W: " + p.getNumber() + " " +  p.getName() + " " + p.getW());
		}
		if (not_selected){
			for(Player p : getNotSelected()){
				System.out.println("N: " + p.getNumber() + " " +  p.getName() + " GK:" + p.getGK() + " DC:" + p.getDC() + " C:" + p.getC() + " W:" + p.getW() + " N:" + p.getN());
			}
		}
		System.out.println("");
		System.out.println("Score: " + getScore());
	}
	
	public void printLineUpPerLine(boolean lineScore){
		for(Player p : getGoalkeepers()){
			System.out.println("GK: " + p.getNumber() + " " +  p.getName() + " " + p.getGK());
		}
		System.out.println("Line 1");
		printLine(line1,1,1,lineScore);
		System.out.println("Line 2");
		printLine(line2,2,1,lineScore);
		System.out.println("Line 3");
		printLine(line3,3,1,lineScore);
		if(!Statics.threeLines){
			System.out.println("Line 4");
			printLine(line4,4,1,lineScore);
		}
		System.out.println("");
		System.out.println("Score: " + getScore());
	}
	
	public void printSpecialLineUp(){
		System.out.println("PowerPlay 1");
		printLine(line1,1,1,false);
		System.out.println("PowerPlay 2");
		printLine(line2,2,1,false);
		System.out.println("BoxPlay 1");
		printLine(line3,3,2,false);
		System.out.println("BoxPlay 2");
		printLine(line4,4,2,false);
	}

	private void printLine(List<Player> line, int lineNb, int nb, boolean lineScore) {
		int i = 0;
		Map<Player,Position> linePlayers = new HashMap<Player,Position>();
		while(!line.isEmpty()){
			Player p = line.get(i);
			if(players.get(p).equals(Position.DEFENDER) && nb >= 4){
				System.out.println("DC" + lineNb + ": " + p.getNumber() + " " +  p.getName() + " " + p.getDC());
				nb++;
				line.remove(i);
				linePlayers.put(p,Position.DEFENDER);
			}else if(players.get(p).equals(Position.CENTER) && nb == 1){
				System.out.println("C" + lineNb + ": " + p.getNumber() + " " +  p.getName() + " " + p.getC());
				nb++;
				line.remove(i);
				linePlayers.put(p,Position.CENTER);
			}else if(players.get(p).equals(Position.WINGER) && nb >= 2){
				System.out.println("W" + lineNb + ": " + p.getNumber() + " " +  p.getName() + " " + p.getW());
				nb++;
				line.remove(i);
				linePlayers.put(p,Position.WINGER);
			}
			i++;
			if(i>line.size()-1){
				i = 0;
			}
		}
		if(lineScore) {
			System.out.println("Score: " + getScore(linePlayers));
		}
		
	}

	public void clearLines() {
		this.line1.clear();
		this.line2.clear();
		this.line3.clear();
		this.line4.clear();
	}

	public void addPlayerToLine(Player p, int line) {
		switch(line){
			case 1 :
				addPlayerToLine1(p);
			break;
			case 2 :
				addPlayerToLine2(p);
			break;
			case 3 :
				addPlayerToLine3(p);
			break;
			case 4 :
				addPlayerToLine4(p);
			break;
		}	
	}
}
