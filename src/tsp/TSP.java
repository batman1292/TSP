/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tsp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

/**
 *
 * @author choocku
 */
public class TSP {

    /**
     * @param args the command line arguments
     */
    static int numberChromosome;
    static int MAX_GEN = 1;
    static int num_attribute = 2;
    static int num_sample;
    static double city[][];
    static City gene;
    static Tour chromosome [];
    static Population pop;
    static String local_path = "D:\\BVH\\MEE\\Intelligent System\\Task4\\";

    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        String[] file = {"berlin52.txt", "d198.txt", "d657.txt"};
        setNumDataFormFile(file[0]);
        city = new double[num_sample][num_attribute];
        chromosome = new Tour [52];
//        pop = new Population(num_sample);
        readData(file[0]);
        initPopulation();
        System.out.println(pop.toString());
        for (int i = 0; i < MAX_GEN; i++) {
            Tour parent1 = selection();
            System.out.println("patent1 " + parent1.toString());
            Tour parent2 = selection();
            System.out.println("patent2 " + parent2.toString());
        }
    }

    public static void setNumDataFormFile(String filename) throws FileNotFoundException, IOException {
        String path = local_path + filename;
        File file = new File(path);
        LineNumberReader lnr = new LineNumberReader(new FileReader(file));
        lnr.skip(Long.MAX_VALUE);
        num_sample = lnr.getLineNumber() + 1;
        lnr.close();
    }

    public static void readData(String filename) {
        String path = local_path + filename;
        File file = new File(path);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            int current_line = 0;
            while ((line = br.readLine()) != null) {
//                System.out.println(line);
                String[] ary = line.split(" ");
                for (int i = 0; i < ary.length; i++) {
                    if (i == 0) {
                        continue;
                    } else {
                        city[current_line][i - 1] = Double.parseDouble(ary[i]);
                    }
                }
                current_line++;
            }
            br.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void initPopulation() {
        for (int i = 0; i < num_sample; i++) {
            chromosome[i] = new Tour();
            for (int j = 0; j < num_sample; j++) {
                chromosome[i].addCity(new City(city[j][0], city[j][1]));
            }
            chromosome[i].initIndividual();
//            System.out.println(chromosome.toString());
//            pop.tours[i] = chromosome;
//            System.out.println(pop.toString());
        }
        pop = new Population(chromosome);
//        pop.toString();
    }
    
    public static Tour selection(){
        double roulette [] = new double[num_sample];
        double fitness = 0.0;
        double max_fitness = pop.sumFitness();
        for(int i = 0; i<num_sample; i++){
            fitness += pop.getTour(i).getFitness();
            roulette[i] = fitness*100/max_fitness;
//            System.out.println("ro["+i+"]"+ pop.getTour(i).getFitness());
        }
        double val_roulette = (Math.random() * max_fitness)*100/max_fitness;//random start position of roulette
//        System.out.println("val random" + val_roulette);
        int pos_roulette = 0;
        for( ; pos_roulette<num_sample; pos_roulette++){
            if(roulette[pos_roulette] > val_roulette){
                break;
            }
        }
//        System.out.println("pos_roulette" + pos_roulette);
//        System.out.print("pop ["+ pos_roulette + "] ");
//            System.out.println(pop.getTour(pos_roulette).toString());
        return pop.getTour(pos_roulette);
    }
}
