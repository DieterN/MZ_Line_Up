package team;

import java.util.ArrayList;
import java.util.List;

import main.Statics;

public class Team {
	
	private String name;
	private List<Player> players = new ArrayList<Player>();
	private LineUp seniorLineUp;
	private LineUp u23LineUp;
	private LineUp u21LineUp;
	private LineUp u18LineUp;
	
	public Team (String name){
		this.name= name;
	}
	
	public void addPlayer(Player player){
		players.add(player);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public LineUp getSeniorLineUp() {
		return seniorLineUp;
	}

	public void setSeniorLineUp(LineUp seniorLineUp) {
		this.seniorLineUp = seniorLineUp;
	}

	public LineUp getU23LineUp() {
		return u23LineUp;
	}

	public void setU23LineUp(LineUp u23LineUp) {
		this.u23LineUp = u23LineUp;
	}

	public LineUp getU21LineUp() {
		return u21LineUp;
	}

	public void setU21LineUp(LineUp u21LineUp) {
		this.u21LineUp = u21LineUp;
	}

	public LineUp getU18LineUp() {
		return u18LineUp;
	}

	public void setU18LineUp(LineUp u18LineUp) {
		this.u18LineUp = u18LineUp;
	}

	public List<Player> getBestCenters(int maxAge) {
		int maxPlayers = 21;
		if(Statics.threeLines){
			maxPlayers = 16;
		}
		List<Player> sortedPlayers = getPlayers(maxAge);
		Player.sortPlayerListDescending(sortedPlayers, Skill.CENTER);
		return new ArrayList<Player>(sortedPlayers.subList(0, Math.min(maxPlayers,sortedPlayers.size())));
	}

	public List<Player> getPlayers(int maxAge) {
		List<Player> returnList = new ArrayList<Player>();
		for(Player p: players){
			if(p.getAge() <= maxAge){
				returnList.add(p);
			}
		}
		return returnList;
	}

	public void printTeam() {
		for(Player p : players) {
			p.printPlayer();
		}
	}

	public void printTeamRatings() {
		for(Player p : players) {
			p.printPlayerRatings();
		}
	}
	
}
