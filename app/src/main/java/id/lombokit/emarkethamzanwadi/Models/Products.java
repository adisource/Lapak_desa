package id.lombokit.emarkethamzanwadi.Models;
/**
 * Created by one on 22/1/18.
 */
public class Products {
    private  String id_barang;
    private  String nama_barang;
    private  String keterangan;
    private  String harga;
    private  String stok;
    private  String status;
    private  String gambar;
    private  String nama;
    private  Integer quantity;

    public Products() {
    }

    public Products(String id_barang, String nama_barang, String harga, String gambar, String nama,String stok) {
        this.id_barang = id_barang;
        this.nama_barang = nama_barang;
        this.harga = harga;
        this.gambar = gambar;
        this.nama = nama;
        this.stok = stok;
    }

    public Products(String id_barang, String nama_barang, String keterangan, String harga, String stok, String status, String gambar, String nama) {
        this.id_barang = id_barang;
        this.nama_barang = nama_barang;
        this.keterangan = keterangan;
        this.harga = harga;
        this.stok = stok;
        this.status = status;
        this.gambar = gambar;
        this.nama = nama;
    }


    public String getId_barang() {
        return id_barang;
    }

    public void setId_barang(String id_barang) {
        this.id_barang = id_barang;
    }

    public String getNama_barang() {
        return nama_barang;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getStok() {
        return stok;
    }

    public void setStok(String stok) {
        this.stok = stok;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}