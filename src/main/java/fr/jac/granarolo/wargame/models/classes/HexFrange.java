package fr.jac.granarolo.wargame.models.classes;

import fr.jac.granarolo.wargame.models.Hex;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class HexFrange extends Hex {
    private Set<Hex> path;

    public HexFrange(Hex hex, Set<Hex> path) {
        super(hex.getId(), hex.getPosX(), hex.getPosY(), hex.getTerrainType(), hex.getUnits());
        this.path = path;
    }

    public HexFrange clone() {
        return new HexFrange(new Hex(this.getId(), this.getPosX(), this.getPosY(), this.getTerrainType(), this.getUnits()), this.path);
    }
}
