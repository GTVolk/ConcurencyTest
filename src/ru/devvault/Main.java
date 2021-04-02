package ru.devvault;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Что-то съедобное
 */
interface Consumable {

    /**
     * Употребить
     */
    void consume();
}

/**
 * Чаёк
 */
class Tea implements Consumable {
    @Override
    public void consume() {
        System.out.println("I drink delicious tea!");
    }
}

/**
 * Пицца
 */
class Pizza implements Consumable {
    @Override
    public void consume() {
        System.out.println("I eat delicious pizza!");
    }
}

/**
 * Салат
 */
class Salad implements Consumable {
    @Override
    public void consume() {
        System.out.println("I eat disgusting salad with broccoli!");
    }
}

/**
 * Приготовить чай
 */
class MakeTea implements Callable<Consumable> {
    public Consumable call() throws Exception {
        System.out.println("Kettle is heating...");
        TimeUnit.SECONDS.sleep((new Random()).nextInt(5));
        System.out.println("Kettle is hot...");
        TimeUnit.SECONDS.sleep((new Random()).nextInt(5));
        if ((new Random()).nextInt(5) > 2) {
            throw new Exception("The water has melted away. Fuuuuu...");
        }
        System.out.println("Kettle is boiled!");
        return new Tea();
    }
}

/**
 * Заказать пиццу
 */
class OrderPizza implements Callable<Consumable> {
    public Consumable call() throws Exception {
        System.out.println("Yandex food is cooking pizza...");
        TimeUnit.SECONDS.sleep((new Random()).nextInt(5));
        System.out.println("Yandex food is delivering your pizza...");
        TimeUnit.SECONDS.sleep((new Random()).nextInt(5));
        if ((new Random()).nextInt(5) > 2) {
            throw new Exception("Courier is got in crash. Fuuuuu...");
        }
        System.out.println("Your order is delivered! Here it is!");
        return new Pizza();
    }
}

/**
 * Замесить салатик
 */
class MixSalad implements Callable<Consumable> {
    public Consumable call() throws Exception {
        System.out.println("Starting mixing vegetables...");
        TimeUnit.SECONDS.sleep((new Random()).nextInt(5));
        if ((new Random()).nextInt(5) > 2) {
            throw new Exception("Vegetables have expired. Fuuuuu...");
        }
        System.out.println("Adding some olive oil...");
        TimeUnit.SECONDS.sleep((new Random()).nextInt(5));
        System.out.println("My best world ever salad is ready!");
        return new Salad();
    }
}

class Main {
    public static void main(String[] args) {
        List<Callable<Consumable>> meal = Arrays.asList(
            new MakeTea(),
            new OrderPizza(),
            new MixSalad()
        );

        ExecutorService es = Executors.newFixedThreadPool(3);
        try {
            List<Future<Consumable>> futures = es.invokeAll(meal);
            for (Future<Consumable> future : futures) {
                Consumable consumable = future.get();
                if (consumable != null) {
                    consumable.consume();
                }
            }
            System.out.println("I ate too much, need to rest!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            es.shutdown();
        }
    }
}
