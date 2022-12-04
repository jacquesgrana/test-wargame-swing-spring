package fr.jac.granarolo.wargame.services;

import fr.jac.granarolo.wargame.models.Hex;
import fr.jac.granarolo.wargame.models.Unit;
import fr.jac.granarolo.wargame.repositories.UnitRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UnitService {

    @Autowired
    private UnitRepository unitRepository;

    public void save(Unit unit) {
        unitRepository.save(unit);
    }

    public void saveAll(Set<Unit> listToSave) {
        unitRepository.saveAll(listToSave);
    }

    public Set<Unit> findAll() {
        return new HashSet<Unit>(unitRepository.findAll());
    }
}
