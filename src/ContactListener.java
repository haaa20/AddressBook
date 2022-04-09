import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ContactListener implements ChangeListener {
    @Override
    public void stateChanged(ChangeEvent e) {
        System.out.println("BEEP BEEP BITCHES");
    }
}
