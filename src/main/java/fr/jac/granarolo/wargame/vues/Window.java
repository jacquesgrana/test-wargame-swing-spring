package fr.jac.granarolo.wargame.vues;

import fr.jac.granarolo.wargame.classes.SpringContext;
import fr.jac.granarolo.wargame.models.Hex;
import fr.jac.granarolo.wargame.models.Unit;
import fr.jac.granarolo.wargame.models.enums.CampEnum;
import fr.jac.granarolo.wargame.models.enums.TerrainTypeEnum;
import fr.jac.granarolo.wargame.models.enums.UnitTypeEnum;
import fr.jac.granarolo.wargame.services.HexService;
import fr.jac.granarolo.wargame.services.UnitService;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationContext;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.swing.*;
//@Configurable
public class Window extends JFrame {

    private static final long serialVersionUID = 1L;

    //@Autowired
    //public UnitService unitService; // TODO verifier visiblité
    //public WindowManager windowManager;

    ApplicationContext context = SpringContext.getApplicationContext();
    UnitService unitService = (UnitService)context.getBean("unitService");

    HexService hexService = (HexService)context.getBean("hexService");
    private JButton button1 = new JButton("Générer frange");
    private JButton button2 = new JButton("Afficher chemin");
    private Container container = new Container();
    private JPanel panelBoard = new JPanel();
    private JLabel labelInfos = new JLabel("Infos : ");

    private GridHexagons board = new GridHexagons(15, 15, 30, this);
    //private JTextField text = new JTextField("Texte");
    //private JComboBox select = new JComboBox();
    private BoxLayout layout;

    public Window() {
        this.setTitle("Fenêtre");
        this.setSize(1000, 1000);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        container = getContentPane();
        layout = new BoxLayout(container, BoxLayout.Y_AXIS);
        container.setLayout(layout);
        panelBoard.setLayout(new CardLayout(100,100));
        board.setBorder(BorderFactory.createLineBorder(Color.black));
        board.setBackground(Color.WHITE);
        panelBoard.add(board);
        container.add(panelBoard);
        container.add(button1, BorderLayout.SOUTH);
        container.add(button2, BorderLayout.SOUTH);
        container.add(labelInfos, BorderLayout.SOUTH);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                //layout.next(container);
                clicButton1();
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                //layout.next(container);
                clicButton2();
            }
        });


        initDatas(false);
    }

    private void clicButton1() {
        System.out.println("Clic bouton");
        //board.generateRandomColorList();

        /*
        // TODO charger les listes depuis la bdd --> TEST a enlever
        Set<Hex> setToAdd = new HashSet<Hex>();
        setToAdd = hexService.findAll();
        setToAdd.stream().forEach(h -> {board.hexes[h.getPosX()][h.getPosY()] = h;});

        board.units = new HashSet<Unit>();
        board.units = unitService.findAll();
        getContentPane().repaint();
        board.setFocusable(true);
        board.requestFocus();
        */
        board.generateFrange();
    }

    private void clicButton2() {
        board.displayPaths();
    }

    public void displayInfos(String text) {
        labelInfos.setText("Infos : " + text);
    }

    @Transactional
    private void initDatas(boolean isSaveToDb) {

        Hex hexInit = new Hex();
        hexInit.setPosX(0);
        hexInit.setPosY(0);
        hexInit.setTerrainType(TerrainTypeEnum.GRASS);
        hexInit.setUnits(new HashSet<>());

        Unit unitToAdd1 = new Unit();
        unitToAdd1.setName("Unité de test n°1");
        unitToAdd1.setCamp(CampEnum.COUNTRY_1);
        unitToAdd1.setType(UnitTypeEnum.LEG_INF);
        unitToAdd1.setSoldierNumber(1000);
        unitToAdd1.setSoldierNumberInitial(1000);
        unitToAdd1.setVehicleNumber(10);
        unitToAdd1.setVehicleNumberInitial(10);
        unitToAdd1.setOrganisation(100);
        unitToAdd1.setMoral(100);
        unitToAdd1.setCarburantNumber(4F);
        unitToAdd1.setAmmunitionNumber(6F);
        unitToAdd1.setSoftAttack(10F);
        unitToAdd1.setSoftDefense(8F);
        unitToAdd1.setHardAttack(0.5F);
        unitToAdd1.setHardDefense(2F);
        unitToAdd1.setRouted(false);

        unitToAdd1.setHex(null);
        //unitService.save(unitToAdd1);

        Unit unitToAdd2 = new Unit();
        unitToAdd2.setName("Unité de test n°2");
        unitToAdd2.setCamp(CampEnum.COUNTRY_2);
        unitToAdd2.setType(UnitTypeEnum.ART);
        unitToAdd2.setSoldierNumber(400);
        unitToAdd2.setSoldierNumberInitial(400);
        unitToAdd2.setVehicleNumber(20);
        unitToAdd2.setVehicleNumberInitial(20);
        unitToAdd2.setOrganisation(100);
        unitToAdd2.setMoral(100);
        unitToAdd2.setCarburantNumber(4F);
        unitToAdd2.setAmmunitionNumber(6F);
        unitToAdd2.setSoftAttack(6F);
        unitToAdd2.setSoftDefense(1F);
        unitToAdd2.setHardAttack(0.5F);
        unitToAdd2.setHardDefense(1F);
        unitToAdd2.setRouted(false);

        unitToAdd2.setHex(null);
        //unitService.save(unitToAdd2);

        Unit unitToAdd3 = new Unit();
        unitToAdd3.setName("Unité de test n°3");
        unitToAdd3.setCamp(CampEnum.COUNTRY_1);
        unitToAdd3.setType(UnitTypeEnum.TANK);
        unitToAdd3.setSoldierNumber(400);
        unitToAdd3.setSoldierNumberInitial(400);
        unitToAdd3.setVehicleNumber(50);
        unitToAdd3.setVehicleNumberInitial(50);
        unitToAdd3.setOrganisation(100);
        unitToAdd3.setMoral(100);
        unitToAdd3.setCarburantNumber(6F);
        unitToAdd3.setAmmunitionNumber(4F);
        unitToAdd3.setSoftAttack(12F);
        unitToAdd3.setSoftDefense(10F);
        unitToAdd3.setHardAttack(8F);
        unitToAdd3.setHardDefense(8F);
        unitToAdd3.setRouted(false);

        unitToAdd3.setHex(null);
        //unitService.save(unitToAdd3);

        Unit unitToAdd4 = new Unit();
        unitToAdd4.setName("Unité de test n°4");
        unitToAdd4.setCamp(CampEnum.COUNTRY_2);
        unitToAdd4.setType(UnitTypeEnum.MOT_INF);
        unitToAdd4.setSoldierNumber(1000);
        unitToAdd4.setSoldierNumberInitial(1000);
        unitToAdd4.setVehicleNumber(60);
        unitToAdd4.setVehicleNumberInitial(60);
        unitToAdd4.setOrganisation(100);
        unitToAdd4.setMoral(100);
        unitToAdd4.setCarburantNumber(6F);
        unitToAdd4.setAmmunitionNumber(6F);
        unitToAdd4.setSoftAttack(8F);
        unitToAdd4.setSoftDefense(6F);
        unitToAdd4.setHardAttack(0.5F);
        unitToAdd4.setHardDefense(2F);
        unitToAdd4.setRouted(false);

        unitToAdd4.setHex(null);
        //unitService.save(unitToAdd4);

        //hexService.truncateTable();
/*
        Hex hexToAdd1 = new Hex();
        hexToAdd1.setPosX(0);
        hexToAdd1.setPosY(0);
        hexToAdd1.setTerrainType(TerrainTypeEnum.GRASS);
        hexToAdd1.setUnits(new HashSet<>());
        hexService.save(hexToAdd1);
*/
        board.hexes[105][105].getUnits().add(unitToAdd1);
        board.hexes[106][108].getUnits().add(unitToAdd2);
        board.hexes[106][109].getUnits().add(unitToAdd3);
        board.hexes[106][108].getUnits().add(unitToAdd4);

        Set<Hex> listToSave = new HashSet<>();
        Arrays.stream(board.hexes).sequential().forEach(hexes -> {
            Arrays.stream(hexes).sequential().forEach(hex -> {
                listToSave.add(hex);
            });
        });

        if(isSaveToDb) {
            hexService.saveAll(listToSave);
        }


        Set<Hex> listToSearchIn = hexService.findAll();
        Hex hex1 = listToSearchIn.stream().filter(h -> h.getPosX() == 105 && h.getPosY() == 105).findFirst().orElse(null);
        Hex hex2 = listToSearchIn.stream().filter(h -> h.getPosX() == 106 && h.getPosY() == 108).findFirst().orElse(null);
        Hex hex3 = listToSearchIn.stream().filter(h -> h.getPosX() == 106 && h.getPosY() == 109).findFirst().orElse(null);

        /*
        unitToAdd1.setHex(board.hexes[105][105]);
        unitToAdd2.setHex(board.hexes[106][108]);
        unitToAdd3.setHex(board.hexes[106][109]);
        unitToAdd4.setHex(board.hexes[106][108]);
        */
        unitToAdd1.setHex(hex1);
        unitToAdd2.setHex(hex2);
        unitToAdd3.setHex(hex3);
        unitToAdd4.setHex(hex2);

        board.units.add(unitToAdd1);
        board.units.add(unitToAdd2);
        board.units.add(unitToAdd3);
        board.units.add(unitToAdd4);

        if(isSaveToDb) {
            unitService.saveAll(board.units);
        }
    }

}