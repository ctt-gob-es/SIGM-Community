/**
 * LICENCIA LGPL:
 * 
 * Esta librería es Software Libre; Usted puede redistribuirla y/o modificarla
 * bajo los términos de la GNU Lesser General Public License (LGPL) tal y como 
 * ha sido publicada por la Free Software Foundation; o bien la versión 2.1 de 
 * la Licencia, o (a su elección) cualquier versión posterior.
 * 
 * Esta librería se distribuye con la esperanza de que sea útil, pero SIN 
 * NINGUNA GARANTÍA; tampoco las implícitas garantías de MERCANTILIDAD o 
 * ADECUACIÓN A UN PROPÓSITO PARTICULAR. Consulte la GNU Lesser General Public 
 * License (LGPL) para más detalles
 * 
 * Usted debe recibir una copia de la GNU Lesser General Public License (LGPL) 
 * junto con esta librería; si no es así, escriba a la Free Software Foundation 
 * Inc. 51 Franklin Street, 5º Piso, Boston, MA 02110-1301, USA o consulte
 * <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 Agencia de Tecnología y Certificación Electrónica
 */
package es.accv.arangi.base.device.util;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.ConfirmationCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public class CustomDialogCallbackHandler  implements CallbackHandler {

	/* -- Fields -- */

    /* The parent window, or null if using the default parent */
    private Component parentComponent;
    private String customText = null;
    private String customTitle = null;
    private static final int JPasswordFieldLen = 8 ;

    /* -- Methods -- */

    /**
     * Creates a callback dialog with the default parent window.
     */
    public CustomDialogCallbackHandler() { }
    
    /**
     * Creates a callback dialog with the default parent window and a custom text dialog.
     */
    public CustomDialogCallbackHandler(String customText, String customTitle) { 
    	this.customText = customText;
    	this.customTitle = customTitle;
    }

    /**
     * Creates a callback dialog and specify the parent window.
     *
     * @param parentComponent the parent window -- specify <code>null</code>
     * for the default parent
     */
    public CustomDialogCallbackHandler(Component parentComponent) {
        this.parentComponent = parentComponent;
    }

    /**
     * Creates a callback dialog and specify the parent window and a custom text.
     *
     * @param parentComponent the parent window -- specify <code>null</code>
     * for the default parent
     */
    public CustomDialogCallbackHandler(Component parentComponent, String customText, String customTitle) {
        this.parentComponent = parentComponent;
        this.customText = customText;
        this.customTitle = customTitle;
    }
    
    /*
     * An interface for recording actions to carry out if the user
     * clicks OK for the dialog.
     */
    private static interface Action {
         void perform();
    }

    /**
     * Handles the specified set of callbacks.
     *
     * @param callbacks the callbacks to handle
     * @throws UnsupportedCallbackException if the callback is not an
     * instance  of NameCallback or PasswordCallback
     */

    public void handle(Callback[] callbacks)
        throws UnsupportedCallbackException
    {
        /* Collect messages to display in the dialog */
        final List<Object> messages = new ArrayList<Object>(3);

        /* Collection actions to perform if the user clicks OK */
        final List<Action> okActions = new ArrayList<Action>(2);

        ConfirmationInfo confirmation = new ConfirmationInfo();

        for (int i = 0; i < callbacks.length; i++) {
            if (callbacks[i] instanceof PasswordCallback) {
                final PasswordCallback pc = (PasswordCallback) callbacks[i];

                JLabel prompt = new JLabel();
                if (customText != null) {
                	prompt.setText(customText);
				} else {
					prompt.setText(pc.getPrompt());					
				}

                final JPasswordField password =
                                        new JPasswordField(JPasswordFieldLen);
                if (!pc.isEchoOn()) {
                    password.setEchoChar('*');
                }

                Box passwordPanel = Box.createHorizontalBox();
                passwordPanel.add(prompt);
                passwordPanel.add(password);
                messages.add(passwordPanel);

                okActions.add(new Action() {
                    public void perform() {
                        pc.setPassword(password.getPassword());
                    }
                });

            } else {
                throw new UnsupportedCallbackException(
                    callbacks[i], "Unrecognized Callback");
            }
        }

        String title = customTitle != null ? customTitle : "Confirmation";
        /* Display the dialog */
        int result = JOptionPane.showOptionDialog(
            parentComponent,
            messages.toArray(),
            title,                     /* title */
            confirmation.optionType,
            confirmation.messageType,
            null,                               /* icon */
            confirmation.options,               /* options */
            confirmation.initialValue);         /* initialValue */

        /* Perform the OK actions */
        if (result == JOptionPane.OK_OPTION
            || result == JOptionPane.YES_OPTION)
        {
            Iterator<Action> iterator = okActions.iterator();
            while (iterator.hasNext()) {
                iterator.next().perform();
            }
        }
        confirmation.handleResult(result);
    }

    /*
     * Provides assistance with translating between JAAS and Swing
     * confirmation dialogs.
     */
    private static class ConfirmationInfo {

        private int[] translations;

        int optionType = JOptionPane.OK_CANCEL_OPTION;
        Object[] options = null;
        Object initialValue = null;

        int messageType = JOptionPane.QUESTION_MESSAGE;

        private ConfirmationCallback callback;

        /* Set the confirmation callback handler */
        void setCallback(ConfirmationCallback callback)
            throws UnsupportedCallbackException
        {
            this.callback = callback;

            int confirmationOptionType = callback.getOptionType();
            switch (confirmationOptionType) {
            case ConfirmationCallback.YES_NO_OPTION:
                optionType = JOptionPane.YES_NO_OPTION;
                translations = new int[] {
                    JOptionPane.YES_OPTION, ConfirmationCallback.YES,
                    JOptionPane.NO_OPTION, ConfirmationCallback.NO,
                    JOptionPane.CLOSED_OPTION, ConfirmationCallback.NO
                };
                break;
            case ConfirmationCallback.YES_NO_CANCEL_OPTION:
                optionType = JOptionPane.YES_NO_CANCEL_OPTION;
                translations = new int[] {
                    JOptionPane.YES_OPTION, ConfirmationCallback.YES,
                    JOptionPane.NO_OPTION, ConfirmationCallback.NO,
                    JOptionPane.CANCEL_OPTION, ConfirmationCallback.CANCEL,
                    JOptionPane.CLOSED_OPTION, ConfirmationCallback.CANCEL
                };
                break;
            case ConfirmationCallback.OK_CANCEL_OPTION:
                optionType = JOptionPane.OK_CANCEL_OPTION;
                translations = new int[] {
                    JOptionPane.OK_OPTION, ConfirmationCallback.OK,
                    JOptionPane.CANCEL_OPTION, ConfirmationCallback.CANCEL,
                    JOptionPane.CLOSED_OPTION, ConfirmationCallback.CANCEL
                };
                break;
            case ConfirmationCallback.UNSPECIFIED_OPTION:
                options = callback.getOptions();
                /*
                 * There's no way to know if the default option means
                 * to cancel the login, but there isn't a better way
                 * to guess this.
                 */
                translations = new int[] {
                    JOptionPane.CLOSED_OPTION, callback.getDefaultOption()
                };
                break;
            default:
                throw new UnsupportedCallbackException(
                    callback,
                    "Unrecognized option type: " + confirmationOptionType);
            }

            int confirmationMessageType = callback.getMessageType();
            switch (confirmationMessageType) {
            case ConfirmationCallback.WARNING:
                messageType = JOptionPane.WARNING_MESSAGE;
                break;
            case ConfirmationCallback.ERROR:
                messageType = JOptionPane.ERROR_MESSAGE;
                break;
            case ConfirmationCallback.INFORMATION:
                messageType = JOptionPane.INFORMATION_MESSAGE;
                break;
            default:
                throw new UnsupportedCallbackException(
                    callback,
                    "Unrecognized message type: " + confirmationMessageType);
            }
        }


        /* Process the result returned by the Swing dialog */
        void handleResult(int result) {
            if (callback == null) {
                return;
            }

            for (int i = 0; i < translations.length; i += 2) {
                if (translations[i] == result) {
                    result = translations[i + 1];
                    break;
                }
            }
            callback.setSelectedIndex(result);
        }
    }

}
