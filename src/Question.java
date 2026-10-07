public class Question {
    QuizType quizType;
    QuestionType questionType;
    Bird bird;
    String correctAnswer,userAnswer;
    Boolean correct;

    public Question(QuizType quizType,QuestionType questionType, Bird bird){
        this.questionType=questionType;
        this.quizType=quizType;
        this.bird=bird;
    }

    public void answer(String ans){
        this.userAnswer=ans.trim();
    }

    public boolean mark(){
        correctAnswer=quizType==QuizType.FAMILY?bird.family:quizType==QuizType.ORDER?bird.order:bird.species;
        correct=userAnswer.equalsIgnoreCase(correctAnswer);
        return correct;
    }
}
