Feature: Make a Coffee
    A customer can order a coffee by choosing a recipe and pay.

    Background: Setup Menus
        Given the cafe is open

    Scenario: Order a coffee
        Given customer order a coffee
        Then if customer order recipe 1 and customer pay 50 the changes is 0

    Scenario: Order a coffee with changes
        Given customer order a coffee
        Then if customer order recipe 2 and customer pay 100 the changes is 25
    

  