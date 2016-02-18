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
    static City chromosome[];
    static Tour population[];
    static String local_path = "D:\\BVH\\MEE\\Intelligent System\\Task4\\";

    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        String[] file = {"berlin52.txt", "d198.txt", "d657.txt"};
        setNumDataFormFile(file[0]);
        city = new double[num_sample][num_attribute];
        chromosome = new City[num_sample];
        population = new Tour[num_sample];
        readData(file[0]);
        initPopulation();
        for (int i = 0; i < MAX_GEN; i++) {

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
            for (int j = 0; j < num_sample; j++) {
                chromosome[j] = new City(city[j][0], city[j][1]);
            }
            population[i] = new Tour(num_sample, chromosome);
            population[i].initIndividual();
        }
    }
}
