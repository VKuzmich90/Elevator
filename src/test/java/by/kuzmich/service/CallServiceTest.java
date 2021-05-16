package by.kuzmich.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import by.kuzmich.models.Call;
import by.kuzmich.models.DirectionType;
import org.junit.jupiter.api.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class CallServiceTest {

    @Test
    void wellCreated() {
        BlockingQueue<Call> blockingQueue = new LinkedBlockingQueue<>();
        CallService.of(blockingQueue);
    }

    @Test
    void invalid() {
        assertThrows(NullPointerException.class, () -> {
            CallService.of(null);
        });
    }

    @Test
    void deleteCallSuccessfully() {
        Call call = Call.of(3, DirectionType.UP);
        BlockingQueue<Call> blockingQueue = new LinkedBlockingQueue<>();
        CallService callService = CallService.of(blockingQueue);

        callService.addCall(2, DirectionType.DOWN);
        callService.addCall(3, DirectionType.DOWN);
        callService.addCall(3, DirectionType.UP);


        Call callDeleted = callService.deleteCall(3, DirectionType.UP);

        assertEquals(callDeleted, call);
        assertThat(blockingQueue.size(), is(2));
    }

    @Test
    void deleteCallInvalidFloorNumber() {
        BlockingQueue<Call> blockingQueue = new LinkedBlockingQueue<>();
        CallService callService = CallService.of(blockingQueue);

        assertThrows(IllegalArgumentException.class, () -> {
            callService.deleteCall(0, DirectionType.DOWN);
        });
    }

    @Test
    void deleteCallInvalidDirection() {
        BlockingQueue<Call> blockingQueue = new LinkedBlockingQueue<>();
        CallService callService = CallService.of(blockingQueue);

        assertThrows(NullPointerException.class, () -> {
            callService.deleteCall(2, null);
        });
    }

    @Test
    void addCallSuccessfully() {
        BlockingQueue<Call> queueCall = new LinkedBlockingQueue<>();
        CallService callService = CallService.of(queueCall);

        callService.addCall(2, DirectionType.DOWN);
        callService.addCall(3, DirectionType.DOWN);

        assertThat(queueCall.size(), is(2));
    }

    @Test
    void addCallInvalidFloorNumber() {
        BlockingQueue<Call> queueCall = new LinkedBlockingQueue<>();
        CallService callService = CallService.of(queueCall);

        assertThrows(IllegalArgumentException.class, () -> {
            callService.addCall(0, DirectionType.DOWN);
        });

    }

    @Test
    void addCallInvalidNullDirection() {
        BlockingQueue<Call> queueCall = new LinkedBlockingQueue<>();
        CallService callService = CallService.of(queueCall);

        assertThrows(NullPointerException.class, () -> {
            callService.addCall(2, null);
        });
    }

    @Test
    void takeCall() {
        Call call = Call.of(2, DirectionType.DOWN);
        BlockingQueue<Call> queueCall = new LinkedBlockingQueue<>();
        CallService callService = CallService.of(queueCall);

        callService.addCall(2, DirectionType.DOWN);
        callService.addCall(3, DirectionType.DOWN);

        assertEquals(callService.takeCall(), call);
    }


}