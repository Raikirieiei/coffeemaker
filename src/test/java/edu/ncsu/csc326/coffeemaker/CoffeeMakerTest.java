/*
 * Copyright (c) 2009,  Sarah Heckman, Laurie Williams, Dright Ho
 * All Rights Reserved.
 * 
 * Permission has been explicitly granted to the University of Minnesota 
 * Software Engineering Center to use and distribute this source for 
 * educational purposes, including delivering online education through
 * Coursera or other entities.  
 * 
 * No warranty is given regarding this software, including warranties as
 * to the correctness or completeness of this software, including 
 * fitness for purpose.
 * 
 * 
 * Modifications 
 * 20171114 - Ian De Silva - Updated to comply with JUnit 4 and to adhere to 
 * 							 coding standards.  Added test documentation.
 */
package edu.ncsu.csc326.coffeemaker;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.beans.Transient;

import javax.crypto.AEADBadTagException;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc326.coffeemaker.exceptions.InventoryException;
import edu.ncsu.csc326.coffeemaker.exceptions.RecipeException;

/**
 * Unit tests for CoffeeMaker class.
 * 
 * @author Sarah Heckman
 */
public class CoffeeMakerTest {


	/**
	 * The object under test.
	 */
	private CoffeeMaker coffeeMaker;
	private CoffeeMaker coffeeMakerMock;
	private Inventory inventory;
	private RecipeBook recipeBookMock;
	
	// Sample recipes to use in testing.
	private Recipe recipe1;
	private Recipe recipe2;
	private Recipe recipe3;
	private Recipe recipe4;

	/**
	 * The stubbed recipe book.
	 */
	private RecipeBook recipeBookStub;

	private Recipe [] stubRecipies;

	/**
	 * Initializes some recipes to test with and the {@link CoffeeMaker} 
	 * object we wish to test.
	 * 
	 * @throws RecipeException  if there was an error parsing the ingredient 
	 * 		amount when setting up the recipe.
	 */
	@Before
	public void setUp() throws RecipeException {
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
		recipe2.setAmtChocolate("20");
		recipe2.setAmtCoffee("3");
		recipe2.setAmtMilk("1");
		recipe2.setAmtSugar("1");
		recipe2.setPrice("75");
		
		//Set up for r3
		recipe3 = new Recipe();
		recipe3.setName("Latte");
		recipe3.setAmtChocolate("50");
		recipe3.setAmtCoffee("50");
		recipe3.setAmtMilk("50");
		recipe3.setAmtSugar("20");
		recipe3.setPrice("100");
		
		//Set up for r4
		recipe4 = new Recipe();
		recipe4.setName("Hot Chocolate");
		recipe4.setAmtChocolate("1");
		recipe4.setAmtCoffee("1");
		recipe4.setAmtMilk("1");
		recipe4.setAmtSugar("1");
		recipe4.setPrice("65");

		stubRecipies = new Recipe [] {recipe1, recipe2, recipe3, null};
	}
	
	
	/**
	 * Given a coffee maker with the default inventory
	 * When we add inventory with well-formed quantities
	 * Then we do not get an exception trying to read the inventory quantities.
	 * 
	 * @throws InventoryException  if there was an error parsing the quanity
	 * 		to a positive integer.
	 */
	@Test
	public void testAddInventory() throws InventoryException {
		coffeeMaker.addInventory("4","7","0","9");
	}
	
	/**
	 * Given a coffee maker with the default inventory
	 * When we add inventory with malformed quantities (i.e., a negative 
	 * quantity and a non-numeric string)
	 * Then we get an inventory exception
	 * 
	 * @throws InventoryException  if there was an error parsing the quanity
	 * 		to a positive integer.
	 */
	@Test(expected = InventoryException.class)
	public void testAddInventoryException() throws InventoryException {
		coffeeMaker.addInventory("4", "-1", "asdf", "3");
	}
	
	/**
	 * Given a coffee maker with one valid recipe
	 * When we make coffee, selecting the valid recipe and paying more than 
	 * 		the coffee costs
	 * Then we get the correct change back.
	 */
	@Test
	public void testMakeCoffee() {
		coffeeMaker.addRecipe(recipe1);
		assertEquals(25, coffeeMaker.makeCoffee(0, 75));
	}

	/**
	 * Coffee maker can only contains 3 recipes at most(according to the requirement)
	 * so when we add recipe more than 3 we should get exception.
	 */
	@Test(expected = RecipeException.class)
	public void testExceedAddRecipe() {
		coffeeMaker.addRecipe(recipe1);
		coffeeMaker.addRecipe(recipe2);
		coffeeMaker.addRecipe(recipe3);
		coffeeMaker.addRecipe(recipe4);
	}


	/**
	 * Given a coffee maker with the default inventory
	 * When we add ingredient in to the inventory,
	 * The amount of ingredient in the inventory should add up too.
	 */
	@Test
	public void testAddAmountInventory() throws InventoryException {
		coffeeMaker.addInventory("5", "5", "5", "5");
		assertEquals("20", coffeeMaker.checkInventory().toString().split(" ")[1].split("\n")[0]);
		assertEquals("20", coffeeMaker.checkInventory().toString().split(" ")[2].split("\n")[0]);
		assertEquals("20", coffeeMaker.checkInventory().toString().split(" ")[3].split("\n")[0]);
		assertEquals("20", coffeeMaker.checkInventory().toString().split(" ")[4].split("\n")[0]);
		
	}

	/**
	 * Given a coffee maker with the default inventory
	 * When we make a coffee,
	 * The amount of ingredient in the inventory should decrease according to the recipe.
	 */
	@Test
	public void testInventoryReduce() throws InventoryException{
		coffeeMaker.addRecipe(recipe1);
		coffeeMaker.makeCoffee(0, 50);
		assertEquals("17", coffeeMaker.checkInventory().toString().split(" ")[1].split("\n")[0]);
		assertEquals("14", coffeeMaker.checkInventory().toString().split(" ")[2].split("\n")[0]);
		assertEquals("14", coffeeMaker.checkInventory().toString().split(" ")[3].split("\n")[0]);
		assertEquals("15", coffeeMaker.checkInventory().toString().split(" ")[4].split("\n")[0]);
		
	}

	/**
	 * When we delete a recipe the function must return the name of the
	 * recipe we want to delete.
	 */
	@Test
	public void testDeleteRecipe(){
		coffeeMaker.addRecipe(recipe1);
		assertEquals("Coffee", coffeeMaker.deleteRecipe(0));
	}

	/**
	 * When we delete a non-existant recipe the function must null
	 */
	@Test
	public void testDeleteNotExistRecipe(){
		assertNull(coffeeMaker.deleteRecipe(0));
	}

	/**
	 * When we add a recipe, the recipe name should be in recipe array.
	 */
	@Test
	public void testAddRecipe(){
		coffeeMaker.addRecipe(recipe1);
		assertEquals(coffeeMaker.getRecipes()[0].toString(), "Coffee");
	}

	/**
	 * When we add a already exist recipe, the function should
	 * return false.
	 */
	@Test
	public void testAddExistRecipe(){
		coffeeMaker.addRecipe(recipe1);
		assertFalse(coffeeMaker.addRecipe(recipe1));
	}


	/**
	 * When we make a coffee from recipe, the changes that customer receive
	 * should return a correct amount of money.
	 */
	@Test
	public void testFullOrderCoffee() throws InventoryException{
		coffeeMaker.addRecipe(recipe1);
		coffeeMaker.addRecipe(recipe2);
		coffeeMaker.addRecipe(recipe3);
		assertEquals(30, coffeeMaker.makeCoffee(0, 80));// 50$ recipe > 30$ changes
		assertEquals(80, coffeeMaker.makeCoffee(1, 80));// not enough chocolate(return full money).
		assertEquals(80, coffeeMaker.makeCoffee(2, 80));// not enough money(return full money).
		assertEquals(120, coffeeMaker.makeCoffee(2, 120));// not enough coffee(return full money).
		coffeeMaker.addInventory("50", "0", "0", "0");
		assertEquals(120, coffeeMaker.makeCoffee(2, 120));// not enough milk(return full money).
		coffeeMaker.addInventory("0", "50", "0", "0");
		assertEquals(80, coffeeMaker.makeCoffee(2, 80));// not enough sugar(return full money).
		assertEquals(80, coffeeMaker.makeCoffee(3, 80));// null recipe
	}

	/**
	 * When we edit recipe it should return name of the recipe that we want
	 * to edit.
	 */
	@Test
	public void testEditRecipe(){
		coffeeMaker.addRecipe(recipe1);
		assertEquals("Coffee", coffeeMaker.editRecipe(1, recipe1));
	}

	/**
	 * When we edit add coffee with alphabet letter,
	 * Program should return InventoryException.
	 */
	@Test(expected = InventoryException.class)
	public void testAddAlphabetCoffee() throws InventoryException{
		coffeeMaker.addInventory("a", "0", "0", "0");
	}

	/**
	 * When we edit add milk with alphabet letter,
	 * Program should return InventoryException.
	 */
	@Test(expected = InventoryException.class)
	public void testAddAlphabetMilk() throws InventoryException{
		coffeeMaker.addInventory("0", "a", "0", "0");
	}

	/**
	 * When we edit add sugar with alphabet letter,
	 * Program should return InventoryException.
	 */
	@Test(expected = InventoryException.class)
	public void testAddAlphabetSugar() throws InventoryException{
		coffeeMaker.addInventory("0", "0", "a", "0");
	}

	/**
	 * When we edit add chocolate with alphabet letter,
	 * Program should return InventoryException.
	 */
	@Test(expected = InventoryException.class)
	public void testAddAlphabetChocolate() throws InventoryException{
		coffeeMaker.addInventory("0", "0", "0", "a");
	}


	/**
	 * When we make a coffee from recipe, the changes that customer receive
	 * should return a correct amount of money.
	 * but this time I include Mockito to help with the test.
	 */
	@Test
	public void testFullOrderCoffeeWithMock() throws InventoryException {
		
		when(coffeeMakerMock.getRecipes()).thenReturn(stubRecipies);
		// coffeeMaker.addRecipe(recipe1);
		// coffeeMaker.addRecipe(recipe2);
		// coffeeMaker.addRecipe(recipe3);
		assertEquals(30, coffeeMakerMock.makeCoffee(0, 80));// 50$ recipe > 30$ changes
		assertEquals(80, coffeeMakerMock.makeCoffee(1, 80));// not enough chocolate(return full money).
		assertEquals(80, coffeeMakerMock.makeCoffee(2, 80));// not enough money(return full money).
		assertEquals(120, coffeeMakerMock.makeCoffee(2, 120));// not enough coffee(return full money).
		coffeeMakerMock.addInventory("50", "0", "0", "0");
		assertEquals(120, coffeeMakerMock.makeCoffee(2, 120));// not enough milk(return full money).
		coffeeMakerMock.addInventory("0", "50", "0", "0");
		assertEquals(80, coffeeMakerMock.makeCoffee(2, 80));// not enough sugar(return full money).
		assertEquals(80, coffeeMakerMock.makeCoffee(3, 80));// null recipe
	}

}
