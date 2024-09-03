package com.domain.models.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.domain.models.entity.Barang;


// antarmuka (interface) untuk entitas Barang
// antarmuka ini memperluas CrudRepository, yang merupakan antarmuka dari Spring Data 
// menyediakan operasi dasar CRUD (Create, Read, Update, Delete) untuk entita
public interface BarangRepo extends CrudRepository<Barang, Long> {

    Optional<Barang> findByNama(String nama);

    Optional<Barang> findByNomorSeri(String nomorSeri);

    // mencari barang berdasarkan nama mengandung txt tertentu
    List<Barang> findByNamaContains(String nama);
}
