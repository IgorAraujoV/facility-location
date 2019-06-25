package Package;

public class IteratedLocalSearch {
    Facility fac;
    Solution bestSol;
    int nFacClosed; //Numero de facilidades modificadas em cada iteração
    int nIter, cIdx[], fIdx[];
    
    final double MAX = 999999999;
    
    public IteratedLocalSearch(int iter, int facChanged, Facility fac) {
        
        nIter = iter;
        nFacClosed = facChanged;
        this.fac = fac;
        bestSol = new Solution(fac);
        
        cIdx = new int[fac.cli];
        fIdx = new int[fac.N];
        
        for (int c=0; c<fac.cli; c++)
            cIdx[c] = c;
        for (int f=0; f<fac.N; f++)
            fIdx[f] = f;
    }
    
    /*Fecha nFacClosed facilidades 
      e realoca os clientes atendidos por ela
      em uma facilidade aleatoria*/
    public void perturb(int nFacClosed, Solution current, Solution best) {
        
        current.copy(best);
        
        int facOf[] = current.facOf;
        boolean facU[] = current.facU;
        double[] sumDem = current.sumDem;
        
        /*Fecha nFacClosed facilidades*/
        for (int k=0; k<nFacClosed; k++) {
            
            int facRand = Utils.rd.nextInt(fac.N);
            
            for (int c=0; c<fac.cli; c++) 
                if (facRand == facOf[c]) 
                    facOf[c] = -1;
            
            facU[facRand] = false;
        }
        
        Utils.shuffler(fIdx);
        
        /*Realoca os clientes retirados em uma facilidade aleatoria*/
        
        //while (contains(facOf, -1)) {
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
        //}
    }
    
    public boolean contains(int v[], int value)
    {
        for (int i=0; i<v.length; i++)
            if (v[i] == value)
                return true;
        return false;
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
        
        for (int ite=0; ite<nIter; ite++) {
            
            perturb(nFacClosed, current, bestSol);
            
            for (int i=0; i<current.facOf.length; i++) {
                boolean a = current.facOf[i] == -1;
                if (a) {
                    System.out.println(current.facOf[i] + " " + i);
                    System.out.println(nFacClosed);
                    System.out.println(ite);
                    System.out.println("Deu ruim");
                }
            }
            
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
