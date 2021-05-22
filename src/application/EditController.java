package application;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONObject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditController {
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	private int id;
	final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    final String EDIT_URL = "http://addressbook-env-1.eba-um8nxvxt.us-east-1.elasticbeanstalk.com/address/editservice";
    final String DELETE_URL = "http://addressbook-env-1.eba-um8nxvxt.us-east-1.elasticbeanstalk.com/address/deleteservice";
    private OkHttpClient client = new OkHttpClient();
	
	@FXML
	TextField firstNEntry;
	@FXML
	TextField lastNEntry;
	@FXML
	TextField addressEntry;
	@FXML
	TextField phoneEntry;
	@FXML
	Label message;

	public void getContact(Contact c) {
		firstNEntry.setText(c.getFirstName());
		lastNEntry.setText(c.getLastName());
		addressEntry.setText(c.getAddress());
		phoneEntry.setText(c.getPhone());
		id = c.getId();
	}
	
	public void editContact(ActionEvent event) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("first_name", firstNEntry.getText());
        params.put("last_name", lastNEntry.getText());
        params.put("address", addressEntry.getText());
        params.put("phone", phoneEntry.getText());
        params.put("id", Integer.toString(id));

        JSONObject parameter = new JSONObject(params);

        executeTask(EDIT_URL, parameter, JSON);
        
        message.setText("Contact successfully updated.");
    }

    public void deleteContact(ActionEvent event) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("id", Integer.toString(id));

        JSONObject parameter = new JSONObject(param);

        executeTask(DELETE_URL, parameter, JSON);
        
        message.setText("Contact successfully deleted.");
    }
	
    private void executeTask (String url, JSONObject parameter, MediaType m) {
    	ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                RequestBody body = RequestBody.create(parameter.toString(), m);
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code: " + response);
                } catch (IOException e) {
                	System.out.println("Unable to post request.");
                    e.printStackTrace();
                }
            }
        });

        executor.shutdown();
    }
	
	public void switchToMain(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("ContactList.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
