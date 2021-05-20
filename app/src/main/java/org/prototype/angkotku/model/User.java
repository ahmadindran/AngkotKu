package org.prototype.angkotku.model;

public class User {
    private String nama, username, email, noHp, password;

    public User() {
    }

    public User(String nama, String username, String email, String noHp, String password) {
        this.nama = nama;
        this.username = username;
        this.email = email;
        this.noHp = noHp;
        this.password = password;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
