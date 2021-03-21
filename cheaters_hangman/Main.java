public class Main {
    public static void main(String[] args) {
        if (args.length > 0) {
            if (args[0].toLowerCase().equals("debug")) {
                System.out.println("Debug mode: ");
            } 
        } else {
                System.out.println("Non-debug mode.");   
        }
    }
}