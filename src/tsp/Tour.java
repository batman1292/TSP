/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tsp;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author 2IDR-PC
 */
//or chromosome 
public class Tour {

    private ArrayList<City> tour = new ArrayList<City>();
//    private City tour[];
    private double fitness = 0.0;
    private double distance = 0.0;

    public Tour() {

    }

    public void addCity(City city) {
        this.tour.add(city);
    }

    public void initIndividual() {
        Collections.shuffle(this.tour);
    }

    public void calDistance() {
        for (int i = 0; i < this.tour.size() - 1; i++) {
            this.distance += this.tour.get(i).distanceTo(this.tour.get(i + 1));
        }
    }

    public double getDistance() {
        if (this.distance == 0) {
            calDistance();
        }
        return this.distance;
    }

    public double getFitness() {
        if (this.distance == 0) {
            calDistance();
        }
        this.fitness = 1 / this.distance;
        return this.fitness;
    }

    @Override
    public String toString() {
        String geneString = "|";
        for (int i = 0; i < this.tour.size(); i++) {
            geneString += this.tour.get(i) + "|";
        }
        return geneString;
    }
}
