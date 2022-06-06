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
