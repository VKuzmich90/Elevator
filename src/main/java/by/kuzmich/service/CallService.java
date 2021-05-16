package by.kuzmich.service;

import by.kuzmich.models.Call;
import by.kuzmich.models.DirectionType;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Slf4j
public class CallService {

    private final BlockingQueue<Call> queueCall;
    private final Lock lock = new ReentrantLock(true);


    private CallService(BlockingQueue<Call> queueCall) {
        log.debug("The call service begins to be created");

        this.queueCall = queueCall;

        log.debug("The call service was created");
    }

    public static CallService of(BlockingQueue<Call> queueCall) {
        checkNotNull(queueCall, "Queue call must exist!");

        return new CallService(queueCall);
    }


    public Call deleteCall(int floorNumber, DirectionType direction) {
        checkNotNull(direction, "Direction must exist!");
        checkArgument(floorNumber > 0, "Floor number must be positive");

        lock.lock();

        Call call = Call.of(floorNumber, direction);
        boolean isDeleted = queueCall.remove(call);

        lock.unlock();
        if (!isDeleted) {
            return null;
        }
        log.info("Delete call: {}", call);

        return call;
    }

    @SneakyThrows
    public void addCall(int floorNumber, DirectionType direction) {
        checkArgument(floorNumber >= 0 && floorNumber < 50, "Incorrect floor number!");
        checkNotNull(direction, "Direction must exist");

        lock.lock();

        Call call = Call.of(floorNumber, direction);

        if (!queueCall.contains(call)) {
            queueCall.put(call);

            log.info("Add call: {}", call);
        }
        lock.unlock();
    }

    @SneakyThrows
    public Call takeCall() {

        Call call = queueCall.take();

        log.info("Take call: {}", call);

        return call;
    }

}
