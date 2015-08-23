package geneticAlgo;

import java.util.Comparator;

public class ChromComparator implements Comparator<Chromosome>
{
    @Override
    public int compare(Chromosome x, Chromosome y)
    {
        if (x == null || y == null) {
        	return 0;
        }
        
        if (x.fitness > y.fitness) {
            return -1;
        }
        else if (x.fitness < y.fitness) {
            return 1;
        }
        
        return 0;
    }
}