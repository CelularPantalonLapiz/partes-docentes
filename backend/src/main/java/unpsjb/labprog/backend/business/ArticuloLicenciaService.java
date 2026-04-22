package unpsjb.labprog.backend.business;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import unpsjb.labprog.backend.model.ArticuloLicencia;

@Service
public class ArticuloLicenciaService {
    @Autowired
    private ArticuloLicenciaRepository repository;

    public List<ArticuloLicencia> findAll() {
        List<ArticuloLicencia> result = new ArrayList<>();
        repository.findAll().forEach(result::add);
        return result;
    }

    @Transactional
    public ArticuloLicencia save(ArticuloLicencia articulo) {
        return repository.save(articulo);
    }

    @Transactional
    public void delete(String id) {
        repository.deleteById(id);
    }

    public ArticuloLicencia findById(String id) {
        return repository.findById(id).orElse(null);
    }

    public Page<ArticuloLicencia> findByPage(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    public List<ArticuloLicencia> search(String term) {
        return repository.search("%" + term.toUpperCase() + "%");
    }
}
