import javax.swing.*;
import java.util.function.Function;

public class Main {

    public static void main(String[] args) throws Exception {

        final double A = -5;
        final double B = 5;
        Function<Double, Double> f = Math::sin;

        JFrame a = new JFrame();

        a.setSize(800, 600);
        a.setVisible(true);
        a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        a.add(new Frame(A, B, f));


    }
}
