import static de.vsy.client.gui.essential_graphical_unit.NavigationGoal.ACCOUNT_CREATION;
import static de.vsy.client.gui.essential_graphical_unit.NavigationGoal.CLOSE_APPLICATION;
import static de.vsy.client.gui.essential_graphical_unit.NavigationGoal.LOGIN;

import de.vsy.client.gui.essential_graphical_unit.NavigationGoal;
import de.vsy.client.gui.essential_graphical_unit.prompt.AccountCreationPanel;
import de.vsy.client.gui.essential_graphical_unit.prompt.LoginPanel;
import de.vsy.client.gui.essential_graphical_unit.prompt.NotificationPanel;
import de.vsy.client.gui.essential_graphical_unit.prompt.WelcomeDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.JOptionPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DialogTesting {

  private static final Logger LOGGER = LogManager.getLogger();

  ExecutorService singleThread;

  @BeforeEach
  void createSingleThreadExecutor(){
    singleThread = Executors.newSingleThreadExecutor();
  }

  @AfterEach
  void stopSingleThreadExecutor() throws InterruptedException{
    singleThread.shutdownNow();
    var threadShutdown = singleThread.awaitTermination(5, TimeUnit.SECONDS);

    if(!threadShutdown){
      throw new RuntimeException("Single thread executor unable to terminate within 5 seconds.");
    }
  }

  @Test
  public
  void testNotificationDialog(){
    final var notification = "This is a notification message";
    final var notificationPanel = new NotificationPanel(notification);
    final var notificationDialog = new JOptionPane(notificationPanel, JOptionPane.INFORMATION_MESSAGE, JOptionPane.OK_OPTION, null);
    Assertions.assertDoesNotThrow(() ->
    JOptionPane.showMessageDialog(null, notificationPanel, "Information",
        JOptionPane.INFORMATION_MESSAGE));
  }

  @Test
  public
  void testWelcomeDialogLogin(){
    LOGGER.info("Test: WelcomeDialog Login started.");
    LOGGER.trace("Command expected: {}", LOGIN);

    Callable<Boolean> runnable = () -> {
      CountDownLatch resultLatch= new CountDownLatch(1);
      final var result = new AtomicReference<Boolean>();
      final var ac = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e) {
          var commandString = e.getActionCommand().trim().toUpperCase();
          LOGGER.trace("Command received: {}", commandString);
          result.set(NavigationGoal.valueOf(commandString).equals(LOGIN));
          resultLatch.countDown();
        }
      };
      final var welcomeDialog = new WelcomeDialog(ac);
      welcomeDialog.setLocationRelativeTo(null);
      welcomeDialog.setVisible(true);
      resultLatch.await();
      return result.get();
    };
    final var futureResult = Executors.newSingleThreadExecutor().submit(runnable);

    try{
      final var correctAction = futureResult.get();
      Assertions.assertTrue(correctAction, "Possibly wrong command set for action. See trace log.");
    } catch (ExecutionException e) {
      Assertions.fail(e);
    } catch (InterruptedException e) {
      Assertions.fail(e);
    }
    LOGGER.info("Test: WelcomeDialog Login terminated.");
  }

  @Test
  public
  void testWelcomeDialogCloseApplication(){
    LOGGER.info("Test: WelcomeDialog Close_Application started.");
    LOGGER.trace("Command expected: {}", CLOSE_APPLICATION);

    Callable<Boolean> runnable = () -> {
      CountDownLatch resultLatch= new CountDownLatch(1);
      final var result = new AtomicReference<Boolean>();
      final var ac = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e) {
          var commandString = e.getActionCommand().trim().toUpperCase();
          LOGGER.trace("Command received: {}", commandString);
          result.set(NavigationGoal.valueOf(commandString).equals(CLOSE_APPLICATION));
          resultLatch.countDown();
        }
      };
      final var welcomeDialog = new WelcomeDialog(ac);
      welcomeDialog.setLocationRelativeTo(null);
      welcomeDialog.setVisible(true);
      resultLatch.await();
      return result.get();
    };
    final var futureResult = Executors.newSingleThreadExecutor().submit(runnable);

    try{
      final var correctAction = futureResult.get();
      Assertions.assertTrue(correctAction, "Possibly wrong command set for action. See trace log.");
    } catch (ExecutionException e) {
      Assertions.fail(e);
    } catch (InterruptedException e) {
      Assertions.fail(e);
    }
    LOGGER.info("Test: WelcomeDialog Close_Application terminated.");
  }

  @Test
  public
  void testWelcomeDialogAccountCreation(){
    LOGGER.info("Test: WelcomeDialog Account_Creation started.");
    LOGGER.trace("Command expected: {}", ACCOUNT_CREATION);

    Callable<Boolean> runnable = () -> {
      CountDownLatch resultLatch= new CountDownLatch(1);
      final var result = new AtomicReference<Boolean>();
      final var ac = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e) {
          var commandString = e.getActionCommand().trim().toUpperCase();
          LOGGER.trace("Command received: {}", commandString);
          result.set(NavigationGoal.valueOf(commandString).equals(ACCOUNT_CREATION));
          resultLatch.countDown();
        }
      };
      final var welcomeDialog = new WelcomeDialog(ac);
      welcomeDialog.setLocationRelativeTo(null);
      welcomeDialog.setVisible(true);
      resultLatch.await();
      return result.get();
    };
    final var futureResult = Executors.newSingleThreadExecutor().submit(runnable);

    try{
      final var correctAction = futureResult.get();
      Assertions.assertTrue(correctAction, "Possibly wrong command set for action. See trace log.");
    } catch (ExecutionException e) {
      Assertions.fail(e);
    } catch (InterruptedException e) {
      Assertions.fail(e);
    }
    LOGGER.info("Test: WelcomeDialog Account_Creation terminated.");
  }

  @Test
  public
  void testAccountCreationDialog(){
    var accountCreationPanel = new AccountCreationPanel();
    String[] options = {"Create", "Cancel"};
    Assertions.assertInstanceOf(Integer.class, JOptionPane.showOptionDialog(null, accountCreationPanel,
        "Account Creation", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
        options, options[0]));
  }

  @Test
  public
  void testLoginDialog(){
    var loginPanel = new LoginPanel();
    final String[] options = {"Login", "Cancel"};
    Assertions.assertInstanceOf(Integer.class, JOptionPane.showOptionDialog(null, loginPanel, "Enter credentials",
        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]));
  }
}
