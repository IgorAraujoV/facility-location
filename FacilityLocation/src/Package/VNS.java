package Package;

public class VNS {
    Facility fac;
    Solution bestSol;
    int nfacCloseByIte, nbh; //Numero de facilidades modificadas em cada iteração e vizinhanças
    int nIter, cIdx[], fIdx[];
    
    final double MAX = 999999999;
    
    public VNS(int iter, int nbh, int facCloseByIte, Facility fac) {
        
        nIter = iter;
        this.nbh = nbh;
        nfacCloseByIte = facCloseByIte;
        this.fac = fac;
        bestSol = new Solution(fac);
        
        cIdx = new int[fac.cli];
        fIdx = new int[fac.N];
        
        for (int c=0; c<fac.cli; c++)
            cIdx[c] = c;
        for (int f=0; f<fac.N; f++)
            fIdx[f] = f;
    }
    
    /*Fecha nfacCloseByIte facilidades 
      e realoca os clientes atendidos por ela
      em uma facilidade aleatoria*/
    public void perturb(int nfacCloseByIte, Solution current, Solution best) {
        
        current.copy(best);
        
        int facOf[] = current.facOf;
        boolean facU[] = current.facU;
        double sumDem[] = current.sumDem;
        
        /*Fecha nfacCloseByIte facilidades*/
        for (int k=0; k<nfacCloseByIte; k++) {
            
            int facRand = Utils.rd.nextInt(fac.N);
            
            for (int c=0; c<fac.cli; c++) {
                if (facRand == facOf[c]) {
                    facOf[c] = -1;
                    sumDem[facRand] -= fac.demCli[c];
                }
            }
            facU[facRand] = false;
        }
        
        Utils.shuffler(fIdx);
        
        /*Realoca os clientes retirados em uma facilidade aleatoria*/
        for (int c=0; c<fac.cli; c++) {
            
            if (facOf[c] != -1)
                continue;
            
            for (int f=0; f<fac.N; f++) {
                
                int fR = fIdx[f];
                
                if (sumDem[fR] + fac.demCli[c] <= fac.c[fR]) {
                    
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
        
        Utils.shuffler(cIdx);
        Utils.shuffler(fIdx);
        
        current.runRandom(cIdx, fIdx);
        bestSol.runRandom(cIdx, fIdx);
        
        VND vnd = new VND(fac);
        vnd.run(current);
        
        bestSol.copy(current);
        
        int nbhCount = 0;
        int nFacClosed = 1;
        
        while (nbhCount < nbh) {
        
            for (int ite=0; ite<nIter; ite++) {

                perturb(nFacClosed, current, bestSol);                
                vnd.run(current);

                double cost = current.cost();
                double bestCost = bestSol.cost();

                if (cost < bestCost) {
                    bestSol.copy(current);
                    nFacClosed = 1;
                    nbhCount = 0;
                }
            }
            nFacClosed += nfacCloseByIte;
            nbhCount++;
        }
        return "VNS { " 
                + "\n     Melhor solução {"
                + "\n            Facilidades usadas : " 
                + bestSol.facUsedCount() + "\n            Custo Total : " + bestSol.cost() + 
                "\n     }"+ "\n}\n";
    }
}

