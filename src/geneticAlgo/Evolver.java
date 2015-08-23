package geneticAlgo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

public class Evolver
{	
	private static ArrayList<Chromosome> currGeneration = new ArrayList<Chromosome>();
	private static ArrayList<Chromosome> nextGeneration = new ArrayList<Chromosome>();
	private static Chromosome[] parents = new Chromosome[Constants.parentCount];
	
	private static Comparator<Chromosome> chromComp = new ChromComparator();
	private static PriorityQueue<Chromosome> fitQueue = new PriorityQueue<Chromosome>(1, chromComp);
	
	private static Chromosome solution = null;
	
	public static Chromosome evolvePopulation(final double target) {
		int generationCount = 0;
		
		initPopulation();
		
		while (!fitnessCheckup(currGeneration, target)) {
			System.out.println();
			System.out.println("***** GENERATION NO." + generationCount + " *****");
			System.out.println();
			
//			for (int i = 0; i < currGeneration.size(); i++) {
//				System.out.println(currGeneration.get(i).genome + ": " + currGeneration.get(i).fitness + ", " + currGeneration.get(i).operation);
//			}
			
			do {				
				if (!fitnessCheckup(nextGeneration, target)) {
					selectParents();
					applyCrossover();
					applyMutation();
				}
				else
					break;
			} while (nextGeneration.size() < Constants.initPopulationSize);
			
			currGeneration.clear();
			currGeneration.addAll(nextGeneration);
			nextGeneration.clear();
			
			if (solution != null)
				break;
			
			generationCount++;
		}
		
		return solution;
	}
	
	// helper methods
	private static void initPopulation() {
		for (int i = 0; i < Constants.initPopulationSize; i++) {
			currGeneration.add(new Chromosome(generateRandonGenome()));
		}
	}
	
	private static String generateRandonGenome() {
		String randGenome = new String();
		int[] randSolution = new int[Constants.solutionWidth];
		
		Random rand = new Random();
		
		for (int i = 0; i < Constants.solutionWidth; i++) {
			randSolution[i] = rand.nextInt(15) + 1;
		}
		
		randGenome = Endec.encode(randSolution);
		return randGenome;
	}
	
	
	private static boolean fitnessCheckup(ArrayList<Chromosome> generation, final double target) {
		boolean solutionFound = false;
		
		for (Chromosome chrom : generation) {
			chrom.fitness = Assessor.assessFitness(chrom, target);
			chrom.aggrFitness = chrom.fitness;
			
			if (chrom.fitness == -1) {
				solutionFound = true;
				solution = chrom;
			}
		}
		
		return solutionFound;
	}
	
	private static void selectParents() {
		int aggrIndex = 1;
		ArrayList<Chromosome> aggrFitList = new ArrayList<Chromosome>();
		
		Random roulette = new Random();
		double selection;
		
		// build fitness priority queue
		if (nextGeneration.isEmpty()) {
			for (int i = 0; i < currGeneration.size(); i++) {
				fitQueue.add(currGeneration.get(i));
			}
		}
		else {
			for (int i = 0; i < nextGeneration.size(); i++) {
				fitQueue.add(nextGeneration.get(i));
			}
		}
		
		// compute aggregated values
		aggrFitList.add(fitQueue.poll());
		while (!fitQueue.isEmpty()) {
			aggrFitList.add(fitQueue.poll());
			aggrFitList.get(aggrIndex).aggrFitness += aggrFitList.get(aggrIndex - 1).aggrFitness;
			aggrIndex++;
		}
		
		// select two parents
		for (int i = 0; i < Constants.parentCount; i ++) {
			selection = roulette.nextDouble() * (aggrFitList.get(aggrFitList.size() - 1).aggrFitness);
			for (int j = 0; j < aggrFitList.size(); j++) {
				if (aggrFitList.get(j).aggrFitness >= selection) {
					parents[i] = aggrFitList.remove(j);
					break;
				}
			}
		}
	}
	
	private static void applyCrossover() {
		Random positioner = new Random();
		int crossPosition = positioner.nextInt(Constants.wordWidth * Constants.solutionWidth - 1) + 1;
		
		if (percentageCheck(Constants.crossoverRate)) {
			nextGeneration.add(new Chromosome(parents[0].genome.substring(0, crossPosition) + parents[1].genome.substring(crossPosition, parents[1].genome.length())));
			nextGeneration.add(new Chromosome(parents[1].genome.substring(0, crossPosition) + parents[0].genome.substring(crossPosition, parents[0].genome.length())));
		} else {
			nextGeneration.add(new Chromosome(parents[0].genome));
			nextGeneration.add(new Chromosome(parents[1].genome));
		}
	}
	

	private static void applyMutation() {
		char[] currChrom;
		
		for (int i = 0; i < nextGeneration.size(); i++) {
			currChrom = nextGeneration.get(i).genome.toCharArray();
			for (int j = 0; j < nextGeneration.get(i).genome.length(); j++) {
				if (percentageCheck(Constants.mutationRate)) {
					currChrom[j] = switchBit(currChrom[j]);
				}
			}
			nextGeneration.get(i).genome = String.valueOf(currChrom);
		}
	}
	
	private static boolean percentageCheck(int percentage) {
		boolean check;
		
		Random rand = new Random();
		int randnum = rand.nextInt(100) + 1;
		
		check = randnum <= percentage;
		
		return check;
	}
	
	private static char switchBit(char bit) {
		return (bit == '0') ? '1' : '0';
	}
}
