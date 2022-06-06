import Objects.MixedDrink;
import org.junit.Test;
import static org.junit.Assert.*;

public class MixedDrinkTester {
    @Test
    public void mixedDrainToString() {
        MixedDrink test = new MixedDrink("test", "", "1/2 cup rum, 1/4 cup rum");
        assertEquals(test.toString(), "test\n1/2 cup rum\n1/4 cup rum");
    }
}
