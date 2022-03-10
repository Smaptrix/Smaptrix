package GUI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.GeneralMatchers;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.TextInputControlMatchers;

import java.awt.*;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

public class AccountCreationPageTest extends ApplicationTest {

    /* ===== Tests for Account Creation Page ===== */

    private AccountCreationController controller; //Reference to controller so that CheckBox can be interacted with directly.

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(LoginApplication.class.getResource("account-create-page.fxml")));
        Parent mainNode = loader.load();
        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();
        this.controller = loader.getController();
    }

    @Before
    public void setUpClass() throws Exception {
    }

    @After
    public void afterEachTest() throws TimeoutException {
        release(new KeyCode[]{});
        release(new MouseButton[]{});
        FxToolkit.hideStage();
    }


    //Unit Test | Confirm text can be entered in username field.
    @Test
    public void enterCreateUsernameTest(){
        sleep(1000);
        clickOn("#usernameField");
        write("username");
        FxAssert.verifyThat("#usernameField", TextInputControlMatchers.hasText("username"));
    }


    //Unit Test | Confirm text can be entered in password field.
    @Test
    public void enterPasswordTest(){
        sleep(1000);
        clickOn("#passField");
        write("password");
        FxAssert.verifyThat("#passField", TextInputControlMatchers.hasText("password"));
    }


    //Unit Test | Confirm text can be entered in confirm password field.
    @Test
    public void enterConfirmPasswordTest(){
        sleep(1000);
        clickOn("#passConfirmField");
        write("password");
        FxAssert.verifyThat("#passConfirmField", TextInputControlMatchers.hasText("password"));
    }

    //Unit Test | Confirm checkbox can be clicked.

    //There is an issue with JavaFX(?) where only the upper half of the checkbox can be clicked. This means
    //that TestFX's robot, which clicks the dead centre, does not cause the box to be registered as clicked.
    //This was an attempt at clicking, on screen, where the box should be but it only works for AC's specific
    //screen size/resolution.
    //A better workaround could possibly be investigated but it is not a priority right now. Otherwise, simply
    //manually test with a user that the box can be clicked.
    //TODO: Currently only works on AC's screen size/resolution...
    //TODO: The relative checkbox position is not the same as when running the full integration tests with the main screen. Investigate why.
    @Test
    public void clickCheckboxTest(){
        sleep(1000);
        //clickOn("#ageCheckBox");
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        clickOn((screensize.getWidth()/2 - 92),(screensize.getHeight()/2-125));
        Assert.assertTrue(controller.ageCheckBox.isSelected());
    }

    //Unit Test | Confirm "Create Account" Button can be pressed.
    @Test
    public void createAccountButtonTest(){
        sleep(1000);
        clickOn("#createAccountButton");
        FxAssert.verifyThat("#errField", LabeledMatchers.hasText("You have not entered a username!"));
    }

    //Unit Test | Confirm Privacy Policy/T&C links work.
    //StackOverflow says this isn't possible to test.
    //https://stackoverflow.com/questions/13085791/possible-to-check-if-a-website-is-open-in-browser-from-java?rq=1

}
