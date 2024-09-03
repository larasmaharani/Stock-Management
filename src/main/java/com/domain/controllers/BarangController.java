package com.domain.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.domain.models.entity.Barang;
import com.domain.services.BarangService;

// anotasi
@RestController
// memberi nama endpoint/api
@RequestMapping("/api/barang") // localhost:8080

public class BarangController {

    // controller>service>repo

    @Autowired
    private BarangService barangService;

    // CREATE DATA
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Barang barang) {
        try {
            Barang savedBarang = barangService.save(barang);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBarang);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // @PutMapping
    // public ResponseEntity<?> update(@Valid @RequestBody Barang barang) {
    // Optional<Barang> existingBarang = barangService.findOne(barang.getId());

    // if (existingBarang.isPresent()) {
    // try {
    // Barang updateBarang = barangService.save(barang);
    // return ResponseEntity.ok(updateBarang);
    // } catch (Exception e) {
    // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    // }
    // } else {
    // return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    // }
    // }

    // UPDATE DATA
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Barang barang) {
        try {
            Barang updatedBarang = barangService.update(id, barang);
            return ResponseEntity.ok(updatedBarang);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // FIND ALL
    @GetMapping
    public Iterable<Barang> findAll() {
        return barangService.findAll();
    }

    // FIND BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> findOne(@PathVariable Long id) {
        try {
            Barang barang = barangService.findOne(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Barang dengan ID " + id + " tidak ditemukan."));
            return ResponseEntity.ok(barang);
        } catch (ResponseStatusException e) {
            // Mengembalikan respons dengan status NOT_FOUND dan pesan kesalahan dalam body
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    // DELETE BY ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeOne(@PathVariable Long id) {
        Optional<Barang> existingBarang = barangService.findOne(id);

        if (existingBarang.isPresent()) {
            barangService.removeOne(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
