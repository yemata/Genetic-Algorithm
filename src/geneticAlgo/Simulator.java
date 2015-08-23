package geneticAlgo;

/**
 * 
 * @author Anta Imata Safo
 * 
 * Genetic simulation aimed at solving the following problem: 
 */

public class Simulator
{
	public static void main(String args[]) {
		Chromosome solution = Evolver.evolvePopulation(Double.valueOf(29.0));
		
		System.out.println();
		System.out.println("***** solution found *****");
		System.out.println();
		
		System.out.println(solution.genome + ": " + solution.operation);
	}
}
