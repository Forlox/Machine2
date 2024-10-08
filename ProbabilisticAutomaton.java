import java.util.Random;

public class ProbabilisticAutomaton {
    // Определение состояний и сигналов
    public static final int TURN_ON = 1;
    public static final int TURN_OFF = 2;

    public static final int HIGH_TEMP = 1;
    public static final int LOW_TEMP = 2;

    public static final int NORMAL_TEMP = 1;
    public static final int OVERCOOL = 2;

    // Таблица переходов
    private static final double[][][] transitionTable = {
            // X=1 (Включить отопление)
            {
                    {0.5, 0.4, 0.1, 0.0},  // Z=1 (Высокая температура) -> Y
                    {0.0, 0.6, 0.2, 0.2}   // Z=2 (Низкая температура) -> Y
            },
            // X=2 (Выключить отопление)
            {
                    {0.0, 0.0, 0.7, 0.3},  // Z=1 (Высокая температура) -> Y
                    {0.0, 0.1, 0.0, 0.9}   // Z=2 (Низкая температура) -> Y
            }
    };

    private int currentState;
    private Random random;

    public ProbabilisticAutomaton() {
        // Изначально автомат находится в состоянии с высокой температурой
        this.currentState = HIGH_TEMP;
        this.random = new Random();
    }

    // Метод для выполнения одного шага автомата
    public int step(int inputSignal) {
        int z = currentState == HIGH_TEMP ? 0 : 1;
        double[] probabilities = transitionTable[inputSignal - 1][z];

        double r = random.nextDouble();
        double cumulativeProbability = 0.0;

        for (int i = 0; i < probabilities.length; i++) {
            cumulativeProbability += probabilities[i];
            if (r < cumulativeProbability) {
                // Определяем следующий сигнал
                if (i == 0 || i == 2) {
                    currentState = HIGH_TEMP;
                } else {
                    currentState = LOW_TEMP;
                }
                return (i % 2 == 0) ? NORMAL_TEMP : OVERCOOL;
            }
        }
        return NORMAL_TEMP; // По умолчанию нормальная температура
    }

    // Метод для преобразования состояния в строку
    public String stateToString(int state) {
        if (state == HIGH_TEMP) {
            return "Высокая температура";
        } else {
            return "Низкая температура";
        }
    }

    // Метод для преобразования выходного сигнала в строку
    public String signalToString(int signal) {
        if (signal == NORMAL_TEMP) {
            return "Нормальная температура";
        } else {
            return "Переохлаждение";
        }
    }

    // Метод для преобразования входного сигнала в строку
    public String inputSignalToString(int inputSignal) {
        if (inputSignal == TURN_ON) {
            return "Включить отопление";
        } else {
            return "Выключить отопление";
        }
    }

    public static void main(String[] args) {
        ProbabilisticAutomaton automaton = new ProbabilisticAutomaton();

        // Симуляция работы автомата
        int[] inputSignals = {TURN_ON, TURN_OFF, TURN_ON, TURN_OFF, TURN_ON};
        for (int inputSignal : inputSignals) {
            System.out.println("Входной сигнал: " + automaton.inputSignalToString(inputSignal));
            int outputSignal = automaton.step(inputSignal);
            System.out.println("Текущее состояние: " + automaton.stateToString(automaton.currentState));
            System.out.println("Выходной сигнал: " + automaton.signalToString(outputSignal));
            System.out.println();
        }
    }
}
