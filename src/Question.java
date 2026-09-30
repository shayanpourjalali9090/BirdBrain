public class Question {
    QuestionType type;
    String bird;
    String userAnswer;

    public Question(QuestionType type, String bird){
        this.type=type;
        this.bird=bird;
    }
    public void answer(String ans){this.userAnswer=ans;}
}
