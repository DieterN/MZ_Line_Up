package calculator;

import java.util.ArrayList;
import java.util.List;

import main.Statics;
import team.LineUp;
import team.Player;
import team.Position;
import team.Skill;
import team.Team;

public class LUCStandard extends LineUpCalculator{
	
	public LUCStandard(Team team, int maxAge){
		super(team, maxAge);
	}

	@Override
	protected void createRandomLineUp() {
		List<Player> players = team.getPlayers();
		int i = 0;
		int maxPlayers = 20;
		if(Statics.threeLines){
			maxPlayers = 15;
		}
		for(Player p : players){
			if (p.getAge() > maxAge){
				continue;
			}
			if (i < 1){
				lineUp.addPlayer(p, Position.GOALKEEPER);
				addGK(p);
			} else if (i <= maxPlayers && (i-1) % 5 == 0) {
				lineUp.addPlayer(p, Position.CENTER);
				addC(p);
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
	}

	@Override
	protected void determineLines() {
		orderPlayers(lineUp.getDefenders(),Position.DEFENDER,Skill.DEFENDER,2);
		orderPlayers(lineUp.getCenters(),Position.CENTER,Skill.CENTER,1);
		orderPlayers(lineUp.getWingers(),Position.WINGER,Skill.WINGER,2);
	}

	private void orderPlayers(List<Player> players, Position pos, Skill skill, int perLine) {
		double[] averages = super.getAverages(players);
//		double avgSt = getAdjustedAvgStamina(defenders, averages[9], 2);
		double avgSt = averages[9];
		List<Player> sortedPlayers = Player.sortPlayerListDescending(players,skill);
		List<Player> filteredPlayers = addToFirstTwoLines(sortedPlayers,avgSt,averages[11],perLine,pos);
		List<Player> otherSortedPlayers = Player.sortPlayerListDescending(filteredPlayers, skill);
		addToLastTwoLines(otherSortedPlayers,avgSt,perLine);
	}

	private List<Player>addToFirstTwoLines(List<Player> players, double avgSt, double avgRt, int perLine, Position pos){
		return addToFirstTwoLines(players, avgSt, avgRt, perLine, pos, 0);
	}
	
	private List<Player> addToFirstTwoLines(List<Player> players, double avgSt, double avgRt, int perLine, Position pos, int loop){
		List<Player> returnList = new ArrayList<Player>();
		returnList.addAll(players);
		LineUp localLineUp = new LineUp();
		int line1 = 0;
		int line2 = 0;
	    for(Player p : players){
	    	if(line1 == perLine && line2 == perLine){
	    		break;
	    	}
    		if(line1 < perLine && ((p.getSt() > Math.min(avgSt+1, 8.25) && p.getScoreForPosition(pos) > avgRt) || loop > 100)){
    			localLineUp.addPlayerToLine1(p);
    			returnList.remove(p);
    			line1++;
    		}else if((line2 < perLine && p.getSt() > Math.min(avgSt, 7.25) && p.getScoreForPosition(pos) > 0.98*avgRt) || loop > 100){
    			localLineUp.addPlayerToLine2(p);
    			returnList.remove(p);
    			line2++;
    		}
	    }
	    if (!(line1 == perLine && line2 == perLine)){
	    	if (loop % 2 == 0)
	    	   returnList = addToFirstTwoLines(players, avgSt-1, avgRt, perLine, pos, loop + 1);
	    	else
		       returnList = addToFirstTwoLines(players, avgSt+0.5, avgRt*0.99, perLine, pos, loop + 1);
	    }
	    else{
	    	lineUp.getLine1().addAll(localLineUp.getLine1());
	    	lineUp.getLine2().addAll(localLineUp.getLine2());
	    }
	    return returnList;
	}
	
	private void addToLastTwoLines(List<Player> players, double avgSt, int perLine){
		LineUp localLineUp = new LineUp();
		int line3 = 0;
		while(line3 < perLine){	
			for(int i = 0;i <players.size(); i++){
				if(line3 == perLine){
					break;
				}
				Player p = players.get(i);
				if(line3 < perLine && p.getSt() > avgSt-1){
					localLineUp.addPlayerToLine3(p);
					players.remove(p);
					line3++;
					i = -1;
				}
			}
		    avgSt--;
		}
		if (Statics.threeLines){
	    	lineUp.getLine3().addAll(localLineUp.getLine3());				
		} else{
	    	lineUp.getLine3().addAll(localLineUp.getLine3());
	    	lineUp.getLine4().addAll(players);				
		}
	}
}
