package Auth;

import java.util.ArrayList;
import java.util.List;
import App.BankUser;

public class AuthenticationService {

    private List<BankUser> users;

    public AuthenticationService() {
        users = new ArrayList<>();
        // Exemplo de usuários registrados
        users.add(
                new BankUser(123, "pass1", 12345678900L, "Caio Gilas", "Rua A, 123", 123456789, 15000.0, 0.5, 0.0));
        users.add(
                new BankUser(456, "pass2", 52468743100L, "Gui Ricas", "Rua B, 456", 521478569, 10000.0, 0.5, 0.0));
        users.add(
                new BankUser(789, "pass3", 98765432100L, "Jota Silva", "Rua C, 789", 987654321, 5000.0, 0.5, 0.0));
    }

    public BankUser authenticate(String accountNumber, String password) {
        for (BankUser user : users) {
            if (String.valueOf(user.getAccountNumber()).equals(String.valueOf(accountNumber)) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null; // Usuário não encontrado ou senha incorreta
    }

    public void registerUser(BankUser user) {
        users.add(user);
    }

    @SuppressWarnings("unlikely-arg-type")
    public BankUser findUserByAccountNumber(int destinationAccount) {
        for (BankUser user : users) {
            if (String.valueOf(user.getAccountNumber()).equals(destinationAccount)) {
                return user;
            }
        }
        return null; // Usuário não encontrado
    }
}
