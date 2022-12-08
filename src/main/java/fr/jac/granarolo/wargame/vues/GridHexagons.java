package fr.jac.granarolo.wargame.vues;

import fr.jac.granarolo.wargame.libraries.PathFinder;
import fr.jac.granarolo.wargame.models.Hex;
import fr.jac.granarolo.wargame.models.classes.HexFrange;
import fr.jac.granarolo.wargame.models.Unit;
import fr.jac.granarolo.wargame.models.enums.TerrainTypeEnum;
import fr.jac.granarolo.wargame.models.enums.UnitTypeEnum;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

public class GridHexagons extends JPanel {

    private Window window;
    private final Polygon hexagon = new Polygon();
    private final BasicStroke bs1 = new BasicStroke(1);
    private final BasicStroke bs3 = new BasicStroke(3);
    private final Point focusedHexagonLocation = new Point();
    private final Dimension dimension;
    private final int rangeY, rangeX, side;
    private Point mousePosition;
    private int posX = -1, posY = -1, number, gapX, gapY, startX = 100, startY = 100;

    private int pathId = 0, findPathLevel = 0;

    private Hex hex = new Hex(0L, 0, 0, TerrainTypeEnum.GRASS, null);

    private Hex selectedHex = new Hex(0L, 0, 0, TerrainTypeEnum.GRASS, null);

    private Hex start = new Hex(0L, 0, 0, TerrainTypeEnum.GRASS, null);

    private Hex startPF = new Hex(0L, 0, 0, TerrainTypeEnum.GRASS, null);
    private Hex endPF = new Hex(0L, 0, 0, TerrainTypeEnum.GRASS, null);


    private boolean isSelectedHexExists = false;
    private boolean isRightClick = false;
    private boolean isFrangeExists = false;
    private boolean isStartPFChoose = false;
    private boolean isEndPFChoose = false;
    private boolean isPathShow = false;
    private boolean isPathFound = false;

    private Hex[] neighbors = new Hex[6];

    private Set<HexFrange> frangeHexes = new HashSet<HexFrange>();
    private Set<HexFrange> treatedHexes = new HashSet<HexFrange>();

    private Set<Hex> pathToShow = new HashSet<>();

    private Set<Set<Hex>> foundPaths = new HashSet<>();

    //private Map<Hex, ArrayList<Hex>> mapFrangeHexes = new HashMap<>();
    public int MAX_X = 220, MAX_Y = 220;
    //private Color[][] colors = new Color[MAX_X][MAX_Y];

    public Hex[][] hexes = new Hex[MAX_X][MAX_Y];

    public Set<Unit> units = new HashSet<Unit>();

    private Image imageUnit;

    public MouseInputAdapter mouseHandler;
    public KeyAdapter keyHandler;
    public GridHexagons(final int rangeY, final int rangeX, final int side, Window window) {
        this.rangeY = rangeY;
        this.rangeX = rangeX;
        this.side = side;
        this.gapX = (int) side / 2;
        this.gapY = (int) side / 2;
        dimension = getHexagon(0, 0).getBounds().getSize();
        mouseHandler = new MouseInputAdapter() {
            @Override
            public void mouseMoved(final MouseEvent e) {
                mousePosition = e.getPoint();
                repaint();
            }

            @Override
            public void mousePressed(final MouseEvent e) {
                if (number != -1 && SwingUtilities.isLeftMouseButton(e)) {
                    String textToDisplay = "posX : " + posX + " : posY : " + posY + " : terrain : " + hex.getTerrainType().toString();
                    if(hex.getUnits().size() > 0) {
                        textToDisplay += " : nb d'unité(s) : " + hex.getUnits().size() + " : nom(s)";
                        for(Unit unit : hex.getUnits()) {
                            textToDisplay += " : " + unit.getName() + " (type : " + unit.getType().toString() + ")";
                        }
                        //hex.getUnits().stream().forEach(u -> {textToDisplay += " : " + u.getName();});
                    }
                    window.displayInfos(textToDisplay);
                    selectedHex = hex.clone();
                    isSelectedHexExists = true;
                    if(isTerrainPassable(selectedHex)) {
                        window.enableButton3();
                    }
                    else {
                        window.disableButton3();
                    }

                }
                else if (number != -1 && SwingUtilities.isRightMouseButton(e)) {

                    isRightClick = !isRightClick;

                    if (isRightClick) {
                        neighbors = calculateNeighbors(hex);
                    }
                    else {
                        neighbors = new Hex[6];
                    }
                }
            }
        };
        addMouseMotionListener(mouseHandler);
        addMouseListener(mouseHandler);


        keyHandler = new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                //super.keyPressed(e);
                //System.out.println("appui touche");
                int keyCode = e.getKeyCode();
                switch (keyCode) {
                    case KeyEvent.VK_UP:
                        modifyStartValues(0,-2);
                        repaint();
                        break;
                    case KeyEvent.VK_DOWN:
                        modifyStartValues(0,2);
                        repaint();
                        break;
                    case KeyEvent.VK_LEFT:
                        modifyStartValues(-2, 0);
                        repaint();
                        break;
                    case KeyEvent.VK_RIGHT:
                        modifyStartValues(2, 0);
                        repaint();
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
            }
        };
        generateRandomColorList();
        addKeyListener(keyHandler);
        setFocusable(true);
        requestFocus();
        //image1 = getTestImage(Color.ORANGE);
        //image2 = getTestImage(Color.LIGHT_GRAY);
    }

    private Hex[] calculateNeighbors(Hex hex) {
        neighbors = new Hex[6];
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

    public void modifyStartValues(int deltaX, int deltaY) {
        startX = startX + deltaX;
        startY = startY + deltaY;
        if (startX < 0) {
            startX = 0;
        }
        else if (startX > MAX_X - rangeX) {
            startX = MAX_X - rangeX;
        }
        if (startY < 0) {
            startY = 0;
        }
        else if (startY > MAX_Y - rangeY) {
            startY = MAX_Y - rangeY;
        }
    }
    public void generateRandomColorList() {
        System.out.println("Appel generate random color list");
        //colors = new Color[MAX_X][MAX_Y];
        hexes = new Hex[MAX_X][MAX_Y];
        //this.window.truncateHex();
        for (int x = 0; x < MAX_X; x++) {
            for (int y = 0; y < MAX_Y; y++) {
                //colors[x][y] = getRandomColor();
                Hex hex = new Hex(0L, x, y, getRandomTerrainType(), new HashSet<Unit>());
                hexes[x][y] = hex;
                //this.window.saveHexInDB(hex);
            }
        }
    }


    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.black);
        g2d.setStroke(bs1);

        posX = -1;
        posY = -1;
        number = -1;
        for (int x = 0; x < rangeX; x++) {
            for (int y = 0; y < rangeY; y++) {
                if (y % 2 == 0) {
                    getHexagon(x * dimension.width, (int) (y * side * 1.5));
                    if (mousePosition != null && hexagon.contains(mousePosition)) {
                        number = y * rangeX + x + startX + MAX_X * startY;
                        posX = x + startX;
                        posY = y + startY;
                        //System.out.println("posX : " + posX + " : posY : " + posY);
                        focusedHexagonLocation.x = x * dimension.width;
                        focusedHexagonLocation.y = (int) (y * side * 1.5);
                        hex.setPosX(posX);
                        hex.setPosY(posY);
                        hex.setTerrainType(hexes[posX][posY].getTerrainType());
                        if (hexes[posX][posY].getUnits() != null) {
                            hex.setUnits(hexes[posX][posY].getUnits());
                        }
                    }
                }
                else {
                    getHexagon(x * dimension.width + dimension.width / 2, (int) (y * side * 1.5 + 0.5));
                    if (mousePosition != null && hexagon.contains(mousePosition)) {
                        number = y * rangeX + x + startX + MAX_X * startY;
                        posX = x + startX;
                        posY = y + startY;
                        //System.out.println("posX : " + posX + " : posY : " + posY);
                        focusedHexagonLocation.x = x * dimension.width + dimension.width / 2;
                        focusedHexagonLocation.y = (int) (y * side * 1.5 + 0.5);
                        hex.setPosX(posX);
                        hex.setPosY(posY);
                        hex.setTerrainType(hexes[posX][posY].getTerrainType());
                        if (hexes[posX][posY].getUnits() != null) {
                            hex.setUnits(hexes[posX][posY].getUnits());
                        }
                    }
                }

                g2d.setColor(getColorFromTerrain(hexes[x + startX][y + startY].getTerrainType()));
                g2d.fillPolygon(hexagon);
                g2d.setStroke(bs1);
                g2d.setColor(Color.gray);
                g2d.draw(hexagon);

                if(hexes[x + startX][y + startY].getUnits().size() > 0) {
                    imageUnit = getUnitImage(Color.BLUE, getColorFromTerrain(hexes[x + startX][y + startY].getTerrainType()), hexes[x + startX][y + startY].getUnits());
                    g2d.translate(-1*(Math.round(side/7)),0);
                    g2d.drawImage(imageUnit,(int)(hexagon.getBounds().x + side*0.5), (int) (hexagon.getBounds().y + side * 0.5), this);
                    g2d.translate(Math.round(side/7),0);
                }
            }
        }
        if (number != -1) {
            g2d.setColor(Color.red);
            g2d.setStroke(bs3);
            Polygon focusedHexagon = getHexagon(focusedHexagonLocation.x, focusedHexagonLocation.y);
            g2d.draw(focusedHexagon);
            repaint();
        }

        // TODO ajouter si isRightClicked --> dessin de la liste des voisins de hex

        if(isRightClick) {
            for (Hex h : neighbors) {
                if(h != null) {
                    drawHex(g2d, Color.RED, h);
                }

                //repaint();
            }
        }

        if(isSelectedHexExists) {
            drawHex(g2d, Color.BLUE, selectedHex);
            //repaint();
        }

        if(isFrangeExists) {
            treatedHexes.stream().forEach(h -> drawHex(g2d, Color.YELLOW, h));
            frangeHexes.stream().forEach(h -> drawHex(g2d, Color.DARK_GRAY, h));
            drawHex(g2d, Color.RED, start);
            //repaint();
        }

        if(isPathShow) {
            pathToShow.stream().forEach(h -> drawHex(g2d, Color.RED, h));
            //System.out.println("path to show size : " + pathToShow.size());
            //repaint();
        }

        if(isPathFound) {
            foundPaths.stream().findFirst().orElse(null).stream().forEach(h -> drawHex(g2d, Color.DARK_GRAY, h));
        }

        if(isStartPFChoose) {
            drawHex(g2d, new Color(220,100,0), startPF);
        }

        if(isEndPFChoose) {
            drawHex(g2d, new Color(100,0,220), endPF);
        }
        requestFocus();
    }

    private void drawHex(Graphics2D g2d, Color strokeColor, Hex h) {
        g2d.setColor(strokeColor);
        g2d.setStroke(bs3);
        Polygon selectedPolygon;

        int x = h.getPosX() - startX;
        int y = h.getPosY() - startY;
        if(x>=0 && y>=0 && x<rangeX && y<rangeY) {
            if (h.getPosY() % 2 == 0) {
                selectedPolygon = getHexagon(x * dimension.width, (int) (y * side * 1.5));
            }
            else {
                selectedPolygon = getHexagon(x * dimension.width + dimension.width / 2, (int) (y * side * 1.5 + 0.5));
            }
            g2d.draw(selectedPolygon);
            g2d.setStroke(bs1);
            repaint();
        }

    }

    public Polygon getHexagon(final int x, final int y) {
        hexagon.reset();
        int h = side / 2;
        int w = (int) (side * (Math.sqrt(3) / 2));
        hexagon.addPoint(x + gapX, y + h + gapY);
        hexagon.addPoint(x + w + gapX, y + gapY);
        hexagon.addPoint(x + 2 * w + gapX, y + h + gapY);
        hexagon.addPoint(x + 2 * w + gapX, y + (int) (1.5 * side) + gapY);
        hexagon.addPoint(x + w + gapX, y + 2 * side + gapY);
        hexagon.addPoint(x + gapX, y + (int) (1.5 * side) + gapY);
        return hexagon;
    }

    private Image getUnitImage(final Color color1, final Color bgColor, Set<Unit> units) {
        BufferedImage img = new BufferedImage(side, side, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D) img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        g2d.setPaint(bgColor);
        g2d.fillRect(0, 0, side, side);
        //g2d.setColor(color1);
        g2d.setPaint(color1);

        g2d.fillRoundRect(0, 0, side, side, Math.round(side/3), Math.round(side/3)); //Math.round(side/7)

        g2d.setPaint(Color.YELLOW);
        g2d.fillRect(Math.round(side/4), Math.round(side/4), Math.round(side/2), Math.round(side/4));
        g2d.setPaint(Color.RED);
        g2d.drawRect(Math.round(side/4), Math.round(side/4), Math.round(side/2), Math.round(side/4));

        //ArrayList<Unit> unitList = new ArrayList<>(units); // unitList.get(0).getType() == UnitTypeEnum.ART
        if(units.stream().filter(u -> u.getType() == UnitTypeEnum.ART).count() > 0) {
            g2d.setPaint(Color.RED);
            g2d.fillOval(side*2/5, side*3/10, side/6, side/6);
        }
        else if(units.stream().filter(u -> u.getType() == UnitTypeEnum.LEG_INF).count() > 0) {
            g2d.setPaint(Color.RED);
            g2d.drawLine(side/4, side/4, side*3/4, side/2);
            g2d.drawLine(side/4, side/2, side*3/4, side/4);
        }
        else if(units.stream().filter(u -> u.getType() == UnitTypeEnum.TANK).count() > 0) {
            g2d.setPaint(Color.RED);
            g2d.drawArc(side/2, side*4/15, 5, 5, -90, 180);
            g2d.drawArc(9, 8, 5, 5, 90, 180);
        }
        return img;
    }

    private Color getColorFromTerrain(TerrainTypeEnum terrain) {
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

    private TerrainTypeEnum getRandomTerrainType() {
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

    // si frange pas créée (booleen) et isSelectedHex

    // alors init de la frange : ajout de selected hex dans treatedHexes , booleen a false
    // sinon ajout de la frange dans treatedHexes
    // ajout dans la frange des nouveaux hex voisins s'ils ne sont pas dans treatedHexes
    public void generateFrange() {
        //System.out.println("appel generate");
        isPathShow = false;
        if(isSelectedHexExists) {
            if(isFrangeExists) {
                //System.out.println("la frange existe");
                treatedHexes.addAll(frangeHexes);

            }
            else {
                start = selectedHex;
                isFrangeExists = true;
                //System.out.println("la frange n'existe pas");
                Set<Hex> initPath = new HashSet<>();
                initPath.add(selectedHex);
                HexFrange initHex = new HexFrange(selectedHex, initPath);
                treatedHexes.add(initHex);
                frangeHexes.add(initHex);
            }
            Set<HexFrange> tempFrange = new HashSet<HexFrange>();
            frangeHexes.stream().forEach(h -> {
                Hex[] neighbors = calculateNeighbors(h);
                Arrays.stream(neighbors).filter(n -> n != null).filter(n -> !isSetContainsHex(treatedHexes, n)).forEach(n -> {
                    Set<Hex> pathFrange = new HashSet<>(h.getPath());
                    pathFrange.add(n);
                    HexFrange hexFrange = new HexFrange(n, pathFrange);
                    if(isSetContainsHexFrange(tempFrange, hexFrange)) {
                        // récupérer chemin de hex deja dans le set
                        HexFrange oldHexF = tempFrange.stream().filter(hex -> hex.getPosX() == hexFrange.getPosX() && hex.getPosY() == hexFrange.getPosY()).findFirst().orElse(null);

                        Set<Hex> oldPath = oldHexF.getPath();
                        float oldPathWeight = calculatePathWeight(oldPath, selectedHex);
                        //System.out.println("old hex weight : " + oldWeight);
                        // comparer les chemin par appel méthode calculatePathWeight(Set<Hex> path, )
                        float newPathWeight = calculatePathWeight(hexFrange.getPath(), selectedHex);
                        //System.out.println("new hex weight : " + newWeight);
                        if(newPathWeight < oldPathWeight) {
                            tempFrange.remove(oldHexF);
                            tempFrange.add(hexFrange);
                        }
                    }
                    else {
                        if (isTerrainPassable(n)) {
                            tempFrange.add(hexFrange);
                        }
                        else {
                            treatedHexes.add(hexFrange);
                        }
                    }


                });
            });
            frangeHexes = tempFrange;
/*
            // boucle pour ajouter les chemins, utiliser une map <hex, chemin> ?
            frangeHexes.stream().forEach(h -> {
                Hex[] neighbors = calculateNeighbors(h);
                Arrays.stream(neighbors).filter(n -> treatedHexes.contains(n)).forEach(n -> {

                });
            });*/
        }

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
        return h.getTerrainType() != TerrainTypeEnum.WATER;
    }

    private boolean isSetContainsHex(Set<HexFrange> set, Hex hex) {
        return set.stream().anyMatch(hf -> hf.getPosX() == hex.getPosX() && hf.getPosY() == hex.getPosY());
    }

    public void displayPaths() {
        isPathShow = true;
        pathId++;
        if(pathId > frangeHexes.size()) {
            pathId = 1;
        }
        if(isFrangeExists) {
            int cpt = 0;
            //frangeHexes
            for (HexFrange fh : frangeHexes) {
                cpt++;
                if (cpt == pathId) {
                    pathToShow = fh.getPath();
                }
            }
        }
    }

    public void setStartHex() {
        startPF = selectedHex.clone();
        isStartPFChoose = true;
        System.out.println("Start Path Finding : " + startPF.toString());
    }

    public void setEndHex() {
        endPF = selectedHex.clone();
        isEndPFChoose = true;
        System.out.println("End Path Finding : " + endPF.toString());
    }

    public void searchAndDisplayPath() {
        System.out.println("Appel méthode générateur de chemin");
        PathFinder pf = new PathFinder();
        foundPaths = pf.generatePaths(hexes, MAX_X, MAX_Y, startPF, endPF);
        isPathFound = foundPaths != null;
    }

    public void resetPathFindingDatas() {
        startPF = null;
        endPF = null;
        isPathFound = false;
        isStartPFChoose = false;
        isEndPFChoose = false;
    }

    public boolean isSelectedHexPassable() {
        if (isSelectedHexExists) {
            return isTerrainPassable(selectedHex);
        }
        else {
            return false;
        }
    }
}