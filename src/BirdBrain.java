import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;

public class BirdBrain {
    static class MultipleChoiceBox{
        JRadioButton[] opts=new JRadioButton[4];
        ButtonGroup group=new ButtonGroup();
        int i=0;
        Font font=null;

        public MultipleChoiceBox(){}

        public boolean hasChoice(String choice){
            for (JRadioButton rb:opts){
                if (rb==null)continue;
                if (rb.getText().equalsIgnoreCase(choice))return true;
            }
            return false;
        }

        public JRadioButton addOption(String option){
            if (i==4)return null;
            opts[i]=new JRadioButton();
            if (font!=null)opts[i].setFont(font);
            opts[i].setText(option);
            group.add(opts[i]);
            return opts[i++];
        }

        public String getSelected(){
            for (JRadioButton rb:opts){
                if (rb.isSelected())return rb.getText();
            }
            return null;
        }

        public void setFont(Font font){
            this.font=font;
        }

    }
    static ArrayList<Bird> birdList=new ArrayList<>();

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

    static QuizType quizType;

    public static void initResources(){
        try {
            BufferedReader reader=new BufferedReader(new InputStreamReader(BirdBrain.class.getResourceAsStream("/birds.txt")));

            String fstr=reader.readAllAsString();

            for (String line:fstr.split("\\r?\\n")){
                if (line.isBlank())continue;

                String[] fields=line.split(",");
                birdList.add(new Bird(fields[0],fields[1],fields[2]));
            }

            reader.close();

            flyingBirdFontSmall=Font.createFont(Font.TRUETYPE_FONT,BirdBrain.class.getResourceAsStream("/fonts/FlyingBird.otf")).deriveFont(32f);
            flyingBirdFontBig=Font.createFont(Font.TRUETYPE_FONT,BirdBrain.class.getResourceAsStream("/fonts/FlyingBird.otf")).deriveFont(48f);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private static JButton createMenuButton(String text){
        JButton button=new JButton(text);
        button.setMaximumSize(new Dimension(text.length()*20,75));
        button.setFont(flyingBirdFontSmall);
        button.setBackground(buttonBG);

        return button;
    }

    public static void createQuizLengthDialog(QuizType type){
        JDialog dialog=new JDialog();
        dialog.setBackground(titleBG);
        dialog.setSize(550,300);
        dialog.setVisible(true);

        JPanel main=new JPanel();
        main.setBackground(titleBG);
        main.setLayout(new BoxLayout(main,BoxLayout.Y_AXIS));

        JLabel countLabel=new JLabel("Please select the amount of questions:");
        countLabel.setFont(flyingBirdFontSmall);
        countLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JComboBox<Integer> comboBox=new JComboBox<>();
        comboBox.setFont(flyingBirdFontSmall);
        comboBox.setMaximumSize(new Dimension(100,75));
        comboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        comboBox.addItem(10);
        comboBox.addItem(25);
        comboBox.addItem(50);

        JButton submitButton=new JButton("Submit");
        submitButton.setFont(flyingBirdFontSmall);
        submitButton.addActionListener(e->{
            int value=(Integer)comboBox.getSelectedItem();
            dialog.dispose();
            createQuiz(type,value);
        });

        main.add(Box.createVerticalStrut(25));
        main.add(countLabel);
        main.add(Box.createVerticalStrut(25));
        main.add(comboBox);

        dialog.add(main,BorderLayout.CENTER);
        dialog.add(submitButton,BorderLayout.SOUTH);

    }

    public static void initMainMenu(){
        mainMenuPanel.setBackground(titleBG);

        JPanel centre=new JPanel();
        centre.setBackground(titleBG);
        centre.setLayout(new BoxLayout(centre,BoxLayout.Y_AXIS));

        JButton speciesQuizButton=createMenuButton("Species Quiz");
        speciesQuizButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        speciesQuizButton.addActionListener(e->{
            createQuizLengthDialog(QuizType.SPECIES);
        });

        JButton familyQuizButton=createMenuButton("Family Quiz");
        familyQuizButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        familyQuizButton.addActionListener(e->{
            createQuizLengthDialog(QuizType.FAMILY);
        });

        JButton orderQuizButton=createMenuButton("Order Quiz");
        orderQuizButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        orderQuizButton.addActionListener(e->{
            createQuizLengthDialog(QuizType.ORDER);
        });

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
        centre.add(speciesQuizButton);
        centre.add(Box.createVerticalStrut(75));
        centre.add(familyQuizButton);
        centre.add(Box.createVerticalStrut(75));
        centre.add(orderQuizButton);

        mainMenuPanel.add(centre,BorderLayout.CENTER);

    }

    public static void finishQuiz(){
        int correct=0;
        for (Question q:questions){
            correct+=q.mark()?1:0;
        }
        JOptionPane.showMessageDialog(frame,String.format("You got %d/%d questions correct",correct,questionCount));
        cardLayout.show(screens,"mainMenu");

    }

    public static String capitalise(String str){
        str=str.trim().toLowerCase();
        char[] arr=str.toCharArray();
        arr[0]=Character.toUpperCase(arr[0]);
        return new String(arr);
    }

    public static MultipleChoiceBox createBirdChoiceBox(Question q){
        MultipleChoiceBox multipleChoiceBox=new MultipleChoiceBox();
        Bird bird=q.bird;
        String birdField=quizType==QuizType.ORDER?bird.order:quizType==QuizType.FAMILY?bird.family:bird.species;

        multipleChoiceBox.setFont(flyingBirdFontSmall);

        int randomIndex=rand.nextInt(0,4);

        for (int i=0;i<4;i++){
            if (i==randomIndex){
                multipleChoiceBox.addOption(birdField);
                continue;
            }
            String field=null;
            while (field==null || field.equals(birdField) || multipleChoiceBox.hasChoice(field)){
                Bird randomBird=getRandomBird();
                field=quizType==QuizType.FAMILY?randomBird.family:quizType==QuizType.ORDER?randomBird.order:randomBird.species;

            }
            multipleChoiceBox.addOption(field);

        }

        return multipleChoiceBox;

    }

    public static void nextQuestion(){
        questionNumberLabel.setText(String.format("%d. What %s?",questionNumber+1,capitalise(quizType.toString())));

        Question current=questions.get(questionNumber);

        boolean lastQuestion=(questionNumber+1)==questionCount;

        questionEntryPanel.removeAll();

        for (ActionListener al:nextQuestionButton.getActionListeners())nextQuestionButton.removeActionListener(al);

        if (current.questionType==QuestionType.MULTIPLE_CHOICE){
            MultipleChoiceBox choiceBox=createBirdChoiceBox(current);
            for (JRadioButton b:choiceBox.opts){
                b.setAlignmentX(Component.CENTER_ALIGNMENT);
                questionEntryPanel.add(b);
            }

            nextQuestionButton.addActionListener(e->{
                String selected=choiceBox.getSelected();
                if (selected!=null){
                    current.answer(selected);
                    if (lastQuestion){
                        finishQuiz();
                    }
                    else nextQuestion();
                }
            });

        }
        else{
            JTextField field=new JTextField();
            field.setFont(flyingBirdFontSmall);
            field.setMaximumSize(new Dimension(200,75));
            questionEntryPanel.add(field);

            nextQuestionButton.addActionListener(e->{
                if (!field.getText().isBlank()){
                    current.answer(field.getText());
                    if (lastQuestion)
                        finishQuiz();
                    else
                        nextQuestion();
                }

            });

        }

        Image birdImage=new ImageIcon(BirdBrain.class.getResource(String.format("/images/%s",current.bird.species))).getImage();
        birdImage=birdImage.getScaledInstance(-1,300,Image.SCALE_SMOOTH);
        questionContentLabel.setIcon(new ImageIcon(birdImage));

        if (lastQuestion){
            nextQuestionButton.setText("Done");

        }

        questionNumber++;

    }

    static int questionNumber;
    static int questionCount;
    static ArrayList<Question> questions=new ArrayList<>();

    public static Bird getRandomBird(){
        int index=rand.nextInt(0,birdList.size());
        return birdList.get(index);
    }

    public static boolean questionsContainsBird(Bird b){
        for (Question q:questions){
            if (q.bird.equals(b))return true;
        }
        return false;
    }

    public static void generateQuestions(){
        questions.clear();
        for (int i=0;i<questionCount;i++){
            boolean entry=rand.nextInt(0,2)==0;
            Bird randomBird=null;
            while (randomBird==null || questionsContainsBird(randomBird)){
                randomBird=getRandomBird();
            }
            questions.add(new Question(quizType,entry?QuestionType.ENTRY:QuestionType.MULTIPLE_CHOICE,randomBird));
        }

    }

    public static void createQuiz(QuizType type, int length){
        questionNumber=0;
        quizType=type;
        questionCount=length;

        generateQuestions();

        cardLayout.show(screens,"quiz");

        for (ActionListener e:nextQuestionButton.getActionListeners())
            nextQuestionButton.removeActionListener(e);

        nextQuestionButton.setText("next");

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

        questionEntryPanel.setLayout(new BoxLayout(questionEntryPanel,BoxLayout.Y_AXIS));
        questionEntryPanel.setMaximumSize(new Dimension(400,400));
        questionEntryPanel.setBackground(buttonBG);

        questionContentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

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
        cardLayout.show(screens,"mainMenu");

        frame.setVisible(true);

    }

    public static void main(String[] args){
        initResources();
        initUI();
        frame.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if (e.getKeyCode()==KeyEvent.VK_ESCAPE){
                    cardLayout.show(screens,"mainMenu");
                }
            }
        });
    }
}
