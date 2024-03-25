package App;

import java.math.BigInteger;

public class BankUser {

    private int accountNumber;
    private String password;
    private long cpf;
    private String nome;
    private String endereco;
    private int telefone;
    private double saldoConta;
    private double saldoPoupanca;
    private double saldoRendaFixa;

    public BankUser(int accountNumber, String password, long l, String nome, String endereco, int telefone,
            double saldoConta, double saldoPoupanca, double saldoRendaFixa) {
        this.accountNumber = accountNumber;
        this.password = password;
        this.cpf = l;
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.saldoConta = saldoConta;
        this.saldoPoupanca = 0.005 * saldoConta;
        this.saldoRendaFixa = 0.0;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getPassword() {
        return password;
    }

    public long getCpf() {
        return cpf;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public int getTelefone() {
        return telefone;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCpf(long cpf) {
        this.cpf = cpf;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setTelefone(int telefone) {
        this.telefone = telefone;
    }

    public double getSaldoConta() {
        return saldoConta;
    }

    public void setSaldoConta(double saldoConta) {
        this.saldoConta = saldoConta;
    }

    public double getSaldoPoupanca() {
        return saldoPoupanca;
    }

    public void setSaldoPoupanca(double saldoPoupanca) {
        this.saldoPoupanca = saldoPoupanca;
    }

    public double getSaldoRendaFixa() {
        return saldoRendaFixa;
    }

    public void setSaldoRendaFixa(double saldoRendaFixa) {
        this.saldoRendaFixa = saldoRendaFixa;
    }

    private String generateAccountNumber() {
        // Gerar um número de conta de 10 números aleatórios
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append((int) (Math.random() * 10));
        }
        return sb.toString();
    }
}