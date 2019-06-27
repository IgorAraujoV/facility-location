import Package.Facility;
import Package.GA;
import Package.HillClimb;
import Package.IteratedLocalSearch;
import Package.RandomMultiStart;
import Package.SimulatedAnnealing;
import Package.Solution;
import Package.VND;
import Package.VNS;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;

public class Main {
    
    public static void main(String args[]) throws FileNotFoundException {
        
        Facility fac = new Facility("Instancias/cap41.txt");
        Solution sol = new Solution(fac);
        sol.run();
        System.out.println(sol);
        System.out.println("");

        HillClimb hill = new HillClimb(fac, sol);
        hill.run();
        System.out.println(hill);
        
        sol.run();
        VND vnd = new VND(fac);
        vnd.run(sol);
        System.out.println(vnd);
        
        sol.run();
        RandomMultiStart rms = new RandomMultiStart(100, fac);
        System.out.print(rms.run(sol));

        sol.run();
        IteratedLocalSearch ils = new IteratedLocalSearch(1000, 5, fac);
        System.out.println(ils.run());

        sol.run();
        SimulatedAnnealing sa = new SimulatedAnnealing(fac, 10000,.1,1e-4);
        System.out.println(sa.run(sol));

        sol.run();
        GA ga = new GA(fac, 100, 1, 10, 100);
        System.out.println(ga.run());

        sol.run();
        VNS vns = new VNS(1000, 8, 1, fac);
        System.out.println(vns.run());

    }
}
