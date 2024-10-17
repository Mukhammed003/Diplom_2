package original.requestbodies;

import java.util.List;

public class RequestBodyForCreatingOrder {

    private List<String> ingredients;

    public RequestBodyForCreatingOrder(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
