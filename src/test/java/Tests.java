import controller.HamiltonController;
import org.junit.jupiter.api.Test;

public class Tests {

    @Test
    public void test() {
        HamiltonController hamiltonController = new HamiltonController(null);
        int[] path = hamiltonController.getPath();

        for (int i = 0; i < path.length; i++) {
            System.out.printf("%d %d\n", i, path[i]);
        }
    }
}


