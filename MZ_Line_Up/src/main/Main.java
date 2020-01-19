package main;

import calculator.LUCStandard;
import calculator.LUCCenter;
import calculator.LUCOptimized;
import calculator.SpecialLineCalculator;
import reader.Reader;
import team.LineUp;
import team.Team;

public class Main {

	public static void main(String... args){
		Reader reader = new Reader(Statics.url);
		Team team = reader.read();
//		LineUpCalculatorCenter calc = new LineUpCalculatorCenter(team, Statics.maxAge);
//		LineUpCalculatorStandard calc = new LineUpCalculatorStandard(team, Statics.maxAge);
		LUCOptimized calc = new LUCOptimized(team, Statics.maxAge);
		LineUp lineUp = calc.calculate();
		if(!Statics.threeLines){
			SpecialLineCalculator speCalc = new SpecialLineCalculator(lineUp);
 			LineUp specialLineUp = speCalc.calculate();
 			lineUp.printLineUpPerLine(true);
 			specialLineUp.printSpecialLineUp();
		}else{
 			lineUp.printLineUpPerLine(true);			
		}
	}
	
}
