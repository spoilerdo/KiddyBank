import Controllers.AccountController;

public class Test {
    public static void main(String[] args) {
        AccountController controller = new AccountController();

        Boolean hoi = controller.CreateAccount();

        System.out.println(hoi);
    }
}
