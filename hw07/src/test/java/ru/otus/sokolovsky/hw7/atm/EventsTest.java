package ru.otus.sokolovsky.hw7.atm;

import org.junit.Test;
import ru.otus.sokolovsky.hw7.events.Dispatcher;
import ru.otus.sokolovsky.hw7.events.Event;

import java.lang.reflect.Constructor;

import static org.junit.Assert.*;

public class EventsTest  {

    class Listener implements ru.otus.sokolovsky.hw7.events.Listener {
        int countOfCalls = 0;

        @Override
        public void notify(Event event) {
            countOfCalls++;
        }

        public int getCountOfCalls() {
            return countOfCalls;
        }
    }

    @SuppressWarnings("unchecked")
    private Dispatcher newDispatcher() throws Exception {
        try {
            Constructor<Dispatcher> constructor;

            constructor = (Constructor<Dispatcher>) Dispatcher.class.getDeclaredConstructors()[0];
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void shouldNotifyForListeners() throws Exception {
        Dispatcher dispatcher = newDispatcher();

        Listener mockListener = new Listener();
        dispatcher.subscribe(new Event("tested event"), mockListener);
        dispatcher.subscribe(new Event("tested event"), mockListener);

        dispatcher.trigger(new Event("tested event"));

        assertEquals(1, mockListener.getCountOfCalls());
    }

    @Test
    public void notifyWithoutSubscribers() throws Exception {
        Dispatcher dispatcher = newDispatcher();
        dispatcher.trigger(new Event("tested"));
    }
}
