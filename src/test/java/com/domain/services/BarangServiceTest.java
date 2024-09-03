package com.domain.services;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

// import static org.mockito.Mockito.doThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.domain.models.entity.Barang;
import com.domain.models.repos.BarangRepo;

public class BarangServiceTest {

    // @InjectMocks -> menandai BarangService agar Mockito memasukkan mock
    // BarangRepo ke dalamnya
    @InjectMocks
    private BarangService barangService;

    // @Mock -> Menandai BarangRepo sebagai mock sehingga dapat diprogram untuk
    // perilaku tertentu
    @Mock
    private BarangRepo barangRepo;

    // @BeforeEach -> metode setUp dijalankan sebelum setiap metode pengujian untuk
    // menginisialisasi objek mock
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // TEST SCENARIO

    // TC01 - Pengujian untuk findAll
    @Test
    void testFindAllBarang() {
        Barang barang1 = new Barang(1L, "Barang A", 10, "123ABC");
        Barang barang2 = new Barang(2L, "Barang B", 20, "456DEF");

        when(barangRepo.findAll()).thenReturn(Arrays.asList(barang1, barang2));

        Iterable<Barang> result = barangService.findAll();

        // convert Iterable to List
        List<Barang> resultList = StreamSupport.stream(result.spliterator(), false)
                .collect(Collectors.toList());

        assertEquals(2, resultList.size());
        assertEquals("Barang A", resultList.get(0).getNama());
        assertEquals("Barang B", resultList.get(1).getNama());
    }

    // TC02 - Pengujian untuk findById
    @Test
    void testFindByIdBarang() {
        Barang barang = new Barang(1L, "Barang A", 10, "123ABC");
        when(barangRepo.findById(1L)).thenReturn(Optional.of(barang));

        Optional<Barang> result = barangService.findOne(1L);

        assertTrue(result.isPresent());
        assertEquals("Barang A", result.get().getNama());
        assertEquals("123ABC", result.get().getNomorSeri());
    }

    // TC03 - Pengujian untuk findById yang tidak ditemukan
    @Test
    void testFindByIdBarangNotFound() {
        when(barangRepo.findById(2L)).thenReturn(Optional.empty());

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            barangService.findOne(2L);
        });

        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatusCode());
        assertEquals("Barang dengan ID 2 tidak ditemukan.", thrown.getReason());
    }

    // TC04 - Pengujian nama barang kosong
    @Test
    void testCreateBarangWithEmptyNama() {
        Barang barang = new Barang(1L, "", 10, "123ABC");

        // assertThrows -> memastikan bahwa ResponseStatusException dilemparkan saat
        // barangService.save(barang) dipanggil
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            barangService.save(barang);
        });

        // assertEquals -> memverifikasi bahwa status kode dan pesan pengecualian sesuai
        // dengan yang diharapkan
        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
        assertEquals("Nama barang tidak boleh kosong.", thrown.getReason());
    }

    // TC05 - Pengujian nomor seri kosong
    @Test
    void testCreateBarangWithEmptyNomorSeri() {
        Barang barang = new Barang(1L, "Barang A", 10, "");

        // assertThrows -> memastikan bahwa ResponseStatusException dilemparkan saat
        // barangService.save(barang) dipanggil
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            barangService.save(barang);
        });

        // assertEquals -> memverifikasi bahwa status kode dan pesan pengecualian sesuai
        // dengan yang diharapkan
        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
        assertEquals("Nomor seri tidak boleh kosong.", thrown.getReason());
    }

    // TC06 - Pengujian nama barang mengandung karakter yang tidak diperbolehkan
    @Test
    void testCreateBarangWithInvalidNama() {
        Barang barang = new Barang(1L, "Barang @A", 10, "123ABC");

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            barangService.save(barang);
        });

        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
        assertEquals("Nama barang harus berupa teks dan tidak boleh mengandung angka atau karakter khusus.",
                thrown.getReason());
    }

    // TC07 - Pengujian nomor seri mengandung karakter yang tidak diperbolehkan
    @Test
    void testCreateBarangWithInvalidNomorSeri() {
        Barang barang = new Barang(1L, "Barang A", 10, "@123ABC");

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            barangService.save(barang);
        });

        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
        assertEquals("Nomor seri harus berupa teks dan tidak boleh mengandung karakter khusus.", thrown.getReason());
    }

    // TC08 - Pengujian barang berhasil ditambahkan
    @Test
    void testCreateBarangSuccess() throws Exception {
        Barang barang = new Barang(1L, "Barang A", 10, "123ABC");

        // mengatur mock untuk mengembalikan barang ketika save dipanggil
        when(barangRepo.save(barang)).thenReturn(barang);

        Barang savedBarang = barangService.save(barang);

        // menyimpan barang sesuai dengan yg diharapkan
        assertEquals(barang.getNama(), savedBarang.getNama());
    }

    // TC09 - Pengujian jika nama barang sudah digunakan atau sudah ada
    @Test
    void testCreateBarangWithExistingNama() throws Exception {
        Barang barang = new Barang(1L, "Barang A", 10, "123ABC");

        when(barangRepo.findByNama(barang.getNama())).thenReturn(Optional.of(new Barang()));

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            barangService.save(barang);
        });

        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
        assertEquals("Nama barang sudah digunakan.", thrown.getReason());
    }

    // TC10 - Pengujian jika nomor seri sudah digunakan atau sudah ada
    @Test
    void testCreateBarangWithExistingNomorSeri() throws Exception {
        Barang barang = new Barang(1L, "Barang B", 10, "123ABC");

        // Simulasi barang dengan nomor seri yang sudah ada
        when(barangRepo.findByNomorSeri(barang.getNomorSeri())).thenReturn(Optional.of(new Barang()));

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            barangService.save(barang);
        });

        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
        assertEquals("Nomor seri sudah digunakan.", thrown.getReason());
    }

    // TC11 - Pengujian untuk update
    @Test
    void testUpdateBarang() throws Exception {
        Barang existingBarang = new Barang(1L, "Barang A", 10, "123ABC");
        Barang updatedBarang = new Barang(1L, "Barang A Updated", 15, "123XYZ");

        when(barangRepo.findById(1L)).thenReturn(Optional.of(existingBarang));
        when(barangRepo.save(existingBarang)).thenReturn(updatedBarang);

        Barang result = barangService.update(1L, updatedBarang);

        assertEquals("Barang A Updated", result.getNama());
        assertEquals("123XYZ", result.getNomorSeri());
    }

    // TC12 - Pengujian untuk update barang yang tidak ditemukan
    @Test
    void testUpdateBarangNotFound() throws Exception {
        Barang updatedBarang = new Barang(1L, "Barang Updated", 15, "123XYZ");
        when(barangRepo.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            barangService.update(1L, updatedBarang);
        });

        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatusCode());
        assertEquals("Barang dengan ID 1 tidak ditemukan.", thrown.getReason());
    }

    // TC13 - Pengujian untuk delete barang
    @Test
    void testDeleteBarang() throws Exception {
        Barang barang = new Barang(1L, "Barang A", 10, "123ABC");

        when(barangRepo.findById(1L)).thenReturn(Optional.of(barang));
        doNothing().when(barangRepo).deleteById(1L);

        barangService.removeOne(1L);
    }

    // TC14 - Pengujian untuk delete barang yang tidak ditemukan
    @Test
    void testDeleteBarangNotFound() throws Exception {
        when(barangRepo.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            barangService.removeOne(1L);
        });

        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatusCode());
        assertEquals("Barang dengan ID 1 tidak ditemukan.", thrown.getReason());
    }

}
