package Package;

import static java.util.Arrays.fill;

public class HillClimb {
    Facility fac;
    Solution sol;
    
    public int facUsedCount;
    public int save[];
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
        
        for (int f = 0; f < fac.N; f++) {
            save[f] = fac.c[f];
            for (int c = 0; c < fac.cli; c++) 
                save[f] += facU[f]? demCli[c] : 0;            
        }
    }

    @Override
    public String toString() {
        return "\nHillClimb { " + "Facilidades Usadas = " + sol.facUsedCount() + ", Soma custo = " + sol.cost()+ " }";
    }
    
    public void run() 
    {
        for (int c=0; c<fac.cli; c++) {
            here:
            for (int f=0; f<fac.N; f++ ) {

                int fc = facOf[c];

                if (fc != f && (fac.sumDem[f] + demCli[c] <= fac.c[f])) {

                    double costFC_C = fac.costFacCli[fc][c];
                    if (fac.sumDem[fc] == fac.demCli[c])
                        costFC_C += fac.costImp[fc];

                    double costF_C  = fac.costFacCli[f][c];
                    if (fac.sumDem[f] == 0)
                        costF_C += fac.costImp[f];

                    //System.out.println("Cost_FC_C : " + costFC_C + " FC :" + fc + " F : " + f + " C : " + c+ " CostF_C : " + costF_C);

                    if(costF_C < costFC_C) {
                        fac.sumDem[f] += demCli[c];
                        fac.sumDem[fc] -= demCli[c];

                        sol.facU[f] = true;
                        sol.facOf[c] = f;

                        if (fac.sumDem[fc] == 0) 
                            facU[fc] = false;

                    }
                }
            }
            //System.out.println("\nFacOf : " + facOf[c]);
        }
    }
}
