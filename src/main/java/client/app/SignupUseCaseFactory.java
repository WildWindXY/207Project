package client.app;

import client.data_access.password_checker.PasswordCheckerDataAccess;
import client.entity.CommonUserFactory;
import client.entity.UserFactory;
import client.interface_adapter.*;
import client.interface_adapter.Main.MainViewModel;
import client.interface_adapter.Signup.SignupController;
import client.interface_adapter.Signup.SignupPresenter;
import client.interface_adapter.Signup.SignupViewModel;
import client.use_case.PasswordChecker.PasswordCheckerInputBoundary;
import client.use_case.PasswordChecker.PasswordCheckerInteractor;
import client.use_case.Signup.SignupDataAccessInterface;
import client.use_case.Signup.SignupInputBoundary;
import client.use_case.Signup.SignupInteractor;
import client.use_case.Signup.SignupOutputBoundary;
import client.use_case.options.OptionsDataAccessInterface;
import client.view.SignupView;

import javax.swing.*;
import java.io.IOException;

public class SignupUseCaseFactory {

    /** Prevent instantiation. */
    private SignupUseCaseFactory() {}

    public static SignupView create(
            ViewManagerModel viewManagerModel,
            LoginViewModel loginViewModel,
            SignupViewModel signupViewModel,
            SignupDataAccessInterface userDataAccessObject,
            OptionsDataAccessInterface optionsDataAccessObject) {

        try {
            SignupController signupController = createUserSignupUseCase(viewManagerModel, signupViewModel, loginViewModel, userDataAccessObject);
            return new SignupView(signupController, signupViewModel, optionsDataAccessObject.get(OptionsDataAccessInterface.HIGH_CONTRAST));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not open user data file.");
        }

        return null;
    }

    private static SignupController createUserSignupUseCase(ViewManagerModel viewManagerModel, SignupViewModel signupViewModel, MainViewModel mainViewModel, SignupDataAccessInterface userDataAccessObject) throws IOException {

        // Notice how we pass this method's parameters to the Presenter.
        SignupOutputBoundary signupOutputBoundary = new SignupPresenter(viewManagerModel, signupViewModel, mainViewModel);

        UserFactory userFactory = new CommonUserFactory();

        SignupInputBoundary userSignupInteractor = new SignupInteractor(
                userDataAccessObject, signupOutputBoundary, userFactory);

        PasswordCheckerInputBoundary passwordCheckerUseCaseInteractor = new PasswordCheckerInteractor(new PasswordCheckerDataAccess());
        return new SignupController(userSignupInteractor, passwordCheckerUseCaseInteractor);
    }
}
