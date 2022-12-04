package fr.jac.granarolo.wargame.vues;

import fr.jac.granarolo.wargame.models.Hex;
import fr.jac.granarolo.wargame.models.Unit;
import fr.jac.granarolo.wargame.models.enums.CampEnum;
import fr.jac.granarolo.wargame.models.enums.TerrainTypeEnum;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

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

    private Hex hex = new Hex(0L, 0, 0, TerrainTypeEnum.GRASS, null);

    private Hex selectedHex = new Hex(0L, 0, 0, TerrainTypeEnum.GRASS, null);
    ;

    private boolean isSelectedHexExists = false;

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
                            textToDisplay += " : " + unit.getName();
                        }
                        //hex.getUnits().stream().forEach(u -> {textToDisplay += " : " + u.getName();});
                    }
                    window.displayInfos(textToDisplay);
                    selectedHex = hex.clone();
                    isSelectedHexExists = true;
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
                } else {

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
                g2d.setColor(Color.black);
                g2d.draw(hexagon);

                if(hexes[x + startX][y + startY].getUnits().size() > 0) {
                    imageUnit = getUnitImage(Color.BLUE, hexes[x + startX][y + startY].getUnits());
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
        }

        if(isSelectedHexExists) {
            g2d.setColor(Color.blue);
            g2d.setStroke(bs3);
            Polygon selectedPolygon;

            int x = selectedHex.getPosX() - startX;
            int y = selectedHex.getPosY() - startY;
            if (selectedHex.getPosY() % 2 == 0) {
                selectedPolygon = getHexagon(x * dimension.width, (int) (y * side * 1.5));
            }
            else {
                selectedPolygon = getHexagon(x * dimension.width + dimension.width / 2, (int) (y * side * 1.5 + 0.5));
            }
            g2d.draw(selectedPolygon);
            g2d.setStroke(bs1);
        }

        /*
        number = -1;
        for (int y = 0; y < rangeY; y += 2) {
            for (int x = 0; x < rangeX; x++) {
                getHexagon(x * dimension.width, (int) (y * side * 1.5));
                if (mousePosition != null && hexagon.contains(mousePosition)) {
                    focusedHexagonLocation.x = x * dimension.width;
                    focusedHexagonLocation.y = (int) (y * side * 1.5);
                    number = y * rangeX + x + startX + MAX_X*startY;
                    //number = row + column * rows + startX + MAX_X*startY;
                }

                //g2d.setColor(getRandomColor());

               // int nb = y * rangeX + x + startX + MAX_X*startY; // *******************************************
                //int nb = row + column * rows + startX + MAX_X*startY;
                g2d.setColor(colors[x + startX][y + startY]);
                //System.out.println("Number : " + (row * columns + column));
                g2d.fillPolygon(hexagon);
                g2d.setColor(Color.black);
                g2d.draw(hexagon);
                //g2d.drawImage(image1,(int)(hexagon.getBounds().x + side*0.5), (int) (hexagon.getBounds().y + side * 0.5), this);
            }
        }
        for (int y = 1; y < rangeY; y += 2) {
            for (int x = 0; x < rangeX; x++) {
                getHexagon(x * dimension.width + dimension.width / 2, (int) (y * side * 1.5 + 0.5));
                if (mousePosition != null && hexagon.contains(mousePosition)) {
                    focusedHexagonLocation.x = x * dimension.width + dimension.width / 2;
                    focusedHexagonLocation.y =(int) (y * side * 1.5 + 0.5);
                    number = y * rangeX + x + startX + MAX_X*startY;
                    //number = row + column * rows + startX + MAX_X*startY;
                }

                //g2d.setColor(getRandomColor());
                // int nb = row * columns + column + startX + MAX_X*startY;
                g2d.setColor(colors[x + startX][y + startY]);
                //System.out.println("Number : " + (row * columns + column));
                g2d.fillPolygon(hexagon);
                g2d.setColor(Color.black);
                g2d.draw(hexagon);
                //g2d.drawImage(image2,(int)(hexagon.getBounds().x + side*0.5), (int) (hexagon.getBounds().y + side * 0.5), this);
            }
        }
        if (number != -1) {
            g2d.setColor(Color.red);
            g2d.setStroke(bs3);
            Polygon focusedHexagon = getHexagon(focusedHexagonLocation.x, focusedHexagonLocation.y);
            g2d.draw(focusedHexagon);
        }
        */
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

    private Image getUnitImage(final Color color1, Set<Unit> units) {
        BufferedImage img = new BufferedImage(side, side, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D) img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        g2d.setColor(color1);
        //g2d.setPaint(color1);
        g2d.fillRoundRect(0, 0, side, side, 10, 10); //Math.round(side/7)
        /*
        g2d.setColor(getBackground());
        g2d.fillRect(0, 0, side, side);
        g2d.setColor(color1);
        g2d.fillOval(0, 0, side - 9, side - 3);
        g2d.drawOval(0, 0, side - 9, side - 3);
        g2d.setColor(Color.WHITE);
        g2d.fillOval(4, 9, 5, 7);
        g2d.setColor(color1);
        g2d.drawOval(4, 9, 5, 7);
        g2d.fillOval(6, 12, 3, 3);
        g2d.setColor(Color.WHITE);
        g2d.fillOval(14, 9, 5, 7);
        g2d.setColor(color1);
        g2d.drawOval(14, 9, 5, 7);
        g2d.fillOval(16, 12, 3, 3);
        g2d.setColor(Color.RED);
        g2d.fillOval(8, 20, 6, 3);
        g2d.setColor(color1);
        g2d.drawOval(8, 20, 6, 3);
        */

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
}