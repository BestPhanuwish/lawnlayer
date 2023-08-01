package lawnlayer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PropagateTest {
    
    private Propagate propagate;

    @BeforeEach
    public void setUp() {
        propagate = new Propagate(0);
    }

    @Test
    @DisplayName("Existence Check")
    public void simplePropagate() {
        assertNotNull(propagate);
    }

    @Test
    @DisplayName("Getter Index")
    public void getterIndex() {
        assertEquals(0, propagate.getIndex());
    }

    @Test
    @DisplayName("Setter Index")
    public void setterIndex() {
        propagate.setIndex(3);
        assertEquals(3, propagate.getIndex());
    }

    @Test
    @DisplayName("Getter Count")
    public void getterCount() {
        assertEquals(0, propagate.getCount());
    }

    @Test
    @DisplayName("Setter Count")
    public void setterCount() {
        propagate.setCount(1);
        assertEquals(1, propagate.getCount());
    }

    @Test
    @DisplayName("Getter Timer")
    public void getterTimer() {
        assertEquals(3, propagate.getTimer());
    }

    @Test
    @DisplayName("Setter Timer")
    public void setterTimer() {
        propagate.setTimer(0);
        assertEquals(0, propagate.getTimer());
    }

}
