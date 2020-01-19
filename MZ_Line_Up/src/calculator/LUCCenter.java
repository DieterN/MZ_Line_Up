package calculator;

import java.util.ArrayList;
import java.util.List;

import main.Statics;
import team.LineUp;
import team.Player;
import team.Position;
import team.Skill;
import team.Team;

public class LUCCenter extends LineUpCalculator{
	
	public LUCCenter(Team team, int maxAge){
		super(team, maxAge);
	}

	@Override
	protected void createRandomLineUp() {
		List<Player> players = team.getPlayers(maxAge);
		List<Player> centers = getCenters();
		int i = 0;
		int maxPlayers = 16;
		if(Statics.threeLines){
			maxPlayers = 12;
		}
		for(Player p : players){
			if (centers.contains(p)){
				continue;
			}
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
		for(Player p : centers){
		   if(p != null)
		      lineUp.addPlayer(p, Position.CENTER);
		}
	}
	
	private List<Player> getCenters() {
		List<Player> top = team.getBestCenters(maxAge);
		double[] averages = getAverages(top);
		int avgPo = (int) averages[1];
		int avgSh = (int) averages[5];
		int avgCh = (int) averages[8];
		int avgSt = (int) averages[9];
		double avgRt = averages[12];
		int avgPucks = (int) averages[14];
		Player[] centers = new Player[4];
		centers = (pickCenters(avgSt, avgPo, avgRt, avgSh, avgCh, avgPucks, top, 0, centers));
		List<Player> returnList = new ArrayList<Player>();
		for(Player p : centers) {
			returnList.add(p);
		}
		return returnList;
	}

	private Player[] pickCenters(int avgSt, int avgPo, double avgRt, int avgSh, int avgCh, double avgPucks, List<Player> players, int loop, Player[] centers) {
//    	System.out.println("AvgSt: " + avgSt + "; AvgPo: " + avgPo + "; AvgRt: " + avgRt + "; AvgSh: " + avgSh + "; AvgCh: " + avgCh + "; AvgPucks: " + avgPucks);
		for(int i = 0; i < players.size(); i++){
			Player p = players.get(i);
//			if(p.getPo() <= avgPo || p.getSh() < Math.min(avgSh,7.75) || p.getCh() < Math.min(avgCh,6.75) 
//					||   p.getTotalPucks() < avgPucks
//			   ){
//				continue;
//			}
    		if(centers[0] == null && p.getSt() > Math.min(avgSt+1, 8.25) && p.getC() > avgRt){
    			centers[0] = p;
    			players.remove(p);
    			i = -1;
    		}else if(centers[1] == null && p.getSt() > Math.min(avgSt, 7.75) && p.getC() > avgRt){
    			centers[1] = p;
    			players.remove(p);
    			i = -1;
    		}else if(centers[2] == null && p.getSt() >= Math.min(avgSt, 7) && p.getSt() <= Math.max(avgSt+1,8) && p.getC() > avgRt*0.99){
    			centers[2] = p;
    			players.remove(p);
    			i = -1;
    			if(Statics.threeLines){
    				break;
    			}
    		}else if(centers[3] == null && !Statics.threeLines && p.getSt() < Math.max(avgSt,6.75) && p.getC() > avgRt*0.98){
    			centers[3] = p;
    			players.remove(p);
    			break;
    		}
    		
		}
		if(centers[0] == null || centers[1] == null || centers[2] == null || (centers[3] == null && !Statics.threeLines)){
			if (loop % 4 == 0){
				avgSt--;
//				avgPucks = avgPucks*0.97;
			} else if (loop % 4  == 1){
				avgSh--;
				avgCh--;
			} else if (loop % 4  == 2){
			    avgPo--;
			} else{
				avgPo++;
			    avgSt++;
				avgRt = avgRt*0.99;
				avgSh+= 0.75;
				avgCh+= 0.75;
			}
			loop++;
			pickCenters(avgSt,avgPo,avgRt,avgSh,avgCh,avgPucks,players,loop,centers);
		}
		return centers;
	}

	@Override
	protected void determineLines() {
		orderPlayers(lineUp.getDefenders(),Position.DEFENDER,Skill.DEFENDER,2);
		orderPlayers(lineUp.getCenters(),Position.CENTER,Skill.CENTER,1);
		orderPlayers(lineUp.getWingers(),Position.WINGER,Skill.WINGER,2);
	}

	private void orderPlayers(List<Player> players, Position pos, Skill skill, int perLine) {
		double[] averages = getAverages(players);
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
	
	@Override
	protected void addGK(Player p){
		gk2dc.put(p,p.getDC() - p.getGK());
		gk2w.put( p,p.getW()  - p.getGK());
		gk2n.put( p,p.getN()  - p.getGK());
	}

	@Override
	protected void addDC(Player p){
		dc2gk.put(p, p.getGK() - p.getDC());
		dc2w.put(p,  p.getW()  - p.getDC());
		dc2n.put(p,  p.getN()  - p.getDC());	
	}

	@Override
	protected void addW(Player p){
		w2gk.put(p, p.getGK() - p.getW());
		w2dc.put(p, p.getDC() - p.getW());
		w2n.put(p,  p.getN()  - p.getW());			
	}

	@Override
	protected void addN(Player p){
		n2gk.put(p, p.getGK() - p.getN());
		n2dc.put(p, p.getDC() - p.getN());
		n2w.put(p,  p.getW()  - p.getN());			
	}
}
