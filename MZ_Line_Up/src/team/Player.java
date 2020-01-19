package team;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import main.Statics;

public class Player {

	private String name;
	private int number;
	private int age;
	private double PI;
	private double Po;
	private double Sk;
	private double Pa;
	private double Qu;
	private double Sh;
	private double Ke;
	private double PC;
	private double Ch;
	private double St;
	private double GK;
	private double DC;
	private double C;
	private double W;
	private double total_pucks;
	private final double N = 0.00;
	
	
	public Player(String name, int number) {
		super();
		this.name = name;
		this.number = number;
	}
	
	public void calculateRatings(){
		calculateTotalPucks();
//		GK = Statics.round((0.5*Ke +0.15*St + 0.1*(PI+Qu) + 0.05*Pa + 0.04*PC+ 0.02*(Po+Sk) + 0.01*(Sh+Ch)) * 0.8 + total_pucks/50,2);
//		DC = Statics.round((0.28*Po + 0.25*Ch + 0.14*Qu + 0.1*St + 0.08*Sk+ 0.07*Sh + 0.04*PI + 0.02*Pa + 0.02*PC + 0*Ke) * 0.8 + total_pucks/50,2);
//		C = Statics.round((0.28*Po + 0.14*Ch + 0.19*Sh + 0.13*Qu + 0.09*St + 0.07*PC + 0.07*Sk + 0.02*PI + 0.01*Pa + 0*Ke) * 0.8 + total_pucks/50,2);
//		W = Statics.round((0.28*Po + 0.25*Sh + 0.14*Qu + 0.1*St + 0.1*Sk + 0.05*Ch + 0.05*PC + 0.02*PI + 0.01*Pa + 0*Ke) * 0.8 + total_pucks/50,2);
		GK = Statics.round((0.5*Math.sin(Ke/10) +0.15*Math.sin(St/10) + 0.1*Math.sin(PI/10)+0.1*Math.sin(Qu/10) + 0.05*Math.sin(Pa/10) + 0.04*Math.sin(PC/10)+ 0.02*Math.sin(Po/10)+0.02*Math.sin(Sk/10) + 0.01*Math.sin(Sh/10)+0.01*Math.sin(Ch/10)) * (8/Math.sin(1)) + total_pucks/50,2);
		DC = Statics.round((0.28*Math.sin(Po/10) + 0.25*Math.sin(Ch/10) + 0.14*Math.sin(Qu/10) + 0.1*Math.sin(St/10) + 0.08*Math.sin(Sk/10) + 0.07*Math.sin(Sh/10) + 0.04*Math.sin(PI/10) + 0.02*Math.sin(Pa/10) + 0.02*Math.sin(PC/10) + 0*Math.sin(Ke)) * (8/Math.sin(1)) + total_pucks/50,2);
		C = Statics.round((0.28*Math.sin(Po/10) + 0.14*Math.sin(Ch/10) + 0.19*Math.sin(Sh/10) + 0.13*Math.sin(Qu/10) + 0.09*Math.sin(St/10) + 0.07*Math.sin(PC/10) + 0.07*Math.sin(Sk/10) + 0.02*Math.sin(PI/10) + 0.01*Math.sin(Pa/10) + 0*Math.sin(Ke)) * (8/Math.sin(1)) + total_pucks/50,2);
		W = Statics.round((0.28*Math.sin(Po/10) + 0.25*Math.sin(Sh/10) + 0.14*Math.sin(Qu/10) + 0.1*Math.sin(St/10) + 0.1*Math.sin(Sk/10) + 0.05*Math.sin(Ch/10) + 0.05*Math.sin(PC/10) + 0.02*Math.sin(PI/10) + 0.01*Math.sin(Pa/10) + 0*Math.sin(Ke)) * (8/Math.sin(1)) + total_pucks/50,2);
	}
	
	public void printPlayer(){
		System.out.println("Name: " + name + ", Age: " + age);
		System.out.println("PI: " + PI + ", Po: " + Po + ", Sk: " + Sk + ", Pa: " + Pa + ", Qu: " + Qu + ", Sh: " + Sh + ", Ke: " + Ke
				       + ", PC: " + PC + ", Ch: " + Ch + ", St: " + St);
		System.out.println("GK: " + GK + ", DC: " + DC + ", C: " + C + ", W: " + W);
	}

	public void printPlayerRatings() {
		System.out.println("Name: " + name + ", GK: " + GK + ", DC: " + DC + ", C: " + C + ", W: " + W);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public double getPI() {
		return PI;
	}

	public void setPI(double pI) {
		PI = pI;
	}

	public double getPo() {
		return Po;
	}

	public void setPo(double po) {
		Po = po;
	}

	public double getSk() {
		return Sk;
	}

	public void setSk(double sk) {
		Sk = sk;
	}

	public double getPa() {
		return Pa;
	}

	public void setPa(double pa) {
		Pa = pa;
	}

	public double getQu() {
		return Qu;
	}

	public void setQu(double qu) {
		Qu = qu;
	}

	public double getSh() {
		return Sh;
	}

	public void setSh(double sh) {
		Sh = sh;
	}

	public double getKe() {
		return Ke;
	}

	public void setKe(double ke) {
		Ke = ke;
	}

	public double getPC() {
		return PC;
	}

	public void setPC(double pC) {
		PC = pC;
	}

	public double getCh() {
		return Ch;
	}

	public void setCh(double ch) {
		Ch = ch;
	}

	public double getSt() {
		return St;
	}

	public void setSt(double st) {
		St = st;
	}

	public double getGK() {
		return GK;
	}

	public void setGK(double gK) {
		GK = gK;
	}

	public double getDC() {
		return DC;
	}

	public void setDC(double dC) {
		DC = dC;
	}

	public double getC() {
		return C;
	}

	public void setC(double c) {
		C = c;
	}

	public double getW() {
		return W;
	}

	public void setW(double w) {
		W = w;
	}

	public double getN() {
		return N;
	}

	public void calculateTotalPucks() {
		total_pucks = PI + Po + Sk + Pa + Qu + Sh + Ke + PC + Ch + St;
	}

	public double getTotalPucks() {
		calculateTotalPucks();
		return this.total_pucks;
	}

	public double getScoreForPosition(Position position) {
		double score = 0.0;
		switch(position){
		   case GOALKEEPER : score = this.GK;
		   	 	break;
		   case DEFENDER : score = this.DC;
		   	 	break;
		   case CENTER : score = this.C;
		   	 	break;
		   case WINGER : score = this.W;
		   	 	break;
		   case NOT_SELECTED : score = this.N;
		   	 	break;
		}
		return Statics.round(score,2);
	}
	
	public static List<Player> sortPlayerListDescending(List<Player> players, Skill skill){
		Collections.sort(players, new Comparator<Player>() {
			@Override
			public int compare(Player p1, Player p2) {
				int returnVal = 0;
				switch(skill){
				   case GOALKEEPER : 
					    if (p1.getGK() > p2.getGK())
					       returnVal = -1;
					    else if (p1.getGK() < p2.getGK())
					       returnVal = 1;
				   	 	break;
				   case DEFENDER : 
					    if (p1.getDC() > p2.getDC())
						       returnVal = -1;
						else if (p1.getDC() < p2.getDC())
						       returnVal = 1;
				   	 	break;
				   case CENTER :
					    if (p1.getC() > p2.getC())
						       returnVal = -1;
						else if (p1.getC() < p2.getC())
						       returnVal = 1;
				   	 	break;
				   case WINGER : 
					    if (p1.getW() > p2.getW())
						       returnVal = -1;
						else if (p1.getW() < p2.getW())
						       returnVal = 1;
				   	 	break;
				   case NOT_SELECTED : 
					    if (p1.getN() > p2.getN())
						       returnVal = -1;
						else if (p1.getN() < p2.getN())
						       returnVal = 1;
				   	 	break;
					case PLAYINTELLIGENCE:
					    if (p1.getPI() > p2.getPI())
						       returnVal = -1;
						else if (p1.getPI() < p2.getPI())
						       returnVal = 1;
						break;
					case POWER:
					    if (p1.getPo() > p2.getPo())
						       returnVal = -1;
						else if (p1.getPo() < p2.getPo())
						       returnVal = 1;
						break;
					case SKATING:
					    if (p1.getSk() > p2.getSk())
						       returnVal = -1;
						else if (p1.getSk() < p2.getSk())
						       returnVal = 1;
						break;
					case PASSING:
					    if (p1.getPa() > p2.getPa())
						       returnVal = -1;
						else if (p1.getPa() < p2.getPa())
						       returnVal = 1;
						break;
					case QUICKNESS:
					    if (p1.getQu() > p2.getQu())
						       returnVal = -1;
						else if (p1.getQu() < p2.getQu())
						       returnVal = 1;
						break;
					case SHOOTING:
					    if (p1.getSh() > p2.getSh())
						       returnVal = -1;
						else if (p1.getSh() < p2.getSh())
						       returnVal = 1;
						break;
					case GOALKEEPING:
					    if (p1.getGK() > p2.getGK())
						       returnVal = -1;
						else if (p1.getGK() < p2.getGK())
						       returnVal = 1;
						break;
					case PUCKCONTROL:
					    if (p1.getPC() > p2.getPC())
						       returnVal = -1;
						else if (p1.getPC() < p2.getPC())
						       returnVal = 1;
						break;
					case CHECKING:
					    if (p1.getCh() > p2.getCh())
						       returnVal = -1;
						else if (p1.getCh() < p2.getCh())
						       returnVal = 1;
						break;
					case STAMINA:
					    if (p1.getSt() > p2.getSt())
						       returnVal = -1;
						else if (p1.getSt() < p2.getSt())
						       returnVal = 1;
						break;
				}
				return returnVal;
			}
		});
		return players;
	}
	
	public static List<Player> sortPlayerListAscending(List<Player> players, Skill skill){
		Collections.sort(players, new Comparator<Player>() {
			@Override
			public int compare(Player p1, Player p2) {
				int returnVal = 0;
				switch(skill){
				   case GOALKEEPER : 
					    if (p1.getGK() < p2.getGK())
					       returnVal = -1;
					    else
					       returnVal = 1;
				   	 	break;
				   case DEFENDER : 
					    if (p1.getDC() < p2.getDC())
						       returnVal = -1;
						    else
						       returnVal = 1;
				   	 	break;
				   case CENTER :
					    if (p1.getC() < p2.getC())
						       returnVal = -1;
						    else
						       returnVal = 1;
				   	 	break;
				   case WINGER : 
					    if (p1.getW() < p2.getW())
						       returnVal = -1;
						    else
						       returnVal = 1;
				   	 	break;
				   case NOT_SELECTED : 
					    if (p1.getN() < p2.getN())
						       returnVal = -1;
						    else
						       returnVal = 1;
				   	 	break;
					case PLAYINTELLIGENCE:
					    if (p1.getPI() < p2.getPI())
						       returnVal = -1;
						    else
						       returnVal = 1;
						break;
					case POWER:
					    if (p1.getPo() < p2.getPo())
						       returnVal = -1;
						    else
						       returnVal = 1;
						break;
					case SKATING:
					    if (p1.getSk() < p2.getSk())
						       returnVal = -1;
						    else
						       returnVal = 1;
						break;
					case PASSING:
					    if (p1.getPa() < p2.getPa())
						       returnVal = -1;
						    else
						       returnVal = 1;
						break;
					case QUICKNESS:
					    if (p1.getQu() < p2.getQu())
						       returnVal = -1;
						    else
						       returnVal = 1;
						break;
					case SHOOTING:
					    if (p1.getSh() < p2.getSh())
						       returnVal = -1;
						    else
						       returnVal = 1;
						break;
					case GOALKEEPING:
					    if (p1.getGK() < p2.getGK())
						       returnVal = -1;
						    else
						       returnVal = 1;
						break;
					case PUCKCONTROL:
					    if (p1.getPC() < p2.getPC())
						       returnVal = -1;
						    else
						       returnVal = 1;
						break;
					case CHECKING:
					    if (p1.getCh() < p2.getCh())
						       returnVal = -1;
						    else
						       returnVal = 1;
						break;
					case STAMINA:
					    if (p1.getSt() < p2.getSt())
						       returnVal = -1;
						    else
						       returnVal = 1;
						break;				
				}
				return returnVal;
			}
		});
		return players;
	}
}
