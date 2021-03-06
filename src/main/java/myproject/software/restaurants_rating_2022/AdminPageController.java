package myproject.software.restaurants_rating_2022;


import com.jfoenix.animation.alert.CenterTransition;
import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;

import java.io.File;
import java.net.URL;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class AdminPageController implements Initializable {
    @FXML
    private ImageView exit,menu,userimg;
    @FXML VBox VBoxOwners,BOXTEXT ;
    @FXML
    private AnchorPane pane1,pane2,paneRes;
    @FXML JFXButton report;
    @FXML AnchorPane message;
    @FXML AnchorPane reportAnchor;
    @FXML VBox itemRes,vboxmsg;
    @FXML HBox box2;
    @FXML TextArea textArea1;



    String x="3";
    String t;

    ObservableList<String> names = FXCollections.observableArrayList();
    ObservableList<String> names2 = FXCollections.observableArrayList();
    ObservableList<String> data = FXCollections.observableArrayList();
    ObservableList<String> data2 = FXCollections.observableArrayList();
    ObservableList <Integer> dataid = FXCollections.observableArrayList();
    ObservableList <Integer> ownerIDMsg = FXCollections.observableArrayList();
    ObservableList<String> MSGINFO = FXCollections.observableArrayList();
    ObservableList <Integer> testid = FXCollections.observableArrayList();
    ObservableList<String> testname = FXCollections.observableArrayList();

    @FXML ListView<String>  listRes=new ListView<>(names);
    @FXML  Label ResSelected,userlabel;
    @FXML TextField text;

    @FXML ListView<String>  listRes2=new ListView<>(names2);
    @FXML ListView l2;
    boolean flage=false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

//        if(sendBUTTON.isHover()==true){
//            sendBUTTON.setStyle("-fx-background-color:   #c6ecd9;");
//        }


        exit.setOnMouseClicked(event ->{
            System.exit(0);
        } );
        pane1.setVisible(false);
//

        try {
            conection conClass=new conection();
            Connection c=conClass.getConnection();
            Statement  s = c.createStatement();
            String sql="select res_name from restaurants";
            ResultSet r=s.executeQuery(sql);

            while (r.next()) {
                String ss=r.getString("res_name");
                    names.add(ss);
                    listRes.setItems(data);
                      listRes.setCellFactory(ComboBoxListCell.forListView(names));
                      listRes.setItems(names);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        /* *************** select item from List View  ************** */

        listRes.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends String> ov, String old_val,
                 String new_val) -> {
                    // System.out.println(new_val);
                    ResSelected.setText(new_val);
                });
      /* ********************************************************** */


        FadeTransition fadeTransition=new FadeTransition(Duration.seconds(0.5),pane1);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();

        TranslateTransition translateTransition=new TranslateTransition(Duration.seconds(0.5),pane2);
        translateTransition.setByX(-600);
        translateTransition.play();


        menu.setOnMouseClicked(event ->{
            menu.setRotate(90);
            pane1.setVisible(true);
            FadeTransition fadeTransition1=new FadeTransition(Duration.seconds(0.5),pane1);
            fadeTransition1.setFromValue(0);
            fadeTransition1.setToValue(0.20);
            fadeTransition1.play();

            TranslateTransition translateTransition1=new TranslateTransition(Duration.seconds(0.5),pane2);
            translateTransition1.setByX(+600);
            translateTransition1.play();


        } );
        pane1.setOnMouseClicked(event ->{

            FadeTransition fadeTransition1=new FadeTransition(Duration.seconds(0.5),pane1);
            fadeTransition1.setFromValue(0.20);
            fadeTransition1.setToValue(0);
            fadeTransition1.play();

            fadeTransition1.setOnFinished(event1 -> {
                pane1.setVisible(false);


            });

            TranslateTransition translateTransition1=new TranslateTransition(Duration.seconds(0.5),pane2);
            translateTransition1.setByX(-600);
            translateTransition1.play();
            menu.setRotate(180);
        });
/* ***************  restaurant owner data base for messages page   *************** */
            int id;
        try {
            conection conClass2=new conection();
            Connection c=conClass2.getConnection();
            Statement  s2 = c.createStatement();
            String sql2="select * from restaurants_owners";
            ResultSet r2=s2.executeQuery(sql2);


            while (r2.next()) {
                String ss2=r2.getString("owner_name");
                String email=r2.getString("owner_email");
                id=r2.getInt("owner_id");
                names2.add(ss2);
                data2.add(email);
                dataid.add(id);
               // listRes2.setItems(data2);
                listRes2.setCellFactory(ComboBoxListCell.forListView(names2));
                listRes2.setItems(names2);



            }


            Image image = new Image(getClass().getResourceAsStream("/image/user.png"));


            listRes2.getSelectionModel().selectedItemProperty().addListener(
                    (ObservableValue<? extends String> ov, String old_val,
                     String new_val2) -> {
                        flage=false;
                        int mid;
                        String str;
                        int fid = 0;
                        vboxmsg.getChildren().clear();
                        ownerIDMsg.clear();
                        MSGINFO.clear();
                        try {
                            conection conClassq=new conection();
                            Connection cq=conClassq.getConnection();
                            Statement  sq = cq.createStatement();
                            String sqlq="select * from restaurants_owners";
                            ResultSet rq=sq.executeQuery(sqlq);
                            while (rq.next()) {
                                mid=rq.getInt("owner_id");
                                str=rq.getString("owner_name");
                                testid.add(mid);
                                testname.add(str);
                              //  String mmm=rq.getString("msg_info");
                              //  int in=rq.getInt("owner_id");
                              //  ownerIDMsg.add(in);
                              //  MSGINFO.add(mmm);
                            }
                            for(int i=0;i< testid.size();i++){
                                if(testname.get(i).equals(new_val2)){
                                    fid=testid.get(i);
                                }
                            }




                            conection conClass3=new conection();
                            Connection c3=conClass3.getConnection();
                            Statement  s3 = c3.createStatement();
                            String sql3="select * from msgs";
                            ResultSet r3=s3.executeQuery(sql3);

                            while (r3.next()) {
                                String mmm=r3.getString("msg_info");

                                int in=r3.getInt("owner_id");
                                if(in==fid){
                                    ownerIDMsg.add(in);
                                    MSGINFO.add(mmm);
                                }


                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                            
                            int idd = 0;

                            for(int i=0; i<names2.size();i++){

                                if(new_val2 ==names2.get(i)){
                                    userimg.setImage(image);
                                    userlabel.setText(data2.get(i));
                                        idd=dataid.get(i);
                                }


               }
                        for(int g=0;g< MSGINFO.size();g++){
                           // if(idd==ownerIDMsg.get(g)){
                                String w=MSGINFO.get(g);
                                Label k=new Label();
                                k.setText(w);
                                vboxmsg.getChildren().add(k);

                            }





        });


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        contentarea.getChildren().removeAll();
    }

   @FXML
    void genreort(MouseEvent event) throws IOException {
       message.setVisible(false);
       reportAnchor.setVisible(true);
       contentarea.getChildren().removeAll();


    }
    public void massageclicked(MouseEvent mouseEvent) {
        message.setVisible(true);
        reportAnchor.setVisible(false);
        contentarea.getChildren().removeAll();

    }
    public void smallClose(MouseEvent mouseEvent) {
        message.setVisible(false);
        reportAnchor.setVisible(false);
        contentarea.getChildren().removeAll();
    }

    public void smallClose2(MouseEvent mouseEvent) {
        message.setVisible(false);
        reportAnchor.setVisible(false);
        contentarea.getChildren().removeAll();
        //flage=false;
    }


    public void keyRes(KeyEvent keyEvent) {

    }


    public void searchhandel(MouseEvent mouseEvent) {

//        FilteredList<String> filteredData = new FilteredList<>(names, s -> true);
//        text.textProperty().addListener(obs->{
//            String filter = text.getText();
//            if(filter == null || filter.length() == 0) {
//                filteredData.setPredicate(s -> true);
//            }
//            else {
//                filteredData.setPredicate(s -> s.contains(filter));
//            }
//        });
//
//        listRes.setItems(filteredData);
        t=text.getText();
        ObservableList<String> namesss = FXCollections.observableArrayList();
        for(int i=0;i<names.size();i++){
            if(t==names.get(i)){
                namesss.add(t);
                listRes.setItems(namesss);

            }
            else{

                listRes.setItems(names);

            }
        }



    }

    public void serchRes(ActionEvent actionEvent) {
        t= text.getText();


    }


    public void myListMouusClicked(MouseEvent mouseEvent) {

        
    }

    public void searchTextKey(KeyEvent keyEvent) {
    }
    Alert a = new Alert(Alert.AlertType.NONE);
    public void getReport(MouseEvent mouseEvent) {
        if(ResSelected.getText()=="" ||ResSelected.getText()==" " ||ResSelected.getText()==null){
            a.setAlertType(Alert.AlertType.CONFIRMATION);
            a.setHeaderText("you must select a valid Restaurant ");
            a.show();
        }
        else{
            // report code => get report by restaurant name from => ResSelected.getText()

        }
    }
    Window stage=null;
      //  fileChooser.showOpenDialog(stage);
    public void getfile(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        File file =fileChooser.showSaveDialog(stage) ;
        //fileChooser.setTitle("Open Resource File");
        System.out.println(file);



    }
    int count=1;
    @FXML TextField typemsg;
    @FXML ScrollPane s;
    Alert a2 = new Alert(Alert.AlertType.NONE);
    String msg,msgad;
    @FXML Button sendBUTTON;

    public void sendMsg(MouseEvent mouseEvent) throws SQLException {
      //  vboxmsg.getChildren().clear();
        String ownerlabel=userlabel.getText();
        if(ownerlabel=="" ||ownerlabel==" " ){
            Alert a2 = new Alert(Alert.AlertType.NONE);
            a2.setAlertType(Alert.AlertType.CONFIRMATION);
            a2.setHeaderText("you must select the owner to send a message  ");
            a2.show();

        }
        else if(typemsg.getText()=="type here ..." || typemsg.getText()=="" || typemsg.getText()==" " ){
            Alert a2 = new Alert(Alert.AlertType.NONE);
            a2.setAlertType(Alert.AlertType.CONFIRMATION);
            a2.setHeaderText("you must type message to send it");
            a2.show();

        }

        else{
            if(flage==false){
                Separator sep=new Separator();
                //vboxmsg.getChildren().add(sep);
                Label LAB=new Label("new message");
                sep.setStyle("-fx-border-color: red;-fx-border-style: solid none none none;");
                //ep.setStyle("-fx-border-style: solid none none none;");
                LAB.setStyle("-fx-background-color:  #ccddff;");

                vboxmsg.getChildren().add(sep);
                vboxmsg.getChildren().add(LAB);

            }

            int finalid=0;
           // Separator sep=new Separator();

           for(int k=0;k<data2.size();k++){
               if(ownerlabel==data2.get(k)) {

                   msg = typemsg.getText();
                   if (msg.equals("") || msg.equals(" ")) {

                   } else {

                       int id2 = dataid.get(k);
                       conection conClass2 = new conection();
                       Connection c2 = conClass2.getConnection();
                       Statement st = c2.createStatement();
                       Statement st2 = c2.createStatement();
                       String sql22 = "select msg_id from msgs";
                       ResultSet r3 = st2.executeQuery(sql22);
                       while (r3.next()) {
                           int f = r3.getInt("msg_id");
                           finalid = f;
                       }
                       finalid++;
                       msgad="admin: "+msg;
                       st.executeUpdate("INSERT INTO msgs " + "VALUES ('" + finalid + "','" + id2 + "',1,'" + msgad+ "')");
                       c2.close();
                       typemsg.setText("");
                       Label l = new Label();

                       l.setText("admin: "+msg);
                       vboxmsg.getChildren().add(l);
                       flage = true;


//                   Alert a2 = new Alert(Alert.AlertType.NONE);
//                   a2.setAlertType(Alert.AlertType.CONFIRMATION);
//                   a2.setHeaderText("send sucsses");
//                   a2.show();


                       count++;

                   }
               }
           }


        }


    }

    public void onactionsearch(ActionEvent event) {
//        t=text.getText();
//        for(int i=0;i<names.size();i++){
//            if(t==names.get(i)){
//                namesss.add(t);
//                listRes.setItems(namesss);
//
//            }
//            else{
//
//                 listRes.setItems(names);
//
//            }
//        }
    }

    /////////////////////////////////--Aseel--////////////////////////////////////////////
    @FXML
    private AnchorPane contentarea;
    @FXML
    private JFXButton homepage;
    public void allrestaurant(ActionEvent actionEvent) throws IOException {
        message.setVisible(false);
        reportAnchor.setVisible(false);
        Parent fxml = FXMLLoader.load(getClass().getResource("AllRestaurant.fxml"));
        contentarea.getChildren().removeAll();
        contentarea.getChildren().setAll(fxml);
    }
    public void allrestaurant() throws IOException{
        pane1.setVisible(true);

        FadeTransition fadeTransition=new FadeTransition(Duration.seconds(0.5),pane1);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();

        TranslateTransition translateTransition=new TranslateTransition(Duration.seconds(0.5),pane2);
        translateTransition.setByX(-1);
        translateTransition.play();

        Parent fxml = FXMLLoader.load(getClass().getResource("AllRestaurant.fxml"));
        contentarea.getChildren().removeAll();
        contentarea.getChildren().setAll(fxml);


    }

    @FXML
    void trending(ActionEvent event) throws IOException {
        message.setVisible(false);
        reportAnchor.setVisible(false);
        FadeTransition fadeTransition1=new FadeTransition(Duration.seconds(0.5),pane1);
        fadeTransition1.setFromValue(0.15);
        fadeTransition1.setToValue(0);
        fadeTransition1.play();

        fadeTransition1.setOnFinished(event1 -> {
            pane1.setVisible(false);
        });


        TranslateTransition translateTransition1=new TranslateTransition(Duration.seconds(0.5),pane2);
        translateTransition1.setByX(-600);
        translateTransition1.play();
        Parent fxml = FXMLLoader.load(getClass().getResource("Trending.fxml"));
        contentarea.getChildren().removeAll();
        contentarea.getChildren().setAll(fxml);
    }
    @FXML
    void homepage(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("AdminPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) homepage.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


//////////////////////////////////////////////////////////////////////









}


