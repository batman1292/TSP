/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tsp;

/**
 *
 * @author 2IDR-PC
 */
public class Population {

    // Holds population of tours
    Tour[] tours;

    public Population(int num_tour) {
        this.tours = new Tour[num_tour];
        for (int i = 0; i < num_tour; i++) {
            this.tours[i] = new Tour();
        }
//        this.tours = tour;
    }

    public Population(Tour tour[]) {
        this.tours = tour;
    }

    public void setTour(int index, Tour tour) {
        this.tours[index] = tour;
    }

    public Tour getTour(int index) {
        return tours[index];
    }

    public int size() {
        return this.tours.length;
    }

    public double getBestFitness() {
        double fitness = 0.0;
        for (int i = 0; i < this.tours.length; i++) {
            if (this.tours[i].getFitness() > fitness) {
                fitness = this.tours[i].getFitness();
            }
        }
        return fitness;
    }

    public double sumFitness() {
        double fitness = 0.0;
        for (int i = 0; i < this.tours.length; i++) {
//            if(this.tours[i].getFitness() > fitness){
            fitness += this.tours[i].getFitness();
//            }?
        }
        return fitness;
    }

    public double getBestDistance() {
        double distance = 999999999;
        for (int i = 0; i < this.tours.length; i++) {
            if (this.tours[i].getDistance() < distance) {
                distance = this.tours[i].getDistance();
            }
        }
        return distance;
    }

    public double sumDistance() {
        double fitness = 0.0;
        for (int i = 0; i < this.tours.length; i++) {
//            if(this.tours[i].getFitness() > fitness){
            fitness += this.tours[i].getDistance();
//            }?
        }
        return fitness;
    }

    public void orderTour() {
        for (int i = 0; i < this.tours.length - 1; i++) {
            int index = i;
//            double i_distance = this.tours[index].getDistance();
            for (int j = i + 1; j < this.tours.length; j++) {
//                double j_distance = this.tours[j].getDistance();
                if(this.tours[index].getDistance() > this.tours[j].getDistance()){
//                if (i_distance < j_distance) {
                    index = j;
                }
            }
            Tour temp = this.tours[index];
            this.tours[index] = this.tours[i];
            this.tours[i] = temp;
//            System.out.print(i + "\t");
//            System.out.println(toString());
        }
    }

    @Override
    public String toString() {
        String geneString = " ";
        for (int i = 0; i < this.tours.length; i++) {
            geneString += "p[" + i + "] " + this.getTour(i).getDistance() + "\t" + this.tours[i].toString() + "\n";
        }
        return geneString;
    }
//
//    // Construct a population
//    public Population(int populationSize, boolean initialise) {
//        tours = new Tour[populationSize];
//        // If we need to initialise a population of tours do so
//        if (initialise) {
//            // Loop and create individuals
//            for (int i = 0; i < populationSize(); i++) {
//                Tour newTour = new Tour();
//                newTour.generateIndividual();
//                saveTour(i, newTour);
//            }
//        }
//    }
//    
//    // Saves a tour
//    public void saveTour(int index, Tour tour) {
//        tours[index] = tour;
//    }
//    
//    // Gets a tour from population
//    public Tour getTour(int index) {
//        return tours[index];
//    }
//
//    // Gets the best tour in the population
//    public Tour getFittest() {
//        Tour fittest = tours[0];
//        // Loop through individuals to find fittest
//        for (int i = 1; i < populationSize(); i++) {
//            if (fittest.getFitness() <= getTour(i).getFitness()) {
//                fittest = getTour(i);
//            }
//        }
//        return fittest;
//    }
//
//    // Gets population size
//    public int populationSize() {
//        return tours.length;
//    }
}
