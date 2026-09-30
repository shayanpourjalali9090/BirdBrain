import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;

public class BirdBrain {
    static List<String> birdList;
    static ArrayList<String> birds;
    static JFrame frame=new JFrame("Bird Brain");
    static Image logo=new ImageIcon(BirdBrain.class.getResource("/BirdBrain.png")).getImage();

    static Random rand=new Random();

    static Font flyingBirdFontSmall;
    static Font flyingBirdFontBig;
    static Color buttonBG=new Color(255, 255, 91);
    static Color titleBG=new Color(254, 196, 148);
    static Color quizBG=new Color(226, 226, 126);

    static CardLayout cardLayout=new CardLayout();
    static JPanel screens=new JPanel(cardLayout);

    static JPanel mainMenuPanel=new JPanel(new BorderLayout()),quizPanel=new JPanel(new BorderLayout());

    public static void initResources(){
        try {
            BufferedReader reader=new BufferedReader(new InputStreamReader(BirdBrain.class.getResourceAsStream("/birds.txt")));
            birdList=reader.lines().toList();
            reader.close();
            flyingBirdFontSmall=Font.createFont(Font.TRUETYPE_FONT,BirdBrain.class.getResourceAsStream("/fonts/FlyingBird.otf")).deriveFont(32f);
            flyingBirdFontBig=Font.createFont(Font.TRUETYPE_FONT,BirdBrain.class.getResourceAsStream("/fonts/FlyingBird.otf")).deriveFont(48f);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        birds=new ArrayList<>(birdList);

    }

    public static void initMainMenu(){
        mainMenuPanel.setBackground(titleBG);

        JPanel centre=new JPanel();
        centre.setBackground(titleBG);
        centre.setLayout(new BoxLayout(centre,BoxLayout.Y_AXIS));

        JButton button=new JButton("test");
        button.setMaximumSize(new Dimension(100,75));
        button.setFont(flyingBirdFontSmall);
        button.setBackground(buttonBG);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel titlePanel=new JPanel();
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBackground(titleBG);

        JLabel titleLabel=new JLabel("Bird Brain");
        titleLabel.setFont(flyingBirdFontBig);
        titleLabel.setForeground(new Color(255, 55, 55));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        Image scaled=logo.getScaledInstance(-1,100,Image.SCALE_SMOOTH);
        titleLabel.setIcon(new ImageIcon(scaled));
        titleLabel.setIconTextGap(12);
        titleLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        titleLabel.setVerticalTextPosition(SwingConstants.CENTER);

        titlePanel.add(titleLabel,BorderLayout.CENTER);
        titlePanel.setMaximumSize(new Dimension(frame.getWidth(),125));

        centre.add(titlePanel);
        centre.add(Box.createVerticalStrut(75));
        centre.add(button);

        mainMenuPanel.add(centre,BorderLayout.CENTER);

    }

    public static void finishQuiz(){

    }

    public static void nextQuestion(){
        questionNumberLabel.setText(questionNumber+".");

        if (questionNumber==questionCount){
            nextQuestionButton.setText("Done");

            for (ActionListener e:nextQuestionButton.getActionListeners()){
                nextQuestionButton.removeActionListener(e);
            }

            nextQuestionButton.addActionListener(e->{
                finishQuiz();

            });
        }

        questionNumber++;

    }

    static int questionNumber;
    static int questionCount;
    static ArrayList<Question> questions=new ArrayList<>();

    public static String getRandomBird(){
        int index=rand.nextInt(0,birdList.size());
        return birdList.get(index);
    }

    public static void generateQuestions(){
        for (int i=0;i<questionCount;i++){
            boolean entry=rand.nextInt(0,2)%2==0;
            questions.add(new Question(entry?QuestionType.ENTRY:QuestionType.MULTIPLE_CHOICE,getRandomBird()));
        }

    }

    public static void createQuiz(int count){
        questionNumber=1;
        questionCount=count;

        generateQuestions();

        cardLayout.show(screens,"quiz");

        nextQuestionButton.addActionListener(e->{
            nextQuestion();

        });
        nextQuestion();



    }

    static JLabel questionNumberLabel=new JLabel();
    static JLabel questionContentLabel=new JLabel();
    static JButton nextQuestionButton=new JButton("Next");
    static JPanel questionEntryPanel=new JPanel();
    static JPanel questionPanel=new JPanel();

    public static void initQuizPanel(){
        questionPanel.setLayout(new BoxLayout(questionPanel,BoxLayout.Y_AXIS));
        questionPanel.setBackground(buttonBG);

        questionNumberLabel.setFont(flyingBirdFontBig);
        questionNumberLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        nextQuestionButton.setFont(flyingBirdFontSmall);
        nextQuestionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextQuestionButton.setBackground(Color.WHITE);

        questionEntryPanel.setMaximumSize(new Dimension(400,400));
        questionEntryPanel.setBackground(buttonBG);

        questionPanel.add(Box.createVerticalStrut(50));
        questionPanel.add(questionNumberLabel);
        questionPanel.add(Box.createVerticalStrut(50));
        questionPanel.add(questionContentLabel);
        questionPanel.add(Box.createVerticalStrut(50));
        questionPanel.add(questionEntryPanel);
        questionPanel.add(Box.createVerticalStrut(50));
        questionPanel.add(nextQuestionButton);

        quizPanel.add(questionPanel,BorderLayout.CENTER);


    }

    public static void initUI(){
        screens.add(mainMenuPanel, "mainMenu");
        screens.add(quizPanel,"quiz");

        frame.setSize(1000,800);
        frame.setIconImage(logo);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(screens);

        initMainMenu();
        initQuizPanel();
        cardLayout.show(screens,"quiz");

        frame.setVisible(true);

    }

    public static void main(String[] args){

        initResources();
        initUI();



    }
}
