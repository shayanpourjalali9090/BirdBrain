public class Question {
    QuizType quizType;
    QuestionType questionType;
    Bird bird;
    String userAnswer;

    public Question(QuizType quizType,QuestionType questionType, Bird bird){
        this.questionType=questionType;
        this.bird=bird;
    }

    public void answer(String ans){
        System.out.println("ANSWERED: "+ans);
        this.userAnswer=ans.trim();
    }

    public boolean mark(){
        return quizType==QuizType.FAMILY?userAnswer.equalsIgnoreCase(bird.family):quizType==QuizType.ORDER?userAnswer.equalsIgnoreCase(bird.order):userAnswer.equalsIgnoreCase(bird.species);
    }
}
