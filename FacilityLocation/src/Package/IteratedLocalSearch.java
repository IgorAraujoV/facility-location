package Package;

public class IteratedLocalSearch {
    Facility fac;
    Solution bestSol;
    int nFacChanged; //Numero de facilidades modificadas em cada iteração
    int nIter, cIdx[], fIdx[];
    
    final double MAX = 999999999;
    
    public IteratedLocalSearch(int iter, int facChanged, Facility fac) {
        
        nIter = iter;
        nFacChanged = facChanged;
        this.fac = fac;
        bestSol = new Solution(fac);
        
        cIdx = new int[fac.cli];
        fIdx = new int[fac.N];
        
        for (int c=0; c<fac.cli; c++)
            cIdx[c] = c;
        for (int f=0; f<fac.N; f++)
            fIdx[f] = f;
    }
    
    /*Fecha nFacChanged facilidades 
      e realoca os clientes atendidos por ela
      em uma facilidade aleatoria*/
    public void perturb(int nFacChanged, Solution current, Solution best) {
        
        current.copy(best);
        
        int facOf[] = current.facOf;
        boolean facU[] = current.facU;
        double[] sumDem = current.sumDem;
        
        /*Fecha nFacChanged facilidades*/
        for (int k=0; k<nFacChanged; k++) {
            
            int facRand = Utils.rd.nextInt(fac.N);
            
            for (int c=0; c<fac.cli; c++) 
                if (facRand == facOf[c]) 
                    facOf[c] = -1;
            
            facU[facRand] = false;
        }
        
        Utils.shuffler(fIdx);
        
        /*Realoca os clientes retirados em uma facilidade aleatoria*/
        for (int c=0; c<fac.cli; c++) {
            
            if (facOf[c] != -1)
                continue;
            
            for (int f=0; f<fac.N; f++) {
                
                int fR = fIdx[f];
                
                if (sumDem[fR] + fac.demCli[c] < fac.c[fR]) {
                    
                    if (!facU[fR])
                        facU[fR] = true;
                    
                    sumDem[fR] += fac.demCli[c];
                    facOf[c] = fR;
                    
                    break;
                }
            }
        }
    }
    
    public String run() {
        Solution current = new Solution(fac);
        current.run();
        bestSol.run();
        
        VND vnd = new VND(fac);
        vnd.run(current);
        
        for (int ite=0; ite<nIter; ite++) {
            
            perturb(nFacChanged, current, bestSol);
            
            vnd.run(current);
            
            double cost = current.cost();
            double bestCost = bestSol.cost();
                        
            if (cost < bestCost) {
                bestSol.copy(current);
            }
        }
        return "\nIteratedLocalSearch { " 
                + "\n     Melhor solução {"
                + "\n            Facilidades usadas : " 
                + bestSol.facUsedCount() + "\n            Custo Total : " + bestSol.cost() + 
                "\n     }"+ "\n}\n";
    }
}
