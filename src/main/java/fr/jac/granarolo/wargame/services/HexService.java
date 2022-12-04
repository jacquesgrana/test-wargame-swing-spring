package fr.jac.granarolo.wargame.services;

import fr.jac.granarolo.wargame.models.Hex;
import fr.jac.granarolo.wargame.models.Unit;
import fr.jac.granarolo.wargame.repositories.HexRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HexService {

    @Autowired
    private HexRepository hexRepository;


    public void save(Hex hex) {
        hexRepository.save(hex);
    }


    public void saveAll(Set<Hex> list) {
        hexRepository.saveAll(list);
    }

    public Set<Hex> findAll() {
        return new HashSet<Hex>(hexRepository.findAll());
    }

    public void truncateTable() {
        hexRepository.truncateHex();
    }

}
