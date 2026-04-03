package luizsantos.com.sistemaecomerce.repository;

import luizsantos.com.sistemaecomerce.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
}
