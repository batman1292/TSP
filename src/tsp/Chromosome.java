/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tsp;

import java.util.ArrayList;

/**
 *
 * @author Apro
 */
public class Chromosome {

    private ArrayList<String> chromosome;//for chromosome
    private ArrayList<Double> fitness;//for number of 1 in chromosomee
    private double sigma_fitness;
    private double avg;
    private double max;

    public Chromosome() {
        this.chromosome = new ArrayList<>();
        this.fitness = new ArrayList<>();
        this.sigma_fitness = 0;
        this.avg = 0;
        this.max = 0;
    }

    void set_chromosome(ArrayList<String> chromosome) {
        this.chromosome = chromosome;
    }

    ArrayList<String> get_chromosome() {
        return this.chromosome;
    }

    void set_fitness(ArrayList<Double> fitness) {
        this.fitness = fitness;
    }

    ArrayList<Double> get_fitness() {
        return this.fitness;
    }

    void set_sigma_fitness(double value) {
        this.sigma_fitness = value;
    }

    double get_sigma_fitness() {
        return this.sigma_fitness;
    }

    int get_size() {
        return this.chromosome.size();
    }

    double update_sigma_fitness() {
        this.sigma_fitness = 0;
        for (int i = 0; i < this.chromosome.size(); i++) {
            this.sigma_fitness += this.fitness.get(i);
        }
        return this.sigma_fitness;
    }

    double update_avg_fitness() {
        this.sigma_fitness = 0;
        for (int i = 0; i < this.chromosome.size(); i++) {
            double fit = this.chromosome.get(i).replace("0", "").length();
//            this.fitness.set(i, fit);
            this.sigma_fitness += this.fitness.get(i);
        }
//        System.out.println("^^ = " + this.sigma_fitness);
        this.avg = this.sigma_fitness / this.fitness.size();
        return this.avg;
    }

    double get_avg_fitness() {
        return this.avg;
    }

    double update_max_fitness() {
        this.max = -1;
        for (int i = 0; i < this.chromosome.size(); i++) {
            double fit = this.chromosome.get(i).replace("0", "").length();
//            this.fitness.set(i, fit);
            if (this.max < fit) {
                this.max = fit;
            }
        }
        return this.max;
    }

    double get_max_fitness() {
        return this.max;
    }

    void update() {
        update_avg_fitness();
        update_max_fitness();
    }
}
