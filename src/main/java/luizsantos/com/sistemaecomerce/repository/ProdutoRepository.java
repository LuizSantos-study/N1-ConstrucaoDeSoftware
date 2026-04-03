package luizsantos.com.sistemaecomerce.repository;

import luizsantos.com.sistemaecomerce.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
