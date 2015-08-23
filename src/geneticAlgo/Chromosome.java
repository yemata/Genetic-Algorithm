package geneticAlgo;

public class Chromosome
{
	public double fitness;
	public double aggrFitness;
	
	public String genome;
	public String operation;
	
	public Chromosome(String genome) {
		this.fitness = 0;
		this.aggrFitness = 0;

		this.genome = genome;
		this.operation = "";
	}
}
