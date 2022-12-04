package fr.jac.granarolo.wargame.repositories;

import fr.jac.granarolo.wargame.models.Hex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HexRepository extends JpaRepository<Hex, Long> {

    @Modifying
    @Query(value = "TRUNCATE TABLE Hex", nativeQuery = true)
    void truncateHex();
}
