package Package;

import static java.util.Arrays.fill;

public class VND {
    Facility fac;
    Solution sol;
    public int fIdx[], cIdx[], lb, facUsedCount;
    public int demCli[], facOf[];
    public double sumDem[];
    public boolean facU[];
    
    public VND(Facility fac, Solution sol) {
        this.fac = fac;
        this.sol = sol;
        cIdx = new int[fac.cli];
        fIdx = new int[fac.N];
        for (int i = 0; i < fac.N; i++) 
            fIdx[i] = i;
        
        for (int i = 0; i < fac.cli; i++) 
            cIdx[i] = i;
    }

    @Override
    public String toString() {
        return "\nVND { " + 
                "Facilidades Usadas = " + sol.facUsedCount()
                + ", Soma Custo = " + sol.cost()
                + '}';
    }
    
    public boolean N1() {
        for (int c : cIdx) {
            
            int fc = facOf[c];
            
            for (int f : fIdx) {
                
                if(fc != f && (sumDem[f] + demCli[c] <= fac.c[f])) {

                    double costT_FC_C = fac.costFacCli[fc][c];
                    double costT_F_C =  fac.costFacCli[f][c];

                    if (fac.sumDem[fc] == fac.demCli[c])
                        costT_FC_C += fac.costImp[fc];

                    if (fac.sumDem[f] == 0)
                        costT_F_C += fac.costImp[f];

                    double costResult = costT_FC_C - costT_F_C;
                    
                    if (costResult > 0) {
                        
                        sumDem[f]  += demCli[c];
                        sumDem[fc] -= demCli[c];
                        facOf[c]    = f;
                        
                        if (sumDem[fc] == 0)
                            facU[fc] = false;
                        
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean N2() {
        
        double costTot = sol.cost();
        
        for (int c1 = 0; c1 < fac.cli; c1++) {
            
            int C1 = cIdx[c1];
            int fC1 = facOf[C1];
            
            for (int c2 = c1 + 1; c2 < fac.cli; c2++) {
                
                int C2 = cIdx[c2];
                int fC2 = facOf[C2];
                
                boolean swapC1_C2 = sumDem[fC1] - demCli[C1] + demCli[C2] <= fac.c[fC1];
                boolean swapC2_C1 = sumDem[fC2] - demCli[C2] + demCli[C1] <= fac.c[fC2]; 
                
                if (fC1 != fC2 && swapC1_C2 && swapC2_C1) {
                    double costC1_fC2 = fac.costFacCli[fC1][C2] - fac.costFacCli[fC1][C1];
                    double costC2_fC1 = fac.costFacCli[fC2][C1] - fac.costFacCli[fC2][C2];
                    
                    double costSwap = sol.costSwapClients(C1, C2);
                    boolean isBest = costTot - costSwap > 0;
                    
                    if ((costC1_fC2 < 0 || costC2_fC1 < 0)) {
                        
                        sumDem[fC1] += -demCli[C1] + demCli[C2];
                        sumDem[fC2] += -demCli[C2] + demCli[C1];

                        facOf[C1] = fC2;
                        facOf[C2] = fC1;

                        return true;
                    }
                }                
            }
        }
        return false;
    }
    
    public void run() {
        facUsedCount = sol.facUsedCount();
        sumDem = fac.sumDem;
        demCli = fac.demCli;
        facOf = sol.facOf;
        facU = sol.facU;
        
        //fill(sumDem, 0);
        
        boolean flag = false;
        do {
            flag = N1();
            if (!flag)
               flag = N2();
            //System.out.println(sol);
        } while (flag);
    }
}
