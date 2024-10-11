package me.altzenck.utils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomUtils {

    private static final Random RANDOM = new SecureRandom();

    @SafeVarargs
    public static <E> E randomPercentaje(RandomElement<E>... elements) {
        List<RandomElement<E>> listValidElements = new ArrayList<>(), listAutoElements = new ArrayList<>();
        float totalProbability = 0, neededProbability = 100.0f;
        for (RandomElement<E> element : elements) {
            if (element.getProbability() == -1) {
                listAutoElements.add(element);
                continue;
            }
            listValidElements.add(element);
            totalProbability += element.getProbability();
        }
        if (totalProbability > neededProbability)
            throw new IllegalStateException();
        if (totalProbability < neededProbability) {
            if(!listAutoElements.isEmpty()) {
                float pd = (neededProbability - totalProbability) / listAutoElements.size();
                for (RandomElement<E> re : listAutoElements) {
                    listValidElements.add(RandomElement.of(re.getValue(), pd));
                    totalProbability += pd;
                }
                neededProbability = totalProbability;
            } else {
                RandomElement<E> defaultElement = RandomElement.of(null, neededProbability - totalProbability);
                listValidElements.add(defaultElement);
            }
        }
        float randomFloat = RANDOM.nextFloat() * neededProbability, cumulativeProbability = 0;
        for (RandomElement<E> element : listValidElements) {
            cumulativeProbability += element.getProbability();
            if (randomFloat <= cumulativeProbability) {
                return element.getValue();
            }
        }
        return null;
    }
}
