public class Account {
    private String accountName;
    private double balance;
    private int pinCode;

    public String getAccountName() {
        return accountName;
    }
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
    public int getPinCode() {
        return pinCode;
    }
    public void setPinCode(int pinCode) {
        this.pinCode = pinCode;
    }

    public Account(String accountName, double balance, int pinCode) {
        this.accountName = accountName;
        this.balance = balance;
        this.pinCode = pinCode;
    }
}