package pdc;

public class Main {

    public static void main(String[] args) throws Exception {

        if (args[0].equals("master")) {
            Master master = new Master();
            master.listen(5000);
        }

        if (args[0].equals("worker")) {
            Worker worker = new Worker();
            worker.joinCluster("localhost", 5000);
        }
    }
}
