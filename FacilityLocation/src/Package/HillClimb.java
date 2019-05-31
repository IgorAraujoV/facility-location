package Package;

import static java.util.Arrays.fill;

public class HillClimb {
    Facility fac;
    Solution sol;
    
    public int facUsedCount;
    public int save[] = new int[fac.N];
    public int facOf[];
    public boolean facU[];
    public int demCli[];
    public double sumCost[];
    
    public HillClimb(Facility fac, Solution sol) 
    {
        this.fac = fac;
        this.sol = sol;
        
        facUsedCount = sol.facUsedCount();
        save = new int[fac.N];
        facOf = sol.facOf;
        facU = sol.facU;
        demCli = fac.demCli;
        sumCost = new double[fac.N];
        
        fill(sumCost, 0);
        
        for (int f = 0; f < fac.N; f++) {
            save[f] = fac.c[f];
            for (int c = 0; c < fac.cli; c++) {
                sumCost[f] += facU[f]? (fac.costImp[f] + fac.costFacCli[f][c]) : 0;
                save[f]    += facU[f]? demCli[c] : 0;
            }
        }
    }
    
    public int run() 
    {
        
        
        
        
        return 0;
    }
}
