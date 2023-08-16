public class Answer {
    //    {"answer":"rouse","tomorrowAnswer":"lucky"}
    private String answer;
    private String tomorrowAnswer;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getTomorrowAnswer() {
        return tomorrowAnswer;
    }

    public void setTomorrowAnswer(String tomorrowAnswer) {
        this.tomorrowAnswer = tomorrowAnswer;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "answer='" + answer + '\'' +
                ", tomorrowAnswer='" + tomorrowAnswer + '\'' +
                '}';
    }
}