package Package;

import java.util.ArrayList;
import java.util.Collections;

public class GA {
    Facility fac;
    Solution bestSol;
    int popSize; //tamanho da população
    double muteRatio; //prob de sofrer mutação
    int popIdx[];
    int cIdx[], fIdx[];
    int ite, k; //ite: numero de iterações, k: membros por torneio
    ArrayList<Solution> pop;
 
    public GA(Facility fac, int popSize, double muteRatio, int k, int ite){
        this.popSize = popSize;
        this.muteRatio = muteRatio;
        this.popSize = popSize;
        this.ite = ite;
        this.k = k;
        popIdx = new int[popSize];
        for(int i = 0; i< popSize; i++)
            popIdx[i] = i;
        
        this.fac = fac;
        this.bestSol = new Solution(fac);
        pop = new ArrayList<>();
        
        fIdx = new int[fac.N];
        for (int i = 0; i < fIdx.length; i++)
            fIdx[i] = i;
        cIdx = new int[fac.cli];
        for (int i = 0; i < cIdx.length; i++) {
            cIdx[i] = i;
        }
    } 
    
    public Solution[] select(){
        Utils.shuffler(popIdx);
        Solution dad = pop.get(0);
        for(int i = 1; i < k; i++) {
            int s = popIdx[i];
            if(pop.get(s).compareTo(dad) < 0)
                dad = pop.get(s);
        }
 
        Utils.shuffler(popIdx);
        Solution mom = null;
        for(int i = 0; i < k; i++) {
            int s = popIdx[i];
            if(pop.get(s) != dad && (mom == null || pop.get(s).compareTo(mom) < 0))
                mom = pop.get(s);
        }
 
        return new Solution[]{dad,mom};
    }
    
    public void initPop() {
        pop.clear();
        for (int i = 0; i < popSize; i++) {
            Utils.shuffler(cIdx);
            Utils.shuffler(fIdx);
            Solution sol = new Solution(fac);
            sol.runRandom(cIdx, fIdx);
            pop.add(sol);
        }
    }
    
    public Solution crossover(Solution dad, Solution mom){
        
        int a = fac.cli/3; // 1/3 da quantidade de clientes vai pertencer ao pai para fazer o crossover 
        int b = 2*a;// 2/3 da mãe
        //instancia o filho
        Solution son = new Solution(fac);
        //o filho não pode ter solução pior que o de seus pais (custo deve ser menor)
        int count = Math.max(mom.facUsedCount(), dad.facUsedCount());
        
        for (int c=0; c<a; c++) {
            
            if (son.sumDem[mom.facOf[c]] + fac.demCli[c] <= fac.c[son.facOf[mom.facOf[c]]]) {
                son.sumDem[mom.facOf[c]] += fac.demCli[c];
                son.facOf[c] = mom.facOf[c];
                son.facU[mom.facOf[c]] = true;
            } else {
                boolean fit = false;
                Utils.shuffler(fIdx);
                for(int j = 0; j < count; j++){
                    int f = fIdx[j];
                    if(son.sumDem[f] + fac.demCli[c] <= fac.c[son.facOf[f]]){
                        son.sumDem[f] += fac.demCli[c];
                        son.facOf[c] = f;
                        fit = true;
                        break;
                    }
                }if(!fit) {
                    System.out.println("Error");
                    return null;
                }
            }
        }
        
        for (int c=a; c<fac.cli; c++) {
            
            if (son.sumDem[dad.facOf[c]] + fac.demCli[c] <= fac.c[son.facOf[dad.facOf[c]]]) {
                son.sumDem[dad.facOf[c]] += fac.demCli[c];
                son.facOf[c] = dad.facOf[c];
                son.facU[dad.facOf[c]] = true;
            } else {
                boolean fit = false;
                Utils.shuffler(fIdx);
                for(int j = 0; j < fac.N; j++){
                    int f = fIdx[j];
                    if(son.sumDem[f] + fac.demCli[c] <= fac.c[son.facOf[f]]){
                        son.sumDem[f] += fac.demCli[c];
                        son.facOf[c] = f;
                        fit = true;
                        break;
                    }
                }if(!fit) {
                    System.out.println("Error 2");
                    return null;
                }
            }
        }
        
        for (int f=0; f<fac.N; f++) 
            if (son.sumDem[f] == 0)
                son.facU[f] = false;
        
        return son;
    }
    
    public void mutation(Solution sol) {
        
        for (int c1 = 0; c1 < fac.cli; c1++) {
            
            int C1 = cIdx[c1];
            int fC1 = sol.facOf[C1];
            
            for (int c2 = c1 + 1; c2 < fac.cli; c2++) {
                
                int C2 = cIdx[c2];
                int fC2 = sol.facOf[C2];
                
                boolean swapC1_C2 = sol.sumDem[fC1] - fac.demCli[C1] + fac.demCli[C2] <= fac.c[fC1];
                boolean swapC2_C1 = sol.sumDem[fC2] - fac.demCli[C2] + fac.demCli[C1] <= fac.c[fC2]; 
                
                if (fC1 != fC2 && swapC1_C2 && swapC2_C1) {
                    double costC1_fC2 = fac.costFacCli[fC1][C2] - fac.costFacCli[fC1][C1];                    
                    double costC2_fC1 = fac.costFacCli[fC2][C1] - fac.costFacCli[fC2][C2];
                                        
                    if (costC1_fC2 < 0 && costC2_fC1 < 0) {
                        
                        sol.sumDem[fC1] += -fac.demCli[C1] + fac.demCli[C2];
                        sol.sumDem[fC2] += -fac.demCli[C2] + fac.demCli[C1];

                        sol.facOf[C1] = fC2;
                        sol.facOf[C2] = fC1;
                    }
                }                
            }
        }
    }
    
    public String run() {
        
        initPop();
        bestSol.run();
        
        for (int i = 0; i < ite; i++) {
            Collections.sort(pop);
            
            while (pop.size() > popSize)
                pop.remove(pop.size() - 1);

            Solution parents[] = select();

            Solution son1 = crossover(parents[0], parents[1]);
            if (son1 != null) {
                if (Utils.rd.nextDouble() < muteRatio)
                    mutation(son1);
                if (!pop.contains(son1))
                    pop.add(son1);
            }


            Solution son2 = crossover(parents[1], parents[0]);
            if (son2 != null) {
                if (Utils.rd.nextDouble() < muteRatio)
                    mutation(son2);
                if (!pop.contains(son2))
                    pop.add(son2);
            }
            
            if(bestSol.compareTo(pop.get(0))>0) {
                bestSol.copy(pop.get(0));
            }
        }
        return "GA { " 
                + "\n     Melhor solução {"
                + "\n            Facilidades usadas : " 
                + bestSol.facUsedCount() + "\n            Custo Total : " + bestSol.cost() + 
                "\n     }"+ "\n}\n";
    }
}
