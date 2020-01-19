package reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import main.Statics;
import team.Player;
import team.Team;

public class Reader {

	private final String url;
	
	public Reader(String url){
		this.url = url;
	}
	
	public Team read(){
		Team team = new Team("De ijskikkers");
		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader(url);
			br = new BufferedReader(fr);
			String sCurrentLine;

			int counter = 1;
			Player player = null;
			int number = 0;
			while ((sCurrentLine = br.readLine()) != null) {
				String[] line = sCurrentLine.split("\\s+");
				int[] eurIndexes = getEURindexes(line);
				if(Statics.entered){
					if (counter == 1) {
						number = Integer.parseInt(line[0]);
						counter++;
					} else if (counter == 2){
						player = createPlayerEntered(line, number);
						counter++;
					} else{
						addPlayerInformation(player, eurIndexes[1], line);
						team.addPlayer(player);
						counter = 1;
					}
				} else{
					number = Integer.parseInt(line[0]);
					player = createPlayerNonEntered(line, eurIndexes, number);	
					team.addPlayer(player);		
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		team.printTeamRatings();
		return team;
	}
	
	private Player createPlayerEntered(String[] line, int number){
		String name = "";
		for (String s : line){
			name += s;
			name += " ";
		}
		return new Player(name, number);
	}
	
	private Player createPlayerNonEntered(String[] line, int[] eurIndexes, int number){
		String name = "";
		for (int i = 1; i < eurIndexes[0]-2; i++){
			name += line[i];
			name += " ";
		}
		return addPlayerInformation(new Player(name, number), eurIndexes[1], line);
	}
	
	private Player addPlayerInformation(Player player, int lastEurIndex, String[] line){
 		player.setAge(Integer.parseInt(line[lastEurIndex+1]));
		addPlayerSkills(player,lastEurIndex,line);
//		player.printPlayer();
		return player;
	}

	private int[] getEURindexes(String[] line) {
		int[] index = new int[2];
		boolean second = false;
		for(int i = 0;i<line.length;i++){
			if(line[i].equals("EUR")){
				if (second){
					index[1] = i;
				} else{
					index[0] =  i;
					second = true;
				}
			}
		}
		return index;
	}

	private void addPlayerSkills(Player player, int index, String[] line) {
		player.setPI(Double.parseDouble(line[index+4]));
		player.setPo(Double.parseDouble(line[index+5]));
		player.setSk(Double.parseDouble(line[index+6]));
		player.setPa(Double.parseDouble(line[index+7]));
		player.setQu(Double.parseDouble(line[index+8]));
		player.setSh(Double.parseDouble(line[index+9]));
		player.setKe(Double.parseDouble(line[index+10]));
		player.setPC(Double.parseDouble(line[index+11]));
		player.setCh(Double.parseDouble(line[index+12]));
		player.setSt(Double.parseDouble(line[index+13]));
		player.calculateRatings();
	}
}
