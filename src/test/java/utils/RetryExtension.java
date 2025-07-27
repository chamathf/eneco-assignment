package utils;

import org.junit.jupiter.api.extension.*;
import java.lang.reflect.Method;

public class RetryExtension implements TestExecutionExceptionHandler, BeforeTestExecutionCallback {

    private static final int MAX_RETRIES = 2;
    private static final long RETRY_DELAY_MS = 1000;

    private int retryCount = 0;

    //This Method use for handle Retry Options
    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        if (retryCount < MAX_RETRIES) {
            retryCount++;
            System.out.println("Retry " + retryCount + " for test: " + context.getDisplayName());

            Thread.sleep(RETRY_DELAY_MS);


            Object testInstance = context.getRequiredTestInstance();
            Method testMethod = context.getRequiredTestMethod();
            try {
                testMethod.invoke(testInstance);
            } catch (Throwable t) {

                handleTestExecutionException(context, t.getCause());
            }
        } else {
            throw throwable;
        }
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        retryCount = 0;
    }
}
