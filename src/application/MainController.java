package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainController implements Initializable{
	private Parent root;
	private Stage stage;
	private Scene scene;
	private Contact currentContact = new Contact();
	private ArrayList<Contact> contacts = new ArrayList<Contact>();
	private ObservableList observableList = FXCollections.observableArrayList();
    private final String LIST_URL = "http://addressbook-env-1.eba-um8nxvxt.us-east-1.elasticbeanstalk.com/address/listservice";
    private OkHttpClient client = new OkHttpClient();
	
	@FXML
	private ListView<Contact> contactListView;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		getContacts();	
		contactListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Contact>() {

			@Override
			public void changed(ObservableValue<? extends Contact> observable, Contact oldValue, Contact newValue) {
				currentContact = contactListView.getSelectionModel().getSelectedItem();
			}
		});
	}
	
	public void getContacts() {
        Request request = new Request.Builder()
                .url(LIST_URL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            	System.out.println("Unable to get request.");
                e.printStackTrace();
            }
            
            @Override
            public void onResponse(Call call, Response response) throws IOException {
            	if(response.isSuccessful()) {
                	  String listResponse = response.body().string();
                	  
                	  Platform.runLater(new Runnable() {
						@Override
						public void run() {
							try {
		                		  JSONObject result = new JSONObject(listResponse);
		                          for(int i = 0; i < result.length(); i++) {
		                              JSONObject data = result.getJSONObject("contact" + (i+1));
		                              String firstName = data.getString("first_name");
		                              String lastName = data.getString("last_name");
		                              String address = data.getString("address");
		                              String phone = data.getString("phone");
		                              int id = data.getInt("id");

		                              contacts.add(new Contact(firstName, lastName, address, phone, id));
		                          }

		                          contactListView.getItems().addAll(contacts);
		                      } catch(JSONException e) {
		                    	  System.out.println("Error occurred with JSON object.");
		                		  e.printStackTrace();
		                	  }
						} 
                	  });	 
                }
            }
        });
	}
	
	public void switchToAdd(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AddScene.fxml"));
		root = loader.load();
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToEdit(ActionEvent event) throws IOException {	
		FXMLLoader loader = new FXMLLoader(getClass().getResource("EditScene.fxml"));
		root = loader.load();
		
		EditController editController = loader.getController();
		editController.getContact(currentContact);
		
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
