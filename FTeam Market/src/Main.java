import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;


public class Main extends Application {
	
	Connection connection;
	Stage window;
	TableView<User> table = new TableView<>();
	TableView<Item> itemTable = new TableView<>();
	TableView<Transaction> transactionTable = new TableView<>();
	TableView<CartItem> cartTable = new TableView<>();
	ObservableList<CartItem> dataCart = FXCollections.observableArrayList();
	ObservableList<Transaction> transactions = FXCollections.observableArrayList();
	ObservableList<User> users = FXCollections.observableArrayList();
	ObservableList<Item> newItems = FXCollections.observableArrayList();
	ObservableList<SellItem> data = FXCollections.observableArrayList();
	String cuid = "US011";
	TableView<SellItem> sellTable = new TableView<>();
	int cqty;
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		window = primaryStage;
		
	// LOGIN
		
		Label usernameL = new Label("Username");
		Label passwordL = new Label("Password");
		TextField usernameTf = new TextField();
		PasswordField passwordTf = new PasswordField();
		Button registerBL = new Button("Register Account Page");
		Button loginBL = new Button("Login");
		Label loginL = new Label("Login");
		loginL.setFont(new Font("Arial", 24));
		HBox lButtonBox = new HBox(10);
		lButtonBox.getChildren().addAll(registerBL, loginBL);
		GridPane loginGrid = new GridPane();
		loginGrid.setHgap(10);
		loginGrid.setVgap(10);
		loginGrid.add(loginL, 0, 0, 2, 1);
		loginGrid.add(usernameL, 0, 1);
		loginGrid.add(passwordL, 0, 2);
		loginGrid.add(usernameTf, 1, 1);
		loginGrid.add(passwordTf, 1, 2);
		loginGrid.add(lButtonBox, 1, 3);
		loginGrid.setPadding(new Insets(30));
		Scene loginScene = new Scene(loginGrid);
		
		
	
	// REGISTER
		
		Label registerL = new Label("REGISTER");
		registerL.setFont(new Font("Arial", 24));
		Label userIdLR =  new Label("User ID");
		Label userNameLR =  new Label("Username");
		Label passwordLR = new Label("Password");
		Label emailLR = new Label("Email");
		Label phoneLR = new Label("Phone Number");
		Label ageLR = new Label("Age");
		Label genderLR = new Label("Gender");
		TextField userIdTfR = new TextField();
		userIdTfR.setDisable(true);
		TextField userNameTfR = new TextField();
		PasswordField passwordTfR  = new PasswordField();
		TextField emailTfR = new TextField();
		TextField phoneTfR = new TextField();
		Spinner<Integer> ageSR = new Spinner<>(15,70, 16);
		
		ToggleGroup gender = new ToggleGroup();  
	    RadioButton maleRR = new RadioButton("Male");  
	    RadioButton femaleRR = new RadioButton("Female");  
	    maleRR.setToggleGroup(gender);
	    femaleRR.setToggleGroup(gender);
	    HBox genderBox=new HBox();  
	    genderBox.setSpacing(10);
	    genderBox.getChildren().addAll(maleRR, femaleRR);
	     
	    Button loginR = new Button("Login Page");
	    Button registerR = new Button("Register");	
	    HBox registerPageButton = new HBox(10);
	    registerPageButton.getChildren().addAll(loginR, registerR);
	    
	    GridPane registerGrid = new GridPane();
	    registerGrid.setHgap(10);
	    registerGrid.setVgap(10);
		registerGrid.add(registerL, 0, 0);
		registerGrid.add(userIdLR, 0, 1);
		registerGrid.add(userIdTfR, 1, 1);
		registerGrid.add(userNameLR, 0, 2);
		registerGrid.add(userNameTfR, 1, 2);
		registerGrid.add(passwordLR, 0, 3);
		registerGrid.add(passwordTfR, 1, 3);
		registerGrid.add(emailLR, 0, 4);
		registerGrid.add(emailTfR, 1, 4);
		registerGrid.add(phoneLR, 0, 5);
		registerGrid.add(phoneTfR, 1, 5);
		registerGrid.add(ageLR, 0, 6);
		registerGrid.add(ageSR, 1, 6);
		registerGrid.add(genderLR, 0, 7);
		registerGrid.add(genderBox, 1, 7);
		registerGrid.add(registerPageButton, 1, 8);
		registerGrid.setPadding(new Insets(30));
		Scene registerScene = new Scene(registerGrid);
		userIdTfR.setText(cuid);


		
	// USER
		
		MenuBar menuBarUser = new MenuBar();
		Menu menuUser = new Menu("Menu");
		MenuItem itemMarket = new MenuItem("Item Market");
		MenuItem CartItem = new MenuItem("Cart Item");
		MenuItem transactionHistory = new MenuItem("Transaction History");
		MenuItem logOutUser = new MenuItem("Logout");
		menuUser.getItems().addAll(itemMarket, CartItem, transactionHistory, logOutUser);
		menuBarUser.getMenus().addAll(menuUser);
		
		TableColumn<SellItem, String> itemIDColS = new TableColumn<>("Item ID");
		itemIDColS.setCellValueFactory(new PropertyValueFactory<>("itemID"));
		TableColumn<SellItem, String> itemNameCol = new TableColumn<>("Item Name");
		itemNameCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));
		TableColumn<SellItem, String> itemDescriptionCol = new TableColumn<>("Item Description");
		itemDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("itemDescription"));
		TableColumn<SellItem, String> priceCol = new TableColumn<>("Price");
		priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
		TableColumn<SellItem, String> quantityCol = new TableColumn<>("Quantity");
		quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		sellTable.getColumns().addAll(itemIDColS, itemNameCol, itemDescriptionCol, priceCol, quantityCol);
		refreshTableSell();
		
		Label itemIdLI = new Label("itemID");
		Label itemNameLI = new Label("item Name");
		Label itemDescLI = new Label("item Description");
		Label itemPriceLI = new Label("price");
		Label itemQtyLI = new Label("Quantity");
		
		TextField itemIdTfI = new TextField();
		itemIdTfI.setDisable(true);
		TextField itemNameTfI = new TextField();
		itemNameTfI.setDisable(true);
		TextField itemDescTfI = new TextField();
		itemDescTfI.setDisable(true);
		TextField itemPriceTfI = new TextField();
		itemPriceTfI.setDisable(true);
		
		Spinner<Integer> itemQtySI = new Spinner<>(0, 2147483647, 0);
		
		GridPane sideGrid3 = new GridPane();
		sideGrid3.add(itemIdLI, 0, 0);
		sideGrid3.add(itemNameLI, 0, 1);
		sideGrid3.add(itemDescLI, 0, 2);
		sideGrid3.add(itemPriceLI, 0, 3);
		sideGrid3.add(itemQtyLI, 0, 4);
		sideGrid3.add(itemIdTfI, 1, 0);
		sideGrid3.add(itemNameTfI, 1, 1);
		sideGrid3.add(itemDescTfI, 1, 2);
		sideGrid3.add(itemPriceTfI, 1, 3);
		sideGrid3.add(itemQtySI, 1, 4);
		sideGrid3.setHgap(10);
		sideGrid3.setVgap(10);
		sideGrid3.setPadding(new Insets(20,10,10,10));
		
		Button clearFormI = new Button("Clear Form");
		Button addToCart = new Button("Add To Cart");
		HBox addCartBox = new HBox(20);		
		addCartBox.getChildren().addAll(clearFormI,addToCart);
	
		VBox addVBox = new VBox(10);
		addVBox.getChildren().addAll(sideGrid3, addCartBox);
	
		TableColumn<CartItem, String> itemIDColz = new TableColumn<>("itemID");
		itemIDColz.setCellValueFactory(new PropertyValueFactory<>("itemID"));
		TableColumn<CartItem, String> itemNameColz = new TableColumn<>("itemName");
		itemNameColz.setCellValueFactory(new PropertyValueFactory<>("itemName"));
		TableColumn<CartItem, String> itemDescriptionColz = new TableColumn<>("itemDescription");
		itemDescriptionColz.setCellValueFactory(new PropertyValueFactory<>("itemDescription"));
		TableColumn<CartItem, String> priceColz = new TableColumn<>("Price");
		priceColz.setCellValueFactory(new PropertyValueFactory<>("price"));
		TableColumn<CartItem, String> quantityColz = new TableColumn<>("Quantity");
		quantityColz.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		TableColumn<CartItem, String> totalPriceColz = new TableColumn<>("Total Price");
		totalPriceColz.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

		cartTable.getColumns().addAll(itemIDColz, itemNameColz, itemDescriptionColz, priceColz, quantityColz, totalPriceColz);
		refreshTableCart(cuid);
		
		TextField removeItemLabel = new TextField("");
		Button removeFCart = new Button("Remove From Cart");
		removeFCart.setDisable(true);
		Button checkout = new Button("CheckOut");
		HBox cartButtonBox = new HBox(20);
		cartButtonBox.setPadding(new Insets(20));
		cartButtonBox.getChildren().addAll(removeFCart, checkout, removeItemLabel);
		
		GridPane itemMarketGrid = new GridPane();
		itemMarketGrid.add(sellTable, 0, 0);
		itemMarketGrid.add(addVBox, 1, 0);
		
		GridPane cartItemGrid = new GridPane();
		cartItemGrid.add(cartTable, 0, 0);
		cartItemGrid.add(cartButtonBox, 0, 1);
		
		GridPane transactionHistoryGrid = new GridPane();
		BorderPane userLayout = new BorderPane(); 
		userLayout.setTop(menuBarUser);
		
		Scene userScene = new Scene(userLayout, 700, 700);
		
	// ADMIN
		
		MenuBar menuBarAdmin = new MenuBar();
		Menu menuAdmin = new Menu("Menu");
		MenuItem manageUser = new MenuItem("Manage User");
		MenuItem manageItem = new MenuItem("Manage Item");
		MenuItem transaction = new MenuItem("Transaction");
		MenuItem logOutAdmin = new MenuItem("Logout");
		
		menuAdmin.getItems().addAll(manageUser, manageItem, transaction, logOutAdmin);
		menuBarAdmin.getMenus().addAll(menuAdmin);
		
		TableColumn<User, String> useridCol = new TableColumn<>("User ID");
        useridCol.setCellValueFactory(new PropertyValueFactory<>("userid"));

        TableColumn<User, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> passwordCol = new TableColumn<>("Password");
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));

        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<User, String> phonenumberCol = new TableColumn<>("Phone Number");
        phonenumberCol.setCellValueFactory(new PropertyValueFactory<>("phonenumber"));

        TableColumn<User, String> ageCol = new TableColumn<>("Age");
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));

        TableColumn<User, String> genderCol = new TableColumn<>("Gender");
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));

        table.getColumns().addAll(useridCol, usernameCol, passwordCol, emailCol, phonenumberCol, ageCol, genderCol);
        table.setItems(users);
        refreshTableUser();
 
        Label userIdLA =  new Label("User ID");
		Label userNameLA =  new Label("Username");
		Label passwordLA = new Label("Password");
		Label emailLA = new Label("Email");
		Label phoneLA = new Label("Phone Number");
		Label ageLA = new Label("Age");
		Label genderLA = new Label("Gender");
		
		TextField userIdTfA = new TextField();
		
		userIdTfA.setEditable(false);
		userIdTfA.setFocusTraversable(false);
		userIdTfA.setDisable(true);
		
		TextField userNameTfA = new TextField();
		
		userNameTfA.setEditable(false);
		userIdTfA.setFocusTraversable(false);
		userNameTfA.setDisable(true);
		
		PasswordField passwordTfA  = new PasswordField();
		
		passwordTfA.setEditable(false);
		passwordTfA.setFocusTraversable(false);
		passwordTfA.setDisable(true);
		
		TextField emailTfA = new TextField();
		TextField phoneTfA = new TextField();
		
		Spinner<Integer> ageSA = new Spinner<>(15,70, 16);
	
		ToggleGroup genderA = new ToggleGroup();  
	    RadioButton maleRA = new RadioButton("Male");  
	    RadioButton femaleRA = new RadioButton("Female");  
	    maleRA.setToggleGroup(genderA);
	    femaleRA.setToggleGroup(genderA);
	    
	    HBox genderBoxA=new HBox();  
	    genderBoxA.setSpacing(10);
	    genderBoxA.getChildren().addAll(maleRA, femaleRA);
	    
	    Button updateUser = new Button("Update User");
	    Button delteUser = new Button("Delete User");

	    HBox adminBBox = new HBox(30);
	    adminBBox.getChildren().addAll(updateUser, delteUser);

		GridPane sideGrid = new GridPane();
		sideGrid.add(userIdLA, 0, 0);
		sideGrid.add(userNameLA, 0, 1);
		sideGrid.add(passwordLA, 0, 2);
		sideGrid.add(emailLA, 0, 3);
		sideGrid.add(phoneLA, 0, 4);
		sideGrid.add(ageLA, 0, 5);
		sideGrid.add(genderLA, 0, 6);
		sideGrid.add(userIdTfA, 1, 0);
		sideGrid.add(userNameTfA, 1, 1);
		sideGrid.add(passwordTfA, 1, 2);
		sideGrid.add(emailTfA, 1, 3);
		sideGrid.add(phoneTfA, 1, 4);
		sideGrid.add(ageSA, 1, 5);
		sideGrid.add(genderBoxA, 1, 6);
		sideGrid.setHgap(10);
		sideGrid.setVgap(10);
		sideGrid.setPadding(new Insets(20,10,10,10));
	
		VBox sideBox = new VBox();
		sideBox.setPadding(new Insets(10));
		sideBox.getChildren().addAll(sideGrid, adminBBox);

		TableColumn<Item, String> itemIDColumn = new TableColumn<>("itemID");
        itemIDColumn.setCellValueFactory(new PropertyValueFactory<>("itemID"));

        TableColumn<Item, String> itemNameColumn = new TableColumn<>("Item Name");
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));

        TableColumn<Item, String> itemDescriptionColumn = new TableColumn<>("Item Description");
        itemDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("itemDescription"));

        TableColumn<Item, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Item, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        itemTable.getColumns().addAll(itemIDColumn, itemNameColumn, itemDescriptionColumn, priceColumn, quantityColumn);
        refreshTableItem();

		Label itemIdLM = new Label("itemID");
		Label itemNameLM = new Label("item Name");
		Label itemDescLM = new Label("item Description");
		Label itemPriceLM = new Label("price");
		Label itemQtyLM = new Label("Quantity");
		
		TextField itemIdTfM = new TextField();
		TextField itemNameTfM = new TextField();
		TextField itemDescTfM = new TextField();
		
		Spinner<Integer> itemPriceSM = new Spinner<>(0, 2147483647, 0);
		Spinner<Integer> itemQtySM = new Spinner<>(0, 2147483647, 0);
		
		GridPane sideGrid2 = new GridPane();
		sideGrid2.add(itemIdLM, 0, 0);
		sideGrid2.add(itemNameLM, 0, 1);
		sideGrid2.add(itemDescLM, 0, 2);
		sideGrid2.add(itemPriceLM, 0, 3);
		sideGrid2.add(itemQtyLM, 0, 4);
		sideGrid2.add(itemIdTfM, 1, 0);
		sideGrid2.add(itemNameTfM, 1, 1);
		sideGrid2.add(itemDescTfM, 1, 2);
		sideGrid2.add(itemPriceSM, 1, 3);
		sideGrid2.add(itemQtySM, 1, 4);
		sideGrid2.setHgap(10);
		sideGrid2.setVgap(10);
		sideGrid2.setPadding(new Insets(20,10,10,10));

		Button insertItem = new Button("Insert Item");
		Button updateItem = new Button("Update Item");
		Button deleteItem = new Button("Delete Item");
		
		HBox crudButtonM = new HBox(10);
		crudButtonM.getChildren().addAll(insertItem, updateItem, deleteItem);
		
		Button clearForm = new Button("Clear Form");

		VBox sideMI = new VBox(10);
		sideMI.getChildren().addAll(sideGrid2, crudButtonM, clearForm);
		
		TableColumn<Transaction, String> transactionIDColT = new TableColumn<>("Transaction ID");
		transactionIDColT.setCellValueFactory(new PropertyValueFactory<>("transactionID"));

		TableColumn<Transaction, String> buyerColY = new TableColumn<>("Buyer");
		buyerColY.setCellValueFactory(new PropertyValueFactory<>("buyer"));

		TableColumn<Transaction, String> itemNameColT = new TableColumn<>("Item Name");
		itemNameColT.setCellValueFactory(new PropertyValueFactory<>("itemName"));

		TableColumn<Transaction, String> itemDescriptionColT = new TableColumn<>("Item Description");
		itemDescriptionColT.setCellValueFactory(new PropertyValueFactory<>("itemDescription"));

		TableColumn<Transaction, String> priceColT = new TableColumn<>("Price");
		priceColT.setCellValueFactory(new PropertyValueFactory<>("price"));

		TableColumn<Transaction, String> quantityColT = new TableColumn<>("Quantity");
		quantityColT.setCellValueFactory(new PropertyValueFactory<>("quantity"));

		TableColumn<Transaction, String> totalPriceColT = new TableColumn<>("Total Price");
		totalPriceColT.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

		TableColumn<Transaction, LocalDate> transactionDateColT = new TableColumn<>("Transaction Date");
		transactionDateColT.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));

		transactionTable.getColumns().addAll(transactionIDColT, buyerColY, itemNameColT, itemDescriptionColT, priceColT, quantityColT, totalPriceColT, transactionDateColT);
		
		transactions.add(new Transaction("123", "John Doe", "Macbook Pro", "Laptop", "1000.0", "1", "1000.0", LocalDate.parse("2022-01-01")));
		transactions.add(new Transaction("124", "Jane Smith", "iPhone", "Smartphone", "800.0", "2", "1600.0", LocalDate.parse("2022-01-02")));
		transactionTable.setItems(transactions);
		refreshTableTransaction();

        GridPane manageUserGrid = new GridPane();
        manageUserGrid.add(table, 0, 0);
        manageUserGrid.add(sideBox, 1, 0);
        manageUserGrid.setPadding(new Insets(0,0,0,0));

        GridPane manageItemGrid = new GridPane();
        manageItemGrid.add(itemTable, 0, 0);
        manageItemGrid.add(sideMI, 1, 0);
        manageItemGrid.setPadding(new Insets(0,0,0,0));

        GridPane manageTransactionGrid = new GridPane();
        manageTransactionGrid.add(transactionTable, 0, 0);
        
		BorderPane adminLayout = new BorderPane(); 
		adminLayout.setTop(menuBarAdmin);
		
		Scene adminScene = new Scene(adminLayout,800,800);
		
		checkout.setOnAction(e -> {
			boolean check = true;
			
			try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/fteam", "root", "")) {
			    String sql = "SELECT item.itemID, item.quantity as itemQ, cart.itemID as cartID, cart.quantity as cartQ FROM cart INNER JOIN item on item.itemID = cart.itemID and cart.userID = ?";
			    PreparedStatement stmt = con.prepareStatement(sql);
			    stmt.setString(1, cuid);
			    ResultSet rs = stmt.executeQuery();
			    while (rs.next()) {			        
			        int itemQ = rs.getInt("itemQ");
			        int cartQ = rs.getInt("cartQ");
			        if(itemQ < cartQ) {
			        	Alerta.alertError("ERROR", "Stock not enough");
			        	check = false;
			        }
			    }
			} catch (SQLException ee) {
			    ee.printStackTrace();
			}
			
			if(check) {
				HashMap<String, String> cartItems = new HashMap<>();
				try {
				    String sql = "SELECT itemid, quantity FROM cart WHERE userid = ?";
				    PreparedStatement statement = connection.prepareStatement(sql);
				    statement.setString(1, cuid);
				    ResultSet rs = statement.executeQuery();

				    while (rs.next()) {
				        String itemid = rs.getString("itemid");
				        String quantity = rs.getString("quantity");
				        cartItems.put(itemid, quantity);
				    }
				    
				} catch (SQLException ee) {
				    System.out.println("Error: " + ee);
				}
				
				for (Map.Entry<String, String> entry : cartItems.entrySet()) {
				    System.out.println("Item ID: " + entry.getKey() + ", Quantity: " + entry.getValue());
				    String lastTrID = transactionIDGenerate();
					try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/fteam", "root", "")) {
						String sql = "INSERT INTO transaction (userid, transactionDate, transactionid) VALUES (?, ?, ?)";
						PreparedStatement statement = con.prepareStatement(sql);
						statement.setString(1, cuid);
						statement.setDate(2, new java.sql.Date(System.currentTimeMillis()));
						statement.setString(3, lastTrID);
						statement.executeUpdate();
					} catch (SQLException ee) {
						System.out.println(2);
					    ee.printStackTrace();
					}
	
					try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/fteam", "root", "")) {
						String sql = "INSERT INTO transactiondetail (transactionid, itemid, quantity) VALUES (?, ?, ?)";
						PreparedStatement statement = con.prepareStatement(sql);
						statement.setString(1, lastTrID);
						statement.setString(2, entry.getKey());
						statement.setString(3, entry.getValue());
						statement.executeUpdate();
					} catch (SQLException ee) {
						System.out.println(3);
					    ee.printStackTrace();
					}

					try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/fteam", "root", "")) {
						String sql = "UPDATE item SET quantity = quantity - ? WHERE itemid = ?";
						PreparedStatement statement = connection.prepareStatement(sql);
						statement.setString(1, entry.getValue());
						statement.setString(2, entry.getKey());
						statement.executeUpdate();
					} catch (SQLException ee) {
						System.out.println(4);
					    ee.printStackTrace();
					}
				}
				Alerta.alertInfo("BERHASIL CHECKOUT", "Click OK to continue");
				refreshTableCart(cuid);
				window.show();
			}
		});
		
	// REMOVE
		
		removeFCart.setOnAction(e -> {
			try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/fteam", "root", "")) {
	            String sql = "DELETE FROM cart WHERE userID = ? AND itemID = ?";
	            try (PreparedStatement statement = conn.prepareStatement(sql)) {
	                statement.setString(1, cuid);
	                statement.setString(2, removeItemLabel.getText());
	                statement.executeUpdate();
	                Alerta.alertInfo("SUCCESS DELETE", "Click ok To Continue");
	                refreshTableCart(cuid);
	                window.show();
	            }
	        } catch (SQLException ee) {
	            System.err.println(ee);
	        }
		});
		
		cartTable.setOnMouseClicked(event -> {
		    CartItem selectedUser = cartTable.getSelectionModel().getSelectedItem();
		    if (selectedUser != null) {
		        removeItemLabel.setText(selectedUser.getItemID());
		        removeFCart.setDisable(false);
				window.show();
		    }
		});
		
	// UPDATE
		
		updateUser.setOnAction(e -> {
			if(userIdTfA.getText().equals("")) {
				Alerta.alertInfo("UPDATE FAILED", "Select User First!");
			} else if(!emailTfA.getText().matches("^(?!.*@.*@)[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.com$")) {
				Alerta.alertError("Registration Error" , "Email email must consist of @ character, @ character must not be in front and must end with .com.");
			} else if( phoneTfA.getText().length() <9 || phoneTfR.getText().length() > 12) {
				Alerta.alertError("Registration Error" , "Phone Number must be between 9 – 12 characters.");
			} else {
				String genderx = "";
				if(maleRA.isSelected()) {
					genderx= "Male";
				} else {
					genderx= "Female";
				}
				
				try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/fteam", "root", "")) {
				    String sql = "UPDATE user SET email = ?, phoneNumber = ?, age = ?, gender = ? WHERE userID = ?";
				    PreparedStatement statement = conn.prepareStatement(sql);
				    statement.setString(1, emailTfA.getText());
				    statement.setString(2, phoneTfA.getText());
				    statement.setInt(3, ageSA.getValue());
				    statement.setString(4, genderx);
				    statement.setString(5, cuid);
				    statement.executeUpdate();
				    Alerta.alertInfo("UPDATE SUCCESS", "Click OK to Continue");
				    refreshTableUser();
				    window.show();
				} catch (SQLException ee) {
				    ee.printStackTrace();
				}
			}
		});
	
	// userMenu-itemMarket
		itemMarket.setOnAction(e -> {
			userLayout.setCenter(itemMarketGrid);
			window.show();
		});	
	
	// userMenu-cartItem
		CartItem.setOnAction(e -> {
			adminLayout.setCenter(cartItemGrid);
			window.show();
		});	
		
		
	// userMenu-transactionHistoryGrid
		transactionHistory.setOnAction(e -> {
			adminLayout.setCenter(transactionHistoryGrid);
			window.show();
		});	
	
	// adminMenu-manageUser
	manageUser.setOnAction(e -> {
		refreshTableUser();
		manageUserGrid.setPadding(new Insets(0,0,0,0));
		adminLayout.setCenter(manageUserGrid);
		window.show();
	});
	
	// adminMenu-manageItem
	manageItem.setOnAction(e -> {
		refreshTableItem();
		manageItemGrid.setPadding(new Insets(0,0,0,0));
		adminLayout.setCenter(manageItemGrid);
		window.show();
	});
		
	// adminMenu-manageTransaction
	transaction.setOnAction(e -> {
		refreshTableTransaction();
		manageTransactionGrid.setPadding(new Insets(0,0,0,0));
		adminLayout.setCenter(manageTransactionGrid);
		window.show();
	});
	
	// DELETE
	
		deleteItem.setOnAction(e->{
			if(itemIDValid(itemIdTfM.getText())) {
				String itemID = itemIdTfM.getText();
				String sql = "DELETE FROM item WHERE itemID = ?";

				try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/fteam", "root", "");
				        PreparedStatement statement = conn.prepareStatement(sql)) {
				        statement.setString(1, itemID);
				        statement.executeUpdate();
				        Alerta.alertInfo("DELETE SUCCESS", "Click OK To Continue");
				        refreshTableItem();
				        window.show();
				    } catch (SQLException eek) {
				        // Handle the exception
				    	System.out.print(eek);
				    }
			} else {
				Alerta.alertError("ERROR DELETE", "Invalid ID");
			}
		});
		
	// UPDATE ITEM
		updateItem.setOnAction(e -> {
			String itemName = itemNameTfM.getText();
			String[] parts = itemName.split(":");
			if(itemIdTfM.getText().equals("")) {
				Alerta.alertError("ERROR UPDATE" , "Select Item!");
			}else if(!itemIDValid(itemIdTfM.getText())) {
				Alerta.alertError("ERROR UPDATE" , "Invalid ID");
			}else if(itemNameTfM.getText().length() < 5 || itemNameTfM.getText().length() >  100) {
				Alerta.alertError("ERROR UPDATE" , "ItemName must be between 5 - 100 characters");
			} else if (parts.length < 2) {
				Alerta.alertError("ERROR UPDATE" , "Item Name must consist of at least 2 words containing the game name and item name then separated by (:)");
			} else if(itemDescTfM.getText().length() < 10 || itemDescTfM.getText().length() >  200) {
				Alerta.alertError("ERROR UPDATE" , "Item Description must be between 10 – 200 characters.");
			} else if(itemPriceSM.getValue() < 1) {
				Alerta.alertError("ERROR UPDATE", "Price must be greater than 0!");
			}else if(itemQtySM.getValue() < 1) {
				Alerta.alertError("ERROR UPDATE", "Quantity must be greater than 0!");
			}else {
				String sql = "UPDATE item SET itemName = ?, itemDescription = ?, price = ?, quantity = ? WHERE itemID = ?";
				try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/fteam", "root", "");
				        PreparedStatement statement = conn.prepareStatement(sql)) {
				        statement.setString(1, itemName);
				        statement.setString(2, itemDescTfM.getText());
				        statement.setInt(3, itemPriceSM.getValue());
				        statement.setInt(4, itemQtySM.getValue());
				        statement.setString(5, itemIdTfM.getText());
				        statement.executeUpdate();
				        Alerta.alertInfo("SUCCESS UPDATE", "Click OK to continue");
				        refreshTableItem();
				        window.show();
				    } catch (SQLException eek) {
				        // Handle the exception
				    	System.out.print(eek);
				    }
			}
		});
		
	// CLEAR FORM
		
	clearForm.setOnAction(e -> {
		itemIdTfM.setText("");
		itemNameTfM.setText("");
		itemDescTfM.setText("");
		itemPriceSM.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 2147483647, 0));
		itemQtySM.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 2147483647, 0));
		window.show();
	});
	
		clearFormI.setOnAction(e -> {
			itemIdTfI.setText("");
			itemNameTfI.setText("");
			itemDescTfI.setText("");
			itemPriceTfI.setText("");
			itemQtySI.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 2147483647, 0));
			window.show();
		});
		
	// INSERT ITEM
		
	insertItem.setOnAction(e -> {
		String itemName = itemNameTfM.getText();
		String[] parts = itemName.split(":");
		if(itemNameTfM.getText().length() < 5 || itemNameTfM.getText().length() >  100) {
			Alerta.alertError("ERROR INSERT", "ItemName must be between 5 - 100 characters");
		} else if (parts.length < 2) {
			Alerta.alertError("ERROR INSERT" , "Item Name must consist of at least 2 words containing the game name and item name then separated by (:)");
		} else if(itemDescTfM.getText().length() < 10 || itemDescTfM.getText().length() >  200) {
			Alerta.alertError("ERROR INSERT" , "Item Description must be between 10 – 200 characters.");
		} else if(itemPriceSM.getValue() < 1) {
			Alerta.alertError("ERROR INSERT" , "Price must be greater than 0!");
		}else if(itemQtySM.getValue() < 1) {
			Alerta.alertError("ERROR INSERT", "Quantity must be greater than 0!");
		} else {
			String sql = "INSERT INTO `item` (`itemID`, `itemName`, `itemDescription`, `price`, `quantity`) VALUES (?, ?, ?, ?, ?)";

			try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/fteam", "root", "");
			    PreparedStatement statement = conn.prepareStatement(sql)) {
			        statement.setString(1, itemIDGenerate());
			        statement.setString(2, itemName);
			        statement.setString(3, itemDescTfM.getText());
			        statement.setDouble(4, itemPriceSM.getValue());
			        statement.setInt(5, itemQtySM.getValue());
			        statement.executeUpdate();
			        refreshTableItem();
			        window.show();
			    } catch (SQLException eek) {
			        // Handle the exception
			    	System.out.print(eek);
			    }
		}
	});
	
	// REMOVE USER
	
		delteUser.setOnAction(e -> {
			try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/fteam", "root", "")) {
			    String sql = "DELETE FROM user WHERE userID = ?";
			    PreparedStatement statement = conn.prepareStatement(sql);
			    statement.setString(1, cuid);
			    statement.executeUpdate();
			    refreshTableUser();
			    window.show();
			} catch (SQLException eek) {
			    eek.printStackTrace();
			}
		});
		
	// ADD TO CART
		
		addToCart.setOnAction(e -> {
			if(itemIdTfI.getText().equals("")) {
				Alerta.alertError("ADD TO CART ERROR", "Select an item");
			} else if(itemQtySI.getValue() < 1) {
				Alerta.alertError("ADD TO CART ERROR", "Quantity input by the user must be more than 0!");
			}else if(itemQtySI.getValue()> cqty) {
				Alerta.alertError("ADD TO CART ERROR", "Quantity input by the user must be less than total Quantity of item.");
			}else if(itemQtySI.getValue()  + getQty(cuid, itemIdTfI.getText())> cqty) {
				Alerta.alertError("ADD TO CART ERROR", "Quantity in your cart plus your wish is out of our stock");
			} else {
				//new item in cart
				 if(itemCartExist(cuid, itemIdTfI.getText())) {
					 try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/fteam", "root", "")) {
				            String sql = "INSERT INTO cart (userID, itemID, quantity) VALUES (?, ?, ?)";
				            try (PreparedStatement statement = conn.prepareStatement(sql)) {
				                statement.setString(1, cuid);
				                statement.setString(2, itemIdTfI.getText());
				                statement.setInt(3, itemQtySI.getValue());
				                statement.executeUpdate();
				                Alerta.alertInfo("SUCCESS ADD TO CART", "Click Ok To Continue");
				            }
				        } catch (SQLException eek) {
				            System.err.println(eek);
				        }
				 } else {
					 int newQuantity = getQty(cuid, itemIdTfI.getText()) + itemQtySI.getValue();
					 try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/fteam", "root", "")) {
				            String sql = "UPDATE cart SET quantity = ? WHERE userID = ? AND itemID = ?";
				            try (PreparedStatement statement = conn.prepareStatement(sql)) {
				                statement.setInt(1, newQuantity);
				                statement.setString(2,cuid);
				                statement.setString(3,itemIdTfI.getText());
				                int rowsAffected = statement.executeUpdate();
				                System.out.println(rowsAffected + " row(s) affected");
				                Alerta.alertInfo("SUCCESS ADD TO CART", "Click Ok To Continue");
				            }
				        } catch (SQLException ee) {
				            System.err.println(ee);
				        }
				 }
			}
		});
		
		table.setOnMouseClicked(event -> {
		    User selectedUser = table.getSelectionModel().getSelectedItem();
		    if (selectedUser != null) {
		        cuid = selectedUser.getUserid();
		        userIdTfA.setText(selectedUser.getUserid()); 
				userNameTfA.setPromptText(selectedUser.getUsername());
				passwordTfA.setText(selectedUser.getPassword());
				emailTfA.setText(selectedUser.getEmail()); 
				phoneTfA.setText(selectedUser.getPhonenumber()); 
				ageSA.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(15, 70, Integer.parseInt(selectedUser.getAge())));
				if(selectedUser.getGender().equals("Male")){
					maleRA.setSelected(true);
				} else {
					femaleRA.setSelected(true);
				}
				window.show();
		    }
		});
		
		sellTable.setOnMouseClicked(event -> {
			SellItem selectedItem = sellTable.getSelectionModel().getSelectedItem();
		    if (selectedItem != null) {
		    	itemIdTfI.setText(selectedItem.getItemID());
		    	itemNameTfI.setText(selectedItem.getItemName());
		    	itemDescTfI.setText(selectedItem.getItemDescription());
		    	itemPriceTfI.setText(selectedItem.getPrice());
		    	cqty = Integer.parseInt(selectedItem.getQuantity());
				window.show();
		    }
		});
		
		itemTable.setOnMouseClicked(event -> {
		    Item selectedItem = itemTable.getSelectionModel().getSelectedItem();
		    if (selectedItem != null) {
		    	itemIdTfM.setText(selectedItem.getItemID());
		    	itemNameTfM.setText(selectedItem.getItemName());
		    	itemDescTfM.setText(selectedItem.getItemDescription());
		    	itemPriceSM.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 2147483647, selectedItem.getQuantity()));
		    	itemQtySM.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 2147483647, selectedItem.getQuantity()));
				window.show();
		    }
		});		
	
		loginBL.setOnAction(e ->{
			if(usernameTf.getText().equals("") || passwordTf.getText().equals("")) {
				Alerta.alertError("Login Failed", "Username or Password can't be null!!");
			} else {
				try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/fteam", "root", "")) {
				    Statement stmt = conn.createStatement();
				    ResultSet rs = stmt.executeQuery("SELECT * FROM user");
				    while (rs.next()) {
				    	if(usernameTf.getText().equals(rs.getString("username")) && passwordTf.getText().equals(rs.getString("password"))) {
				    		Alerta.alertInfo("Berhasil Login", "Click OK to Continue");
				    		if(rs.getString("role").equals("admin")) {
				    			cuid = rs.getString("userID");
				    			Label welcome = new Label("Welcome " + cuid);
				    			welcome.setPadding(new Insets(200));
				    			adminLayout.setCenter(welcome);
				    			window.setTitle("Admin Page");
				    			window.setScene(adminScene);
				    		} else {
				    			cuid = rs.getString("userID");
				    			Label welcome = new Label("Welcome " + cuid);
				    			userLayout.setCenter(welcome);
				    			window.setTitle("User Page");
				    			window.setScene(userScene);
				    		}
				    	}
				    }
				} catch (SQLException er) {
					System.out.println(er);
				}
			}
		});
		
		registerR.setOnAction(e->{
			
			if(userNameTfR.getText().length() <5 || userNameTfR.getText().length() > 20) {
				Alerta.alertError("Registration Error" , "Username must be between 5 - 20 characters");
			} else if(passwordTfR.getText().length() <5 || passwordTfR.getText().length() > 20) {
				Alerta.alertError("Registration Error" , "Password must be between 5 - 20 characters");
			} else if(!passwordTfR.getText().matches("^[a-zA-Z0-9]*$")) {
				Alerta.alertError("Registration Error" , "Password must be alphanumberic.");
			} else if(!emailTfR.getText().matches("^(?!.*@.*@)[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.com$")) {
				Alerta.alertError("Registration Error" , "Email email must consist of @ character, @ character must not be in front and must end with .com.");
			} else if( phoneTfR.getText().length() <9 || phoneTfR.getText().length() > 12) {
				Alerta.alertError("Registration Error" , "Phone Number must be between 9 – 12 characters.");
			} else if(gender.getSelectedToggle() == null) {
				Alerta.alertError("Registration Error" , "Gender must be selected, either ‘Male’ or ‘Female’.");
			} else {
				String genderx = "";
				if(maleRR.isSelected()) {
					genderx= "Male";
				} else {
					genderx= "Female";
				}
				try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/fteam", "root", "")) {
				    String sql = "INSERT INTO user (userID, username, password, gender, email, phoneNumber, age, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
				    PreparedStatement statement = conn.prepareStatement(sql);
				    statement.setString(1, userIDGenerate());
				    statement.setString(2, userNameTfR.getText());
				    statement.setString(3, passwordTfR.getText());
				    statement.setString(4, genderx);
				    statement.setString(5, emailTfR.getText());
				    statement.setString(6, phoneTfR.getText());
				    statement.setInt(7, ageSR.getValue());
				    statement.setString(8, "user");
				    statement.executeUpdate();
				    userIdTfR.setText("");
					userNameTfR.setText("");
					passwordTfR.setText("");
					emailTfR.setText("");
					phoneTfR.setText("");
				    window.setScene(loginScene);
				} catch (SQLException eek) {
				    eek.printStackTrace();
				}
			}
		});	
		
	// loginPage->RegistrationPage
		registerBL.setOnAction(e->{
			window.setTitle("Register");
			window.setScene(registerScene);
		});
		
	// RegisterPage->loginPage
		loginR.setOnAction(e->{
			window.setTitle("Login");
			window.setScene(loginScene);
		});
		
	// adminPage->LoginPage
		logOutAdmin.setOnAction(e ->{
			window.setTitle("Login");
			window.setScene(loginScene);
		});
		
	// userPage->loginPage
		logOutUser.setOnAction(e ->{
			window.setTitle("Login");
			window.setScene(loginScene);
		});
		
	// userpage-itemMarket
		itemMarket.setOnAction(e -> {
			refreshTableSell();
			userLayout.setCenter(itemMarketGrid);
			window.show();
		});
			
	// userpage-itemMarket
		CartItem.setOnAction(e -> {
			refreshTableCart(cuid);
			userLayout.setCenter(cartItemGrid);
			window.show();
		});
		
	// userpage-transactionhistory
		transactionHistory.setOnAction(e -> {
			userLayout.setCenter(transactionHistoryGrid);
			window.show();
		});
		
		window.setTitle("Login");
		window.setScene(loginScene);
		window.setResizable(true);
		window.show();
	}

	
	private Integer getQty(String userID, String itemID) {
		 try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/fteam", "root", "")) {
	            String sql = "SELECT quantity FROM cart WHERE userID = ? AND itemID = ?";
	            try (PreparedStatement statement = conn.prepareStatement(sql)) {
	                statement.setString(1, userID);
	                statement.setString(2, itemID);
	                try (ResultSet resultSet = statement.executeQuery()) {
	                    if (resultSet.next()) {
	                        int quantity = resultSet.getInt("quantity");
	                        return quantity;
	                    }
	                }
	            }
	        } catch (SQLException e) {
	            System.err.println(e);
	        }
		 return 0;
	}

	private boolean itemCartExist(String cuid2, String itemID) {
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/fteam", "root", "")) {
            String sql = "SELECT COUNT(*) FROM cart WHERE userID = ? AND itemID = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, cuid2);
                statement.setString(2, itemID);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        if (count > 0) {
                            return false;
                        } else {
                            return true;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
		return false;
	}

	public void refreshTableSell() {
		sellTable.getItems().clear();
		Connection connection = null;
		try {
		    connection = DriverManager.getConnection("jdbc:mysql://localhost/fteam", "root", "");
		} catch (SQLException e) {
			System.out.println("koneksi gagal");
		}
		data =FXCollections.observableArrayList();
		try (Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM item where quantity > 0")) {
			    while (rs.next()) {
			    	data.add(new SellItem(rs.getString("itemID"), rs.getString("itemName"), rs.getString("itemDescription"), rs.getString("price"), rs.getString("quantity")));
			    }
			    sellTable.setItems(data);
			} catch (SQLException e) {
		}
	}

	private boolean itemIDValid(String itemID) {
		// TODO Auto-generated method stub
		String sql = "SELECT COUNT(*) FROM item WHERE itemID = ?";
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/fteam", "root", "");
		        PreparedStatement statement = conn.prepareStatement(sql)) {
		    statement.setString(1, itemID);
		    try (ResultSet rs = statement.executeQuery()) {
		        if (rs.next()) {
		            int count = rs.getInt(1);
		            if (count > 0) {
		            	return true;
		            } else {
		                return false;
		            }
		        }
		    }
		} catch (SQLException e) {
		    // Handle the exception
		}
		return false;
	}

	public void refreshTableUser() {
		table.getItems().clear();
		Connection connection = null;
		try {
		    connection = DriverManager.getConnection("jdbc:mysql://localhost/fteam", "root", "");
		} catch (SQLException e) {
			System.out.println("koneksi gagal");
		}
		users =FXCollections.observableArrayList();
		try (Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM user where role = 'user'")) {
			    while (rs.next()) {
			        users.add(new User(rs.getString("userID"), rs.getString("username"), rs.getString("password"), rs.getString("email"), rs.getString("phoneNumber"), rs.getString("age"), rs.getString("gender")));
			    }
			    table.setItems(users);
			} catch (SQLException e) {
		}
	}
	
	public void refreshTableCart(String cuid2) {
		cartTable.getItems().clear();
		Connection connection = null;		
		try {
		    connection = DriverManager.getConnection("jdbc:mysql://localhost/fteam", "root", "");
		} catch (SQLException e) {
			System.out.println("koneksi gagal");
		}
		dataCart =FXCollections.observableArrayList();
		try (Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery("select * from cart INNER JOIN item on item.itemID = cart.itemID and cart.userID ='" + cuid2 + "'")) {
			    while (rs.next()) {
			    	int totalPrice = Integer.parseInt(rs.getString("quantity")) * Integer.parseInt(rs.getString("price"));
			    	dataCart.add(new CartItem(rs.getString("itemID"), rs.getString("itemName"), rs.getString("itemDescription"), rs.getString("price"), rs.getString("quantity"), Integer.toString(totalPrice)));
			    }
			    cartTable.setItems(dataCart);
			} catch (SQLException e) {
		}
	}
	
	public void refreshTableItem() {
		itemTable.getItems().clear();
		Connection connection = null;
		try {
		    connection = DriverManager.getConnection("jdbc:mysql://localhost/fteam", "root", "");
		} catch (SQLException e) {
			System.out.println("koneksi gagal");
		}
		newItems =FXCollections.observableArrayList();
		try (Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM item")) {
			    while (rs.next()) {
			    	newItems.add(new Item(rs.getString("itemID"), rs.getString("itemName"), rs.getString("itemDescription"), rs.getDouble("price"), rs.getInt("quantity")));
			    }
			    itemTable.setItems(newItems);
			} catch (SQLException e) {
		}
	}
	
	public void refreshTableTransaction() {
		transactionTable.getItems().clear();
		Connection connection = null;
		try {
		    connection = DriverManager.getConnection("jdbc:mysql://localhost/fteam", "root", "");
		} catch (SQLException e) {
			System.out.println("koneksi gagal");
		}
		transactions =FXCollections.observableArrayList();
		try (Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM transaction JOIN transactiondetail ON transaction.transactionID = transaction.transactionID JOIN item ON transactiondetail.itemID = item.itemID JOIN user ON transaction.userID = user.userID;")) {
			    while (rs.next()) {
			    	
			    	transactions.add(new Transaction(rs.getString("transactionID"), rs.getString("username"), rs.getString("itemName"), rs.getString("itemDescription"), rs.getString("price"), rs.getString("quantity"), rs.getString("price"),  rs.getDate("transactionDate").toLocalDate()));
			    }
			    transactionTable.setItems(transactions);
			} catch (SQLException e) {
		}
	}
	
	public String userIDGenerate() {
		String newUserIDs = "";
		String sql = "SELECT userID FROM user ORDER BY userID DESC LIMIT 1";
		try (Connection conn = DriverManager.getConnection("jdbc:your_database_url", "username", "password");
		        Statement statement = conn.createStatement();
		        ResultSet rs = statement.executeQuery(sql)) {
		    if (rs.next()) {
		        String lastUserID = rs.getString("userID");
		        String numberString = lastUserID.substring(2);
		        int number = Integer.parseInt(numberString);
		        number += 1;
		        String newNumberString = Integer.toString(number);
		        String zeroPadding = "";
		        for(int i = 0; i < 3 - newNumberString.length(); i++) {
		            zeroPadding += "0";
		        }
		        newNumberString = zeroPadding + newNumberString;
		        String newUserID = "US" + newNumberString;
		        return newUserID;
		    }
		} catch (SQLException e) {
		    // Handle the exception
		}
		return newUserIDs;
	}
	
	public String itemIDGenerate() {
		String sql = "SELECT itemID FROM item ORDER BY itemID DESC LIMIT 1";
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/fteam", "root", "");
		        Statement statement = conn.createStatement();
		        ResultSet rs = statement.executeQuery(sql)) {
		    if (rs.next()) {
		        String lastItemID = rs.getString("itemID");
		        String numberString = lastItemID.substring(2);
		        int number = Integer.parseInt(numberString);
		        number += 1;
		        String newNumberString = Integer.toString(number);
		        String zeroPadding = "";
		        for(int i = 0; i < 3 - newNumberString.length(); i++) {
		            zeroPadding += "0";
		        }
		        newNumberString = zeroPadding + newNumberString;
		        String newItemID = "IT" + newNumberString;
		        return newItemID;
		    }
		} catch (SQLException e) {
		    // Handle the exception
		}
		return "";
	}
	
	public String transactionIDGenerate() {
		String a = "";
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/fteam", "root", "");
			     Statement statement = connection.createStatement()) {

			    String query = "SELECT transactionID FROM transaction ORDER BY transactionID DESC LIMIT 1";
			    ResultSet resultSet = statement.executeQuery(query);

			    if (resultSet.next()) {
			        String lastTransactionID = resultSet.getString("transactionID");
			        int nextTransactionNumber = Integer.parseInt(lastTransactionID.substring(2)) + 1;
			        String nextTransactionID = "TR" + String.format("%03d", nextTransactionNumber);
			        return nextTransactionID;
			    }
			} catch (SQLException e) {
			    e.printStackTrace();
			}
		return a;
	}
	
	public boolean userIdcheck(String id) {
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/fteam", "root", "");
			     Statement stmt = connection.createStatement();
			     ResultSet rs = stmt.executeQuery("SELECT * FROM user")) {
			    while (rs.next()) {
			        String column1 = rs.getString("id");
			        if(id.equals(column1)) {
			            return true;
			        }
			    }
			} catch (SQLException e) {
			    System.out.println("koneksi gagal");
			}
			return false;
	}
	
	
}
