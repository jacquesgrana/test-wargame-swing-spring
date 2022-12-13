package fr.jac.granarolo.wargame.libraries;

import fr.jac.granarolo.wargame.models.Hex;
import fr.jac.granarolo.wargame.models.classes.HexFrange;
import fr.jac.granarolo.wargame.models.enums.TerrainTypeEnum;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static javax.swing.JOptionPane.showMessageDialog;

public class PathFinder {

    private Set<HexFrange> frangeHexes = new HashSet<HexFrange>();
    private Set<HexFrange> treatedHexes = new HashSet<HexFrange>();
    private boolean isJobFinished = false;
    private boolean isFrangeExists = false;

    public PathFinder() {}
    public Set <Set <Hex>> generatePaths(Hex[][] hexes, int MAX_X, int MAX_Y, Hex start, Hex end) {
// TODO modifier en Set<Map<int, Hex>>
        frangeHexes = new HashSet<HexFrange>();
        treatedHexes = new HashSet<HexFrange>();
        isJobFinished = false;
        isFrangeExists = false;
        Set <Set <Hex>> setToReturn = new HashSet<Set <Hex>>();
        //Hex start = startPF;
        System.out.println("start : " + start.toString());
        System.out.println("end : " + end.toString());
        do {
            if(isFrangeExists) {
                //System.out.println("la frange existe");
                treatedHexes.addAll(frangeHexes);
            }
            else {
                //start = startPF;
                isFrangeExists = true;
                //System.out.println("la frange n'existe pas");
                Set<Hex> initPath = new HashSet<>();
                initPath.add(start);
                HexFrange initHex = new HexFrange(start, initPath);
                treatedHexes.add(initHex);
                frangeHexes.add(initHex);
            }
            Set<HexFrange> tempFrange = new HashSet<HexFrange>();
            frangeHexes.stream().forEach(h -> {
                Hex[] neighbors = HexLibrary.calculateNeighbors(h, hexes, MAX_X, MAX_Y);
                Arrays.stream(neighbors).filter(n -> n != null).filter(n -> !HexLibrary.isSetContainsHex(treatedHexes, n)).forEach(n -> {
                    Set<Hex> pathFrange = new HashSet<>(h.getPath());
                    pathFrange.add(n);
                    HexFrange hexFrange = new HexFrange(n, pathFrange);
                    if(HexLibrary.isSetContainsHexFrange(tempFrange, hexFrange)) {
                        // récupérer chemin de hex deja dans le set
                        HexFrange oldHexF = tempFrange.stream().filter(hex -> hex.getPosX() == hexFrange.getPosX() && hex.getPosY() == hexFrange.getPosY()).findFirst().orElse(null);

                        Set<Hex> oldPath = oldHexF.getPath();
                        float oldPathWeight = HexLibrary.calculatePathWeight(oldPath, start);
                        //System.out.println("old hex weight : " + oldWeight);
                        // comparer les chemin par appel méthode calculatePathWeight(Set<Hex> path, )
                        float newPathWeight = HexLibrary.calculatePathWeight(hexFrange.getPath(), start);
                        //System.out.println("new hex weight : " + newWeight);
                        if(newPathWeight < oldPathWeight) {
                            tempFrange.remove(oldHexF);
                            tempFrange.add(hexFrange);
                        }
                    }
                    else {

                        if(hexFrange.isEqualToHex(end)) {
                            Set<Hex> pathToAdd = hexFrange.getPath();
                            setToReturn.add(pathToAdd);
                            //isJobFinished = true;
                            System.out.println("chemin trouvé");

                            int pathNb = setToReturn.size();
                            Hex[] endNeighbors = HexLibrary.calculateNeighbors(end, hexes, MAX_X, MAX_Y);
                            long notPassableHexNb = Arrays.stream(endNeighbors).filter(nE -> nE != null).filter(nE -> !HexLibrary.isTerrainPassable(nE)).count();
                            long nullHexNb = Arrays.stream(endNeighbors).filter(nE -> nE == null).count();


                            if(pathNb + notPassableHexNb + nullHexNb == 6) {
                                System.out.println("travail fini");
                                isJobFinished = true;
                            }
                        }
                        else {
                            //System.out.println("hexFrange : " + hexFrange.toString());
                            if (HexLibrary.isTerrainPassable(n)) {
                                tempFrange.add(hexFrange);
                            }
                            else {
                                treatedHexes.add(hexFrange);
                            }
                        }


                    }
                    //System.out.println("isJobFinished : " + isJobFinished);
                });
            });
            frangeHexes = tempFrange;
            //System.out.println("frangeHexes size : " + frangeHexes.size());
            if(tempFrange.size() == 0) {
                System.out.println("liste vide");
                isJobFinished = true;
/*
                if(setToReturn.size() == 0) {
                    showMessageDialog(null, "Pas de chemin(s) possible(s)");
                }
*/
            }
        }
        while(!isJobFinished);

        if(setToReturn.size() > 0) {
            System.out.println("nombre de chemin : " + setToReturn.size());
            return setToReturn;
        }
        else {
            return null;
        }
    }


}
