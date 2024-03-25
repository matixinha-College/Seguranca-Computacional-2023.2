package App;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import Auth.AuthenticationService;

public class BankService {

    private AuthenticationService authService;

    public BankService(AuthenticationService authService) {
        this.authService = authService;
    }

    public void handleClient(BufferedReader input, PrintStream output, int clientId) {
        Scanner scanner = new Scanner(input);

        boolean authenticated = false;
        BankUser currentUser = null;
        try {

            while (!authenticated) {
                output.println("\nEscolha uma opção:");
                output.println("1 - Login");
                output.println("2 - Cadastro");
                output.println("F - Desconectar-se");

                String choice = scanner.nextLine();
                //System.out.println("\nCliente " + clientId + ": " + choice);
                switch (choice) {
                    case "1":
                        currentUser = handleLogin(scanner, output);
                        if (currentUser != null) {
                            authenticated = true;
                            output.println("\nBem-vindo ao Banco!");
                        }
                        break;
                    case "2":
                        handleRegistration(scanner, output);
                        break;
                    case "F":
                    case "f":
                        output.println("\nDesconectando-se...");
                        return;
                    default:
                        output.println("Opção inválida.");
                        output.println("");
                }
            }
            if (authenticated) {
                handleMenu(currentUser, scanner, output);
            }
        } catch (NoSuchElementException e) {
            System.out.println("--BANCO--\nCliente " + clientId + " desconectado.");
        } finally {
            scanner.close();
        }
    }

    private BankUser handleLogin(Scanner scanner, PrintStream output) {
        output.println("\nDigite o número da conta:");
        String accountNumber = scanner.nextLine();
        output.println("\nDigite sua senha:");
        String password = scanner.nextLine();

        BankUser user = authService.authenticate(accountNumber, password);

        if (user != null) {
            output.println("\nutenticação bem-sucedida. Bem-vindo, " + user.getNome() + "!");
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
        String password;
        long cpf = 0;
        String nome;
        String endereco;
        int telefone = 0;
        Double saldoConta = 0.0;
        int accountNumber = 0;

        boolean cpfValid = false;
        boolean saldoValid = false;
        boolean telefoneValid = false;
        boolean accountNumberValid = false;

        output.println("\nDigite a senha:");
        password = scanner.nextLine();

        while (cpfValid == false) {
            try {
                output.println("Digite o CPF:");
                cpf = scanner.nextLong();
                cpfValid = true;
            } catch (InputMismatchException e) {
                output.println("\nValor inválido para o CPF. Certifique-se de digitar um número.");
                scanner.nextLine(); // Limpe o buffer do scanner
            }
        }

        output.println("Digite o nome:");
        nome = scanner.nextLine();

        output.println("Digite o endereço:");
        endereco = scanner.nextLine();

        while (telefoneValid == false) {
            try {
                output.println("Digite o telefone:");
                telefone = scanner.nextInt();
                telefoneValid = true;
            } catch (InputMismatchException e) {
                output.println("\nValor inválido para o telefone. Certifique-se de digitar um número.");
                scanner.nextLine(); // Limpe o buffer do scanner
            }

        }

        while (saldoValid == false) {
            try {
                output.println("Digite o saldo inicial:");
                saldoConta = scanner.nextDouble();
                saldoValid = true;
            } catch (InputMismatchException e) {
                output.println("\nValor inválido para o saldo. Certifique-se de digitar um número.");
                scanner.nextLine(); // Limpe o buffer do scanner
            }
        }
        output.println("Digite o saldo inicial:");
        saldoConta = scanner.nextDouble();

        while (accountNumberValid == false) {
            
        }
        output.println("Digite número da conta(provisório):");
        accountNumber = scanner.nextInt();

        BankUser newUser = new BankUser(accountNumber, password, cpf, nome, endereco, telefone,
                saldoConta, 0.005, 0.0);
        authService.registerUser(newUser);

        output.println("\nCadastro realizado com sucesso.");

    }

    private void handleMenu(BankUser currentUser, Scanner scanner, PrintStream output) {
        boolean exit = false;
        while (!exit) {
            output.println("\nEscolha uma opção:");
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
                    output.println("\nOpção inválida.");
            }
        }
    }

    private void handleTransfer(BankUser currentUser, Scanner scanner, PrintStream output) {
        output.println("\nDigite o número da conta de destino:");
        int destinationAccount = scanner.nextInt();
        output.println("Digite o valor a ser transferido:");
        double amount = scanner.nextDouble();

        // Verifica se a conta de destino é válida
        BankUser destinationUser = authService.findUserByAccountNumber(destinationAccount);
        if (destinationUser == null) {
            output.println("\nConta de destino não encontrada.");
            return;
        }

        // Verifica se o usuário tem saldo suficiente para a transferência
        if (currentUser.getSaldoConta() < amount) {
            output.println("\nSaldo insuficiente para a transferência.");
            return;
        }

        // Realiza a transferência
        currentUser.setSaldoConta(currentUser.getSaldoConta() - amount);
        destinationUser.setSaldoConta(destinationUser.getSaldoConta() + amount);

        output.println("\nTransferência realizada com sucesso.");
        output.println("Novo saldo da conta: " + currentUser.getSaldoConta());
    }

    private void handleDeposit(BankUser currentUser, Scanner scanner, PrintStream output) {
        output.println("\nDigite o valor a ser depositado:");
        double amount = scanner.nextDouble();
        if (amount <= 0) {
            output.println("\nValor inválido.");
            return;
        }
        currentUser.setSaldoConta(currentUser.getSaldoConta() + amount);
        output.println("\nDepósito de R$" + amount + " realizado com sucesso.");
    }

    private void handleWithdraw(BankUser currentUser, Scanner scanner, PrintStream output) {
        output.println("\nDigite o valor a ser sacado:");
        double amount = scanner.nextDouble();
        if (amount <= 0 || amount > currentUser.getSaldoConta()) {
            output.println("\nValor inválido ou saldo insuficiente.");
            return;
        }
        currentUser.setSaldoConta(currentUser.getSaldoConta() - amount);
        output.println("\nSaque de R$" + amount + " realizado com sucesso.");
    }

    private void handleCheckBalance(BankUser currentUser, PrintStream output) {
        output.println("\nSaldo da conta: R$" + currentUser.getSaldoConta());
        output.println("Saldo da poupança: R$" + currentUser.getSaldoPoupanca());
        output.println("Saldo da renda fixa: R$" + currentUser.getSaldoRendaFixa());
    }

    private void handleInvestments(BankUser currentUser, Scanner scanner, PrintStream output) {
        output.println("\nEscolha uma opção de investimento:");
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
                output.println("\nVoltando ao menu principal.");
                break;
            default:
                output.println("\nOpção inválida.");
        }
    }

    private void handleSavings(BankUser currentUser, Scanner scanner, PrintStream output) {
        output.println("\nOpção de poupança selecionada.");

        output.println("\nDigite o valor a ser investido na poupança:");
        double amount = scanner.nextDouble();

        // Verifica se o valor é válido
        if (amount <= 0 || amount > currentUser.getSaldoConta()) {
            output.println("\nValor inválido.");
            return;
        }

        // Realiza o investimento na poupança
        currentUser.setSaldoConta(currentUser.getSaldoConta() - amount);
        currentUser.setSaldoPoupanca(amount);

        output.println("\nInvestimento em poupança realizado com sucesso.");

        // Simulação de rendimento para três, seis e doze meses
        double simulatedAmount = currentUser.getSaldoPoupanca();
        output.println("\nSimulação de rendimento para poupança:");
        output.println("\nAplicação inicial: R$" + simulatedAmount);

        for (int i = 3; i <= 12; i += 3) {
            double interest = simulatedAmount * 0.005 * i;
            output.println("\nRendimento em " + i + " meses: R$" + interest);
            output.println("\nTotal em " + i + " meses: R$" + (simulatedAmount + interest));
        }
    }

    private void handleFixedIncome(BankUser currentUser, Scanner scanner, PrintStream output) {
        output.println("\nOpção de renda fixa selecionada.");

        output.println("\nDigite o valor a ser investido em renda fixa:");
        double amount = scanner.nextDouble();

        // Verifica se o valor é válido
        if (amount <= 0 || amount > currentUser.getSaldoConta()) {
            output.println("\nValor inválido.");
            return;
        }

        // Realiza o investimento em renda fixa
        currentUser.setSaldoConta(currentUser.getSaldoConta() - amount);
        currentUser.setSaldoRendaFixa(amount);

        output.println("\nInvestimento em renda fixa realizado com sucesso.");

        // Simulação de rendimento para três, seis e doze meses
        output.println("\nSimulação de rendimento para renda fixa:");
        output.println("Aplicação inicial: R$" + amount);

        for (int i = 3; i <= 12; i += 3) {
            double interest = amount * 0.015 * i;
            output.println("\nRendimento em " + i + " meses: R$" + interest);
            output.println("Total em " + i + " meses: R$" + (amount + interest));
        }
    }

}
