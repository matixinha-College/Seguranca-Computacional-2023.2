package App;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

import Auth.AuthenticationService;

public class BankService {

    private AuthenticationService authService;

    public BankService(AuthenticationService authService) {
        this.authService = authService;
    }

    public void handleClient(BufferedReader input, PrintStream output) {
        Scanner scanner = new Scanner(input);

        boolean authenticated = false;
        BankUser currentUser = null;

        while (!authenticated) {
            output.println("Escolha uma opção:");
            output.println("1 - Login");
            output.println("2 - Cadastro");
            output.println("F - Desconectar-se");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    currentUser = handleLogin(scanner, output);
                    if (currentUser != null) {
                        authenticated = true;
                        output.println("Bem-vindo ao Banco!");
                    }
                    break;
                case "2":
                    handleRegistration(scanner, output);
                    break;
                case "F":
                case "f":
                    output.println("Desconectando-se...");
                    return;
                default:
                    output.println("Opção inválida.");
            }
        }

        if (authenticated) {
            handleMenu(currentUser, scanner, output);
        }

        scanner.close();
    }

    private BankUser handleLogin(Scanner scanner, PrintStream output) {
        output.println("Digite o número da conta:");
        String accountNumber = scanner.nextLine();
        output.println("Digite sua senha:");
        String password = scanner.nextLine();

        BankUser user = authService.authenticate(accountNumber, password);

        if (user != null) {
            output.println("Autenticação bem-sucedida. Bem-vindo, " + user.getNome() + "!");
            output.println("CPF: " + user.getCpf());
            output.println("Endereço: " + user.getEndereco());
            output.println("Telefone: " + user.getTelefone());
            return user;
        } else {
            output.println("Número da conta ou senha inválidos. Tente novamente.");
            return null;
        }
    }

    private void handleRegistration(Scanner scanner, PrintStream output) {
        output.println("Digite a senha:");
        String password = scanner.nextLine();
        output.println("Digite o CPF:");
        String cpf = scanner.nextLine();
        output.println("Digite o nome:");
        String nome = scanner.nextLine();
        output.println("Digite o endereço:");
        String endereco = scanner.nextLine();
        output.println("Digite o telefone:");
        String telefone = scanner.nextLine();
        output.println("Digite o telefone:");
        String saldoConta = scanner.nextLine();

        BankUser newUser = new BankUser(password, cpf, nome, endereco, telefone, Integer.parseInt(saldoConta), 0.5, 0.0);
        authService.registerUser(newUser);
        output.println("Cadastro realizado com sucesso.");
    }

    private void handleMenu(BankUser currentUser, Scanner scanner, PrintStream output) {
        boolean exit = false;
        while (!exit) {
            output.println("Escolha uma opção:");
            output.println("1 - Transferência");
            output.println("2 - Depósito");
            output.println("3 - Saque");
            output.println("4 - Saldo");
            output.println("5 - Investimentos");
            output.println("F - Desconectar-se");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    handleTransfer(currentUser, scanner, output);
                    break;
                case "2":
                    handleDeposit(currentUser, scanner, output);
                    break;
                case "3":
                    handleWithdraw(currentUser, scanner, output);
                    break;
                case "4":
                    handleCheckBalance(currentUser, output);
                    break;
                case "5":
                    handleInvestments(currentUser, scanner, output);
                    break;
                case "F":
                case "f":
                    output.println("Desconectando-se...");
                    exit = true;
                    break;
                default:
                    output.println("Opção inválida.");
            }
        }
    }

    private void handleTransfer(BankUser currentUser, Scanner scanner, PrintStream output) {
        output.println("Digite o número da conta de destino:");
        String destinationAccount = scanner.nextLine();
        output.println("Digite o valor a ser transferido:");
        double amount = Double.parseDouble(scanner.nextLine());
    
        // Verifica se a conta de destino é válida
        BankUser destinationUser = authService.findUserByAccountNumber(destinationAccount);
        if (destinationUser == null) {
            output.println("Conta de destino não encontrada.");
            return;
        }
    
        // Verifica se o usuário tem saldo suficiente para a transferência
        if (currentUser.getSaldoConta() < amount) {
            output.println("Saldo insuficiente para a transferência.");
            return;
        }
    
        // Realiza a transferência
        currentUser.setSaldoConta(currentUser.getSaldoConta() - amount);
        destinationUser.setSaldoConta(destinationUser.getSaldoConta() + amount);
    
        output.println("Transferência realizada com sucesso.");
        output.println("Novo saldo da conta: " + currentUser.getSaldoConta());
    }

    private void handleDeposit(BankUser currentUser, Scanner scanner, PrintStream output) {
        output.println("Digite o valor a ser depositado:");
        double amount = Double.parseDouble(scanner.nextLine());
        if (amount <= 0) {
            output.println("Valor inválido.");
            return;
        }
        currentUser.setSaldoConta(currentUser.getSaldoConta() + amount);
        output.println("Depósito de R$" + amount + " realizado com sucesso.");
    }

    private void handleWithdraw(BankUser currentUser, Scanner scanner, PrintStream output) {
        output.println("Digite o valor a ser sacado:");
        double amount = Double.parseDouble(scanner.nextLine());
        if (amount <= 0 || amount > currentUser.getSaldoConta()) {
            output.println("Valor inválido ou saldo insuficiente.");
            return;
        }
        currentUser.setSaldoConta(currentUser.getSaldoConta() - amount);
        output.println("Saque de R$" + amount + " realizado com sucesso.");
    }
    
    private void handleCheckBalance(BankUser currentUser, PrintStream output) {
        output.println("Saldo da conta: R$" + currentUser.getSaldoConta());
        output.println("Saldo da poupança: R$" + currentUser.getSaldoPoupanca());
        output.println("Saldo da renda fixa: R$" + currentUser.getSaldoRendaFixa());
    }

    private void handleInvestments(BankUser currentUser, Scanner scanner, PrintStream output) {
        output.println("Escolha uma opção de investimento:");
        output.println("1 - Poupança");
        output.println("2 - Renda Fixa");
        output.println("F - Voltar");
    
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                handleSavings(currentUser, scanner, output);
                break;
            case "2":
                handleFixedIncome(currentUser, scanner, output);
                break;
            case "F":
            case "f":
                output.println("Voltando ao menu principal.");
                break;
            default:
                output.println("Opção inválida.");
        }
    }
    
    private void handleSavings(BankUser currentUser, Scanner scanner, PrintStream output) {
        output.println("Opção de poupança selecionada.");
    
        output.println("Digite o valor a ser investido na poupança:");
        double amount = Double.parseDouble(scanner.nextLine());

        // Verifica se o valor é válido
        if (amount <= 0 || amount > currentUser.getSaldoConta()) {
            output.println("Valor inválido.");
            return;
        }

        // Realiza o investimento na poupança
        currentUser.setSaldoConta(currentUser.getSaldoConta() - amount);
        currentUser.setSaldoPoupanca(amount);

        output.println("Investimento em poupança realizado com sucesso.");
    
        // Simulação de rendimento para três, seis e doze meses
        double simulatedAmount = currentUser.getSaldoPoupanca();
        output.println("Simulação de rendimento para poupança:");
        output.println("Aplicação inicial: R$" + simulatedAmount);
    
        for (int i = 3; i <= 12; i += 3) {
            double interest = simulatedAmount * 0.005 * i;
            output.println("Rendimento em " + i + " meses: R$" + interest);
            output.println("Total em " + i + " meses: R$" + (simulatedAmount + interest));
        }
    }
    
    private void handleFixedIncome(BankUser currentUser, Scanner scanner, PrintStream output) {
        output.println("Opção de renda fixa selecionada.");
    
        output.println("Digite o valor a ser investido em renda fixa:");
        double amount = Double.parseDouble(scanner.nextLine());
    
        // Verifica se o valor é válido
        if (amount <= 0 || amount > currentUser.getSaldoConta()) {
            output.println("Valor inválido.");
            return;
        }
    
        // Realiza o investimento em renda fixa
        currentUser.setSaldoConta(currentUser.getSaldoConta() - amount);
        currentUser.setSaldoRendaFixa(amount);
    
        output.println("Investimento em renda fixa realizado com sucesso.");
    
        // Simulação de rendimento para três, seis e doze meses
        output.println("Simulação de rendimento para renda fixa:");
        output.println("Aplicação inicial: R$" + amount);
    
        for (int i = 3; i <= 12; i += 3) {
            double interest = amount * 0.015 * i;
            output.println("Rendimento em " + i + " meses: R$" + interest);
            output.println("Total em " + i + " meses: R$" + (amount + interest));
        }
    }
    

}