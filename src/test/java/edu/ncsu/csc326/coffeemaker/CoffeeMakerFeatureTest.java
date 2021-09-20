package edu.ncsu.csc326.coffeemaker;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.beans.Transient;

import javax.crypto.AEADBadTagException;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc326.coffeemaker.exceptions.InventoryException;
import edu.ncsu.csc326.coffeemaker.exceptions.RecipeException; 

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CoffeeMakerFeatureTest {

    private CoffeeMaker coffeeMaker;
    private CoffeeMaker coffeeMakerMock;
	private Inventory inventory;
	private RecipeBook recipeBookMock;
	
	// Sample recipes to use in testing.
	private Recipe recipe1;
	private Recipe recipe2;
	private Recipe recipe3;

	private Recipe [] stubRecipies;

    @Given("the cafe is open")
    public void the_cafe_is_open() throws RecipeException {
        recipeBookMock = mock(RecipeBook.class); // mock a concrete class
		inventory = new Inventory();
		coffeeMakerMock = new CoffeeMaker(recipeBookMock, inventory);
		coffeeMaker = new CoffeeMaker();
		//Set up for r1
		recipe1 = new Recipe();
		recipe1.setName("Coffee");
		recipe1.setAmtChocolate("0");
		recipe1.setAmtCoffee("3");
		recipe1.setAmtMilk("1");
		recipe1.setAmtSugar("1");
		recipe1.setPrice("50");
		
		//Set up for r2
		recipe2 = new Recipe();
		recipe2.setName("Mocha");
		recipe2.setAmtChocolate("2");
		recipe2.setAmtCoffee("3");
		recipe2.setAmtMilk("1");
		recipe2.setAmtSugar("1");
		recipe2.setPrice("75");
		
		//Set up for r3
		recipe3 = new Recipe();
		recipe3.setName("Latte");
		recipe3.setAmtChocolate("5");
		recipe3.setAmtCoffee("5");
		recipe3.setAmtMilk("5");
		recipe3.setAmtSugar("2");
		recipe3.setPrice("100");
		

		stubRecipies = new Recipe [] {recipe1, recipe2, recipe3, null};
    }   

    @Given("customer order a coffee")
    public void CustomerOrderACoffee() {
        when(coffeeMakerMock.getRecipes()).thenReturn(stubRecipies);
    }

    @Then("if customer order recipe {int} and customer pay {int} the changes is {int}")
    public void IfCustomerOrderRecipeAndCustomerPayTheChangesIs(Integer recipe, Integer pay, Integer changes) {
        assertEquals((Integer)changes, (Integer)coffeeMakerMock.makeCoffee(recipe-1, pay));
    }
   
}
