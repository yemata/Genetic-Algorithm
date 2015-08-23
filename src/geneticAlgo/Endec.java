package geneticAlgo;

public class Endec
{	
	public static String encode(int[] decChrom) {
		String enChrom = new String();

		for (int i = 0; i < Constants.solutionWidth; i++) {
			enChrom += intToBits(decChrom[i]);
		}
		
		return enChrom;
	}
	
	public static int[] decode(String enChrom) {
		int[] decChrom = new int[Constants.solutionWidth];
		String[] parsedChrom = parseChrom(enChrom);
		
		for (int i = 0; i < Constants.solutionWidth; i++) {
			decChrom[i] = bitsToInt(parsedChrom[i]);
		}
		
		return decChrom;
	}
	
	// helper methods
	
	private static String intToBits(int n) {
		String bits = new String();
		String bitString = Integer.toBinaryString(n);
		
		// fill up front bits with zeros
		for (int i = 0; i < (Constants.wordWidth - bitString.length()); i++) {
			bits += '0';
		}
		
		bits += bitString;
		
		return bits;
	}
	
	private static int bitsToInt(String bits) {
		int n = 0;
		
		for (int i = 0; i < bits.length(); i++) {
			
			n += (Integer.valueOf(bits.charAt(i)) - Constants.ASCIIOffset)*(Math.pow(2, 3 - i));
		}
		
		return n;
	}
	
	private static String[] parseChrom(String chrom) {
		String[] parsedSolution = new String[Constants.solutionWidth];
		
		for (int i = 0; i < Constants.solutionWidth; i++) {
			parsedSolution[i] = chrom.substring(i*4, (i + 1)*4);
		}
		
		return parsedSolution;
	}
}
