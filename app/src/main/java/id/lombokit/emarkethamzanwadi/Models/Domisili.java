package id.lombokit.emarkethamzanwadi.Models;

public class Domisili {
    private  String id_kawasan;
    private  String id_kab;
    private  String id_kec;
    private  String nama_desa;
    private  String kecamatan;
    private  String kabupaten;

    public Domisili() {
    }

    public Domisili(String nama_desa, String kecamatan, String kabupaten) {
        this.nama_desa = nama_desa;
        this.kecamatan = kecamatan;
        this.kabupaten = kabupaten;
    }

    public String getId_kawasan() {
        return id_kawasan;
    }

    public String getId_kab() {
        return id_kab;
    }

    public void setId_kab(String id_kab) {
        this.id_kab = id_kab;
    }

    public String getId_kec() {
        return id_kec;
    }

    public void setId_kec(String id_kec) {
        this.id_kec = id_kec;
    }

    public void setId_kawasan(String id_kawasan) {
        this.id_kawasan = id_kawasan;
    }

    public String getKecamatan() {
        return kecamatan;
    }

    public void setKecamatan(String kecamatan) {
        this.kecamatan = kecamatan;
    }

    public String getKabupaten() {
        return kabupaten;
    }

    public void setKabupaten(String kabupaten) {
        this.kabupaten = kabupaten;
    }

    public String getNama_desa() {
        return nama_desa;
    }

    public void setNama_desa(String nama_desa) {
        this.nama_desa = nama_desa;
    }
}
