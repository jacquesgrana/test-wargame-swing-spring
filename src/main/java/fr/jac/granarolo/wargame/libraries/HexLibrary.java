package fr.jac.granarolo.wargame.libraries;

import fr.jac.granarolo.wargame.models.Hex;
import fr.jac.granarolo.wargame.models.classes.HexFrange;
import fr.jac.granarolo.wargame.models.enums.TerrainTypeEnum;

import java.awt.*;
import java.util.Set;

public class HexLibrary {
    public static Hex[] calculateNeighbors(Hex hex, Hex[][] hexes, int MAX_X, int MAX_Y) {

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

    public static float calculatePathWeight(Set<Hex> path, Hex startHex) {
        float toReturn = path.stream().map(h -> getTerrainWeight(h.getTerrainType())).reduce(0F, (s,w) ->s + w);
        toReturn -= getTerrainWeight(startHex.getTerrainType());
        return toReturn;
    }
    public static boolean isSetContainsHexFrange(Set<HexFrange> set, HexFrange hexFrange) {
        return set.stream().anyMatch(hf -> hf.getPosX() == hexFrange.getPosX() && hf.getPosY() == hexFrange.getPosY());
        //return false;
    }

    public static boolean isTerrainPassable(Hex h) {
        if(h != null) {
            return h.getTerrainType() != TerrainTypeEnum.WATER;
        }
        return false;
    }

    public static boolean isSetContainsHex(Set<HexFrange> set, Hex hex) {
        return set.stream().anyMatch(hf -> hf.getPosX() == hex.getPosX() && hf.getPosY() == hex.getPosY());
    }

    public static float getTerrainWeight(TerrainTypeEnum terrainType) {
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

    public static Color getColorFromTerrain(TerrainTypeEnum terrain) {
        Color toReturn = Color.BLACK;
        switch (terrain) {
            case WATER:
                toReturn = Color.CYAN;
                break;
            case GRASS:
                toReturn = Color.GREEN;
                break;
            case SAND:
                toReturn = Color.YELLOW;
                break;
            case HILL:
                toReturn = Color.ORANGE;
                break;
        }
        return toReturn;
    }

    public static TerrainTypeEnum getRandomTerrainType() {
        TerrainTypeEnum toReturn = TerrainTypeEnum.GRASS;
        int random = (int) Math.floor(Math.random() * 9);
        switch (random) {
            case 0, 1, 2, 3, 4:
                toReturn = TerrainTypeEnum.GRASS;
                break;
            case 5, 6:
                toReturn = TerrainTypeEnum.HILL;
                break;
            case 7:
                toReturn = TerrainTypeEnum.SAND;
                break;
            case 8:
                toReturn = TerrainTypeEnum.WATER;
                break;
        }
        return toReturn;
    }
}
