package Objects;

public class MixedDrink {
    private final String name;
    private final String ingredients;
    private final String recipe;

    public MixedDrink(String name, String ingredients, String recipe) {
        this.name = name;
        this.ingredients = ingredients;
        this.recipe = recipe;
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
}
