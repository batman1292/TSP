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
//    static String local_path = "D:\\BVH\\MEE\\Intelligent System\\Task4\\";
    static String local_path = "D:\\anatoliy\\MEE-Term2\\Intelligence System\\TSP\\src\\tsp\\";

    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        String[] file = {"berlin52.txt", "d198.txt", "d657.txt"};
        setNumDataFormFile(file[0]);
        city = new double[num_sample][num_attribute];
        chromosome = new Tour[num_sample];
//        pop = new Population(num_sample);
        readData(file[0]);
        initPopulation();
//        System.out.println(pop.getBestFitness());
        for (int i = 0; i < MAX_GEN; i++) {
            Population selection_pop = selection();
            Population crossover_pop = crossover(selection_pop);
            Population mutation_pop = mutation(crossover_pop);
            pop = mutation_pop;
            Population cdss_pop = cdss(mutation_pop);
//            System.out.print(i+"\t");
//            System.out.print(pop+"\t");
////            System.out.println(pop.getBestFitness());
//            System.out.println(pop.toString());
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

    public static Population selection() {
        Population result = new Population(num_sample);
        double roulette[] = new double[num_sample];
        double fitness = 0.0;
        double max_fitness = pop.sumFitness();
        for (int i = 0; i < num_sample; i++) {
            fitness += pop.getTour(i).getFitness();
            roulette[i] = fitness * 100 / max_fitness;
        }
        for (int i = 0; i < num_sample; i++) {
            double val_roulette = (Math.random() * max_fitness) * 100 / max_fitness;//random start position of roulette
            int pos_roulette = 0;
            for (; pos_roulette < num_sample; pos_roulette++) {
                if (roulette[pos_roulette] > val_roulette) {
                    break;
                }
            }
            result.setTour(i, pop.getTour(pos_roulette));
        }
        return result;
//        return pop.getTour(pos_roulette);
    }

    public static Population crossover(Population selection_pop) {
        Population result = new Population(num_sample);
        int index = 0;
        for (int i = 0; i < num_sample / 2; i++) {
            pmxCrossover(selection_pop.getTour(index), selection_pop.getTour(++index), index, result);
            index++;
        }
        return result;
    }

    public static void pmxCrossover(Tour p1, Tour p2, int index, Population cross) {
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
//            System.out.println("cross 1=" + crossPoint1 + ", cross 2=" + crossPoint2);
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
//            System.out.println();
//            System.out.println("_off1 " + offspring1.toString());
//            System.out.println("_off2 " + offspring2.toString());

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
//            System.out.println("don't crossover");
            offspring1 = p1;
            offspring2 = p2;
        }
        cross.setTour(--index, offspring1);
        cross.setTour(++index, offspring2);
//        System.out.println();
//        System.out.println("_off1 " + offspring1.toString());
//        System.out.println("_off2 " + offspring2.toString());
//        inverseMutation(offspring1);
//        return crossover;
    }

    public static Population mutation(Population crossover_pop) {
        Population result = new Population(num_sample);
        for (int i = 0; i < num_sample; i++) {
            result.setTour(i, inverseMutation(crossover_pop.getTour(i)));
        }
        return result;
    }

//    public static void inverseMutation(Tour offs1, Tour offs2) {
//        double probability = 0.1;/\
//        System.out.println("inverse mmutation");
//        Tour offs1_inverse = new Tour();
//        Tour offs2_inverse = new Tour();
//=======
    public static Tour inverseMutation(Tour offs) {
//        System.out.println("inverse mutation");
        Tour offs_inverse = new Tour();
//>>>>>>> origin/master
        int min = 0;
        int max = num_sample - 1;
        int crossPoint1 = 0;
        int crossPoint2 = 0;
        int temp = 0;
        double mutationProbability = 0.1;
        crossPoint1 = min + (int) (Math.random() * ((max - min) + 1));
        crossPoint2 = min + (int) (Math.random() * ((max - min) + 1));
        if (Math.random() < mutationProbability) {
            while (crossPoint1 == crossPoint2) {
                crossPoint2 = min + (int) (Math.random() * ((max - min) + 1));
            }

            if (crossPoint1 > crossPoint2) {
                temp = crossPoint1;
                crossPoint1 = crossPoint2;
                crossPoint2 = temp;
            }
            offs_inverse = offs;
//            System.out.println("to_inv " + offs_inverse.toString());
//            System.out.println("cross 1=" + crossPoint1 + ", cross 2=" + crossPoint2);

            int len = crossPoint2 - crossPoint1;
            int len2 = len >> 1;
            for (int i = 0; i < len2; ++i) {
                City t = offs_inverse.getCity(crossPoint1 + i);
                offs_inverse.setCity(crossPoint1 + i, offs_inverse.getCity(crossPoint2 - i));
                offs_inverse.setCity(crossPoint2 - i, t);
            }
//            System.out.println("mutation complete");
        } else {
            offs_inverse = offs;
        }

//        System.out.println();
//        System.out.println("inv res " + offs.toString());
        return offs_inverse;
//>>>>>>> origin/master
    }

    public static Population cdss(Population mutation_pop) {
        Population result = new Population(num_sample);

        // print before order
        for (int a = 0; a < mutation_pop.size(); a++) {
            System.out.println("b4, a=" + a + " ,\t" + mutation_pop.getTour(a).getFitness() + ", " + mutation_pop.getTour(a));
        }

        // order fitness
        for (int i = 0; i < mutation_pop.size(); i++) {
            double i_fitness = mutation_pop.getTour(i).getFitness();
            for (int j = 0; j < mutation_pop.size(); j++) {
                double j_fitness = mutation_pop.getTour(j).getFitness();
                if (i_fitness < j_fitness) {
                    Tour temp = mutation_pop.getTour(i);
                    mutation_pop.setTour(i, mutation_pop.getTour(j));
                    mutation_pop.setTour(j, temp);
                }
            }
        }
        System.out.println("order done");
        // print after order
        for (int b = 0; b < mutation_pop.size(); b++) {
            System.out.println("AF b=" + b + " ,\t" + mutation_pop.getTour(b).getFitness() + ", " + mutation_pop.getTour(b));
        }

        // comparison
        for (int i = 0; i < mutation_pop.size() - 1; i++) {
            for (int j = i + 1; j < mutation_pop.size(); j++) {
                System.out.println("i=" + i + ",j=" + j);
                cdssFunction(mutation_pop.getTour(i), mutation_pop.getTour(j));
            }
        }
        return result;
    }

    public static Tour cdssFunction(Tour tour1, Tour tour2) {
        boolean check = true;
        for (int i = 0; i < tour1.size(); i++) {
            check = true;
            if (!tour1.getCity(i).equals(tour2.getCity(i))) {
//                System.out.print("false");
//                System.out.println();
                check = false;
                break;
            }
        }
        
        // if two tours are the same
        // need to delete the same element
        if (check) {
            System.out.print("true");
            System.out.println();
            System.out.println(tour1);
            System.out.println(tour2);
        }
        return tour1;
    }

}
