package core;

public class FeastMenu extends FeastMenuList{
    private String codeSetMenu;
    private String name;
    private double price;
    private String ingredients;

    public FeastMenu(String codeSetMenu, String name, double price, String ingredients) {
        this.codeSetMenu = codeSetMenu;
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
    }

    public double getPrice() {
        return price;
    }


    public String getCodeSetMenu() {
        return codeSetMenu;
    }

    public String getName() {
        return name;
    }

    public String getIngredients() {
        return ingredients;
    }

}
