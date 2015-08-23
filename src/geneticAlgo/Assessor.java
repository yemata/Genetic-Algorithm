package geneticAlgo;

import java.util.ArrayList;

public class Assessor
{
	
	/*
	 * encoding scheme
	 * 
	 * 0000: 0, 0
	 * 0001: 1, 1
	 * 0010: 2, 2
	 * 0011: 3, 3
	 * 0100: 4, 4
	 * 0101: 5, 5
	 * 0110: 6, 6
	 * 0111: 7, 7
	 * 1000: 8, 8
	 * 1001: 9, 9
	 * 1010: 10, +
	 * 1011: 11, -
	 * 1100: 12, *
	 * 1101: 13, /
	 *
	 * e.g. (normative)
	 * 		3 12 4 11 2
	 *      3 *  4 -  2
	 *      12     -  2
	 *      10
	 *      
	 *      (non-normative)
	 *      12 7 9 10 2
	 *      *  7 9 +  2
	 *           7 +  2
	 *      
	 *      (non-normative 2)
	 *      3 4 11 2 9
	 *      3 4 -  2 9
	 *      3   -  2 9
	 */
	
	public static double assessFitness(Chromosome chrom, double target) {
		double fitness, result;
		
		result = computeSolution(chrom);
		fitness = (result == target) ? -1 : 1 / (Math.abs(target - result));
				
		return fitness;
	}
	
	private static double computeSolution(Chromosome chrom) {
		int[] solution = Endec.decode(chrom.genome);
		ArrayList<Double> opList = new ArrayList<Double>();
		
		double result = 0;
		int opIndex = 0, prevOpand;
		boolean isCurrOpandValid = false;
		
		chrom.operation = "";
		
		for (int i = 0; i < Constants.solutionWidth; i++) {
			prevOpand = (int) ((opIndex == 0) ? 0 : opList.get(opIndex - 1));
			isCurrOpandValid = isOpandValid(opIndex == 0, prevOpand, solution[i]);
			
			if (isCurrOpandValid) {
//				System.out.println(prevOpand + ", " + solution[i]);
				opList.add((double)solution[i]);
				chrom.operation += numToOpand(solution[i]);
				opIndex++;
			}
			
			if (opList.size() == Constants.opSize) {
				result = decodeOp(opList.get(0), opList.get(1), opList.get(2));
				opList.clear();
				opList.add(result);
				opIndex = 1; // skip current result
			}
		}
		
		return result;
	}
	
	private static double decodeOp(double opand1, double opsym, double opand2) {
		double opResult = -1;
		
		if (opsym == 10)
			opResult = opand1 + opand2;
		else if (opsym == 11)
			opResult = opand1 - opand2;
		else if (opsym == 12)
			opResult = opand1 * opand2;
		else if (opsym == 13)
			opResult = opand1 / opand2;
		
		return opResult;
	}
	
	private static boolean isOpandValid(boolean first, int prev, int curr) {
		boolean opandValid = false;
		
		if (first) {
			opandValid = curr < 10;
		}
		else {
			opandValid = (prev > 9 && prev < 14 && curr < 10)
					|| (prev < 10 && curr > 9 && curr < 14);
		}
		
		return opandValid;
	}
	
	private static char numToOpand(int num) {
		char opand;
		
		if (num < 10)
			opand = (char)(num + Constants.ASCIIOffset);
		else {
			switch (num) {
			case 10:
				opand = '+';
				break;
			case 11:
				opand = '-';
				break;
			case 12:
				opand = '*';
				break;
			case 13:
				opand = '/';
				break;
			default:
				opand = ' ';
			}
		}
		
		return opand;
	}
	public static void main(String args[]) {
		Chromosome testo = new Chromosome("10001010111101101100");
		
		for (int i = 0; i < 5; i++) {
			System.out.println(Endec.decode(testo.genome)[i]);
		}
		
		System.out.println("fitness result: " + assessFitness(testo, 20));
		System.out.println(testo.operation);
	}
}
