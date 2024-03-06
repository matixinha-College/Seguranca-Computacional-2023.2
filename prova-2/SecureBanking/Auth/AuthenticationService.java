package Auth;

import java.util.ArrayList;
import java.util.List;
import App.BankUser;

public class AuthenticationService {

    private List<BankUser> users;

    public AuthenticationService() {
        users = new ArrayList<>();
        // Exemplo de usuários registrados
        users.add(new BankUser("pass1", "12345678900", "Caio Gilas", "Rua A, 123", "123456789",15000, 0.5, 0.0));
        users.add(new BankUser("pass2", "52468743100", "Guilher Ricas", "Rua B, 456", "521478569",10000, 0.5, 0.0));
        users.add(new BankUser("pass3", "98765432100", "Jota Silva", "Rua C, 789", "987654321",5000, 0.5, 0.0));
    }

    public BankUser authenticate(String accountNumber, String password) {
        for (BankUser user : users) {
            if (user.getAccountNumber().equals(accountNumber) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null; // Usuário não encontrado ou senha incorreta
    }

    public void registerUser(BankUser user) {
        users.add(user);
    }

    public BankUser findUserByAccountNumber(String accountNumber) {
        for (BankUser user : users) {
            if (user.getAccountNumber().equals(accountNumber)) {
                return user;
            }
        }
        return null; // Usuário não encontrado
    }
}
