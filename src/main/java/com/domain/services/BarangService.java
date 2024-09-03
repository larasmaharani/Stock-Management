package com.domain.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.domain.models.entity.Barang;
import com.domain.models.repos.BarangRepo;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BarangService {

    @Autowired
    private BarangRepo barangRepo;

    public Barang save(Barang barang) throws Exception {
        validateBarang(barang);
        return barangRepo.save(barang);
    }

    // UPDATE
    public Barang update(Long id, Barang updatedBarang) throws Exception {
        Optional<Barang> existingBarang = barangRepo.findById(id);
        if (existingBarang.isPresent()) {
            Barang barangToUpdate = existingBarang.get();

            // Validasi barang sebelum update
            validateBarang(updatedBarang);

            // Copy the updated details into the existing entity
            barangToUpdate.setNama(updatedBarang.getNama());
            barangToUpdate.setJumlah(updatedBarang.getJumlah());
            barangToUpdate.setNomorSeri(updatedBarang.getNomorSeri());

            return barangRepo.save(barangToUpdate);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Barang dengan ID " + id + " tidak ditemukan.");
        }
    }
    
    // method validateBarang
    private void validateBarang(Barang barang) throws Exception {
        // Validasi nama barang
        if (barang.getNama() == null || barang.getNama().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nama barang tidak boleh kosong.");
        }

        // Validasi nomor seri
        if (barang.getNomorSeri() == null || barang.getNomorSeri().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nomor seri tidak boleh kosong.");
        }

        // Validasi tipe data untuk jumlah (harus integer dan lebih dari 0)
        if (barang.getJumlah() == null || barang.getJumlah() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Jumlah tidak boleh kosong dan harus berupa angka yang lebih besar dari 0.");
        }

        // Validasi nama barang (hanya huruf dan spasi)
        // ^ -> awal string // $ -> akhir dr string
        if (!barang.getNama().matches("^[a-zA-Z ]+$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Nama barang harus berupa teks dan tidak boleh mengandung angka atau karakter khusus.");
        }

        // Validasi nomor seri (hanya huruf dan angka)
        // ^ -> awal string // + -> mengizinkan 1 atau lebih dari karakter// $ -> akhir
        // dr string
        if (!barang.getNomorSeri().matches("^[a-zA-Z0-9]+$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Nomor seri harus berupa teks dan tidak boleh mengandung karakter khusus.");
        }

        // Cek apakah nama barang sudah ada
        Optional<Barang> existingNama = barangRepo.findByNama(barang.getNama());
        if (existingNama.isPresent() && existingNama.get().getId() != barang.getId()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nama barang sudah digunakan.");
        }

        // Cek apakah nomor seri sudah ada
        Optional<Barang> existingNomorSeri = barangRepo.findByNomorSeri(barang.getNomorSeri());
        if (existingNomorSeri.isPresent() && existingNomorSeri.get().getId() != barang.getId()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nomor seri sudah digunakan.");
        }
    }

    // public Optional<Barang> findOne(Long id) {
    // return barangRepo.findById(id);
    // }

    public Optional<Barang> findOne(Long id) {
        Optional<Barang> barang = barangRepo.findById(id);
        if (barang.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Barang dengan ID " + id + " tidak ditemukan.");
        }
        return barang;
    }

    public Iterable<Barang> findAll() {
        return barangRepo.findAll();
    }

    // public void removeOne(Long id) {
    // barangRepo.deleteById(id);
    // }

    public void removeOne(Long id) {
        Optional<Barang> barang = barangRepo.findById(id);
        if (barang.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Barang dengan ID " + id + " tidak ditemukan.");
        }
        barangRepo.deleteById(id);
    }

    public List<Barang> findByName(String nama) {
        return barangRepo.findByNamaContains(nama);
    }
}
