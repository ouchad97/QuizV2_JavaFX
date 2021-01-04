package application;

import java.io.File;
import java.time.LocalTime;
import java.util.ArrayList;


import jaco.mp3.player.MP3Player;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javafx.scene.Group;
import javafx.scene.Scene;



public class Main extends Application implements InterListe {
	
	static Players player1;
	static int cpT_=0,
	_Tentative=0;
	static Group panel;
	private static Label Label_Timer;
	private static Button btn_valider;
	private static Button btn_debut;
	static Label lapel_score;
	static ArrayList<ToggleGroup> listeRadioButtonGroup;
	private static Stage pStage;
	private static Timeline timeline;
	private static TextField txt_nom;
	private static TextField txt_prenom;
	private static TextField txt_age;
	private static Label label_nom;
	private static Label label_prenom;
	private static Label label_age;
	static MP3Player music;
	private static AnchorPane Anchor;
	static ImageView live1 = new ImageView(new Image("file:points.png")); 
	static ImageView live2 = new ImageView(new Image("file:points.png")); 
	
	@Override
	public void start(Stage primaryStage) {
		try {
			listeRadioButtonGroup = new ArrayList<ToggleGroup>();
			panel = new Group();
		
			Scene scene = new Scene(panel,800, 600);
			
			String css = this.getClass().getResource("application.css").toExternalForm(); 
			scene.getStylesheets().add(css);
			primaryStage.setTitle("Quiz");
			login();
			Label_Timer= new Label();
			primaryStage.setScene(scene);
			primaryStage.show();
			pStage = primaryStage;
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static void playerMusic(String chemin) {
		if(music!=null) {
			music.stop();
			music=null;
		}
		music = new MP3Player(new File(chemin));
		music.setRepeat(true);
		music.play();
	}
	
	public static void login() {
		
		playerMusic("quiz-show.mp3");
		Anchor = new AnchorPane();
		Anchor.getStyleClass().add("ach");
		Anchor.setPrefSize(800, 40);
		panel.getChildren().add(Anchor);
		
		label_nom = new Label("Nom :");
		label_nom.relocate(200, 170);
		panel.getChildren().add(label_nom);
		
		label_prenom = new Label("Prénom :");
		label_prenom.relocate(200, 220);
		panel.getChildren().add(label_prenom);
		
		label_age = new Label("Age :");
		label_age.relocate(200, 270);
		panel.getChildren().add(label_age);
		
		txt_nom = new TextField();
		txt_nom.relocate(300, 170);
		panel.getChildren().add(txt_nom);
		
		txt_prenom = new TextField();
		txt_prenom.relocate(300, 220);
		panel.getChildren().add(txt_prenom);
		
		txt_age = new TextField();
		txt_age.relocate(300, 270);
		panel.getChildren().add(txt_age);
		
		btn_debut = new Button("Démarrer le quiz");
		btn_debut.relocate(310, 350);
		panel.getChildren().add(btn_debut);
		btn_debut.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				player1 = new Players(txt_nom.getText(), txt_prenom.getText(), Integer.parseInt(txt_age.getText()));
				
				timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
				    @Override
				    public void handle(ActionEvent event) {
				    	if(player1.getDuration() > 0) {
							player1.setDuration(player1.getDuration()-1);
							Label_Timer.setText(LocalTime.MIN.plusSeconds(player1.getDuration()).toString());
						}
						else {
							timeline.stop();
							getAlert("GAME OVER");
		                    System.exit(0);
						}
				    }
				}));
				timeline.setCycleCount(Timeline.INDEFINITE);
				timeline.play();
				
				niveau1();
				playerMusic("timeTicking.mp3");
			}
		});
	}
	
	
	public static void remplirePanelNiveau(int niveau) {
		int start , end;
		if(niveau == 1)
		{
			start=0;
			end  =5;
 
		}
		else if(niveau == 2)
		{
			start=5;
			end  =10;
		}
		else
		{
			start=10;
			end  =15;
		}
		pStage.setTitle(NiveauActuel());
		panel.getChildren().clear();
		Label tentative = new Label("Tentative : ");
		tentative.relocate(550, 50);
		panel.getChildren().add(tentative);
		live1.setVisible(true);
		live1.relocate(610, 50);
		panel.getChildren().add(live1);
		live2.setVisible(true);
		live2.relocate(630, 50);
		panel.getChildren().add(live2);
		lapel_score =new Label();
		lapel_score.relocate(700,70);
		panel.getChildren().add(lapel_score);
		Label_Timer.relocate(700,50);
		panel.getChildren().add(Label_Timer);
		int y=80;
		for(int i = start;i<end;i++) {
			int x=30;
			Label lapel_question = new Label(listeQuiz.get(i).getQuestion());
			lapel_question.relocate(x, y);
			panel.getChildren().add(lapel_question);
			
			y += 54;
			RadioButton choise1 = new RadioButton(listeQuiz.get(i).getChoice_one());
			choise1.relocate(x, y);
			panel.getChildren().add(choise1);
			
			x += 240;
			RadioButton choise2 = new RadioButton(listeQuiz.get(i).getChoice_two());
			choise2.relocate(x, y);
			panel.getChildren().add(choise2);
			
			ToggleGroup group = new ToggleGroup();
			choise1.setToggleGroup(group);
			choise2.setToggleGroup(group);
			
			if(listeQuiz.get(i).getChoice_three() != null) {
				x += 240;
				RadioButton choise3 = new RadioButton(listeQuiz.get(i).getChoice_three());
				choise3.relocate(x, y);
				panel.getChildren().add(choise3);
				choise3.setToggleGroup(group);
			}
			y += 40;
			listeRadioButtonGroup.add(group);
		}
	}
	
	public static void niveau1() {
		Quiz quiz1 = new Quiz("JAVA est  un langage", "Compilé et interpreté", "Compilé", "Interprété", "Compilé et interpreté");
		Quiz quiz2 = new Quiz("Toutes les classes héritent de la classe", "Object", "Main", "Object", "AWT");
		Quiz quiz3 = new Quiz("Par convention une classe", "commence par une majuscule", "est en minuscule", "commence par une majuscule", "est en majuscules");
		Quiz quiz4 = new Quiz("Est-ce que on peut avoir plusieurs constructeurs pour la même classe", "oui", "oui", "non");
		Quiz quiz5 = new Quiz("Dans la ligne \"public class A implements B\", B est:", "Interfacce", "Interfacce", "Classe parent", "Package");
		
		listeQuiz.add(quiz1);
		listeQuiz.add(quiz2);
		listeQuiz.add(quiz3);
		listeQuiz.add(quiz4);
		listeQuiz.add(quiz5);
		
		remplirePanelNiveau(1);
		

		btn_valider = new Button("valider");
		btn_valider.relocate(630, 530);
		panel.getChildren().add(btn_valider);

		panel.getChildren().add(Anchor);
		btn_valider.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if(checkReponseAllQuestion(1)) {
					getTentative(1);
				}
				else {
					getAlert("Merci de répondre à toutes les questions");
				}
			}
		});
	}
	
	public static void niveau2() {
		Quiz quiz1 = new Quiz("Après la compilation, un programme écrit en JAVA, il se transforme en programme en bytecode. Quelle est l’extension du programme en bytecode ?", ".Class", "aucun des choix", ".JAVA", ".Class");
		Quiz quiz2 = new Quiz("Class Test{Public Test () {System.out.println(”Bonjour”);}public Test (int i) {this(); System.out.println(”Nous sommes en ”+i+”!”);}; }qu’affichera l’instruction suivante? Test t1=new Test (2020);", "Bonjour Nous sommes en 2020 !", "aucun des choix", "Bonjour Nous sommes en 2020 !", "Nous sommes en 2020 !");
		Quiz quiz3 = new Quiz("Voici un constructeur de la classe Employee, y-a-t'il un problème Public void Employe(String n){Nom=n;}", "vrai", "vrai", "faux");
		Quiz quiz4 = new Quiz("Pour spécifier que la variable ne pourra plus être modifiée et doit être initialisée dès sa déclaration, on la déclare comme une constante avec le mot réservé", "final", "aucun des choix", "final","const");
		Quiz quiz5 = new Quiz("Dans une classe, on accède à ses variables grâce au mot clé", "this", "aucun des choix", "this", "super");
		
		listeQuiz.add(quiz1);
		listeQuiz.add(quiz2);
		listeQuiz.add(quiz3);
		listeQuiz.add(quiz4);
		listeQuiz.add(quiz5);
		
		remplirePanelNiveau(2);
		

		btn_valider = new Button("valider");
		btn_valider.relocate(630, 530);
		panel.getChildren().add(btn_valider);
		panel.getChildren().add(Anchor);
		btn_valider.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if(checkReponseAllQuestion(2)) {
					getTentative(2);
				}
				else {
					getAlert("Merci de répondre à toutes les questions");
				}
			}
		});
	}
	
	public static void niveau3() {
		Quiz quiz1 = new Quiz("calculerSalaire(int) calculerSalaire(int, double)La méthode calculerSalaire est:", "surchargée", "aucun des choix", "surchargée", "redéfinie");
		Quiz quiz2 = new Quiz("Une classe qui contient au moins une méthode abstraite doit être déclarée abstraite.", "vrai", "vrai", "faux");
		Quiz quiz3 = new Quiz("Est-ce qu’une classe peut implémenter plusieurs interfaces?", "vrai", "vrai", "faux");
		Quiz quiz4 = new Quiz("La déclaration d'une méthode suivante :public void traitement() throws IOExceptionprécise que la méthode propage une exception contrôlée", "vrai", "vrai", "faux");
		Quiz quiz5 = new Quiz("class Test{public static void main (String[] args) {try {int a =10;System.out.println (\"a = \" + a );int b = 0 / a;System.out.println (\"b = \" + b);}catch(ArithmeticException e){System.out.println (\"diviser par 0!\"); }finally{System.out.println(\"je suis à l’intérieur de finally\");}}}", "a=10 b=0 Je suis à l’intérieur de finally", "aucun des choix", "a=10 b=0 Je suis à l’intérieur de finally", "a=10 b=0 diviser par 0! je suis à l’intérieur de finally");
		
		listeQuiz.add(quiz1);
		listeQuiz.add(quiz2);
		listeQuiz.add(quiz3);
		listeQuiz.add(quiz4);
		listeQuiz.add(quiz5);
		
		remplirePanelNiveau(3);
		

		btn_valider = new Button("valider");
		btn_valider.relocate(630, 530);
		panel.getChildren().add(btn_valider);
		panel.getChildren().add(Anchor);
		btn_valider.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if(checkReponseAllQuestion(3)) {
					getTentative(3);
				}
				else {
					getAlert("Merci de répondre à toutes les questions");
				}
			}
		});
	}
	
	public static boolean checkReponseAllQuestion(int niveau){
		int start , end;
		if(niveau == 1)
		{
			start=0;
			end  =5;
 
		}
		else if(niveau == 2)
		{
			start=5;
			end  =10;
		}
		else
		{
			start=10;
			end  =15;
		}
		boolean ret=true;
		for(int i = start;i<end;i++) {
			if(listeRadioButtonGroup.get(i).getSelectedToggle() == null) {
				ret = false;
			}
		}
		return ret;
	}
	
	public static String NiveauActuel()
	{
		String ret ="QUIZ";
		if(listeQuiz.size()==5)
		{
			ret= "Niveau 1";
		}
		else if (listeQuiz.size()==10)
		{
			ret= "Niveau 2";
		}
		else if(listeQuiz.size()==15)
		{
			ret= "Niveau 3";
		}
		return ret;
		
	}
	
	public static void getreponses(int niveau)
	{
		int start , end;
		if(niveau == 1)
		{
			start=0;
			end  =5;
 
		}
		else if(niveau == 2)
		{
			start=5;
			end  =10;
		}
		else
		{
			start=10;
			end  =15;
		}
		for(int i = start;i<end;i++) {
			boolean choice; 
			RadioButton chk = (RadioButton)listeRadioButtonGroup.get(i).getSelectedToggle();
			if(listeQuiz.get(i).getReponse() == chk.getText()) {
				choice=true;
			}
			else {
				choice=false;
			}
			Player_QUIZ player_QUIZ = new Player_QUIZ(player1.getId_Player(), listeQuiz.get(i).getId_quiz(), chk.getText(), choice);
			listePlayer_QUIZ.add(player_QUIZ);
		}
	}
	
	public static int calculeScore(int niveau)
	{
		int start , end;
		if(niveau == 1)
		{
			start=0;
			end  =5;
 
		}
		else if(niveau == 2)
		{
			start=5;
			end  =10;
		}
		else
		{
			start=10;
			end  =15;
		}
		int score = 0;
		for(int i = start;i<end;i++) {
			if(listePlayer_QUIZ.get(i).isGoodchoice()) {
				score += 20;
			}
		}
		return score;
	}
	
	public static void correction(int niveau){
		int start , end;
		if(niveau == 1)
		{
			start=0;
			end  =5;
 
		}
		else if(niveau == 2)
		{
			start=5;
			end  =10;
		}
		else
		{
			start=10;
			end  =15;
		}
		for(int i = start;i<end;i++) {
			if(listePlayer_QUIZ.get(i).isGoodchoice()) {
				RadioButton chk = (RadioButton)listeRadioButtonGroup.get(i).getSelectedToggle();
		        chk.getStyleClass().add("radio-button-correct");
			}
			else{
				ToggleGroup t1 = listeRadioButtonGroup.get(i);
				for (Toggle t : t1.getToggles()) {
	            	RadioButton rd = (RadioButton)t;
		            if (rd.isSelected()) {
						rd.getStyleClass().add("radio-button-incorrect");
					}else if(rd.getText() == listeQuiz.get(i).getReponse()) {
						rd.getStyleClass().add("radio-button-correct");
					}
				}
			}
		}
	}
	
	public static void afficheCorrection(int niveau) {
		if(cpT_==0) {
			cpT_=1;
			btn_valider.setText("Suivant");
			correction(niveau);
			lapel_score.setText("Score : " + calculeScore(niveau));
		}
		else {
			cpT_=0;
			if(niveau==1) {
				if(calculeScore(1) >= 40) {
					
					niveau2();
					
				}
				else {
					playerMusic("lose.mp3");
					getAlertWithImage("lose.gif","LOSE");
					timeline.stop();
                    System.exit(0);
				}
			}
			else if(niveau==2) {
				if(calculeScore(2) >= 60) {
					niveau3();
					
				}
				else {
					playerMusic("lose.mp3");
					getAlertWithImage("lose.gif","LOSE");
					timeline.stop();
					System.exit(0);
				}
			}
			else {
				if(calculeScore(3) >= 80) {
					playerMusic("win.mp3");
					getAlertWithImage("source.gif","WIN");
					timeline.stop();
					System.exit(0);
				}
				else {
					playerMusic("lose.mp3");
					getAlertWithImage("lose.gif","LOSE");
					timeline.stop();
                    System.exit(0);
				}
			}
		}
	}
	
	public static void getTentative(int niveau) {
		if(cpT_==0) {
			getreponses(niveau);
			if (_Tentative == 0) {
				if ((niveau == 1  && calculeScore(1) >= 40) || (niveau == 2  && calculeScore(2) >= 60) || (niveau == 3  && calculeScore(3) >= 80)) {
					afficheCorrection(niveau);
				}else {
					_Tentative = 1;
					live2.setVisible(false);
					//delete last score
					for (int i = 0; i < 5; i++) {
						listePlayer_QUIZ.remove(listePlayer_QUIZ.size()-1);
					}
					getAlert("Vous n'avez pas passé cette niveau, vous avez une autre tentative BONNE CHANCE!");
					for (int i = 0; i < listeRadioButtonGroup.size(); i++) {
						listeRadioButtonGroup.get(i).selectToggle(null);
					}
				}
			}
			else {
				if ((niveau == 1  && calculeScore(1) < 40) || (niveau == 2  && calculeScore(2) < 60) || (niveau == 3  && calculeScore(3) < 80)) {
					live1.setVisible(false);
				}
				_Tentative = 0;
				afficheCorrection(niveau);
			}
		}
		else {
			afficheCorrection(niveau);
		}
	}
	
	public static void getAlert(String text) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText(text);
		alert.showAndWait();
	}
	
	public static void getAlertWithImage(String chemin, String Title) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(Title);
		alert.setHeaderText(null);
		alert.setResizable(true);
		alert.getDialogPane().setPrefSize(400, 320);
		Image image = new Image("file:"+chemin);
		ImageView imageView = new ImageView(image);
		alert.setGraphic(imageView);
		alert.showAndWait();
	}
}
