import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class AddressBook extends Application {
    enum Countries {
        UK,
        US,
        DE,
        CN
    }

    // The contact model
    ContactsModel contactsModel = new ContactsModel();
    // The TabPane and Tabs
    TabPane tabPane;
    Tab entryTab;
    Tab listTab;
    Tab searchTab;

    public static void main(String[] args) {
        launch(args);
    }

    private MenuBar setupMenu() {
        MenuBar menuBar = new MenuBar();

        // FILE
        Menu file = new Menu("File");
        MenuItem save = new MenuItem("Save");
        MenuItem load = new MenuItem("Load");
        file.getItems().addAll(save, load);

        // VIEW
        Menu view = new Menu("View");
        MenuItem entry = new MenuItem("Entry");
        entry.setOnAction(e -> {
            tabPane.getSelectionModel().select(entryTab);
        });
        MenuItem list = new MenuItem("List");
        list.setOnAction(e -> {
            tabPane.getSelectionModel().select(listTab);
        });
        MenuItem search = new MenuItem("Search");
        search.setOnAction(e -> {
            tabPane.getSelectionModel().select(searchTab);
        });
        view.getItems().addAll(entry, list, search);

        menuBar.getMenus().addAll(file, view);

        return menuBar;
    }

    private Tab setupEntryTab() {
        Tab entry = new Tab("Entry");
        HBox buttons = new HBox();
        TilePane tilePane = new TilePane(10, 4);
        BorderPane borderPane = new BorderPane();

        // Setting up the nodes to be added to the tile pane
        Label nameLabel = new Label("Name:");
        Label numberLabel = new Label("Number:");
        Label addressLabel = new Label("Address:");
        Label countryLabel = new Label("Country:");
        TextField nameField = new TextField();
        TextField numberField = new TextField();
        TextField addressField = new TextField();
        ComboBox<Countries> countryBox = new ComboBox();
        countryBox.getItems().addAll(Countries.values());
        countryBox.setValue(Countries.UK);
        tilePane.setPrefColumns(2);
        tilePane.getChildren().addAll(
                nameLabel,
                nameField,
                numberLabel,
                numberField,
                addressLabel,
                addressField,
                countryLabel,
                countryBox
        );

        // Adding the buttons
        Button entryClear = new Button("Clear");
        entryClear.setOnAction(e -> {
            Control.clearText(tilePane);
        });
        Button entryEdit = new Button("Edit");
        Button entryAdd = new Button("Add");
        entryAdd.setOnAction(e -> {
            // Check for empty required fields
            if (Control.emptyTextInput(tilePane)) {
                showAlert("Empty Inputs",
                        "Please fill in all required entries");
                return;
            }
            // Check for duplicate name/number entries
            else if (contactsModel.exists(nameField.getText(), numberField.getText())) {
                showAlert("Duplicate Input",
                        "An entry with that name and number already exists");
                return;
            }
            // Create our new contact
            Contact newContact = new Contact(
                    nameField.getText(),
                    numberField.getText(),
                    addressField.getText(),
                    countryBox.getValue()
            );
            contactsModel.add(newContact);
        });
        Button entryDelete = new Button("Delete");
        entryDelete.setOnAction(event -> {
            // Remove the matching entry from the data
            if (!contactsModel.remove(nameField.getText(), numberField.getText())) {
                showAlert("No Such Entry",
                        "Could not find an entry matching than name and number");;
            }
        });
        buttons.getChildren().addAll(
                entryClear,
                entryEdit,
                entryAdd,
                entryDelete
        );
        buttons.setAlignment(Pos.CENTER);

        // Putting it all together
        borderPane.setCenter(tilePane);
        borderPane.setBottom(buttons);
        entry.setContent(borderPane);

        return entry;
    }

    private Tab setupListTab() {
        Tab list = new Tab("List");
        VBox vBox = new VBox();
        TableView table = contactsModel.table();
        Button editButton = new Button("Edit");

        // Adding the event for the button
        editButton.setOnAction(event -> {
            Contact selected = (Contact) table.getSelectionModel().getSelectedItem();
            System.out.println(selected.getName());
        });

        vBox.getChildren().addAll(table, editButton);
        list.setContent(vBox);
        return list;
    }


    private Tab setupSearchTab() {
        Tab search = new Tab("Search");
        TilePane tilePane = new TilePane(10, 4);
        VBox vBox = new VBox();

        // Nodes to go in the TilePane
        Label nameLabel = new Label("Name:");
        Label numberLabel = new Label("Number:");
        TextField nameField = new TextField();
        TextField numberField = new TextField();
        Button clearButton = new Button("Clear");
        Button searchButton = new Button("Search");

        // Adding nodes to the TilePane
        tilePane.setPrefColumns(2);
        tilePane.getChildren().addAll(
                nameLabel,
                nameField,
                numberLabel,
                numberField,
                clearButton,
                searchButton
        );

        vBox.getChildren().addAll(tilePane, contactsModel.table());
        search.setContent(vBox);
        return search;
    }

    @Override
    public void start(Stage primaryStage) {
        //FlowPane root = new FlowPane(); // The root node of our scene
        BorderPane root = new BorderPane(); // The border pane, to which everything will be added
        Scene scene = new Scene(root); // The scene
        tabPane = new TabPane();

        // Instancing and adding the tabs
        entryTab = setupEntryTab();
        listTab = setupListTab();
        searchTab = setupSearchTab();
        tabPane.getTabs().addAll(entryTab, listTab, searchTab);

        root.setCenter(tabPane);
        root.setTop(setupMenu());

        //root.getChildren().add(root);

        primaryStage.setTitle("Address Book");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAlert(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(text);
        alert.showAndWait();
    }
}
