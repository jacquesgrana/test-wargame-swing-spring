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
                Hex[] neighbors = calculateNeighbors(h, hexes, MAX_X, MAX_Y);
                Arrays.stream(neighbors).filter(n -> n != null).filter(n -> !isSetContainsHex(treatedHexes, n)).forEach(n -> {
                    Set<Hex> pathFrange = new HashSet<>(h.getPath());
                    pathFrange.add(n);
                    HexFrange hexFrange = new HexFrange(n, pathFrange);
                    if(isSetContainsHexFrange(tempFrange, hexFrange)) {
                        // récupérer chemin de hex deja dans le set
                        HexFrange oldHexF = tempFrange.stream().filter(hex -> hex.getPosX() == hexFrange.getPosX() && hex.getPosY() == hexFrange.getPosY()).findFirst().orElse(null);

                        Set<Hex> oldPath = oldHexF.getPath();
                        float oldPathWeight = calculatePathWeight(oldPath, start);
                        //System.out.println("old hex weight : " + oldWeight);
                        // comparer les chemin par appel méthode calculatePathWeight(Set<Hex> path, )
                        float newPathWeight = calculatePathWeight(hexFrange.getPath(), start);
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
/*
                            int pathNb = setToReturn.size();
                            Hex[] endNeighbors = calculateNeighbors(end, hexes, MAX_X, MAX_Y);
                            long notPassableHexNb = Arrays.stream(endNeighbors).filter(nE -> nE != null).filter(nE -> !isTerrainPassable(nE)).count();
                            long nullHexNb = Arrays.stream(endNeighbors).filter(nE -> nE == null).count();


                            if(pathNb + notPassableHexNb + nullHexNb == 6) {
                                System.out.println("travail fini");
                                isJobFinished = true;
                            }*/
                        }
                        else {
                            //System.out.println("hexFrange : " + hexFrange.toString());
                            if (isTerrainPassable(n)) {
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
                if(setToReturn.size() == 0) {
                    showMessageDialog(null, "Pas de chemin possible");
                }

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

    private Hex[] calculateNeighbors(Hex hex, Hex[][] hexes, int MAX_X, int MAX_Y) {
        /*
        Hex[] neighbors = new Hex[6];
        int x = hex.getPosX();
        int y = hex.getPosY();
        if(x%2 == 0) {
            if(y%2 == 0) {
                neighbors[0] = hexes[x+1][y];
                neighbors[1] = hexes[x][y-1];
                neighbors[2] = hexes[x-1][y-1];
                neighbors[3] = hexes[x-1][y];
                neighbors[4] = hexes[x-1][y+1];
                neighbors[5] = hexes[x][y+1];
            } else {
                neighbors[0] = hexes[x+1][y];
                neighbors[1] = hexes[x+1][y-1];
                neighbors[2] = hexes[x][y-1];
                neighbors[3] = hexes[x-1][y];
                neighbors[4] = hexes[x][y+1];
                neighbors[5] = hexes[x+1][y+1];
            }

        }
        else {

            if(y%2 == 0) {
                neighbors[0] = hexes[x+1][y];
                neighbors[1] = hexes[x][y-1];
                neighbors[2] = hexes[x-1][y-1];
                neighbors[3] = hexes[x-1][y];
                neighbors[4] = hexes[x-1][y+1];
                neighbors[5] = hexes[x][y+1];
            }
            else {
                neighbors[0] = hexes[x+1][y];
                neighbors[1] = hexes[x+1][y-1];
                neighbors[2] = hexes[x][y-1];
                neighbors[3] = hexes[x-1][y];
                neighbors[4] = hexes[x][y+1];
                neighbors[5] = hexes[x+1][y+1];
            }
        }
        return  neighbors;
        */
        Hex[] neighbors = new Hex[6];
        int x = hex.getPosX();
        int y = hex.getPosY();

        if(y%2 == 0) {
            neighbors[0] = x < (MAX_X - 1) ? hexes[x+1][y] : null;
            neighbors[1] = y > 0 ? hexes[x][y-1] : null;
            neighbors[2] = x > 0 && y > 0 ? hexes[x-1][y-1] : null;
            neighbors[3] = x > 0 ? hexes[x-1][y] : null;
            neighbors[4] = x > 0 && y < (MAX_Y - 1) ? hexes[x-1][y+1] : null;
            neighbors[5] = y < (MAX_Y - 1) ? hexes[x][y+1] : null;
        } else {
            neighbors[0] = x < (MAX_X - 1) ? hexes[x+1][y] : null;
            neighbors[1] = x < (MAX_X - 1) && y > 0 ? hexes[x+1][y-1] : null;
            neighbors[2] = y > 0 ? hexes[x][y-1] : null;
            neighbors[3] = x > 0 ? hexes[x-1][y] : null;
            neighbors[4] = y < (MAX_Y - 1) ? hexes[x][y+1] : null;
            neighbors[5] = x < (MAX_X - 1) && y < (MAX_Y - 1) ? hexes[x+1][y+1] : null;
        }
        return  neighbors;
    }

    private float calculatePathWeight(Set<Hex> path, Hex startHex) {
        float toReturn = path.stream().map(h -> getTerrainWeight(h.getTerrainType())).reduce(0F, (s,w) ->s + w);
        toReturn -= getTerrainWeight(startHex.getTerrainType());
        return toReturn;
    }
    private boolean isSetContainsHexFrange(Set<HexFrange> set, HexFrange hexFrange) {
        return set.stream().anyMatch(hf -> hf.getPosX() == hexFrange.getPosX() && hf.getPosY() == hexFrange.getPosY());
        //return false;
    }

    private boolean isTerrainPassable(Hex h) {
        if(h != null) {
            return h.getTerrainType() != TerrainTypeEnum.WATER;
        }
        return false;
    }

    private boolean isSetContainsHex(Set<HexFrange> set, Hex hex) {
        return set.stream().anyMatch(hf -> hf.getPosX() == hex.getPosX() && hf.getPosY() == hex.getPosY());
    }

    private float getTerrainWeight(TerrainTypeEnum terrainType) {
        float toReturn = 1F;
        switch (terrainType) {
            case WATER -> {
                toReturn = 999F;
            }
            case GRASS -> {
                toReturn = 1F;
            }
            case SAND -> {
                toReturn = 1.5F;
            }
            case HILL -> {
                toReturn = 3F;
            }
        }
        return toReturn;
    }
}
