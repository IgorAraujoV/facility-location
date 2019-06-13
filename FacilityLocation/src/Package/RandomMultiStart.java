package Package;

public class RandomMultiStart {
    
    Facility fac;
    Solution bestSol;
    int nIter, cIdx[], fIdx[];
    
    final double MAX = 999999999;
    
    public RandomMultiStart(int iter, Facility fac) {
        nIter = iter;
        this.fac = fac;
        bestSol = new Solution(fac);
        cIdx = new int[fac.cli];
        fIdx = new int[fac.N];
        for (int c=0; c<fac.cli; c++)
            cIdx[c] = c;
        for (int f=0; f<fac.N; f++)
            fIdx[f] = f;
    }
    
    public String run(Solution bestSol) {

        Solution current = new Solution(fac);
        VND vnd = new VND(fac, current);
        
        //double bestCost = MAX;
        
        for (int i=0; i<nIter; i++) {
            
            Utils.shuffler(cIdx);
            Utils.shuffler(fIdx);
            
            current.runRandom(cIdx, fIdx);
            vnd.run(current);
            
            if (current.cost() < bestSol.cost()) {   
                bestSol.copy(current);
                System.out.println(bestSol);
            }
        }   
        return "\nRandomMultiStart { " 
                + "\n     Melhor solução {"
                + "\n            Facilidades usadas : " 
                + bestSol.facUsedCount() + "\n            Custo Total : " + bestSol.cost() + 
                "\n     }"+ "\n}\n";
    }

//    @Override
//    public String toString() {
//        return "\nRandomMultiStart { " 
//                + "\n     Melhor solução {"
//                + "\n            Facilidades usadas : " 
//                + bestSol.facUsedCount() + "\n            Custo Total : " + bestSol.cost() + 
//                "\n     }"+ "\n}\n";
//    }
    
}
