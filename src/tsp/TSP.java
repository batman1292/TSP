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
    static Tour chromosome[];
    static Population pop;
    static String local_path = "D:\\anatoliy\\MEE-Term2\\Intelligence System\\TSP\\src\\tsp\\";

    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        String[] file = {"berlin52.txt", "d198.txt", "d657.txt"};
        setNumDataFormFile(file[0]);
        city = new double[num_sample][num_attribute];
        chromosome = new Tour[52];
//        pop = new Population(num_sample);
        readData(file[0]);
        initPopulation();
//        System.out.println(pop.toString());
        System.out.print("\t\t");
        for (int a = 0; a < num_sample; a++) {
            System.out.print(a + "\t\t");
        }
        System.out.println();
        for (int i = 0; i < MAX_GEN; i++) {
            Tour parent1 = selection();
            System.out.println("patent1 " + parent1.toString());
            Tour parent2 = selection();
            System.out.println("patent2 " + parent2.toString());
            pmxCrossover(parent1, parent2);
//            System.out.println("a p1 " + parent1.toString());
//            System.out.println("a p2 " + parent2.toString());
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

    public static Tour selection() {
        double roulette[] = new double[num_sample];
        double fitness = 0.0;
        double max_fitness = pop.sumFitness();
        for (int i = 0; i < num_sample; i++) {
            fitness += pop.getTour(i).getFitness();
            roulette[i] = fitness * 100 / max_fitness;
//            System.out.println("ro["+i+"]"+ pop.getTour(i).getFitness());
        }
        double val_roulette = (Math.random() * max_fitness) * 100 / max_fitness;//random start position of roulette
//        System.out.println("val random" + val_roulette);
        int pos_roulette = 0;
        for (; pos_roulette < num_sample; pos_roulette++) {
            if (roulette[pos_roulette] > val_roulette) {
                break;
            }
        }
//        System.out.println("pos_roulette" + pos_roulette);
//        System.out.print("pop ["+ pos_roulette + "] ");
//            System.out.println(pop.getTour(pos_roulette).toString());
        return pop.getTour(pos_roulette);
    }

    public static void pmxCrossover(Tour p1, Tour p2) {
        Tour offspring1 = new Tour();
        Tour offspring2 = new Tour();
        int temp = 0;
        int crossPoint1 = 0;
        int crossPoint2 = 0;
        double probability = 0.7;
        int min = 0;
        int max = num_sample - 1;
        crossPoint1 = min + (int) (Math.random() * ((max - min) + 1));
        crossPoint2 = min + (int) (Math.random() * ((max - min) + 1));
        if (Math.random() < probability) {
            while (crossPoint1 == crossPoint2) {
                crossPoint2 = min + (int) (Math.random() * ((max - min) + 1));
            }

            if (crossPoint1 > crossPoint2) {
                temp = crossPoint1;
                crossPoint1 = crossPoint2;
                crossPoint2 = temp;
            }
//            System.out.println("parent1 " + p1.toString());
//            System.out.println("parent2 " + p2.toString());
            System.out.println("cross 1=" + crossPoint1 + ", cross 2=" + crossPoint2);
            for (int a = 0; a < num_sample; a++) {
                offspring1.addCity(new City(-1, -1));
                offspring2.addCity(new City(-1, -1));
            }
//        System.out.println("off1   " + offspring1.toString());
//        System.out.println("off2   " + offspring2.toString());
            for (int i = crossPoint1; i <= crossPoint2; i++) {
                City c1 = p1.getCity(i);
                City c2 = p2.getCity(i);
                offspring1.setCity(i, c2);
                offspring2.setCity(i, c1);
            }
            System.out.println();
            System.out.println("_off1 " + offspring1.toString());
            System.out.println("_off2 " + offspring2.toString());

            for (int i = 0; i < num_sample; i++) {
                for (int j = crossPoint1; j <= crossPoint2; j++) {
                    if (i >= crossPoint1 && i <= crossPoint2) {
                        continue;
                    }
                    if (p1.getCity(i).getX() == offspring1.getCity(j).getX()
                            && p1.getCity(i).getY() == offspring1.getCity(j).getY()) {
//                        System.out.println("off1 i=" + i + ",j=" + j);
                        offspring1.setCity(i, offspring2.getCity(j));
                    }

                    if (p2.getCity(i).getX() == offspring2.getCity(j).getX()
                            && p2.getCity(i).getY() == offspring2.getCity(j).getY()) {
//                        System.out.println("*off2 i=" + i + ",j=" + j);
                        offspring2.setCity(i, offspring1.getCity(j));
                    }
                }
            }

            for (int i = 0; i < num_sample; i++) {
                if (offspring1.getCity(i).getX() == -1) {
                    offspring1.setCity(i, p1.getCity(i));
                }

                if (offspring2.getCity(i).getX() == -1) {
                    offspring2.setCity(i, p2.getCity(i));
                }
            }
        } else {
            System.out.println("don't crossover");
            offspring1 = p1;
            offspring2 = p2;
        }
        System.out.println();
        System.out.println("_off1 " + offspring1.toString());
        System.out.println("_off2 " + offspring2.toString());
//        inverseMutation(offspring1, offspring2);
//        return crossover;
    }

    public static void inverseMutation(Tour offs1, Tour offs2) {
        System.out.println("inverse mmutation");
        Tour offs1_inverse = new Tour();
        Tour offs2_inverse = new Tour();
        int min = 0;
        int max = num_sample - 1;
        int crossPoint1 = 0;
        int crossPoint2 = 0;
        int temp = 0;
        crossPoint1 = min + (int) (Math.random() * ((max - min) + 1));
        crossPoint2 = min + (int) (Math.random() * ((max - min) + 1));

        while (crossPoint1 == crossPoint2) {
            crossPoint2 = min + (int) (Math.random() * ((max - min) + 1));
        }

        if (crossPoint1 > crossPoint2) {
            temp = crossPoint1;
            crossPoint1 = crossPoint2;
            crossPoint2 = temp;
        }

        System.out.println("offs1 " + offs1.toString());
//        System.out.println("offs2 " + offs2.toString());
        System.out.println("cross 1=" + crossPoint1 + ", cross 2=" + crossPoint2);

        System.out.println();
        System.out.println("_off1 " + offs1.toString());
//        System.out.println("_off2 " + offs2.toString());
    }
}
