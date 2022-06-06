import Objects.MixedDrink;
import Utilities.FileLoader;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class FileInitializerTest {
    @Test
    public void test() throws FileNotFoundException {
        ArrayList<MixedDrink> temp = FileLoader.fileLoaderInitializer();
        assertEquals(temp.get(0).getName(), "pina colada");
        assertEquals(temp.get(0).getIngredients(), "rum, coke, cock");
        assertEquals(temp.get(0).getRecipe(), "1/2 cup rum, 1/4 cup rum");
    }
}
