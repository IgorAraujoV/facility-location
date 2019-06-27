package Package;

public class SimulatedAnnealing {
    
    Facility fac;
    public int nIter;
    public double alpha;
    public double Tf;
    public int cIdx[], fIdx[];   
    
    public SimulatedAnnealing(Facility fac, int ITE, double alpha, double tf) {
        this.nIter = ITE;
        this.alpha = alpha;
        this.fac = fac;
        
        Tf = tf;
        cIdx = new int[fac.cli];
        for (int i=0; i<fac.cli; i++)
            cIdx[i] = i;
        fIdx = new int[fac.N];
        for (int i=0; i<fac.N; i++)
            fIdx[i] = i;
        
    }
    
    public void perturb(Solution copy, Solution current) {
        copy.copy(current);
        
        int facOf[] = copy.facOf;
        boolean facU[] = copy.facU;
        double sumDem[] = copy.sumDem;
        
        Utils.shuffler(cIdx);
        
        if (Utils.rd.nextBoolean()) {
            for (int c1 = 0; c1 < fac.cli; c1++) {
            
                int C1 = cIdx[c1];
                int fC1 = facOf[C1];

                for (int c2 = c1 + 1; c2 < fac.cli; c2++) {

                    int C2 = cIdx[c2];
                    int fC2 = facOf[C2];

                    boolean canSwapC1_C2 = sumDem[fC1] - fac.demCli[C1] + fac.demCli[C2] <= fac.c[fC1];
                    boolean canSwapC2_C1 = sumDem[fC2] - fac.demCli[C2] + fac.demCli[C1] <= fac.c[fC2]; 

                    if (fC1 != fC2 && canSwapC1_C2 && canSwapC2_C1) {

                        sumDem[fC1] += -fac.demCli[C1] + fac.demCli[C2];
                        sumDem[fC2] += -fac.demCli[C2] + fac.demCli[C1];

                        facOf[C1] = fC2;
                        facOf[C2] = fC1;

                        return;
                    }                
                }
            }
        } else {
            for (int c=0; c<fac.cli; c++) {
                
                int fc = facOf[cIdx[c]];
            
                for (int f : fIdx) {
                    if(fc != f && (sumDem[f] + fac.demCli[c] <= fac.c[f])) {                        
                        sumDem[f]  += fac.demCli[c];
                        sumDem[fc] -= fac.demCli[c];

                        facU[f] = true;
                        facOf[c] = f;

                        if (sumDem[fc] == 0)
                            facU[fc] = false;

                        return;
                    }
                }
            }
        }
    }
    
    public String run(Solution sol) {
        sol.run();
        
        Solution current = new Solution(fac);
        current.copy(sol);
        
        double cost = sol.cost();
        double T = cost*alpha/Math.log(fac.N);
        
        double lambda = Math.pow(Tf/T,(double)1/nIter);
        
        Solution copy = new Solution(sol.fac);
        
        for (int i=0; i<nIter; i++) {
            
            perturb(copy, current);
            
            if (copy.cost() > current.cost()) {
                current.copy(sol);
            } else if (Utils.rd.nextDouble() < P(T, copy.cost() - current.cost())){
                current.copy(copy);
            }

            if(current.cost() < sol.cost())
                sol.copy(current);
            
            T*=lambda;
        }
        
        return "SimulatedAnnealing { " 
                + "\n     Melhor solução {"
                + "\n            Facilidades usadas : " 
                + sol.facUsedCount() + "\n            Custo Total : " + sol.cost() + 
                "\n     }"+ "\n}\n";
    }
    
    private double P(double t, double delta) {
        return Math.exp(delta/t);
    }
}
