package com.domain.models.entity;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "tbl_barang")
public class Barang implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "Nama tidak boleh null.")
    @NotEmpty(message = "Nama tidak boleh kosong.")
    @Column(name = "nama")
    private String nama;

    @NotNull(message = "Jumlah tidak boleh null.")
    @Min(value = 1, message = "Jumlah harus lebih besar dari 0.")
    @Column(name = "jumlah")
    private Integer jumlah;

    @NotNull(message = "Nomor seri tidak boleh null.")
    @NotEmpty(message = "Nomor seri tidak boleh kosong.")
    @Column(name = "nomor_seri")
    private String nomorSeri;

    // Konstruktor default yang diperlukan oleh JPA
    public Barang() {}

    public Barang(long id, String nama, Integer jumlah, String nomorSeri) {
        this.id = id;
        this.nama = nama;
        this.jumlah = jumlah;
        this.nomorSeri = nomorSeri;
    }

    // Getter dan setter
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Integer getJumlah() {
        return jumlah;
    }

    public void setJumlah(Integer jumlah) {
        this.jumlah = jumlah;
    }

    public String getNomorSeri() {
        return nomorSeri;
    }

    public void setNomorSeri(String nomorSeri) {
        this.nomorSeri = nomorSeri;
    }
}
