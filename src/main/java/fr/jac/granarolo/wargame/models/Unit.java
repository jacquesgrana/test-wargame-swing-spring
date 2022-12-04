package fr.jac.granarolo.wargame.models;

import fr.jac.granarolo.wargame.models.enums.CampEnum;
import fr.jac.granarolo.wargame.models.enums.UnitTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// TODO ajouter vitesse de déplacement et portée de tir
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Entity(name="Unit")
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name="name", length=50, nullable=false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name="camp", length=50, nullable=false)
    private CampEnum camp;

    @Enumerated(EnumType.STRING)
    @Column(name="type", length=50, nullable=false)
    private UnitTypeEnum type;

    @Column(name="soldier_number", nullable=false)
    private int soldierNumber;

    @Column(name="soldier_number_init", nullable=false)
    private int soldierNumberInitial;

    @Column(name="vehicle_number", nullable=false)
    private int vehicleNumber;

    @Column(name="vehicle_number_init", nullable=false)
    private int vehicleNumberInitial;

    @Column(name="organisation", nullable=false)
    private float organisation;

    @Column(name="moral", nullable=false)
    private float moral;

    @Column(name="carburant_number", nullable=false)
    private float carburantNumber;

    @Column(name="ammunition_number", nullable=false)
    private float ammunitionNumber;

    @Column(name="soft_attack", nullable=false)
    private float softAttack;

    @Column(name="soft_defense", nullable=false)
    private float softDefense;

    @Column(name="hard_attack", nullable=false)
    private float hardAttack;

    @Column(name="hard_defense", nullable=false)
    private float hardDefense;

    @Column(name="is_routed", nullable=false)
    private boolean isRouted;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="hex_id", referencedColumnName = "id", nullable=true)
    private Hex hex;

    // effectif, effecif vehicule, organisation, fatigue?, moral, nbcarburant, nbmunition, softAttack, hardAttack, softDefense, hardDefense
    // isRouted

}
