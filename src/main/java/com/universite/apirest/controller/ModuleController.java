package com.universite.apirest.controller;

import com.universite.apirest.entity.Module;
import com.universite.apirest.service.ModuleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/modules")
public class ModuleController {

    private final ModuleService moduleService;

    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @GetMapping
    public ResponseEntity<List<Module>> getAllModules() {
        List<Module> modules = moduleService.getAllModules();
        return ResponseEntity.ok(modules);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getModuleById(@PathVariable Long id) {
        Optional<Module> module = moduleService.getModuleById(id);
        if (module.isPresent()) {
            return ResponseEntity.ok(module.get());
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Module non trouvé");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<?> getModuleByCode(@PathVariable String code) {
        Optional<Module> module = moduleService.getModuleByCode(code);
        if (module.isPresent()) {
            return ResponseEntity.ok(module.get());
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Module non trouvé");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<?> createModule(@Valid @RequestBody Module module) {
        try {
            Module createdModule = moduleService.createModule(module);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Module créé avec succès");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateModule(@PathVariable Long id, @Valid @RequestBody Module module) {
        try {
            Module updatedModule = moduleService.updateModule(id, module);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Module modifié");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteModule(@PathVariable Long id) {
        try {
            moduleService.deleteModule(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Module supprimé");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
