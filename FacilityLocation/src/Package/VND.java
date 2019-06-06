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
        //lb = fac.LB();
        for (int i = 0; i < fac.N; i++) 
            fIdx[i] = i;
        
        for (int i = 0; i < fac.cli; i++) 
            cIdx[i] = i;
    }
    
    public boolean N1() {
        for (int c : cIdx) {
            
            int fc = facOf[c];
            
            for (int f : fIdx) {
                
                if(fc != f && (sumDem[f] + demCli[c] <= fac.c[f])) {
                    double costT_FC_C = facU[fc] ? fac.costFacCli[fc][c] : fac.costFacCli[fc][c] + fac.costImp[fc];
                    double costT_F_C =  facU[f]  ? fac.costFacCli[f][c] : fac.costFacCli[f][c] + fac.costImp[f];
                    
                    double costResult = costT_FC_C - costT_F_C;
                    
                    if (costResult > 0) {
                        
                        sumDem[f]  += demCli[c];
                        sumDem[fc] -= demCli[c];
                        facOf[c]    = f;
                        
                        if (sumDem[fc] == 0)
                            facU[fc] = false;
                        
                        return true;
                    }
                    
                    System.out.println("costAtResult : " + costResult);
                }
            }
        }
        
        return false;
    }
    
    public void run() {
        facUsedCount = sol.facUsedCount();
        sumDem = new double[fac.N];
        demCli = fac.demCli;
        facOf = sol.facOf;
        facU = sol.facU;
        
        fill(sumDem, 0);
        
        for (int i=0; i<fac.N; i++)
            sumDem[i] = fac.sumDem[i];
        for (int i = 0; i < fIdx.length; i++)
            fIdx[i] = i;
        
        boolean flag = false;
        do {
            flag = N1();
            //if (!flag)
//                System.out.println("oi");
                //flag = move2();
//            System.out.println(sol);
            //sol.updateAtributes();
        } while (flag);
    }
}
