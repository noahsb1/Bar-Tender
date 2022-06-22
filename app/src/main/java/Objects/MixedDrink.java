package Objects;

public class MixedDrink implements java.io.Serializable {
    private final String name;
    private final String ingredients;
    private final String recipe;
    private final String categoriesOfIngredients;
    private final byte[] image;

    public MixedDrink(String name, String ingredients, String recipe, String categoriesOfIngredients, byte[] image) {
        this.name = name;
        this.ingredients = ingredients;
        this.recipe = recipe;
        this.categoriesOfIngredients = categoriesOfIngredients;
        this.image = image;
    }

    public String getCategoriesOfIngredients() {
        return categoriesOfIngredients;
    }

    public byte[] getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getRecipe() {
        return recipe;
    }

    @Override
    public String toString() {
        String[] temp = recipe.split(", ");
        String rtn = name + "\n";
        for(String string : temp) {
            rtn += string + "\n";
        }
        rtn = rtn.substring(0, rtn.length() - 1);
        return rtn;
    }
}
