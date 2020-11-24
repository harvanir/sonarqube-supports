package org.harvanir.sonarqube.share.presenter;

/**
 * @author Harvan Irsyadi
 */
public abstract class ImmutablePresenter<T> {

    private boolean isExecuted = false;

    public final void present(T response) {
        if (!isExecuted) {
            isExecuted = true;

            presentInternal(response);
        } else {
            throw new HasExecutedException("Presenter has presented.");
        }
    }

    protected abstract void presentInternal(T response);

    static class HasExecutedException extends RuntimeException {

        private static final long serialVersionUID = -6621406211309367446L;

        public HasExecutedException(String message) {
            super(message);
        }
    }
}