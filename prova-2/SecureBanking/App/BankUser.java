package App;

public class BankUser {

    private String accountNumber;
    private String password;
    private String cpf;
    private String nome;
    private String endereco;
    private String telefone;
    private double saldoConta;
    private double saldoPoupanca;
    private double saldoRendaFixa;

    public BankUser(String password, String cpf, String nome, String endereco, String telefone, double saldoConta, double saldoPoupanca, double saldoRendaFixa) {
        this.accountNumber = generateAccountNumber();
        this.password = password;
        this.cpf = cpf;
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.saldoConta = 0.0;
        this.saldoPoupanca = 0.005;
        this.saldoRendaFixa = 0.0;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getPassword() {
        return password;
    }

    public String getCpf() {
        return cpf;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setTelefone(String telefone) {
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
