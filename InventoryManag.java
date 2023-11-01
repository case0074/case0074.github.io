package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

class InventoryManager {
	private Map<String, List<Item>> inventory = new HashMap<>();

	public InventoryManager() {
		inventory = new HashMap<>();
		inventory.put("Canned Foods", new ArrayList<>());
		inventory.put("Fresh Foods", new ArrayList<>());
		inventory.put("Clothing Items", new ArrayList<>());
		inventory.put("House Items", new ArrayList<>());
	}

	public void addItem(String category, String name, int quantity, Date expirationDate) {
		List<Item> items = inventory.get(category);
		if (items != null) {
			items.add(new Item(name, quantity, expirationDate));
		} else {
			System.out.println("Invalid category.");
		}
	}

	public void removeItem(String category, String name) {
	    List<Item> items = inventory.get(category);
	    if (items != null) {
	        Item removedItem = null;
	        for (Item item : items) {
	            if (item.getName().equals(name)) {
	                removedItem = item;
	                break;
	            }
	        }
	        if (removedItem != null) {
	            items.remove(removedItem);
	            saveInventoryToFile("inventory.txt"); // Save the updated inventory to file
	        } else {
	            System.out.println("Item not found. Category: " + category + ", Name: " + name);
	        }
	    } else {
	        System.out.println("Invalid category.");
	    }
	}

	public void displayInventory() {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy");
	    
	    for (Map.Entry<String, List<Item>> entry : inventory.entrySet()) {
	        System.out.println("Category: " + entry.getKey());
	        List<Item> items = entry.getValue();
	        for (Item item : items) {
	            System.out.println(item.getName() + ", " + item.getQuantity() + ", " + dateFormat.format(item.getExpirationDate()));
	        }
	        System.out.println();
	    }
	}

	public Map<String, List<Item>> getInventory() {
		return inventory;
	}

	public void saveInventoryToFile(String fileName) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

	    try (PrintWriter writer = new PrintWriter(fileName)) {
	        for (Map.Entry<String, List<Item>> entry : inventory.entrySet()) {
	            String category = entry.getKey();
	            for (Item item : entry.getValue()) {
	                String expirationDate = dateFormat.format(item.getExpirationDate());
	                writer.println(category + "," + item.getName() + "," + item.getQuantity() + "," + expirationDate);
	            }
	        }
	    } catch (FileNotFoundException e) {
	        System.out.println("Error saving inventory to file: " + e.getMessage());
	    }
	}

	public void loadInventoryFromFile(String fileName) {
	    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
	        String line;
	        while ((line = br.readLine()) != null) {
	            String[] parts = line.split(",");
	            String category = parts[0];
	            String name = parts[1];
	            int quantity = Integer.parseInt(parts[2]);
	            String dateString = parts[3];
	            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
	            Date expirationDate = dateFormat.parse(dateString);
	            addItem(category, name, quantity, expirationDate);
	        }
	    } catch (IOException | ParseException e) {
	        System.out.println("Error loading inventory from file: " + e.getMessage());
	    }
	}

    public int getItemQuantity(String category, String itemName) {
        List<Item> items = inventory.get(category);

        if (items != null) {
            for (Item item : items) {
                if (item.getName().equals(itemName)) {
                    return item.getQuantity();
                }
            }
        }

        return -1;
    }
    public void decrementQuantity(String category, String name) {
        List<Item> items = inventory.get(category);
        if (items != null) {
            for (Item item : items) {
                if (item.getName().equals(name)) {
                    item.decrementQuantity();
                    saveInventoryToFile("inventory.txt"); // Save the updated inventory to file
                    return; // Found and decremented the item, no need to continue the loop
                }
            }
            System.out.println("Item not found. Category: " + category + ", Name: " + name);
        } else {
            System.out.println("Invalid category.");
        }
    }
}


