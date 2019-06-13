package Package;

import static java.util.Arrays.fill;

public class Solution {
    /*Facilidade que atende o cliente i*/
    public int facOf[];
    
    /*Facilidade é ou não usada*/
    public boolean facU[];
    
    Facility fac;
    
    /*Custo total da implantação das facilidades*/
    public double facCost;
    
    /*Quantidade de facilidades usadas*/
    public int facUsedCount;
    
    /*Numero de facilidades*/
    public int N;
    
    /*Numero de clientes*/
    public int cli;
    
    /*Salvar demanda de cada facilidade*/
    public double save[];
    public double sumDem;
    public static final int MAX = 99999999;

    @Override
    public String toString() {
        return "Solution { " + 
                "Facilidades Usadas = " + facUsedCount()
                + ", Soma Custo = " + cost()
                + '}';
    }
    
    public Solution(Facility fac)
    {
        this.fac = fac;
        N = fac.N;
        cli = fac.cli;
        facOf = new int[cli];
        facU = new boolean[N];
        save = new double[N];
        sumDem = 0;
    }
    
    /*Número de facilidades usadas*/
    public int facUsedCount()
    {
        int count = 0;
        for (boolean u : facU)
            if(u)
                count++;
        return count;
    }
    
    /*Custo de implantação + custo de atendimento
    do cliente i pela facilidade de i*/
    public double cost()
    {
        double cost = 0;
        for (int i=0; i<N; i++) {
            if (facU[i])
                cost += fac.costImp[i];
        }
        
        for (int i=0; i<cli; i++) {
            cost += fac.costFacCli[facOf[i]][i];
        }
        
        return cost;
    }
    
        /* Custo total após mover o cliente c1 pra facilidade de c2
         e o c2 para a facilidade do c1  */
    public double costSwapClients(int c1, int c2) {
        double cost = 0;
        for (int i = 0; i < fac.N; i++) {
            if (facU[i]) {
                cost += fac.costImp[i];
            }
        }
        for (int i = 0; i < fac.cli; i++) {
            if (i == c1) {
                cost += fac.costFacCli[facOf[c2]][i];
                continue;
            }
            if (i == c2) {
                cost += fac.costFacCli[facOf[c1]][i];
                continue;
            }
            cost += fac.costFacCli[facOf[i]][i];
        }
        
        return cost;
    }
    
    public void run() 
    {
        fill(facOf, -1);
        fill(facU, false);
        fill(fac.sumDem, 0);
        
        for (int f=0; f<N; f++) {
            for (int c=0; c<cli; c++) {

                double capacity = fac.c[f] - fac.demCli[c] - fac.sumDem[f];
                if (capacity < 0 || facOf[c] != -1)
                    continue;

                fac.sumDem[f] += fac.demCli[c];
                facOf[c] = f;
            }  
            if(fac.sumDem[f] > 0) {
                facU[f] = true;
            } else {
                facU[f] = false;
            }
        }
//        System.out.println("\n, FacOf = {");
//        for (int c=0; c<cli; c++) 
//            System.out.print(facOf[c] + ", ");
    }
    
    public void runRandom(int[] cIdx, int[] fIdx) {
        fill(facOf, -1);
        fill(facU, false);
        fill(fac.sumDem, 0);
        
        for (int f1=0; f1<N; f1++) {
            
            int f = fIdx[f1];
            
            for (int c1 = 0; c1<cli; c1++) {
                
                int c = cIdx[c1];
                
                double capacity = fac.c[f] - fac.demCli[c] - fac.sumDem[f];
                if (capacity < 0 || facOf[c] != -1)
                    continue;

                fac.sumDem[f] += fac.demCli[c];
                facOf[c] = f;
            }  
            if(fac.sumDem[f] > 0) {
                facU[f] = true;
            } else {
                facU[f] = false;
            }
        }
    }
    
    public void copy(Solution sol) {
        for (int c=0; c<cli; c++)
            facOf[c] = sol.facOf[c];
        for (int f=0; f<N; f++) 
            facU[f] = sol.facU[f];
    }
}