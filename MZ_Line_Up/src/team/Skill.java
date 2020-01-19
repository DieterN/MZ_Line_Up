package team;

public enum Skill {
	PLAYINTELLIGENCE("PI"), POWER("Po"), SKATING("Sk"), PASSING("Pa"), 
	QUICKNESS("Qu"), SHOOTING("Sh"), GOALKEEPING("GK"), PUCKCONTROL("PC"), 
	CHECKING("Ch"), STAMINA("St"), 
	GOALKEEPER("GK"), DEFENDER("D"), CENTER("C"), WINGER("W"), NOT_SELECTED("N");

	private final String symbol;
	
	private Skill(String symbol){
		this.symbol = symbol;
	}

	public String getSymbol() {
		return symbol;
	}
	
}
