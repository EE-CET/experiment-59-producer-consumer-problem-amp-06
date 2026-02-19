class SharedResource {
    private int item;
    private boolean available = false;

    // Produce method
    public synchronized void produce(int value) throws InterruptedException {
        while (available) {
            wait();   // Wait if item not yet consumed
        }

        item = value;
        System.out.println("Produced: " + item);

        available = true;
        notify();     // Notify consumer
    }

    // Consume method
    public synchronized void consume() throws InterruptedException {
        while (!available) {
            wait();   // Wait if no item produced
        }

        System.out.println("Consumed: " + item);

        available = false;
        notify();     // Notify producer
    }
}

class Producer extends Thread {
    SharedResource resource;

    Producer(SharedResource resource) {
        this.resource = resource;
    }

    public void run() {
        try {
            for (int i = 1; i <= 5; i++) {
                resource.produce(i);
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}

class Consumer extends Thread {
    SharedResource resource;

    Consumer(SharedResource resource) {
        this.resource = resource;
    }

    public void run() {
        try {
            for (int i = 1; i <= 5; i++) {
                resource.consume();
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}

public class ProducerConsumer {
    public static void main(String[] args) {

        SharedResource obj = new SharedResource();

        Producer p = new Producer(obj);
        Consumer c = new Consumer(obj);

        p.start();
        c.start();
    }
}
