package com.brainysoftware.downloader.ui;

import java.io.File;
import java.util.List;
import java.util.Optional;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;

/**
 * @author Budi Kurniawan (http://brainysoftware.com)
 */
public class GUIManager {
	

	private static GUIManager instance;
    private MenuBar menuBar;

	public static GUIManager getInstance() {
        return instance;
    }
	
	public static class Builder {
	    public static GUIManager build(MenuBar menuBar) {
	        synchronized(GUIManager.class) {
	            if (instance == null) {
	                instance = new GUIManager(menuBar);
	            }
	        }
	        return instance;
	    }
	}
	
	private GUIManager(MenuBar menuBar) {
	    this.menuBar = menuBar;
	}


	public void setWindowTitle(String title) {
	    //currentStage.setTitle(title);
	}
	
    public void showMessageDialog(String header, String content) {
        Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
        errorAlert.setTitle("Message");
        errorAlert.setHeaderText(header);
        errorAlert.setContentText(content);
        errorAlert.showAndWait();
    }
    
    public void showErrorDialog(String header, String content) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error");
        errorAlert.setHeaderText(header);
        errorAlert.setContentText(content);
        errorAlert.showAndWait();
    }
    
    public ButtonType showConfirmDialog(String header, String content) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm");
        confirmAlert.setHeaderText(header);
        confirmAlert.setContentText(content);
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent()) {
            return result.get();
        }
        return null;
    }
    
    public String showInputDialog(String header, String content) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Input Dialog");
        dialog.setHeaderText(header);
        dialog.setContentText(content);

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            return result.get();
        } else {
            return null;
        }
    }

    public Menu getMenu(MenuBar menuBar, String id) {
        if (menuBar == null) {
            return null;
        }
        ObservableList<Menu> menus = menuBar.getMenus();
        for (Menu menu : menus) {
        	if (id.equals(menu.getId())) {
        		return menu;
        	} else {
        		// try submenus. Note that Menu is derived from MenuItem
        		ObservableList<MenuItem> menuItems = menu.getItems();
        		for (MenuItem menuItem : menuItems) {
        			if (menuItem instanceof Menu && id.equals(menuItem.getId())) {
        				return (Menu) menuItem;
        			}
        		}
        	}
        }
        return null;
    }
    
    public MenuItem getMenuItem(String id) {
        ObservableList<Menu> menus = menuBar.getMenus();
        for (Menu menu : menus) {
            ObservableList<MenuItem> items = menu.getItems();
            for (MenuItem item : items) {
                if (id.equals(item.getId())) {
                    return item;
                }
            }
        }
        return null;
    }
    
    public Node getRootNode(Node control) {
        Node node = control;
        while (node.getParent() != null) {
            node = node.getParent();
        }
        return node;
    }
    
    public List<File> chooseFiles(String title, File initDir) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.setInitialDirectory(initDir);
        return fileChooser.showOpenMultipleDialog(null);
    }
    
    public void setVisibleMenuById(String menuId, boolean value) {
        ObservableList<Menu> menus = menuBar.getMenus();
        for (Menu menu : menus) {
        	if (menuId.equals(menu.getId())) {
        		menu.setVisible(value);
       		 	break;
       	 	}
        }
    }

    public void setDisableMenuItemById(String menuId, boolean value) {
        ObservableList<Menu> menus = menuBar.getMenus();
        for (Menu menu : menus) {
            if (menuId.equals(menu.getId())) {
                menu.setVisible(value);
                break;
            }
            menu.getItems().forEach(menuItem -> {
                if (menuId.equals(menuItem.getId())) {
                    menuItem.setDisable(value);
                    return;
                }
            });
        }
    }
}