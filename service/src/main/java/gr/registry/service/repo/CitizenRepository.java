package gr.registry.service.repo;

import gr.registry.domain.Citizen;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CitizenRepository extends JpaRepository<Citizen, Long> {
    Optional<Citizen> findByAt(String at);
    boolean existsByAt(String at);
    void deleteByAt(String at);
	List<Citizen> findAll(Specification<Citizen> spec);
}
