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
    static int MAX_GEN = 7000;
    static int num_attribute = 2;
    static int num_sample;
    static double city[][];
    static City gene;
    static Tour chromosome[];
    static Population pop;
//    static String local_path = "D:\\BVH\\MEE\\Intelligent System\\Task4\\";
    static String local_path = "D:\\anatoliy\\MEE-Term2\\Intelligence System\\TSP\\src\\tsp\\";
    static boolean Debug = false;

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
//        while(pop.getBestDistance() > 7542){
        for (int i = 0; i < MAX_GEN; i++) {
//            if (MAX_GEN-1 == i) {
//                Debug = true;
//            }
            Population selection_pop = selection();
            Population crossover_pop = crossover(selection_pop);
            Population mutation_pop = mutation(crossover_pop);
            Population cdss_pop = cdss(pop, mutation_pop);
            pop = cdss_pop;
//            System.out.println("-------------------------------------------------------------------------------------------------------------------");
//            System.out.print(i+"\t");
//            System.out.print(pop+"\t");
            
                System.out.print(pop.getBestDistance() + "\t");
                System.out.println(pop.sumDistance() / num_sample);
            
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
        int[] replacement1 = new int[p1.size()];
        int[] replacement2 = new int[p1.size()];
        int temp = 0;
        int crossPoint1 = 0;
        int crossPoint2 = 0;
        double probability = 0.7;
        int min = 0;
        int max = num_sample - 1;
        if (Debug) {
            System.out.print("\t\t");
            for (int i = 0; i < num_sample; i++) {
                System.out.print(i + "\t\t");
            }
            System.out.println();
        }
        if (Math.random() < probability) {
            // step 1 : Get two cutting points
            crossPoint1 = min + (int) (Math.random() * ((max - min) + 1));
            crossPoint2 = min + (int) (Math.random() * ((max - min) + 1));
            while (crossPoint1 == crossPoint2) {
                crossPoint2 = min + (int) (Math.random() * ((max - min) + 1));
            }

            if (crossPoint1 > crossPoint2) {
                temp = crossPoint1;
                crossPoint1 = crossPoint2;
                crossPoint2 = temp;
            } // if

            if (Debug) {
                System.out.println("parent1 " + p1.toString());
                System.out.println("parent2 " + p2.toString());
                System.out.println("cross 1=" + crossPoint1 + ", cross 2=" + crossPoint2);
            } // if

            // STEP 2: Get the subchains to interchange
            for (int a = 0; a < num_sample; a++) {
                offspring1.addCity(new City(-1, -1));
                offspring2.addCity(new City(-1, -1));
            } // for

            // insert 
            for (int i = crossPoint1; i <= crossPoint2; i++) {
                City c1 = p1.getCity(i);
                City c2 = p2.getCity(i);
                offspring1.setCity(i, c2);
                offspring2.setCity(i, c1);
            }
            if (Debug) {
                System.out.println();
                System.out.println("off1 " + offspring1.toString());
                System.out.println("off2 " + offspring2.toString());
            }
            for (int i = 0; i < num_sample; i++) {
                if (i < crossPoint1 || i > crossPoint2) {
                    offspring1.setCity(i, p1.getCity(i));
                    offspring2.setCity(i, p2.getCity(i));
                }
            }
            if (Debug) {
                System.out.println();
                System.out.println("off1 " + offspring1.toString());
                System.out.println("off2 " + offspring2.toString());
            }
            for (int i = 0; i < offspring1.size(); i++) {
                if ((i < crossPoint1) || (i > crossPoint2)) {

                    // check offspring 1 duplicated
                    while (check_forDuplicates(offspring1, i)) {
                        for (int j = crossPoint1; j <= crossPoint2; j++) {
                            if (offspring1.getCity(j).getX() == offspring1.getCity(i).getX()
                                    && offspring1.getCity(j).getY() == offspring1.getCity(i).getY()) {
                                offspring1.setCity(i, offspring2.getCity(j));
                            } else if (offspring2.getCity(j).getX() == offspring1.getCity(i).getX()
                                    && offspring2.getCity(j).getY() == offspring1.getCity(i).getY()) {
                                offspring1.setCity(i, offspring1.getCity(j));
                            }
                        }
                    }

                    // check offspring 2 duplicated
                    while (check_forDuplicates(offspring2, i)) {
                        for (int j = crossPoint1; j <= crossPoint2; j++) {
                            if (offspring2.getCity(j).getX() == offspring2.getCity(i).getX()
                                    && offspring2.getCity(j).getY() == offspring2.getCity(i).getY()) {
                                offspring2.setCity(i, offspring1.getCity(j));
                            } else if (offspring1.getCity(j).getX() == offspring2.getCity(i).getX()
                                    && offspring1.getCity(j).getY() == offspring2.getCity(i).getY()) {
                                offspring2.setCity(i, offspring2.getCity(j));
                            }
                        }
                    }
                }
            }
            if (Debug) {
                System.out.println("repair1 " + offspring1.toString());
                System.out.println("repair2 " + offspring2.toString());
            }
        } else {
            if (Debug) {
                System.out.println("don't crossover");
            }
            offspring1 = p1;
            offspring2 = p2;
        }
        if (Debug) {
            System.out.println();
            System.out.println("off1 " + offspring1.toString());
            System.out.println("off2 " + offspring2.toString());
        }
        cross.setTour(--index, offspring1);
        cross.setTour(++index, offspring2);

//        inverseMutation(offspring1);
//        return crossover;
    }

    public static boolean check_forDuplicates(Tour offspring, int i) {
        for (int index = 0; index < num_sample; index++) {
            if ((offspring.getCity(index).getX() == offspring.getCity(i).getX())
                    && offspring.getCity(index).getY() == offspring.getCity(i).getY()
                    && (i != index)) {
                if (Debug) {
                    System.out.println("duplicate");
                    System.out.println("index=" + index + ", i=" + i
                            + ", offx(i)=" + offspring.getCity(index).getX() + ", offy(index)" + offspring.getCity(i).getY()
                            + ", offx(i)=" + offspring.getCity(index).getX() + ", offy(index)" + offspring.getCity(i).getY());
                }
                return true;
            }
        }
        return false;
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

    public static Population cdss(Population parent_pop, Population mutation_pop) {
        Population temp_result = new Population(num_sample * 2);
        Population result = new Population(num_sample);
        int count = 0;
        //order
//        System.out.println(parent_pop.toString());
        parent_pop.orderTour();
//        System.out.println(parent_pop.toString());
        // comparison
        for (int i = 0; i < num_sample; i++) {
            if (cdssFunction(parent_pop.getTour(i), mutation_pop.getTour(i))) {
                temp_result.setTour(count, parent_pop.getTour(i));
                count++;
            } else {
//                System.out.print(temp_);
                temp_result.setTour(count, parent_pop.getTour(i));
                count++;
                temp_result.setTour(count, mutation_pop.getTour(i));
                count++;
            }
        }
//        System.out.println(count);
        if (count > num_sample) {
            for (int i = 0; i < num_sample; i++) {
                result.setTour(i, temp_result.getTour(i));
            }
        } else {
            result = temp_result;
        }

        return result;
    }

    public static Boolean cdssFunction(Tour tour1, Tour tour2) {
        boolean check = true;
        for (int i = 0; i < tour1.size(); i++) {
            check = true;
            if (tour1.getCity(i).getX() != tour2.getCity(i).getX()
                    || tour1.getCity(i).getY() != tour2.getCity(i).getY()) {
//                System.out.print("false");
//                System.out.println();
                check = false;
                break;
            }
        }
        return check;
    }
}
//>>>>>>> 7c9c67a8a04ba5a44f1e54edd9cc3bdcb8ab4dae
//
