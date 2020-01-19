package team;

public enum Position {
	GOALKEEPER("GK"), DEFENDER("D"), CENTER("C"), WINGER("W"), NOT_SELECTED("N");

	private final String symbol;
	
	private Position(String symbol){
		this.symbol = symbol;
	}

	public String getSymbol() {
		return symbol;
	}
	
}
