package Package;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Scanner;

public class Facility {
    /*Numero de Facilidades*/
    public int N;
    
    /*Numero de clientes*/
    public int cli;
    
    /*Demanda de cada cliente*/
    public int demCli[];
    
    /*Capacidade de cada facilidade*/
    public int c[]; 
    
    /*Custo de implantação de cada facilidade*/
    public double costImp[];
    
    /*Custo de atendimento da facilidade i pro cliente j*/
    public double costFacCli[][];
    
    /*Soma da demanda*/
    public double sumDem[];
    
    @Override
    public String toString() {
        return "Facility {" + 
                " Facilidades = " + N + 
                ", Clientes = " + cli + 
                ", Capacidade = " + c + '}';
    }
    
    public Facility(String file) throws FileNotFoundException 
    {
        Scanner sc = new Scanner(new FileInputStream(file));
        sc.useLocale(Locale.US);
        N = sc.nextInt();
        cli = sc.nextInt();
        c = new int[N];
        costImp = new double[N];
        costFacCli = new double[N][cli];
        demCli = new int[cli];
        sumDem = new double[N];
        for(int i=0; i<N;i++){
            c[i] = sc.nextInt();
            costImp[i] = sc.nextDouble();
        }
        for (int j = 0; j < cli; j++) {
            demCli[j] = sc.nextInt();

            for (int i = 0; i < N; i++)
                costFacCli[i][j] = sc.nextDouble();
        }
        for (int i = 0; i < cli; i++) {
            if (demCli[i] > c[0]){
                demCli[i] = c[0];
            }
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < cli; j++) {
                if (demCli[j] == 0) {
                    costFacCli[i][j] = 0;
                }
            }
        }
    }
}
