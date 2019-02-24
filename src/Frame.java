import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Line2D;
import java.util.function.Function;

public class Frame extends JPanel {

    private Double A, B;
    private Function<Double, Double> f;

    private static GraphParams params = new GraphParams();

    public Frame(double A, double B, Function<Double, Double> f) throws Exception {

        setSize(800, 600);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                params.HorizontalSize = getWidth();
                params.VerticalSize = getHeight();

                findFunctionBoundValues();
                findAxisPosition();

            }
        });

        this.A = A;
        this.B = B;
        this.f = f;

        params.HorizontalSize = 800;
        params.VerticalSize = 560;

        findFunctionBoundValues();
        findAxisPosition();

        if(functionConstantOnSegment()) throw new Exception("Функция - константа");
    }

    private void findAxisPosition() {

        double xAxisEvaluation = Double.POSITIVE_INFINITY;
        double yAxisEvaluation = Double.POSITIVE_INFINITY;

        params.XAxisPosition = -1;
        params.YAxisPosition = -1;

        for (int i = 0; i < params.HorizontalSize; i++) {
            double x = A + (B - A) * i/(params.HorizontalSize - 1);
            double y = f.apply(x);

            if(Math.abs(x) < xAxisEvaluation) {
                xAxisEvaluation = Math.abs(x);
                params.XAxisPosition = i;
            }
            if(Math.abs(y) < yAxisEvaluation) {
                yAxisEvaluation = Math.abs(y);
                double a1 =(y - params.MinY);
                double a2 = (params.MaxY - params.MinY);
                double a3 = (params.VerticalSize - 1);
                double d = a1 / a2 * a3;
                double round = Math.round(d);
                params.YAxisPosition = params.VerticalSize - (int)round - 1;
            }
        }
    }

    private void findFunctionBoundValues() {

        params.MinY = Double.POSITIVE_INFINITY;
        params.MaxY = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < params.HorizontalSize; i++) {
            double x = A + (B - A) * i / (params.HorizontalSize - 1);
            double y = f.apply(x);

            params.MinY = Math.min(params.MinY, y);
            params.MaxY = Math.max(params.MaxY, y);
        }
    }

    private boolean functionConstantOnSegment() {
        return Math.abs(params.MaxY - params.MinY) < 1e-10;
    }

    private void redrawAxis(Graphics g) {
        if (A < 0 && B > 0)
            drawLine(g, params.XAxisPosition, 0, params.XAxisPosition, params.VerticalSize);
        if (params.MaxY > 0 && params.MinY < 0)
            drawLine(g, 0, params.YAxisPosition, params.HorizontalSize, params.YAxisPosition);
    }

    private int calcPoint(double x){
        return params.VerticalSize - (int)Math.round((f.apply(x) - params.MinY) / (params.MaxY - params.MinY) * (params.VerticalSize - 1)) - 1;
    }

    private void draw(Graphics g) {
        redrawAxis(g);

        int previousX = 0;
        int previousY = calcPoint(A);

        for (int i = 0; i < params.HorizontalSize; i++) {
            double x = A + (B - A) * i / (params.HorizontalSize - 1);
            int y = calcPoint(x);
            drawLine(g, previousX, previousY, i, y);
            previousX = i;
            previousY = y;
        }
    }

    public void drawLine(Graphics g, int x1, int y1, int x2, int y2) {
        System.out.println(y1+"|"+y2);
        Graphics2D g2 = (Graphics2D) g;
        Line2D lin = new Line2D.Float(x1, y1, x2, y2);
        g2.setColor(Color.BLACK);
        g2.draw(lin);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        draw(g);
    }
}
