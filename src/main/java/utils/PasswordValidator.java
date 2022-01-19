package utils;

public class PasswordValidator {
    String password1;
    String password2;

    public PasswordValidator(String password1, String password2) {
        this.password1 = password1;
        this.password2 = password2;
    }
    public boolean isPasswordValid(){
        boolean isValid = false;
        isValid = password1.equals(password2);


        return isValid;
    }
}

