import org.jgraph.graph.DefaultEdge;


public class Krawedz extends DefaultEdge {
    int przepustowosc;
    int przeplyw;
    double pr;

    public void setPrzepustowosc(int i) {
        this.przepustowosc = i;
    }

    public void setPrzeplyw(int i) {
        this.przeplyw = i;
    }

    public void setPr(double i) {
        this.pr = i;
    }

    public int getPrzepustowosc() {
        return this.przepustowosc;
    }

    public int getPrzeplyw() {
        return this.przeplyw;
    }

    public double getPr() {
        return this.pr;
    }

}
