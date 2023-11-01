package application;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.text.SimpleDateFormat;
import java.util.Locale;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class InventoryManagerGUI extends Application {
	private InventoryManager inventoryManager;
	 private Map<String, List<Item>> inventory;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		inventoryManager = new InventoryManager();
		inventoryManager.loadInventoryFromFile("inventory.txt");

		// Create UI components
		BorderPane root = new BorderPane();
		ListView<String> categoryListView = new ListView<>();
		ListView<String> itemListView = new ListView<>();
		Label nameLabel = new Label("Name:");
		TextField nameTextField = new TextField();
		Label quantityLabel = new Label("Quantity:");
		TextField quantityTextField = new TextField();
		Label expirationLabel = new Label("Expiration Date:");
		DatePicker expirationDatePicker = new DatePicker();
		Button addButton = new Button("Add Item");
		Button displayButton = new Button("Display Inventory");
		// Set preferred width for categoryListView
		categoryListView.setPrefWidth(400); // Adjust the value to your desired width

		// Set preferred height for categoryListView
		categoryListView.setPrefHeight(400); // Adjust the value to your desired height

		// Set preferred width for itemListView
		itemListView.setPrefWidth(500); // Adjust the value to your desired width

		// Set preferred height for itemListView
		itemListView.setPrefHeight(800); // Adjust the value to your desired height
		// Add event handlers
		categoryListView.getItems().addAll("Canned Foods", "Fresh Foods", "Clothing Items", "House Items");
		
		categoryListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
		    itemListView.getItems().clear();
		    List<Item> items = inventoryManager.getInventory().get(newValue);
		    SimpleDateFormat guiDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		    for (Item item : items) {
		        String formattedExpirationDate = guiDateFormat.format(item.getExpirationDate());
		        itemListView.getItems().add("Name: " + item.getName() + ", Quantity: " + item.getQuantity() + ", Expiration Date: " + formattedExpirationDate);
		    }
		});
	

		addButton.setOnAction(e -> {
		    String category = categoryListView.getSelectionModel().getSelectedItem();
		    String name = nameTextField.getText();
		    int quantity = Integer.parseInt(quantityTextField.getText());
		    Date expirationDate = Date.valueOf(expirationDatePicker.getValue());
		    inventoryManager.addItem(category, name, quantity, expirationDate);

		    SimpleDateFormat guiDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		    String formattedExpirationDate = guiDateFormat.format(expirationDate);

		    itemListView.getItems().add("Name: " + name + ", Quantity: " + quantity + ", Expiration Date: " + formattedExpirationDate);
		});

		displayButton.setOnAction(e -> inventoryManager.displayInventory());

		// Create layout
		VBox inputVBox = new VBox(nameLabel, nameTextField, quantityLabel, quantityTextField, expirationLabel,
				expirationDatePicker, addButton, displayButton);
		VBox listVBox = new VBox(categoryListView, itemListView);
		root.setLeft(inputVBox);
		root.setRight(listVBox);

		// Create scene and stage
		Scene scene = new Scene(root, 1000, 600);
		primaryStage.setTitle("Food Pantry Inventory Manager");
		primaryStage.setScene(scene);
		primaryStage.show();
		Button removeButton = new Button("Remove Item");

		removeButton.setOnAction(e -> {
		    String selectedCategory = categoryListView.getSelectionModel().getSelectedItem();
		    String selectedItem = itemListView.getSelectionModel().getSelectedItem();
		    if (selectedCategory != null && selectedItem != null) {
		        // Extract the name from the selected item
		        String itemName = selectedItem.split(", ")[0].substring(6);
		        inventoryManager.removeItem(selectedCategory, itemName);
		        itemListView.getItems().remove(selectedItem);
		    }
		});

		// Add the removeButton to the inputVBox
		inputVBox.getChildren().add(removeButton);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			inventoryManager.saveInventoryToFile("inventory.txt");
		}));
		Button decrementButton = new Button("Sell an Item");
		inputVBox.getChildren().add(decrementButton);
		decrementButton.setOnAction(e -> {
		    String selectedCategory = categoryListView.getSelectionModel().getSelectedItem();
		    String selectedItem = itemListView.getSelectionModel().getSelectedItem();
		    if (selectedCategory != null && selectedItem != null) {
		        // Extract the name and quantity from the selected item
		        String[] itemInfo = selectedItem.split(", ");
		        String itemName = itemInfo[0].substring(6);
		        int itemQuantity = Integer.parseInt(itemInfo[1].substring(10));
		        
		        // Decrement the quantity
		        if (itemQuantity > 0) {
		            itemQuantity--;
		            // Update the GUI display
		            itemListView.getItems().remove(selectedItem);
		            itemListView.getItems().add("Name: " + itemName + ", Quantity: " + itemQuantity + ", Expiration Date: " + itemInfo[2]);
		            // Update the inventory
		            inventoryManager.decrementQuantity(selectedCategory, itemName);
		        }
		    }
		});
}
}