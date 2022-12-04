package fr.jac.granarolo.wargame.models;

import fr.jac.granarolo.wargame.models.enums.TerrainTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Entity(name="Hex")
public class Hex {

    //private int posX;
    //private int posY;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name="pos_x", nullable=false)
    private int posX;

    @Column(name="pos_y", nullable=false)
    private int posY;
    @Enumerated(EnumType.STRING)
    @Column(name="terrainType", length=50, nullable=false)
    private TerrainTypeEnum terrainType;

    @OneToMany(mappedBy="hex", fetch=FetchType.EAGER)
    @Column(nullable=true)
    private Set<Unit> units = new HashSet<Unit>();

    public Hex clone() {
        return new Hex(this.id, this.posX, this.posY, this.terrainType, this.units);
    }

}
