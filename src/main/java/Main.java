public class Main {

    public static void main(String[] args) {
        ExprEvaluator evaluator = new ExprEvaluator("1 ++ 2");
        int answer = evaluator.evaluate();
        System.out.println(answer);
    }

}
