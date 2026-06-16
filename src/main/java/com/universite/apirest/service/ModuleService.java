package com.universite.apirest.service;

import com.universite.apirest.entity.Module;
import com.universite.apirest.repository.ModuleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ModuleService {

    private final ModuleRepository moduleRepository;

    public ModuleService(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    public List<Module> getAllModules() {
        return moduleRepository.findAll();
    }

    public Optional<Module> getModuleById(Long id) {
        return moduleRepository.findById(id);
    }

    public Optional<Module> getModuleByCode(String code) {
        return moduleRepository.findByCode(code);
    }

    public Module createModule(Module module) {
        if (moduleRepository.existsByCode(module.getCode())) {
            throw new IllegalArgumentException("Un module avec ce code existe déjà");
        }
        return moduleRepository.save(module);
    }

    public Module updateModule(Long id, Module moduleDetails) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Module non trouvé avec l'ID: " + id));

        module.setCode(moduleDetails.getCode());
        module.setNom(moduleDetails.getNom());
        module.setDescription(moduleDetails.getDescription());
        module.setCredit(moduleDetails.getCredit());
        module.setVolumeHoraire(moduleDetails.getVolumeHoraire());

        return moduleRepository.save(module);
    }

    public void deleteModule(Long id) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Module non trouvé avec l'ID: " + id));
        moduleRepository.delete(module);
    }
}
